package com.meiliwan.emall.sp2.client;

import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.sp2.bean.LotteryResult;

public class LotteryResultClient {

	private static final MLWLogger logger = MLWLoggerFactory
			.getLogger(LotteryResultClient.class);

	private static String getMethod(String method) {
		return String.format("lotteryResultService/%s", method);
	}

	public static List<LotteryResult> all() {

		try {
			JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
					JSONTool.buildParams(getMethod("all"), 10));
			return new Gson().fromJson(obj.get("resultObj"),
					new TypeToken<List<LotteryResult>>() {
					}.getType());
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			logger.error(e, null, null);
		}
		return Collections.emptyList();
	}

	public static void insert(LotteryResult result)
			throws ServiceException {
		IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
				JSONTool.buildParams(getMethod("insert"), result));
	}

	public static PagerControl<LotteryResult> page( PageInfo pageInfo) {
        JsonObject obj=null;
        obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams(getMethod("page"),pageInfo));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<LotteryResult>>() {
        }.getType());
    }
	
	/**
	 * 取得最新一个
	 * @param uid
	 * @return
	 */
	public static LotteryResult getTopByUid( int uid) {
		try {
			JsonObject obj=null;
			obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
					JSONTool.buildParams(getMethod("getTopByUid"),uid));
			return new Gson().fromJson(obj.get("resultObj"), LotteryResult.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			logger.error(e, null, null);
		}
		return null;
	}
}
