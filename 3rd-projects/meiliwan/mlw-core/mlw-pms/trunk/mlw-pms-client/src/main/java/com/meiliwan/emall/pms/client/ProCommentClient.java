package com.meiliwan.emall.pms.client;

import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.pms.bean.ProComment;
import com.meiliwan.emall.pms.bean.ProCommentClick;
import com.meiliwan.emall.pms.bean.ProCommentDv;
import com.meiliwan.emall.pms.bean.ProCommentVo;
import com.meiliwan.emall.pms.dto.CommentDTO;
import com.meiliwan.emall.pms.dto.CommentScoreCount;
import com.meiliwan.emall.pms.dto.ProCommentPage;
import com.meiliwan.emall.pms.util.CommentOpt;
import com.meiliwan.emall.service.BaseService;

/**
 * User: guangdetang
 * Date: 13-6-13
 * Time: 下午5:55
 */
public class ProCommentClient {

    private static final String SERVICE_NAME = "proCommentService";
    private static final String GETCOUNTNOTCOMMENT = "proCommentService/getCountNotComment";
    private static final String addShamComment = "proCommentService/addShamComment";

    /**
     * 用户在确认收货的时候，调用此方法
     *
     * @param comment
     */
    public static boolean addCommentByOrder(ProComment comment) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/addCommentByOrder", comment));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }


    /**
     * 用户确定评价
     *
     * @param comment
     * @return
     */
    public static boolean addCommentByUser(ProComment comment) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/addCommentByUser", comment));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 回复评价
     *
     * @param id
     * @param replyContent
     * @return
     */
    public static boolean replyComment(int id, String replyContent) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/updateReplyComment", id, replyContent));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 修改评价
     *
     * @param comment
     * @return
     */
    public static boolean updateComment(ProComment comment) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/updateComment", comment));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 管理员修改评价各种状态
     *
     * @param ids
     * @return
     */
    public static boolean updateCommentStateByAdmin(int[] ids, int handle) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/updateCommentStateByAdmin", ids, handle));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 获得一条评价
     *
     * @param id
     * @return
     */
    public static ProComment getCommentById(int id) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getCommentById", id));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), ProComment.class);
    }

    /**
     * 通过订单获得订单下的评价列表
     *
     * @param orderId
     * @return
     */
    public static List<ProComment> getCommentListByOrder(String orderId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getCommentListByOrder", orderId));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<ProComment>>() {
        }.getType());
    }


    /**
     * 获得后台评价分页列表
     *
     * @param view
     * @param pageInfo
     * @return
     */
    public static PagerControl<ProComment> getCommentAdminPager(CommentDTO view, PageInfo pageInfo) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getCommentPagerAdmin", view, pageInfo));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<PagerControl<ProComment>>() {
        }.getType());
    }

    
    /**
     * 获得后台评价分页列表
     *
     * @param view
     * @param pageInfo
     * @return
     */
    public static PagerControl<ProCommentPage> getPagerForBkstage(CommentDTO view, PageInfo pageInfo) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getPagerForBkstage", view, pageInfo));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<PagerControl<ProCommentPage>>() {
        }.getType());
    }
    /**
     * 个人中心查询评价分页列表
     *
     * @param pageInfo
     * @param userId
     * @param state    是否已评价
     * @return
     */
    public static PagerControl<ProComment> getCommentListByUserId(PageInfo pageInfo, int userId, short state) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getCommentListByUserId", pageInfo, userId, state));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<PagerControl<ProComment>>() {
        }.getType());
    }

    /**
     * 商品详情页查询出评论分页列表<br/>
     *
     * @param proId 产品ID<br/>
     * @param flag  好评3、中平2、差评1
     */
    public static PagerControl<ProComment> getCommentList(PageInfo pageInfo, int proId, int flag, String orderBy) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getCommentList", pageInfo, proId, flag, orderBy));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<PagerControl<ProComment>>() {
        }.getType());
    }

    /**
     * 记录用户点击有用无用,同时增加评论表中有用无用点击次数
     * 前置条件：判断用户未点击过该评论
     *
     * @param click
     * @return
     */
    public static boolean addUsefulClick(ProCommentClick click) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/addUsefulClick", click));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 有用无用是否已经点击
     *
     * @param cid
     * @param uid
     * @return true已点击，false未点击
     */
    public static boolean isHasClick(int cid, int uid) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/isHasClick", cid, uid));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 查询最新的5条评论
     *
     * @param proId
     */
    public static List<ProComment> getFlontFiveComments(int proId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getFiveComments", proId));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<ProComment>>() {
        }.getType());
    }

    /**
     * 校验用户对某个商品的评论情况
     * 返回内容：unbuy 未购买该商品
     * commentOver （有购买）评论完毕
     * uncomment （有购买）未评论完
     *
     * @param proId
     * @param uid
     */
    public static String checkComment(int proId, int uid) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/checkComment", proId, uid));
        return obj.get(BaseService.RESULT_OBJ) == null ? "unbuy" : obj.get(BaseService.RESULT_OBJ).getAsString();
    }

    /**
     * 用户取得未评价的数量
     *
     * @param uid
     */
    public static int getCountNotComment(int uid) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(GETCOUNTNOTCOMMENT, uid));
        return obj.get(BaseService.RESULT_OBJ).getAsInt();
    }

    /**
     * 根据字段修改对应的数据
     *
     * @param comId
     * @param param
     * @return
     */
    public static int updateComByParam(int comId, CommentOpt param) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/updateComByParam", comId, param));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 通过商品IDs获取评论列表，每个商品至多查三个好评
     *
     * @param ids
     */
    public static List<ProCommentVo> getListByProIds(List<Integer> ids) throws JedisClientException {

        Integer[] proIds = new Integer[ids.size()];
        proIds = ids.toArray(proIds);
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildArrayParams(SERVICE_NAME + "/getListByProIds", proIds));
        List<ProCommentVo> list = new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<ProCommentVo>>() {
        }.getType());

        return list;
    }

    /**
     * 管理员添加虚假评论
     *
     * @param comment
     */
    public static boolean addShamComment(ProComment comment) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(addShamComment, comment));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 商品详情页用到，查询商品虚假评论
     *
     * @param proId 商品ID
     * @param count 需要查询的条数
     */
    public static List<ProComment> getShamCommentByProId(int proId, int count) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getShamCommentByProId", proId, count));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<ProComment>>() {
        }.getType());
    }

    /**
     * 使用场景：商品详情页展示好评中评差评条数
     * 说明：获得不同评论分数的评论条数
     * @param proId
     * @return CommentScoreCount：总评论数和每个分数的评论条数列表
     */
    public static CommentScoreCount getCountsGroupByScore(int proId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getCountsGroupByScore", proId));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), CommentScoreCount.class);
    }

    /**
     * 根据订单IDS获取未评价的订单号列表
     * 使用场景：个人中心订单列表，查询未评价的订单
     * @param orderIds
     * @return
     */
    public static List<String> getOrderIsCommentListByOrderCenter(String[] orderIds){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildArrayParams(SERVICE_NAME + "/getOrderIsCommentListByOrderCenter", orderIds));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<String>>() {
        }.getType());
    }

    /**
     * 根据订单IDS获取已评价的订单号列表及其已评价数量
     * 使用场景：个人中心订单列表，查询已评价的订单
     * @param orderIds
     * @return
     */
    public static List getOrderAllCommentListByOrderCenter(String[] orderIds){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildArrayParams(SERVICE_NAME + "/getOrderAllCommentListByOrderCenter", orderIds));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List>() {}.getType());
    }

    public static boolean addCommentDvByAdmin(ProCommentDv dv) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/addCommentDvByAdmin", dv));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    public static boolean updateCommentDvByAdmin(ProCommentDv dv) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/updateCommentDvByAdmin", dv));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    public static PagerControl<ProCommentDv> getCommentDvListByAdmin(ProCommentDv dv, PageInfo info){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getCommentDvListByAdmin", dv, info));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<PagerControl<ProCommentDv>>() {
        }.getType());
    }

    public static ProCommentDv getCommentDvById(int id) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getCommentDvById", id));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), ProCommentDv.class);
    }

    public static ProCommentDv getCommentDvByScore(int score) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getCommentDvByScore", score));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), ProCommentDv.class);
    }
}
