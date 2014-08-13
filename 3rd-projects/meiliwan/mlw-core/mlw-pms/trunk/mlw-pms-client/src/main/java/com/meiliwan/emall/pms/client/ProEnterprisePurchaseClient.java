package com.meiliwan.emall.pms.client;



import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.pms.bean.ProEnterprisePurchase;

import java.util.List;


/**
 *  企业采购 client
 * Created by yiyou.luo on 13-6-18.
 */
public class ProEnterprisePurchaseClient {
    private static final String SERVICE_NAME = "proEnterprisePurchaseService";

    public static void main(String args[]){
        // 1、通过id获取 企业采购 实例
        ProEnterprisePurchase ep = new ProEnterprisePurchase();
        ep.setContact("林小姐");
        ep.setContactPhone("15227892396");
        ep.setEnterprise("超越健身连锁集团");
        ep.setEnterpriseAddr("广西南宁市东葛路12号");
        ep.setPurchaseDemand("需要一次性购入一吨优质泰国山竹");
        int id = saveProEnterprisePurchase(ep);

        // 2。通过 id获取 企业采购 list
        ProEnterprisePurchase gep = getProEnterprisePurchaseById(id);
        System.out.println(gep.getContact() + "于：" + gep.getCreateTime() + "发出需求 " + gep.getPurchaseDemand());

        //3。修改 企业采购                                              六个核桃
        gep.setEnterprise(gep.getEnterprise()+"分公司");
        System.out.println("修改结果：" + updateProEnterprisePurchase(gep));


        //4.
        PageInfo pageInfo  = new PageInfo();
        ProEnterprisePurchase pep = new ProEnterprisePurchase();
        System.out.println("分页查到数据条数=" + getProEnterprisePurchasePaperByObj(pep, pageInfo).getEntityList().size());

    }
    /**
     * 通过id获取 企业采购 实例
     * @param id
     */
    public static ProEnterprisePurchase getProEnterprisePurchaseById(int id) {
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                    JSONTool.buildParams(SERVICE_NAME + "/getProEnterprisePurchaseById", id));
            return new Gson().fromJson(obj.get("resultObj"), new TypeToken<ProEnterprisePurchase>() {
            }.getType());
    }

    /**
     * 添加 企业采购
     *
     * @param proEnterprisePurchase
     */
    @IceServiceMethod
    public static int  saveProEnterprisePurchase(ProEnterprisePurchase proEnterprisePurchase) {
        if (proEnterprisePurchase !=null){
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                    JSONTool.buildParams(SERVICE_NAME + "/saveProEnterprisePurchase", proEnterprisePurchase));
            return Integer.parseInt(obj.get("resultObj").getAsString());
        } else{
            return -1;
        }
    }

    /**
     * 修改 企业采购
     *
     * @param proEnterprisePurchase
     */
    @IceServiceMethod
    public static boolean updateProEnterprisePurchase(ProEnterprisePurchase proEnterprisePurchase) {
        if (proEnterprisePurchase !=null){
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                    JSONTool.buildParams(SERVICE_NAME + "/updateProEnterprisePurchase", proEnterprisePurchase));
            return obj.get("resultObj") == null ? false : obj.get("resultObj").getAsBoolean();
        } else {
            return false;
        }
    }

    /**
     * 通过  /**
     * 通过 企业采购 实体参数获取对应的实体列表包含物理分页
     *
     * @param pageInfo
     */
    @IceServiceMethod
    public static PagerControl getProEnterprisePurchasePaperByObj(ProEnterprisePurchase proEnterprisePurchase, PageInfo pageInfo) {
        if (proEnterprisePurchase != null && pageInfo != null) {
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                    JSONTool.buildParams(SERVICE_NAME + "/getProEnterprisePurchasePaperByObj", proEnterprisePurchase, pageInfo));
            return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<ProEnterprisePurchase>>() {
            }.getType());
        } else{
            return null;
        }
    }



}
