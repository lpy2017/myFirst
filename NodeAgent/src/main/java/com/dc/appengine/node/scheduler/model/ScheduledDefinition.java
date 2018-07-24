package com.dc.appengine.node.scheduler.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.dc.appengine.node.configuration.model.AbstractDefinition;

@XmlRootElement( name = "scheduled-config" )
public class ScheduledDefinition extends AbstractDefinition {
	@XmlElement( name = "job-config" )
	private List< JobDefinition > jobs = new ArrayList< JobDefinition >( 10 );

	public List< JobDefinition > getJobs() {
		return jobs;
	}

	public void setJobs( List< JobDefinition > jobs ) {
		this.jobs = jobs;
	}
	
}
