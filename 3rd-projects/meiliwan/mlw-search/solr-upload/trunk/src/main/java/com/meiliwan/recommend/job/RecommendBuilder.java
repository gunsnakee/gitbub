package com.meiliwan.recommend.job;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.plugin.zk.ZKClient;
import com.meiliwan.emall.commons.util.IPUtil;
import com.meiliwan.emall.recommend.Recommender;
import com.meiliwan.search.job.basic.DBSource;

/**
 * 
 *  @Description 依然会对随机列表重建
 *	@author shanbo.liang
 */
public class RecommendBuilder {
	static MLWLogger logger = MLWLoggerFactory.getLogger(RecommendBuilder.class);
	DBSource productDB ;
	static String PRODUCT_SQL_TEMPLATE = "select pro_id from pro_product where state = 1" ;

	public RecommendBuilder() throws Exception{

		byte[] pptBuf = ZKClient.get().getData("/mlwconf/search/solrupload/product.properties");
		InputStream inStream = new ByteArrayInputStream(pptBuf);

		Properties properties = new Properties();
		properties.load(inStream);

		String dbUrl = properties.getProperty("dburl");
		String userName = properties.getProperty("dbusername");
		String password = properties.getProperty("dbpasswd");

		productDB = new DBSource(dbUrl, userName, password);
	}

	public void build(boolean delOld) throws SQLException, JedisClientException{
		ResultSet executeQuery = productDB.getStmt().executeQuery(PRODUCT_SQL_TEMPLATE);
		List<Integer> idSet = new ArrayList<Integer>();
		while(executeQuery.next()){
			idSet.add(executeQuery.getInt(1));
		}
		executeQuery.close();
		System.out.println("#products:" + idSet.size());
		logger.info("#products:" + idSet.size()  + "  #keyPrefixes:" + Recommender.keyPrefixes.length, 
				"", IPUtil.getLocalIp());
		

		for(int i = 0 ; i < Recommender.keyPrefixes.length; i++){
			System.out.println(Recommender.keyPrefixes[i]);
			if (delOld)
				ShardJedisTool.getInstance().del(JedisKey.recommend$prodRelated2, Recommender.keyPrefixes[i]);
			HashMap<String, Object> bigMap = new HashMap<String, Object>();
			for(Integer id : idSet){
				List<String> related = new ArrayList<String>();
				for(int j = 0 ; j < 12; j++)
					related.add(idSet.get(RandomUtils.nextInt(idSet.size())) +":1");
				related.remove(id + ":1");//
				String relatedString = StringUtils.join(related, ",");
				bigMap.put(id.toString(), relatedString);
			}
			ShardJedisTool.getInstance().hmset(JedisKey.recommend$prodRelated2, Recommender.keyPrefixes[i], bigMap);
			System.out.println("finished!");
		}
		System.out.println("all finished!!!!!!!!!!!!!!!!!!!!!!!!");
	}

	public void close() throws Exception{
		productDB.close();
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 1){
			System.out.println("exe <0|1>       #1 for delete old matrix");
			System.exit(0);
		}
		String delOld = args[0];
		boolean del = false;
		if (delOld.equals("1")){
			del = true;
		}
		RecommendBuilder rb = new RecommendBuilder();

		rb.build(del);
		rb.close();
		System.exit(0);
	}

}
