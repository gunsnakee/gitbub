package com.meiliwan.emall.cms2.vo;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 14-4-15
 * Time: 下午6:38
 * To change this template use File | Settings | File Templates.
 */
public class ProductJsonVo {
    // 商品名称
    private String proName;
    //美丽湾价
    private double mlwPrice;
    //市场价
    private double marketPrice;
    //静态标签
    private String mark;
    //图片url
    private String picUrl;
    //商品id
    private Integer proId;
    //商品库存
    private Integer stock;

    private String advName;

    public String getAdvName() {
        return advName;
    }

    public void setAdvName(String advName) {
        this.advName = advName;
    }



    //商品状态
    private short state;

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public double getMlwPrice() {
        return mlwPrice;
    }

    public void setMlwPrice(double mlwPrice) {
        this.mlwPrice = mlwPrice;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((advName == null) ? 0 : advName.hashCode());
		result = prime * result + ((mark == null) ? 0 : mark.hashCode());
		long temp;
		temp = Double.doubleToLongBits(marketPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(mlwPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((picUrl == null) ? 0 : picUrl.hashCode());
		result = prime * result + ((proId == null) ? 0 : proId.hashCode());
		result = prime * result + ((proName == null) ? 0 : proName.hashCode());
		result = prime * result + state;
		result = prime * result + ((stock == null) ? 0 : stock.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductJsonVo other = (ProductJsonVo) obj;
		if (advName == null) {
			if (other.advName != null)
				return false;
		} else if (!advName.equals(other.advName))
			return false;
		if (mark == null) {
			if (other.mark != null)
				return false;
		} else if (!mark.equals(other.mark))
			return false;
		if (Double.doubleToLongBits(marketPrice) != Double
				.doubleToLongBits(other.marketPrice))
			return false;
		if (Double.doubleToLongBits(mlwPrice) != Double
				.doubleToLongBits(other.mlwPrice))
			return false;
		if (picUrl == null) {
			if (other.picUrl != null)
				return false;
		} else if (!picUrl.equals(other.picUrl))
			return false;
		if (proId == null) {
			if (other.proId != null)
				return false;
		} else if (!proId.equals(other.proId))
			return false;
		if (proName == null) {
			if (other.proName != null)
				return false;
		} else if (!proName.equals(other.proName))
			return false;
		if (state != other.state)
			return false;
		if (stock == null) {
			if (other.stock != null)
				return false;
		} else if (!stock.equals(other.stock))
			return false;
		return true;
	}
    
    
}
