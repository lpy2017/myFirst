//package com.dc.appengine.node.ws.server;
//
//import java.io.FileNotFoundException;
//import java.net.URISyntaxException;
//import java.util.Map;
//import java.util.NoSuchElementException;
//
//import javax.ws.rs.FormParam;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.alibaba.fastjson.JSON;
//import com.dc.appengine.ConfigClient.service.impl.ModuleService;
//import com.dc.appengine.node.NodeEnv;
//import com.dc.appengine.node.init.ParaFormatException;
//
//@Path("module")
//public class ConfigRestService
//{
//  private static Logger log = LoggerFactory.getLogger(ConfigRestService.class);
//
//  @POST
//  @Path("updateModuleConfigs")
//  public String modifyModuleConfigs(@FormParam("json") String json)
//  {
//    String modifyResul = "{\"result\":\"true\",\"message\":\"modifyConfigs is success\"}";
//    ModuleService moduleService = new ModuleService();
//    Map jsons = (Map)JSON.parseObject(json);
//    String content = jsons.get("content").toString();
//    String module = jsons.get("module").toString();
//    String operate = jsons.get("operate").toString();
//    String moduleName = (String)jsons.get("module");
//    log.debug(json);
//    try {
//      modifyResul = moduleService.modifyModuleConfServiceIssue(content, moduleName.toLowerCase());
//      if (modifyResul.contains("false")) {
//        modifyResul = "{\"result\":\"false\",\"message\":\"modifyConfigs is error\"}";
//        return modifyResul;
//      }
//
//      reloadNormalConfigs(moduleName, null);
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      modifyResul = "{\"result\":\"false\",\"message\":\"modifyConfigs is error\"}";
//    }
//    return modifyResul; }
//
//  public String reloadNormalConfigs(String moduleName, String mapJson) {
//    Map mapdiff = null;
//    if (mapJson != null)
//      mapdiff = (Map)JSON.parseObject(mapJson);
//
//    String result = "{\"result\":\"true\",\"message\":\"reload Configs is success\"}";
//    try {
//      NodeEnv.getInstance().reset(mapdiff, "MOM");
//    }
//    catch (NoSuchElementException e) {
//      e.printStackTrace();
//    }
//    catch (IllegalArgumentException e) {
//      e.printStackTrace();
//    }
//    catch (FileNotFoundException e) {
//      e.printStackTrace();
//    }
//    catch (URISyntaxException e) {
//      e.printStackTrace();
//    }
//    catch (ParaFormatException e) {
//      e.printStackTrace();
//    }
//    return result;
//  }
//}