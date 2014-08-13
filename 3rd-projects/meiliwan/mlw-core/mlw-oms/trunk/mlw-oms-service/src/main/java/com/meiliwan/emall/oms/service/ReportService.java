package com.meiliwan.emall.oms.service;


import static com.meiliwan.emall.icetool.JSONTool.addToResult;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.oms.bean.Ord;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.dao.OrdDao;
import com.meiliwan.emall.oms.dao.OrdiDao;
import com.meiliwan.emall.oms.dto.OrdDetailDTO;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;


/**
 * 品牌Service
 * 
 * @author yinggao.zhuo
 * 
 */
@Service
public class ReportService extends DefaultBaseServiceImpl {

	
	@Autowired
	private OrdDao ordDao;
	@Autowired
	private OrdiDao ordiDao;

	/**
	 * 根据订单号，获取订单及订单行商品信息
	 * @param resultObj
	 * @param orderIds
	 */
	public void getOrdersAndItems(JsonObject resultObj, String[] orderIds){
		if( null== orderIds || orderIds.length ==0){
			addToResult(orderIds, resultObj);
			return;
		}

        List<OrdDetailDTO> dtos = new ArrayList<OrdDetailDTO>(orderIds.length);
        for(String orderId : orderIds){
            //查询订单信息
            Ord ord = ordDao.getEntityById(orderId);
            if(ord == null) throw new ServiceException("ReportService-getOrdersAndItems {}", orderId);

            //订单行列表
            List<Ordi> ordiList = ordiDao.getOrdIListByOrdId(orderId);
            OrdDetailDTO dto = new OrdDetailDTO();
            dto.setOrd(ord);
            dto.setOrdiList(ordiList);
            dtos.add(dto);
        }
        addToResult(dtos, resultObj);
	}



}