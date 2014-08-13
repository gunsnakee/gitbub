package com.meiliwan.emall.mms.client;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.mms.bean.UserStationMsg;
import com.meiliwan.emall.mms.constant.Constant;
import com.meiliwan.emall.service.BaseService;

import java.util.List;

/**
 * 站内信 client
 * Created by yiyou.luo on 13-6-13.
 */
public class UserStationMsgClient {
    private static final String SERVICE_NAME = "userStationMsgService";

    public static void main(String args[]) {
       // Object[] uids = {1, 2};
      //  saveSendMsgs(1, uids, "test");
        // 1、通过id获取 站内信 实例
        /*UserStationMsg msg = new UserStationMsg();
        msg.setAdminId(110);
        msg.setAdminName("system");
        msg.setContent("系统来信：您的账户有积分派送，请查收！");
        msg.setNickName("邓肯");
        msg.setUid(10000);
        msg.setMsgType((short)1);*/
        // int id = saveUserStationMsg(msg);

        // 2。通过 用户id获取 站内信 list
       /* UserStationMsg gmsg = getUserStationMsgById(id);
        System.out.print(gmsg.getAdminId()+" "+gmsg.getAdminName()+" 说："+gmsg.getNickName()+" "+ gmsg.getContent());
*/
        //3。修改 站内信
       /* gmsg.setContent("再次说："+gmsg.getContent());
        System.out.print(updateUserStationMsg(gmsg));*/

        //4.通过 用户id获取 站内信 list
        List<UserStationMsg> list = getUserStationMsgByUId(3);
        System.out.print("list.size(0="+list.size());
        setIsUserReadByList(list);

        //5.
        //  PageInfo pageInfo  = new PageInfo();

        //   getUserStationMsgPaperByObj(msg,pageInfo);

        // 1、通过id获取 站内信 实例
        /*UserStationMsg msg = new UserStationMsg();
        msg.setAdminId(110);
        msg.setAdminName("system");
        msg.setContent("系统来信：您的账户有积分派送，请查收！");
        msg.setNickName("邓肯");
        msg.setUid(10000);
        msg.setMsgType((short) 0); //公告
        msg.setPublicMsgid(123456778);  //公告id*/
        //  int id = saveUserStationMsg(msg);

        //2、
        //  System.out.println("公告列表条数："+ getUserPublicMsg(123456778,10000).size());
       // setIsUserReadByList(getUserStationMsgByUId(12313));


    }

    /**
     * 通过id获取 站内信 实例
     * @param id
     */
    public static UserStationMsg getUserStationMsgById(int id) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getUserStationMsgById", id));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<UserStationMsg>() {
        }.getType());
    }

    /**
     * 通过 用户id获取 站内信 list
     * @param uid
     */
    public static List<UserStationMsg> getUserStationMsgByUId(int uid) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getUserStationMsgByUId", uid));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<UserStationMsg>>() {
        }.getType());
    }

    /**
     * 添加 站内信
     * @param userStationMsg
     */
    public static int saveUserStationMsg(UserStationMsg userStationMsg) {
        if (userStationMsg != null) {
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                    JSONTool.buildParams(SERVICE_NAME + "/addUserStationMsg", userStationMsg));
            return Integer.parseInt(obj.get("resultObj").getAsString());
        } else {
            return -1;
        }
    }

    /**
     * 管理员发送站内信
     * @param sendUid
     * @param receiveUids
     * @param content
     */
    public static boolean saveSendMsgs(int sendUid, String adminName, Object receiveUids, String content) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/addSendMsgs", sendUid, adminName, receiveUids, content));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 修改 站内信
     * @param userStationMsg
     */
    public static boolean updateUserStationMsg(UserStationMsg userStationMsg) {
        if (userStationMsg != null) {
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                    JSONTool.buildParams(SERVICE_NAME + "/updateUserStationMsg", userStationMsg));
            return obj.get("resultObj") == null ? false : obj.get("resultObj").getAsBoolean();
        } else {
            return false;
        }
    }

    /**
     * 通过  /**
     * 通过 站内信 实体参数获取对应的实体列表包含物理分页
     * @param pageInfo
     */
    public static PagerControl getUserStationMsgPaperByObj(UserStationMsg userStationMsg, PageInfo pageInfo) {
        if (userStationMsg != null && pageInfo != null) {
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                    JSONTool.buildParams(SERVICE_NAME + "/getUserStationMsgPaperByObj", userStationMsg, pageInfo));
            return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<UserStationMsg>>() {
            }.getType());
        } else {
            return null;
        }
    }

    /**
     * 获取某个用户的某个公告
     * @param pubicMsgid
     * @param uid
     */
    public static List<UserStationMsg> getUserPublicMsg(Integer pubicMsgid, Integer uid) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getUserPublicMsg", pubicMsgid, uid));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<UserStationMsg>>() {
        }.getType());
    }

    /**
     * 通过  /**
     * 通过 获取用户站内信数量
     * @param userStationMsg
     */
    public static int getUserStationMsgNumByObj(UserStationMsg userStationMsg) {
        if (userStationMsg != null) {
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                    JSONTool.buildParams(SERVICE_NAME + "/getUserStationMsgNumByObj", userStationMsg));
            return obj.get("resultObj") == null ? 0 : obj.get("resultObj").getAsInt();
        }
        return 0;
    }

    /**
     * 设置已读
     * @param list
     * @return
     */
    public static void setIsUserReadByList(List<UserStationMsg> list) {
        if (list != null && list.size() > 0) {
            for (UserStationMsg msg : list) {
                if (msg.getIsUserRead().equals(Constant.USER_STATIONMSG_ISREAD_NO)) {
                    UserStationMsg stationMsg = new UserStationMsg();
                    stationMsg.setId(msg.getId());
                    stationMsg.setIsUserRead(Constant.USER_STATIONMSG_ISREAD_YES);
                    JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                            JSONTool.buildParams(SERVICE_NAME + "/updateUserStationMsg", stationMsg));
                }
            }
        }
    }
}
