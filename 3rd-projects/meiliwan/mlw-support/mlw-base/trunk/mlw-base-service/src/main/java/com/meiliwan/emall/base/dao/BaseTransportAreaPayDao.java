package com.meiliwan.emall.base.dao;

import java.util.Collection;
import java.util.List;

import com.meiliwan.emall.base.bean.AreaPayKey;
import com.meiliwan.emall.base.bean.BaseTransportAreaPay;
import com.meiliwan.emall.base.dto.TransportAreaDTO;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.core.db.IDao;

public interface BaseTransportAreaPayDao extends IDao<AreaPayKey,BaseTransportAreaPay> {

	List<BaseTransportAreaPay> getAreaIdExists(Integer integer, Collection<String> ids);
	
	/**
	 * 后台分页
	 * @param dto
	 * @param pageinfo
	 * @return
	 */
	PagerControl<TransportAreaDTO> page(TransportAreaDTO dto,PageInfo pageinfo);
	
	List<TransportAreaDTO> getTansportAreaByPid(TransportAreaDTO dto);

	List<TransportAreaDTO> getAreaByPid(TransportAreaDTO dto);

	List<TransportAreaDTO> getAllAreaPayDetail(TransportAreaDTO dto);
	
	List<BaseTransportAreaPay> selectByareaCode(BaseTransportAreaPay pay);

	/**
	 * 更新有效ByCompanyId
	 * @param id
	 * @param stateUnvalid
	 */
	void updateStateValidByCompanyId(int companyId);

	/**
	 * 更新无效ByCompanyId
	 * @param id
	 * @param stateUnvalid
	 */
	void updateStateInValidByCompanyId(int companyId);
	
	/**
	 * 批量更新
	 * @param pay
	 * @param ids
	 */
	void updateByAreaCodeBatch(BaseTransportAreaPay pay, Collection<String> ids);
}