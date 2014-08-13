package com.meiliwan.emall.service.impl;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import Ice.Current;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.exception.BaseRuntimeException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.rabbitmq.MqModel;
import com.meiliwan.emall.commons.rabbitmq.MsgSender;
import com.meiliwan.emall.commons.util.BaseConfig;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.icetool.IceFileTool;
import com.meiliwan.emall.pojo.IceConstant;
import com.meiliwan.emall.pojo.MethodInfo;
import com.meiliwan.emall.service.BackgroundService;
import com.meiliwan.emall.service.BaseService;
import com.meiliwan.emall.service.SecurityInterceptor;
import com.meiliwan.emall.service.util.IceConnParser;
import com.meiliwan.interf._CommonServiceIceDisp;

/**
 * 
 * @author lsf
 * @Description 通用的ice通信管理器
 * 
 */
public class CommonServiceIceImpl extends _CommonServiceIceDisp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7398400906339071639L;

//	private final static Logger LOGGER = LoggerFactory
//			.getLogger(CommonServiceIceImpl.class);
	private static MLWLogger LOGGER = MLWLoggerFactory.getLogger(CommonServiceIceImpl.class);
	private final static String DEFAULT_SECURITY = "defaultSecurityInterp";

	private static Map<String, BaseService> serviceMap = new HashMap<String, BaseService>(
			50);
	private static Map<String, SecurityInterceptor> securityInterpMap = new HashMap<String, SecurityInterceptor>();
	private static Map<String, MethodInfo> methodMap = new HashMap<String, MethodInfo>(
			200);

	static {
		initServiceMap();

		initMethods();
	}

	@Override
	@SuppressWarnings(value = { "rawtypes" })
	public String handleMsg(String msg, Current __current) {
		long startMillis = System.currentTimeMillis();
		String uuid = UUID.randomUUID().toString();
		
		JsonObject resultObj = new JsonObject();
		resultObj.addProperty(BaseService.CODE,
				BaseService.DEFAULT_SUCCESS_CODE); // 默认消息处理成功
		
//		String ipInfo = IPUtil.parseIceConIP(__current != null ? __current.con.toString() : "unkown");
		String[] ipArr = IceConnParser.parseIPs(__current);
		String clientIP = ipArr[0];
		String serverIP = ipArr[1];
		resultObj.addProperty(BaseService.CLIENT_IP_STR, clientIP);
		resultObj.addProperty(BaseService.SERVER_IP_STR, serverIP);
		
		String ipInfo = "clientIP:" + clientIP + ",serverIP:" + serverIP;
		LOGGER.debug("received msg(" + msg + "),ip("
				+ ipInfo + "), msgId("
				+ uuid + ")");

		

		String action = null;
		boolean delAfterHandled = false;
		boolean hasFiles = false;
		File[] files = new File[0];
		try {
			if(msg.contains(IceConstant.FILE_TRANS_PROTOCOL)){
				hasFiles = true;
				String[] msgParts = msg.split(IceConstant.FILE_TRANS_PROTOCOL);
				msg = msgParts[0];
				String fileStr = msgParts[1];
			    delAfterHandled = Boolean.valueOf(msgParts[2]);
				
				String[] remoteFilePaths = fileStr.split(IceConstant.FILE_SPLITER);
				
				files = IceFileTool.downloadFile(delAfterHandled, remoteFilePaths);
			}
			
			JsonObject msgObj = (JsonObject) new JsonParser().parse(msg);
			JsonElement actionElem = msgObj.get("action");
			if (actionElem == null) {
				resultObj.addProperty(BaseService.CODE,
						BaseService.DEFAULT_FAIL_CODE);
				resultObj.addProperty(BaseService.FAIL_MESSAGE_NAME,
						"action cannot be null.");
			}

			action = actionElem.getAsString();
			MethodInfo methodInfo = methodMap.get(action);

			if (methodInfo != null) {
				JsonObject paramsObj = msgObj.getAsJsonObject("params");
				JsonArray jsonArr = null;
				if (paramsObj != null) {
					jsonArr = paramsObj.getAsJsonArray("elements");
				}
				// 先进行安全检测，是否允许该客户端beanName访问这个处理器
				SecurityInterceptor secInterp = securityInterpMap.get(action) == null ? securityInterpMap
						.get(DEFAULT_SECURITY) : securityInterpMap.get(action);
				if (secInterp.allowAccess(jsonArr, __current)) {
					Method method = methodInfo.getMethod();
					Class[] paramClzs = methodInfo.getParamClzs();
					Type[] paramTypes = methodInfo.getParamTypes();

					String beanName = action.split("/")[0];
					if (paramTypes == null || paramTypes.length <= 0) {
						method.invoke(serviceMap.get(beanName));
					} else {
						List<Object> values = new ArrayList<Object>(paramTypes.length);
						values.add(resultObj); // 将获取结果的json对象添加到参数的最开始位置
						if(hasFiles){
							values.add(files);
						}

						int khownParamNum = values.size();
						for (int index = khownParamNum; index < paramTypes.length; index++) {
							values.add(parseToObject(
									jsonArr.get(index - khownParamNum), paramTypes[index],
									paramClzs[index]));
						}

						method.invoke(serviceMap.get(beanName), values.toArray());
					}

					checkResult(resultObj, BaseService.DEFAULT_FAIL_MSG);
				} else {
					resultObj.addProperty(BaseService.CODE,
							BaseService.NOT_ALLOWED_CODE);
					resultObj.addProperty(BaseService.FAIL_MESSAGE_NAME,
							"not allowed");
				}
			} else {
				resultObj.addProperty(BaseService.CODE,
						BaseService.NOT_SUPPORTED_CODE);
				resultObj.addProperty(BaseService.FAIL_MESSAGE_NAME,
						"not supported action '" + action + "'");
			}
		} catch (Exception ex) {
			String errorMsg = ex.getMessage();
			String code = BaseService.DEFAULT_FAIL_CODE;
			if (ex instanceof InvocationTargetException) {
				Throwable ite = ((InvocationTargetException) ex)
						.getTargetException();
				if (ite instanceof BaseRuntimeException) {
					BaseRuntimeException bre = (BaseRuntimeException) ite;
					errorMsg = bre.getMessage();
					code = bre.getCode();
				} else if (ite instanceof BaseException) {
					BaseException be = (BaseException) ite;
					errorMsg = be.getMessage();
					code = be.getCode();
				} else {
					errorMsg = ite.getMessage();
				}
			}

			LOGGER.error(ex, "errorMsg:" + errorMsg + ", params:" + msg, clientIP);
//			LOGGER.error(
//					"code:iceintf-CommonServiceIceImpl-2, msg:" + errorMsg, ex);
			resultObj.addProperty(BaseService.CODE, code);
			checkResult(resultObj, errorMsg);
		}finally{
			//如果消息包含文件传输协议，并且参数指明在消息处理完后需要删除，则执行下边的文件删除逻辑
			if(delAfterHandled && hasFiles){
				for(File file : files){
					if(file != null){
						try{
						    file.delete();
						}catch(Exception e){
							LOGGER.error(e, "del file '" + file.getAbsolutePath() + " error.", clientIP);
						}
					}
				}
			}
		}

		String result = resultObj.toString();
		LOGGER.debug("msgId(" + uuid + "), result(" + result + ")");

		long endMillis = System.currentTimeMillis();
		String watchLog = "iceS(" + action + ")(" + startMillis + ")("
				+ endMillis + ")(" +ipInfo + ")";

		LOGGER.debug("watchlog:RTime:(" + (endMillis - startMillis) +")" + watchLog);
		MsgSender.getInstance(MqModel.WATCHLOG).send(MqModel.WATCHLOG,
				"logService", watchLog);

		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Object parseToObject(JsonElement value, Type type, Class clz)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		if ("java.lang.String".equals(clz.getName())) {
			return (value == null || "null".equals(value) || value instanceof JsonNull) ? null
					: value.getAsString();
		}

		// 如果为枚举类型
		if (clz.isEnum()) {
			Method method = clz.getMethod("valueOf",
					new Class[] { String.class });

			return value == null || value instanceof JsonNull ? null : method.invoke(null,
					value.getAsString());
		}

		return new Gson().fromJson(value, type);
	}

	/**
	 * 加载实现了BaseService接口的类，并实例化存入serviceMap
	 */
	private static void initServiceMap() {
		String iceConfig = BaseConfig.getValue("projectName")
				+ "/ice_config.xml";

		loadNormalBeans(iceConfig);

		loadSpringBeans(iceConfig);
	}

	private static void loadNormalBeans(String iceConfig) {
		int size;
		try {
			size = ConfigOnZk.getInstance().getElemSize(iceConfig,
					"handler-folder.folder");
			if (size > 0) {
				// 加载指定目录下实现了BaseService接口的类，并实例化存入serviceMap

			}
		} catch (BaseException ex) {
			LOGGER.error(ex, "loadNormalBeans from zkNode " + iceConfig, null);
//			LOGGER.error(
//					"code:iceintf-loadNormalBeans-2, msg:" + ex.getMessage(),
//					ex);
		}
	}

	private static void loadSpringBeans(String iceConfig) {
		try {
			int size = ConfigOnZk.getInstance().getElemSize(iceConfig,
					"springConfig.config");
			if (size > 0) {
				String[] configs = new String[size];
				for (int i = 0; i < size; i++) {
					configs[i] = ConfigOnZk.getInstance().getValue(iceConfig,
							"springConfig.config(" + i + ")");
				}
				ApplicationContext context = new ClassPathXmlApplicationContext(
						configs);

				String[] beanNames = context
						.getBeanNamesForType(BaseService.class);
				if (beanNames != null && beanNames.length > 0) {
					for (String beanName : beanNames) {
						BaseService service = (BaseService) context
								.getBean(beanName);
						if(service instanceof BackgroundService){
							BackgroundService bgService = (BackgroundService)service;
							bgService.bgRun();
						}else{
							serviceMap.put(beanName, service);
						}
					}
				}

				// 添加默认的安全策略
				securityInterpMap.put(DEFAULT_SECURITY,
						new DefaultSecurityInterceptor());

				// 从spring中添加指定的安全策略
				String[] securityInterps = context
						.getBeanNamesForType(SecurityInterceptor.class);
				if (securityInterps != null && securityInterps.length > 0) {
					for (String beanName : securityInterps) {
						SecurityInterceptor interp = (SecurityInterceptor) context
								.getBean(beanName);
						securityInterpMap.put(beanName, interp);
					}
				}
			}
		} catch (BaseException ex) {
			LOGGER.error(ex, "loadSpringBeans from zkNode " + iceConfig, null);
//			LOGGER.error(
//					"code:iceintf-loadSpringBeans-2,msg:" + ex.getMessage(), ex);
		}
	}

	private static void initMethods() {
		Set<String> beanSet = serviceMap.keySet();
		if (beanSet != null && beanSet.size() > 0) {
			for (String beanName : beanSet) { // 循环每个实现了BaseService接口的bean类
				BaseService service = serviceMap.get(beanName);
				Method[] methods = service.getClass().getDeclaredMethods();
				if (methods != null && methods.length > 0) {
					for (Method method : methods) { // 将方法的名字和方法相关的信息以键值对的形式存储到methodMap中，以便客户端调用
						// 只加载public方法作为接口方法,
						// 由于项目可能用到aspectj，它将会调用cglib对字节吗进行改写而增加一些以CGLIB开头的方法，所以需要将这些方法也去掉
						if (Modifier.isPublic(method.getModifiers())
								&& !method.getName().startsWith("CGLIB")) {
							Type[] paramTypes = method
									.getGenericParameterTypes();

							MethodInfo methodInfo = null;

							String methodName = method.getName();
							String methodKey = beanName + "/" + methodName;
							if (paramTypes != null && paramTypes.length > 0) {
								if (!"class com.google.gson.JsonObject"
										.equals(paramTypes[0].toString())) {
									LOGGER.debug("method '"
											+ methodKey
											+ "' should has a JSONObject parameter as the first parameter, this method will be ignored.");
									continue;
								}
								LOGGER.debug("method '" + methodKey + "' has "
										+ paramTypes.length + " parameters");
								methodInfo = new MethodInfo(method, paramTypes,
										method.getParameterTypes());
							} else {

								LOGGER.debug("method '"
										+ methodKey
										+ "' should has a JSONObject parameter as the first parameter, this method will be ignored.");

								continue;
//								methodInfo = new MethodInfo(method);
							}

							// 如果IceServiceMethod中指定了方法的名字，就优先使用改指定的名字；否则使用方法的名字作为方法信息的键
							methodMap.put(methodKey, methodInfo);

							LOGGER.debug("method(" + methodKey
									+ ") added success!");
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param resultObj
	 */
	private void checkResult(JsonObject resultObj, String errorMsg) {
		JsonElement codeElem = resultObj.get("code");
		String resultCode = codeElem == null || (codeElem instanceof JsonNull)  ? null : codeElem.getAsString();
		if (StringUtils.isBlank(resultCode)) {
			resultObj.addProperty(BaseService.CODE,
					BaseService.DEFAULT_FAIL_CODE);
			resultCode = BaseService.DEFAULT_FAIL_CODE;
		}

		JsonElement msgElem = resultObj.get(BaseService.FAIL_MESSAGE_NAME);
		if (!BaseService.DEFAULT_SUCCESS_CODE.equals(resultCode)
				&& (msgElem == null || StringUtils.isBlank(msgElem.toString()))) {
			resultObj.addProperty(BaseService.FAIL_MESSAGE_NAME, errorMsg); // 如果没有设置错误消息，则提供一个默认的错误消息
		}
	}
}
