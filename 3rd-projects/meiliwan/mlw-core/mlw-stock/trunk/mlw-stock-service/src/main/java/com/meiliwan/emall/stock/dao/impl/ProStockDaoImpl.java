package com.meiliwan.emall.stock.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.stock.bean.ProStock;
import com.meiliwan.emall.stock.bean.SafeStockItem;
import com.meiliwan.emall.stock.bean.StockItem;
import com.meiliwan.emall.stock.dao.ProStockDao;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("proStockDaoImpl")
public class ProStockDaoImpl extends BaseDao<Integer, ProStock> implements ProStockDao {

    @Override
    public JedisKey getUseJedisCacheKey() {
        return JedisKey.pms$stock;
    }

    @Override
    public String getMapperNameSpace() {
        return ProStockDao.class.getName();
    }

    @Override
    public int addStock(Integer id, Integer stock) {
        deleteCacheByPk(id);
        Map<String, Object> map = stockOpt(id, stock);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".addStock", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".addStock: {},{}", new String[]{id.toString(), stock.toString()}, e);
        }
    }

    @Override
    public int addStockByOrderCancel(Integer id, Integer stock) {
        deleteCacheByPk(id);
        Map<String, Object> map = stockOpt(id, stock);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".addStockByOrderCancel", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".addStockByOrderCancel: {},{}", new String[]{id.toString(), stock.toString()}, e);
        }
    }

    @Override
    public int subStock(Integer id, Integer stock) {
        deleteCacheByPk(id);
        Map<String, Object> map = stockOpt(id, stock);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".subStock", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".subStock: {},{}", new String[]{id.toString(), stock.toString()}, e);
        }
    }

    @Override
    public int subStockByOrder(Integer id, Integer stock) {
        deleteCacheByPk(id);
        Map<String, Object> map = stockOpt(id, stock);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".subStockByOrder", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".subStockByOrder: {},{}", new String[]{id.toString(), stock.toString()}, e);
        }
    }

    @Override
    public int subStockByAdminSend(Integer id, Integer stock) {
        deleteCacheByPk(id);
        Map<String, Object> map = stockOpt(id, stock);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".subStockByAdminSend", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".subStockByAdminSend: {},{}", new String[]{id.toString(), stock.toString()}, e);
        }
    }


    @Override
    public List<StockItem> getSellStockByIds(int[] ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", ids);
        List<StockItem> list = null;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + ".getSellStockByIds", getShardParam(null, map, false));
            return list;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getSellStockByIds: {}", ids.toString(), e);
        }
    }

    @Override
    public int addUnsellStock(Integer id, Integer stock) {
        deleteCacheByPk(id);
        Map<String, Object> map = stockOpt(id, stock);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".addUnsellStock", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".addUnsellStock: {},{}", new String[]{id.toString(), stock.toString()}, e);
        }
    }

    @Override
    public List<ProStock> getListByIds(int[] ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", ids);
        List<ProStock> list = null;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + ".getListByIds", getShardParam(null, map, false));
            return list;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getListByIds: {}", ids.toString(), e);
        }
    }

    @Override
    public List<SafeStockItem> getSafeStockList() {
        List<SafeStockItem> list = null;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + ".getSafeStockList", getShardParam(null, null, false));
            return list;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getSafeStockList: {}", "", e);
        }
    }

    @Override
    public int updateSafeStock(Integer id, Integer stock) {
        deleteCacheByPk(id);
        Map<String, Object> map = stockOpt(id, stock);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateSafeStock", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateSafeStock: {},{}", new String[]{id.toString(), stock.toString()}, e);
        }
    }

    @Override
    public SafeStockItem getProIdByBarCode(String barCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", barCode);
        SafeStockItem item = null;
        try {
            item = getSqlSession().selectOne(getMapperNameSpace() + ".getProIdByBarCode", getShardParam(null, map, false));
            return item;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getProIdByBarCode: {}", barCode, e);
        }
    }

    private Map<String, Object> stockOpt(Integer id, Integer num) {
        Map<String, Object> map = new HashMap<String, Object>();
        Timestamp updateTime = DateUtil.getCurrentTimestamp();
        map.put("id", id);
        map.put("num", num);
        map.put("updateTime", updateTime);
        return map;
    }
}
