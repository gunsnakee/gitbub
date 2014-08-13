package com.meiliwan.emall.core.db;


import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.core.bean.BaseEntity;
import com.meiliwan.emall.core.cache.JedisDaoCache;
import com.meiliwan.emall.core.db.shard.ShardParam;
import com.meiliwan.emall.core.db.shard.spring.support.SqlSessionDaoSupport;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Created by Sean on 13-5-22.
 */
@SuppressWarnings("unchecked")
public abstract class BaseDao<T extends Serializable, M extends BaseEntity>
        extends SqlSessionDaoSupport implements IDao<T, M>, JedisDaoCache {

    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    public static final String insertSelective = ".insertSelective";
    public static final String updateSelective = ".updateByPrimaryKeySelective";
    public static final String selectByPrimaryKey = ".selectByPrimaryKey";
    public static final String selectByPrimaryKeys = ".selectByPrimaryKeys";
    public static final String getListByEntityAndPageInfo = ".getListByEntityAndPageInfo";
    public static final String getTotalByEntity = ".getTotalByEntity";
    public static final String deleteByPrimaryKey = ".deleteByPrimaryKey";
    public static final String deleteByEntity = ".deleteByEntity";
    public static final String defShard = "CommShard";
    //用作排序验证对比
    private Set<String> entityFields = new HashSet<String>();

    private Class<M> curClassType = null;

    @Override
    public JedisKey getUseJedisCacheKey() {
        return JedisKey.defaultNotUse;
    }

    //默认为缓存数据保存在内存当中
    @Override
    public int cacheSaveType() {
        return MEMORY;
    }

    private ShardJedisTool getShardJedisTool() throws JedisClientException {

        //一般不会出现
        return ShardJedisTool.getInstance();
    }

    public BaseDao() {
        if (getClass().getGenericSuperclass() instanceof ParameterizedType) {
            curClassType = (Class<M>) ((ParameterizedType) getClass()
                    .getGenericSuperclass()).getActualTypeArguments()[1];
            for (Field field : curClassType.getDeclaredFields()) {
                if ("serialVersionUID".equals(field.getName())) continue;
                StringBuilder sb = new StringBuilder();
                for (char c : field.getName().toCharArray()) {
                    if (Character.isUpperCase(c)) {
                        sb.append("_").append(Character.toLowerCase(c));
                    } else {
                        sb.append(c);
                    }
                }
                entityFields.add(sb.toString());
            }
        }
    }

    @Override
    public boolean deleteCacheByPk(T pk) {
        boolean optSuc = false;
        if (JedisKey.defaultNotUse != getUseJedisCacheKey()) {
            try {
                optSuc = getShardJedisTool().del(getUseJedisCacheKey(), pk);
            } catch (JedisClientException e) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("pk", pk);
                logger.error(e, map, "");
            }
        }
        return optSuc;
    }

    public ShardParam getShardParam(Serializable shardValue, Object entity, boolean isUpdate) {
        ShardParam shardParam = new ShardParam();
        if (shardValue != null)
            shardParam.setShardValue(shardValue);
        shardParam.setName(getShardName());
        shardParam.setParams(entity);
        shardParam.setUpdate(isUpdate);
        return shardParam;
    }

    public String getOrderBySql(PageInfo pageInfo) {
        if (pageInfo == null) return null;
        StringBuilder sb = new StringBuilder();
        if (!Strings.isNullOrEmpty(pageInfo.getOrderField()) && entityFields.contains(pageInfo.getOrderField())) {
            sb.append("order by ")
                    .append(pageInfo.getOrderField())
                    .append(" ")
                    .append("desc".equals(pageInfo.getOrderDirection()) ? "desc" : "asc");
        }
        return sb.toString();
    }

    /**
     * 对应 Mapper NameSpace名称 做匹配
     *
     * @return
     */
    public abstract String getMapperNameSpace();

    /**
     * 分库分表名称
     *
     * @return
     */
    public String getShardName() {
        return defShard;
    }

    public Map<String, Object> getMapParams(M entity, PageInfo pageInfo, String whereSql, String orderBySql) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != entity) map.put("entity", entity);
        if (null != pageInfo) map.put("pageInfo", pageInfo);
        if (null != whereSql) map.put("whereSql", whereSql);
        if (null != orderBySql) map.put("orderBy", orderBySql);
        return map;
    }

    @Override
    public int insert(M entity) {
        try {
            return getSqlSession().insert(getMapperNameSpace() + insertSelective,
                    getShardParam(entity != null ? entity.getId() : null, entity, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".insert: {}", entity == null ? "" : entity.toString(), e);
        }
    }

    @Override
    public int update(M entity) {
        if (JedisKey.defaultNotUse != getUseJedisCacheKey()) {
            try {
                boolean isDelCache = getShardJedisTool().del(getUseJedisCacheKey(), entity.getId());
                logger.debug("delete jedis " + curClassType.getSimpleName() + " parse Json error " + " key:" + entity.getId() + ", delResult:" + isDelCache);
            } catch (JedisClientException e) {
                logger.error(e, entity, "");
            }
        }

        try {
            return getSqlSession().update(getMapperNameSpace() + updateSelective,
                    getShardParam(entity != null ? entity.getId() : null, entity, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".update: {}", entity == null ? "" : entity.toString(), e);
        }
    }

    @Override
    public int delete(T pk) {
        deleteCacheByPk(pk);
        try {
            return getSqlSession().delete(getMapperNameSpace() + deleteByPrimaryKey,
                    getShardParam(pk, pk, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".delete: {}", pk.toString(), e);
        }
    }

    @Override
    public int deleteByEntity(M entity) {
        try {
            return getSqlSession().delete(getMapperNameSpace() + deleteByEntity,
                    getShardParam(entity.getId(), entity, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".deleteByEntity: {}", entity.toString(), e);
        }
    }

    @Override
    public Map<T, M> getEntityByIds(List<T> pks, String idField) {
        if (pks == null || pks.size() == 0) {
            return null;
        }
        try {
            return getSqlSession().selectMap(getMapperNameSpace() + selectByPrimaryKeys, getShardParam(pks.get(0), pks, false), idField);
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getEntityByIds: {}", pks.get(0).toString(), e);
        }
    }

    @Override
    public Map<T, M> getEntityByIds(List<T> pks, String idField, boolean isMainDS) {
        if (pks == null || pks.size() == 0) {
            return null;
        }
        try {
            return getSqlSession().selectMap(getMapperNameSpace() + selectByPrimaryKeys, getShardParam(pks.get(0), pks, isMainDS), idField);
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getEntityByIds: {}", pks.get(0).toString(), e);
        }
    }

    @Override
    public List<M> getEntityByIds(List<T> pks) {
        return getEntityByIds(pks, false);
    }

    @Override
    public List<M> getEntityByIds(List<T> pks, boolean isMainDS) {
        if (pks == null || pks.size() == 0) {
            return null;
        }
        try {
            return getSqlSession().selectList(getMapperNameSpace() + selectByPrimaryKeys, getShardParam(pks.get(0), pks, isMainDS));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getEntityByIds: {}", pks.get(0).toString(), e);
        }
    }

    @Override
    public M getEntityById(T pk) {
      return getEntityById(pk,false);
    }

    public String isExistDBField(String valiField) {
        if (entityFields.contains(valiField)) {
            return valiField;
        } else {
            throw new ServiceException("service-" + getMapperNameSpace() + ".validateDBField: {" + valiField + "}");
        }
    }

    @Override
    public M getEntityById(T pk, boolean isMainDS) {
        M returnObj = null;
        //jedis 服务使用状态
        boolean jedisUseStatus = true;
        String entityJsonStr = null;
        //需要定义JedisKey  Dao实现层 覆写 getUseJedisCacheKey() 方法
        if (JedisKey.defaultNotUse != getUseJedisCacheKey()) {
            try {
                entityJsonStr = getShardJedisTool().get(getUseJedisCacheKey(), pk);
            } catch (JedisClientException e) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("pk", pk);
                map.put("isMainDS", isMainDS);
                logger.error(e, map, "");

                jedisUseStatus = false;
            } catch (Exception e) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("pk", pk);
                map.put("isMainDS", isMainDS);
                logger.error(e, map, "");

                jedisUseStatus = false;
            }
            try {
                //如果不为空
                if (!Strings.isNullOrEmpty(entityJsonStr)) {
                    logger.debug("load jedis " + curClassType.getSimpleName() + " key:" + pk + " value: " + entityJsonStr);
                    returnObj = new Gson().fromJson(entityJsonStr, curClassType);
                }
            } catch (JsonSyntaxException e) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("pk", pk);
                map.put("isMainDS", isMainDS);
                map.put("entityJsonStr", entityJsonStr);
                logger.error(e, map, "");
            }
        }
//        else {
//            logger.error("If you want to use the cache，you must override getUseJedisCacheKeys  method");
//        }
        if (returnObj != null) {
            return returnObj;
        }
        returnObj = getSqlSession().selectOne(getMapperNameSpace() + selectByPrimaryKey, getShardParam(pk, pk, isMainDS));
        if (JedisKey.defaultNotUse != getUseJedisCacheKey() && returnObj != null && jedisUseStatus) {
            String objJson = new Gson().toJson(returnObj, curClassType);
            try {
                boolean isInsertCache = getShardJedisTool().set(getUseJedisCacheKey(), pk, objJson);
                if (isInsertCache)
                    logger.debug("insert jedis " + curClassType.getSimpleName() + " key:" + pk + " value: " + objJson);
            } catch (JedisClientException e) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("pk", pk);
                map.put("isMainDS", isMainDS);
                map.put("returnObj", returnObj);
                logger.error(e, map, "");
            }
        }
        return returnObj;
    }

    @Override
    public M getEntityByIdUseDB(T pk) {
        return getEntityByIdUseDB(pk, false);
    }

    @Override
    public M getEntityByIdUseDB(T pk, boolean isMainDS) {
        return getSqlSession().selectOne(getMapperNameSpace() + selectByPrimaryKey, getShardParam(pk, pk, isMainDS));
    }

    @Override
    public M getEntityByObj(M entity) {
        try {
            return getEntityByObj(entity, null);
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getEntityByObj: {}", entity == null ? "" : entity.toString(), e);
        }
    }

    @Override
    public M getEntityByObj(M entity, boolean isMainDS) {
        try {
            return getEntityByObj(entity, null, isMainDS);
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getEntityByObj: {}", entity == null ? "" : entity.toString(), e);
        }

    }

    @Override
    public M getEntityByObj(M entity, String whereSql) {

        try {
            return getSqlSession().selectOne(getMapperNameSpace() + getListByEntityAndPageInfo,
                    getShardParam(entity != null ? entity.getId() : null, getMapParams(entity, null, whereSql, null), false));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getEntityByObj: {},{}", new String[]{entity == null ? "" : entity.toString(), whereSql}, e);
        }
    }

    @Override
    public M getEntityByObj(M entity, String whereSql, boolean isMainDS) {
        try {
            return getSqlSession().selectOne(getMapperNameSpace() + getListByEntityAndPageInfo,
                    getShardParam(entity != null ? entity.getId() : null, getMapParams(entity, null, whereSql, null), isMainDS));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getEntityByObj: {},{}", new String[]{entity == null ? "" : entity.toString(), whereSql}, e);
        }
    }

    @Override
    public int getCountByObj(M entity) {
        return getCountByObj(entity, false);
    }


    @Override
    public int getCountByObj(M entity, boolean isMainDS) {
        try {
            return getCountByObj(entity, null, isMainDS);
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getCountByObj: {}", entity == null ? "" : entity.toString(), e);
        }
    }

    @Override
    public int getCountByObj(M entity, String whereSql) {
        return getCountByObj(entity, whereSql, false);
    }

    @Override
    public int getCountByObj(M entity, String whereSql, boolean isMainDS) {

        try {

            Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + getTotalByEntity,
                    getShardParam(entity != null ? entity.getId() : null, getMapParams(entity, null, whereSql, null), isMainDS));
            return (Integer) selectOne;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getCountByObj: {},{}", new String[]{entity == null ? "" : entity.toString(), whereSql}, e);
        }
    }

    @Override
    public PagerControl<M> getPagerByObj(M entity, PageInfo pageInfo, String whereSql, String orderBySql) {
        return getPagerByObj(entity, pageInfo, whereSql, orderBySql, false);
    }

    @Override
    public PagerControl<M> getPagerByObj(M entity, PageInfo pageInfo, String whereSql, String orderBySql, boolean isMainDS) {
        PagerControl<M> pagerControl = new PagerControl<M>();
        pageInfo.startTime();
        List<M> list = new ArrayList<M>();
        int total = 0;
        try {
            total = getCountByObj(entity, whereSql, isMainDS);
            if(total > 0){
                list = getListByObj(entity, pageInfo, whereSql, orderBySql, isMainDS);
            }
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getPagerByObj-" + e.getMessage() + ": {},{},{},{}", new String[]{entity == null ? "" : entity.toString(),
                    pageInfo.toString(), whereSql, orderBySql}, e);
        }

        pageInfo.endTime();
        pageInfo.setTotalCounts(total);
        if (list != null) {
            pagerControl.setEntityList(list);
        }
        pagerControl.setPageInfo(pageInfo);
        return pagerControl;
    }

    @Override
    public PagerControl<M> getPagerByObj(M entity, PageInfo pageInfo, String whereSql) {
        return getPagerByObj(entity, pageInfo, whereSql, null, false);
    }

    @Override
    public PagerControl<M> getPagerByObj(M entity, PageInfo pageInfo, String whereSql, boolean isMainDS) {
        return getPagerByObj(entity, pageInfo, whereSql, null, isMainDS);
    }


    @Override
    public List<M> getAllEntityObj() {
        return getListByObj(null, null, false);
    }

    @Override
    public List<M> getAllEntityObj(boolean isMainDS) {
        return getListByObj(null, null, isMainDS);
    }

    @Override
    public List<M> getListByObj(M entity) {
        return getListByObj(entity, null, null, null, false);
    }

    @Override
    public List<M> getListByObj(M entity, boolean isMainDS) {
        return getListByObj(entity, null, null, null, isMainDS);
    }


    @Override
    public List<M> getListByObj(M entity, String whereSql) {
        return getListByObj(entity, null, whereSql, null, false);
    }

    @Override
    public List<M> getListByObj(M entity, String whereSql, boolean isMainDS) {
        return getListByObj(entity, null, whereSql, null, isMainDS);
    }


    @Deprecated
    @Override
    public List<M> getListByObj(M entity, String whereSql, String orderBySql) {
        return getListByObj(entity, null, whereSql, orderBySql, false);
    }

    @Override
    public List<M> getListByObj(M entity, String whereSql, String orderBySql, boolean isMainDS) {
        return getListByObj(entity, null, whereSql, orderBySql, isMainDS);
    }


    @Override
    public List<M> getListByObj(M entity, PageInfo pageInfo, String whereSql) {
        return getListByObj(entity, pageInfo, whereSql, null, false);
    }

    @Override
    public List<M> getListByObj(M entity, PageInfo pageInfo, String whereSql, boolean isMainDS) {
        return getListByObj(entity, pageInfo, whereSql, null, isMainDS);
    }


    @Override
    public List<M> getListByObjSortByMultiField(M entity, PageInfo pageInfo, String whereSql, String orderBySql) {
        return getListByObjSortByMultiField(entity, pageInfo, whereSql, orderBySql, false);
    }

    @Override
    public List<M> getListByObjSortByMultiField(M entity, PageInfo pageInfo, String whereSql, String orderBySql, boolean isMainDS) {
        try {
            return getSqlSession().selectList(getMapperNameSpace() + getListByEntityAndPageInfo,
                    getShardParam(entity != null ? entity.getId() : null, getMapParams(entity, pageInfo, whereSql, orderBySql), isMainDS));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getListByObj: {},{},{},{}", new String[]{entity == null ? "" : entity.toString(),
                    pageInfo.toString(), whereSql, orderBySql}, e);
        }
    }

    @Override
    public List<M> getListByObj(M entity, PageInfo pageInfo, String whereSql, String orderBySql) {
        return getListByObj(entity, pageInfo, whereSql, orderBySql, false);
    }

    @Override
    public List<M> getListByObj(M entity, PageInfo pageInfo, String whereSql, String orderBySql, boolean isMainDS) {
        try {
            return getSqlSession().selectList(getMapperNameSpace() + getListByEntityAndPageInfo,
                    getShardParam(entity != null ? entity.getId() : null,
                            getMapParams(entity, pageInfo, whereSql,
                                    pageInfo != null && !Strings.isNullOrEmpty(pageInfo.getOrderField())
                                            ? getOrderBySql(pageInfo) : orderBySql), isMainDS));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getListByObj: {},{},{},{}",
                    new String[]{entity == null ? "" : entity.toString(),
                            (pageInfo != null) ? pageInfo.toString() : "", whereSql, orderBySql}, e);
        }
    }
}
