package com.meiliwan.emall.account.dao.impl;

import com.meiliwan.emall.account.bean.WalletOptRecord;
import com.meiliwan.emall.account.dao.WalletOptRecordDao;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 讨论对于校验走主库
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-7-10
 * Time: 下午9:52
 */
@Repository
public class WalletOptRecordDaoImpl extends BaseDao<Integer,WalletOptRecord> implements WalletOptRecordDao{
    @Override
    public String getMapperNameSpace() {
        return WalletOptRecordDao.class.getName();
    }

    @Override
    public Double incomeTotalAmount(Integer uid, Date beginDate, Date endDate) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("uid",uid);
        map.put("beginDate",beginDate);
        map.put("endDate",endDate);

        try{
        	if(getSqlSession().selectOne(getMapperNameSpace() + ".getIncomeTotalAmount", getShardParam(null, map, true)) == null){
        		return 0.00;
        	}else{
        		return  getSqlSession().selectOne(getMapperNameSpace() + ".getIncomeTotalAmount", getShardParam(null, map, true));
        	}
            
        }catch (Exception e){
            throw new ServiceException("service-" + getMapperNameSpace() + ".getIncomeTotalAmount: {}", map== null ? "" : map.toString(), e);
        }

    }

    @Override
    public Double paymentTotalAmount(Integer uid, Date beginDate, Date endDate) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("uid",uid);
        map.put("beginDate",beginDate);
        map.put("endDate",endDate);

        try{
        	if(getSqlSession().selectOne(getMapperNameSpace() + ".getPaymentTotalAmount", getShardParam(null, map, true)) == null){
        		return 0.00 ;
        	}else{
        		return  getSqlSession().selectOne(getMapperNameSpace() + ".getPaymentTotalAmount", getShardParam(null, map, true));
        	}
        }catch (Exception e){
            throw new ServiceException("service-" + getMapperNameSpace() + ".getPaymentTotalAmount: {}", map== null ? "" : map.toString(), e);
        }

    }

    @Override
    public List<Integer> getUidsByTime(Date beginDate, Date endDate) {
        Map<String,Date> map = new HashMap<String,Date>();
        map.put("beginDate",beginDate);
        map.put("endDate",endDate);
        try{
            return getSqlSession().selectList(getMapperNameSpace() + ".getUidsByTime", getShardParam(null, map, true));
        }catch (Exception e){
            throw new ServiceException("service-" + getMapperNameSpace() + ".getUidsByTime: {}", map== null ? "" : map.toString(), e);
        }
    }
}
