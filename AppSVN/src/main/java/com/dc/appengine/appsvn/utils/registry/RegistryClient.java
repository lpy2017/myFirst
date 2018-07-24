package com.dc.appengine.appsvn.utils.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.alibaba.fastjson.JSON;

public class RegistryClient {
	private WebTarget webTarget;
	private TokenUtil tokenUtil;

	public RegistryClient(String domain, String port, String aPort) {
		webTarget = ClientBuilder.newClient().target(
				"http://" + domain + ":" + port + "/v2");
		tokenUtil = new TokenUtil(domain, aPort);
	}

	@SuppressWarnings("unchecked")
	public List<String> getRepositories(String account, String password) {
		String[] scope = { "registry:catalog:*" };
		String token = tokenUtil.getToken(account, password, scope);
		Builder builder = webTarget.path("_catalog").request()
				.header("Authorization", "Bearer " + token);
		Response response = builder.get();
		Map<String, Object> map = (Map<String, Object>) JSON
				.parseObject((String) ResponseUtil.getResponseMap(response)
						.get("body"));
		return (List<String>) map.get("repositories");
	}

	@SuppressWarnings("unchecked")
	public List<String> getTagList(String account, String password,
			String repository) {
		String[] scope = { "repository:" + repository + ":pull" };
		String token = tokenUtil.getToken(account, password, scope);
		Builder builder = webTarget.path(repository).path("/tags/list")
				.request().header("Authorization", "Bearer " + token);
		Response response = builder.get();
		Map<String, Object> map = (Map<String, Object>) JSON
				.parseObject((String) ResponseUtil.getResponseMap(response)
						.get("body"));
		return (List<String>) map.get("tags");
	}

	@SuppressWarnings("unchecked")
	public String getImageDigestByTag(String account, String password,
			String repository, String tag) {
		String[] scope = { "repository:" + repository + ":pull" };
		String token = tokenUtil.getToken(account, password, scope);
		Builder builder = webTarget
				.path(repository)
				.path("manifests")
				.path(tag)
				.request()
				.header("Authorization", "Bearer " + token)
				.header("Accept",
						"application/vnd.docker.distribution.manifest.v2+json");
		Response response = builder.get();
		String header = (String) ResponseUtil.getResponseMap(response).get(
				"header");
		List<String> digests = (List<String>) JSON.parseObject(header).get(
				"Docker-Content-Digest");
		if (digests == null) {
			return null;
		}
		return digests.get(0);
	}

	public List<String> checkBeforeDelete(String account, String password,
			String repository, String tag) {
		String digest = getImageDigestByTag(account, password, repository, tag);
		List<String> resultList = new ArrayList<>();
		if (digest != null) {
			List<String> list = getTagList(account, password, repository);
			list.remove(tag);
			for (String t : list) {
				String d = getImageDigestByTag(account, password, repository,
						tag);
				if (digest.equals(d)) {
					resultList.add(t);
				}
			}
		}
		return resultList;
	}

	public String deleteImageByTag(String account, String password,
			String repository, String tag) {
		String digest = this.getImageDigestByTag(account, password, repository,
				tag);
		if (digest == null) {
			return Constant.success;
		}
		String[] scope = { "repository:" + repository + ":*" };
		String token = tokenUtil.getToken(account, password, scope);
		Builder builder = webTarget.path(repository).path("manifests")
				.path(digest).request()
				.header("Authorization", "Bearer " + token);
		Response response = builder.delete();
		String status = String.valueOf(ResponseUtil.getResponseMap(response)
				.get("status"));
		String result = "";
		if ("202".equals(status)) {
			result = Constant.success;
		} else {
			result = (String) ResponseUtil.getResponseMap(response).get("body");
		}
		return result;
	}// docker exec -it registry registry garbage-collect
		// /etc/docker/registry/config.yml

	public String deleteImageByDigest(String account, String password,
			String repository, String digest) {
		String[] scope = { "repository:" + repository + ":*" };
		String token = tokenUtil.getToken(password, account, scope);
		Builder builder = webTarget.path(repository).path("manifests")
				.path(digest).request()
				.header("Authorization", "Bearer " + token);
		Response response = builder.delete();
		String status = String.valueOf(ResponseUtil.getResponseMap(response)
				.get("status"));
		String result = "";
		if ("202".equals(status)) {
			result = Constant.success;
		} else {
			result = (String) ResponseUtil.getResponseMap(response).get("body");
		}
		return result;
	}

	public static void main(String args[]) {
		String password = "96e79218965eb72c92a549dd5a330112";// "111111";
		String account = "test";
		String repository = "test/cad";
		// String tag = "1111";
		RegistryClient client = new RegistryClient("192.168.209.143", "5001",
				"5002");

		// List<String> result = client.getRepositories(account, password);
		List<String> results = client.getTagList(account, password, repository);
		// result = client.deleteImageByTag(account, password, repository, tag);
		System.out.println(results);
	}
}
