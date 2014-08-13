package com.meiliwan.emall.async.pms;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter.MsgTaskWorker;
import com.meiliwan.emall.commons.util.StringUtil;

/**
 * 商品 Date: 13-7-5 Time: 下午4:51
 */
public class ProductTaskWorker implements MsgTaskWorker {

	public static final MLWLogger log = MLWLoggerFactory
			.getLogger(ProductTaskWorker.class);

	/**
	 * 批量处理
	 * 
	 * @param msg
	 *            =11,22,33
	 */
	@Override
    public void handleMsg(String msg) {
        try {
            if(StringUtil.checkNull(msg)){
            	return ;
            }
            String[] ids = msg.split(",");
            for (String id : ids) {
			//更新
            }
            	
        }catch (Exception e) {
        		log.error(e,msg,null);
		}
    }
}
