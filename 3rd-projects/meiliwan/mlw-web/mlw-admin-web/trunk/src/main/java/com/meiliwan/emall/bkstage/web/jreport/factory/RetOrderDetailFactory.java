package com.meiliwan.emall.bkstage.web.jreport.factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSourceProvider;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.meiliwan.emall.bkstage.web.jreport.vo.BaseReportVO;
import com.meiliwan.emall.bkstage.web.jreport.vo.RetOrderDetail;
import com.meiliwan.emall.bkstage.web.jreport.vo.RetOrderGoodsItem;
import com.meiliwan.emall.bkstage.web.jreport.vo.RetOrderLog;

public class RetOrderDetailFactory extends JRAbstractBeanDataSourceProvider {


    public RetOrderDetailFactory() {
       super(RetOrderDetailFactory.class);
    }
    public JRDataSource create(JasperReport arg0) throws JRException {
       /**
        *测试数据，在使用中，不需要继承JRAbstractBeanDataSourceProvider，
        *只需要把集合类封装到JRBeanCollectionDataSource中就可以了
        **/

        RetOrderDetail rod = new RetOrderDetail();
        rod.setApplyTime(new Date());
        rod.setCreateType("换货");
        rod.setIsInvoice((short)1);
        rod.setOldOrderId("123456789012");
        rod.setOrderId("120987654321");
        rod.setPayType("网上支付");
        rod.setRetStatus("等待客服审核");
        rod.setRetType("用户自建");

        
        rod.setRecvName("林好好");
        rod.setRecvArea("广西壮族自治区南宁市西乡塘区");
        rod.setRecvAddress("总部路1号");
        rod.setRecvMobile("139123456789");
        rod.setRecvPhone("0771-12345678");

        
        rod.setUserId("mlw_124567890");
        rod.setUserNickName("我是黄鼠狼");
        rod.setUserEmail("yuxiong@sohu.com");
        rod.setUserEmailVerified(true);
        rod.setUserMobile("139123456789");
        rod.setUserMobileVerified(false);
        rod.setUserAfterSaleScore(5);


        
        rod.setCheckResult("同意");
        rod.setCheckResult("情况属实，同意退款给用户了");
        rod.setCheckTime(new Date());

        List<RetOrderLog> logs = new ArrayList<RetOrderLog>();

        RetOrderLog log = new RetOrderLog();
        log.setCreateTime(new Date());
        log.setOptContent("您的退换货申请已提交，正在等待相关人员审核");
        log.setOptId("10");
        log.setOptMan("客户");
        log.setOptResult("同意");
        
        logs.add(log);


        log = new RetOrderLog();
        log.setCreateTime(new Date());
        log.setOptContent("您的审核已通过，同意退换货");
        log.setOptId("11");
        log.setOptMan("客服");
        log.setOptResult("同意");
       
        logs.add(log);

        log = new RetOrderLog();
        log.setCreateTime(new Date());
        log.setOptContent("您的审核已通过，同意退换货");
        log.setOptId("11");
        log.setOptMan("客服");
        log.setOptResult("同意");
       
        logs.add(log);
        
        log = new RetOrderLog();
        log.setCreateTime(new Date());
        log.setOptContent("您的审核已通过，同意退换货");
        log.setOptId("11");
        log.setOptMan("客服");
        log.setOptResult("同意");
       
        logs.add(log);
        
        log = new RetOrderLog();
        log.setCreateTime(new Date());
        log.setOptContent("您的审核已通过，同意退换货");
        log.setOptId("11");
        log.setOptMan("客服");
        log.setOptResult("同意");
       
        logs.add(log);
        List<RetOrderGoodsItem> items = new ArrayList<RetOrderGoodsItem>();

        RetOrderGoodsItem item = new RetOrderGoodsItem();
        item.setProId(12345678);
        item.setProSn("12345678");
        item.setProName("泰国精油香薰哦");
        item.setBuyNum(10);
        item.setRetNum(5);
        items.add(item);

        item = new RetOrderGoodsItem();
        item.setProId(12345671);
        item.setProSn("12345671");
        item.setProName("美丽湾按摩服务一个钟");
        item.setBuyNum(50);
        item.setRetNum(2);
        items.add(item);

        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);

        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        rod.setRetOrderGoodsItems(items);
        rod.setRetOrderLogs(logs);
       
        List<BaseReportVO> mainList = new ArrayList<BaseReportVO>();
        mainList.add(rod);
       return new JRBeanCollectionDataSource(mainList);
    }
    
    public static List<BaseReportVO> getBeanCollection() {
    	/**
         *测试数据，在使用中，不需要继承JRAbstractBeanDataSourceProvider，
         *只需要把集合类封装到JRBeanCollectionDataSource中就可以了
         **/
        RetOrderDetail rod = new RetOrderDetail();
        rod.setApplyTime(new Date());
        rod.setCreateType("换货");
        rod.setIsInvoice((short)1);
        rod.setOldOrderId("123456789012");
        rod.setOrderId("120987654321");
        rod.setPayType("网上支付");
        rod.setRetStatus("等待客服审核");
        rod.setRetType("用户自建");

        
        rod.setRecvName("林好好");
        rod.setRecvArea("广西壮族自治区南宁市西乡塘区");
        rod.setRecvAddress("总部路1号");
        rod.setRecvMobile("139123456789");
        rod.setRecvPhone("0771-12345678");

        
        rod.setUserId("mlw_124567890");
        rod.setUserNickName("我是黄鼠狼");
        rod.setUserEmail("yuxiong@sohu.com");
        rod.setUserEmailVerified(true);
        rod.setUserMobile("139123456789");
        rod.setUserMobileVerified(false);
        rod.setUserAfterSaleScore(5);


        
        rod.setCheckResult("同意");
        rod.setCheckResult("情况属实，同意退款给用户了");
        rod.setCheckTime(new Date());

        List<RetOrderLog> logs = new ArrayList<RetOrderLog>();

        RetOrderLog log = new RetOrderLog();
        log.setCreateTime(new Date());
        log.setOptContent("您的退换货申请已提交，正在等待相关人员审核");
        log.setOptId("10");
        log.setOptMan("客户");
        log.setOptResult("同意");
        
        logs.add(log);


        log = new RetOrderLog();
        log.setCreateTime(new Date());
        log.setOptContent("您的审核已通过，同意退换货");
        log.setOptId("11");
        log.setOptMan("客服");
        log.setOptResult("同意");
        
        logs.add(log);

        log = new RetOrderLog();
        log.setCreateTime(new Date());
        log.setOptContent("您的审核已通过，同意退换货");
        log.setOptId("11");
        log.setOptMan("客服");
        log.setOptResult("同意");
       
        logs.add(log);
        log = new RetOrderLog();
        log.setCreateTime(new Date());
        log.setOptContent("您的审核已通过，同意退换货");
        log.setOptId("11");
        log.setOptMan("客服");
        log.setOptResult("同意");
       
        logs.add(log);
        log = new RetOrderLog();
        log.setCreateTime(new Date());
        log.setOptContent("您的审核已通过，同意退换货");
        log.setOptId("11");
        log.setOptMan("客服");
        log.setOptResult("同意");
       
        logs.add(log);
        List<RetOrderGoodsItem> items = new ArrayList<RetOrderGoodsItem>();

        RetOrderGoodsItem item = new RetOrderGoodsItem();
        item.setProId(12345678);
        item.setProSn("12345678");
        item.setProName("泰国精油香薰哦");
        item.setBuyNum(10);
        item.setRetNum(5);
        items.add(item);

        item = new RetOrderGoodsItem();
        item.setProId(12345671);
        item.setProSn("12345671");
        item.setProName("美丽湾按摩服务一个钟");
        item.setBuyNum(50);
        item.setRetNum(2);
        items.add(item);

        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        item = new RetOrderGoodsItem();
        item.setProId(12345672);
        item.setProSn("2222222");
        item.setProName("美丽湾冰火两重天服务两个钟");
        item.setBuyNum(100);
        item.setRetNum(3);
        items.add(item);
        
        rod.setRetOrderGoodsItems(items);
        rod.setRetOrderLogs(logs);
       
        List<BaseReportVO> mainList = new ArrayList<BaseReportVO>();
        mainList.add(rod);
        return mainList;
    }
    
	@Override
	public void dispose(JRDataSource arg0) throws JRException {
		// TODO Auto-generated method stub
		
	}
}