package com.meiliwan.emall.monitor.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.meiliwan.emall.core.bean.BaseEntity;

/**
 * 
 * @author lsf
 *
 */
public class UnionOrder extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id ;
	private String sourceId;
	private String sourceType;
	private String orderId;
	private int uid;
	private double totalPrice;
	private Date createTime;
	private String mlwQueryInfo;
	private int payStatus;
	private String payMethod;
	private int orderStatus;
	
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
	
	@Override
	@SuppressWarnings("unchecked")
	public Integer getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getMlwQueryInfo() {
		return mlwQueryInfo;
	}
	public void setMlwQueryInfo(String mlwQueryInfo) {
		this.mlwQueryInfo = mlwQueryInfo;
		
		parseProInfos(mlwQueryInfo);
	}
	public int getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public List<ProductInfo> getProInfos() {
		return proInfos;
	}
	
	public String getOrderStringStatus(){
		return orderStatusMap.get(new Integer(getOrderStatus()));
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
