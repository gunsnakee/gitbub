package com.meiliwan.emall.service.union.handler.impl;

import java.sql.Timestamp;
import java.util.List;

import com.meiliwan.emall.commons.bean.CpsOrderVO;
import com.meiliwan.emall.commons.bean.CpsOrderVO.CpsProduct;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.NumberUtil;
import com.meiliwan.emall.mms.bean.UserForeign;
import com.meiliwan.emall.mms.client.UserForeignClient;

public class LinkTechHandler extends TwoDimCodeHandler  {
	
	private final static String FOREIGN_SOURCE = "qq";
	private final static String openIdPrefix = "A100136514";
	private final static String valSpliter = "||";
	
	@Override
	public String buildPushParam(CpsOrderVO cpsOrder) {
		StringBuilder p_cd_builder = new StringBuilder();
		StringBuilder it_cnt_builder = new StringBuilder();
		StringBuilder price_builder = new StringBuilder();
		
		List<CpsProduct> proList = cpsOrder.getProList();
		if (proList != null && proList.size() > 0) {
			for (CpsProduct product : proList) {
				String price = NumberUtil.format(product.getPrice());

				p_cd_builder.append(product.getProId());
				p_cd_builder.append(valSpliter);

				it_cnt_builder.append(product.getBuyNum());
				it_cnt_builder.append(valSpliter);

				price_builder.append(price);
				price_builder.append(valSpliter);
			}
		}
		
		String p_cd_str = "";
		String it_cd = "";
		String price_cd = "";
		if (p_cd_builder.length() > 0) {
			int spliterLength = valSpliter.length();
			p_cd_builder.setLength(p_cd_builder.length() - spliterLength);
			p_cd_str = p_cd_builder.toString();

			it_cnt_builder.setLength(it_cnt_builder.length() - spliterLength);
			it_cd = it_cnt_builder.toString();

			price_builder.setLength(price_builder.length() - spliterLength);
			price_cd = price_builder.toString();
		}

		String queryParam = "a_id="
				+ cpsOrder.getSourceId() + "&m_id=" + "meiliwan"
				+ "&mbr_id=0&o_cd=" + cpsOrder.getOrderId() + "&p_cd="
				+ p_cd_str + "&it_cnt=" + it_cd + "&price=" + price_cd;
		
		int uid = cpsOrder.getUid();
		UserForeign userForeign = UserForeignClient.getForeignByUid(uid, FOREIGN_SOURCE);
		if (userForeign != null&& "CaiBei".equals(userForeign.getUnionType())) {
			queryParam += ("&mbr_name="+openIdPrefix+userForeign.getForeignUid());
		}
		
		return queryParam;
	}

	@Override
	public String buildQueryInfo(CpsOrderVO cpsOrder) {

		StringBuilder queryInfo = new StringBuilder();
		
		List<CpsProduct> proList = cpsOrder.getProList();
		if (proList != null && proList.size() > 0) {
			Timestamp currTime = new Timestamp(System.currentTimeMillis());
			String time = DateUtil.formatDate(currTime, "HHmmss");
			for (CpsProduct product : proList) {
				String price = NumberUtil.format(product.getPrice());

				queryInfo.append(2);
				queryInfo.append("\t");
				queryInfo.append(time);
				queryInfo.append("\t");
				queryInfo.append(cpsOrder.getSourceId());
				queryInfo.append("\t");
				queryInfo.append(cpsOrder.getOrderId());
				queryInfo.append("\t");
				queryInfo.append(product.getProId());
				queryInfo.append("\t");
				queryInfo.append(0);
				queryInfo.append("\t");
				queryInfo.append(product.getBuyNum());
				queryInfo.append("\t");
				queryInfo.append(price);
				queryInfo.append("\n");
			}
		}
		
		return queryInfo.toString();
	}

	@Override
	public String getPushUrl() {
		return "http://service.linktech.cn/purchase_cps.php";
	}

	@Override
	public boolean isStoped() {
		boolean isStoped = false;
		try {
			isStoped = Boolean.valueOf(ConfigOnZk.getInstance().getValue("commons/system.properties", "linkTech.isStoped", "false").trim());
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return isStoped;
	}

}
