package com.meiliwan.emall.cms.client;

import com.google.gson.JsonObject;
import com.meiliwan.emall.cms.bean.CmsFragment;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;

/**
 * Created by wenlepeng on 13-8-22.
 */
public class CmsFragmentServiceClient {

    public static String getCmsFragmentByPageId(int pageId){
        String content="";
        if(pageId > 0){
          JsonObject object = IceClientTool.sendMsg(IceClientTool.CMS_ICE_SERVICE,JSONTool.buildParams("cmsFragmentService/getLatestFragmentByPageId",pageId));
          content = object == null ?"":object.get("resultObj").getAsString();
        }
        return content;
    }


    public static int addCmsFragment(CmsFragment cmsFragment){
      int flag=0;
        if(cmsFragment != null){
            JsonObject object = IceClientTool.sendMsg(IceClientTool.CMS_ICE_SERVICE,JSONTool.buildParams("cmsFragmentService/addFragment",cmsFragment));
            flag = object == null ? 0:object.get("resultObj").getAsInt();
        }
        return flag;
    }
}
