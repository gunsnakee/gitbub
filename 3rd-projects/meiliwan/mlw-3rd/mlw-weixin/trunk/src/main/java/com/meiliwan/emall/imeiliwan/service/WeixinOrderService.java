package com.meiliwan.emall.imeiliwan.service;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.imeiliwan.bean.WeixinOrder;
import com.meiliwan.emall.imeiliwan.dao.WeixinOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeixinOrderService {
	
	@Autowired
	private WeixinOrderDao weixinOrderDao;

	
	public WeixinOrder getWeixinOrderById(int id){
		if(id<=0){
			throw new ServiceException("the id can not less than 0");
		}
        WeixinOrder wxOrd = weixinOrderDao.getEntityById(id);
		return wxOrd;
	}
	
	public PagerControl<WeixinOrder> getPageWeixinOrder(PageInfo pageInfo){
		
		PagerControl<WeixinOrder> pc = weixinOrderDao.getPagerByObj(null, pageInfo, null);
		return pc;
	}
	
	public Integer add(WeixinOrder weixinOrder){
		if(weixinOrder==null){
			throw new ServiceException("the weixinOrder can not be null");
		}
        int id = weixinOrderDao.insert(weixinOrder);
        return id;
	}
	
	public void update(WeixinOrder weixinOrder){
		if(weixinOrder==null){
			throw new ServiceException("the weixinOrder can not be null");
		}
		if(weixinOrder.getId()!=null){
			throw new ServiceException("the weixinOrder is not valid");
    }
        weixinOrderDao.update(weixinOrder);
	}

    /**
     * 根据mlw订单查号询微信订单
     * @param outTradeNo
     * @return
     */
    public WeixinOrder getWxOrdByOrdId(String outTradeNo){
        WeixinOrder queryObj = new WeixinOrder();
        queryObj.setOutTradeNo(outTradeNo);
        List<WeixinOrder> list = weixinOrderDao.getListByObj(queryObj);
        if(list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }

}
