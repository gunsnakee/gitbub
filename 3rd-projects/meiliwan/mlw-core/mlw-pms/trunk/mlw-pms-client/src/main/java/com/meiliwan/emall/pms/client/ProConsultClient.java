package com.meiliwan.emall.pms.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.pms.bean.ProConsult;
import com.meiliwan.emall.pms.dto.ConsultDTO;
import com.meiliwan.emall.pms.dto.QueryCountsDTO;
import com.meiliwan.emall.service.BaseService;

import java.util.List;


/**
 * User: guangdetang
 * Date: 13-6-14
 * Time: 上午11:16
 */
public class ProConsultClient {

    private static final String SERVICE_NAME = "proConsultService";

    /**
     * 发起咨询
     * @param consult
     * @return
     */
    public static boolean addConsult(ProConsult consult) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/addConsult", consult));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();

    }

    /**
     * 修改咨询
     * @param consult
     * @return
     */
    public static boolean updateConsult(ProConsult consult) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/updateConsult", consult));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get("resultObj").getAsBoolean();
    }

    /**
     * 管理员修改咨询列表上的各种咨询状态（各种删除、显示）
     * @param ids
     * @param handle
     * @return
     */
    public static boolean updateConsultStateByAdmin(int[] ids, int handle) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/updateConsultStateByAdmin", ids, handle));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 管理员回复咨询
     * @param id
     * @param replyContent
     * @return
     */
    public static boolean replyConsult(int id, String replyContent) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/updateReplyConsult", id, replyContent));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 获得咨询详情
     * @param id
     * @return
     */
    public static ProConsult getConsult(int id) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getConsult", id));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), ProConsult.class);
    }

    /**
     * 管理员获得咨询列表
     * @param consult
     * @param pageInfo
     * @return
     */
    public static PagerControl<ProConsult> getConsultPagerByAdmin(ConsultDTO consult, PageInfo pageInfo) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getConsultPagerByAdmin", consult, pageInfo));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<PagerControl<ProConsult>>() {
        }.getType());
    }

    /**
     * 买家个人中心查询咨询列表
     * @param pageInfo
     * @param uid
     * @return
     */
    public static PagerControl<ProConsult> getConsultPagerByBuyer(ProConsult consult, PageInfo pageInfo, int uid) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getConsultPagerByBuyer",consult, pageInfo, uid));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<PagerControl<ProConsult>>() {
        }.getType());
    }

    /**
     * 根据商品ID获得咨询详情（商品详情页用）<br />返回的列表：管理员已经回复，管理员未删，用户未删，在前台显示
     * @param type     咨询类型（传值：0查询全部，1商品咨询，2库存及配送，3支付问题，4促销及赠品，5其他）
     * @param pageInfo
     * @param proId
     * @return
     */
    public static PagerControl<ProConsult> getConsultPagerByProId(PageInfo pageInfo, int proId, short type) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getConsultPagerByProId", pageInfo, proId, type));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<PagerControl<ProConsult>>() {
        }.getType());
    }

    /**
     * 获得不同咨询类型的咨询条数
     * 返回QueryCountsDTO列表：每个对象包含咨询类型和该类型的咨询条数
     * @param proId
     * @return
     */
    public static List<QueryCountsDTO> getConsultCounts(int proId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getConsultCounts", proId));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<QueryCountsDTO>>() {
        }.getType());
    }

    public static void main(String[] args) {
        List<QueryCountsDTO> ll = getConsultCounts(1);
        if (ll != null && ll.size() > 0) {
            for (QueryCountsDTO q : ll) {
                System.out.println("=========================咨询类型是" + q.getKeyType()+"的咨询条数为："+q.getCounts());
            }
        }
    }
}
