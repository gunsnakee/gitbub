package com.meiliwan.emall.log.bean;

/**
 * @author leo
 * 业务操作接口定义，实现者一般为业务枚举;
 */
public interface BizLogOp {
	
	/**
	 * 业务操作的字符串说明
	 * @return
	 */
	String getBizLogOpName();
	
	/**
	 * 业务操作 增
	 */
	BizLogOp ADD = new BizLogOp() {

		@Override
		public String getBizLogOpName() {
			return "ADD";
		}
		
	};
	
	/**
	 * 业务操作 更新
	 */
	BizLogOp UPDATE = new BizLogOp() {
		
		@Override
		public String getBizLogOpName() {
			return "UPDATE";
		}
		
	};
	
	/**
	 * 业务操作 软删除
	 */
	BizLogOp SOFT_DEL = new BizLogOp() {
		
		@Override
		public String getBizLogOpName() {
			return "SOFT_DEL";
		}
		
	};
	
	/**
	 * 业务操作 硬删除
	 */
	BizLogOp DEL = new BizLogOp() {
		
		@Override
		public String getBizLogOpName() {
			return "DEL";
		}
		
	};
	
	/**
	 * 
	 * 业务操作 查询  一般来说，这类业务操作并不需要记录bizlog,除非某一业务数据的机密权限比较高;
	 * 
	 */
	BizLogOp QUERY = new BizLogOp() {
		
		@Override
		public String getBizLogOpName() {
			return "QUERY";
		}
		
	};
}
