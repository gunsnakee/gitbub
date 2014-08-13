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
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.meiliwan.dc.ov.SourceLoginCounter;
import com.meiliwan.dc.ov.SourceOrderCounter;
import com.meiliwan.dc.ov.SourceOrderSaleCounter;
import com.meiliwan.dc.ov.SourcePvCounter;
import com.meiliwan.dc.ov.SourceRegisterCounter;
import com.meiliwan.dc.ov.SourceStat;
import com.meiliwan.dc.ov.SourceUserSessionCounter;
import com.meiliwan.dc.ov.SourceUvCounter;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.BaseConfig;

public class StatJob {
	
	private final static MLWLogger LOG = MLWLoggerFactory.getLogger(StatJob.class);
	
	List<PageView> totalData;

	Map<TableStat, Map<String, int[]> >  accountants;


	public StatJob() throws Exception{

		totalData = new ArrayList<PageView>();
		accountants = new HashMap<TableStat, Map<String, int[]>>();

	}


	public void addAccountant(TableStat... accountants){
		for(TableStat accountant : accountants){
			this.accountants.put(accountant, new HashMap<String, int[]>());
		}
	}

	private void addCountingMap(Map<String, int[]> counter, Map<String, int[]> stats){
		for(Entry<String, int[]> statpv : stats.entrySet()){
			int[] pvuv= counter.get(statpv.getKey());
			if (pvuv == null){
				pvuv = new int[statpv.getValue().length];
				System.arraycopy(statpv.getValue(), 0, pvuv, 0, statpv.getValue().length);
				counter.put(statpv.getKey(), pvuv);
			}else{
				for(int i = 0 ; i < pvuv.length; i ++){
					pvuv[i] += statpv.getValue()[i];
				}
			}
		}
	}


	public void count(File logFile) throws IOException, SQLException, ClassNotFoundException{
		DirReader reader = new DirReader(logFile);
		for(String line = reader.readLine(); line != null; line = reader.readLine()){
			try {
				PageView pv = LogParser.parse(line);
				if (pv != null){
					totalData.add(pv);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("err line . " + line);
				continue;
			}
		}
		
		reader.close();
		Collections.sort(totalData, new PageView.Sorter());

		List<PageView> pvsOfOne = new ArrayList<PageView>();
		String lastCookieId = "";
		for(PageView pv : totalData){
			if (pv.cookieId.equals(lastCookieId)){
			}else{
				if (!pvsOfOne.isEmpty()){
					for(Entry<TableStat , Map<String, int[]>> accountant : accountants.entrySet()){
						Map<String, int[]> stats = accountant.getKey().compute(pvsOfOne);
						addCountingMap(accountant.getValue(), stats);
					}
					pvsOfOne.clear();
					
				}
			}
			pvsOfOne.add(pv);
			lastCookieId = pv.cookieId;
		}
		//last
		for(Entry<TableStat , Map<String, int[]>> accountant : accountants.entrySet()){
			Map<String, int[]> stats = accountant.getKey().compute(pvsOfOne);
			addCountingMap(accountant.getValue(), stats);
		}

		

	}

	private void display(){
		for(Entry<TableStat , Map<String, int[]>> accountant : accountants.entrySet()){
			System.out.println("==============");
			for(Entry<String, int[]> stats : accountant.getValue().entrySet()){
				int [] allv = stats.getValue();
				String al = StringUtils.join(ArrayUtils.toObject(allv), ",");
				System.out.println(stats.getKey() + "\t=>\t[" + al + "]");
			}
			System.out.println("==============");
		}

	}
	
	
	public void resultToDB(DB db , String statRange) throws SQLException, ClassNotFoundException{

		Statement statement = db.conn.createStatement();
		DateTime d = new DateTime();
		String statTime = d.toString("yyyy-MM-dd HH:mm:ss");
		display();
		for(Entry<TableStat , Map<String, int[]>> accountant : accountants.entrySet()){

			for(Entry<String, int[]> stats : accountant.getValue().entrySet()){
				String template = accountant.getKey().getSQLFormat();
				int [] allv = stats.getValue();
				for(int i = 0 ; i < allv.length; i++){
					template = template.replace("$"+i, allv[i] + "");
				}
				String SQL = String.format(template, statTime, statRange, stats.getKey());
				statement.addBatch(SQL);
			}

		}
		statement.executeBatch();
		statement.close();
		

	}

	
	
	public static void main(String[] args) throws Exception {
		StatJob reaper = new StatJob();
		TableStat stat = new SourceStat();
		stat.addStatWorker(new SourcePvCounter(), new SourceUvCounter(), new SourceLoginCounter(), new SourceUserSessionCounter(),
					new SourceRegisterCounter(), new SourceOrderCounter(), new SourceOrderSaleCounter());
		reaper.addAccountant(stat);
		DateTime d = new DateTime();
		String statRange = d.minusDays(1).toString("yyyy-MM-dd");

		if (args.length > 0){
			statRange = args[0];
		}
		LOG.debug("state date: " + statRange);
		reaper.count(new File(BaseConfig.getValue("datadir") +  statRange.replace("-", "/")));

		DB db = new DB();
		reaper.resultToDB(db, statRange);
		db.close();
	}
}
