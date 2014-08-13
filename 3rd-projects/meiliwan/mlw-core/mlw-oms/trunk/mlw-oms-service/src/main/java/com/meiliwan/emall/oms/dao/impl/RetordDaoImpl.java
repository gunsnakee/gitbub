package com.meiliwan.emall.oms.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.oms.bean.Retord;
import com.meiliwan.emall.oms.dao.RetordDao;
import com.meiliwan.emall.oms.dto.export.RetOrdReportRow;

import org.springframework.stereotype.Repository;

/**
 * User: guangdetang
 * Date: 13-10-2
 * Time: 下午1:35
 */
@Repository
public class RetordDaoImpl extends BaseDao<String, Retord> implements RetordDao {
    @Override
    public String getMapperNameSpace() {
        return RetordDao.class.getName();
    }

	@Override
	public List<RetOrdReportRow> getRetOrderExcel(String starTime, String endTime) {
		Map<String,Object> whereMap = new HashMap<String, Object>();
		whereMap.put("starTime", DateUtil.parseDateTime(starTime));
		whereMap.put("endTime", DateUtil.parseDateTime(endTime));
		
		try {
            return getSqlSession().selectList(
                    getMapperNameSpace() + ".getRetOrderExcel",getShardParam(null, whereMap, false));
            
        } catch (Exception ex) {
            throw new ServiceException("RetordDao-" + getMapperNameSpace()
                    + ".getRetOrderExcel:"+whereMap, ex);
        }
	}
}
