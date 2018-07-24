package com.dc.appengine.appsvn.utils.structurecheck;

import java.util.Map;

public interface IChecker {
	public boolean check(String fileName,CheckerDefinition checker,Map<String,Object> messgae);
}
