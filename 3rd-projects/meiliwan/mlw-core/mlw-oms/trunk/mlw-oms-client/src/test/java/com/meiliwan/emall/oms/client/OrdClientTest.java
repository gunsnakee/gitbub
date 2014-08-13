package com.meiliwan.emall.oms.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.meiliwan.emall.base.capability.AbstractWorker;
import com.meiliwan.emall.base.capability.CapcityRunner;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.bean.ClientType;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.bean.PayCode;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.oms.bean.Ord;
import com.meiliwan.emall.oms.bean.OrdAddr;
import com.meiliwan.emall.oms.bean.OrdInvoice;
import com.meiliwan.emall.oms.bean.OrdPay;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.constant.Constant;
import com.meiliwan.emall.oms.constant.OrdITotalStatus;
import com.meiliwan.emall.oms.constant.OrderType;
import com.meiliwan.emall.oms.dto.OrdDTO;
import com.meiliwan.emall.oms.dto.OrdDetailDTO;
import com.meiliwan.emall.oms.dto.OrderQueryDTO;
import com.meiliwan.emall.oms.util.TestUitl;

public class OrdClientTest {

	private Random random = new Random();
	private String oids[]=new String[]{
			"0000361429",
			"0000361433",
			"0000361444",
			"0000361458",
			"0000361461",
			"0000361463",
			"0000361464",
			"0000361489",
			"0000361493"
	};
	
	@Test
    public void testOrderDelete() throws ServiceException{
		OrdClient.deleteOrder("000004256143");
		OrdClient.getOrdAddrById("000004258250");
    }
	
	@Test
	public void getOrdAddrById() throws ServiceException{
		OrdAddr addr = OrdClient.getOrdAddrById("000004258250");
		System.out.println(addr);
	}
	
	@Test
    public void testCreateOrder() throws ServiceException{
		create(Constant.ORDER_BILL_TYPE_FORWARD,OrderType.REAL_ORDER,PayCode.ALIPAY );
		/*PayCode payCode = TestUitl.randomPayCode();
		if(PayCode.OFFLINE_COD.equals(payCode)||PayCode.OFFLINE_POS.equals(payCode)){
			//或到付款
			create(GlobalNames.ORDER_BILL_TYPE_FORWARD,OrderType.REAL_ORDER_COD,payCode );
		}else{
			create(GlobalNames.ORDER_BILL_TYPE_FORWARD,OrderType.REAL_ORDER,payCode );
		}*/
    }

    private void create(short billType,OrderType orderType,PayCode payCode){
    	JsonObject resultObj = new JsonObject();
        OrdDetailDTO ordDetailDTO = new OrdDetailDTO();
        Ord ord = new Ord();
     
        double pay=15;
        double fee=12;
        int sale = TestUitl.randomInt(1000);
        ord.setOrderPayAmount(pay);
        ord.setOrderSaleAmount(pay+sale);
        ord.setTransportFee(fee);
        ord.setRealPayAmount(pay+fee);
        String recvName=TestUitl.randomThreeChar();
        ord.setRecvName(recvName);
        
        ord.setBillType(billType);
        ord.setClientId(ClientType.PC.getType());
        ord.setCreateTime(new Date());
        ord.setPayTime(new Date());
        ord.setIsInvoice((short)1);
        ord.setOrderComments(TestUitl.randomThreeChar());
       
        ord.setOrderType("dddddddddddddddddddddddddddddd");
        ord.setRecvAddrId(2);
        ord.setTotalItem((short) 2);
        String userName=TestUitl.randomThreeChar();
        ord.setUserName(userName);
        int uid = 18;//TestUitl.randomInt(100);
        ord.setUid(uid);
        ordDetailDTO.setOrd(ord);

        String actName = TestUitl.randomThreeChar();
        
        List<Ordi> list = new ArrayList<Ordi>();
        for(int i=1;i<=2;i++){
            Ordi ordiDTO1 = new Ordi();
            ordiDTO1.setTransportFee(10d);
            ordiDTO1.setUid(uid);
            ordiDTO1.setUserName(userName);
            ordiDTO1.setRecvAddrId(2);
            ordiDTO1.setRecvName(recvName);

//            ordiDTO1.setActId(1);
//            ordiDTO1.setActName(actName);
            ordiDTO1.setBillType(billType);
            ordiDTO1.setBrandId(1);
            ordiDTO1.setBuyType(TestUitl.randomBuyType());
//            ordiDTO1.setDeliveryComments(TestUitl.randomThreeChar());
//            ordiDTO1.setDistributeType("");
//            ordiDTO1.setHopeArrivalTime(new Date());
            ordiDTO1.setOrderType(orderType.getCode());
            ordiDTO1.setSaleNum(5);
            ordiDTO1.setSaleUnit("件");
            ordiDTO1.setSupplierId(11);
            ordiDTO1.setTotalAmount(50.11);
            ordiDTO1.setUintPrice(30d);
//            ordiDTO1.setVoucherTotalMoney(20d);
            ordiDTO1.setProId(10235106+TestUitl.randomInt(500));
            ordiDTO1.setProName(TestUitl.randomThreeChar());
            ordiDTO1.setProCateId(uid);
            ordiDTO1.setProPic("/xxx/rrr/12345.jpg");
            ordiDTO1.setCreateTime(new Date());
            ordiDTO1.setOrderItemStatus(OrdITotalStatus.ORDI_COMMITTED.getCode());
            
            list.add(ordiDTO1) ;
        }
        ordDetailDTO.setOrdiList(list);

        OrdPay ordPay = new OrdPay();
        ordPay.setOrderId(ord.getOrderId());
        ordPay.setPayTime(new Date());
        ordPay.setUserName(ord.getUserName());
        ordPay.setUid(uid);
        ordPay.setBillType(billType);
        ordPay.setPayAmount(ord.getRealPayAmount());
        
        
        ordPay.setPayCode(payCode.name());
        ordPay.setPayItemNum((short)2);
        ordPay.setPayName(payCode.getDesc());
        ordPay.setState(GlobalNames.STATE_VALID);
        List<OrdPay>  payList = new ArrayList<OrdPay>();
        payList.add(ordPay);

        

        OrdInvoice invoice = new OrdInvoice();
        invoice.setOrderId(ord.getOrderId());
//        invoice.setAccountBank("招商银行");
//        invoice.setAccountNo("123456789");
//        invoice.setBillType(billType);
//        invoice.setDeliveryAre("广西");
//        invoice.setIfSend((short) 1);
//        invoice.setInvoiceAddress("广西壮族自治区南宁市");
//        invoice.setInvoiceContent("收到收到收到");
//        invoice.setInvoiceHead("猫扑网");
//        invoice.setInvoiceSendMan("猫扑网行政助理");
//        invoice.setInvoiceType("企业");
//        invoice.setpCity("西安");
//        invoice.setReceiveCallNo("1364578942");
//        invoice.setRegisterAddress("西安市秦始皇兵马俑");
//        invoice.setRegisterPhone("123456789");
        invoice.setUid(11);
        invoice.setUpdateTime(new Date());
        
        	
        ordDetailDTO.setInvoice(invoice);
        ordDetailDTO.setOrdPayList(payList);
        
        if(payCode.name().equals(PayCode.OFF_COD.name())||PayCode.OFF_POS.name().equals(payCode)){
	    		OrdClient.createOrdCOD(ordDetailDTO);
	    	}else{
	    		OrdClient.createOrd( ordDetailDTO);
	    	
	    }
    }
	
	@Test
	public void testGetDetail(){
		String orderId=oids[random.nextInt(oids.length)];
		//OrdClient.getDetail(orderId);
		OrdDetailDTO dto = OrdClient.getDetail("000004258188");
		Assert.assertNotNull(dto);
		
		dto = OrdClient.getDetail("");
		Assert.assertNull(dto);
		
		dto = OrdClient.getDetail(null);
		Assert.assertNull(dto);
	}
	@Test
	public void testGetOrdByOrdId(){
		//String orderId=oids[random.nextInt(oids.length)];
		//OrdClient.getOrdByOrdId(orderId);
		try {
			Ord ord = OrdClient.getOrdByOrdId("00000000000021141");
			System.out.println(ord);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR");
			//e.printStackTrace();
		}
	}

    @Test
    public void testGetOrderListSortCreateTime(){
        String orderId=oids[random.nextInt(oids.length)];
        OrderQueryDTO dto = new OrderQueryDTO();
        dto.setUid(1+random.nextInt(20));
        PageInfo pi = new PageInfo();
        pi.setPagesize(10);
        PagerControl<OrdDTO> pc = OrdClient.getOrderListSortCreateTime(dto, pi, false);
    }

	@Test   (threadPoolSize = 5, invocationCount = 100)
	public void updateSubStockAndPayStatus(){
        /*
        * "params":{"elements":[{"subject":"美丽传说 苹果（Apple）iPad 2 MC773CH/A 9.7英寸平>板电脑 （16G WIFI+3G版）黑色","totalAmount":10.0,"uid":105005046,"targetUid":105005046,"orderId":"000004255933","payType":"BUY_PRO","timeMillis":0,
        * "payParams":[{"payId":"00000106892623","payCode":"ALIPAY","amount":0.01,"payStatus":"PAY_FINISHED"},{"payId":"00000106892622","payCode":"MLW_W","amount":9.99,"payStatus":"PAY_FINISHED"}]},
        * {"payId":"00000106892623","payCode":"ALIPAY","amount":0.01,"payStatus":"PAY_FINISHED"}]}

        * */

//    PaymentDTO paymentDTO = new PaymentDTO();
//        paymentDTO.setOrderId("000004255933");
//        paymentDTO.setPayType(PayType.BUY_PRO);
//        paymentDTO.setSubject("美丽传说 苹果（Apple）iPad 2 MC773CH/A 9.7英寸平>板电脑 （16G WIFI+3G版）黑色");
//        paymentDTO.setTargetUid(105005046);
//        paymentDTO.setTotalAmount(10.0);
//        paymentDTO.setUid(105005046);
//
//        PayParam payParam = new PayParam();
//        payParam.setPayCode(PayCode.ALIPAY);
//        payParam.setAmount(0.01);
//        payParam.setPayId("00000106892623");
//        payParam.setPayStatus(PayStatus.PAY_FINISHED);
//
//        PayParam payParam2 = new PayParam();
//        payParam2.setPayCode(PayCode.MLW_W);
//        payParam2.setAmount(9.99);
//        payParam2.setPayId("00000106892622");
//        payParam2.setPayStatus(PayStatus.PAY_FINISHED);




//        PayParam[] payParams      = new PayParam[2];
//        payParams[0] = payParam;
//        payParams[1] = payParam2;
//        paymentDTO.setPayParams(payParams);

//         boolean result = OrdClient.updateSubStockAndPayStatus(paymentDTO, payParam);
	}
	
	/**
	 * 性能测试
	 */
	@Test
	public void testCapcity(){
		
		CapcityRunner runner = new CapcityRunner(10);
		
		runner.setWorker(new AbstractWorker(){
			
			@Override
			public void runTask() {
				//new OrdClientTest().testGetDetail();
				//new OrdClientTest().testGetOrdByOrdId();
				//new OrdClientTest().testGetOrderListSortCreateTime();
				new OrdClientTest().testCreateOrder();
			}
		});
		runner.setRequestSize(100);
		runner.startThread();
		System.exit(1);
	}
}

