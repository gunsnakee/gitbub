package com.meiliwan.emall.async.order;


import java.util.List;

import com.meiliwan.emall.base.constant.Constants;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter.MsgTaskWorker;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.oms.client.OrdClient;
import com.meiliwan.emall.oms.constant.OrdITotalStatus;
import com.meiliwan.emall.oms.dto.OrdDetailDTO;
import com.meiliwan.emall.pms.bean.ProComment;
import com.meiliwan.emall.pms.client.ProCommentClient;

/**
 * User: lzl
 * Date: 2014-05-23
 * Time: 1008
 * 定时超时自动好评
 */
public class OrderAppraiseTaskWorker implements MsgTaskWorker {

    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    @Override
    public void handleMsg(String msg) {
        try {
            if (msg != null) {
                OrdDetailDTO order = OrdClient.getDetail(msg);
                if (order != null && order.getOrd() != null && order.getOrd().getOrderStatus().equals(OrdITotalStatus.ORDI_FINISHED.getCode())) {
                	List<ProComment> commentList = ProCommentClient.getCommentListByOrder(msg);
                	if(commentList != null && commentList.size() > 0){
	                    for (ProComment comment : commentList) {
	                        //如果已经评价就不需要系统默认评价
	                    	if(comment != null && comment.getState().equals(Constants.COMMENT_IS_COMMENT_NO)){
//	                    		comment.setContent("系统默认好评.");
	                    		comment.setScore((short)5);
	                    		comment.setState(Constants.COMMENT_IS_COMMENT_YES);
	                    		comment.setCommentTime(DateUtil.getCurrentTimestamp());
	                    		
	                    		ProCommentClient.updateComment(comment);
	                    	}
	                    }
                	}
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e, "系统自动好评订单遇到异常, orderId(" + msg + ")", null);
        }
    }
    
}
