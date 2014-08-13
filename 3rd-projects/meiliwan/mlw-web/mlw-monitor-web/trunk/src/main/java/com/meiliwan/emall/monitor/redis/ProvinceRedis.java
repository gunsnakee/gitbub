package com.meiliwan.emall.monitor.redis;

import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.monitor.constant.Constant;

public class ProvinceRedis {

	private static final MLWLogger logger = MLWLoggerFactory
			.getLogger(ProvinceRedis.class);
	private ShardJedisTool redis;
	private static ProvinceRedis provinceRedis = new ProvinceRedis();

	public static ProvinceRedis getInstance() {
		return provinceRedis;
	}

	private ProvinceRedis() {
		try {
			redis = ShardJedisTool.getInstance();
		} catch (JedisClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initProvicne();
	}

	String[] provinces = new String[] { "上海", "云南", "内蒙古", "北京", "台湾", "吉林",
			"四川", "天津", "宁夏", "安徽", "山东", "山西", "广东", "广西", "新疆", "江苏", "江西",
			"河北", "河南", "浙江", "海南", "湖北", "湖南", "澳门", "甘肃", "福建", "西藏", "贵州",
			"辽宁", "重庆", "陕西", "青海", "香港", "黑龙江" };

	private void initProvicne() {
		for (String proName : provinces) {
			String count = getProvince(proName);
			if (count == null) {
				setProvince(proName, "0");
			}
		}
	}

	public void redisIncrement(String province) {

		String count = getProvince(province);
		if (count == null) {
			count = "0";
		}
		int countInt = Integer.parseInt(count);
		countInt++;
		setProvince(province, countInt + "");
	}

	private boolean setProvince(String key, String value) {

		try {
			return redis.hset(JedisKey.monitor$statistics,
					Constant.REDIS_MAP, key, value);
		} catch (JedisClientException e) {
			// TODO Auto-generated catch block
			logger.error(e, null, null);
		}
		return false;

	}

	private String getProvince(String key) {

		try {
			return redis.hget(JedisKey.monitor$statistics,
					Constant.REDIS_MAP, key);
		} catch (JedisClientException e) {
			// TODO Auto-generated catch block
			logger.error(e, null, null);
		}
		return null;

	}
}
