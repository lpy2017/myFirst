package com.dc.cd.plugins.utils.command.analyser;

public  abstract class  AbstractLogAnalyser <R>  implements Analyser<R> {
	
	protected String logPath;
	protected String successRE;
	protected String failRE;
	
	protected String type;
	
	protected R result;

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setSuccessRE(String successRE) {
		// TODO Auto-generated method stub
		this.successRE=successRE;
	}
	public void setFailRE(String failRE) {
		// TODO Auto-generated method stub
		this.failRE=failRE;
	}

	public void setlogPath(String logPath) {
		// TODO Auto-generated method stub
		this.logPath=logPath;
	}

	public R getResult() {
		// TODO Auto-generated method stub
		return this.result;
	}

}
