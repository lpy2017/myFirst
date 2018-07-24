package com.dc.appengine.appmaster.utils;

public class PatternUtil {
	public  static String P1 = "\\$\\{([a-zA-Z0-9_]+)\\}";
	public  static String P2 = "\\$\\{([a-zA-Z0-9_]+)\\#([a-zA-Z0-9_\\.]+)\\}";
	public  static String P3 = "\\$\\{([a-zA-Z0-9_]+)\\#[V|v]([0-9]+)\\#([a-zA-Z0-9_]+)\\}";
	public  static String P4 = "\\$\\{([a-zA-Z0-9_]+)\\#([-a-zA-Z0-9_]+)\\#([a-zA-Z0-9_]+)\\#([a-zA-Z0-9_]+)\\}";
	public  static String P5 = "\\$\\{([a-zA-Z0-9_]+)\\#([-a-zA-Z0-9_]+)\\#([\\${a-zA-Z0-9_}]+)\\#([a-zA-Z0-9_]+)\\}";
	public  static String P6 = "\\$\\{([-a-zA-Z0-9_]+)\\#([a-zA-Z0-9_]+)\\#([a-zA-Z0-9_]+)\\}";
	public  static String P7 = "\\$\\{([a-zA-Z0-9_]+)\\#([a-zA-Z0-9_]+)\\#([a-zA-Z0-9_]+)\\}";
	public  static String P8 = "\\$\\{([a-zA-Z0-9_]+)\\#([V|v][0-9]+)\\#([a-zA-Z0-9_]+)\\}";
	public  static String P9 = "\\$\\{([a-zA-Z0-9_]+)\\#([a-zA-Z0-9_]+)\\#([V|v][0-9]+)\\#([a-zA-Z0-9_]+)\\}";
	public  static String P10 = "\\$\\{([a-zA-Z0-9_-]+)\\#([a-zA-Z0-9_]+)\\#([a-zA-Z0-9_]+)\\}";
	public  static String P11 = "\\$\\{(blueprint)\\#([a-zA-Z0-9_\\.]+)\\}";
	
}
