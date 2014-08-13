package com.meiliwan.emall.sp2.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.sp2.bean.ExamTopic;
import com.meiliwan.emall.sp2.dto.ExamParams;

/**
 * User: wuzixin
 * Date: 14-4-25
 * Time: 上午10:45
 */
public class ExamClient {

    /**
     * 随机获取一道题目
     */
    public static ExamTopic getTopicByRand() {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("examTopicService/getTopicByRand"));
        return new Gson().fromJson(obj.get("resultObj"), ExamTopic.class);
    }

    /**
     * 根据用户ID和当天时间 查询用户当天答题的机会次数
     *
     * @param uid
     */
    public static int getCountAnswerGroupNum(int uid) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("examTopicService/getCountAnswerGroupNum", uid));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 获取用户答题得到的礼包等级数
     *
     * @param uid
     */
    public static int getExamLevelByUid(int uid) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("examTopicService/getExamResultByUid", uid));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 增加每组答题的操作记录，记录每组答题的数目
     *
     * @param uid
     */
    public static int addExamLog(int uid) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("examTopicService/addExamLog", uid));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 修改对应题目组的状态
     *
     * @param groupId
     * @param uid
     */
    public static int updateGroupState(int groupId, int uid) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("examTopicService/updateGroupState", groupId, uid));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 判断答题是否正确，同时判断是否礼包等级是否增加
     * error表示无法进行下一组答题，next 表示可以进行继续答题，ok表示已经答完三道题目
     * 如果返回是ok，那么直接判断是否该用户的礼包已经大于3，大于三则不曾经，否则增加
     *
     * @param params
     * @return error表示无法进行下一组答题，next 表示可以进行继续答题，ok表示已经答完三道题目
     */
    public static String checkAnswerResult(ExamParams params) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("examTopicService/checkAnswerResult", params));
        return obj.get("resultObj").getAsString();
    }

    /**
     * 导出用户获取礼包等级数
     * @return
     */
    public static boolean exportUserExamLevel(){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("examTopicService/exportUserExamLevel"));
        return obj.get("resultObj").getAsBoolean();
    }

}
