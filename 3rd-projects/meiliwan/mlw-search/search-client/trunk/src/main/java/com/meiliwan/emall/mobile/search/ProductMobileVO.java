package com.meiliwan.emall.mobile.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.imeiliwan.search.cache.CacheTools;
import com.meiliwan.emall.imeiliwan.search.vo.ProductEntity;
import com.meiliwan.emall.imeiliwan.search.vo.ProductVO;

/**
 * 
 *  @Description 移动端搜索商品列表页 VO
 *	@author shanbo.liang
 */
public class ProductMobileVO implements ProductVO{
	private String id;
	private String proName;
	private String imageUri;
	private float avgScore;
	private String placeString;
	private String marketPrice;
	private String numComment;
	private String stock;
	private String spPrice;

	public ProductMobileVO() {
		super();
	}

	public String getMarketPrice() {
		return marketPrice;
	}




	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}




	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	public float getAvgScore() {
		return avgScore;
	}

	public void setAvgScore(float avgScore) {
		this.avgScore = avgScore;
	}

	public String getPlaceString() {
		return placeString;
	}

	public void setPlaceString(String placeString) {
		this.placeString = placeString;
	}


	public String getNumComment() {
		return numComment;
	}

	public void setNumComment(String numComment) {
		this.numComment = numComment;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getSpPrice() {
		return spPrice;
	}

	public void setSpPrice(String spPrice) {
		this.spPrice = spPrice;
	}
	
	public static Date parseSolrDate(String solrDate){
		String dateYYYY = solrDate.substring(0, solrDate.length()-1).replace("T", " ");
		return DateUtil.parseDateTime(dateYYYY);
	}

	@Override
	public void swallowEntity(ProductEntity doc) {
		this.setId(doc.getProId());
		this.setImageUri(doc.getDefaultImageUri());
		this.setAvgScore(doc.getAvgScore());
		this.setMarketPrice(doc.getMarketPrice());
		this.setNumComment(doc.getNumComment() + "");
		this.setPlaceString(CacheTools.get().getPlaceNameById(doc.getPlaceId()));
		this.setProName(doc.getProName());
		this.setSpPrice(doc.getSpPrice());
		this.setStock(doc.getStock());		
	}
	
	public static List<ProductMobileVO> convertEntitiesToVos(List<ProductEntity> productEntities){
		List<ProductMobileVO> productMobileVOs = new ArrayList<ProductMobileVO>();
		
		for(ProductEntity entity : productEntities){
			ProductMobileVO proVo = new ProductMobileVO();
			proVo.swallowEntity(entity);
			productMobileVOs.add(proVo);
		}
		
		return productMobileVOs;
		
	}
	
	public String toString(){
		return new Gson().toJson(this);
	}
	
}
