package com.meiliwan.emall.imeiliwan.search.vo;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * 请确认fl有你需要的字段！ 目前没有缓存SPU那些异图
 *  @Description TODO
 *	@author shanbo.liang
 */
public class ProductRecommendVO  implements ProductVO{
	
	String proName;
	String advName;
	String proId;
	String stock;
	String mlwPrice;
	String marketPrice;
	String spPrice;
	String defaultImageUri;
	int numComment;
	int numSold;
	String discount;
	int numLove;
	float avgScore;
	String storeEnName;
	
	
	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getAdvName() {
		return advName;
	}

	public void setAdvName(String advName) {
		this.advName = advName;
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getMlwPrice() {
		return mlwPrice;
	}

	public void setMlwPrice(String mlwPrice) {
		this.mlwPrice = mlwPrice;
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

	public String getDefaultImageUri() {
		return defaultImageUri;
	}

	public void setDefaultImageUri(String defaultImageUri) {
		this.defaultImageUri = defaultImageUri;
	}

	public int getNumComment() {
		return numComment;
	}

	public void setNumComment(int numComment) {
		this.numComment = numComment;
	}

	public int getNumSold() {
		return numSold;
	}

	public void setNumSold(int numSold) {
		this.numSold = numSold;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public int getNumLove() {
		return numLove;
	}

	public void setNumLove(int numLove) {
		this.numLove = numLove;
	}



	public float getAvgScore() {
		return avgScore;
	}

	public void setAvgScore(float avgScore) {
		this.avgScore = avgScore;
	}

	public String getStoreEnName() {
		return storeEnName;
	}

	public void setStoreEnName(String storeEnName) {
		this.storeEnName = storeEnName;
	}

	public String toString(){
		JsonObject json = new JsonObject();
		json.addProperty("proName", getProName());
		json.addProperty("advName", getAdvName());
		json.addProperty("proId", getProId());
		json.addProperty("stock", getStock());
		json.addProperty("mlwPrice", getMlwPrice());
		json.addProperty("marketPrice", getMarketPrice());
		json.addProperty("spPrice", getSpPrice());
		json.addProperty("defaultImageUri", getDefaultImageUri() );
		json.addProperty("storeEnName", getStoreEnName());
		json.addProperty("numComment", getNumComment());
		json.addProperty("numSold", getNumSold());
		json.addProperty("avgScore", getAvgScore());
		json.addProperty("discount", getDiscount());
		json.addProperty("numLove", getNumLove());
		return json.toString();
	}

	/**
	 * 只获取需要部分,注意doc的字段是否足够
	 * @param doc
	 */
	public void swallowEntity(ProductEntity doc){
		setProName( doc.getProName());
		setAdvName(  doc.getAdvName());
		setProId( doc. getProId());
		setStock( doc.getStock());
		setMlwPrice( doc.getMlwPrice());
		setMarketPrice(  doc.getMarketPrice());
		setSpPrice(  doc.getSpPrice());
		setDefaultImageUri( doc.getDefaultImageUri() );
		setStoreEnName(  doc.getStoreEnName());
		setNumComment( doc. getNumComment());
		setNumSold(  doc.getNumSold());
		setAvgScore(  doc.getAvgScore());
		setDiscount( doc. getDiscount());
		setNumLove( doc. getNumLove());
	}
	
	public static List<ProductRecommendVO> convertEntitiesToVOs(List<? extends ProductEntity> entities){
		List<ProductRecommendVO> array = new ArrayList<ProductRecommendVO>(entities == null ? 0 : entities.size());
		for(ProductEntity entity : entities){
			ProductRecommendVO vo = new ProductRecommendVO();
			vo.swallowEntity(entity);
			array.add(vo);
		}
		return array;
	}
	
	public static ProductRecommendVO convertFromJsonObject(JsonElement doc){
		ProductRecommendVO fromJson = new Gson().fromJson(doc, ProductRecommendVO.class);
		return fromJson;
	}
	
}

