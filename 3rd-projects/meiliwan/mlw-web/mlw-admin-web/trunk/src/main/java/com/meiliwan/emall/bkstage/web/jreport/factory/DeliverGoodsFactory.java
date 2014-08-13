package com.meiliwan.emall.bkstage.web.jreport.factory;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSourceProvider;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.meiliwan.emall.bkstage.web.jreport.vo.BaseReportVO;
import com.meiliwan.emall.bkstage.web.jreport.vo.DeliverGoods;
import com.meiliwan.emall.bkstage.web.jreport.vo.DeliverGoodsItem;

public class DeliverGoodsFactory extends JRAbstractBeanDataSourceProvider {
	

    public DeliverGoodsFactory() {
       super(DeliverGoods.class);
    }
    public JRDataSource create(JasperReport arg0) throws JRException {
       /**
        *测试数据，在使用中，不需要继承JRAbstractBeanDataSourceProvider，
        *只需要把集合类封装到JRBeanCollectionDataSource中就可以了
        **/
    	List<BaseReportVO> mainList = new ArrayList<BaseReportVO>();
        DeliverGoods goods = new DeliverGoods();
        goods.setOrderId("1234567");
        goods.setRecvName("杜拉拉1");
        goods.setMobile("13952850012");
        
        goods.setComment("测试备注稀里哗啦测试备注稀里哗啦测试备注稀里哗啦测试备注稀里哗啦测试备注稀里哗啦测试备注稀里哗啦测试备注稀里哗啦测试备注稀里哗啦测试备注稀里哗啦测试备注稀里哗啦测试备注稀");
        List<DeliverGoodsItem> list = new ArrayList<DeliverGoodsItem>();
        DeliverGoodsItem dgi = new DeliverGoodsItem();
        dgi.setProName("护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1");
        dgi.setProSn("123");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴2");
        dgi.setProSn("124");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴3");
        dgi.setProSn("125");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴4");
        dgi.setProSn("126");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴5");
        dgi.setProSn("127");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴6");
        dgi.setProSn("128");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴7");
        dgi.setProSn("129");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴8");
        dgi.setProSn("130");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴9");
        dgi.setProSn("131");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴10");
        dgi.setProSn("132");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴11");
        dgi.setProSn("133");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴12");
        dgi.setProSn("134");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴13");
        dgi.setProSn("135");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴14");
        dgi.setProSn("136");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴15");
        dgi.setProSn("137");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴16");
        dgi.setProSn("138");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴17");
        dgi.setProSn("139");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴18");
        dgi.setProSn("140");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴19");
        dgi.setProSn("141");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴20");
        dgi.setProSn("142");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴21");
        dgi.setProSn("143");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴22");
        dgi.setProSn("144");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴23");
        dgi.setProSn("145");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴24");
        dgi.setProSn("146");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴25");
        dgi.setProSn("147");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴26");
        dgi.setProSn("148");
        list.add(dgi);
        goods.setDeliverGoodsItems(list);
        goods.setTotalItem(list.size());
        mainList.add(goods);
        /**测试数据自写*/
      
       return new JRBeanCollectionDataSource(mainList);
    }
    
    public static List<BaseReportVO> getBeanCollection() {
    	/**
         *测试数据，在使用中，不需要继承JRAbstractBeanDataSourceProvider，
         *只需要把集合类封装到JRBeanCollectionDataSource中就可以了
         **/
    	List<BaseReportVO> mainList = new ArrayList<BaseReportVO>();
        DeliverGoods goods = new DeliverGoods();
        goods.setOrderId("1234567");
        goods.setRecvName("杜拉拉1");
        goods.setMobile("13952850012");
        goods.setComment("测试备注稀里哗啦测试备注稀里哗啦测试备注稀里哗啦测试备注稀里哗啦测试备注稀里哗啦测试备注稀里哗啦测试备注稀里哗啦测试备注稀里哗啦测试备注稀里哗啦测试备注稀里哗啦测试备注稀");
        List<DeliverGoodsItem> list = new ArrayList<DeliverGoodsItem>();
        DeliverGoodsItem dgi = new DeliverGoodsItem();
        dgi.setProName("护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1护舒宝1");
        dgi.setProSn("123");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴2");
        dgi.setProSn("124");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴3");
        dgi.setProSn("125");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴4");
        dgi.setProSn("126");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴5");
        dgi.setProSn("127");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴6");
        dgi.setProSn("128");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴7");
        dgi.setProSn("129");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴8");
        dgi.setProSn("130");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴9");
        dgi.setProSn("131");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴10");
        dgi.setProSn("132");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴11");
        dgi.setProSn("133");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴12");
        dgi.setProSn("134");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴13");
        dgi.setProSn("135");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴14");
        dgi.setProSn("136");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴15");
        dgi.setProSn("137");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴16");
        dgi.setProSn("138");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴17");
        dgi.setProSn("139");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴18");
        dgi.setProSn("140");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴19");
        dgi.setProSn("141");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴20");
        dgi.setProSn("142");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴21");
        dgi.setProSn("143");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴22");
        dgi.setProSn("144");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴23");
        dgi.setProSn("145");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴24");
        dgi.setProSn("146");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴25");
        dgi.setProSn("147");
        list.add(dgi);
        dgi = new DeliverGoodsItem();
        dgi.setProName("洁尔阴26");
        dgi.setProSn("148");
        list.add(dgi);
        goods.setDeliverGoodsItems(list);
        goods.setTotalItem(list.size());
        mainList.add(goods);
        
        return mainList;
    }
    
	@Override
	public void dispose(JRDataSource arg0) throws JRException {
		// TODO Auto-generated method stub
		
	}
}