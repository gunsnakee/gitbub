package com.meiliwan.emall.commons.util;

import com.meiliwan.emall.commons.constant.GlobalNames;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.web.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>根据浏览器cookieId检查其是否在线，回话保存在redis持续30分钟</p>
 * <p></p>
 * @author lgn-mop
 *
 */
public class CookieSessionUtil {
    //
	public final static String sessionField = "sessionId";
	private final static String userField = "userId";
	private final static String NICKNAME = "nickName";
	private final static boolean registerWhenCheck = true;

	/**
	 * set操作会让会话顺延指定时长
	 * @author lgn-mop
	 *
	 */
	public static class Session{
		private boolean isOnline;
		private String sessionId;
		private String cookieId;

		/**
		 * 拿到session那个时候的在线情况。
		 * @return
		 * @throws JedisClientException
		 */
		public boolean isOnline() throws JedisClientException {
			return isOnline;
		}

		public String getCookieId() {
			return cookieId;
		}


		public Session(String cookieId, String sessionId, boolean isOnline){
			this.cookieId = cookieId;
			this.sessionId = sessionId;
			this.isOnline = isOnline;
		}

		/**
		 * 如果你拿到的这个session对象长时间
		 * @return
		 * @throws JedisClientException
		 */
		public String getSessionId() throws JedisClientException{
			String sid = ShardJedisTool.getInstance().hget(JedisKey.session, cookieId, sessionField);

			if (sid == null){
				if (registerWhenCheck){
					String newSessionId = createSessionId(cookieId);
					ShardJedisTool.getInstance().hset(JedisKey.session, cookieId, sessionField, newSessionId);
					return newSessionId;
				}else{
					return sessionId;
				}
			}else{
				ShardJedisTool.getInstance().expire(JedisKey.session, this.cookieId, GlobalNames.SESSION_EXPIRE_SECONDS);
				return sid;
			}
		}



		public String getAttribute(String key) throws JedisClientException{
			ShardJedisTool.getInstance().expire(JedisKey.session, cookieId, GlobalNames.SESSION_EXPIRE_SECONDS);
			return ShardJedisTool.getInstance().hget(JedisKey.session, cookieId, key);

		}

        public List<String> getAttribute(String... keys) throws JedisClientException{
            ShardJedisTool.getInstance().expire(JedisKey.session, cookieId, GlobalNames.SESSION_EXPIRE_SECONDS);
            return ShardJedisTool.getInstance().hmget(JedisKey.session, cookieId,keys);

        }


		public void setAttribute(String key, Object value) throws JedisClientException{

			ShardJedisTool.getInstance().hset(JedisKey.session, cookieId, key, value);
		}

        public void msetAttribute(Map<String,Object> dataMap) throws JedisClientException{
            ShardJedisTool.getInstance().hmset(JedisKey.session, cookieId, dataMap);
        }

		public boolean removeAttribute(String... keys) throws JedisClientException{
			return ShardJedisTool.getInstance().hdel(JedisKey.session, cookieId, keys);
		}


		public void setUid(Object uid) throws JedisClientException{
			setAttribute(userField, uid.toString());
		}

        public void setUidAndNickname(Object uid,String nickName) throws JedisClientException{
            Map<String,Object> dataMap = new HashMap<String, Object>();
            dataMap.put(userField,uid.toString());
            dataMap.put(NICKNAME,nickName);
            msetAttribute(dataMap);
        }

		/**
		 * 没有返回 Integer.MIN_VALUE
		 * @return
		 * @throws JedisClientException
		 */
		public int getUid() throws JedisClientException{
			String uid = getAttribute(userField);
			if (uid != null)
				try{
					int uidInt = Integer.parseInt(uid);
					return uidInt;
				}catch (NumberFormatException e) {
					return GlobalNames.UID_NULL;
				}else{
					return GlobalNames.UID_NULL;
				}

		}
		
		/**
		 * 
		 * @param nickName
		 * @throws JedisClientException
		 */
		public void setNickName(String nickName) throws JedisClientException{
			setAttribute(NICKNAME, nickName);
		}
		
		/**
		 * 
		 * @return
		 * @throws JedisClientException
		 */
		public String getNickName() throws JedisClientException{
			String nickName = getAttribute(NICKNAME);
			if (!StringUtil.checkNull(nickName)){
				return nickName;
			}
			return "";
		}

        public void removeUid() throws JedisClientException{
			removeAttribute(userField);
		}

        public void removeUidAndNickname() throws JedisClientException{
            removeAttribute(userField,NICKNAME);
        }


    }

	/**
	 * 默认检查时候也会创建一个新会话
	 * @param cookieId
	 * @return
	 * @throws JedisClientException
	 */
	public static Session getSession(String cookieId) throws JedisClientException{

		String sid = ShardJedisTool.getInstance().hget(JedisKey.session, cookieId, sessionField);

		if (sid == null){
			if (registerWhenCheck){
				String newSessionId = createSessionId(cookieId);
				ShardJedisTool.getInstance().hset(JedisKey.session, cookieId, sessionField, newSessionId);
				return new Session(cookieId, newSessionId, false);
			}else{
				return new Session(cookieId, null, false);
			}
		}
			
		return new Session(cookieId, sid, true);
	}


	private static String createSessionId(String cookieId){
		String code = MD5Util.toMD5(cookieId) + System.currentTimeMillis() + "A" + RandomCode.randomNumCode(4);
		return code;
	}

    private static String createCookieId(){
        return "mlw-" + System.currentTimeMillis() + RandomCode.randomNumCode(6);
    }

	/**
	 * 根据request获取cookieID后，得到session。如果是新游客，会创建cookieID写回response
	 * @param request 
	 * @param response 如果不想写回cookieid,设置为null
	 * @return
	 * @throws JedisClientException
	 */
	public static Session getSession(HttpServletRequest request, HttpServletResponse response) throws JedisClientException{
		String cookieId = WebUtils.getCookieValue(request, WebUtils.COOKIE_ID_KEY);
		if (cookieId == null){ //never visited
			String newCookieId = createCookieId();
			if(response != null){
                //WebUtils.setCookieValue(response, WebUtils.COOKIE_ID_KEY, newCookieId, null, GlobalNames.MLW_DOMAIN, 86400 * 365 * 2);
                WebUtils.setCookieValue(response, WebUtils.COOKIE_ID_KEY, newCookieId, null, GlobalNames.MLW_DOMAIN, -1);
            }
			return getSession(newCookieId);
		}else{//不管cookie过期 //TODO
			return getSession(cookieId);
		}
	}

    /**
     * 使用场景： 目前仅登录成功后使用
     * 强制创建一个cookieID写回response。
     *
     * @param request
     * @param response
     * @return
     * @throws JedisClientException
     */
    public static Session newSession(HttpServletRequest request, HttpServletResponse response) throws JedisClientException{
        String newCookieId = createCookieId();
        if(response != null){
            WebUtils.setCookieValue(response, WebUtils.COOKIE_ID_KEY, newCookieId, null, GlobalNames.MLW_DOMAIN, -1);
        }
        return getSession(newCookieId);
    }


}
