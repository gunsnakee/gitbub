package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.Card;
import com.meiliwan.emall.pms.constant.Constant;
import com.meiliwan.emall.pms.dao.CardDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 礼品卡数据Dao层实现
 * User: wuzixin
 * Date: 13-12-25
 * Time: 下午3:40
 */
@Repository
public class CardDaoImpl extends BaseDao<String, Card> implements CardDao {
    @Override
    public String getMapperNameSpace() {
        return CardDao.class.getName();
    }

    @Override
    public Card getCardByPwd(String pwd) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", pwd);
        Card card = null;
        try {
            card = getSqlSession().selectOne(getMapperNameSpace() + ".getCardByPwd", getShardParam(null, map, true));
            return card;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getCardByPwd: {}", pwd, e);
        }
    }

    @Override
    public int insertByBatch(List<Card> cards) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", cards);
        try {
            return getSqlSession().insert(getMapperNameSpace() + ".insertByBatch",
                    getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".insertByBatch: {}", cards.toString(), e);
        }
    }

    @Override
    public int updateActiveCard(String cardId,int userId,String userName) {
        Map<String,Object> map = getMap(Constant.CARDACTIVE);
        map.put("id",cardId);
        map.put("userId",userId);
        map.put("userName",userName);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateActiveCard", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateActiveCard: {},{}", cardId, e);
        }
    }

    @Override
    public int updateFreezeCard(String cardId, int state) {
        Map<String,Object> map = getMap(state);
        map.put("id",cardId);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateFreezeCard", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateFreezeCard: {},{}", new String[]{cardId, String.valueOf(state)}, e);
        }
    }

    @Override
    public int updateDeleteCard(String[] cardIds) {
        Map<String,Object> map = getMap(Constant.CARDDEL);
        map.put("ids",cardIds);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateDeleteCard", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateDeleteCard: {},{}", cardIds, e);
        }
    }

    @Override
    public List<Card> getCardsByIds(String[] cardIds) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", cardIds);
        List<Card> list = null;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + ".getCardsByIds", getShardParam(null, map, false));
            return list;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getCardsByIds: {}", cardIds.toString(), e);
        }
    }

    @Override
    public int updateFreezeByBatchId(String batchId) {
        Map<String,Object> map = getMap(Constant.CARDFREEZE);
        map.put("id",batchId);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateActiveCard", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateActiveCard: {},{}", map.toString(), e);
        }
    }

    @Override
    public int updateSellCard(String cardId, String field, String data) {
        Map<String,Object> map = getMap(Constant.CARDSELL);
        map.put("id",cardId);
        map.put("field",field);
        map.put("data",data);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateSellCard", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateSellCard: {},{}", map.toString(), e);
        }
    }

    @Override
    public int updateSellByImport(String cardId, String field, String data, String sellerName) {
        Map<String,Object> map = getMap(Constant.CARDSELL);
        map.put("id",cardId);
        map.put("field",field);
        map.put("data",data==null?"":data);
        map.put("sellerName",sellerName);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateSellByImport", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateSellByImport: {},{}", map.toString(), e);
        }    }

    @Override
    public List<Card> getCardWithBatchByIds(String[] ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", ids);
        List<Card> list = null;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + ".getCardWithBatchByIds", getShardParam(null, map, false));
            return list;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getCardWithBatchByIds: {}", ids.toString(), e);
        }
    }

    @Override
    public int getCountByUserId(int userId,String batchId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", userId);
        map.put("batchId", batchId);
        try {
            Object count = getSqlSession().selectOne(getMapperNameSpace() + ".getCountByUserId",getShardParam(null, map, false));
            return (Integer)count;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getCountByUserId: {}", userId+","+batchId, e);
        }
    }

    private Map<String, Object> getMap(int state) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("time", DateUtil.getCurrentDateTimeStr());
        map.put("state", state);
        return map;
    }
}
