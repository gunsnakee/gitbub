package com.meiliwan.emall.core.db.shard;


/**
 * 
 * 功能描述： 分表分库参数类，该类适用于DAO的实现类中，
 * 例如：
 * ShardParam shardParam = new ShardParam("ShardUser", user.getId(), user);
   return getSqlSession().insert("User.insertUser", shardParam);
 * @author 作者 xiong.yu
 * @created 2013-5-21 下午5:50:09
 * @version 1.0.0
 * @date 2013-5-21 下午5:50:09
 */
public class ShardParam {

	public static final ShardParam NO_SHARD = new ShardParam();

	private String name;
	private Object shardValue;
	private Object params;
	private boolean isUpdate = false;
	
	public boolean isUpdate() {
		return isUpdate;
	}

	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public ShardParam() {
	}

	public ShardParam(String name, Object shardValue, Object params) {
		super();
		this.name = name;
		this.shardValue = shardValue;
		this.params = params;
	}

	public ShardParam(String name, Object shardValue, Object params,
			boolean isUpdate) {
		super();
		this.name = name;
		this.shardValue = shardValue;
		this.params = params;
		this.isUpdate = isUpdate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getShardValue() {
		return shardValue;
	}

	public void setShardValue(Object shardValue) {
		this.shardValue = shardValue;
	}

	public Object getParams() {
		return params;
	}

	public void setParams(Object params) {
		this.params = params;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ShardParam [name=");
		builder.append(name);
		builder.append(", shardValue=");
		builder.append(shardValue);
		builder.append(", params=");
		builder.append(params);
		builder.append(", isUpdate=");
		builder.append(isUpdate);
		builder.append("]");
		return builder.toString();
	}
}
