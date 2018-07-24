package com.dc.appengine.appmaster.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.IOUtils;
import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.appmaster.constants.Constants;


public class FileUtil {
    private static boolean found = false;
    private static final int buffer = 2048;
    
	public static String[] binaryFileLists = {"jar","zip","rar","war","jpg","png","gif","xlsx","class","rpm"};

	public List<String> fileList = new ArrayList<String>();
	public List<String> traverseFolder2(String path) {
		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				System.out.println("文件夹是空的!");
				return null;
			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
						traverseFolder2(file2.getAbsolutePath());
					} else {
						fileList.add(file2.getAbsolutePath());


					}
				}
			}
		} else {
			System.out.println("文件不存在!");
		}
		return fileList;
	}
	
	public static void deleteAllFilesOfDir(File path) {  
	    if (!path.exists())  
	        return;  
	    if (path.isFile()) {  
	        path.delete();  
	        return;  
	    }  
	    File[] files = path.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        deleteAllFilesOfDir(files[i]);  
	    }  
	    path.delete();  
	}  
	
    /**
     * 传入一个文件(File)对象，检查文件编码
     * 
     * @param file
     *            File对象实例
     * @return 文件编码，若无，则返回null
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<String> guessFileEncoding(File file) throws FileNotFoundException, IOException {
        return guessFileEncoding(file, new nsDetector());
    }

    /**
     * <pre>
     * 获取文件的编码
     * @param file
     *            File对象实例
     * @param languageHint
     *            语言提示区域代码 @see #nsPSMDetector ,取值如下：
     *             1 : Japanese
     *             2 : Chinese
     *             3 : Simplified Chinese
     *             4 : Traditional Chinese
     *             5 : Korean
     *             6 : Dont know(default)
     * </pre>
     * 
     * @return 文件编码，eg：UTF-8,GBK,GB2312形式(不确定的时候，返回可能的字符编码序列)；若无，则返回null
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<String> guessFileEncoding(File file, int languageHint) throws FileNotFoundException, IOException {
        return guessFileEncoding(file, new nsDetector(languageHint));
    }

    /**
     * 获取文件的编码
     * 
     * @param file
     * @param det
     * @return [UTF-8, GB18030, UTF-16BE, UTF-16LE]
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static List<String> guessFileEncoding(File file, nsDetector det) throws FileNotFoundException, IOException {
        // Set an observer...
        // The Notify() will be called when a matching charset is found.
    	List<String> encoding = new ArrayList();
        det.Init(new nsICharsetDetectionObserver() {
            public void Notify(String charset) {
                encoding.add(charset);
                found = true;
            }
        });

        BufferedInputStream imp = new BufferedInputStream(new FileInputStream(file));
        byte[] buf = new byte[1024];
        int len;
        boolean done = false;
        boolean isAscii = false;

        while ((len = imp.read(buf, 0, buf.length)) != -1) {
            // Check if the stream is only ascii.
            isAscii = det.isAscii(buf, len);
            if (isAscii) {
                break;
            }
            // DoIt if non-ascii and not done yet.
            done = det.DoIt(buf, len, false);
            if (done) {
                break;
            }
        }
        imp.close();
        det.DataEnd();

        if (isAscii) {
        	encoding.add("ASCII");
            found = true;
        }

        if (!found) {
            String[] prob = det.getProbableCharsets();
            //这里将可能的字符集组合起来返回
            for (int i = 0; i < prob.length; i++) {
                	encoding.add(prob[i]);
            }

            if (prob.length > 0) {
                // 在没有发现情况下,也可以只取第一个可能的编码,这里返回的是一个可能的序列
                return encoding;
            } else {
                return null;
            }
        }
        return encoding;
    }
	public static void main(String[] argv) throws Exception {
		File path = new File("C:\\Users\\paas\\Desktop\\file");
		new FileUtil().ss(path);

	}
	public void ss(File files){
		File[] file = files.listFiles();
		String encoding = "";
		System.out.println("文件名     "+"		--------"+"文件编码:");
		for (File file1 : file) {
			if (file1.isDirectory()) {
				ss(file1);
			} else {
				try {
					System.out.println(file1.getName()+" -------- " + new FileUtil().guessFileEncoding(file1));
					
					try {
						StringBuilder builder = null;
						List<String> encodings = FileUtil.guessFileEncoding(file1);
						if (encodings.size()>0) {
							encoding = encodings.get(0);
						}else{
							encoding = "UTF-8";
						}
						InputStream ins = new FileInputStream(file1.getAbsolutePath());
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(ins, encoding));
						//		byte[] b = new byte[1024];
						//		try (InputStream in = ins) {
						//			IOUtils.read(in, b);
						//			
						//		}
						String line;
						builder = new StringBuilder(150);
						while ((line = reader.readLine()) != null) {
							builder.append(line+"\n");
						}
						reader.close();
						//	   System.out.println(builder);
						if (ins != null) {
							ins.close();
						}
//						fileText = IOUtils.toString(new FileInputStream(filePath));
						String fileText = builder.toString();
						
						System.out.println(fileText);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		}
	
	
  
	  public static Map<String,Object> parsePackage(String file,String packageType){
			Map<String,Object> record = new HashMap<>();
			try (ZipFile zip = new ZipFile(file)) {
				List<Map<String,Object>> templateList = new ArrayList<>();
				List<Map<String,Object>> resourceList = new ArrayList<>();
				List<Map<String,Object>> workpieceList = new ArrayList<>();
				Enumeration<ZipArchiveEntry> zipArchives = zip.getEntries();
				while(zipArchives.hasMoreElements()) {
					ZipArchiveEntry zae = zipArchives.nextElement();
					String name = zae.getName();
					if(Constants.Package.WORKPIECE.equals(packageType)){
						if(name.equals(Constants.Package.WORKPIECEJSON)){
							String packageString = org.apache.commons.io.IOUtils.toString(zip.getInputStream(zae),"UTF-8");
	    					workpieceList = JSON.parseObject(packageString, new TypeReference<List<Map<String,Object>>>(){});
	    					break;
						}
					}else{
						Map<String,Object> map =null;
						if(name.contains(Constants.Package.WORKPIECEJSON)||name.contains(Constants.Package.COMPONETJSON)||
								name.contains(Constants.Package.BLUEPRINTJSON)){
							String packageString = org.apache.commons.io.IOUtils.toString(zip.getInputStream(zae),"UTF-8");
							map = JSON.parseObject(packageString, new TypeReference<Map<String,Object>>(){});
							if(name.contains(Constants.Package.WORKPIECEJSON)){
								workpieceList.add(map);
							}else if(name.contains(Constants.Package.COMPONETJSON)){
								resourceList.add(map);
							}else if(name.contains(Constants.Package.BLUEPRINTJSON)){
								templateList.add(map);
							}
						}
					}
				}
				record.put(Constants.Package.WORKPIECE,workpieceList);
				record.put(Constants.Package.COMPONET,resourceList);
				record.put(Constants.Package.BLUEPRINT,templateList);
				return record;
			}catch (Exception e){
				e.printStackTrace();
			}
			return record;
		}
}
