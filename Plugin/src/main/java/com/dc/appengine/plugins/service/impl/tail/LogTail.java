package com.dc.appengine.plugins.service.impl.tail;  
  
import java.io.File;  
import java.io.IOException;  
import java.io.RandomAccessFile;  
import java.util.HashSet;  
import java.util.Map;
import java.util.Set;  
import java.util.regex.Pattern;

import com.dc.appengine.plugins.constants.Constants;
  
public class LogTail implements Runnable {  
  
    /** 
     * 存储TailLog侦听器 
     */  
    private final Set<TailNotify> listeners = new HashSet<TailNotify>();  
  
    /** 
     * 当读到文件结尾后暂停的时间间隔 
     */  
    private long sampleInterval = 10;  
  
    /** 
     * 设置日志文件 
     */  
    private File logfile;  
  
    /** 
     * 设置是否从头开始读 
     */  
    private boolean startAtBeginning = false;  
  
    /** 
     * 设置tail运行标记 
     */  
    private boolean tailing = false;  
    
    Process p;
    Object obj;
    
    Pattern success = null;
	Pattern fail = null;
  
	Map<String,Object> result=null;
	
    public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public Pattern getSuccess() {
		return success;
	}

	public void setSuccess(Pattern success) {
		this.success = success;
	}

	public Pattern getFail() {
		return fail;
	}

	public void setFail(Pattern fail) {
		this.fail = fail;
	}

	public Process getP() {
		return p;
	}

	public void setP(Process p) {
		this.p = p;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	public String end;
	
	

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public LogTail(long sampleInterval, File logfile, boolean startAtBeginning) {  
        super();  
        this.sampleInterval = sampleInterval;  
        this.logfile = logfile;  
        this.startAtBeginning = startAtBeginning;  
    }  
  
    /** 
     * 将侦听器加入TailLog中 
     *  
     * @param tailListener 
     */  
    public void add(TailNotify tailListener) {  
        listeners.add(tailListener);  
    }  
  
    /** 
     * 通知所有注册的侦听 
     *  
     * @param line 
     */  
    protected void notify(String line) {  
        for (TailNotify tail : listeners) {  
            tail.notifyMsg(line);  
        }  
    }  
  
    @Override  
    public void run() {  
        long filePointer = 0;  
        if (this.startAtBeginning) { //判断是否从头开始读文件  
            filePointer = 0;  
        } else {  
            filePointer = this.logfile.length(); //指针标识从文件的当前长度开始。  
        }  
        try {  
            RandomAccessFile file = new RandomAccessFile(logfile, "r"); //创建随机读写文件  
            while (this.tailing) {  
                long fileLength = this.logfile.length();  
                if (fileLength < filePointer) {  
                    file = new RandomAccessFile(logfile, "r");  
                    filePointer = 0;  
                }  
                if (fileLength > filePointer) {  
                    file.seek(filePointer);  
                    String line =null;
                    while ((line=file.readLine()) != null) {  
                        this.notify(line);  
//                        line = file.readLine();  
                        if(success!=null && success.matcher(line).find()){
        					result.put(Constants.Plugin.RESULT, true);
        					p.destroy();
                        	stop();
        				}else if(fail!=null && fail.matcher(line).find()){
        					result.put(Constants.Plugin.RESULT, false);
        					p.destroy();
                        	stop();
        				}
//                        if(line.equals("end")){
//                        	p.destroy();
//                        	stop();
//                        }
                    }  
                    filePointer = file.getFilePointer();  
                }  
                Thread.sleep(this.sampleInterval);  
            } 
            synchronized (obj) {
            	obj.notify();
            }
            file.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
  
        }  
    }  
      
      
    /** 
     * 停止tail 
     */  
    public void stop()  
    {  
        this.tailing=false;  
    }  
  
    public void start(){
    	this.tailing=true;
    }
}  