package com.meiliwan.emall.mms.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.mms.bean.UserComplaints;
import com.meiliwan.emall.service.BaseService;

/**
 * User: guangdetang
 * Date: 13-6-15
 * Time: 下午5:59
 */
public class UserComplaintsClient {

    private static final String SERVICE_NAME = "userComplaintsService";
    private static final String RETURN_STR = "resultObj";

    /**
     * 用户发起投诉
     * @param complaints
     * @return
     */
    public static boolean addComplaints(UserComplaints complaints) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/addComplaints", complaints));
        return  obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 修改投诉
     * @param complaints
     * @return
     */
    public static boolean updateComplaints(UserComplaints complaints) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/updateComplaints", complaints));
        return  obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 修改投诉各种状态（适用于运营后台模块）
     * @param ids
     * @param handle
     * @return
     */
    public static boolean updateComplaintsState(int[] ids, int handle) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/updateComplaintsState", ids, handle));
        return  obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 用户取消投诉（适用于前台模块）
     * @param id
     * @return
     */
    public static boolean cancelComplaints(int id) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/updateCancelComplaints", id));
        return  obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 管理员回复投诉
     * @param id
     * @param replyContent
     * @return
     */
    public static boolean replyComplaints(int id, String replyContent) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/updateReplyComplaints", id, replyContent));
        return  obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 根据投诉编号获得投诉详情
     * @param id
     * @return
     */
    public static UserComplaints getComplaints(int id) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getComplaints", id));
        return new Gson().fromJson(obj.get(RETURN_STR), UserComplaints.class);
    }

    /**
     * 通过对象获得投诉详情
     * @param complaints
     * @return
     */
    public static UserComplaints getComplaintsByObj(UserComplaints complaints){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getComplaintsByObj", complaints));
        return new Gson().fromJson(obj.get(RETURN_STR), UserComplaints.class);
    }

    /**
     * 运营后台获得投诉分页列表
     * @param complaints
     * @param pageInfo
     * @return
     */
    public static PagerControl<UserComplaints> getComplaintsPagerByAdmin(UserComplaints complaints, PageInfo pageInfo) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getComplaintsPagerByAdmin", complaints, pageInfo));
        return new Gson().fromJson(obj.get(RETURN_STR), new TypeToken<PagerControl<UserComplaints>>() {
        }.getType());
    }

    /**
     * 用户中心获得投诉分页列表
     * @param complaints
     * @param pageInfo
     * @return
     */
    public static PagerControl<UserComplaints> getComplaintsPagerByUser(UserComplaints complaints, PageInfo pageInfo) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getComplaintsPagerByUser", complaints, pageInfo));
        return new Gson().fromJson(obj.get(RETURN_STR), new TypeToken<PagerControl<UserComplaints>>() {
        }.getType());
    }
}
