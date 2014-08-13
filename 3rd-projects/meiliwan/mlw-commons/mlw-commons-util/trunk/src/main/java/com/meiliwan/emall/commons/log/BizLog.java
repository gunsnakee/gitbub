package com.meiliwan.emall.commons.log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.web.UserLoginUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.meiliwan.emall.commons.constant.GlobalNames;
import com.meiliwan.emall.commons.exception.BizLogFormatException;
import com.meiliwan.emall.commons.rabbitmq.MsgSender;
import com.meiliwan.emall.commons.util.IPUtil;
import com.meiliwan.emall.commons.web.UserCookieUtil;
import com.meiliwan.emall.commons.web.WebUtils;

/**
 * @author leo
 * 唯一通过 BizLog.LogBuilder.build() 进行实例化;
 * 
 * eg:
 * 该对象的构建代码示例如下：
 * {@code
 * Map<String, String> extraDataMap = new HashMap<String, String>();
 * 使用金额数
 * extraDataMap.put("quantity", "38");
 * # why
 * extraDataMap.put("target", "buyProduct");
 * # with product id
 * extraDataMap.put("productId", "123198898198");
 * # with order id
 * extraDataMap.put("orderId", "30989771279");
 *
 * BizLog bizLog = new BizLog.LogBuilder(BizLogModel.BKSTAGE, new BizLogSubModel() {
 *
 *public String getSubModelName() {
 *	return "SubModelName";
 *}
 *}, "SP-smallsp-add", new Date())
 * setInfoFromHttpRequest(request) 自动从request中进行信息提取（userClientIp, userid, username, ）;
 *.setObj(BizLogObj, "a231920990")
 *.setOp(BizLogOp)
 * 以上必填项
 *
 * #extradata建议填
 *
 *.setExtraData(new Gson().toJson(extraDataMap))
 *
 *.setBlkFlag()   // 设置为后台动作-1, 默认为前台动作-1
 *
 *.setHost("account.meiliwan.com")
 *.setUrl("http://account.meiliwan.com/asck");
 *.setUserClientIp("10.249.10.110")
 *.build();
 * }
 * 
 */
public final class BizLog implements Serializable {

	private static final long serialVersionUID = 5637422026286450943L;

	private static final Logger LOG = LoggerFactory.getLogger(BizLog.class);

	private static final String SDFDATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private static final String LOG_SPLIT = ",";

	/**
	 * model
	 */
	private String model;

	/**
	 * 大模块中的小模块: 如用户模块中的：1.帐户，2.咨询 3.钱包 等
	 */
	private String subModel;

	/**
	 * logcode ， logcode的命名方式同异常code规范;
	 */
	private String logCode;

	/**
	 * 判断日志前后台系统标志位; -1：后台， 1：前台
	 */
	private int flag;
	
	/**
	 * 用户id,前端系统的用户的网站id 或 后台系统管理员的id
	 */
	private int userId;

	/**
	 * 用户name
	 */
	private String userName = "";

	/**
	 * 标识用户的唯一cookie值
	 */
	private String uc = "";

	/**
	 * 业务对象
	 */
	private String obj;

	/**
	 * 业务对象id
	 */
	private String objId;

	/**
	 * 操作行为
	 */
	private String op;

	/**
	 * 业务发生所在的域名
	 */
	private String host;

	/**
	 * 业务发生所在的页面
	 */
	private String url;

	/**
	 * 操作业务的用户个人机器ip
	 */
	private String userClientIp;

	/**
	 * 承接服务，记录log的服务器ip
	 */
	private String svrIp;

	/**
	 * 日志时间
	 */
	private Date time;

	/**
	 * extraData
	 */
	private String extraData;

	/**
	 * 业务描述
	 */
	private String descp;
	
	public BizLog(){}
	
	public String getModel() {
		return model;
	}

	public String getSubModel() {
		return subModel;
	}

	public String getLogCode() {
		return logCode;
	}

	public int getFlag() {
		return flag;
	}

	public int getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getUc() {
		return uc;
	}

	public String getObj() {
		return obj;
	}

	public String getObjId() {
		return objId;
	}

	public String getOp() {
		return op;
	}

	public String getHost() {
		return host;
	}

	public String getUrl() {
		return url;
	}

	public String getUserClientIp() {
		return userClientIp;
	}

	public String getSvrIp() {
		return svrIp;
	}

	public Date getTime() {
		return time;
	}

	public String getExtraData() {
		return extraData;
	}

	public String getDescp() {
		return descp;
	}

	private static String strf(final String str) {
		if (StringUtils.isBlank(str)) {
			return "\"?\"";
		}
		String fstr = pickoffQuotAndLf(StringUtils.trim(str));
		// convert : " -> \"
		fstr = fstr.replaceAll("\"", "\\\\\"");
		return "\"" + fstr + "\"";
	}

	/**
	 * 将字符串中的引号和换行符剔除
	 * 
	 * @param str
	 * @return
	 */
	private static String pickoffQuotAndLf(String str) {
		char[] strC = str.toCharArray();
		char[] strN = new char[str.length()];
		for (int i = 0, ni = 0; i < strC.length; i++) {
			if (strC[i] != '\n' && strC[i] != '\r') {
				strN[ni] = strC[i];
				ni++;
			}
		}
		return new String(strN).trim();
	}

	private static String numberf(final Number n) {
		return String.valueOf(n);
	}

	private static String timef(final Date time) {
		String str = null;
		if (time == null) {
			str = "";
		}
		str = new SimpleDateFormat(SDFDATE_FORMAT).format(time);
		return strf(str);
	}
	
	public static final class LogBuilder {

		private String model;

		private String subModel;

		private String logCode;

		private int flag ;

		private String host;

		private String url;

		private String userClientIp;

		private String svrIp;

		private Date time;

		private int userId;

		private String userName;

		private String uc;

		private String obj;

		private String objId;

		private String op;

		private String extraData;

		private String descp;

		/**
		 * model , submodel, logcode , time必传参数;
		 * 
		 * @param model
		 * @param subModel
		 * @param logCode
		 */
		public LogBuilder(BizLogModel model, BizLogSubModel bizLogSubModel, String logCode, Date time) {
			this.model = model.toMqModel().mqname();
			this.subModel = bizLogSubModel.getSubModelName();
			this.logCode = logCode;
			this.time = time;
			this.flag = 1;
			this.svrIp = IPUtil.getLocalIp();
		}
		
		/**
		 * model , submodel, logcode 必传参数;
		 * 
		 * @param model
		 * @param subModel
		 * @param logCode
		 */
		public LogBuilder(BizLogModel model, BizLogSubModel bizLogSubModel, String logCode) {
			this(model, bizLogSubModel, logCode, new Date());
		}
		
		public LogBuilder setInfoFromHttpRequest(HttpServletRequest request,HttpServletResponse response) {
			this.userClientIp = IPUtil.getClientIpAddr(request);
			this.url = request.getRequestURL().toString();
			this.host = WebUtils.getHost(url);
			
			Integer adminId = (Integer)request.getSession().getAttribute(GlobalNames.SESSIONKEY_ADMIN_ID);
			if (adminId == null) {
                this.userId = UserLoginUtil.getLoginUid(request, response);
                this.userName = UserLoginUtil.getNickName(request,response);
			} else {
				this.userId = adminId;
				this.userName = (String)request.getSession().getAttribute(GlobalNames.SESSIONKEY_ADMIN_NAME);
			}
			
			return this;
		}

		/**
		 * 用户信息，三个参数中必传一个
		 * 
		 * @param userId
		 * @param userName
		 * @param uc
		 */
		public LogBuilder setUser(int userId, String userName, String uc) {
			this.userId = userId;
			this.userName = userName;
			this.uc = uc;
			return this;
		}
		
		/**
		 * 用户信息，三个参数中必传一个
		 * 
		 * @param userId
		 * @param userName
		 * @param uc
		 */
		public LogBuilder setUserUc(String uc) {
			this.uc = uc;
			return this;
		}

		/**
		 * 传入操作的业务对象的信息
		 * 
		 * @param obj
		 * @param objId
		 */
		public LogBuilder setObj(BizLogObj obj, String objId) {
			this.obj = obj.getBizLogObjName();
			this.objId = objId;
			return this;
		}

		/**
		 * 传入的业务操作对象的信息
		 * 
		 * @param op
		 */
		public LogBuilder setOp(BizLogOp op) {
			this.op = op.getBizLogOpName();
			return this;
		}

		
		/** 选传参数 */
		public LogBuilder setBlkFlag() {
			this.flag = -1;
			return this;
		}
		
		public LogBuilder setFlag() {
			this.flag = 1;
			return this;
		}

		public LogBuilder setHost(String host) {
			this.host = host;
			return this;
		}

		public LogBuilder setUrl(String url) {
			this.url = url;
			return this;
		}
		
		public LogBuilder setUserClientIp(String userClientIp) {
			this.userClientIp = userClientIp;
			return this;
		}

		public LogBuilder setExtraData(String extraData) {
			this.extraData = extraData;
			return this;
		}

		public LogBuilder setDescp(String descp) {
			this.descp = descp;
			return this;
		}

		/**
		 * @author leo
		 * 
		 * build BizLog 对象
		 */
		public BizLog build() {
			return new BizLog(this);
		}

	}

	private BizLog(LogBuilder lBuilder) {
		
		this.model = lBuilder.model;

		this.subModel = lBuilder.subModel;

		this.logCode = lBuilder.logCode;

		this.flag = lBuilder.flag;

		this.host = lBuilder.host;

		this.url = lBuilder.url;

		this.userClientIp = lBuilder.userClientIp;

		this.svrIp = lBuilder.svrIp;

		this.time = lBuilder.time;

		this.userId = lBuilder.userId;

		this.userName = lBuilder.userName;

		this.uc = lBuilder.uc;

		this.obj = lBuilder.obj;

		this.objId = lBuilder.objId;

		this.op = lBuilder.op;

		this.extraData = lBuilder.extraData;

		this.descp = lBuilder.descp;
	}

	/**
	 * 日志格式 (non-Javadoc)
	 * 
	 * 1.model,subModel,logCode,flag;
	 * 2.userId,userName,uniqC,obj,objId,op;
	 * 3.host,url,userClientIp,svrIp,time
	 * 4.extraData,descp
	 * 
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(strf(model)).append(LOG_SPLIT);
		sb.append(strf(subModel)).append(LOG_SPLIT);
		sb.append(strf(logCode)).append(LOG_SPLIT);
		sb.append(numberf(flag)).append(LOG_SPLIT);
		
		sb.append(numberf(userId)).append(LOG_SPLIT);
		sb.append(strf(userName)).append(LOG_SPLIT);
		sb.append(strf(uc)).append(LOG_SPLIT);
		sb.append(strf(obj)).append(LOG_SPLIT);
		sb.append(strf(objId)).append(LOG_SPLIT);
		sb.append(strf(op)).append(LOG_SPLIT);
		
		sb.append(strf(host)).append(LOG_SPLIT);
		sb.append(strf(url)).append(LOG_SPLIT);
		sb.append(strf(userClientIp)).append(LOG_SPLIT);
		sb.append(strf(svrIp)).append(LOG_SPLIT);
		sb.append(timef(time)).append(LOG_SPLIT);
		
		sb.append(strf(extraData)).append(LOG_SPLIT);
		sb.append(strf(descp));
		return sb.toString();
	}

	/**
	 * json格式，准备网络发送
	 * 
	 * @return
	 */
	public String toJson() {
		return new Gson().toJson(this);
	}

	/**
	 * from jsonstr to get bizlog
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static BizLog fromJson(String jsonStr) {
		return new Gson().fromJson(jsonStr, BizLog.class);
	}

	/**
	 * log之前先进行数据校验, 其中必填项： 基本项 model, subModel,
	 *         logCode; 用户项 userName, uniqC, userId [userId&userName || uniqC] 操作项 obj, objId, op
	 * 
	 * @throws BizLogFormatException
	 *             BizLog.Builder的必传参数没有传入
	 *             
	 */
	public static void log(BizLog bizlog) {
		try {
			verifyLogFormat(bizlog);
		} catch (BizLogFormatException e) {
			LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME).error("verifyLogFormat err.",e);
			return;
		}
		LOG.info(bizlog.toString());
	}
	
	public static void verifyLogFormat(BizLog bizlog) throws BizLogFormatException {
		if (StringUtils.isBlank(bizlog.model) || StringUtils.isBlank(bizlog.subModel)
				|| StringUtils.isBlank(bizlog.logCode)) {
			throw new BizLogFormatException(
					"BASE-LOG-BizLog.LogBuilder.build",
					"check log info : [model, subModel, logCode]",
					new IllegalArgumentException("the [model, subModel, logCode] info should not be null."));
		}
		if (StringUtils.isBlank(bizlog.userName) && StringUtils.isBlank(bizlog.uc)
				&& bizlog.userId == 0) {
			throw new BizLogFormatException(
					"BASE-LOG-BizLog.LogBuilder.build",
					"check log info : [userName, uniqC, userId] in model["
							+ bizlog.model + "] ,  localcode[" + bizlog.logCode + "]",
					new IllegalArgumentException("the [userName, userId,  uc] info should at least have one value."));
		}
		if ((!StringUtils.isBlank(bizlog.userName) || bizlog.userId != 0)) {
			if (StringUtils.isBlank(bizlog.userName) || bizlog.userId == 0) {
				throw new BizLogFormatException(
						"BASE-LOG-BizLog.LogBuilder.build",
						"check log info : [userName, userId] in model[" + bizlog.model + "] localcode[" + bizlog.logCode + "]",
						new IllegalArgumentException("the [userName,  userId] should both has value || both has no value."));
			}
		}
		if (StringUtils.isBlank(bizlog.obj) || StringUtils.isBlank(bizlog.objId)
				|| StringUtils.isBlank(bizlog.op)) {
			throw new BizLogFormatException(
					"BASE-LOG-BizLog.LogBuilder.build",
					"check log info : [obj, objId, op] in model["+ bizlog.model + "] localcode[" + bizlog.logCode + "]", 
					new IllegalArgumentException("the [obj,  objId, op] should both has value "));
		}
	}
	
	private static enum BizLogObjBlk implements BizLogObj{
		
		HTML_CONTENT;
		
		@Override
		public String getBizLogObjName() {
			return this.name();
		}
		
	}
	
	private static enum BizLogSubModelBlk implements BizLogSubModel{
		
		CMS;
		
		@Override
		public String getSubModelName() {
			return this.name();
		}
		
	}

	public static void main(String[] args) {
		 System.out.println(strf("|\""));
		 System.out.println(strf(""));
		 
		 System.out.println(numberf(0));
		 System.out.println(timef(new Date()));
		 
		
		 String s = "sdf\"sdfsd";
		 System.out.println(s.replaceAll("\"", "\\\\\""));
		 
		 Map<String, String> map = new HashMap<String, String>();
		 map.put("content", "");
		 
		 HttpServletRequest request  = null;
		 BizLog bizLog = new LogBuilder(BizLogModel.SP, BizLogSubModelBlk.CMS, "SP-CMS-HTML_CONTENT-ADD")
		 			// new BizLog.LogBuilder(BizLogModel.SP, BizLogSubModelBlk.CMS, "SP-CMS-HTML_CONTENT-ADD", new Date())
		 			.setInfoFromHttpRequest(request,null)
		 			.setBlkFlag()
			 		.setObj(BizLogObjBlk.HTML_CONTENT, "1213213")
			 		.setOp(BizLogOp.ADD)
			 		
			 		//
			 		//.setExtraData(extraData);
			 		.build();
		 
		MsgSender.bizLog(bizLog);
		 
	}
}
