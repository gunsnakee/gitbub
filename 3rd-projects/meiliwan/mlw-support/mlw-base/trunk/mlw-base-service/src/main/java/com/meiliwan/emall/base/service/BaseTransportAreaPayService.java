package com.meiliwan.emall.base.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.base.bean.AreaPayKey;
import com.meiliwan.emall.base.bean.BaseTransportAreaPay;
import com.meiliwan.emall.base.constant.Constants;
import com.meiliwan.emall.base.dao.BaseTransportAreaPayDao;
import com.meiliwan.emall.base.dto.TransportAreaDTO;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

/**
 * 
 * @author yinggao.zhuo
 * @date 2013-6-5
 */
@Service
public class BaseTransportAreaPayService extends DefaultBaseServiceImpl {

	private MLWLogger logger = MLWLoggerFactory.getLogger(getClass());
	// 状态有效
	public static final short STATE_VALID = 1;
	
	@Autowired
	private BaseTransportAreaPayDao baseTransportAreaPayDao;
	
	public BaseTransportAreaPay findByPK(JsonObject resultObj, AreaPayKey pk) {
		BaseTransportAreaPay bean = baseTransportAreaPayDao.getEntityById(pk);
		JSONTool.addToResult(bean, resultObj);
		return bean;
	}

	public int update(JsonObject resultObj, BaseTransportAreaPay pay) {

		if (pay != null) {
			deleteCacheTransportAreaCode(pay.getAreaCode());
		}
		// TODO Auto-generated method stub
		int result = baseTransportAreaPayDao.update(pay);
		// try {
		// ShardJedisTool.getInstance().del(JedisKey.base$transAreaAndCOD, "");
		// } catch (JedisClientException e) {
		// // TODO Auto-generated catch block
		// logger.error(e, pay, null);
		// }
		return result;

	}

	public int delete(JsonObject resultObj, AreaPayKey key) {

		if (key == null) {
			throw new ServiceException(
					"BaseTransportAreaPayService - delete{}",
					"AreaPayKey is null");
		}
		if (key != null) {
			deleteCacheTransportAreaCode(key.getAreaCode());
		}
		int result = baseTransportAreaPayDao.delete(key);
		return result;
	}

	/**
	 * 增加商品品牌
	 * 
	 * @param BaseTransportAreaPay
	 *            proBrand
	 * @return i,成功为1,其他不成功
	 */
	public int add(JsonObject resultObj, BaseTransportAreaPay bean) {
		if (bean != null) {
			deleteCacheTransportAreaCode(bean.getAreaCode());
		}
		int result = baseTransportAreaPayDao.insert(bean);
		return result;
	}

	/**
	 * bean是含有companyId
	 * 
	 * @param resultObj
	 * @param ids
	 * @param addOrUpdateBean
	 */
	public void addOrUpdate(JsonObject resultObj, String[] ids,
			BaseTransportAreaPay addOrUpdateBean) {

		if (ids == null || ids.length == 0) {
			throw new ServiceException("the areaCode  is null");
		}
		if (addOrUpdateBean == null
				|| addOrUpdateBean.getTransCompanyId() == null
				|| addOrUpdateBean.getTransCompanyId().intValue() <= 0) {
			throw new ServiceException(
					"the BaseTransportAreaPay or companyId  is null");
		}
		// 所有areaCode
		Set<String> allIds = new HashSet<String>();
		for (String areaCode : ids) {
			allIds.add(areaCode);
			deleteCacheTransportAreaCode(areaCode);
		}

		Set<String> updateIds = new HashSet<String>();
		List<BaseTransportAreaPay> updateBeans = baseTransportAreaPayDao
				.getAreaIdExists(addOrUpdateBean.getTransCompanyId(), allIds);

		// 经过一轮循环,all变成add,这里报 线程安全异常,使用ArrayList.remove(int) 比较保险
		for (int j = 0; j < updateBeans.size(); j++) {
			updateIds.add(updateBeans.get(j).getAreaCode());
		}
		allIds.removeAll(updateIds);

		update(updateBeans, addOrUpdateBean);
		add(allIds, addOrUpdateBean);
	}

	/**
	 * 更新 这里可以优化成一条SQL语句
	 * 
	 * @param updateIds
	 * @param price
	 * @param freeSupport
	 * @param fullFreeSupport
	 */
	private void update(List<BaseTransportAreaPay> updateIds,
			BaseTransportAreaPay bean) {

		for (BaseTransportAreaPay aid : updateIds) {
			bean.setAreaCode(aid.getAreaCode());
			bean.setState(STATE_VALID);
			baseTransportAreaPayDao.update(bean);
		}
	}

	/**
	 * 添加
	 * 
	 * @param idList
	 * @param price
	 * @param freeSupport
	 * @param fullFreeSupport
	 */
	private void add(Collection<String> idList, BaseTransportAreaPay bean) {
		// TODO Auto-generated method stub
		for (String aid : idList) {
			// 含有公司ID
			bean.setAreaCode(aid);
			bean.setState(STATE_VALID);
			baseTransportAreaPayDao.insert(bean);
		}
	}

	public void pageTransportAreaServe(JsonObject resultObj, PageInfo pageInfo) {

		TransportAreaDTO dto = new TransportAreaDTO();
		dto.setTransCompanyId(Constants.DEFAULT_TRANS_ID);
		PagerControl<TransportAreaDTO> pages = baseTransportAreaPayDao.page(
				dto, pageInfo);
		JSONTool.addToResult(pages, resultObj);
	}

	

	/**
	 * 根据parentID得到
	 * 
	 * @param resultObj
	 * @param dto
	 *            parentID为必须
	 */
	public void getTansportAreaByPid(JsonObject resultObj, TransportAreaDTO dto) {
		dto.setState(STATE_VALID);
		List<TransportAreaDTO> list = baseTransportAreaPayDao
				.getTansportAreaByPid(dto);
		JSONTool.addToResult(list, resultObj);
	}

	/**
	 * 根据parentID得到
	 * 
	 * @param resultObj
	 * @param dto
	 *            parentID为必须
	 */
	public void getAreaByPid(JsonObject resultObj, TransportAreaDTO dto) {

		List<TransportAreaDTO> list = baseTransportAreaPayDao.getAreaByPid(dto);
		JSONTool.addToResult(list, resultObj);
	}

	/**
	 * 取得所有配送区域详情
	 * 
	 * @param resultObj
	 * @param dto
	 */
	public void getAllAreaPayDetail(JsonObject resultObj, TransportAreaDTO dto) {
		dto.setState(STATE_VALID);
		List<TransportAreaDTO> list = baseTransportAreaPayDao
				.getAllAreaPayDetail(dto);
		JSONTool.addToResult(list, resultObj);
	}

	

	/**
	 * 得到第四级货到付款,去除重复，不考虑物流公司，相当于合并,已物流公司为前提
	 * 
	 * @param resultObj
	 * @param dto
	 */
	public void getFourAreaCODByPid(JsonObject resultObj, TransportAreaDTO dto) {
		dto.setTransCompanyId(null);
		dto.setCashOnDelivery(Constants.BASE_CASH_ON_DELIVER_YES);
		List<TransportAreaDTO> listCod = baseTransportAreaPayDao
				.getTansportAreaByPid(dto);
		HashSet<String> set = new HashSet<String>();
		List<TransportAreaDTO> list = new ArrayList<TransportAreaDTO>();
		for (TransportAreaDTO area1 : listCod) {
			set.add(area1.getAreaCode());
		}
		boolean add = false;
		for (String code : set) {
			for (int i = 0; i < listCod.size() && !add; i++) {
				if (code.equals(listCod.get(i).getAreaCode())) {
					list.add(listCod.get(i));
					add = true;
				}
			}
			add = false;
		}
		JSONTool.addToResult(list, resultObj);
	}

	/**
	 * 删除全部品牌列表缓存
	 * 
	 * @param remark
	 */
	private void deleteCacheTransportAreaCode(String areaCode) {
		if (StringUtils.isEmpty(areaCode)) {
			return;
		}
		try {
			ShardJedisTool.getInstance().del(JedisKey.base$transportAreaCode,
					areaCode);
		} catch (JedisClientException e) {
			// TODO Auto-generated catch block
			logger.error(e, areaCode, null);
		}
	}

	/**
	 * 根据areaCode查询
	 * 
	 * @param resultObj
	 * @param dto
	 */
	public void selectByareaCode(JsonObject resultObj, BaseTransportAreaPay pay) {

		if (pay == null || StringUtils.isEmpty(pay.getAreaCode())) {
			return;
		}
		String listss = null;
		try {
			listss = ShardJedisTool.getInstance().get(
					JedisKey.base$transportAreaCode, pay.getAreaCode());
			if (listss != null) {
				List<BaseTransportAreaPay> areaDetails = new Gson().fromJson(
						listss, new TypeToken<List<BaseTransportAreaPay>>() {
						}.getType());
				JSONTool.addToResult(areaDetails, resultObj);
				return;
			}
		} catch (JedisClientException e1) {
			// TODO Auto-generated catch block
			logger.error(e1, listss, null);

		}

		List<BaseTransportAreaPay> list = baseTransportAreaPayDao
				.selectByareaCode(pay);

		String strs = new Gson().toJson(list);
		try {

			ShardJedisTool.getInstance().set(JedisKey.base$transportAreaCode,
					pay.getAreaCode(), strs);
		} catch (JedisClientException e) {
			// TODO Auto-generated catch block
			logger.error(e, list, null);
		}
		JSONTool.addToResult(list, resultObj);
	}

	/**
	 * 根据实体查询
	 * 
	 * @param resultObj
	 * @param dto
	 */
	// public void selectByEntity(JsonObject resultObj,BaseTransportAreaPay
	// pay){
	// List<BaseTransportAreaPay> list =
	// baseTransportAreaPayDao.getEntityByObj(pay);
	// JSONTool.addToResult(list, resultObj);
	// }

}