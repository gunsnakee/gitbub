package com.meiliwan.emall.base.dto;

/**
 * 配送金额范围
 * @author rubi
 *
 */
public class TransportLimit {

	private double priceMin;
	private double priceMax;
	
	public double getPriceMin() {
		return priceMin;
	}
	public void setPriceMin(double priceMin) {
		this.priceMin = priceMin;
	}
	public double getPriceMax() {
		return priceMax;
	}
	public void setPriceMax(double priceMax) {
		this.priceMax = priceMax;
	}
	@Override
	public String toString() {
		return "TransportLimit [priceMin=" + priceMin + ", priceMax="
				+ priceMax + "]";
	}
	
	
}
