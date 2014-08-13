package com.meiliwan.emall.imeiliwan.search.vo;

import java.util.ArrayList;
import java.util.List;

/**
 *  VO列表，增加了一个Numfound
 *  @Description TODO
 *	@author shanbo.liang
 */
public class VOList {
	List<? extends ProductVO> productList = new ArrayList<ProductVO>();
	String reqId = "";
	int numFound = 0;

	public String getReqId() {
		return reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

	public List<? extends ProductVO> getProductList() {
		return productList;
	}

	public void setProductList(List<? extends ProductVO> productList) {
		this.productList = productList;
	}

	
	public int getNumFound() {
		return numFound;
	}

	public void setNumFound(int numFound) {
		this.numFound = numFound;
	}

	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("{\"numFound\":" + numFound)
			   .append(",\"reqId\":\"" + reqId + "\"")
			   .append(", \"productList\":" + productList.toString() + "}");	
		
		
		return builder.toString();
	}
	
	public static class RecommendVOList {
		List<ProductRecommendVO> productList = new ArrayList<ProductRecommendVO>();
		String reqId = "";
		int numFound = 0;
		
		public RecommendVOList(VOList voList){
			this.numFound = voList.getNumFound();
			this.reqId = voList.getReqId();
			for(ProductVO vo: voList.getProductList()){
				productList.add((ProductRecommendVO)vo);
			}
		}
		
		public String toString(){
			StringBuilder builder = new StringBuilder();
			builder.append("{\"numFound\":" + numFound)
				   .append(",\"reqId\":\"" + reqId + "\"")
				   .append(", \"productList\":" + productList.toString() + "}");	
			
			
			return builder.toString();
		}
		
		public List<ProductRecommendVO> getProductList() {
			return productList;
		}
	}
}
