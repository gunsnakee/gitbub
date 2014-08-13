package com.meiliwan.emall.pms.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.pms.BaseTest;
import com.meiliwan.emall.pms.bean.ProComment;
import com.meiliwan.emall.pms.bean.ProCommentClick;
import com.meiliwan.emall.pms.dto.CommentDTO;
import com.meiliwan.emall.pms.util.CommentOpt;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guangdetang on 13-8-19.
 */
public class ProCommentServiceTest extends BaseTest{
    @Autowired
    private ProCommentService commentService;
    private JsonObject resultObj = new JsonObject();
    @Test
    public void testAddCommentByOrder() throws Exception {
        ProComment comment = new ProComment();
        comment.setOrderId("1010000012");
        comment.setProId(10235013);
        comment.setProName("商品名称");
        comment.setUid(1);
        comment.setNickName("nickName");
        commentService.addCommentByOrder(resultObj,comment);
    }

    @Test
    public void testAddCommentByUser() throws Exception {
        ProComment comment = new ProComment();
        comment.setId(1);
        comment.setContent("评论内容");
        comment.setScore((short)5);
        commentService.addCommentByUser(resultObj,comment);
    }

    @Test
    public void testUpdateReplyComment() throws Exception {
        commentService.updateReplyComment(resultObj,1,"回复内容");
    }

    @Test
    public void testUpdateComment() throws Exception {
        ProComment comment = new ProComment();
        comment.setId(1);
        comment.setContent("修改评论内容");
        comment.setScore((short)4);
        commentService.updateComment(resultObj,comment);
    }

    @Test
    public void testUpdateCommentStateByAdmin() throws Exception {
        int[] ids = {1};
        commentService.updateCommentStateByAdmin(resultObj,ids,1);
    }

    @Test
    public void testGetCommentById() throws Exception {
        commentService.getCommentById(resultObj,1);
    }

    @Test
    public void testGetCommentListByOrder() throws Exception {
        commentService.getCommentListByOrder(resultObj,"1000000033");
    }

    @Test
    public void testGetCommentListByUserId() throws Exception {
        commentService.getCommentListByUserId(resultObj,new PageInfo(1,10), 17,(short)1);
    }

    @Test
    public void testGetCommentList() throws Exception {
        commentService.getCommentList(resultObj,new PageInfo(1,10),10235105, 1,"comment_time");
    }

    @Test
    public void testGetCommentPagerAdmin() throws Exception {
        CommentDTO view = new CommentDTO();
        view.setState((short)1);
        view.setOrderId("1000000022");
        view.setNickName("nickName");
        view.setProId(10235014);
        view.setProName("proName");
        view.setStartUseful(1);
        view.setEndUseful(10);
        view.setStartScore(1);
        view.setEndScore(4);
        view.setIsTop((short) 1);
        view.setContent("1");
        view.setStartTime("2013-05-05 12:12:12");
        view.setEndTime("2013-08-05 12:12:12");
        commentService.getCommentPagerAdmin(resultObj, view, new PageInfo(1, 10));
    }

    @Test
    public void testIsHasClick() throws Exception {
        commentService.isHasClick(resultObj,2,10);
    }

    @Test
    public void testAddIsUseClick() throws Exception {
        ProCommentClick click = new ProCommentClick();
        click.setUid(112);
        click.setClickType((byte) 1);
        click.setCommentId(1);
        click.setCreateTime(DateUtil.getCurrentTimestamp());
        commentService.addIsUseClick(resultObj,click);
    }

    @Test
    public void testGetFiveComments() throws Exception {
        commentService.getFiveComments(resultObj,10235015);
    }

    @Test
    public void testCheckComment() throws Exception {
        commentService.checkComment(resultObj,10235015, 14);
    }

    @Test
    public void testGetCountNotComment() throws Exception {
        commentService.getCountNotComment(resultObj,13);
    }

    @Test
    public void testUpdateComByParam() throws Exception {
        commentService.updateComByParam(resultObj,23, CommentOpt.USEFULCOUNT);
    }

    @Test
    public void testGetListByProIds(){
        List<Integer> ids = new ArrayList<Integer>();
        ids.add(10235202);
        ids.add(10235257);


    }
}
