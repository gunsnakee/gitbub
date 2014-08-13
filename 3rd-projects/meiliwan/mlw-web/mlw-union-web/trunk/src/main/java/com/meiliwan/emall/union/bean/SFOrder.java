package com.meiliwan.emall.union.bean;

import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

public class SFOrder {
	private Integer ShopId;
	private String province;
	private String area;
	private String shopName;
	private String ShopEmail;
	private String OrderId;
	private Date CreateTime;
	private Date PayTime;
	private String OrderStatus;
	private Double OrderTotalPrice;
	private Double OrderRealPay;
	private List<Goods> OrderGoodsItems;
	public Integer getShopId() {
		return ShopId;
	}

	public void setShopId(Integer shopId) {
		ShopId = shopId;
	}

	public String getShopEmail() {
		return ShopEmail;
	}

	public void setShopEmail(String shopEmail) {
		ShopEmail = shopEmail;
	}

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	public Date getCreateTime() {
		return CreateTime;
	}



	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}

	public Date getPayTime() {
		return PayTime;
	}

	public void setPayTime(Date payTime) {
		PayTime = payTime;
	}

	public String getOrderStatus() {
		return OrderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		OrderStatus = orderStatus;
	}

	public Double getOrderTotalPrice() {
		return OrderTotalPrice;
	}

	public void setOrderTotalPrice(Double orderTotalPrice) {
		this.OrderTotalPrice = orderTotalPrice;
	}
	
	

//	public short getGoodsCount() {
//		return GoodsCount;
//	}
//
//	public void setGoodsCount(Short goodsCount) {
//		GoodsCount = goodsCount;
//	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Double getOrderRealPay() {
		return OrderRealPay;
	}

	public void setOrderRealPay(Double orderRealPay) {
		OrderRealPay = orderRealPay;
	}

	public List<Goods> getOrderGoodsItems() {
		return OrderGoodsItems;
	}

	public void setOrderGoodsItems(List<Goods> orderGoodsItems) {
		this.OrderGoodsItems = orderGoodsItems;
	}


//	private short GoodsCount;
	
	public class Goods{
		private Integer GoodsId;
		private String GoodsName;
		private Double GoodsPrice;
		private Integer SaleNum;
		public Integer getGoodsId() {
			return GoodsId;
		}
		public void setGoodsId(Integer goodsId) {
			GoodsId = goodsId;
		}
		public String getGoodsName() {
			return GoodsName;
		}
		public void setGoodsName(String goodsName) {
			GoodsName = goodsName;
		}
		public Double getGoodsPrice() {
			return GoodsPrice;
		}
		public void setGoodsPrice(Double goodsPrice) {
			GoodsPrice = goodsPrice;
		}
		public Integer getSaleNum() {
			return SaleNum;
		}
		public void setSaleNum(Integer saleNum) {
			SaleNum = saleNum;
		}
		
		
	}
	
	@Override
	public String toString(){
		return new Gson().toJson(this);
	}
}
