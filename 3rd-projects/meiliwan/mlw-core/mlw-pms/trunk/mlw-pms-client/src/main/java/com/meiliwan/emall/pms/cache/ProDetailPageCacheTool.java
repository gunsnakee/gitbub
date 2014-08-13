package com.meiliwan.emall.pms.cache;

import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.pms.constant.ProInfoType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  商品详情页缓存
 *  （仅用用于商品详情业务缓存）
 * @author yiyou.luo
 *
 */
public class ProDetailPageCacheTool {
	private static final MLWLogger LOG = MLWLoggerFactory.getLogger(ProDetailPageCacheTool.class);

    /**
     * 获取商品详情页缓存
     * @param proId
     * @return
     */
	public static Map<String,String> getAllProDetail(Integer proId){
		Map<String,String> detailMap = new HashMap<String,String>();
		try {
			ShardJedisTool jedisTool = ShardJedisTool.getInstance();
            detailMap = jedisTool.hgetAll(JedisKey.pms$detail6,proId);
		} catch (JedisClientException e) {
			LOG.error(e, proId, "获取spu规格缓存");
		}
        return detailMap;
	}

    /**
     * 根据信息类型获取商品详情对应信息块缓存信息
     * @param proId
     * @return
     */
    public static String getProDetailByType(Integer proId,ProInfoType proInfoType){
        String detailFieldInfo = null;
        try {
            ShardJedisTool jedisTool = ShardJedisTool.getInstance();
            List<String> list = jedisTool.hmget(JedisKey.pms$detail6,proId,proInfoType.toString());
            if(list!=null && list.size()>0){
               detailFieldInfo = list.get(0);
            }
        } catch (JedisClientException e) {
            Map errorMap = new HashMap();
            errorMap.put("proId",proId);
            errorMap.put("proId",proId);
            LOG.error(e, errorMap, "获取spu规格缓存");
        }
        return detailFieldInfo;
    }

    /**
     * 获取spu规格缓存
     * @param spuId
     * @return
     */
    public static Map<String,String> getSpuStandard(Integer spuId){
        Map<String,String> standardMap = new HashMap<String,String>();
        try {
            ShardJedisTool jedisTool = ShardJedisTool.getInstance();
            standardMap = jedisTool.hgetAll(JedisKey.pms$standard,spuId);
        } catch (JedisClientException e) {
            LOG.error(e, standardMap,"获取spu规格缓存");
        }
        return standardMap;
    }

    /**
     * 根据信息块修改商品详情缓存
     * @param proId
     * @param proInfoType
     * @param object
     * @return
     */
    public static boolean updateProDetailByType(Integer proId,ProInfoType proInfoType,Object object){
        boolean isSuccess = false;
        try {
            ShardJedisTool jedisTool = ShardJedisTool.getInstance();
            jedisTool.hdel(JedisKey.pms$detail6, proId, proInfoType.toString());
            isSuccess = jedisTool.hset(JedisKey.pms$detail6, proId, proInfoType.toString(), object);
            isSuccess = true;
        } catch (JedisClientException e) {
            Map errorMap = new HashMap();
            errorMap.put("proId",proId) ;
            errorMap.put("proInfoType",proInfoType);
            errorMap.put("object",object);
            LOG.error(e, errorMap, "根据信息块修改商品详情缓存");
        }
        return  isSuccess;
    }

    /**
     * 添加or修改spu规格缓存
     * @param spuId
     * @param map
     * @return
     */
    public static boolean addOrUpdateSpuStandard(Integer spuId,Map map){
        boolean isSuccess = false;
        try {
            ShardJedisTool jedisTool = ShardJedisTool.getInstance();
            //先删除之前的再添加新的，防止部分field没有更新到最新情况
            jedisTool.del(JedisKey.pms$standard,spuId);
            isSuccess = jedisTool.hmset(JedisKey.pms$standard,spuId,map);
        } catch (JedisClientException e) {
            Map errorMap = new HashMap();
            errorMap.put("spuId",spuId) ;
            errorMap.put("object",map);
            LOG.error(e, errorMap, "添加or修改spu规格缓存");
        }
        return  isSuccess;
    }

    /**
     * 添加商品详情页缓存
     * @param proId
     * @param proInfo
     * @return
     */
    public static boolean addAllProDetail(Integer proId, Map<String,Object> proInfo){
        boolean isSuccess = false;
        try {
            ShardJedisTool jedisTool = ShardJedisTool.getInstance();
            //先删除之前的再添加新的，防止部分field没有更新到最新情况
            jedisTool.del(JedisKey.pms$detail6,proId);
            isSuccess = jedisTool.hmset(JedisKey.pms$detail6,proId, proInfo);
        } catch (JedisClientException e) {
            Map errorMap = new HashMap();
            errorMap.put("proId",proId) ;
            errorMap.put("proInfo",proInfo);
            LOG.error(e, errorMap, "添加商品详情页信息缓存报异常");
        }
        return  isSuccess;
    }

}
