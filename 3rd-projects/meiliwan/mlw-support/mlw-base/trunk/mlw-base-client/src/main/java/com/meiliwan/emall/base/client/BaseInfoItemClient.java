package com.meiliwan.emall.base.client;

//import com.alibaba.fastjson.TypeReference;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.base.bean.BaseInfoItem;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;

/**
 * User: wuzixin
 * Date: 13-6-14
 * Time: 上午9:28
 */
public class BaseInfoItemClient {
//    private static final Logger logger = LoggerFactory.getLogger(BaseInfoItemClient.class);
	
    public static boolean addBaseInfoItem(BaseInfoItem baseIT) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseInfoItemService/addBaseInfoItem", baseIT));
        return obj.get("resultObj").getAsBoolean();
    }

    public static boolean updateBaseInfoItem(BaseInfoItem baseIT) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseInfoItemService/updateBaseInfoItem", baseIT));
        return obj.get("resultObj").getAsBoolean();
    }

    public static BaseInfoItem getBaseInfoItemById(int itemId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseInfoItemService/getBaseInfoItemById", itemId));
        return new Gson().fromJson(obj.get("resultObj"), BaseInfoItem.class);
    }

    public static PagerControl<BaseInfoItem> getListByPager(BaseInfoItem baseIT, PageInfo pageInfo) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseInfoItemService/getListByPager", baseIT, pageInfo));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<BaseInfoItem>>() {
        }.getType());
    }

    public static boolean deleteBaseInfoItem(int itemId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseInfoItemService/deleteBaseInfoItem", itemId));
        return obj.get("resultObj").getAsBoolean();
    }

    public static List<BaseInfoItem> getListByBaseIT(BaseInfoItem baseIT) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseInfoItemService/getListByBaseIT", baseIT));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<BaseInfoItem>>() {
        }.getType());
    }

    public static void main(String[] args) {
        BaseInfoItem bif = new BaseInfoItem();
        bif.setParentId(0);
        bif.setInfoItemName("关于我们");
        bif.setItemType((short) 1);

        System.out.println(addBaseInfoItem(bif));
    }
}
