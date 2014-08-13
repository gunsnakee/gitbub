package com.meiliwan.emall.bkstage.web.controller.pms;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.web.TextUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.mms.bean.UserPassport;
import com.meiliwan.emall.mms.client.UserPassportClient;
import com.meiliwan.emall.pms.bean.ProComment;
import com.meiliwan.emall.pms.bean.ProCommentDv;
import com.meiliwan.emall.pms.bean.SimpleProduct;
import com.meiliwan.emall.pms.client.ProActionClient;
import com.meiliwan.emall.pms.client.ProCommentClient;
import com.meiliwan.emall.pms.client.ProProductClient;
import com.meiliwan.emall.pms.constant.Constant;
import com.meiliwan.emall.pms.dto.CommentDTO;
import com.meiliwan.emall.pms.dto.ProCommentPage;
import com.meiliwan.emall.pms.util.ActionOpt;

/**
 * User: guangdetang
 * Date: 13-6-13
 * Time: 上午10:07
 */
@Controller("commentController")
@RequestMapping("/pms/comment")
public class CommentController {

    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    /**
     * 管理员添加评论
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/add")
    public String add(Model model, HttpServletRequest request,HttpServletResponse response) {
    		ProComment comment = new ProComment();
        try {
        	int handler = ServletRequestUtils.getIntParameter(request, "handler", 0);
        	int proId = ServletRequestUtils.getIntParameter(request, "proId", 0);
        	if(handler==0){
        			UserPassport user = UserPassportClient.getRandomShamUser();
        			model.addAttribute("user", user);
    				model.addAttribute("proId", proId);
        			return "/pms/comment/add";
        	}
			int uid = ServletRequestUtils.getIntParameter(request, "uid", 0);
			
			int score = ServletRequestUtils.getIntParameter(request, "score", 0);
			String nickName = ServletRequestUtils.getStringParameter(request, "nickName", "");
			String content = ServletRequestUtils.getStringParameter(request, "content", "");
			String time = ServletRequestUtils.getStringParameter(request, "time", "");
			int systemTime = ServletRequestUtils.getIntParameter(request, "systemTime", 0);
			if(uid<=0||proId<=0||score<=0||content.length()<10){
				throw new RuntimeException("参数不正确 uid"+uid+"proId"+proId+"score"+score+"content"+content);
			}
			
			SimpleProduct pro = ProProductClient.getProductById(proId);
			
			
			comment.setUid(uid);
			comment.setProId(proId);
			comment.setContent(content);
		    comment.setScore((short) score);
		    comment.setNickName(nickName);
		    comment.setProName(pro.getProName());
		    comment.setOrderId(GlobalNames.COMMENT_ORDERID);
		    setProductScore(proId, score);
			comment.setState(Constant.COMMENT_IS_COMMENT_YES);
			if(systemTime==1){
				comment.setCommentTime(DateUtil.getCurrentTimestamp());
			}else{
				Timestamp date = DateUtil.parseTimestamp(time);
				comment.setCommentTime(date);
			}
			ProCommentClient.addShamComment(comment);
			return StageHelper.dwzSuccessForward("添加评论成功","","",response);
		} catch (Exception e) {
            logger.error(e, comment, WebUtils.getIpAddr(request));
			return StageHelper.dwzFailForward("添加评论失败","","",response);
		}
        
    }
    
    //给商品表打分
    private void setProductScore(int proId, int score) {
        if (proId>0) {
            if (score == 1)
                ProActionClient.updateActionByOpt(proId, ActionOpt.ONE_SOCRE_NUN);
            if (score == 2)
                ProActionClient.updateActionByOpt(proId, ActionOpt.TWO_SOCRE_NUM);
            if (score == 3)
                ProActionClient.updateActionByOpt(proId, ActionOpt.THREE_SOCRE_NUM);
            if (score == 4)
                ProActionClient.updateActionByOpt(proId, ActionOpt.FOUR_SOCRE_NUM);
            if (score == 5)
                ProActionClient.updateActionByOpt(proId, ActionOpt.FIVE_SOCRE_NUM);
        }
    }
    
    /**
     * 评价列表
     * @param model
     * @param commentView
     * @param request
     * @return
     */
    @RequestMapping(value = "/list")
    public String list(Model model, @ModelAttribute CommentDTO commentView, HttpServletRequest request) {
        commentView.setState((short)0);
        String divIndex = ServletRequestUtils.getStringParameter(request, "divIndex", "0");
        String isReply = ServletRequestUtils.getStringParameter(request, "isReply", "1"); //默认为1 表示默认打开已回复tab
        if("0".equals(divIndex)){   //默认打开“已回复tab”
            commentView.setIsReply(isReply);    //控制mapper查询条件
        }
        //处理更新操作后dwz自带的请求参数重复问题
        commentView.setOrderId(ServletRequestUtils.getStringParameter(request, "orderId", null));
        commentView.setNickName(ServletRequestUtils.getStringParameter(request, "nickName", null));
        commentView.setProName(ServletRequestUtils.getStringParameter(request, "proName", null));
        commentView.setStartTime(ServletRequestUtils.getStringParameter(request, "startTime", null));
        commentView.setEndTime(ServletRequestUtils.getStringParameter(request, "endTime", null));
        commentView.setContent(ServletRequestUtils.getStringParameter(request, "content", null));
        commentView.setIsReply(ServletRequestUtils.getStringParameter(request, "isReply", null));

        PagerControl<ProCommentPage> pc = null;
        if("2".equals(isReply) || "3".equals(divIndex)){  // 未回复Tab && 未审核：默认按照评价时间旧-新排序
            pc = ProCommentClient.getPagerForBkstage(commentView, StageHelper.getPageInfo(request, "comment_time", "asc"));
        }else{
            pc = ProCommentClient.getPagerForBkstage(commentView, StageHelper.getPageInfo(request, "comment_time", "desc"));
        }


        String state = ServletRequestUtils.getStringParameter(request, "state", "");
        if (!Strings.isNullOrEmpty(state)) {
            commentView.setState(Short.valueOf(state));
        }
        model.addAttribute("commentView", commentView);
        model.addAttribute("divIndex", divIndex);
        model.addAttribute("pc", pc);
        return "/pms/comment/list";
    }

    /**
     * 修改评价状态
     * @param request
     * @param response
     */
    @RequestMapping(value = "/update")
    public void update(HttpServletRequest request, HttpServletResponse response) {
        try {
            int handle = ServletRequestUtils.getIntParameter(request, "handle", -1);
            int[] ids = ServletRequestUtils.getIntParameters(request, "ids");
            boolean isSuc = ProCommentClient.updateCommentStateByAdmin(ids, handle);
            StageHelper.writeDwzSignal(isSuc ? "200" : "300", isSuc ? "操作成功" : "操作失败", "135", StageHelper.DWZ_FORWARD, "/pms/comment/list", response);
        } catch (Exception e) {
            logger.error(e, "管理员添加评论遇到异常", WebUtils.getIpAddr(request));
            StageHelper.writeDwzSignal("300", "操作失败", "135", StageHelper.DWZ_FORWARD, "/pms/comment/list", response);
        }
    }

    /**
     * 评价详情
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/detail")
    public String detail(Model model, HttpServletRequest request) {
        int id = ServletRequestUtils.getIntParameter(request, "id", -1);
        if (id > 0) {
            model.addAttribute("entity", ProCommentClient.getCommentById(id));
        }
        return "/pms/comment/detail";
    }

    /**
     * 回复评价
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/reply")
    public String reply(Model model, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        try {
            int handle = ServletRequestUtils.getIntParameter(request, "handle", -1);
            int id = ServletRequestUtils.getIntParameter(request, "id", -1);
            if (handle == 0) {
                ProComment comment = ProCommentClient.getCommentById(id);
                model.addAttribute("entity", comment);
                return "/pms/comment/reply";
            } else {
                String replyContent = ServletRequestUtils.getStringParameter(request, "replyContent", null);
                boolean isSuc = false;
                if (id > 0 && replyContent != null) {
                    int replyLength = new String(replyContent.getBytes("gbk"), "ISO8859_1").length();
                    if (replyLength <= Constant.COMMENT_REPLY_LENGTH * 2) {
                        isSuc = ProCommentClient.replyComment(id, TextUtil.cleanHTML(replyContent));
                    }
                }
                return StageHelper.writeDwzSignal(isSuc ? "200" : "300", isSuc ? "操作成功" : "操作失败", "135", StageHelper.DWZ_CLOSE_CURRENT, "/pms/comment/list", response);
            }
        } catch (Exception e) {
            logger.error(e, "管理员回复用户的评价时，遇到异常", WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "操作失败", "135", StageHelper.DWZ_FORWARD, "/pms/comment/list", response);
        }
    }

    /**
     * 评论缺省值设置
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/list_dv")
    public String defaultValueList(Model model, HttpServletRequest request, HttpServletResponse response) {
        ProCommentDv dv = new ProCommentDv();
        PagerControl<ProCommentDv> pc = ProCommentClient.getCommentDvListByAdmin(dv, StageHelper.getPageInfo(request, "create_time", "desc"));
        model.addAttribute("pc", pc);
        return "/pms/comment/list_dv";
    }

    /**
     * 评论缺省值设置
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/add_dv")
    public String defaultValueAdd(Model model, HttpServletRequest request, HttpServletResponse response) throws JedisClientException {
        int handle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        if (handle == 0) {
            //去添加或修改
            ProCommentDv dv = null;
            int id = ServletRequestUtils.getIntParameter(request, "id", 0);
            if (id > 0) {
                dv = ProCommentClient.getCommentDvById(id);
            }
//            String dv = ShardJedisTool.getInstance().get(JedisKey.search$key, "score_" + score);
            if (dv != null) {

                model.addAttribute("entity", dv);
            }
            model.addAttribute("handle", handle);
            return "/pms/comment/add_dv";
        } else if (handle == 1) {
            //确认添加或修改
            int id = ServletRequestUtils.getIntParameter(request, "id", 0);
            String score = ServletRequestUtils.getStringParameter(request, "score", "");
            String content = ServletRequestUtils.getStringParameter(request, "content", "");
            ProCommentDv dv = new ProCommentDv();
            dv.setContent(content);
            dv.setScore(score);
            boolean result = false;
            if (id > 0) {
                ProCommentDv commentDv = ProCommentClient.getCommentDvById(id);
                dv.setId(id);
                dv.setCreateTime(commentDv.getCreateTime());
                dv.setState(commentDv.getState());
                result = ProCommentClient.updateCommentDvByAdmin(dv);
            } else {
                result = ProCommentClient.addCommentDvByAdmin(dv);
            }
            updateCommentDvRedis(request, score);
            return StageHelper.writeDwzSignal(result ? "200" : "300", result ? "操作成功" : "操作失败", "10231", StageHelper.DWZ_CLOSE_CURRENT, "/pms/comment/list_dv", response);
        } else {
            int id = ServletRequestUtils.getIntParameter(request, "id", 0);
            ProCommentDv commentDv = ProCommentClient.getCommentDvById(id);
            ProCommentDv dv = new ProCommentDv();
            dv.setId(id);
            dv.setState((int) GlobalNames.STATE_INVALID);
            boolean result = ProCommentClient.updateCommentDvByAdmin(dv);
            try {
                updateCommentDvRedis(request, commentDv.getScore());
            } catch (Exception e) {
                logger.error(e, "", "");
            }
            return StageHelper.writeDwzSignal(result ? "200" : "300", result ? "操作成功" : "操作失败", "10231", StageHelper.DWZ_FORWARD, "/pms/comment/list_dv", response);
        }
    }

    //修改评价缺省值缓存
    private void updateCommentDvRedis(HttpServletRequest request, String score) throws JedisClientException {
        if (!Strings.isNullOrEmpty(score)) {
            String[] scores = score.split("；");
            if (scores != null && score.length() > 0) {
                PagerControl<ProCommentDv> pc = ProCommentClient.getCommentDvListByAdmin(new ProCommentDv(), StageHelper.getPageInfo(request, "create_time", "desc"));
                for (String st : scores) {
                    if (st != null) {//剔除空数组
                        List<String> dvs = new ArrayList<String>();
                        if (pc != null && pc.getEntityList() != null && pc.getEntityList().size() > 0) {
                            for (ProCommentDv p : pc.getEntityList()) {
                                if (p != null && p.getScore() != null && p.getScore().contains(st)) {
                                    dvs.add(new Gson().toJson(p));
                                }
                            }
                        }
                        ShardJedisTool.getInstance().del(JedisKey.pms$cmt$score, st);
                        ShardJedisTool.getInstance().sadd(JedisKey.pms$cmt$score, st, dvs.toArray(new String[0]));
                    }
                }
            }
        }
    }
}
