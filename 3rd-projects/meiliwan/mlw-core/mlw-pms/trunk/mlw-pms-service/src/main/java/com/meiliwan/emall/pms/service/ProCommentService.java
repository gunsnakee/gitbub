package com.meiliwan.emall.pms.service;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.RedisSearchInfoUtil;
import com.meiliwan.emall.pms.bean.ProComment;
import com.meiliwan.emall.pms.bean.ProCommentClick;
import com.meiliwan.emall.pms.bean.ProCommentDv;
import com.meiliwan.emall.pms.constant.Constant;
import com.meiliwan.emall.pms.dao.ProActionDao;
import com.meiliwan.emall.pms.dao.ProCommentClickDao;
import com.meiliwan.emall.pms.dao.ProCommentDao;
import com.meiliwan.emall.pms.dao.ProCommentDvDao;
import com.meiliwan.emall.pms.dto.CommentDTO;
import com.meiliwan.emall.pms.dto.CommentScoreCount;
import com.meiliwan.emall.pms.dto.CommentScoreDTO;
import com.meiliwan.emall.pms.dto.ProCommentPage;
import com.meiliwan.emall.pms.dto.QueryCountsDTO;
import com.meiliwan.emall.pms.util.ActionOpt;
import com.meiliwan.emall.pms.util.CommentOpt;
import com.meiliwan.emall.service.BaseService;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * User: guangdetang
 * Date: 13-6-13
 * Time: 下午4:41
 */
@Service
public class ProCommentService extends DefaultBaseServiceImpl implements BaseService {

    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());
    @Autowired
    private ProCommentDao commentDao;
    @Autowired
    private ProCommentClickDao clickDao;
    @Autowired
    private ProActionDao proActionDao;
    @Autowired
    private ProCommentDvDao proCommentDvDao;
    /**
     * 用户在确认收货的时候，调用此方法
     * @param comment
     */
    @IceServiceMethod
    public void addCommentByOrder(JsonObject resultObj, ProComment comment) {
        if (comment != null && comment.getProId() != null && comment.getUid() != null) {
            ProComment pc = new ProComment();
            pc.setOrderId(comment.getOrderId());
            pc.setState(GlobalNames.STATE_INVALID);
            pc.setUid(comment.getUid());
            pc.setNickName(comment.getNickName());
            pc.setProId(comment.getProId());
            pc.setProName(comment.getProName());
            pc.setSequence(GlobalNames.STATE_INVALID);
            pc.setScore((short) 5);
            pc.setCreateTime(DateUtil.getCurrentTimestamp());
            int result = commentDao.insert(pc);
            addToResult(result > 0 ? true : false, resultObj);
        }
    }

    /**
     * 用户确定评价
     * @param comment
     * @return
     */
    @IceServiceMethod
    public void addCommentByUser(JsonObject resultObj, ProComment comment) {
        if (comment != null) {
            comment.setCommentTime(DateUtil.getCurrentTimestamp());
            comment.setState(GlobalNames.STATE_VALID);
            int result = commentDao.update(comment);
            addToResult(result > 0 ? true : false, resultObj);
        }
    }

    /**
     * 回复评价
     * @param resultObj
     * @param id
     * @param replyContent
     * @return
     */
    @IceServiceMethod
    public void updateReplyComment(JsonObject resultObj, int id, String replyContent) {
        if (id > 0 && replyContent != null) {
            ProComment comment = new ProComment();
            comment.setId(id);
            comment.setReplyContent(replyContent);
            comment.setReplyTime(DateUtil.getCurrentTimestamp());
            int result = commentDao.update(comment);
            addToResult(result > 0 ? true : false, resultObj);
        }
    }

    /**
     * 修改评论内容
     * @param resultObj
     * @param comment
     */
    @IceServiceMethod
    public void updateComment(JsonObject resultObj, ProComment comment) {
    	// 如果传入内容为空且数据库里内容也为空，则使用评论缺省值
        if (comment != null) {
        	if(StringUtils.isBlank(comment.getContent())){
        		ProComment oldComment = commentDao.getEntityById(comment.getId(), true);
        		if(oldComment != null && StringUtils.isBlank(oldComment.getContent())){
        			ProCommentDv qdv = new ProCommentDv();
        	        qdv.setScore("%"+comment.getScore()+"%");
        	        qdv.setState(0);
        	        PageInfo page = new PageInfo();
        	        PagerControl<ProCommentDv> pc = proCommentDvDao.getPagerByObj(qdv, page, "", "", true);
        	        ProCommentDv dv = new ProCommentDv();
        	        if(pc!=null && pc.getEntityList()!=null && pc.getEntityList().size()>0){
        	            dv = pc.getEntityList().get(0);
        	            comment.setContent(!StringUtils.isBlank(dv.getContent()) ? dv.getContent() : "系统默认评价");
        	        }
        		}
        	}
        	
            int result = commentDao.update(comment);
            addToResult(result > 0 ? true : false, resultObj);
        }
    }

    /**
     * 管理员修改评论各种状态
     * @param resultObj
     * @param ids
     * @param handle
     */
    @IceServiceMethod
    public void updateCommentStateByAdmin(JsonObject resultObj, int[] ids, int handle) {
        if (ids != null && ids.length > 0 && handle > 0) {
            for (Integer i : ids) {
                ProComment oldComment = commentDao.getEntityById(i);
                ProComment comment = new ProComment();
                if(oldComment !=null ){
                    comment.setId(i);
                    //暂时修改一下 "0".equals(oldComment.getIsAdminDel()) 的判断 modify by yuxiong 2013-12-27
                    //留待字鑫做确认这样修改判断条件是否OK ？？？
                    if (handle == 1 || handle == 2) {
                        //1 改为未审核通过； 2 改为审核通过
                        comment.setIsWebVisible((short) (handle - 2));
                        if (handle == 1 && "0".equals(Short.toString(oldComment.getIsAdminDel())) && oldComment.getIsWebVisible().intValue()==0) {
                            delUserAction(oldComment);
                        }
                        if (handle == 2 && "0".equals(Short.toString(oldComment.getIsAdminDel())) && oldComment.getIsWebVisible().intValue()!=0) {
                            addUserAction(oldComment);
                        }
                    } else if (handle == 3 || handle == 4) {
                        //修改管理员是否删除：3 改为已删除； 4 改为未删除
                        comment.setIsAdminDel((short) (handle - 4));
                        if (handle == 3 && "0".equals(Short.toString(oldComment.getIsWebVisible())) && "0".equals(Short.toString(oldComment.getIsAdminDel()))) {
                            delUserAction(oldComment);
                        }
                        //这里用"0".equals(Short.toString(oldComment.getIsAdminDel())判断会有bug，该改作-1， modify by yuxiong 2014-1-4
                        if (handle == 4 && "0".equals(Short.toString(oldComment.getIsWebVisible()))  && "-1".equals(Short.toString(oldComment.getIsAdminDel()))) {
                            addUserAction(oldComment);
                        }
                    }  else if (handle == 7 || handle == 8) {
                        //修改是否置顶
                        comment.setSequence((short) (handle - 8));
                    }
                }
                int num = commentDao.update(comment);

            }
            addToResult(true, resultObj);
        }
    }

    //删除用户行为，使平均分合法
    private void delUserAction(ProComment comment) {
        try {
            if (comment != null) {
                int updateNum = 0;
                if (comment.getScore() == 1)
                    updateNum = proActionDao.updateCommentByDelete(comment.getProId(), ActionOpt.ONE_SOCRE_NUN.getCode());
                if (comment.getScore() == 2)
                    updateNum = proActionDao.updateCommentByDelete(comment.getProId(), ActionOpt.TWO_SOCRE_NUM.getCode());
                if (comment.getScore() == 3)
                    updateNum = proActionDao.updateCommentByDelete(comment.getProId(), ActionOpt.THREE_SOCRE_NUM.getCode());
                if (comment.getScore() == 4)
                    updateNum = proActionDao.updateCommentByDelete(comment.getProId(), ActionOpt.FOUR_SOCRE_NUM.getCode());
                if (comment.getScore() == 5)
                    updateNum = proActionDao.updateCommentByDelete(comment.getProId(), ActionOpt.FIVE_SOCRE_NUM.getCode());
                //更新搜索的东西
                if (updateNum>0){
                    RedisSearchInfoUtil.addSearchInfo(comment.getProId());
                }
            }
        } catch (Exception e) {
            logger.error(e, "删除用户评论行为时遇到异常！", null);
        }
    }

    //增加用户行为，使平均分合法
    private void addUserAction(ProComment comment) {
        try {
            if (comment != null) {
                int updateNum = 0;
                if (comment.getScore() == 1)
                    updateNum = proActionDao.updateActionByOpt(comment.getProId(), ActionOpt.ONE_SOCRE_NUN.getCode());
                if (comment.getScore() == 2)
                    updateNum = proActionDao.updateActionByOpt(comment.getProId(), ActionOpt.TWO_SOCRE_NUM.getCode());
                if (comment.getScore() == 3)
                    updateNum = proActionDao.updateActionByOpt(comment.getProId(), ActionOpt.THREE_SOCRE_NUM.getCode());
                if (comment.getScore() == 4)
                    updateNum = proActionDao.updateActionByOpt(comment.getProId(), ActionOpt.FOUR_SOCRE_NUM.getCode());
                if (comment.getScore() == 5)
                    updateNum = proActionDao.updateActionByOpt(comment.getProId(), ActionOpt.FIVE_SOCRE_NUM.getCode());
                 //更新搜索的东西
                if (updateNum>0){
                    RedisSearchInfoUtil.addSearchInfo(comment.getProId());
                }
            }
        } catch (Exception e) {
            logger.error(e, "增加用户评论行为时遇到异常！", null);
        }
    }
    /**
     * 通过id获得评价详情
     * @param id
     * @return
     */
    @IceServiceMethod
    public void getCommentById(JsonObject resultObj, int id) {
        if (id > 0) {
            addToResult(commentDao.getEntityById(id), resultObj);
        }
    }

    /**
     * 获得订单下的评价列表
     * @param resultObj
     * @param orderId
     */
    @IceServiceMethod
    public void getCommentListByOrder(JsonObject resultObj, String orderId) {
        if (!Strings.isNullOrEmpty(orderId)) {
            ProComment comment = new ProComment();
            comment.setOrderId(orderId);
            comment.setIsAdminDel(GlobalNames.STATE_VALID);
            comment.setIsUserDel(GlobalNames.STATE_VALID);
            addToResult(commentDao.getListByObj(comment, "", "order by create_time desc"), resultObj);
        }
    }

    /**
     * 评价管理列表（个人中心）
     * @param pageInfo
     * @param userId
     * @param resultObj
     * @return
     */
    @IceServiceMethod
    public void getCommentListByUserId(JsonObject resultObj, PageInfo pageInfo, int userId, short flag) {
        if (userId > 0 && pageInfo != null) {
            ProComment comment = new ProComment();
            comment.setUid(userId);
            comment.setIsAdminDel(GlobalNames.STATE_VALID);
            comment.setIsUserDel(GlobalNames.STATE_VALID);
            comment.setState(flag);
            addToResult(commentDao.getPagerByObj(comment, pageInfo, "", "order by comment_time desc, create_time desc"), resultObj);
        }
    }

    /**
     * 1.商品详情页查询出评论分页列表<br/>
     * 2.针对score（评分），条件查询score>=4的评论<br/>
     * @param proId 产品ID<br/>
     * @param flag  好评3、中平2、差评1
     */
    @IceServiceMethod
    public void getCommentList(JsonObject resultObj, PageInfo pageInfo, int proId, int flag, String orderBy) {
        if (pageInfo != null && proId > 0) {
            ProComment comment = new ProComment();
            StringBuilder whereSql = new StringBuilder();
            comment.setProId(proId);
            comment.setIsAdminDel(GlobalNames.STATE_VALID);
            comment.setState(GlobalNames.STATE_VALID);
            comment.setIsWebVisible(GlobalNames.STATE_VALID);
            if (flag == 1) {
                if (!Strings.isNullOrEmpty(whereSql.toString())) {
                    whereSql.append(" and");
                }
                whereSql.append(" (score=1 or score=2)");
            } else if (flag == 2) {
                if (!Strings.isNullOrEmpty(whereSql.toString())) {
                    whereSql.append(" and");
                }
                whereSql.append(" (score=3)");
            } else if (flag == 3) {
                if (!Strings.isNullOrEmpty(whereSql.toString())) {
                    whereSql.append(" and");
                }
                whereSql.append(" (score=4 or score=5)");
            }
            addToResult(commentDao.getPagerByObj(comment, pageInfo, whereSql.toString(), "order by sequence desc, " + commentDao.isExistDBField(orderBy) + " desc"), resultObj);
        }
    }


    /**
     * 获得评价分页列表（前后台）
     * @param view
     * @param pageInfo
     */
    @IceServiceMethod
    public void getCommentPagerAdmin(JsonObject resultObj, CommentDTO view, PageInfo pageInfo) {
        if (view != null && pageInfo != null) {
            if (view.getNickName() != null && !Strings.isNullOrEmpty(view.getNickName())) {
                view.setNickName("%" + view.getNickName() + "%");
            } else {
                view.setNickName(null);
            }
            if (view.getProName() != null && !Strings.isNullOrEmpty(view.getProName())) {
                view.setProName("%" + view.getProName() + "%");
            } else {
                view.setProName(null);
            }
            if (view.getOrderId() != null && !Strings.isNullOrEmpty(view.getOrderId().trim())) {
                view.setOrderId(view.getOrderId());
            } else {
                view.setOrderId(null);
            }
            if (view.getStartTime() != null && !Strings.isNullOrEmpty(view.getStartTime().trim())) {
                view.setStartTime(view.getStartTime());
            } else {
                view.setStartTime(null);
            }
            if (view.getEndTime() != null && !Strings.isNullOrEmpty(view.getEndTime().trim())) {
                view.setEndTime(view.getEndTime());
            } else {
                view.setEndTime(null);
            }
            if (view.getContent() != null && !Strings.isNullOrEmpty(view.getContent().trim())) {
                view.setContent("" + Constant.COMMENT_CONTENT_LENGTH);
            } else {
                view.setContent(null);
            }
            if (view.getIsReply() != null && !Strings.isNullOrEmpty(view.getIsReply().trim())) {
                //防止前台传（1，1）数组
                if (view.getIsReply().equals("1,1")) {
                    view.setIsReply("1");
                } else if (view.getIsReply().equals("2,2")) {
                    view.setIsReply("2");
                } else {
                    view.setIsReply(view.getIsReply());
                }
            } else {
                view.setIsReply(null);
            }
            addToResult(commentDao.getPagerByCommentView(view, pageInfo), resultObj);
        }
    }

    /**
     * 只为后台查询
     */
    public void getPagerForBkstage(JsonObject resultObj, CommentDTO view, PageInfo pageInfo) {
        if (view != null && pageInfo != null) {
            if (view.getNickName() != null && !Strings.isNullOrEmpty(view.getNickName())) {
                view.setNickName("%" + view.getNickName() + "%");
            } else {
                view.setNickName(null);
            }
            if (view.getProName() != null && !Strings.isNullOrEmpty(view.getProName())) {
                view.setProName("%" + view.getProName() + "%");
            } else {
                view.setProName(null);
            }
            if (view.getOrderId() != null && !Strings.isNullOrEmpty(view.getOrderId().trim())) {
                view.setOrderId(view.getOrderId());
            } else {
                view.setOrderId(null);
            }
            if (view.getStartTime() != null && !Strings.isNullOrEmpty(view.getStartTime().trim())) {
                view.setStartTime(view.getStartTime());
            } else {
                view.setStartTime(null);
            }
            if (view.getEndTime() != null && !Strings.isNullOrEmpty(view.getEndTime().trim())) {
                view.setEndTime(view.getEndTime());
            } else {
                view.setEndTime(null);
            }
            if (view.getContent() != null && !Strings.isNullOrEmpty(view.getContent().trim())) {
                view.setContent("" + Constant.COMMENT_CONTENT_LENGTH);
            } else {
                view.setContent(null);
            }
            if (view.getIsReply() != null && !Strings.isNullOrEmpty(view.getIsReply().trim())) {
                if (view.getIsReply().equals("1,1")) {
                    view.setIsReply("1");
                } else if (view.getIsReply().equals("2,2")) {
                    view.setIsReply("2");
                } else {
                    view.setIsReply(view.getIsReply());
                }
            } else {
                view.setIsReply(null);
            }
            PagerControl<ProCommentPage> pc = commentDao.getPagerForBkstage(view, pageInfo);
            addToResult(pc, resultObj);
        }
    }
    
    /**
     * 有用无用是否已经点击
     * @param resultObj
     * @param cid
     * @param uid
     */
    public void isHasClick(JsonObject resultObj, int cid, int uid) {
        if (cid > 0 && uid > 0) {
            ProCommentClick click = new ProCommentClick();
            click.setCommentId(cid);
            click.setUid(uid);
            List<ProCommentClick> listPCC = clickDao.getListByObj(click, null);
            addToResult(listPCC != null && listPCC.size() > 0 ? true : false, resultObj);
        }
    }

    /**
     * 点击有用、无用
     * @param resultObj
     * @param click
     */
    public void addIsUseClick(JsonObject resultObj, ProCommentClick click) {
        if (click != null) {
            click.setCreateTime(DateUtil.getCurrentTimestamp());
            int result = clickDao.insert(click);
            int isSu = -1;
            if (result > 0) {
                ProComment proComment = commentDao.getEntityById(click.getCommentId());
                if (proComment != null) {
                    ProComment comment = new ProComment();
                    if (click.getClickType() == Constant.COMMENT_USEFUL) {
                        comment.setUsefulCount(proComment.getUsefulCount() + 1);
                    } else if (click.getClickType() == Constant.COMMENT_USELESS) {
                        comment.setUselessCount(proComment.getUselessCount() + 1);
                    }
                    isSu = commentDao.update(comment);
                }
            }
            addToResult(isSu > 0 ? true : false, resultObj);
        }
    }

    /**
     * 根据时间查询前五条评论
     * @param resultObj
     * @param proId
     */
    public void getFiveComments(JsonObject resultObj, int proId) {
        if (proId > 0) {
            ProComment comment = new ProComment();
            comment.setProId(proId);
            comment.setState((short) 0);
            comment.setIsAdminDel(GlobalNames.STATE_VALID);
            comment.setIsWebVisible(GlobalNames.STATE_VALID);
            addToResult(commentDao.getListByObj(comment, new PageInfo(1, 5), null, "order by create_time desc"), resultObj);
        }
    }

    /**
     * 校验用户对某个商品的评论情况
     * 返回内容：unbuy 未购买该商品
     * commentOver （有购买）评论完毕
     * uncomment （有购买）未评论完
     * @param resultObj
     * @param proId
     * @param uid
     */
    public void checkComment(JsonObject resultObj, int proId, int uid) {
        ProComment entity = new ProComment();
        entity.setProId(proId);
        entity.setUid(uid);
        String result = "unbuy";
        int num = commentDao.getCountByObj(entity);
        if (num > 0) {
            entity.setState(Constant.COMMENT_IS_COMMENT_NO);
            num = commentDao.getCountByObj(entity);
            if (num > 0) {
                result = "uncomment";
            } else {
                result = "commentOver";
            }
        }
        addToResult(result, resultObj);
    }

    /**
     * 取得用户的待评价订单数量
     * @param resultObj
     * @param uid
     */
    public void getCountNotComment(JsonObject resultObj, int uid) {
        ProComment entity = new ProComment();
        entity.setUid(uid);
        entity.setState(GlobalNames.STATE_INVALID);
        if (uid < 0) {
            throw new ServiceException("ProCommentService-getCountNotCommentUser {} ", uid + "");
        }
        int count = commentDao.getCountByObj(entity);
        addToResult(count, resultObj);
    }

    /**
     * 根据字段修改相应的字段
     * 有用和无用功能：修改相关的数量等
     */
    public void updateComByParam(JsonObject resultObj, int comId, CommentOpt param) {
        addToResult(commentDao.updateComByParam(comId, param.getCode()), resultObj);
    }

    /**
     * 通过商品IDs获取评论列表，每个商品至多查三个好评
     * @param ids
     */
    public void getListByProIds(JsonObject resultObj, Integer[] ids) {
        addToResult(commentDao.getListByProIds(ids), resultObj);
    }


    /**
     * 增加虚假评论
     * @param comment
     */
    public void addShamComment(JsonObject resultObj, ProComment comment) {
        if (comment != null && comment.getProId() != null && comment.getUid() != null) {
            comment.setState(GlobalNames.STATE_VALID);
            comment.setIsAdminDel(GlobalNames.STATE_VALID);
            comment.setIsWebVisible(GlobalNames.STATE_VALID);
            comment.setCreateTime(DateUtil.getCurrentTimestamp());
            int result = commentDao.insert(comment);
            addToResult(result > 0 ? true : false, resultObj);
        }
    }

    /**
     * 商品详情页用到，查询商品虚假评论
     * @param resultObj
     * @param proId     商品ID
     * @param count     需要查询的条数
     */
    public void getShamCommentByProId(JsonObject resultObj, int proId, int count) {
        if (proId > 0 && count > 0) {
            ProComment comment = new ProComment();
            comment.setProId(proId);
            comment.setOrderId(GlobalNames.COMMENT_ORDERID);
            comment.setIsAdminDel(GlobalNames.STATE_VALID);
            comment.setState(GlobalNames.STATE_VALID);
            comment.setIsWebVisible(GlobalNames.STATE_VALID);
            addToResult(commentDao.getListByObj(comment, new PageInfo(1, count), "", "order by create_time desc"), resultObj);
        }
    }

    /**
     * 获得不同评分类型的条数
     * @param resultObj
     * @param proId
     */
    @IceServiceMethod
    public void getCountsGroupByScore(JsonObject resultObj, int proId) {
        ProComment comment = new ProComment();
        comment.setProId(proId);
        comment.setState(GlobalNames.STATE_VALID);
        comment.setIsWebVisible(GlobalNames.STATE_VALID);
        comment.setIsAdminDel(GlobalNames.STATE_VALID);
        List<QueryCountsDTO> queryList = commentDao.getCountsGroupByScore(comment, " group by score");
        CommentScoreCount csc = new CommentScoreCount();
        if (queryList != null && queryList.size() > 0) {
            List<CommentScoreDTO> listCS = new ArrayList<CommentScoreDTO>();
            int totalCounts = 0;
            for (QueryCountsDTO dtoList : queryList) {
                CommentScoreDTO dto = new CommentScoreDTO();
                /** 评论分数类型（1好评(4,5)，2中评(3)，3差评(1,2)）*/
                int scoreType = 3 ;
                if(dtoList.getKeyType() == 4 || dtoList.getKeyType() == 5){
                	scoreType = 1 ;
                }
                if(dtoList.getKeyType() == 3){
                	scoreType = 2 ;
                }
                dto.setScoreType(scoreType);
                dto.setCounts(dtoList.getCounts());
                listCS.add(dto);
                totalCounts += dtoList.getCounts();
            }
            csc.setScoreDTOList(listCS);
            csc.setTotalCount(totalCounts);
        }
        addToResult(csc, resultObj);
    }

    @IceServiceMethod
    public void getOrderIsCommentListByOrderCenter(JsonObject resultObj, String[] orderIds) {
        List<String> queryList = commentDao.getOrderIsCommentListByOrderCenter(orderIds);
        addToResult(queryList, resultObj);
    }

    @IceServiceMethod
    public void getOrderAllCommentListByOrderCenter(JsonObject resultObj, String[] orderIds) {
        List queryList = commentDao.getOrderAllCommentListByOrderCenter(orderIds);
        addToResult(queryList, resultObj);
    }

    @IceServiceMethod
     public void addCommentDvByAdmin(JsonObject resultObj, ProCommentDv dv) {
        if (dv != null && !Strings.isNullOrEmpty(dv.getContent()) && !Strings.isNullOrEmpty(dv.getScore())) {
            dv.setState((int)GlobalNames.STATE_VALID);
            dv.setCreateTime(DateUtil.getCurrentTimestamp());
            int result = proCommentDvDao.insert(dv);
            addToResult(result > 0 ? true : false, resultObj);
        } else {
            addToResult(false, resultObj);
        }
    }

    @IceServiceMethod
    public void getCommentDvById(JsonObject resultObj, int id) {
        addToResult(proCommentDvDao.getEntityById(id), resultObj);
    }

    @IceServiceMethod
    public void updateCommentDvByAdmin(JsonObject resultObj, ProCommentDv dv) {
        if (dv != null) {
            int result = proCommentDvDao.update(dv);
            addToResult(result > 0 ? true : false, resultObj);
        } else {
            addToResult(false, resultObj);
        }
    }

    @IceServiceMethod
    public void getCommentDvListByAdmin(JsonObject resultObj, ProCommentDv dv, PageInfo info){
        dv.setState((int)GlobalNames.STATE_VALID);
        addToResult(proCommentDvDao.getPagerByObj(dv, info, "", "order by create_time desc"), resultObj);
    }

    /**
     * 根据评分获取缺省评论
     * @param resultObj
     * @param score
     */
    @IceServiceMethod
    public void  getCommentDvByScore(JsonObject resultObj,int score){
        ProCommentDv qdv = new ProCommentDv();
        qdv.setScore("%"+score+"%");
        qdv.setState(0);
        PageInfo page = new PageInfo();
        PagerControl<ProCommentDv> pc = proCommentDvDao.getPagerByObj(qdv, page, "", "");
        ProCommentDv dv = new ProCommentDv();
        if(pc!=null && pc.getEntityList()!=null && pc.getEntityList().size()>0){
            dv = pc.getEntityList().get(0);
        }
        addToResult(dv,resultObj);
    }
}
