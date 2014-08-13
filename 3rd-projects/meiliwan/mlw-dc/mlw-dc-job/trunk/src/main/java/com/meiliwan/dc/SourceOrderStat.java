package com.meiliwan.dc;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.meiliwan.dc.ov.SourceChecker;
import com.meiliwan.dc.ov.SourceChecker.SourceInfo;
import com.meiliwan.dc.ov.SourceOrderCounter;
import com.meiliwan.emall.commons.util.BaseConfig;

public class SourceOrderStat {
	SourceChecker checker = new SourceChecker();

	String SQL_FORMAT = "insert into tj_source_order values('%s', '%s', '$0', '$1', $2, '$3', '$4', '$5')";

	static String SQL_FOOT_PRINTER = "insert into mc_foot_printer values(0,'$1','$2','$5','$6','$3','$4')";

	List<String> sqls = new ArrayList<String>();
	List<PageView> totalData = new ArrayList<PageView>();
	Map<String, List<String>> footMap = new HashMap<String, List<String>>();
	List<String> footSqls = new ArrayList<String>();

	public List<String> extract(List<PageView> pvs) {
		SourceInfo source = checker.getSource(pvs);
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < pvs.size(); i++) {
			PageView pv = pvs.get(i);
			String visit = pv.getVisitPage();
			if (visit.contains("/gateway/pay")
					|| visit.contains("/ucenter/order/payfull")
					|| visit.contains("/ucenter/order/finish")) {
				String orderId = SourceOrderCounter.extractOrderId(pvs.get(i)
						.getVisitPage());
				if (orderId != null) {
					result.add(SQL_FORMAT
							.replace("$0", source.getSourceSite())
							.replace("$1", orderId)
							.replace("$2", pv.getAccessTime() + "")
							.replace("$3", pv.getMcps())
							.replace(
									"$4",
									StringUtils.isBlank(pv.getMjid()) ? "" : pv
											.getMjid())
							.replace(
									"$5",
									StringUtils.isBlank(pv.getMc()) ? "" : pv
											.getMc()));
				}
			}

			String footKey = pv.mc + "##" + pv.mjid + "##"
					+ source.getSourceSite();
			List<String> printList = footMap.get(footKey);
			if (printList == null) {
				printList = new ArrayList<String>(20);
				footMap.put(footKey, printList);
			}
			printList.add("[" + pv.accessTime + "] " + pv.visitPage);
		}
		return result;
	}

	public void count(File logFile) throws IOException, SQLException,
			ClassNotFoundException {

		DirReader reader = new DirReader(logFile);
		for (String line = reader.readLine(); line != null; line = reader
				.readLine()) {
			PageView pv = LogParser.parse(line);
			if (pv != null) {
				totalData.add(pv);
			}
		}
		reader.close();
		System.out.println("parse finish");
		Collections.sort(totalData, new PageView.Sorter());

		List<PageView> pvsOfOne = new ArrayList<PageView>();
		String lastCookieId = "";
		System.out.println("start counting");
		for (PageView pv : totalData) {
			if (pv.cookieId.equals(lastCookieId)) {

			} else {
				if (!pvsOfOne.isEmpty()) {
					sqls.addAll(extract(pvsOfOne));
					pvsOfOne.clear();
				}
			}
			pvsOfOne.add(pv);
			lastCookieId = pv.cookieId;

		}
		// last
		sqls.addAll(extract(pvsOfOne));

		Set<String> footKeySet = footMap.keySet();
		for (String footKey : footKeySet) {
			String[] parts = footKey.split("##");
			List<String> foots = footMap.get(footKey);
			Collections.sort(foots);
			StringBuilder builder = new StringBuilder();
			if (foots != null && foots.size() > 0) {
				for (String foot : foots) {
					builder.append(foot);
					builder.append("#~");
				}

				builder.setLength(builder.length() - 2);
				footSqls.add(SQL_FOOT_PRINTER.replace("$1", parts[0])
						.replace("$2", parts[1]).replace("$3", parts[2])
						.replace("$4", builder.toString()));
			}
		}

	}

	public void resultToDB(String yestoday, DB db) throws SQLException {
		DateTime d = new DateTime();
		String statTime = d.toString("yyyy-MM-dd HH:mm:ss");

		int line = 0;
		Statement stmt = db.conn.createStatement();
		for (String sql : sqls) {
			stmt.addBatch(String.format(sql, statTime, yestoday));
			line += 1;
			if (line >= 2000) {
				stmt.executeBatch();
				stmt.clearBatch();
			}
		}
		stmt.executeBatch();
		stmt.clearBatch();

		for (String sql : footSqls) {
			stmt.addBatch(sql.replace("$5", statTime).replace("$6", yestoday));
			line += 1;
			if (line >= 2000) {
				stmt.executeBatch();
				stmt.clearBatch();
			}
		}
		stmt.executeBatch();
		stmt.clearBatch();

		stmt.close();

	}

	public static void main(String[] args) throws ClassNotFoundException,
			IOException, SQLException {
		SourceOrderStat sos = new SourceOrderStat();
		DateTime d = new DateTime();
		String statRange = d.minusDays(1).toString("yyyy-MM-dd");

		if (args.length > 0) {
			statRange = args[0];
		}
		System.out.println(statRange);
		sos.count(new File(BaseConfig.getValue("datadir")
				+ statRange.replace("-", "/")));
		DB db = new DB();
		sos.resultToDB(statRange, db);
		db.close();

		// System.out.println(SQL_FOOT_PRINTER.replace("$1",
		// "mlw-1398216758150345341").replace("$2",
		// "13982167640528246").replace("$3", "hot.17k.com").replace("$4",
		// "[1398216748212] http://www.meiliwan.com/sp/2014/watersplashing/index#~[1398216766212] https://passport.meiliwan.com/user/login?targetUrl=http://www.meiliwan.com/ucenter/order/list?g=1398216779192-==13982237111835306==photo.pclady.com.cn==[1398223698291] http://www.meiliwan.com/aroma.html"));

	}
}
