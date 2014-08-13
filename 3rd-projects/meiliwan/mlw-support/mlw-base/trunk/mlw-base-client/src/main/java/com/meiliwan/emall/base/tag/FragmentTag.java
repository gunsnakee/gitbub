package com.meiliwan.emall.base.tag;

import com.meiliwan.emall.base.client.CmsFragmentServiceClient;
import com.meiliwan.emall.base.constant.FragmentName;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;


/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-9-25
 * Time: 下午3:32
 */
public class FragmentTag extends TagSupport {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7609663760673435787L;

	private static final MLWLogger logger = MLWLoggerFactory.getLogger(FragmentTag.class);

    private String incNameStr;

    public String getIncNameStr() {
        return incNameStr;
    }

    public void setIncNameStr(String incNameStr) {
        this.incNameStr = incNameStr;
    }

    @Override
    public int doStartTag() throws JspException {

            String[] incName = incNameStr.split(",");
            if( incName != null && incName.length > 0){
                for(int i = 0;i < incName.length; i++){
                    FragmentName fragmentName = FragmentName.valueOf(incName[i]);
                    String fragmentContent = null;
                    try{
                        fragmentContent  = ShardJedisTool.getInstance().hget(JedisKey.global$inc,fragmentName.getFirstCode(),fragmentName.getSecondCode());
                    }catch (JedisClientException e){
                        logger.error(e,"[碎片标签解析Redis异常]"+fragmentName, "");
                    }

                    //碎片为空，从数据库取
                    if(StringUtils.isEmpty(fragmentContent)){
                        fragmentContent = CmsFragmentServiceClient.getCmsFragmentByPageId(fragmentName.getId());
                        if(fragmentContent != null){
                            try {
                                ShardJedisTool.getInstance().hset(JedisKey.global$inc, fragmentName.getFirstCode(), fragmentName.getSecondCode(), fragmentContent);
                            } catch (JedisClientException e) {
                                logger.error(e,"[FragmentTag 碎片写入缓存异常 ]"+fragmentName,"");
                            }
                        }
                    }
                    if(fragmentContent != null)this.pageContext.setAttribute(incName[i],fragmentContent);
                }
            }
        return 1;
    }

}
