package com.meiliwan.emall.base.tag;

import com.meiliwan.emall.base.client.CmsFragmentServiceClient;
import com.meiliwan.emall.base.constant.FragmentName;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;

import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-9-28
 * Time: 上午11:22
 */
public class FragmentBannerTag  extends TagSupport{

    /**
	 * 
	 */
	private static final long serialVersionUID = 602142274889400317L;

	private static final MLWLogger logger = MLWLoggerFactory.getLogger(FragmentBannerTag.class);

    private  String incName ;
    
    private  String incName2 ;

    public String getIncName2() {
		return incName2;
	}

	public void setIncName2(String incName2) {
		this.incName2 = incName2;
	}

	public String getIncName() {
        return incName;
    }

    public void setIncName(String incName) {
        this.incName = incName;
    }

    @Override
    public int doStartTag() throws JspException {
        FragmentName fragmentName = FragmentName.valueOf(incName);
        String fragmentContent = getRedis(fragmentName);
        if(StringUtils.isBlank(fragmentContent)){
	        	FragmentName fragmentName2 = FragmentName.valueOf(incName2);
	        	fragmentContent = getRedis(fragmentName2);
        }
        if(fragmentContent != null){
            try {
                this.pageContext.getOut().write(fragmentContent);
            } catch (IOException e) {
                logger.error(e,"[碎片写入页面异常]"+fragmentName,"");
            }
        }
        return 1;
    }

	private String getRedis(FragmentName fragmentName) {
		String fragmentContent=null;
		try {
            fragmentContent = ShardJedisTool.getInstance().hget(JedisKey.global$inc,fragmentName.getFirstCode(),fragmentName.getSecondCode());
        } catch (JedisClientException e) {
            logger.error(e,"[redis 读取异常] "+fragmentName,"");
        }

        if(StringUtils.isEmpty(fragmentContent)){
            //加补偿机制
            try{
                fragmentContent = CmsFragmentServiceClient.getCmsFragmentByPageId(fragmentName.getId());
                if(fragmentContent != null) ShardJedisTool.getInstance().hset(JedisKey.global$inc,fragmentName.getFirstCode(),fragmentName.getSecondCode(),fragmentContent);
            }catch (JedisClientException redisE) {
                logger.error(redisE,"[标签解析 redis 写入异常] "+fragmentName,"");
            }catch (Exception iceE){
                logger.error(iceE,"[标签解析从数据库读取异常]"+fragmentName,"");
            }
        }
		return fragmentContent;
	}
}
