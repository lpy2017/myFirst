package com.dc.appengine.cloudui.master.service;

public interface IClusterService {

	void addRoster(String ip, String userName, String password);
	void delRoster(String ip, String userName, String password);


}