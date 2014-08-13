package com.meiliwan.emall.imeiliwan.search.vo;

import com.google.gson.JsonElement;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.meiliwan.emall.search.common.query.StringHighlighter;

public class ProductEntity {

	final static String[] imageHosts = new String[]{
		"http://img.meiliwan.com", "http://img0.meiliwan.com", "http://img1.meiliwan.com",
		"http://img2.meiliwan.com","http://img3.meiliwan.com","http://img4.meiliwan.com"};

	String proName;
	String shortName;
	String advName;
	String proId;
	String stock;
	int state;

	String mlwPrice;
	String marketPrice;
	String defaultImageUri;
	
	String storeEnName;

	int numComment;
	int numSold;
	float avgScore;
	
	int numLove = -1;

	boolean isFalls = false;
	String fallsImageUri;
	int fallsHeight = -1;
	


	String discount = null;
	String spPrice = null;
		
	int brandId;
	int placeId;
	int fcid;
	int scid;
	int tcid;
	
	public boolean isFalls() {
		return isFalls;
	}
	public void setFalls(boolean isFalls) {
		this.isFalls = isFalls;
	}
	public String getFallsImageUri() {
		return fallsImageUri;
	}
	public void setFallsImageUri(String fallsImageUri) {
		this.fallsImageUri = fallsImageUri;
	}
	public int getBrandId() {
		return brandId;
	}
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}
	public int getPlaceId() {
		return placeId;
	}
	public void setPlaceId(int placeId) {
		this.placeId = placeId;
	}
	public int getFcid() {
		return fcid;
	}
	public void setFcid(int fcid) {
		this.fcid = fcid;
	}
	public int getScid() {
		return scid;
	}
	public void setScid(int scid) {
		this.scid = scid;
	}
	public int getTcid() {
		return tcid;
	}
	public void setTcid(int tcid) {
		this.tcid = tcid;
	}
	public String getSpPrice() {
		return spPrice;
	}
	public void setSpPrice(String spPrice) {
		this.spPrice = spPrice;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
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
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getStoreEnName() {
		return storeEnName;
	}
	public void setStoreEnName(String storeEnName) {
		this.storeEnName = storeEnName;
	}
	public int getNumLove() {
		return numLove;
	}
	public void setNumLove(int numLove) {
		this.numLove = numLove;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}

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
	
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getMlwPrice() {
		return mlwPrice;
	}
	public int getFallsHeight() {
		return fallsHeight;
	}
	public void setFallsHeight(int fallsHeight) {
		this.fallsHeight = fallsHeight;
	}
	public void setMlwPrice(String mlwPrice) {
		this.mlwPrice = mlwPrice;
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
	public float getAvgScore() {
		return avgScore;
	}
	public void setAvgScore(float avgScore) {
		this.avgScore = avgScore;
	}

	public String getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}
	
	/**
	 * 并非所有字段都显示的，
	 * @return
	 */
	protected JsonObject jsonFormat1(){
		JsonObject json = new JsonObject();
		json.addProperty("proName", this.proName);
		json.addProperty("shortName", this.shortName);
		json.addProperty("advName", this.advName);
		json.addProperty("proId", this.proId);
		json.addProperty("stock", this.stock);
		json.addProperty("mlwPrice", this.mlwPrice);
		json.addProperty("marketPrice", this.marketPrice);
		json.addProperty("spPrice", this.spPrice);
		json.addProperty("defaultImageUri", this.defaultImageUri );
		if (isFalls){
			json.addProperty("isFalls", this.isFalls);
			json.addProperty("fallsHeight", this.fallsHeight);
			json.addProperty("fallsImageUri", this.fallsImageUri);
		}
		json.addProperty("storeEnName", this.storeEnName);
		json.addProperty("numComment", this.numComment);
		json.addProperty("numSold", this.numSold);
		json.addProperty("avgScore", this.avgScore);
		json.addProperty("discount", this.discount);
		json.addProperty("numLove", this.numLove);
		return json;
	}
	
	public String toString(){
		return jsonFormat1().toString();
	}

	public static ProductEntity parseFromSelf(String text){
		return new Gson().fromJson(text, ProductEntity.class);
	}

	public static String giveRandomHostToImage(String imageUri){
		return imageHosts[0] + imageUri;
	}
	
	/**
	 * 解析文档, 注意字段不一定都有....注意fl
	 * @param doc
	 * @param highlightQuery
	 */
	public void parseFromDoc(JsonObject doc , String highlightQuery){
		String proId = doc.get("id").getAsString();
		this.setProId(proId);

		JsonElement stock = doc.get("stock");
		setStock(stock == null ? "0" : stock.getAsString());
		
		JsonElement state = doc.get("state");
		setState(state == null || state instanceof JsonNull ? 2 : state.getAsInt());

		JsonElement mlwPrice = doc.get("mlw_prices");
		setMlwPrice(mlwPrice == null ? "0.0": mlwPrice.getAsString());
		JsonElement spPrice = doc.get("sp_prices");
		setSpPrice(spPrice == null ? "0.0": spPrice.getAsString());

//		int dc = Math.round(spPrice.getAsFloat() * 100 /mlwPrice.getAsFloat());
		JsonElement spIds = doc.get("sp_ids");
		if (spIds != null){
			String spInfo = spIds.getAsString();
			discount = extraceDiscount(spInfo);
		}
		
		JsonElement marketPrice = doc.get("market_prices");
		setMarketPrice(marketPrice == null ? null : marketPrice.getAsString());

		JsonElement defaultImage = doc.get("default_image_uri");
		if (defaultImage != null)
			setDefaultImageUri( giveRandomHostToImage(defaultImage.getAsString()));
		
		JsonElement is_falls = doc.get("is_falls");
		if (is_falls != null){
			isFalls = is_falls.getAsBoolean();
			if (isFalls){
				JsonElement fallsheight = doc.get("height_volume");
				setFallsHeight(fallsheight == null ? -1 : fallsheight.getAsInt());
				
				JsonElement fallsImageUri = doc.get("falls_image_uri");
				setFallsImageUri(fallsImageUri == null ? null : giveRandomHostToImage( fallsImageUri.getAsString()));
				
			}
		}
		String productName = doc.get("pro_name").getAsString();
		if (!StringUtils.isEmpty(highlightQuery))
			setProName(StringHighlighter.highlight(productName, highlightQuery));
		else {
			setProName(productName);
		}

		JsonElement advName = doc.get("adv_name");
		setAdvName(advName == null ? null : advName.getAsString());

		JsonElement shortName = doc.get("short_name");
		setShortName(shortName == null ? null : shortName.getAsString());
		
		JsonElement avgScore = doc.get("avg_score");
		setAvgScore(avgScore == null ? 0.0f : avgScore.getAsFloat());

		JsonElement loveVolume = doc.get("love_volume");
		setNumLove(loveVolume==null?0:loveVolume.getAsInt());
			

		JsonElement saleVolume = doc.get("sale_volume");
		setNumSold(saleVolume == null ? 0 : saleVolume.getAsInt());

		JsonElement storeIds = doc.get("store_ids");
		setStoreEnName(storeIds == null ? null : storeIds.getAsString());
				
		JsonElement specialId = doc.get("brand_id");
		brandId = (specialId == null ? 0 : specialId.getAsInt());
		specialId = doc.get("place_id");
		placeId = (specialId == null ? 0 : specialId.getAsInt());
		specialId = doc.get("first_category_id");
		fcid = (specialId == null ? 0 : specialId.getAsInt());
		specialId = doc.get("second_category_id");
		scid = (specialId == null ? 0 : specialId.getAsInt());
		specialId = doc.get("third_category_id");
		tcid = (specialId == null ? 0 : specialId.getAsInt());
		
		//-----------activities-----------
		JsonElement numComment = doc.get("comment_volume");
		setNumComment(numComment == null ? 0:numComment.getAsInt());

	}
	
	private  static String extraceDiscount(String spInfo){
		int dcStart = spInfo.indexOf("discount");
		if (dcStart > -1){
			int dcEnd = spInfo.indexOf(",", dcStart + 9);
			return spInfo.substring(dcStart + 9, dcEnd);
		}
		return null;
	}
	
	/**
	 * 判断该商品是否可以被加入购物车
	 * @return  如果商品为上架状态，并且库存大于0，则返回true；否则返回false
	 */
	public boolean isCanAdd2Cart(){
		return state == 1 && Integer.valueOf(stock) > 0;
	}
	
	public static void main(String[] args) {
		System.out.println(extraceDiscount("discount,12,"));
	}
	
//	public static Date parseSolrDate(String solrDate){
//		String dateYYYY = solrDate.substring(0, solrDate.length()-1).replace("T", " ");
//		return DateUtil.parseDateTime(dateYYYY);
//	}

}
