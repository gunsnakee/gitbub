package com.meiliwan.emall.union.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.meiliwan.emall.core.bean.BaseEntity;

public class UnionOrder extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int Id;
	private String sourceidId;
	private String SourceType;
	private String OrderId;
	private int UId;
	private double TotalPrice;
	private Date CreateTime;
	private String QueryInfo;
	private String mlwQueryInfo;
	private int PayStatus;
	private String PayMethod;
	private int OrderStatus;
	
	
	private List<ProductInfo> proInfos = new ArrayList<ProductInfo>();
	private static Map<Integer, String> orderStatusMap = new HashMap<Integer, String>();
	static {
		orderStatusMap.put(new Integer(10), "等待付款");
		orderStatusMap.put(new Integer(12), "处理失败");
		orderStatusMap.put(new Integer(20), "处理完成");
		orderStatusMap.put(new Integer(30), "等待发货");
		orderStatusMap.put(new Integer(40), "等待确认收货");
		orderStatusMap.put(new Integer(50), "已收货已付");
		orderStatusMap.put(new Integer(60), "交易成功");
		orderStatusMap.put(new Integer(80), "已取消");
	}
	
	@SuppressWarnings("unchecked")
	public Integer getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getSourceidId() {
		return sourceidId;
	}
	public void setSourceidId(String sourceidId) {
		this.sourceidId = sourceidId;
	}
	public String getSourceType() {
		return SourceType;
	}
	public void setSourceType(String sourceType) {
		SourceType = sourceType;
	}
	public String getOrderId() {
		return OrderId;
	}
	public void setOrderId(String orderId) {
		OrderId = orderId;
	}
	public int getUId() {
		return UId;
	}
	public void setUId(int uId) {
		UId = uId;
	}
	public double getTotalPrice() {
		return TotalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		TotalPrice = totalPrice;
	}
	public Date getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}
	public String getQueryInfo() {
		return QueryInfo;
	}
	public void setQueryInfo(String queryInfo) {
		QueryInfo = queryInfo;
	}
	public String getMlwQueryInfo() {
		return mlwQueryInfo;
	}
	public void setMlwQueryInfo(String mlwQueryInfo) {
		this.mlwQueryInfo = mlwQueryInfo;
		
		parseProInfos(mlwQueryInfo);
	}
	public int getPayStatus() {
		return PayStatus;
	}
	public void setPayStatus(int payStatus) {
		PayStatus = payStatus;
	}
	public String getPayMethod() {
		return PayMethod;
	}
	public void setPayMethod(String payMethod) {
		PayMethod = payMethod;
	}
	public int getOrderStatus() {
		return OrderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		OrderStatus = orderStatus;
	}

	public List<ProductInfo> getProInfos() {
		return proInfos;
	}
	
	public String getOrderStringStatus(){
		return orderStatusMap.get(getOrderStatus());
	}
	
	private void parseProInfos(String mlwQueryInfo){
		if(StringUtils.isNotBlank(mlwQueryInfo)){
			String[] parts = mlwQueryInfo.split("\\|\\|");
			for(String part : parts){
				String[] proParts = part.split("\\|");
				ProductInfo proInfo = new ProductInfo();
				proInfo.setProId(Integer.valueOf(proParts[0]));
				proInfo.setProName(proParts[1]);
				proInfo.setBuyNum(Integer.valueOf(proParts[2]));
				proInfo.setPrice(Double.valueOf(proParts[3]));
				
				proInfos.add(proInfo);
			}
		}
	}
	
	
	public class ProductInfo{
		private int proId;
		private String proName;
		private double price;
		private int buyNum;
		public int getProId() {
			return proId;
		}
		public void setProId(int proId) {
			this.proId = proId;
		}
		public String getProName() {
			return proName;
		}
		public void setProName(String proName) {
			this.proName = proName;
		}
		public double getPrice() {
			return price;
		}
		public void setPrice(double price) {
			this.price = price;
		}
		public int getBuyNum() {
			return buyNum;
		}
		public void setBuyNum(int buyNum) {
			this.buyNum = buyNum;
		}
		
	}
}
