package com.meiliwan.emall.bkstage.client;

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.util.TypeUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.bkstage.bean.BksUserOperateLog;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 *  后台用户操作日志 client
 * Created by yiyou.luo on 13-6-16.
 */
public class BksUserOperateLogClient {
    private static final String SERVICE_NAME = "bksUserOperateLogService";



    public static void main(String args[]){
        BksUserOperateLog log = new BksUserOperateLog();
        log.setLoginId(1231313);
        log.setLoginName("lawyer");
        log.setIp("10.249.0.1");
        log.setMenuId(123);
        log.setOperateUrl("mms/collect/list");
        log.setOperateParameter("parameter.....");
        saveBksUserOperateLog(log);

    }
    /**
     * 通过id获取 后台用户操作日志 实例
     * @param id
     */
    public static BksUserOperateLog getBksUserOperateLogById(int id) {
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                    JSONTool.buildParams(SERVICE_NAME + "/getBksUserOperateLogById", id));
            return  new Gson().fromJson(obj.get("resultObj"), new TypeToken<BksUserOperateLog>() {
            }.getType());
    }

    /**
     * 通过 用户id获取 后台用户操作日志 list
     * @param uid
     */
    @IceServiceMethod
    public static List<BksUserOperateLog> getBksUserOperateLogByUId(int uid) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getBksUserOperateLogByUId", uid));
        return  new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<BksUserOperateLog>>() {
        }.getType());
    }

    /**
     * 添加 后台用户操作日志
     *
     * @param bksUserOperateLog
     */
    @IceServiceMethod
    public static int  saveBksUserOperateLog(BksUserOperateLog bksUserOperateLog) {
        if (bksUserOperateLog !=null){
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                    JSONTool.buildParams(SERVICE_NAME + "/saveBksUserOperateLog", bksUserOperateLog));
            return Integer.parseInt(obj.get("resultObj").getAsString());
        } else{
            return -1;
        }
    }

    /**
     * 修改 后台用户操作日志
     *
     * @param bksUserOperateLog
     */
    @IceServiceMethod
    public static boolean updateBksUserOperateLog(BksUserOperateLog bksUserOperateLog) {
        if (bksUserOperateLog !=null){
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                    JSONTool.buildParams(SERVICE_NAME + "/updateBksUserOperateLog", bksUserOperateLog));
            return obj.get("resultObj") == null ? false : obj.get("resultObj").getAsBoolean();

        } else {
            return false;
        }
    }

    /**
     * 通过  /**
     * 通过 后台用户操作日志 实体参数获取对应的实体列表包含物理分页
     *
     * @param pageInfo
     */
    @IceServiceMethod
    public static PagerControl getBksUserOperateLogPaperByObj(BksUserOperateLog bksUserOperateLog, PageInfo pageInfo) {
        if (bksUserOperateLog != null && pageInfo != null) {
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                    JSONTool.buildParams(SERVICE_NAME + "/getBksUserOperateLogPaperByObj", bksUserOperateLog, pageInfo));
            return  new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<BksUserOperateLog>>() {
            }.getType());
        } else{
            return null;
        }
    }
}
