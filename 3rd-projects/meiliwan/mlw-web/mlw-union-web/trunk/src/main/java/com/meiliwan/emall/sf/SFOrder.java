package com.meiliwan.emall.sf;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
<Request service='OrderService' lang='zh-CN'>
        <Head>7710332275, [cn3MPmYKrp38eN-</Head><Body>
         <Order orderid='MLCS1306170001'
                  express_type='1'
                  j_province='广西壮族自治区'                                           
                   j_city='南宁市'              
                  j_company='美丽传说股份有限公司'
                  j_contact='客服'
                  j_tel=' 18665311931'
                  j_address='广西壮族自治区南宁市XX区XX路XXX号'
                  d_province='广东省'                                             
                  d_city='深圳市'
                  d_company='顺丰速运'
                  d_contact='小顺'
                  d_tel='0755-33992159'
                  d_address='广东省深圳市福田区新洲十一街万基商务大厦'
                  parcel_quantity='1'
                  pay_method='1'>
                 <OrderOption custid='7710332275' 
                           cargo='货物'
                           mailno='655123456789' > 
                    </OrderOption>                                                                          
                   </Order></Body>
         </Request>  
 * @author rubi
 *
 */
public class SFOrder {

	private String service="OrderService";
	private String checkword="nLyvmUj1{KSnP:pl]kiWQutsuBdgi634";
	private String custCode="7710332275";
	private String orderid="";
	private String express_type="1";
	private String j_province="广西壮族自治区";
	private String j_city="南宁市";
	private String j_company="美丽传说股份有限公司";
	private String j_contact="客服";
	private String j_tel="0771-5086886";
	private String j_address="广西省南宁市总部路1号中国-东盟科技企业孵化基地一期D5栋";
	private String d_province="广东省";
	private String d_city="深圳市";
	private String d_company="顺丰速运";
	private String d_contact="小顺";
	private String d_tel="0755-33992159";
	private String d_address="广东省深圳市福田区新洲十一街万基商务大厦";
	private String parcel_quantity="1";
	private String pay_method="1";
	
	private String cargo="货物";
	private String mailno;
	
	private SFOrder(){};
	public  SFOrder(String orderid,String mailno){
		this.orderid=orderid;
		this.mailno=mailno;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getExpress_type() {
		return express_type;
	}
	public void setExpress_type(String express_type) {
		this.express_type = express_type;
	}
	public String getJ_province() {
		return j_province;
	}
	public void setJ_province(String j_province) {
		this.j_province = j_province;
	}
	public String getJ_city() {
		return j_city;
	}
	public void setJ_city(String j_city) {
		this.j_city = j_city;
	}
	public String getJ_company() {
		return j_company;
	}
	public void setJ_company(String j_company) {
		this.j_company = j_company;
	}
	public String getJ_contact() {
		return j_contact;
	}
	public void setJ_contact(String j_contact) {
		this.j_contact = j_contact;
	}
	public String getJ_tel() {
		return j_tel;
	}
	public void setJ_tel(String j_tel) {
		this.j_tel = j_tel;
	}
	public String getJ_address() {
		return j_address;
	}
	public void setJ_address(String j_address) {
		this.j_address = j_address;
	}
	public String getD_province() {
		return d_province;
	}
	public void setD_province(String d_province) {
		this.d_province = d_province;
	}
	public String getD_city() {
		return d_city;
	}
	public void setD_city(String d_city) {
		this.d_city = d_city;
	}
	public String getD_company() {
		return d_company;
	}
	public void setD_company(String d_company) {
		this.d_company = d_company;
	}
	public String getD_contact() {
		return d_contact;
	}
	public void setD_contact(String d_contact) {
		this.d_contact = d_contact;
	}
	public String getD_tel() {
		return d_tel;
	}
	public void setD_tel(String d_tel) {
		this.d_tel = d_tel;
	}
	public String getD_address() {
		return d_address;
	}
	public void setD_address(String d_address) {
		this.d_address = d_address;
	}
	public String getParcel_quantity() {
		return parcel_quantity;
	}
	public void setParcel_quantity(String parcel_quantity) {
		this.parcel_quantity = parcel_quantity;
	}
	public String getPay_method() {
		return pay_method;
	}
	public void setPay_method(String pay_method) {
		this.pay_method = pay_method;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	public String getMailno() {
		return mailno;
	}
	public void setMailno(String mailno) {
		this.mailno = mailno;
	}
	public List<WaybillRoute> getRoutes() {
		return routes;
	}
	public void setRoutes(List<WaybillRoute> routes) {
		this.routes = routes;
	}
	public String getCheckword() {
		return checkword;
	}
	public void setCheckword(String checkword) {
		this.checkword = checkword;
	}
	public String getCustCode() {
		return custCode;
	}
	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}
	private List<WaybillRoute> routes= new ArrayList<WaybillRoute>();
	
	public void addRoute(WaybillRoute rout){
		routes.add(rout);
	}
	public String toXml(){
		StringBuilder sb = new StringBuilder();
		sb.append("<Request service='"+service+"' lang='zh-CN'>");
        sb.append("<Head>"+custCode+","+checkword+"</Head><Body>");
        sb.append("<Order orderid='"+orderid+"'").append(" express_type='"+express_type+"'");
        sb.append(" j_province='"+j_province+"'").append(" j_city='"+j_city+"'");
        sb.append(" j_company='"+j_company+"'").append(" j_contact='"+j_contact+"'");
        sb.append(" j_tel='"+j_tel+"'").append(" j_address='"+j_address+"'");
        sb.append(" d_province='"+d_province+"'").append(" d_city='"+d_city+"'");
        sb.append(" d_company='"+d_company+"'").append(" d_contact='"+d_contact+"'");
        sb.append(" d_tel='"+d_tel+"'").append(" d_address='"+d_address+"'");
        sb.append(" parcel_quantity='"+parcel_quantity+"'").append(" pay_method='"+pay_method+"'>");
        
        sb.append("<OrderOption custid='"+custCode+"' ").append(" cargo='"+cargo+"'");
        sb.append(" mailno='"+mailno+"' >");
        sb.append("</OrderOption></Order></Body></Request>");
        
        return sb.toString();
	}
 
}
