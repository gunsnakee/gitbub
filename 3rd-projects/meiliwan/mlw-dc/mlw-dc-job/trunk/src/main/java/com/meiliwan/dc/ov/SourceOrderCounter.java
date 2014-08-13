package com.meiliwan.dc.ov;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.meiliwan.dc.DB;
import com.meiliwan.dc.PageView;
import com.meiliwan.dc.StatWorker;
import com.meiliwan.dc.ov.SourceChecker.SourceInfo;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.BaseConfig;

public class SourceOrderCounter implements StatWorker {

	private final static MLWLogger LOG = MLWLoggerFactory
			.getLogger(SourceOrderCounter.class);

	SourceChecker checker;

	public SourceOrderCounter() {
		checker = new SourceChecker();
	}

	public static String extractOrderId(String url) {
		int i = url.indexOf("?");
		if (i < 0)
			return null;
		String param = url.substring(i + 1);
		try {
			Map<String, String> split = Splitter.on("&")
					.withKeyValueSeparator("=").split(param);
			return StringUtils.isBlank(split.get("orderId")) ? split.get("oid")
					: split.get("orderId");
		} catch (Exception ex) {
			LOG.error(ex, "url:" + url, null);
		}

		return null;
	}

	public Map<String, Integer> stat(List<PageView> pvs) {
		SourceInfo source = checker.getSource(pvs);
		Map<String, Integer> result = new HashMap<String, Integer>();
		HashSet<String> orders = new HashSet<String>();

		for (int i = source.indexOfList; i < pvs.size(); i++) {
			String visit = pvs.get(i).getVisitPage();

			if (visit.contains("/gateway/pay")
					|| visit.contains("/ucenter/order/payfull")
					|| visit.contains("/ucenter/order/finish")) {
				String orderId = extractOrderId(visit);
				// LOG.info("visit url:" , visit + ",source:" +
				// source.sourceSite, null);
				if (orderId != null && !orders.contains(orderId)) {
					orders.add(orderId);
				}
			}

		}

		int numOrder = 0;
		if (!orders.isEmpty()) {
			String sql = "select count(*) from oms_ord where order_id in ('"
					+ Joiner.on("','").join(orders)
					+ "') and order_status in (30, 40, 60) ";

			try {
				Connection con = new DB(BaseConfig.getValue("dcbase_url"),
						BaseConfig.getValue("dcbase_user"),
						BaseConfig.getValue("dcbase_pass")).getConn();
				Statement sts = con.createStatement();
				ResultSet rs = sts.executeQuery(sql);
				while (rs.next()) {
					numOrder = rs.getInt(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		result.put(source.sourceSite, numOrder);
		return result;
	}

}
