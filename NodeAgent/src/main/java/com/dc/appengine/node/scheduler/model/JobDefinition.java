package com.dc.appengine.node.scheduler.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.dc.appengine.node.configuration.model.AbstractDefinition;

@XmlRootElement( name = "job-config" )
public class JobDefinition extends AbstractDefinition {
	@XmlAttribute
	private String id;
	@XmlAttribute
	private int delay;
	@XmlAttribute
	private int period;
	@XmlAttribute
	private TimeEnume unit;
	@XmlElement( name = "step" )
	private List< StepDefinition > steps = new ArrayList< StepDefinition >( 10 );

	public String getId() {
		return id;
	}

	public void setId( String id ) {
		this.id = id;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay( int delay ) {
		this.delay = delay;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod( int period ) {
		this.period = period;
	}

	public TimeEnume getUnit() {
		return unit;
	}

	public void setUnit( TimeEnume unit ) {
		this.unit = unit;
	}

	public List< StepDefinition > getSteps() {
		return steps;
	}

	public void setSteps( List< StepDefinition > steps ) {
		this.steps = steps;
	}

}
