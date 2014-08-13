package com.meiliwan.emall.base.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.base.bean.BaseTransportCompany;
import com.meiliwan.emall.base.dao.BaseTransportAreaPayDao;
import com.meiliwan.emall.base.dao.BaseTransportCompanyDao;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

/**
 * 物流公司管理 
 * @author yinggao.zhuo
 * @date 2013-6-5
 */
@Service
public class BaseTransportCompanyService extends DefaultBaseServiceImpl{

    @Autowired
    private BaseTransportCompanyDao baseTransportCompanyDao;

    @Autowired
    private BaseTransportAreaPayDao baseTransportAreaPayDao;
    
    public BaseTransportCompany findById(JsonObject resultObj,
			int id) {
    		BaseTransportCompany bean = baseTransportCompanyDao.getEntityById(id);
		JSONTool.addToResult(bean, resultObj);
		return bean;
	}

    /**
     * 软删除
     * @param resultObj
     * @param id
     * @return
     */
    public void del(JsonObject resultObj,int id) {
	   
	    	BaseTransportCompany bean = new BaseTransportCompany();
	    	bean.setId(id);
	    	bean.setIsDel((short)-1);
		baseTransportCompanyDao.update(bean);
		
	}

   /**
     * 增加物流公司管理
     *
     * @param ProBrand proBrand
     * @return i,成功为1,其他不成功
     */
    public void add(JsonObject resultObj,BaseTransportCompany company) {
    		
    		addValidate(company);
    		company.setStateValid();
    		company.setIsDefaultNo();
    		company.setIsDelNo();
        baseTransportCompanyDao.insert(company);
    }
	
    private void addValidate(BaseTransportCompany bean) throws ServiceException{
    		
    		if(bean==null){
			throw new ServiceException("company can not be null");
		}
    		if(StringUtils.isEmpty(bean.getName())){
    			throw new ServiceException("company's name can not be null");
    		}
    		if(bean.getSupportCash()==null){
    			throw new ServiceException("company supportCash can not be null");
    		}
    		if(bean.getSupportCheck()==null){
    			throw new ServiceException("company SupportCheck can not be null");
    		}
    		if(bean.getSupportPos()==null){
    			throw new ServiceException("company SupportPos can not be null");
    		}
    		if(bean.getAdminId()==null){
    			throw new ServiceException("company AdminId can not be null");
    		}
    }

	public int update(JsonObject resultObj, BaseTransportCompany company) {

		//addValidate(company);
		if (company.getId() == null) {
			throw new ServiceException("company id can not be null");
		}
		int result = baseTransportCompanyDao.update(company);
		if (!company.isValid() || company.isDel()) {
			baseTransportAreaPayDao.updateStateInValidByCompanyId(company.getId());
		}
		if (company.isValid()) {
			baseTransportAreaPayDao.updateStateValidByCompanyId(company.getId());
		}
		return result;
	}
    
	
	
	public PagerControl<BaseTransportCompany> page(JsonObject resultObj,
			BaseTransportCompany bean, PageInfo pageInfo,String whereSql,
			String orderBySql) {
    	
    	PagerControl<BaseTransportCompany> pages = baseTransportCompanyDao.getPagerByObj(bean, pageInfo, whereSql, orderBySql);
		JSONTool.addToResult(pages, resultObj);
		return pages;
	}
	
}