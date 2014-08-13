//package com.meiliwan.emall.log.dao;
//
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//
//import com.meiliwan.emall.commons.log.BizLog;
//import com.meiliwan.emall.commons.rabbitmq.MqModel;
//
////@ContextConfiguration(locations = { "classpath:conf/spring/bizlog-dal.xml" })
//public class TestBizlogDao {
//	
//	//@Autowired
//	BizLogDao bizLogDao = null;
//	
//	//@Test
//	public void testBizlogSave()throws Exception {
//		BizLog bizLog = new BizLog.LogBuilder(MqModel.SP, "smallsp", "SP-smallsp-add")
// 		.setUser(0, "", "sdfds")
// 		.setObj("order", "1213213")
// 		.setOp("op").build();
//		
//		bizLogDao.insert(bizLog);
//	}
//	
//	public static void main(String[] args)throws Exception  {
//		ApplicationContext cxt = new ClassPathXmlApplicationContext("classpath:conf/spring/bizlog-dal.xml");
//		
//		BizLogDao bizLogDao = (BizLogDao)cxt.getBean("bizLogDao");
//		
//		// bkstage
//		BizLog bizLog = new BizLog.LogBuilder(MqModel.BKSTAGE, "smallsp", "SP-smallsp-add")
// 		.setUser(0, "", "sdfds")
// 		.setObj("order", "1213213")
// 		.setOp("op").build();
//		bizLogDao.insert(bizLog);
//		
//		// account
//		bizLog = new BizLog.LogBuilder(MqModel.BKSTAGE, "smallsp", "SP-smallsp-add")
// 		.setUser(0, "", "sdfds")
// 		.setObj("order", "1213213")
// 		.setOp("op").build();
//		bizLogDao.insert(bizLog);
//		
//		// order
//		bizLog = new BizLog.LogBuilder(MqModel.ORDER, "smallsp", "SP-smallsp-add")
// 		.setUser(0, "", "sdfds")
// 		.setObj("order", "1213213")
// 		.setOp("op").build();
//		bizLogDao.insert(bizLog);
//		
//		// product
//		bizLog = new BizLog.LogBuilder(MqModel.PRODUCT, "smallsp", "SP-smallsp-add")
// 		.setUser(0, "", "sdfds")
// 		.setObj("order", "1213213")
// 		.setOp("op").build();
//		bizLogDao.insert(bizLog);
//		
//		// sp
//		bizLog = new BizLog.LogBuilder(MqModel.SP, "smallsp", "SP-smallsp-add")
// 		.setUser(0, "", "sdfds")
// 		.setObj("order", "1213213")
// 		.setOp("op").build();
//		bizLogDao.insert(bizLog);
//		
//		// user
//		bizLog = new BizLog.LogBuilder(MqModel.USER, "smallsp", "SP-smallsp-add")
// 		.setUser(0, "", "sdfds")
// 		.setObj("order", "1213213")
// 		.setOp("op").build();
//		bizLogDao.insert(bizLog);
//		
//		
//	}
//	
//}
