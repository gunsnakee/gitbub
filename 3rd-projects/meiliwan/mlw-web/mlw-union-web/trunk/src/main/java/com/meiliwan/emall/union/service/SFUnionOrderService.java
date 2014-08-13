package com.meiliwan.emall.union.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.mms.bean.UserPassport;
import com.meiliwan.emall.oms.bean.Ord;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.client.OrdClient;
import com.meiliwan.emall.union.bean.SFOrder;
import com.meiliwan.emall.union.bean.SFOrder.Goods;
import com.meiliwan.emall.union.dao.SFUnionOrderDao;
import com.meiliwan.emall.union.dao.UnionUserDao;

public class SFUnionOrderService {
	private final static MLWLogger LOGGER = MLWLoggerFactory.getLogger(SFUnionOrderService.class);
	private final String ZK_CONFIG_PATH="union-web/2wCode.properties";
	private final String SF_USERNAME_KEY="sf.username";
	private final String SF_PASSWORD_KEY = "sf.password";
			
	
	@Autowired
	private SFUnionOrderDao sFUnionOrderDao;
	@Autowired
	private UnionUserDao unionUserDao;
	@Autowired
	private OrdClient ordClient;
	
	
	
	
	public SFUnionOrderDao getsFUnionOrderDao() {
		return sFUnionOrderDao;
	}

	public void setsFUnionOrderDao(SFUnionOrderDao sFUnionOrderDao) {
		this.sFUnionOrderDao = sFUnionOrderDao;
	}

	public UnionUserDao getUnionUserDao() {
		return unionUserDao;
	}

	public void setUnionUserDao(UnionUserDao unionUserDao) {
		this.unionUserDao = unionUserDao;
	}

	public OrdClient getOrdClient() {
		return ordClient;
	}

	public void setOrdClient(OrdClient ordClient) {
		this.ordClient = ordClient;
	}

	/**
	 * 登录验证  
	 * @param userName
	 * @param password
	 * @return
	 */
	public boolean UserCheck(String userName,String password){
		if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
			return false;
		}
		
		try {
			String zkUserName = ConfigOnZk.getInstance().getValue(ZK_CONFIG_PATH, SF_USERNAME_KEY,"");
			String zkPassword = ConfigOnZk.getInstance().getValue(ZK_CONFIG_PATH, SF_PASSWORD_KEY, "");
			if (zkUserName.equals(userName)&&zkPassword.equals(password)) {
				return true;
			}
		} catch (BaseException e) {
			LOGGER.error(e, "fail to  get config from zk ", "");
		}
		
		return false;
	}
	
	/**
	 * 验证用户 是否存在合法的帐号
	 * @param emailList
	 * @return
	 */
	public List<String>  verifyEmailUser(String emailList){
		if (StringUtils.isBlank(emailList)) {
			LOGGER.info("param emailList is invalid", "", "");
			return new ArrayList<String>();
		}
		
		List<UserPassport> userPassports = unionUserDao.getUserList(getEmailParam(emailList));
		TreeSet<String> emailSet = new TreeSet<String>();
		String[] emails = StringUtils.split(emailList, " ");
		for (String email : emails) {
			emailSet.add(email);
		}
		
		for (UserPassport userPassport : userPassports) {
			if (emailSet.contains(userPassport.getEmail())) {
				emailSet.remove(userPassport.getEmail());
			}
		}
		
		List<String> invalidUsers = new ArrayList<String>();
		for (String string : emailSet) {
			invalidUsers.add(string);
		}
		
		return invalidUsers;
	}
	
	/**
	 * 构造顺丰订单		如果订单数据量过多，会存在DB查询效率问题
	 * @param sfShop
	 * @param startTime
	 * @param endTime
	 * @param fsOrderEnum
	 * @return
	 */
	public  List<SFOrder> getSFOrderList(Map<String, SFOrder> sfShop,Date startTime,Date endTime,FSOrderEnum fsOrderEnum){
		StringBuilder emailList = new StringBuilder();
		
		Iterator<String> shEmails = sfShop.keySet().iterator();
		while (shEmails.hasNext()) {
			emailList.append("'").append(shEmails.next()).append("'").append(",");
		}
		emailList.setLength(emailList.length()-1);
		
		List<UserPassport> userPassports = unionUserDao.getUserList(emailList.toString());
		StringBuilder uids = new StringBuilder();
		Map<Integer, String> uidEmailMap = new TreeMap<Integer, String>();
		for (UserPassport userPassport : userPassports) {
			uids.append(userPassport.getUid()).append(",");
			uidEmailMap.put(userPassport.getUid(), userPassport.getEmail());
		}
		if(uids.length() > 0){
			uids.setLength(uids.length()-1);
		}else{
			return null;
		}
		
		
		List<Ord> oList = null;
		if (fsOrderEnum.getValue() == FSOrderEnum.ORDI_ALL.getValue()) {
			String orderStatus = "30,40,50,60";
			oList =  sFUnionOrderDao.getSForderList(uids.toString(), startTime, endTime,orderStatus);
		}
		else {
			oList  = sFUnionOrderDao.getSForderList(uids.toString(), startTime, endTime,String.valueOf(fsOrderEnum.getValue()));
		}
			
		List<SFOrder> sfOrders = new ArrayList<SFOrder>();
		for (Ord ord : oList) {
				SFOrder sfOrder = new SFOrder();
				SFOrder excelOrder = sfShop.get(uidEmailMap.get(ord.getUid()));
				if(excelOrder == null){
					sfOrder.setShopEmail(uidEmailMap.get(ord.getUid()));
				}else{
					sfOrder.setShopEmail(excelOrder.getShopEmail());
					sfOrder.setArea(excelOrder.getArea());
					sfOrder.setProvince(excelOrder.getProvince());
					sfOrder.setShopName(excelOrder.getShopName());
					sfOrder.setShopId(excelOrder.getShopId());
				}
				sfOrder.setOrderId(ord.getOrderId());
				sfOrder.setCreateTime(ord.getCreateTime());
//				sfOrder.setGoodsCount(ord.getTotalItem());
				sfOrder.setOrderStatus(getOrderMapperStatus(ord.getOrderStatus()));
				sfOrder.setOrderTotalPrice(ord.getRealPayAmount());
				sfOrder.setOrderRealPay(ord.getOrderSaleAmount());
				sfOrder.setPayTime(ord.getPayTime());

				
				if(sfOrder.getOrderGoodsItems() == null){
					sfOrder.setOrderGoodsItems(new ArrayList<Goods>());
				}
				
				List<Ordi> ordis = ordClient.getOrdiListByOrderId(ord.getOrderId()) ;
				if(ordis != null){
					for (Ordi ordi : ordis) {
						SFOrder.Goods goods = sfOrder.new Goods();
						goods.setGoodsId(ordi.getProId());
						goods.setGoodsName(ordi.getProName());
						goods.setGoodsPrice(ordi.getUintPrice());
						sfOrder.getOrderGoodsItems().add(goods);
						goods.setSaleNum(ordi.getSaleNum());
					}
					
					sfOrders.add(sfOrder);
				}else{
					LOGGER.warn("no order items", "orderId:" + ord.getOrderId(), null);
				}
				
		}
		return sfOrders;
	}
	
	
	private String getEmailParam(String emailList){
		StringBuilder stringBuilder = new StringBuilder();
		String[] strs = StringUtils.split(emailList, " ");
		
		for (String string : strs) {
			stringBuilder.append("'").append(string).append("'").append(",");
		}
		
		stringBuilder.setLength(stringBuilder.length()-1);
		return stringBuilder.toString();
	}
	
	private String getOrderMapperStatus(String key){
		if ("30".equalsIgnoreCase(key)) {
			return "待发货";
		} else if ("40".equalsIgnoreCase(key)) {
			return "等待确认收货";
		} else if ("50".equalsIgnoreCase(key)) {
			return "已收货已付款";
		} else  {
			return "交易成功";
		}
		
	}
	
	/**
	 * 顺丰状态查询 订单状态枚举
	 * @author yuzhe
	 *
	 */
	public enum FSOrderEnum{
		ORDI_EFFECTIVED(30),
		ORDI_CONSINGMENT(40),
		ORDI_RECEIPTED(50),
		ORDI_FINISHED(60),
		ORDI_ALL(-1);
		
		private final int value;
		FSOrderEnum(int value){
			this.value = value;
		}
		
		public int getValue(){
			return value;
		}
	}
}
