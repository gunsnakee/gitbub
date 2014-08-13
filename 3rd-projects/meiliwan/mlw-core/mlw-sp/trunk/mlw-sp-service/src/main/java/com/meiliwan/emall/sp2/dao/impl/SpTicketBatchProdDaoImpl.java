package com.meiliwan.emall.sp2.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.sp2.bean.SpTicketBatchProd;
import com.meiliwan.emall.sp2.dao.SpTicketBatchProdDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: wuzixin
 * Date: 14-5-28
 * Time: 下午4:26
 */
@Repository
public class SpTicketBatchProdDaoImpl extends BaseDao<Integer, SpTicketBatchProd> implements SpTicketBatchProdDao {
    @Override
    public String getMapperNameSpace() {
        return SpTicketBatchProdDao.class.getName();
    }

    @Override
    public int insertByBatchProd(List<SpTicketBatchProd> list) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        try {
            return getSqlSession().insert(getMapperNameSpace() + ".insertByBatchProd",
                    getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".insertByBatchProd: {}", list.toString(), e);
        }
    }

    @Override
    public List<SpTicketBatchProd> getTicketProdsByProIds(int[] ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", ids);
        List<SpTicketBatchProd> list = null;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + ".getTicketProdsByProIds", getShardParam(null, map, false));
            return list;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getTicketProdsByProIds: {}", ids.toString(), e);
        }
    }
    

    @Override
	public List<SpTicketBatchProd> getTicketProdsByProIdsAndBatchIds(
			int[] proIds, int[] batchIds) {
    	Map<String, Object> map = new HashMap<String, Object>();
        map.put("proIds", proIds);
        map.put("batchIds", batchIds);
        List<SpTicketBatchProd> list = null;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + ".getTicketProdsByProIdsAndBatchIds", getShardParam(null, map, false));
            return list;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getTicketProdsByProIdsAndBatchIds: proIds: " + proIds.toString() + ", batchIds {}",batchIds.toString() , e);
        }
	}

	@Override
    public int deleteProdById(int proId, int batchId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("proId", proId);
        map.put("batchId", batchId);
        try {
            return getSqlSession().delete(getMapperNameSpace() + ".deleteProdById", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".deleteProdById: {}", new String[]{String.valueOf(proId), String.valueOf(batchId)}, e);
        }
    }
}
