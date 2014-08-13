package com.meiliwan.emall.core.db;


import java.io.Serializable;
import java.util.List;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.core.bean.BaseEntity;

/**
 * User: Sean
 * Date: 2013-5-23 下午1:02
 */
public interface ILogDao<T extends Serializable, M extends BaseEntity> {

    /**
     * 通过传入 pk 获取对象
     *
     * @param pk
     * @return
     */
    M getEntityById(T pk);

    /**
     * 通过传入 实体参数 获取对象
     *
     * @param entity
     * @return
     */
    M getEntityByObj(M entity);

    /**
     * 通过传入 实体参数 获取对象
     *
     * @param entity
     * @param whereSql 自定义WhereSql 例如:   String whereSql ="id > 10 and id < 20 ";
     * @return
     */
    M getEntityByObj(M entity, String whereSql);

    /**
     * 通过传入实体参数获取条数
     *
     * @param entity
     * @return
     */
    int getCountByObj(M entity);

    /**
     * 通过传入实体参数 和自定义 whereSql 获取条数
     *
     * @param entity
     * @param whereSql 例如:   String whereSql ="id > 10 and id < 20 ";
     * @return
     */
    int getCountByObj(M entity, String whereSql);

    /**
     * 通过实体参数获取对应的实体列表包含物理分页
     *
     * @param entity   实体对象
     * @param pageInfo 分页对象
     * @param whereSql 自定义WhereSql 例如:   String whereSql ="id > 10 and id < 20 ";
     *                 让id倒序排列 <br/>
     *                 只需要传入 order by id desc 即可
     * @return 返回分页好的实体集合
     */
    PagerControl<M> getPagerByObj(M entity, PageInfo pageInfo, String whereSql);

    /**
     * 通过实体参数获取对应的实体列表包含物理分页
     *
     * @param entity     实体对象
     * @param pageInfo   分页对象
     * @param whereSql   自定义WhereSql 例如:   String whereSql ="id > 10 and id < 20 ";
     * @param orderBySql 排序sql 例如: select * from user order by id desc <br/>
     *                   让id倒序排列 <br/>
     *                   只需要传入 order by id desc 即可
     * @return 返回分页好的实体集合
     */
    PagerControl<M> getPagerByObj(M entity, PageInfo pageInfo, String whereSql, String orderBySql);

    /**
     * 作用读取所有对象<br/>
     * 注意:使用范围主要作用读取配置，地区，或者是一些状态标，数据量小的可以使用
     *
     * @return 返回对象集合
     */
    List<M> getAllEntityObj();

    /**
     * 作用读取对象列表 带实体条件 <br/>
     *
     * @param entity 参数查询
     * @return 返回对象集合
     */
    List<M> getListByObj(M entity);

    /**
     * 作用读取对象列表 带实体条件 <br/>
     *
     * @param entity   参数查询
     * @param whereSql 自定义WhereSql 例如:   String whereSql ="id > 10 and id < 20 ";
     * @return 返回对象集合
     */
    List<M> getListByObj(M entity, String whereSql);

    /**
     * 作用读取对象列表 带实体条件 <br/>
     *
     * @param entity     参数查询
     * @param whereSql   自定义WhereSql 例如:   String whereSql ="id > 10 and id < 20 ";
     * @param orderBySql 不使用传空字符串或者null<br/>
     *                   排序sql 例如: select * from user order by id desc <br/>
     *                   让id倒序排列 <br/>
     *                   只需要传入 id desc 即可
     * @return 返回对象集合
     */
    List<M> getListByObj(M entity, String whereSql, String orderBySql);

    /**
     * 作用读取对象列表 带实体条件 <br/>
     *
     * @param entity   参数查询
     * @param pageInfo 分页对象 不使用传null
     * @param whereSql 自定义WhereSql 例如:   String whereSql ="id > 10 and id < 20 ";
     * @return 返回对象集合
     */
    List<M> getListByObj(M entity, PageInfo pageInfo, String whereSql);

    /**
     * 作用读取对象列表 带实体条件 <br/>
     *
     * @param entity     参数查询
     * @param pageInfo   分页对象 不使用传null
     * @param whereSql   自定义WhereSql 例如:   String whereSql ="id > 10 and id < 20 ";
     * @param orderBySql 不使用传空字符串或者null<br/>
     *                   排序sql 例如: select * from user order by id desc <br/>
     *                   让id倒序排列 <br/>
     *                   只需要传入 id desc 即可
     * @return 返回对象集合
     */
    List<M> getListByObj(M entity, PageInfo pageInfo, String whereSql, String orderBySql);
	
}
