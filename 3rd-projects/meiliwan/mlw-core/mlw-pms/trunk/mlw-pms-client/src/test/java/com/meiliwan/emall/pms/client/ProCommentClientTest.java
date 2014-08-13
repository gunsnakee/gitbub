package com.meiliwan.emall.pms.client;

import java.util.List;

import com.meiliwan.emall.pms.util.CommentOpt;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.pms.bean.ProComment;
import com.meiliwan.emall.pms.dto.CommentDTO;
import com.meiliwan.emall.pms.dto.ProCommentPage;

/**
 * 用户商品评价 压测类
 * User: yiyou.luo
 * Date: 13-9-6
 * Time: 下午5:05
 */
public class ProCommentClientTest {

    /**
     * 用户在确认收货的时候，调用此方法
     */
    @Test(invocationCount = 100, threadPoolSize = 5)
    public void addCommentByOrder() {
        ProComment comment = new ProComment();
        comment.setOrderId("1010000012");
        comment.setProId(10235013);
        comment.setProName("商品名称");
        comment.setUid(1);
        comment.setNickName("nickName");
        ProCommentClient.addCommentByOrder(comment);
    }


   /**
     * 用户确定评价
     */
    @Test(invocationCount = 5000, threadPoolSize = 5)
    public void addCommentByUser() {
        ProComment comment = new ProComment();
        comment.setUid(18);
        ProCommentClient.addCommentByUser(comment);
    }






   /**
     * 获得一条评价
     */
   @Test//(invocationCount = 50, threadPoolSize = 5)
    public void getCommentById() {
	   ProComment comment = ProCommentClient.getCommentById(52);
	   System.out.println(comment);
	   Assert.assertNotNull(comment);
	   
    }




   @Test
   public void testGetPagerForBkstage(){
	   CommentDTO view = new CommentDTO();
	   view.setIsReply("1");
	   PagerControl<ProCommentPage> pc = ProCommentClient.getPagerForBkstage(view, new PageInfo());
	   System.out.println(pc);
	   Assert.assertTrue(pc.getEntityList().size()>0);
   }

  /**
     * 个人中心查询评价分页列表
     */
  @Test(invocationCount = 5000, threadPoolSize = 5)
    public void getCommentListByUserId() {
        ProCommentClient.getCommentListByUserId(new PageInfo(),15,(short)-1);
    }  ;

   /**
     * 商品详情页查询出评论分页列表<br/>
     */
   @Test(invocationCount = 5000, threadPoolSize = 5)
    public void getCommentList() {
        ProCommentClient.getCommentList(new PageInfo(),10235013,5,"comment_time");
    }




    /**
     * 查询最新的5条评论
     */
    @Test(invocationCount = 1000, threadPoolSize = 10)
    public void getFlontFiveComments() {
        ProCommentClient.getFlontFiveComments(10235013);
    }

  /**
     * 校验用户对某个商品的评论情况
     * 返回内容：unbuy 未购买该商品
     *           commentOver （有购买）评论完毕
     *           uncomment （有购买）未评论完
     */
  @Test(invocationCount = 1000, threadPoolSize = 10)
    public void checkComment(){
        ProCommentClient.checkComment(10235013,1);
    }

    /**
     * 用户取得未评价的数量
     */
    @Test(invocationCount = 5000, threadPoolSize = 10)
    public void getCountNotComment() {
        ProCommentClient.getCountNotComment(10235013);
    }

   /**
     * 根据字段修改对应的数据
     */
   @Test(invocationCount = 5000, threadPoolSize = 10)
    public void updateComByParam(){
        ProCommentClient.updateComByParam(50, CommentOpt.USELESSCOUNT);
    }

   /**
     * 通过商品IDs获取评论列表，每个商品至多查三个好评
     * @param ids
     */
    public void getListByProIds(List<Integer> ids){

    }
    
    @Test
    public void testGetShamCommentByProId(){
    	
    		List<ProComment> list =	ProCommentClient.getShamCommentByProId(10235250,1);
	    	System.out.println(list.size());
    }
    
}
