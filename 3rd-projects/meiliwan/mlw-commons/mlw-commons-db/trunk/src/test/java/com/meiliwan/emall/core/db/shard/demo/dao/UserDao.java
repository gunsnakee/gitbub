package com.meiliwan.emall.core.db.shard.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.core.db.shard.demo.dao.entity.UserEntity;

/**
 * 演示用户DAO
 */
public interface UserDao {
	public boolean insertUser(UserEntity user);
	<M> List<M> findUser(@Param("entity") M m, @Param("pageInfo") PageInfo pageInfo);
}
