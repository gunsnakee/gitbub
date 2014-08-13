package com.meiliwan.emall.core.db.shard.demo.dao.entity;

/**
 * 演示用户实体类
 */
public class UserEntity {
	private long id;
	private String name;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", name=" + name + "]";
	}
	
	
}
