package com.meiliwan.emall.core.db;


import com.google.common.base.Strings;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.bean.BaseEntity;
import com.meiliwan.emall.core.db.shard.ShardParam;
import com.meiliwan.emall.core.db.shard.spring.support.SqlSessionDaoSupport;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Created by yuxiong
 */
public abstract class LogDao<T extends Serializable, M extends BaseEntity>
        extends SqlSessionDaoSupport implements ILogDao<T, M> {

    public static final String defShard = "LogShard";
    public static final String selectByPrimaryKey = ".selectByPrimaryKey";
    public static final String getListByEntityAndPageInfo = ".getListByEntityAndPageInfo";
    public static final String getTotalByEntity = ".getTotalByEntity";


    //用作排序验证对比
    private Set<String> entityFields = new HashSet<String>();

    public LogDao() {
        if (getClass().getGenericSuperclass() instanceof ParameterizedType) {
            Class<M> entiry = (Class<M>) ((ParameterizedType) getClass()
                    .getGenericSuperclass()).getActualTypeArguments()[1];
            for (Field field : entiry.getDeclaredFields()) {
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
    public M getEntityById(T pk) {
        try {
            return getSqlSession().selectOne(getMapperNameSpace() + selectByPrimaryKey, getShardParam(pk, pk, false));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getEntityById: {}", pk.toString(), e);
        }
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
    public M getEntityByObj(M entity, String whereSql) {

        try {
            return getSqlSession().selectOne(getMapperNameSpace() + getListByEntityAndPageInfo,
                    getShardParam(entity != null ? entity.getId() : null, getMapParams(entity, null, whereSql, null), false));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getEntityByObj: {},{}", new String[]{entity == null ? "" : entity.toString(), whereSql}, e);
        }
    }

    @Override
    public int getCountByObj(M entity) {
        try {
            return getCountByObj(entity, null);
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getCountByObj: {}", entity == null ? "" : entity.toString());
        }
    }

    @Override
    public int getCountByObj(M entity, String whereSql) {

        try {

            Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + getTotalByEntity,
                    getShardParam(entity != null ? entity.getId() : null, getMapParams(entity, null, whereSql, null), false));
            return (Integer) selectOne;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getCountByObj: {},{}", new String[]{entity == null ? "" : entity.toString(), whereSql}, e);
        }
    }

    @Override
    public PagerControl<M> getPagerByObj(M entity, PageInfo pageInfo, String whereSql, String orderBySql) {
        PagerControl<M> pagerControl = new PagerControl<M>();
        pageInfo.startTime();
        List<M> list = null;
        int total = 0;
        try {
            list = getListByObj(entity, pageInfo, whereSql, orderBySql);
            total = getCountByObj(entity, whereSql);
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
        return getPagerByObj(entity, pageInfo, whereSql, null);
    }

    @Override
    public List<M> getAllEntityObj() {
        return getListByObj(null, null);
    }

    @Override
    public List<M> getListByObj(M entity) {
        return getListByObj(entity, null, null, null);
    }

    @Override
    public List<M> getListByObj(M entity, String whereSql) {
        return getListByObj(entity, null, whereSql, null);
    }

    @Deprecated
    @Override
    public List<M> getListByObj(M entity, String whereSql, String orderBySql) {
        return getListByObj(entity, null, whereSql, orderBySql);
    }

    @Override
    public List<M> getListByObj(M entity, PageInfo pageInfo, String whereSql) {
        return getListByObj(entity, pageInfo, whereSql, null);
    }

    @Override
    public List<M> getListByObj(M entity, PageInfo pageInfo, String whereSql, String orderBySql) {

        try {
            return getSqlSession().selectList(getMapperNameSpace() + getListByEntityAndPageInfo,
                    getShardParam(entity != null ? entity.getId() : null, getMapParams(entity, pageInfo, whereSql, getOrderBySql(pageInfo)), false));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getListByObj: {},{},{},{}", new String[]{entity == null ? "" : entity.toString(),
                    pageInfo.toString(), whereSql, orderBySql}, e);
        }
    }
}
