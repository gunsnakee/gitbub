package com.meiliwan.emall.base.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.base.bean.BaseTransportPrice;
import com.meiliwan.emall.base.dao.BaseTransportPriceDao;
import com.meiliwan.emall.base.dto.TransportPriceDTO;
import com.meiliwan.emall.base.dto.TransportPriceTip;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.core.db.BaseDao;

@Repository
public class BaseTransportPriceDaoImpl extends
		BaseDao<String, BaseTransportPrice> implements BaseTransportPriceDao {

	@Override
	public String getMapperNameSpace() {
		// TODO Auto-generated method stub
		return BaseTransportPriceDao.class.getName();
	}
	
	@Override
	public JedisKey getUseJedisCacheKey() {
		// TODO Auto-generated method stub
		return JedisKey.base$transportPrice;
	}
	
	/* (non-Javadoc)
	 * @see com.meiliwan.emall.base.dao.impl.BaseTransportPriceDao#getListByParentCode(java.lang.String)
	 */
	@Override
	public List<TransportPriceDTO> getListAreaFirstByParentCode(String parentCode){
		try{
			List<TransportPriceDTO> list = getSqlSession().selectList(getMapperNameSpace() + ".getListAreaFirstByParentCode",
					getShardParam(parentCode,parentCode,false));
			return list;
		} catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getListByParentCode: {}",parentCode, e);
        }        
	}
	
	@Override
	public PagerControl<TransportPriceTip> page(PageInfo pageInfo) {
		// TODO Auto-generated method stub
		
		PagerControl<TransportPriceTip> pagerControl = new PagerControl<TransportPriceTip>();
        pageInfo.startTime();
        
        List<TransportPriceTip> list = getSqlSession().selectList(getMapperNameSpace() + ".getListProvinceCityCountry",
        		getShardParam(null,getMapParams(null,pageInfo, null, null),false));
        
        Integer total = getSqlSession().selectOne(getMapperNameSpace() + ".getListProvinceCityCountryTotal",
        		getShardParam(null,getMapParams(null,null, null, null),false));
        pageInfo.endTime();
        pageInfo.setTotalCounts(total);
        if (list != null) {
            pagerControl.setEntityList(list);
        }
        pagerControl.setPageInfo(pageInfo);
        return pagerControl;
        
	}

	@Override
	public int deleteByAreaCodes(String[] areaCodes) {
		// TODO Auto-generated method stub
		
		for (String areaCode : areaCodes) {
			deleteCacheByPk(areaCode);
		}
		 try {
            return getSqlSession().delete(getMapperNameSpace() + ".deleteByAreaCodes",
                    getShardParam(null, areaCodes, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".deleteByAreaCodes: {}", areaCodes, e);
        }
	}

	@Override
	public List<TransportPriceDTO> getListPriceFirstByParentCode(
			String parentCode) {
		try{
			List<TransportPriceDTO> list = getSqlSession().selectList(getMapperNameSpace() + ".getListPriceFirstByParentCode",
					getShardParam(parentCode,parentCode,false));
			return list;
		} catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getListPriceFirstByParentCode: {}",parentCode, e);
        }        
	}
}