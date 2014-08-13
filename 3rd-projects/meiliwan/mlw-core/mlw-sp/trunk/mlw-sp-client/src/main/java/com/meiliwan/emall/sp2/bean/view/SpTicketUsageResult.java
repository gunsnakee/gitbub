package com.meiliwan.emall.sp2.bean.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import com.meiliwan.emall.commons.util.NumberUtil;
import com.meiliwan.emall.sp2.bean.SpTicket;
import com.meiliwan.emall.sp2.constant.SpTicketType;

public class SpTicketUsageResult implements Serializable{
	
	private static final long serialVersionUID = -1179561386195291618L;

	private boolean status;
	
	private BigDecimal discountPrice;
	
	private Map<SimpleOrdi, BigDecimal> ordiDiscountResult;
	
	private Map<Integer, SimpleOrdi> proIdSoiMap;
	
	
	private List<SimpleSpTicket> ssts ;
	
	public SpTicketUsageResult() {
		ordiDiscountResult = new LinkedHashMap<SimpleOrdi, BigDecimal>();
		proIdSoiMap = new HashMap<Integer, SimpleOrdi>();
		ssts  = new ArrayList<SimpleSpTicket>();
		status = true;
		discountPrice = new BigDecimal(0.0);
	}
	

	public boolean isStatus() {
		return status;
	}


	public void setStatus(boolean status) {
		this.status = status;
		// if false;
		if (!status) {
			discountPrice = new BigDecimal(0.0);
		}
	}

	public BigDecimal getDiscountPrice() {
		return discountPrice;
	}
	
	public void setDiscountPrice(BigDecimal discountPrice) {
		this.discountPrice = discountPrice;
	}


	@Override
	public String toString() {
		return "SpTicketUsageResult [status=" + status + ", discountPrice="
				+ discountPrice + ", ordiDiscountResult=" + ordiDiscountResult
				+ ", ssts=" + ssts + "]";
	}


	public Map<SimpleOrdi, BigDecimal> getOrdiDiscountResult() {
		return ordiDiscountResult;
	}

	public void setOrdiDiscountResult(Map<SimpleOrdi, BigDecimal> ordiDiscountResult) {
		this.ordiDiscountResult = ordiDiscountResult;
		
		for (SimpleOrdi soi : ordiDiscountResult.keySet()) {
			proIdSoiMap.put(soi.getProId(), soi);
		}
		
	}
	
	public void addSimpleOrdi(SimpleOrdi soi) {
		ordiDiscountResult.put(soi, soi.getPrice());
		
		proIdSoiMap.put(soi.getProId(), soi);
	}
	
	public void updSimpleOrdiDiscount(SimpleOrdi soi, BigDecimal discount) {
		BigDecimal rprice = ordiDiscountResult.get(soi).subtract(discount)  ;
		ordiDiscountResult.put(soi, rprice);
		
		this.discountPrice = discountPrice.add(discount);
	}
	
	public SimpleSpTicket[] getUsedSimpleTickets(){
		if (ssts == null || ssts.isEmpty()) {
			return null;
		}
		return ssts.toArray(new SimpleSpTicket[ssts.size()]);
	}
	
	/**
	 * 普通商品券
	 * @param spTicket
	 */
	public void useCommonSpTicket(SpTicket spTicket) {
		
		if (SpTicketType.COMMON != SpTicketType.valueOf(spTicket.getTicketType())) {
			throw new IllegalArgumentException(String.format("SpTicket {} should be common", spTicket));
		}
		
		Map<Integer, Double> proIdPriceValueMap = new HashMap<Integer, Double>();
		
		double sum = 0;
		// 优惠券的处理是折扣上再折扣
		for (SimpleOrdi soi : ordiDiscountResult.keySet()) {
			proIdPriceValueMap.put(soi.getProId(), ordiDiscountResult.get(soi).doubleValue());
			sum += soi.getPrice().doubleValue();
		}
		
		// get discount
		double p = spTicket.getTicketPrice().doubleValue();
		if (sum < spTicket.getTicketPrice().doubleValue()) {
			p = sum;
		}
		Map<SimpleOrdi, BigDecimal> soiDiscountMap = amortizeByPriceRatio(proIdPriceValueMap, p);
		
		// add ticket
		SimpleSpTicket sst = new SimpleSpTicket();
		sst.setSoiDiscountMap(soiDiscountMap);
		sst.setTicketId(spTicket.getTicketId());
		sst.setStType(SpTicketType.COMMON);
		ssts.add(sst);
	}
	
	
	
	/**
	 * @param cateSpTicket 品类券
	 * @param proIds  符合目标品类券使用的商品id
	 */
	public void useCategorySpTicket(SpTicket cateSpTicket, int[] proIds) {
		
		if (SpTicketType.CATEGORY != SpTicketType.valueOf(cateSpTicket.getTicketType()) || proIds == null) {
			throw new IllegalArgumentException(String.format("SpTicket %s should be common or proIds [%s] == null", cateSpTicket, proIds.toString()));
		}
		// check
		for (int proId : proIds) {
			if (!proIdSoiMap.containsKey(Integer.valueOf(proId))) {
				throw new IllegalArgumentException(String.format("proIds [%s] should be in range of this sr_ticketusage_result", cateSpTicket, ArrayUtils.toString(proIds)));
			}
		}
		
		Map<Integer, Double> proIdPriceValueMap = new HashMap<Integer, Double>();
		double sum = 0;
		// 优惠券的处理是折扣上再折扣
		for (int proId : proIds) {
			SimpleOrdi soi = proIdSoiMap.get(proId);
			proIdPriceValueMap.put(proId, ordiDiscountResult.get(soi).doubleValue());
			sum += soi.getPrice().doubleValue();
		}
		
		double p = cateSpTicket.getTicketPrice().doubleValue();
		if (sum < cateSpTicket.getTicketPrice().doubleValue()) {
			p = sum;
		}
		Map<SimpleOrdi, BigDecimal> soiDiscountMap = amortizeByPriceRatio(proIdPriceValueMap, p);
		
		SimpleSpTicket sst = new SimpleSpTicket();
		sst.setTicketId(cateSpTicket.getTicketId());
		sst.setStType(SpTicketType.CATEGORY);
		sst.setSoiDiscountMap(soiDiscountMap);
		ssts.add(sst);
	}


	private Map<SimpleOrdi, BigDecimal> amortizeByPriceRatio(
			Map<Integer, Double> proIdPriceValueMap,
			double p) {
		
		this.discountPrice = this.discountPrice.add(new BigDecimal(p));
		Map<Integer, Double> amortizedMap =  NumberUtil.amortizeByPriceRatio(proIdPriceValueMap, p);
		
		Map<SimpleOrdi, BigDecimal> soiDiscountMap = new HashMap<SimpleOrdi, BigDecimal>();
		for (Integer proId : amortizedMap.keySet()) {
			SimpleOrdi soi = proIdSoiMap.get(proId);
			double discount = amortizedMap.get(proId);
			double price = ordiDiscountResult.get(soi).doubleValue();
			ordiDiscountResult.put(soi, new BigDecimal(price - discount));
			
			soiDiscountMap.put(soi, new BigDecimal(discount));
		}
		return soiDiscountMap;
	}
	
	public void df(){
		
	}
	
}
