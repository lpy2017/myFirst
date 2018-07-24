package com.dc.appengine.cloudui.master.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dc.appengine.cloudui.entity.Host;
import com.dc.appengine.cloudui.master.service.IClusterService;
import com.dc.appengine.cloudui.utils.FileUtil;

@Service("clusterService")
public class ClusterService extends BaseService implements IClusterService{

	private static final Logger log = LoggerFactory.getLogger(ClusterService.class);
	private static final String roster = "/etc/salt/roster";
	@Override
	public void addRoster(String ip, String userName, String password) {
		Host AddHost = new Host(ip, userName, password);
		List<Host> hostLists  =FileUtil.getHost(roster);
		for (Host host : hostLists) {
			if (host.getHost().equals(ip)) {
				log.debug("主机信息已存在，更新主机信息");
				hostLists.remove(host);
				hostLists.add(AddHost);
				String data = hostLists.toString().replace("[", "").replace("]", "").replace(", ", "");
				FileUtil.writeHost(data,roster);
				return ;
			}
		}

		hostLists.add(AddHost);
		String data = hostLists.toString().replace("[", "").replace("]", "").replace(", ", "");
		FileUtil.writeHost(data,roster);

		log.debug("新增主机信息");
	}
	
	@Override
	public void delRoster(String ip, String userName, String password) {
		List<Host> hostLists  =FileUtil.getHost(roster);
		for (Host host : hostLists) {
			if (host.getHost().equals(ip)) {
				hostLists.remove(host);
				String data = hostLists.toString().replace("[", "").replace("]", "").replace(", ", "");
				FileUtil.writeHost(data,roster);
				return ;
			}
		}

	}


}