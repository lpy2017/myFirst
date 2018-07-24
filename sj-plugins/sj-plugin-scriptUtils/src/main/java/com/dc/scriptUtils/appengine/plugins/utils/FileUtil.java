package com.dc.scriptUtils.appengine.plugins.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;

public class FileUtil {
	private static Logger log = Logger.getLogger( FileUtil.class );
	
	public static void loadJar(String path){
		File file = new File(path);
		if(!file.exists()){
			log.error("no ["+path+"] file to load");
		}
		try {
			URL url = new URL(path);
			URLClassLoader classLoader = new URLClassLoader(new URL[] { url }, Thread.currentThread()  
					.getContextClassLoader()); 
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
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
	public static Boolean createZipFile(String sourceFilePath,String zipFilePath,String fileName){
		boolean flag = false;  
        File sourceFile = new File(sourceFilePath);  
        FileInputStream fis = null;  
        BufferedInputStream bis = null;  
        FileOutputStream fos = null;  
        ZipOutputStream zos = null;  
          
        if(sourceFile.exists() == false){  
            System.out.println("待压缩的文件目录："+sourceFilePath+"不存在.");  
        }else{  
            try {  
                File zipFile = new File(zipFilePath + "/" + fileName +".zip");  
                if(zipFile.exists()){  
                    System.out.println(zipFilePath + "目录下存在名字为:" + fileName +".zip" +"打包文件."); 
                    zipFile.delete();
                }
                File[] sourceFiles = sourceFile.listFiles();  
                if(null == sourceFiles || sourceFiles.length<1){  
                    System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");  
                }else{  
                    fos = new FileOutputStream(zipFile);  
                    zos = new ZipOutputStream(new BufferedOutputStream(fos));  
                    byte[] bufs = new byte[1024*10];  
                    for(int i=0;i<sourceFiles.length;i++){  
                        //创建ZIP实体，并添加进压缩包  
                        ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());  
                        zos.putNextEntry(zipEntry);  
                        //读取待压缩的文件并写进压缩包里  
                        fis = new FileInputStream(sourceFiles[i]);  
                        bis = new BufferedInputStream(fis, 1024*10);  
                        int read = 0;  
                        while((read=bis.read(bufs, 0, 1024*10)) != -1){  
                            zos.write(bufs,0,read);  
                        }  
                    }  
                    flag = true;  
                }  
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
                throw new RuntimeException(e);  
            } catch (IOException e) {  
                e.printStackTrace();  
                throw new RuntimeException(e);  
            } finally{  
                //关闭流  
                try {  
                    if(null != bis) bis.close();  
                    if(null != zos) zos.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                    throw new RuntimeException(e);  
                }  
            }  
        }  
        return flag;  
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
	//先增加方法
	public static Boolean writeFile(String json,String fileParePath,String fileName){
		Boolean success = false;
		File f =null;
		if(fileName ==null){
			f = new File(fileParePath);
		}else{
			File fp =new File(fileParePath);
			if(!fp.exists()){
				fp.mkdirs();
			}
			f =new File(fileParePath+File.separator+fileName);
		}
		
		BufferedWriter wr =null;
		if(f.exists()){
			f.delete();
		}else{
			try {
				f.createNewFile();
				wr= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
				wr.write(json.trim());
				success = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				try {
					if(wr !=null){
						wr.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return success;
	}
	
	public static Boolean writeFileIO(InputStream is,String fileParePath,String fileName){
		Boolean success = false;
		File fp =new File(fileParePath);
		File f =new File(fileParePath+File.separator+fileName);
		BufferedInputStream in=null;
		BufferedOutputStream out = null;
		if(!fp.exists()){
			fp.mkdir();
		}
		if(f.exists()){
			f.delete();
		}else{
			try {
				f.createNewFile();
				out= new BufferedOutputStream(new FileOutputStream(f));
				in = new BufferedInputStream(is);
				int byteread=0;
				byte[] buffer = new byte[1024];
				while((byteread = in.read(buffer)) !=-1){
					out.write(buffer, 0, byteread);
				}
				out.flush();
				success = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				try {
					if(out !=null){
						out.close();
					}
					if(in !=null){
						in.close();
					}
					if(is !=null){
						is.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}	
		return success;
	}

	 public static Boolean unZipFile(String filePath,String targetPath){
		 Boolean success=false;
		 try {  
	            ZipInputStream Zin=new ZipInputStream(new FileInputStream(new File(filePath)));//输入源zip路径  
	            BufferedInputStream Bin=new BufferedInputStream(Zin);  
	            String Parent=targetPath; //输出路径（文件夹目录）  
	            File Fout=null;  
	            ZipEntry entry;  
	            try {  
	                while((entry = Zin.getNextEntry())!=null && !entry.isDirectory()){  
	                    Fout=new File(Parent,entry.getName());  
	                    if(!Fout.exists()){  
	                        (new File(Fout.getParent())).mkdirs();  
	                    }  
	                    FileOutputStream out=new FileOutputStream(Fout);  
	                    BufferedOutputStream Bout=new BufferedOutputStream(out);  
	                    int b;  
	                    while((b=Bin.read())!=-1){  
	                        Bout.write(b);  
	                    }  
	                    Bout.close();  
	                    out.close();  
	                    System.out.println(Fout+"解压成功"); 
	                    success=true;
	                }  
	                Bin.close();  
	                Zin.close();  
	            } catch (IOException e) {  
	                // TODO Auto-generated catch block  
	                e.printStackTrace();  
	            }  
	        } catch (FileNotFoundException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }
		 return success;
	 }
	 
	
	 public static List<String> traverseFolder(String path,List<String> fileList) {
		 File file = new File(path);
		 if (file.exists()) {
			 File[] files = file.listFiles();
			 if (files.length == 0) {
				 log.error(file.getAbsolutePath()+" 文件夹是空的!");
				 return null;
			 } else {
				 for (File file2 : files) {
					 if (file2.isDirectory()) {
						 traverseFolder(file2.getAbsolutePath(),fileList);
					 } else {
						 fileList.add(file2.getAbsolutePath());


					 }
				 }
			 }
		 } else {
			 log.error(file.getAbsolutePath()+" 文件不存在!");
		 }
		 return fileList;
	 }
	 
	 public static Map<String,Object> generateScriptFile(String CMD){
			Map<String,Object> resultMap = new HashMap<String,Object>();
			Boolean result=true;
			String message ="";
			//生成脚本文件
			String fileName="";
			boolean isWin = System.getProperty("os.name").toLowerCase().startsWith("win");
			if(isWin){
				fileName="tmpCMDScript"+UUID.randomUUID()+".bat";
				CMD=CMD.replace("\n", System.lineSeparator());//"\n"==>"\r\n"
			}else{
				fileName="tmpCMDScript"+UUID.randomUUID()+".sh";
			}
			String filePath="../tmp/"+fileName;
			File file = new File(filePath);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			FileOutputStream out=null;
			OutputStreamWriter outWR=null;
			BufferedWriter wr =null;
			try {
				out = new FileOutputStream(file);
				outWR=new OutputStreamWriter(out);
				wr=new BufferedWriter(outWR);
				wr.write(CMD);
				wr.flush();
				//设置文件权限
				file.setExecutable(true);
				file.setWritable(true);
				file.setReadable(true);
				log.debug("file is execute allow : " + file.canExecute());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				result=false;
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				result=false;
				e.printStackTrace();
			}finally{
				try {
					if(wr !=null){
						wr.close();
					}
					if(outWR !=null){
						outWR.close();
					}
					if(out !=null){
						out.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(!result){
				message="生成scriptFile error";
			}else{
				try {
					String tmpFilePath =URLDecoder.decode(file.getAbsolutePath(), "utf-8");
					resultMap.put("filePath", tmpFilePath);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			resultMap.put("result",result);
			resultMap.put("message",message);
			return resultMap;
		}
	 
	 //将文件patchFile合并到文件srcFile中
	 public static void mergeZipPackage(File srcFile,File patchFile){
			String jarFileOutTmp=srcFile.getParentFile().getAbsolutePath()+File.separator+"tmp_"+srcFile.getName();
			ArchiveOutputStream out=null;
			ArchiveInputStream srcIn = null;
			ArchiveInputStream patchIn = null;
			try {
				FileUtils.copyFile(srcFile, new File(jarFileOutTmp), true);
				BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(srcFile));
				BufferedInputStream srcFileIn = new BufferedInputStream(new FileInputStream(jarFileOutTmp));
				BufferedInputStream PathFileIn = new BufferedInputStream(new FileInputStream(patchFile));
				out = new ArchiveStreamFactory()
						.createArchiveOutputStream(ArchiveStreamFactory.JAR, fileOut);
				srcIn = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, srcFileIn);
				patchIn = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, PathFileIn);
				//拷贝源文件
				JarArchiveEntry entrySrc = null;
				while((entrySrc=(JarArchiveEntry)srcIn.getNextEntry())!=null){
					out.putArchiveEntry(entrySrc);
					IOUtils.copy(srcIn, out);  
	                out.closeArchiveEntry();
				};
				//拷贝补丁文件
				JarArchiveEntry entryPath = null;
				while((entryPath=(JarArchiveEntry)patchIn.getNextEntry())!=null){
					out.putArchiveEntry(entryPath);
					IOUtils.copy(patchIn, out);  
	                out.closeArchiveEntry();
				};
	            out.flush();  
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					if(out !=null){
						out.close();
					}
					if(srcIn !=null){
						srcIn.close();
					}
					if(patchIn !=null){
						patchIn.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
	 }
	 
	 
	 public static Map<String,Object> generateSqlFile(String content,String fileRootPath,String fileName){
			Map<String,Object> resultMap = new HashMap<>();
			Boolean result=true;
			String message ="";
			//生成脚本文件
			String filePath=fileRootPath+File.separator+fileName;
			File file = new File(filePath);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			FileOutputStream out=null;
			OutputStreamWriter outWR=null;
			BufferedWriter wr =null;
			try {
				out = new FileOutputStream(file);
				outWR=new OutputStreamWriter(out,Charset.forName(System.getProperty("sun.jnu.encoding")));
				wr=new BufferedWriter(outWR);
				wr.write(content);
				wr.flush();
				//设置文件权限
				file.setExecutable(true);
				file.setWritable(true);
				file.setReadable(true);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				result=false;
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				result=false;
				e.printStackTrace();
			}finally{
				try {
					if(wr !=null){
						wr.close();
					}
					if(outWR !=null){
						outWR.close();
					}
					if(out !=null){
						out.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(!result){
				message="生成"+fileName+" error";
			}else{
				try {
					String tmpFilePath =URLDecoder.decode(file.getAbsolutePath(), "utf-8");
					resultMap.put("filePath", tmpFilePath);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			resultMap.put("result",result);
			resultMap.put("message",message);
			return resultMap;
		}
	 
	 public static File changeDoc2Sql(File file){
			FileInputStream is=null;
			try {
				String fileName = file.getName();
				is = new FileInputStream(file);
				if(fileName.endsWith(".doc")){
					HWPFDocument doc = new HWPFDocument(is);
					Range range = doc.getRange();
					String content =readTable(range);
					Map<String,Object> resultMap =FileUtil.generateSqlFile(content, file.getParentFile().getAbsolutePath(),fileName.replace(".doc", "TmpCD.sql"));
					doc.close();
					return new File((String)resultMap.get("filePath"));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(is!=null){
					try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
			return null;
	 }
	 
	 private static String readTable(Range range) {
			String content=null;
			// 遍历range范围内的table。
			TableIterator tableIter = new TableIterator(range);
			Table table;
			TableRow row;
			TableCell cell;
			start:{
				while (tableIter.hasNext()) {
					table = tableIter.next();
					int rowNum = table.numRows();
					for (int j = 0; j < rowNum; j++) {
						if(j!=4){
							continue;
						}
						row = table.getRow(j);
						int cellNum = row.numCells();
						for (int k = 0; k < cellNum; k++) {
							cell = row.getCell(k);
							content=cell.text().trim();
							System.out.println(content);
							break start;
						}
					}
				}
			}
			return content;
		} 
	 
	 public static void main(String[] args){
		 String CMD="ddd\r\r\r\naaa";
		 System.out.println("修改前==========="+CMD);
		 CMD=CMD.replace("\n", System.lineSeparator());
		 System.out.println("修改后==========="+CMD);
		 
	 }
}
