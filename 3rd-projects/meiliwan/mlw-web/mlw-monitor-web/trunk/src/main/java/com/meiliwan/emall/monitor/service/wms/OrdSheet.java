package com.meiliwan.emall.monitor.service.wms;

import java.util.ArrayList;
import java.util.List;

public class OrdSheet<Ord,Pro> {

	private List<Ord> ords = new ArrayList<Ord>();
	private List<Pro> pros = new ArrayList<Pro>();
	
	public Ord getLastOrd(){
		if(ords.size()>0){
			return ords.get(ords.size()-1);
		}
		return null;
	}
	
	/**
	 * 取得倒数第二个
	 * @param size
	 * @return
	 */
	public Ord getLastOrdByTopTwo(){
		if(ords.size()>1){
			//size:1 2 3 4
			//index0 1 2 3
 			//item:9 9 9 9
			return ords.get(ords.size()-2);
		}
		return null;
	}
	
	public Pro getProBySize(int size){
		if(pros.size()>0){
			return pros.get(pros.size()-size);
		}
		return null;
	}
	
	public Pro getLastPro(){
		if(pros.size()>0){
			return pros.get(pros.size()-1);
		}
		return null;
	}
	
	public void addOrd(Ord ord){
		ords.add(ord);
	}
	public void addPro(Pro pro){
		pros.add(pro);
	}
	
	public List<Ord> getOrds() {
		return ords;
	}
	public void setOrds(List<Ord> ords) {
		this.ords = ords;
	}
	public List<Pro> getPros() {
		return pros;
	}
	public void setPros(List<Pro> pros) {
		this.pros = pros;
	}
	
	
}
