package com.meiliwan.emall.commons.bean;

import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author lsf
 * 
 */
public class CpsOrderVO {

	private String orderId; //订单ID
	private double totalAmount; //订单实际需要支付的总价（除去运费）
	private String _mcps; //cookie值
	private int uid; //下单的用户ID
	private List<CpsProduct> proList;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String get_mcps() {
		return _mcps;
	}
	public void set_mcps(String _mcps) {
		this._mcps = _mcps;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getSourceType(){
		return StringUtils.isEmpty(_mcps) && _mcps.indexOf('|') > 0 ? null : _mcps.substring(0, _mcps.indexOf('|'));
	}
	
	public String getSourceId(){
		return StringUtils.isEmpty(_mcps) && _mcps.indexOf('|') > 0 && _mcps.indexOf('|') + 1 <= _mcps.length()? null : _mcps.substring(_mcps.indexOf('|') + 1);
	}
	
	public List<CpsProduct> getProList() {
		return proList;
	}
	public void setProList(List<CpsProduct> proList) {
		this.proList = proList;
	}



	public static class CpsProduct {
	    private int proId; //商品ID
        private String proName; //商品短标题
		private double price; //如果参与优惠活动，则给出活动价
		private int buyNum; //购买数量
		private String category ; //商品类别
		
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
		
        public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public CpsProduct(){}

        public CpsProduct(int proId, String proName, double price, int buyNum) {
            this.proId = proId;
            this.proName = proName;
            this.price = price;
            this.buyNum = buyNum;
        }
    }
}
