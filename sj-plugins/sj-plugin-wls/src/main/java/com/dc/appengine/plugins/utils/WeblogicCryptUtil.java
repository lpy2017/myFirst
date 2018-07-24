package com.dc.appengine.plugins.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import com.rsa.jsafe.CryptoJ;
import com.rsa.jsafe.FIPS140Context;
import com.rsa.jsafe.JSAFE_InvalidUseException;
import com.rsa.jsafe.JSAFE_SecretKey;
import com.rsa.jsafe.JSAFE_SecureRandom;
import com.rsa.jsafe.JSAFE_SymmetricCipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class WeblogicCryptUtil {
	private static Map<String, WeblogicAES> wlsAESMap = new HashMap<String, WeblogicAES>();

	public static WeblogicAES getAESInstance(String wlsDomainPath) {
		if (wlsAESMap.get(wlsDomainPath) == null) {
			synchronized (WeblogicCryptUtil.class) {
				if (wlsAESMap.get(wlsDomainPath) == null) {
					WeblogicAES instance = new WeblogicAES(wlsDomainPath);
					wlsAESMap.put(wlsDomainPath, instance);
				}
			}
		}
		return wlsAESMap.get(wlsDomainPath);
	}
	
	public static boolean testDBConnection(String user, String password, String url, String driver, String sql) {
		boolean isConnected = false;
		try {
			Class.forName(driver);
			Connection connection = DriverManager.getConnection(url, user, password);
			System.out.println("数据库连接测试成功！");
			// PreparedStatement statement = connection.prepareStatement(sql);
			// ResultSet result = statement.executeQuery();
			// if(result != null){
			// isConnected = true;
			// }
			// result.close();
			// statement.close();
			isConnected = true;
			connection.close();
		} catch (Exception e) {
			System.out.println("数据库连接测试失败！");
			e.printStackTrace();
		}
		return isConnected;
	}

	public static boolean verifyUsrPwdAES(String wlsDomainPath, String wlsUsr, String wlsPwd) throws Exception {
		return getAESInstance(wlsDomainPath).verifyWlsUsrPwd(wlsUsr, wlsPwd);
	}

	public static String decryptAES(String wlsDomainPath, String cipherText) throws Exception {
		return getAESInstance(wlsDomainPath).decryptAES(cipherText);
	}

	public static String encryptAES(String wlsDomainPath, String clearText) throws Exception {
		return getAESInstance(wlsDomainPath).encryptAES(clearText);
	}

}

class WeblogicAES {
	private static final String prefix = "{AES}";
	private static final String algorithm = "AES/CBC/PKCS5Padding";
	private static final String key = "0xccb97558940b82637c8bec3c770f86fa3a391a56";
	private static final int randomLen = 16;
	private String usernameCipher = null;
	private String passwordCipher = null;
	private byte[] salt = null;
	private byte[] encryptionKey = null;
	private byte[] encryptionAESKey = null;
	private final Object seedingLock = new Object();
	private JSAFE_SecureRandom seedingRandom = null;
	private FIPS140Context NON_FIPS140_CONTEXT;

	WeblogicAES(String wlsDomainPath) {
		reaBootProperties(wlsDomainPath);
		readSystemIni(wlsDomainPath);
	}

	boolean verifyWlsUsrPwd(String wlsUsr, String wlsPwd) throws Exception {
		String usernameClear = decryptAES(usernameCipher);
		String passwordClear = decryptAES(passwordCipher);
		if (wlsUsr.equals(usernameClear) && wlsPwd.equals(passwordClear)) {
			return true;
		} else {
			return false;
		}
	}

	String decryptAES(String cipherText) throws Exception {
		char password[] = new char[key.length()];
		key.getChars(0, password.length, password, 0);

		JSAFE_SecretKey secretKey = null;
		JSAFE_SymmetricCipher cipher = null;
		byte[] decryptedData = null;
		try {
			char[] copyPassword = new char[password.length];
			System.arraycopy(password, 0, copyPassword, 0, password.length);
			secretKey = decryptSecretKey(algorithm, encryptionAESKey, copyPassword, salt);

			cipher = JSAFE_SymmetricCipher.getInstance(algorithm, "Java");
			// salt = null;
			// if (salt != null) {
			// cipher.setIV(salt, 0, salt.length);
			// }
			cipher.decryptInit(secretKey);

			byte[] encrypted = new BASE64Decoder().decodeBuffer(cipherText);
			System.out.println("starting decrypt operation ");
			int ivLen = randomLen;
			int cipherLen = encrypted.length - ivLen;
			if (cipherLen < 0) {
				throw new IllegalStateException("Invalid input length");
			}
			decryptedData = new byte[cipherLen];
			if (ivLen > 0) {
				cipher.setIV(encrypted, 0, ivLen);
				cipher.decryptReInit();
			}
			int partOut = cipher.decryptUpdate(encrypted, ivLen, cipherLen, decryptedData, 0);
			int finalOut = cipher.decryptFinal(decryptedData, partOut);
			int totalOut = partOut + finalOut;
			byte[] actualData;
			if (totalOut < decryptedData.length) {
				actualData = new byte[totalOut];
				System.arraycopy(decryptedData, 0, actualData, 0, totalOut);
				decryptedData = actualData;
			}
			cipher.clearSensitiveData();
			String clearText = new String(decryptedData, "UTF-8");
			System.out.println("解密后明文=" + clearText);
			System.out.println("done with decrypt operation ");
			return clearText;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	String encryptAES(String clearText) throws Exception {
		char password[] = new char[key.length()];
		key.getChars(0, password.length, password, 0);

		JSAFE_SecretKey secretKey = null;
		JSAFE_SymmetricCipher cipher = null;
		try {
			char[] copyPassword = new char[password.length];
			System.arraycopy(password, 0, copyPassword, 0, password.length);
			secretKey = decryptSecretKey(algorithm, encryptionAESKey, copyPassword, salt);

			cipher = JSAFE_SymmetricCipher.getInstance(algorithm, "Java");
			// salt = null;
			// if (salt != null) {
			// cipher.setIV(salt, 0, salt.length);
			// }
			cipher.encryptInit(secretKey);

			byte[] cipherBytes = null;
			byte[] data = clearText.getBytes("UTF-8");
			System.out.println("starting encrypt");
			int ivLen = randomLen;
			cipherBytes = new byte[ivLen + cipher.getOutputBufferSize(data.length)];
			if (ivLen > 0) {
				JSAFE_SecureRandom newRandom = (JSAFE_SecureRandom) JSAFE_SecureRandom.getInstance("FIPS186Random",
						"Java");
				byte[] seedBytes = null;
				synchronized (seedingLock) {
					if (seedingRandom == null) {
						seedingRandom = (JSAFE_SecureRandom) JSAFE_SecureRandom.getInstance("FIPS186Random", "Java");
						seedingRandom.autoseed();
					}
					seedBytes = seedingRandom.generateRandomBytes(32);
				}
				newRandom.setSeed(seedBytes);
				newRandom.generateRandomBytes(cipherBytes, 0, ivLen);
				cipher.setIV(cipherBytes, 0, ivLen);
				cipher.encryptReInit();
			}
			int partOut1 = cipher.encryptUpdate(data, 0, data.length, cipherBytes, ivLen);
			int finalOut1 = cipher.encryptFinal(cipherBytes, partOut1 + ivLen);

			cipher.clearSensitiveData();
			String encodedEncrypted = new BASE64Encoder().encodeBuffer(cipherBytes);
			System.out.println("加密后密码=" + prefix + encodedEncrypted);
			System.out.println("done with encrypt operation ");
			return prefix + encodedEncrypted;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private void reaBootProperties(String wlsDomainPath) {
		String boot = wlsDomainPath + File.separator + "servers" + File.separator + "AdminServer" + File.separator
				+ "security" + File.separator + "boot.properties";
		File file = new File(boot);
		if (!file.exists()) {
			System.out.println("weblogic登录用户文件[" + file.getAbsolutePath() + "]不存在！");
		}
		try {
			BufferedReader bd = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = bd.readLine()) != null) {
				if (line.startsWith("username")) {
					usernameCipher = line.substring("username".length() + prefix.length() + 1);
				}
				if (line.startsWith("password")) {
					passwordCipher = line.substring("password".length() + prefix.length() + 1);
				}
			}
			if (bd != null) {
				bd.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readSystemIni(String wlsDomainPath) {
		String serializedSystemIni = wlsDomainPath + File.separator + "security" + File.separator
				+ "SerializedSystemIni.dat";
		try {
			FileInputStream is = new FileInputStream(serializedSystemIni);
			salt = readBytes(is);
			int version = is.read();
			if (version != -1) {
				encryptionKey = readBytes(is);
				if (version == 2) {
					encryptionAESKey = readBytes(is);
				}
			}
			if (is != null) {
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private JSAFE_SecretKey decryptSecretKey(String algorithm, byte[] encryptedKey, char[] password, byte[] salt)
			throws Exception {
		JSAFE_SecretKey secretKey = null;
		JSAFE_SecretKey returnKey = null;
		JSAFE_SymmetricCipher cipher = null;
		try {
			byte[] keyMaterial = new byte[encryptedKey.length];
			// System.out.println("key material length: " + keyMaterial.length);
			byte[] expandedSalt = doubleSalt(salt);
			cipher = JSAFE_SymmetricCipher.getInstance("PBE/SHA1/RC2/CBC/PKCS12PBE-5-128", "Java");
			if (getNonFIPS140Ctx() == null) {
				cipher = JSAFE_SymmetricCipher.getInstance("PBE/SHA1/RC2/CBC/PKCS12PBE-5-128", "Java");
			} else {
				cipher = JSAFE_SymmetricCipher.getInstance("PBE/SHA1/RC2/CBC/PKCS12PBE-5-128", "Java",
						getNonFIPS140Ctx());
			}
			cipher.setSalt(salt, 0, salt.length);
			secretKey = cipher.getBlankKey();
			secretKey.setPassword(password, 0, password.length);
			cipher.decryptInit(secretKey);

			int partOut = cipher.decryptUpdate(encryptedKey, 0, encryptedKey.length, keyMaterial, 0);
			int finalOut = cipher.decryptFinal(keyMaterial, partOut);
			int totalOut = partOut + finalOut;
			// System.out.println(totalOut + " bytes of the array filled");

			byte[] actualMaterial = new byte[totalOut];
			System.arraycopy(keyMaterial, 0, actualMaterial, 0, actualMaterial.length);
			// System.out.println("getting cipher to generate key");

			cipher = JSAFE_SymmetricCipher.getInstance(algorithm, "Java");
			// System.out.println("blank key from cipher");
			returnKey = cipher.getBlankKey();
			// System.out.println("setting key data: " + actualMaterial.length);
			returnKey.setSecretKeyData(actualMaterial, 0, actualMaterial.length);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (secretKey != null) {
				secretKey.clearSensitiveData();
			}
			if (cipher != null) {
				cipher.clearSensitiveData();
			}
			for (int i = 0; i < password.length; i++) {
				password[i] = '\000';
			}
		}
		return returnKey;
	}

	private FIPS140Context getNonFIPS140Ctx() throws JSAFE_InvalidUseException {
		if ((CryptoJ.isFIPS140Compliant()) && (NON_FIPS140_CONTEXT == null)) {
			try {
				FIPS140Context fips140Ctx = CryptoJ.getFIPS140Context();
				NON_FIPS140_CONTEXT = fips140Ctx.setMode(1);
			} catch (JSAFE_InvalidUseException jiue) {
				throw jiue;
			}
		}
		return NON_FIPS140_CONTEXT;
	}

	private byte[] doubleSalt(byte[] salt) {
		if (salt.length == 8) {
			return salt;
		}
		byte[] doubleSalt = new byte[8];

		System.arraycopy(salt, 0, doubleSalt, 0, 4);
		System.arraycopy(salt, 0, doubleSalt, 4, 4);
		return doubleSalt;
	}

	private byte[] readBytes(InputStream is) throws IOException {
		int len = is.read();
		if (len < 0) {
			throw new IOException("SerializedSystemIni is empty");
		}
		byte[] bytes = new byte[len];
		int readin = 0;
		int justread = 0;
		while (readin < len) {
			justread = is.read(bytes, readin, len - readin);
			if (justread == -1)
				break;
			readin += justread;
		}
		return bytes;
	}

}
