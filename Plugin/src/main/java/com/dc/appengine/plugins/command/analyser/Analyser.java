package com.dc.appengine.plugins.command.analyser;

import java.io.InputStream;

public interface Analyser <R>{
  public void analysis(InputStream is) throws Exception;
  public void setSuccessRE(String successRE);
  public void setFailRE(String failRE);
  public void setlogPath(String logPath);
  public R getResult();
}
