package com.meiliwan.emall.mobile.search;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.meiliwan.emall.imeiliwan.search.vo.ProductEntity;
import com.meiliwan.emall.imeiliwan.search.vo.ProductVO;

/**
 * 移动端的国家馆商品VO
 *  @Description TODO
 *	@author shanbo.liang
 */
public class FallsMobileVO implements ProductVO{
	public String proId;
	public String proName;
	public String fallsImageUri;
	public String defaultImageUri;
	public int height;
	public String marketPrice;
	public String spPrice;
	public float avgScore;
	
	
	public String getDefaultImageUri() {
		return defaultImageUri;
	}

	public void setDefaultImageUri(String defaultImageUri) {
		this.defaultImageUri = defaultImageUri;
	}

	public float getAvgScore() {
		return avgScore;
	}

	public void setAvgScore(float avgScore) {
		this.avgScore = avgScore;
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getFallsImageUri() {
		return fallsImageUri;
	}

	public void setFallsImageUri(String fallsImageUri) {
		this.fallsImageUri = fallsImageUri;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getSpPrice() {
		return spPrice;
	}

	public void setSpPrice(String spPrice) {
		this.spPrice = spPrice;
	}

	public void swallowEntity(ProductEntity doc){
		setProId(doc.getProId());
		setFallsImageUri(doc.getFallsImageUri());
		setProName(doc.getProName());
		setMarketPrice(doc.getMarketPrice());
		setSpPrice(doc.getSpPrice());
		setHeight(doc.getFallsHeight());
		setAvgScore(doc.getAvgScore());
		setDefaultImageUri(doc.getDefaultImageUri());
	}
	
	public static List<FallsMobileVO> convertEntitiesToVOs(List<ProductEntity> entities){
		List<FallsMobileVO> array = new ArrayList<FallsMobileVO>(entities == null ? 0 : entities.size());
		for(ProductEntity entity : entities){
			FallsMobileVO vo = new FallsMobileVO();
			vo.swallowEntity(entity);
			array.add(vo);
		}
		return array;
	}
	
	public String toString(){
		return new Gson().toJson(this);
	}
	
}
