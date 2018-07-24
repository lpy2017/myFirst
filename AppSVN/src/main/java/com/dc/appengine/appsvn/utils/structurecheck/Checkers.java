package com.dc.appengine.appsvn.utils.structurecheck;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="template")
public class Checkers {
	@XmlElement(name="checker")
	private ArrayList<CheckerDefinition> checkers;

	public ArrayList<CheckerDefinition> getCheckers() {
		return checkers;
	}

	public void setCheckers(ArrayList<CheckerDefinition> checkers) {
		this.checkers = checkers;
	}
}
