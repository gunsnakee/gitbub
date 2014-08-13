package com.meiliwan.emall.pms.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.pms.bean.ProSupplier;
import com.meiliwan.emall.service.BaseService;

import java.util.List;

/**
 * User: guangdetang
 * Date: 13-6-9
 * Time: 下午1:50
 */
public class ProSupplierClient {

    private static final String SERVICE_NAME = "proSupplierService";
    /**
     * 添加供应商
     * @param supplier
     * @return
     */
    public static boolean addSupplier(ProSupplier supplier) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/saveSupplier", supplier));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 修改供应商、删除
     * @param supplier
     * @return
     */
    public static boolean updateSupplier(ProSupplier supplier) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/updateSupplier", supplier));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 获得一条供应商
     * @param supplierId
     * @return
     */
    public static ProSupplier getSupplierById(int supplierId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getSupplierById", supplierId));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), ProSupplier.class);
    }

    /**
     * 获得供应商分页列表
     * @param supplier
     * @param pageInfo
     * @return
     */
    public static PagerControl<ProSupplier> getSupplierPager(ProSupplier supplier, PageInfo pageInfo) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getSupplierPager", supplier, pageInfo));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<PagerControl<ProSupplier>>() {
        }.getType());
    }

    /**
     * 获得供应商产地下拉列表。（前台使用）
     * @param supplier
     * @return
     */
    public static List<ProSupplier> getSupplierList(ProSupplier supplier) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getSupplierList", supplier));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<ProSupplier>>() {
        }.getType());
    }

    /**
     * 查询供应商是否重复
     * @param supplierName
     * @return
     */
    public static boolean getRepeatSupplier(String supplierName) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getRepeatSupplier", supplierName));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }
}
