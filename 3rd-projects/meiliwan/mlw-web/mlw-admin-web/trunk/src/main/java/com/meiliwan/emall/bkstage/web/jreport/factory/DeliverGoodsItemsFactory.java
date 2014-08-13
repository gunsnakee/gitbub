package com.meiliwan.emall.bkstage.web.jreport.factory;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSourceProvider;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.meiliwan.emall.bkstage.web.jreport.vo.DeliverGoodsItem;


public class DeliverGoodsItemsFactory extends JRAbstractBeanDataSourceProvider{
	
	

	public DeliverGoodsItemsFactory() {
		super(DeliverGoodsItem.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JRDataSource create(JasperReport arg0) throws JRException {
		List<DeliverGoodsItem> list = new ArrayList<DeliverGoodsItem>();
	      
	    return new JRBeanCollectionDataSource(list);
	}

	@Override
	public void dispose(JRDataSource arg0) throws JRException {
		// TODO Auto-generated method stub
		
	}
	
}