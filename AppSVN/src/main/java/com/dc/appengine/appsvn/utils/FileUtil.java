package com.dc.appengine.appsvn.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipFile;

import com.dc.appengine.appsvn.upload.MUploadWebService;

/**
 * 文件操作帮助类
 * @author yanglei
 *
 */
public class FileUtil {
	
	private static List<String> fileList = new ArrayList<String>();
	/**
	 * 创建文件
	 * @param pathStr 路径
	 * @param fileName 文件名字
	 * @return File实例
	 */
	public static File createFile(String pathStr,String fileName){
		File path=new File(pathStr);
		if(!path.exists()){
			path.mkdirs();
		}
		File file=new File(pathStr+"/"+fileName);
		if(file.exists()){
			int dotIndex=fileName.lastIndexOf(".");
			if(dotIndex!=-1){
				file=new File(pathStr + "/" + fileName.substring(0,dotIndex) + System.currentTimeMillis()
						+ fileName.substring(dotIndex, fileName.length()));
			}else{
				file=new File(pathStr + "/" + fileName + System.currentTimeMillis());
			}			
		}
		return file;
	}
	
	public static void delFile(File file){
		if(file.exists()){
			if(file.isFile()){
				file.delete();
			} else {
				File[] files = file.listFiles();
				for(File f:files){
					delFile(f);
				}
				file.delete();
			}
		}
	}
	
	public static void delFile(String path){
		delFile(new File(path));
	}
	/**
	 * 创建临时目录
	 * @param tpmPathStr
	 * @return
	 */
	public static File createDirectory(String tpmPathStr){
		File path=new File(tpmPathStr);
		if(!path.exists()){
			path.mkdirs();
		}
		return path;
	}
	
	
	/**
	 * 构建文件
	 * @param tpmPathStr
	 * @return
	 */
	public static File createFile(String pathStr){
		File path=new File(pathStr);
		
		return path;
	}
	/**
	 * 删除临时目录及其下所有文件
	 * @param dir
	 * @return
	 */
	public static void delDirectory(File dir) {
		
		if(dir.isDirectory()){
			File[] delFile = dir.listFiles();
			if(delFile.length==0) {  //若目录下没有文件则直接删除
				dir.delete();
			}else {
				for(File subFile : delFile) {
					if(subFile.isDirectory()){
						delDirectory(subFile);  //递归删除 (若有子目录的话)
					}else {
						subFile.delete();
					}
				}
			}
		}
		dir.delete();
		
	}

	/**
	 * 创建zipfile
	 * @param pathStr
	 * @return
	 */
	public static ZipFile createZipFile(String pathStr){
		ZipFile file = null;
		try {
			file = new ZipFile(pathStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}
	
	
	
	
	
	/**
	 * 获取指定后缀的文件的全路径名  递归实现 废弃
	 * @param path
	 * @param matched
	 * @return
	 */
	@Deprecated
	public static List<String> getAssignDirectoryFiles1(File tmpFile,String matched) {
		//List<String> fileList = new ArrayList<String>();
		String filePath = "";
		if(!tmpFile.exists()) 
			return fileList;
		if(matched!=null&&!matched.equals("")) 
			matched = matched.toLowerCase();
		
		if(tmpFile.isDirectory()) {
			File[] childFiles = tmpFile.listFiles();
			for(File file : childFiles) {
				if(file.isDirectory()) {
					getAssignDirectoryFiles1(file,matched);    //递归
				}else {
					filePath = file.toString();
					if(matched!=null&&!matched.equals("")&&filePath.endsWith(matched)) {
						//
						fileList.add(filePath);
						
					}else {  //存储非xml文件
						fileList.add(filePath);
					}
				}
				
			}
		}else {
			filePath = tmpFile.toString();
			if(matched!=null&&!matched.equals("")&&filePath.toLowerCase().endsWith(matched)) {
				//
				fileList.add(filePath);
				
			}else {    //存储非xml文件
				fileList.add(filePath);
			}
		}
		
		return fileList;
		
	}
	
	//非递归方式实现
	public static List<String> getAssignDirectoryFiles(File tmpFile) {
		List<String> fileList1 = new ArrayList<String>();
		LinkedList<File> linkedList = new LinkedList<File>();
		String filePath = "";
		if(!tmpFile.exists()) 
			return fileList1;
		//if(matched!=null&&!matched.equals("")) 
			//matched = matched.toLowerCase();
		
		linkedList.addLast(tmpFile);  //构造一个栈
		while(linkedList.size()>0) {
			File file = linkedList.removeFirst();
			File[] childFiles = file.listFiles();
			if( childFiles != null ){
				for(File f : childFiles) {
					if(f.isDirectory()) {
						linkedList.addLast(f);
					}else {
						filePath = f.toString();
						
							fileList1.add(filePath);
					}
				}
			}
		}
		
		return fileList1;
		
	}
	
	
	/**
	 * 将指定文件从当前目录移动到目标目录下:单个文件的复制
	 * @param srcPath
	 * @param destDirPath
	 */
	public static void moveFile(String srcPath,String destDirPath) {
		
		File srcFile = FileUtil.createDirectory(srcPath);
		File destDir = FileUtil.createDirectory(destDirPath);
		
		InputStream is = null;
		OutputStream os = null;
		int resp = 0;
		
		if(srcFile.exists()&& destDir.exists()&&destDir.isDirectory()) {
			try {
				is = new FileInputStream(srcFile);
				String destFile = destDirPath+File.separator+srcFile.getName();
				File dFile = new File(destFile);
				if(dFile.exists()) {
					dFile.delete();
				}
				dFile.createNewFile();
				os = new FileOutputStream(dFile);
				byte[] buffer = new byte[1024];
				while((resp = is.read(buffer))!=-1) {
					os.write(buffer, 0, resp);      //注意此处不能用os.write(buffer),会出现多写乱码  resp表示有多少读取多少字节数
				}
				os.flush();
				os.close();
				os = null;
				
				is.close();
				is = null;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	
	
	
	//-----------------------------------------------------------------------
    /**
     * Deletes a directory recursively. 
     *
     * @param directory  directory to delete
     * @throws IOException in case deletion is unsuccessful
     */
    public static void deleteDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        cleanDirectory(directory);
        if (!directory.delete()) {
            String message =
                "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }


    /**
     * Cleans a directory without deleting it.
     *
     * @param directory directory to clean
     * @throws IOException in case cleaning is unsuccessful
     */
    public static void cleanDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        File[] files = directory.listFiles();
        if (files == null) {  // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            try {
                forceDelete(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }
    
    
    //-----------------------------------------------------------------------
    /**
     * Deletes a file. If file is a directory, delete it and all sub-directories.
     * <p>
     * The difference between File.delete() and this method are:
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>You get exceptions when a file or directory cannot be deleted.
     *      (java.io.File methods returns a boolean)</li>
     * </ul>
     *
     * @param file  file or directory to delete, must not be <code>null</code>
     * @throws NullPointerException if the directory is <code>null</code>
     * @throws FileNotFoundException if the file was not found
     * @throws IOException in case deletion is unsuccessful
     */
    public static void forceDelete(File file) throws IOException {
    	if(!file.exists())
    		return;
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            if (!file.delete()) {
                String message =
                    "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }

    /**
     * Schedules a file to be deleted when JVM exits.
     * If file is directory delete it and all sub-directories.
     *
     * @param file  file or directory to delete, must not be <code>null</code>
     * @throws NullPointerException if the file is <code>null</code>
     * @throws IOException in case deletion is unsuccessful
     */
    public static void forceDeleteOnExit(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectoryOnExit(file);
        } else {
            file.deleteOnExit();
        }
    }

    /**
     * Schedules a directory recursively for deletion on JVM exit.
     *
     * @param directory  directory to delete, must not be <code>null</code>
     * @throws NullPointerException if the directory is <code>null</code>
     * @throws IOException in case deletion is unsuccessful
     */
    private static void deleteDirectoryOnExit(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        cleanDirectoryOnExit(directory);
        directory.deleteOnExit();
    }

    /**
     * Cleans a directory without deleting it.
     *
     * @param directory  directory to clean, must not be <code>null</code>
     * @throws NullPointerException if the directory is <code>null</code>
     * @throws IOException in case cleaning is unsuccessful
     */
    private static void cleanDirectoryOnExit(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        File[] files = directory.listFiles();
        if (files == null) {  // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            try {
                forceDeleteOnExit(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }
    
    
    /**
	 * 将指定文件夹下的目录和文件从当前目录移动到目标目录下:嵌套目录结构及散放的单个文件
	 * @param srcPath
	 * @param destDirPath
	 */
	public static void fromSrcFolderToDestFolder(String srcPath,String destDirPath) {
		
		File srcFile = FileUtil.createDirectory(srcPath);
		File destDir = FileUtil.createDirectory(destDirPath);
		FileInputStream inStream =null;
		FileOutputStream outStream = null;
		if(!destDir.exists())
			destDir.mkdirs();
		
		File temp = null;
		String[] fileLists = srcFile.list();
		for(String file : fileLists) {
			if(srcPath.endsWith(File.separator)) {
				temp = FileUtil.createFile(srcPath+file);
			}else {
				temp = FileUtil.createFile(srcPath+File.separator+file);
			}
			if(temp.isFile()) {
				if(destDirPath.endsWith(File.separator)) {
					  destDir = FileUtil.createFile(destDirPath+temp.getName());
				}else {
					destDir = FileUtil.createFile(destDirPath+File.separator+temp.getName());
				}
				  
					try {
						inStream = new FileInputStream(temp);
						outStream = new FileOutputStream(destDir);
						
						byte[] bt = new byte[1024*5];
						int len;
						while((len=inStream.read(bt))!=-1) {
							outStream.write(bt,0,len);
						}
						
						outStream.flush();
						outStream.close();
						inStream.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				
			}else if(temp.isDirectory()) {
				fromSrcFolderToDestFolder(srcPath+File.separator+file,destDirPath+File.separator+file);
			}
			
		}	
	}
	/**
	 * 将指定文件从当前目录移动到目标目录下:单个文件的复制
	 * @param srcPath
	 * @param destDirPath
	 */
	public static String mvFile(String srcPath,String destDirPath) {
		
		File srcFile = FileUtil.createDirectory(srcPath);
		File destDir = FileUtil.createDirectory(destDirPath);
		
		InputStream is = null;
		OutputStream os = null;
		int resp = 0;
		
		if(srcFile.exists()&& destDir.exists() && destDir.isDirectory()) {
			try {
				is = new FileInputStream(srcFile);
				String destFile = destDirPath+File.separator+srcFile.getName();
				File dFile = new File(destFile);
				if( dFile.exists() ){
					destFile = destDirPath + File.separator + UUID.randomUUID().toString() + srcFile.getName();
					dFile = new File( destFile );
				}
				dFile.createNewFile();
				os = new FileOutputStream(dFile);
				byte[] buffer = new byte[1024];
				while((resp = is.read(buffer))!=-1) {
					os.write(buffer, 0, resp);      //注意此处不能用os.write(buffer),会出现多写乱码  resp表示有多少读取多少字节数
				}
				os.flush();
				return destFile;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					os.close();
				} catch (IOException e) {
					os = null;
				}
				try{
					is.close();
				} catch (IOException e) {
					is = null;
				}			
			}
		}
		return null;
	}

	public static String rename(String fileName) {
		String s = String.valueOf(Math.random());
		s = s.substring(2);
		if(fileName == null) {
			return s;
		}
		if(fileName.contains(".")) {
			int i = fileName.lastIndexOf(".");
			if(fileName.length() + s.length() + 1 > MUploadWebService.MAX_FILE_NAME_LEN) {
				i = MUploadWebService.MAX_FILE_NAME_LEN - (s.length() + 1) - (fileName.length() - i);
			}
			return fileName.substring(0, i) + "_" + s + fileName.substring(i);
		}
		return fileName + "_" + s;
	}

	public static String renameContainer(String fileName) {
		String s = UUID.randomUUID().toString();
		s = s.substring(2);
		if(fileName == null) {
			return s;
		}
		if(fileName.contains(".")) {
			int i = fileName.lastIndexOf(".");
			if(fileName.length() + s.length() + 1 > MUploadWebService.MAX_FILE_NAME_LEN) {
				i = MUploadWebService.MAX_FILE_NAME_LEN - (s.length() + 1) - (fileName.length() - i);
			}
			return fileName.substring(0, i) + "_" + s + fileName.substring(i);
		}
		return fileName + "_" + s;
	}

	public static void moveFile(String srcPath, String destDirPath,
			String remoteName) {
		File srcFile = FileUtil.createDirectory(srcPath);
		File destDir = FileUtil.createDirectory(destDirPath);
		
		InputStream is = null;
		OutputStream os = null;
		int resp = 0;
		
		if(srcFile.exists()&& destDir.exists()&&destDir.isDirectory()) {
			try {
				is = new FileInputStream(srcFile);
				String destFile = destDirPath+File.separator+remoteName;
				File dFile = new File(destFile);
				if(dFile.exists()) {
					dFile.delete();
				}
				dFile.createNewFile();
				os = new FileOutputStream(dFile);
				byte[] buffer = new byte[1024];
				while((resp = is.read(buffer))!=-1) {
					os.write(buffer, 0, resp);      //注意此处不能用os.write(buffer),会出现多写乱码  resp表示有多少读取多少字节数
				}
				os.flush();
				os.close();
				os = null;
				
				is.close();
				is = null;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static boolean saveFile(InputStream is, String dir) {
		try {
			File file = new File(dir);
			if(!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			OutputStream os = new FileOutputStream(file);
			byte[] b = new byte[1024];
			int readLen = -1;
			while ((readLen = is.read(b)) != -1) {
				os.write(b, 0, readLen);
			}
			os.flush();
			os.close();
			is.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String getNameWithoutExtension(String name) {
		int i = name.lastIndexOf(".");
		if (i >= 0) {
			return name.substring(0, i);
		}
		return name;
	}
}
