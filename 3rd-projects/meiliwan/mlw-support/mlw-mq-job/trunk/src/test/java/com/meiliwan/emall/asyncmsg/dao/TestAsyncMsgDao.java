//package com.meiliwan.emall.asyncmsg.dao;
//
//import java.util.Date;
//import java.util.List;
//
//import junit.framework.Assert;
//
//import com.meiliwan.emall.commons.rabbitmq.asyncmsg.AsyncMsg;
//
////@ContextConfiguration(locations = { "classpath:conf/spring/async-task-dal.xml" })
//public class TestAsyncMsgDao{
//	
//	//@Autowired
//	private AsyncMsgDao asyncMsgDao;
//	
//	
//	private AsyncMsg asyncMsg;
//	
//	//@BeforeMethod
//	public void init() {
////		asyncMsg = new AsyncMsg();
////		asyncMsg.setFromModel("fromModel");
////		asyncMsg.setToModel("toModel");
////		asyncMsg.setMsgGroupName("asyncMsg");
////		asyncMsg.setExchangeType("topic");
////		asyncMsg.setRoutingKey("fromModel.toModel.asyncMsg");
////		asyncMsg.setComeTime(new Date());
////		asyncMsg.setSendTime(new Date());
////		asyncMsg.setDelayTime(0);
////		asyncMsg.setMsg("It is so good a boy!");
////		asyncMsg.setMsgDescp("msg descp");
//	}
//	
//	//@Test
//	public void testInsertAsyncMsg(){
//		int id = asyncMsgDao.insertSelective(asyncMsg);
//		Assert.assertTrue(id > 0);
//	}
//	
//	//@Test
//	public void testSelectAsyncMsgList() {
//		int id = asyncMsgDao.insertSelective(asyncMsg);
//		Assert.assertTrue(id > 0);
//		List<AsyncMsg> asyncMsgList = asyncMsgDao.selectNeed2SendAsyncmsgBefore(new Date());
//		Assert.assertNotNull(asyncMsgList);
//		Assert.assertTrue(!asyncMsgList.isEmpty());
//		
//		for (AsyncMsg asyncMsg : asyncMsgList) {
//			System.out.println(asyncMsg);
//		}
//	}
//	
//	//@Test
//	public void testUpdateAsyncMsg() {
//		int id = asyncMsgDao.insertSelective(asyncMsg);
//		Assert.assertTrue(id > 0);
//		List<AsyncMsg> asyncMsgList = asyncMsgDao.selectNeed2SendAsyncmsgBefore(new Date());
//		Assert.assertNotNull(asyncMsgList);
//		Assert.assertTrue(!asyncMsgList.isEmpty());
//		for (AsyncMsg asyncMsg : asyncMsgList) {
//			asyncMsg.setIsSend((short)1);
//			asyncMsgDao.updateByPrimaryKeySelective(asyncMsg);
//		}
//		
//		asyncMsgList = asyncMsgDao.selectNeed2SendAsyncmsgBefore(new Date());
//		Assert.assertNotNull(asyncMsgList);
//		Assert.assertTrue(asyncMsgList.isEmpty());
//	}
//}
