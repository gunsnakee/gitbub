package com.meiliwan.emall.base.client;

import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonObject;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;


/**
 * Created by wenlepeng on 13-8-22.
 */
public class CmsFragmentServiceClient {

    public static String getCmsFragmentByPageId(int pageId){
        String content="";
        if(pageId > 0){
          JsonObject object = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,JSONTool.buildParams("cmsFragmentService/getLatestFragmentByPageId",pageId));
          content = object == null ?"":object.get("resultObj").getAsString();
        }
        return content;
    }

    public static String getCmsFragmentByPageName(String pageName){
        String content="";
        if(!StringUtils.isBlank(pageName)){
          JsonObject object = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,JSONTool.buildParams("cmsFragmentService/getLatestFragmentByPageName",pageName));
          content = object == null ?"":object.get("resultObj").getAsString();
        }
        return content;
    }
    
}
