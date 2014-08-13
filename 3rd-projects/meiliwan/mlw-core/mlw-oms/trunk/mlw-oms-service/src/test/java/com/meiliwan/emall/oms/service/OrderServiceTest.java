package com.meiliwan.emall.oms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.oms.bean.OrdPrintLogs;
import com.meiliwan.emall.oms.bean.OrdRemark;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.oms.constant.Constant;
import com.meiliwan.emall.oms.constant.OrdIDeliverStatus;
import com.meiliwan.emall.oms.constant.OrdITotalStatus;
import com.meiliwan.emall.oms.constant.OrderStatusType;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.oms.BaseTest;
import com.meiliwan.emall.oms.dao.OrdDao;
import com.meiliwan.emall.oms.dao.OrdiDao;
import com.meiliwan.emall.oms.dto.OrdDTO;
import com.meiliwan.emall.oms.dto.OrderQueryDTO;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.StatusMachineService;
import com.meiliwan.emall.service.BaseService;

public class OrderServiceTest extends BaseTest{


    @Autowired
    private OrderService orderService;
    @Autowired
    private StatusMachineService machineService;
    @Autowired
    private OrdDao dao;
    @Autowired
    private OrdiDao ordiDao;
    private JsonObject resultObj;

    @BeforeMethod
    public void BeforeMethod(){
        if(resultObj == null)resultObj= new JsonObject();
    }

    @Test(invocationCount = 100,threadPoolSize = 100)
    public void testAddRemark(){
        OrdRemark ordRemark = new OrdRemark();
        ordRemark.setOrderId("000004258184");
        ordRemark.setContent("hello");
        ordRemark.setState(0);
        ordRemark.setCreateTime(new Date());
        ordRemark.setUid(45);
        ordRemark.setUserName("王志明");

        orderService.insertRemark(resultObj,ordRemark);

        //成功
        Assert.assertEquals(1,resultObj.get(BaseService.RESULT_OBJ).getAsInt());
        //失败
        Assert.assertEquals(0,resultObj.get(BaseService.RESULT_OBJ).getAsInt());
    }

    @Test(invocationCount = 100,threadPoolSize = 100)
    public void testGetRemarksByOrderId(){
        orderService.getRemarksByOrderId(resultObj,"000004258184");
        List<OrdRemark> list =new Gson().fromJson(resultObj.get(BaseService.RESULT_OBJ), new TypeToken<List<OrdRemark>>() {
        }.getType());

        //成功
        Assert.assertNotNull(list);
        //数量跟数据库一致
        Assert.assertEquals(300,list.size());
    }

    @Test(invocationCount = 100, threadPoolSize = 100)
    public void testAddPrintLog() {
        OrdPrintLogs ordPrintLogs = new OrdPrintLogs();
        ordPrintLogs.setOptUname("王志明");
        ordPrintLogs.setOrderId("000004258184");
        ordPrintLogs.setPrintType(0);
        ordPrintLogs.setCreateTime(new Date());
        ordPrintLogs.setOptUid(45);

        orderService.insertPrintLog(resultObj,ordPrintLogs);

        Assert.assertEquals(1,resultObj.get(BaseService.RESULT_OBJ).getAsInt());
    }

    @Test(invocationCount = 100, threadPoolSize = 100)
    public void testGetPrintLogList(){
        OrdPrintLogs ordPrintLogs = new OrdPrintLogs();
        ordPrintLogs.setOrderId("000004258184");
        ordPrintLogs.setPrintType(0);
        orderService.getPrintLogListBy(resultObj,ordPrintLogs);
        List<OrdPrintLogs> list =new Gson().fromJson(resultObj.get(BaseService.RESULT_OBJ), new TypeToken<List<OrdPrintLogs>>() {
        }.getType());

        System.out.println(list);
    }

    @Test
    public void testDetail() {
	    	JsonObject resultObj = new JsonObject();
	
	    	orderService.selectOrderDetail(resultObj, "12345");
	    	JsonElement pc = resultObj.get(BaseService.RESULT_OBJ);
	
	    	System.out.println("pc="+pc);
    }
    
    @Test(invocationCount = 100, threadPoolSize = 50)
    public void test() throws ServiceException {

    	OrderQueryDTO orderQuery = new OrderQueryDTO();
        orderQuery.setOrderId("0000000008");
    	//orderQuery.setStatusType("ID");
      //  orderQuery.setOrderItemStatus("10");
//        orderQuery.setOrderItemStatus("10");
        orderQuery.setBillType((short) 1);
    //    orderQuery.setOrderTimeStart(DateUtil.parseStringToDate("2013-01-01"));
    	//orderQuery.setProId(10);
//    	orderQuery.setPayCode("ZFB");
//    	orderQuery.setProName("浴露");
      // System.out.println(orderQuery.toString());
       for(int i=0;i<1;i++) {
           PageInfo pageInfo = new PageInfo();
           JsonObject resultObj = new JsonObject();
           try{
               orderService.selectOrderList(resultObj, orderQuery, pageInfo, true);
               System.out.println("resultObj="+resultObj);
      //  JsonObject jpc = resultObj.get(BaseService.RESULT_OBJ).getAsJsonObject();
           PagerControl pc = new Gson().fromJson(resultObj.get(BaseService.RESULT_OBJ), PagerControl.class);
       //        String str = new Gson().fromJson(resultObj.get(BaseService.RESULT_OBJ), new TypeToken<String>(){}.getType());


            List<OrdDTO> list = pc.getEntityList();
         //      System.out.println("i="+i+"----------->");
          //     System.out.println(i+", pc.getPageInfo()()="+pc.getPageInfo());
               System.out.println(list.get(0).toString());
           }catch (Exception e){
               e.printStackTrace();
           }

       }


    }
    
    /**
     * 查询等待出库的订单
     * <select name="order_state">
		<option value="">全部</option>
		<option value="10">等待出库</option>
	    <option value="20">等待发货</option>
	    <option value="30">已发货</option>
	    <option value="40">已收货</option>
	    <option value="50">拒收</option>
	    </select>
     */
    /**
     * 根据分页大小,设置所有等待出库数据改为确认收货
     */
    @Test
    public void testSelectWaittingforOut(){
     	JsonObject resultObj = new JsonObject();
    		OrderQueryDTO orderQuery = new OrderQueryDTO();
    		orderQuery.setStatusType(OrderStatusType.ID.getType());
    		orderQuery.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
    		orderQuery.setState(GlobalNames.STATE_VALID);
    		orderQuery.setOrderItemStatus("30");
    		
    		
    		PageInfo pageInfo = new PageInfo();
    		pageInfo.setPagesize(10);
    		pageInfo.setPage(1);
    		while(true){
    			
	    		orderService.selectOrderList(resultObj, orderQuery, pageInfo, false);
	    		System.out.println(resultObj);
	    		JsonObject jbean = resultObj.get(BaseService.RESULT_OBJ).getAsJsonObject();
	
	    		 PagerControl<OrdDTO> pc = new Gson().fromJson(jbean, new TypeToken<PagerControl<OrdDTO>>() {
	    	        }.getType());
	    		List<OrdDTO> list = pc.getEntityList();
	    		if(list==null)
	    			break ;
	    		StringBuilder orderIds = new StringBuilder();
	    		for (OrdDTO ordDTO : list) {
	    			String orderId = ordDTO.getOrderId();
	    			orderIds.append(orderId).append(",");
	    			
	    			//出库
	    			//testORDI_EFFECTIVED(orderId);
	    			//发货
	    			//testDELI_DELIVER_GOODS(orderId);
	    			//
	    			testReceive(orderId);
			}
	    		System.out.println(orderIds.toString());
    		}
    }
    
    
    /**
     * 主线状态出库
     * @throws ServiceException
     */
    public void testORDI_EFFECTIVED(String orderId) throws ServiceException {
    	
	    	OrderStatusDTO ordStatusDIO = new OrderStatusDTO();
	    	//ordStatusDIO.setCheckOrder(true);
	    	ordStatusDIO.setAdminId(0);
	    	ordStatusDIO.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
	    	ordStatusDIO.setOrderId(orderId);
	    	
	    	//支付完成
	    	ordStatusDIO.setStatusType(OrderStatusType.IS);
	    	ordStatusDIO.setStatusCode(OrdITotalStatus.ORDI_EFFECTIVED.getCode());
	    	
	    	ordStatusDIO.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
		ordStatusDIO.setUid(0);
		ordStatusDIO.setStatusType(OrderStatusType.ID);
		ordStatusDIO.setStatusCode(OrdIDeliverStatus.DELI_WAIT_CONSIGNMENT
                            .getCode());
		
		machineService.updateStatus(null,ordStatusDIO);
    }
    
    /**
     * 主线状态发货
     * @throws ServiceException
     */
    public void testDELI_DELIVER_GOODS(String orderId) throws ServiceException {
    	
	    	OrderStatusDTO ordStatusDIO = new OrderStatusDTO();
	    	//ordStatusDIO.setCheckOrder(true);
	    	ordStatusDIO.setAdminId(0);
	    	ordStatusDIO.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
	    	ordStatusDIO.setOrderId(orderId);
	    	
	    	//支付完成
	    	ordStatusDIO.setStatusType(OrderStatusType.IS);
	    	ordStatusDIO.setStatusCode(OrdITotalStatus.ORDI_EFFECTIVED.getCode());
	    	
	    	ordStatusDIO.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
		ordStatusDIO.setUid(0);
		
		ordStatusDIO.setTransportInfo("物流公司:顺风快递 货运单号:123124142314");
		// ordStatusDIO.setCheckOrder(true);

		ordStatusDIO.setStatusType(OrderStatusType.ID);
		ordStatusDIO.setStatusCode(OrdIDeliverStatus.DELI_DELIVER_GOODS
				.getCode());
		machineService.updateStatus(null,ordStatusDIO);
    }
    
    /**
     * 确认收获
     */
    @Test
    public void testReceive(String orderId){
    		OrderStatusDTO ordStatusDIO = new OrderStatusDTO();
		// ordStatusDIO.setCheckOrder(true);
		ordStatusDIO.setAdminId(0);
		ordStatusDIO.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
		ordStatusDIO.setOrderId(orderId);
		ordStatusDIO.setUid(0);
		ordStatusDIO.setStatusType(OrderStatusType.ID);
		ordStatusDIO.setStatusCode(OrdIDeliverStatus.DELI_RECEIPTED
				.getCode());
		machineService.updateStatus(null,ordStatusDIO);
    }
} 
