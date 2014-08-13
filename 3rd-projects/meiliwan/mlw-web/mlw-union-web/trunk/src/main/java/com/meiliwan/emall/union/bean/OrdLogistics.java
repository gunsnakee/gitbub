package com.meiliwan.emall.union.bean;



/**
 * id, int(11), NO, PRI, , auto_increment
ordi_id, varchar(20), NO, , , 
order_id, varchar(20), YES, , , 
logistics_company, varchar(20), YES, , , 
logistics_number, varchar(20), YES, , , 
bill_type, int(2), YES, , , 
uid, int(11), YES, , , 
update_time, timestamp, NO, , CURRENT_TIMESTAMP, on update CURRENT_TIMESTAMP
create_time, timestamp, NO, , 0000-00-00 00:00:00, 


 * @author rubi
 *
 */
public class OrdLogistics{
	
    private String order_id;
    private String logistics_company;
    private String logistics_number;
    
    public boolean isEMS(){
    		if("EMS".equalsIgnoreCase(logistics_company)){
    			return true;
    		}
    		return false;
    }
    public boolean isSF(){
    		if("SF".equalsIgnoreCase(logistics_company)){
			return true;
		}
    		return false;
    }
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getLogistics_company() {
		return logistics_company;
	}
	public void setLogistics_company(String logistics_company) {
		this.logistics_company = logistics_company;
	}
	public String getLogistics_number() {
		return logistics_number;
	}
	public void setLogistics_number(String logistics_number) {
		this.logistics_number = logistics_number;
	}
	@Override
	public String toString() {
		return "OrdLogistics [order_id=" + order_id + ", logistics_company="
				+ logistics_company + ", logistics_number=" + logistics_number
				+ "]";
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((logistics_company == null) ? 0 : logistics_company
						.hashCode());
		result = prime
				* result
				+ ((logistics_number == null) ? 0 : logistics_number.hashCode());
		result = prime * result
				+ ((order_id == null) ? 0 : order_id.hashCode());
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
		OrdLogistics other = (OrdLogistics) obj;
		if (logistics_company == null) {
			if (other.logistics_company != null)
				return false;
		} else if (!logistics_company.equals(other.logistics_company))
			return false;
		if (logistics_number == null) {
			if (other.logistics_number != null)
				return false;
		} else if (!logistics_number.equals(other.logistics_number))
			return false;
		if (order_id == null) {
			if (other.order_id != null)
				return false;
		} else if (!order_id.equals(other.order_id))
			return false;
		return true;
	}
	
    
}