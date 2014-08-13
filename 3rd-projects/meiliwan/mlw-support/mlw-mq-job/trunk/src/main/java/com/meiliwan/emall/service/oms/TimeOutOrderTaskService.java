package com.meiliwan.emall.service.oms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.base.constant.Constants;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.mms.client.UserIntegralServiceClient;
import com.meiliwan.emall.mms.dto.OrderInteralDto;
import com.meiliwan.emall.mms.dto.OrderItemIntegralDto;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.client.OrdClient;
import com.meiliwan.emall.oms.constant.Constant;
import com.meiliwan.emall.oms.constant.OrdIDeliverStatus;
import com.meiliwan.emall.oms.constant.OrdITotalStatus;
import com.meiliwan.emall.oms.constant.OrderStatusType;
import com.meiliwan.emall.oms.dto.OrdDTO;
import com.meiliwan.emall.oms.dto.OrderQueryDTO;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.dto.OrdiDTO;
import com.meiliwan.emall.pms.bean.ProComment;
import com.meiliwan.emall.pms.client.ProActionClient;
import com.meiliwan.emall.pms.client.ProCommentClient;
import com.meiliwan.emall.pms.dto.CommentDTO;
import com.meiliwan.emall.pms.dto.ProductStockItem;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

@Service
public class TimeOutOrderTaskService extends DefaultBaseServiceImpl {

	private final MLWLogger logger = MLWLoggerFactory.getLogger(PresaleOrderTaskService.class) ;
	private final String CONFIG_FILE = "bg-oms-service/system.properties";
	/**
     * 批量处理过期订单
     * 1.过期待签收
     */
    public void autoCheckTimeOutOrderReceived(JsonObject resultObj){
    	try{
	    	PageInfo pageInfo = new PageInfo() ;
			pageInfo.setPagesize(Integer.MAX_VALUE);
			//条件：待签收 + 发货超过n天 
			String time = ConfigOnZk.getInstance().getValue(CONFIG_FILE, "AUTO_RECEIVED_ORDER_TIMEOUT");//分钟
	        long times = new Date().getTime() - Integer.parseInt(time) * 60000;
	        String endTime = DateUtil.getDateTimeStr(times) ;
			OrderQueryDTO orderQuery = new OrderQueryDTO() ;
			orderQuery.setOrderItemStatus(OrdITotalStatus.ORDI_CONSINGMENT.getCode());
			orderQuery.setUpdateTimeEnd(DateUtil.parseDateTime(endTime));
	        
			PagerControl<OrdDTO> pc = OrdClient.getOrderList(orderQuery, pageInfo, true);
			//如果没有这类订单则不需要继续逻辑
			if(pc == null || pc.getEntityList() == null || pc.getEntityList().size() <= 0){
				logger.info("系统暂无过期的待签收订单", orderQuery.toString(), null);
				return ;
			}
			
			for(OrdDTO dto : pc.getEntityList()){
				//获取创建者ID
                OrderStatusDTO ordStatusDIO = new OrderStatusDTO();
                //ordStatusDIO.setCheckOrder(true);
                ordStatusDIO.setAdminId(0);
                ordStatusDIO.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
                ordStatusDIO.setOrderId(dto.getOrderId());
                ordStatusDIO.setUid(dto.getUid());
                ordStatusDIO.setStatusType(OrderStatusType.ID);
                ordStatusDIO.setStatusCode(OrdIDeliverStatus.DELI_RECEIPTED.getCode());
                OrdClient.updateStatus(ordStatusDIO);
                
            	//增加销量
                List<ProductStockItem> items = new ArrayList<ProductStockItem>();
                List<OrdiDTO> ordis = dto.getOrdiList();
                if(ordis == null || ordis.size() <= 0){
                	List<Ordi> ordiList = OrdClient.getOrdiListByOrderId(dto.getOrderId());
                	for (int i = 0; i < ordiList.size(); i++) {
                        ProductStockItem item = new ProductStockItem(ordis.get(i).getProId(), ordis.get(i).getSaleNum());
                        items.add(item);
                    }
                }else{
	            	for (int i = 0; i < ordis.size(); i++) {
	                    ProductStockItem item = new ProductStockItem(ordis.get(i).getProId(), ordis.get(i).getSaleNum());
	                    items.add(item);
	                }
                }
                ProActionClient.updateSaleByOrderConfm(items);

                OrderInteralDto idto = setOrderInteralDto(dto);
                //增加评论
                addProComment(dto.getUid(), dto.getUserName(), dto);
                //增加积分
                UserIntegralServiceClient.addCreateOrderIntegral(idto, dto.getUid());
			}
			
    	} catch (Exception e) {
            logger.error(e, "系统自动好评订单遇到异常,autoCheckTimeOutOrderAppraise", null);
        }
    	
    }
    
    /**
     * 批量处理过期订单
     * 2.过期待评价
     */
    public void autoCheckTimeOutOrderAppraise(JsonObject resultObj) {
    	try{
	    	PageInfo pageInfo = new PageInfo() ;
			pageInfo.setPagesize(Integer.MAX_VALUE);
			//条件：待评价 + 签收超过n天 
			String time = ConfigOnZk.getInstance().getValue(CONFIG_FILE, "AUTO_APPRAISE_ORDER_TIMEOUT");//分钟
	        long times = new Date().getTime() - Integer.parseInt(time) * 60000;
	        String endTime = DateUtil.getDateTimeStr(times) ;
			CommentDTO view = new CommentDTO() ;
			view.setState(Constants.COMMENT_IS_COMMENT_NO);
			view.setEndCreateTime(endTime);
			view.setIsReply("0");
			PagerControl<ProComment> pc = ProCommentClient.getCommentAdminPager(view, pageInfo);
			//如果没有这类订单则不需要继续逻辑
			if(pc == null || pc.getEntityList() == null || pc.getEntityList().size() <= 0){
				logger.info("系统暂无过期的待评价订单", view.toString(), null);
				return ;
			}
	    	
			for(ProComment comment : pc.getEntityList()){
				comment.setScore((short)5);
	    		comment.setState(Constants.COMMENT_IS_COMMENT_YES);
	    		comment.setCommentTime(DateUtil.getCurrentTimestamp());
	    		
	    		ProCommentClient.updateComment(comment);
			}
    	} catch (Exception e) {
            logger.error(e, "系统自动好评订单遇到异常,autoCheckTimeOutOrderAppraise", null);
        }
    }
    
    
    /**
     * 构造积分的DTO
     *
     * @return
     */
    private OrderInteralDto setOrderInteralDto(OrdDTO dto) {
    	List<OrdiDTO> list = dto.getOrdiList();
        //6、下订单送积分多种方法测试
        OrderInteralDto orderInteralDto = new OrderInteralDto();
        //1、设置订单号
        orderInteralDto.setOrderId(dto.getOrderId());
        //2、构建订单行
        List<OrderItemIntegralDto> items = new ArrayList<OrderItemIntegralDto>();
        if(list != null && list.size() > 0){
        	for (OrdiDTO ordi : list) {
	            OrderItemIntegralDto itemIntegralDto = new OrderItemIntegralDto();
	            itemIntegralDto.setProAmount(ordi.getUintPrice());
	
	            itemIntegralDto.setProId(ordi.getProId());
	            itemIntegralDto.setSaleNum(ordi.getSaleNum());
	
	            itemIntegralDto.setCategoryId(ordi.getProCateId());
	            items.add(itemIntegralDto);
	        }
        	
        }else{
	        List<Ordi> ordiList = OrdClient.getOrdiListByOrderId(dto.getOrderId());
	        for (Ordi ordi : ordiList) {
	            OrderItemIntegralDto itemIntegralDto = new OrderItemIntegralDto();
	            itemIntegralDto.setProAmount(ordi.getUintPrice());
	
	            itemIntegralDto.setProId(ordi.getProId());
	            itemIntegralDto.setSaleNum(ordi.getSaleNum());
	
	            itemIntegralDto.setCategoryId(ordi.getProCateId());
	            items.add(itemIntegralDto);
	        }
        }

        //3、设置订单行
        orderInteralDto.setItem(items);
        return orderInteralDto;
    }
    
    /**
     * 增加评价
     *
     * @param uid
     * @param nickName
     * @return
     */
    private void addProComment(int uid, String nickName, OrdDTO dto) {
        List<OrdiDTO> list = dto.getOrdiList();
        if(list != null && list.size() > 0){
	        for (OrdiDTO ordi : list) {
	            ProComment comment = new ProComment();
	            comment.setOrderId(dto.getOrderId());
	            comment.setUid(uid);
	            comment.setNickName(nickName);
	            comment.setProId(ordi.getProId());
	            comment.setProName(ordi.getProName());
	            ProCommentClient.addCommentByOrder(comment);
	        }
	        
        }else{
        	List<Ordi> ordiList = OrdClient.getOrdiListByOrderId(dto.getOrderId());
        	for (Ordi ordi : ordiList) {
	            ProComment comment = new ProComment();
	            comment.setOrderId(dto.getOrderId());
	            comment.setUid(uid);
	            comment.setNickName(nickName);
	            comment.setProId(ordi.getProId());
	            comment.setProName(ordi.getProName());
	            ProCommentClient.addCommentByOrder(comment);
	        }
        }
    }
}
