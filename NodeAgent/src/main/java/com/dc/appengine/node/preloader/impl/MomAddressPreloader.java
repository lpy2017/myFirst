package com.dc.appengine.node.preloader.impl;

import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.node.configuration.Constants;
import com.dc.appengine.node.configuration.model.NodeProperties;
import com.dc.appengine.node.preloader.AbstractFilePreloader;
import com.dc.appengine.node.utils.FileUtil;
import com.dc.appengine.node.utils.XmlConfigUtil;

public class MomAddressPreloader extends AbstractFilePreloader {
	private static Logger log = LoggerFactory
			.getLogger(MomAddressPreloader.class);

	@Override
	public void preload() throws Exception {
		String momAddress = NodeProperties.getMomUrl();
		log.info("change mom address to " + momAddress);
		reWrite("mxsd_client.xml", momAddress);
		reWrite("mxsd_client_topic.xml", momAddress);
	}

	private void reWrite(String name, String momAddress) throws Exception {
		FileWriter fw1 = null;
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<client>");
			for (String s : momAddress.split("#")) {
				String tmp = XmlConfigUtil
						.xmlUpdateDemo(
								FileUtil.getInstance().getFile(
										"conf/template/" + name,
										Constants.Env.BASE_CONF),
								"ProviderURL", s)
						.replace("<client>", "")
						.replace("</client>", "")
						.replace(
								"<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
								"")
						.replace(
								"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>",
								"");
				sb.append(tmp);
			}
			sb.append("</client>");
			fw1 = new FileWriter(FileUtil.getInstance().getFile("conf/" + name,
					Constants.Env.BASE_CONF), false);
			fw1.write(sb.toString());
			fw1.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				fw1.close();
			} catch (IOException e) {
				throw e;
			}
		}
	}
}
