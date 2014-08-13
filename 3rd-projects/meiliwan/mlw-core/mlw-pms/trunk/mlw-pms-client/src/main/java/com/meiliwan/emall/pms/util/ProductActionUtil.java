package com.meiliwan.emall.pms.util;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;

public class ProductActionUtil {

	private final static MLWLogger logger = MLWLoggerFactory.getLogger(ProductActionUtil.class);


    /**
     * 用于保存用户对商品喜爱行为做一个记录，为了下次操作做一个判断
     */
    public static boolean setLoveAttribute(String userId, String proId, Object value) {
        try {
            ShardJedisTool.getInstance().hset(JedisKey.imeiliwan$proMyLove, userId, proId, value);
            return true;
        } catch (Exception e) {
            logger.error(new BaseException("地方官用户喜爱数-增加缓存记录失败", e), "userId:" + userId + ",proId:" + proId + ",valuer" + value, "");
            return false;
        }
    }

    /**
     * 判断用户是否对对应的商品喜爱已经操作
     */
    public static boolean exitsClickLove(String userId, String proId) {
        try {
            return ShardJedisTool.getInstance().hexists(JedisKey.imeiliwan$proMyLove, userId, proId);
        } catch (JedisClientException e) {
            logger.error(new BaseException("地方官用户喜爱数-检查用户是否点击过失败", e), "userId:" + userId + ",proId:" + proId, "");
            return false;
        }
    }

    /**
     * 用于保存用户对商品评论有用或无用功能做一个记录，为了下次操作做一个判断
     */
    public static boolean setCommentAttribute(String userId, String commentId, Object value) {
        try {
            ShardJedisTool.getInstance().hset(JedisKey.imeiliwan$commentUsed, userId, commentId, value);
            return true;
        } catch (Exception e) {
            logger.error(new BaseException("商品详情-评论有用和无用功能-增加缓存记录失败", e), "userId:" + userId + ",commentId:" + commentId + ",valuer" + value, "");
            return false;
        }
    }

    /**
     * 判断用户是否对商品评论的有用或者无用功能点击过
     */
    public static boolean exitsCommentUsed(String userId, String commentId) {
        try {
            return ShardJedisTool.getInstance().hexists(JedisKey.imeiliwan$commentUsed, userId, commentId);
        } catch (JedisClientException e) {
            logger.error(new BaseException("商品详情-评论有用和无用功能-检查用户是否点击过失败", e), "userId:" + userId + ",commentId:" + commentId, "");
            return false;
        }
    }
}
