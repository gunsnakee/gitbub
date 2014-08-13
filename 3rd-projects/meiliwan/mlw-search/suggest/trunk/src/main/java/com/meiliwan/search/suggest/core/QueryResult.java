package com.meiliwan.search.suggest.core;

import java.io.Serializable;
import java.util.Comparator;

public class QueryResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6339679680287814653L;
	public String queryString;
	double weight;
	String catInfo;
	
	public QueryResult(String queryString){
		this.queryString = queryString;
		this.weight = 1.0;
		this.catInfo = "{}";
	}
	
	
	/**
	 * for ranking
	 * @author lgn-mop
	 *
	 */
	public static class QueryResultBag implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		double weight ;
		public QueryResult qr;
		public QueryResultBag(QueryResult qr, double weight){
			this.qr = qr;
			this.weight = weight;
		}
	}
	
	public static class QueryResultOrder implements Comparator<QueryResultBag>{

		public int compare(QueryResultBag o1, QueryResultBag o2) {
			if (o1.weight > o2.weight){
				return -1;
			}else if (o1.weight < o2.weight){
				return 1;
			}
			return 0;
		}
		
	}
	
	public String getCatInfo() {
		return catInfo;
	}

	/**
	 * must be a JSON 
	 * @param catInfo
	 */
	public void setCatInfo(String catInfo) {
		this.catInfo = catInfo;
	}

	
	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String toString(){
		return String.format("{\"queryString\":\"%s\",\"catInfo\":%s,\"weight\":%.2f}", 
				this.queryString, this.catInfo, this.weight);
	}
}