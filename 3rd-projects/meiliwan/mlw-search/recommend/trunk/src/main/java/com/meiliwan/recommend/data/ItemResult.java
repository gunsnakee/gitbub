package com.meiliwan.recommend.data;

import java.io.Serializable;

/**
 * 退化为只标记ID
 * @author lgn-mop
 *
 */
public class ItemResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4192627435346812009L;
	protected int id;
//	protected float weight;
//	int status; // for online use
//	long showTimeStart;
//	long showTimeEnd;
	public ItemResult(int id){
		this.id = id;
//		status = 1;
	}
	
	
	
//	public long getShowTimeStart() {
//		return showTimeStart;
//	}
//
//
//
//	public void setShowTimeStart(long showTimeStart) {
//		this.showTimeStart = showTimeStart;
//	}
//
//
//
//	public long getShowTimeEnd() {
//		return showTimeEnd;
//	}
//
//
//
//	public void setShowTimeEnd(long showTimeEnd) {
//		this.showTimeEnd = showTimeEnd;
//	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



//	public float getWeight() {
//		return weight;
//	}
//
//
//
//	public void setWeight(float weight) {
//		this.weight = weight;
//	}
//
//
//
//	public int getStatus() {
//		return status;
//	}
//
//
//
//	public void setStatus(int status) {
//		this.status = status;
//	}



	public String toString(){
		return String.format("%d", id);
	}
}
