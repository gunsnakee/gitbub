package com.meiliwan.emall.base.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.base.bean.BaseHelpPages;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import org.apache.commons.lang.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-12-6
 * Time: 上午11:41
 * To change this template use File | Settings | File Templates.
 */
public class BaseHelpPagesClient {

    public static BaseHelpPages getHelpPageById(Integer uid){
        if(uid == null || uid.intValue() < 0)return null;
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,JSONTool.buildParams("baseHelpPagesService/getHelpPageById", uid));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<BaseHelpPages>() {}.getType());
    }

    public static BaseHelpPages getHelpPageByShortUrl(String shortUrl){
        if(StringUtils.isEmpty(shortUrl)) return  null;
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,JSONTool.buildParams("baseHelpPagesService/getHelpPageByShortUrl", shortUrl));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<BaseHelpPages>() {}.getType());
    }

    public static int updateHelpPageById(BaseHelpPages objParam){
        if(objParam == null) return 0;
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,JSONTool.buildParams("baseHelpPagesService/updateHelpPageById",objParam));
        return obj.get("resultObj") == null ?0:obj.get("resultObj").getAsInt();
    }

    public static int addHelpPage(BaseHelpPages objParam){
        if(objParam == null)return 0;
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,JSONTool.buildParams("baseHelpPagesService/addHelpPage",objParam));
        return obj.get("resultObj") == null ?0:obj.get("resultObj").getAsInt();
    }
}
