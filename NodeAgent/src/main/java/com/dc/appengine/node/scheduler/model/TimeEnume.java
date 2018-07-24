package com.dc.appengine.node.scheduler.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum( String.class )
public enum TimeEnume {
	
	@XmlEnumValue( value="micros" )MICROSECONDS,
	@XmlEnumValue( value="millis" )MILLISECONDS,
	@XmlEnumValue( value="seconds" )SECONDS,
	@XmlEnumValue( value="minutes" )MINUTES,
	@XmlEnumValue( value="hours" )HOURS,
	@XmlEnumValue( value="days" )DAYS,
	@XmlEnumValue( value="months" )MONTHS,
	
}
