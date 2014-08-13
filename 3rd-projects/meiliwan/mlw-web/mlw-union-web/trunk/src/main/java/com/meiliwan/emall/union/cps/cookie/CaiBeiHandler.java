package com.meiliwan.emall.union.cps.cookie;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;

import com.meiliwan.emall.commons.bean.WebConstant;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.commons.util.EncryptTools;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.mms.bean.UserForeign;
import com.meiliwan.emall.mms.bean.UserPassport;
import com.meiliwan.emall.mms.client.UserForeignClient;
import com.meiliwan.emall.mms.client.UserPassportClient;
import com.meiliwan.emall.union.bean.UnionConstant;
import com.meiliwan.emall.union.cps.CPSHandler;
import com.meiliwan.emall.union.util.LoginHelper;
import com.meiliwan.emall.union.util.UserCommonHelper;

public class CaiBeiHandler extends CPSHandler {
	private final static MLWLogger LOGGER = MLWLoggerFactory
			.getLogger(CaiBeiHandler.class);

	/**
	 * 很据彩贝 md5加密规则，生成加密字符串
	 * 
	 * @param Acct
	 * @param OpenId
	 * @param LoginFrom
	 * @param ClubInfo
	 * @param ViewInfo
	 * @param Url
	 * @param Ts
	 * @param Attach
	 * @return
	 */
	private String getEncryMd5(String Acct, String OpenId, String LoginFrom,
			String ClubInfo, String ViewInfo, String Url, String Ts,
			String Attach) {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(Acct).append(Attach).append(ClubInfo)
				.append(LoginFrom);
		stringBuilder.append(OpenId).append(Ts).append(Url).append(ViewInfo);
		String KEY_1 = "caibei123test1";
		String KEY_2 = "caibei123test2";
		try {
			KEY_1 = ConfigOnZk.getInstance().getValue(ZK_CONFIG_PATH,
					"CaiBei.key_1", KEY_1);
			KEY_2 = ConfigOnZk.getInstance().getValue(ZK_CONFIG_PATH,
					"CaiBei.key_2", KEY_2);
		} catch (BaseException e) {
			LOGGER.error(e, "get config on zk error", "");
		}
		String md5String = EncryptTools.EncryptByMD5(EncryptTools
				.EncryptByMD5(stringBuilder.toString() + KEY_1) + KEY_2);
		return md5String;
	}

	/**
	 * 实施彩贝验证规则
	 * 
	 * @param request
	 * @return
	 */
	private boolean validation(HttpServletRequest request) {
		String Acct = ServletRequestUtils.getStringParameter(request, "Acct",
				"");
		String OpenId = ServletRequestUtils.getStringParameter(request,
				"OpenId", "");
		String LoginFrom = ServletRequestUtils.getStringParameter(request,
				"LoginFrom", "");
		String ClubInfo = ServletRequestUtils.getStringParameter(request,
				"ClubInfo", "");
		String ViewInfo = ServletRequestUtils.getStringParameter(request,
				"ViewInfo", "");
		String Url = ServletRequestUtils.getStringParameter(request, "Url", "");
		String Ts = ServletRequestUtils.getStringParameter(request, "Ts", "");
		String Vkey = ServletRequestUtils.getStringParameter(request, "Vkey",
				"");
		String Attach = ServletRequestUtils.getStringParameter(request,
				"Attach", "");

		if (StringUtils.isBlank(Acct) || StringUtils.isBlank(OpenId)
				|| StringUtils.isBlank(LoginFrom)
				|| StringUtils.isBlank(ClubInfo)
				|| StringUtils.isBlank(ViewInfo) || StringUtils.isBlank(Ts)) {
			return false;
		}

		String md5V = getEncryMd5(Acct, OpenId, LoginFrom, ClubInfo, ViewInfo,
				Url, Ts, Attach);
		if (!md5V.equalsIgnoreCase(Vkey)) {
			return false;
		}

		return true;
	}

	/**
	 * 为联合登录用户创建美丽湾用户，并返回passport信息
	 * 
	 * @param fuser
	 * @param _nickName
	 * @return
	 */
	private UserPassport getAndSaveUserPassport(UserForeign fuser,
			String _nickName) {
		String userName = UserCommonHelper.grantUserId();
		String nickName = userName;
		if (!StringUtils.isBlank(_nickName)) {
			nickName = _nickName;
		}

		UserPassport passport = new UserPassport();
		passport.setUserName(userName);
		passport.setNickName(nickName);
		passport.setHeadUri(WebConstant.DEFAULT_HEAD_URI);
		passport.setCreateTime(new Date());

		UserPassportClient.save(passport);
		LOGGER.info("create userpassport for caibei qq user", passport, "");
		return UserPassportClient.getPassportByUserName(userName);
	}

	/**
	 * 生成UserForeign用户
	 * 
	 * @param request
	 * @return
	 */
	private UserForeign getUserForeign(HttpServletRequest request) {
		UserForeign userForeign = new UserForeign();

		userForeign.setSource("qq");
		userForeign.setState((short) 1);
		userForeign.setUnionType("CaiBei");
		userForeign.setUnionLoginTime(new Date());
		userForeign.setForeignUid(ServletRequestUtils.getStringParameter(
				request, "Acct", ""));

		return userForeign;
	}

	/**
	 * 从编码的信息中取出跳转用户的nickname
	 * 
	 * @param request
	 * @return
	 */
	private String getNcikName(HttpServletRequest request) {
		String viewInfo = "";

		try {
			viewInfo = URLDecoder.decode(ServletRequestUtils
					.getStringParameter(request, "ViewInfo", ""), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e, "decode viewInfo error",
					WebUtils.getClientIp(request));
		}

		if (StringUtils.isBlank(viewInfo)) {
			return "";
		}

		String[] keyValue = viewInfo.split("&NickName");
		for (String string : keyValue) {
			if (string.contains("ShowMsg")) {
				String[] parts = string.split("=");
				if(parts.length > 1){
					parts = string.split("=")[1].split("，");
					return parts.length == 2 ? parts[1] : "";
				}
			}
		}
		return "";
	}

	/**
	 * 创建美丽湾用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	private boolean createUser(HttpServletRequest request,
			HttpServletResponse response) {

		UserForeign userForeign = getUserForeign(request);
		if (StringUtils.isBlank(userForeign.getForeignUid())) {
			return false;
		}

		String nickName = getNcikName(request);
		UserPassport userPassport = getAndSaveUserPassport(userForeign,
				nickName);
		if (userPassport == null || userPassport.getUid().intValue() < 0) {
			return false;
		}

		userForeign.setUid(userPassport.getUid());
		LOGGER.info("create foreign user ", userPassport,
				WebUtils.getClientIp(request));
		UserForeignClient.save(userForeign);

		return setLoginStatus(request, response, userPassport);
	}

	/**
	 * 设置用户登录状态
	 * 
	 * @param request
	 * @param response
	 * @param userPassport
	 *            用户信息
	 * @return
	 */
	private boolean setLoginStatus(HttpServletRequest request,
			HttpServletResponse response, UserPassport userPassport) {
		request.setAttribute("loginName",
				ServletRequestUtils.getStringParameter(request, "Acct", ""));

		String result = LoginHelper.loginAfter(request, response, userPassport);
		if (StringUtils.isBlank(result)) {
			return false;
		}
		return true;
	}

	/**
	 * 解析表单viewInfo字符串数据，返回处理后map集合字符串
	 * 
	 * @param viewInfo
	 * @return
	 */
	private Map<String, String> getViewInfo(String viewInfo) {
		if (StringUtils.isBlank(viewInfo)) {
			return null;
		}

		Map<String, String> viewMap = new HashMap<String, String>();
		String[] keyValue = viewInfo.split("&");
		for (String string : keyValue) {
			String[] kv = string.split("=");
			viewMap.put(kv[0], kv[1]);
		}

		return viewMap;
	}

	/**
	 * 生成cookie
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	private Cookie getCookie(String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setPath("/");
		cookie.setDomain(".meiliwan.com");
		return cookie;
	}

	/**
	 * 生成除_mcps之外的cookie
	 * 
	 * @param request
	 * @param response
	 */
	private void setOtherCookie(HttpServletRequest request,
			HttpServletResponse response) {
		String viewInfo = ServletRequestUtils.getStringParameter(request,
				"ViewInfo", "");
		Map<String, String> viewMap = getViewInfo(viewInfo);
		if (viewMap == null) {
			return;
		}

		String show_msg = viewMap.get("ShowMsg");
		String attach = ServletRequestUtils.getStringParameter(request,
				"Attach", "");
		String cpPoints = viewMap.get("CBPoints");
		String cbBonus = viewMap.get("CBBonus");
		String headShow = viewMap.get("HeadShow");
		String jifenUrl = viewMap.get("JifenUrl");
		String openKey = viewMap.get("OpenKey");
		String nickName = viewMap.get("NickName");
		if (StringUtils.isBlank(nickName)) {
			show_msg = nickName;
		}

		Cookie cbCookie = new Cookie("cb_user", show_msg);
		cbCookie.setPath("/");
		cbCookie.setDomain(".meiliwan.com");
		cbCookie.setComment("identy the user from caibei");

		response.addCookie(cbCookie);
		response.addCookie(getCookie("QQ_showmsg", show_msg));
		response.addCookie(getCookie("QQ_attach", attach));
		response.addCookie(getCookie("QQ_point", cpPoints));
		response.addCookie(getCookie("QQ_bonus", cbBonus));
		response.addCookie(getCookie("QQ_headShow", headShow));
		response.addCookie(getCookie("QQ_jifenUrl", jifenUrl));
		response.addCookie(getCookie("QQ_openKey", openKey));
	}

	@Override
	public boolean needUpdateCookie(HttpServletRequest request, String _mcps,
			String currMcps) {
		return super.needUpdateCookie(request, _mcps, currMcps)
				&& validation(request);
	}

	@Override
	public String getTargetUrl(HttpServletRequest request) {
		return request.getParameter("Url");
	}

	@Override
	public String getMcps(HttpServletRequest request) {
		String acct = ServletRequestUtils.getStringParameter(request, "Acct",
				"");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("A100136514").append(acct);
		return stringBuilder.toString();
	}

	@Override
	public String getCPSType() {
		return UnionConstant.CAIBEI;
	}

	@Override
	public void doOther(HttpServletRequest request, HttpServletResponse response) {
		if (validation(request)) {
			String Acct = ServletRequestUtils.getStringParameter(request,
					"Acct", "");

			boolean loginStatus = false;
			UserForeign userForeign = UserForeignClient.getForeignByFid(Acct,
					"qq");
			if (userForeign != null) {
				UserPassport userPassport = UserPassportClient
						.getPassportByUid(userForeign.getId());
				userForeign.setUnionLoginTime(new Date());
				if (StringUtils.isBlank(userForeign.getUnionType())) {
					userForeign.setUnionType(UnionConstant.CAIBEI);
				}
				UserForeignClient.update(userForeign);
				loginStatus = setLoginStatus(request, response, userPassport);
			} else {
				loginStatus = createUser(request, response);
			}

			if (loginStatus) {
				setOtherCookie(request, response);
			}
		}
	}
}
