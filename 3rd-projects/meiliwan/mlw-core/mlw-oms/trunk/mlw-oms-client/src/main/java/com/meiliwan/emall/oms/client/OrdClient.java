package com.meiliwan.emall.oms.client;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.meiliwan.emall.commons.bean.PayCode;
import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.IPUtil;
import com.meiliwan.emall.commons.util.NumberUtil;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.oms.bean.Ord;
import com.meiliwan.emall.oms.bean.OrdAddr;
import com.meiliwan.emall.oms.bean.OrdException;
import com.meiliwan.emall.oms.bean.OrdPay;
import com.meiliwan.emall.oms.bean.OrdPrintLogs;
import com.meiliwan.emall.oms.bean.OrdRemark;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.bean.Retord;
import com.meiliwan.emall.oms.constant.BizCode;
import com.meiliwan.emall.oms.constant.Constant;
import com.meiliwan.emall.oms.constant.OrdIPayStatus;
import com.meiliwan.emall.oms.constant.OrderStatusType;
import com.meiliwan.emall.oms.dto.OrdDTO;
import com.meiliwan.emall.oms.dto.OrdDetailDTO;
import com.meiliwan.emall.oms.dto.OrdNoStockDTO;
import com.meiliwan.emall.oms.dto.OrderQueryDTO;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.dto.SaleProOrdDTO;
import com.meiliwan.emall.pay.constant.PayStatus;
import com.meiliwan.emall.pay.dto.PayParam;
import com.meiliwan.emall.pay.dto.PaymentDTO;
import com.meiliwan.emall.service.BaseService;
import com.meiliwan.emall.sp2.bean.LotteryUserTimes.SOURCE;
import com.meiliwan.emall.sp2.client.LotteryUserTimesClient;

public class OrdClient {

    public static final String CHANGESTATE = "statusMachineService/updateStatus";
    public static final String GETORDERDETAIL = "orderService/selectOrderDetail";
    public static final String GETORDBYORDID = "orderService/selectOrderByOrderId";
    public static final String GETORDERLIST = "orderService/selectOrderList";
    public static final String GETORDISTATUSLIST = "orderService/selectOrdiStatusList";
    public static final String GETORDERLISTSORTCREATETIME = "orderService/selectOrderListSortCreateTime";
    public static final String GETORDIBYORDERITEMID = "orderService/selectOrdiByOrderItemId";
    public static final String SHIP = "orderService/updateStateShip";
    public static final String UPDATEORDERCOMMENT = "orderService/updateOrderComment";
    public static final String DELETEBYORDERID = "orderService/deleteByOrderId";
    public static final String CREATEORDER = "orderService/createOrder";
    public static final String CREATEORDERCOD = "orderService/createOrderCOD";
    public static final String GETCOUNTBYUIDANDSTATUS = "orderService/selectCountByUidAndStatus";
    public static final String GETSALEPRODUCTPAGER = "orderService/selectSaleProductPager";
    public static final String GETORDERPAGERBYADDCOMPLAINTS = "orderService/selectOrderPagerByAddComplaints";
    public static final String GETORDIPAYLIST = "orderService/selectOrdPayList";
    public static final String getOrderAndItems = "orderService/selectOrderAndItems";
    public static final String GET_REMARKS_BY_ORDERID = "orderService/getRemarksByOrderId";
    public static final String INSERT_REMARK = "orderService/insertRemark";
    public static final String GET_PRINT_LOG_LIST_BY = "orderService/getPrintLogListBy";
    public static final String INSERT_PRINT_LOG = "orderService/insertPrintLog";



    private static final MLWLogger logger = MLWLoggerFactory.getLogger(OrdClient.class);

    /**
     *获取（发货单|拣货单）打印记录列表
     */
    public static List<OrdPrintLogs> getPrintLogListBy(OrdPrintLogs ordPrintLogs){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams(GET_PRINT_LOG_LIST_BY, ordPrintLogs));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<OrdPrintLogs>>() {
        }.getType());
    }

    /**
     *添加（发货单|拣货单）打印记录
     */
    public static int insertPrintLog(OrdPrintLogs ordPrintLogs){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams(INSERT_PRINT_LOG, ordPrintLogs));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 获取备注列表
     * @param orderId
     * @return
     */
    public static List<OrdRemark> getRemarksByOrderId(String orderId){
    		if(StringUtils.isEmpty(orderId)){
			return new ArrayList<OrdRemark>();
		}
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams(GET_REMARKS_BY_ORDERID, orderId));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<OrdRemark>>() {
        }.getType());
    }

    /**
     * 添加备注
     * @param ordRemark
     * @return
     */
    public static int insertRemark(OrdRemark ordRemark){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams(INSERT_REMARK, ordRemark));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 获得订单的支付列表
     * @param orderId
     * @return
     */
    public static List<OrdPay> getOrdPayList(String orderId) {
    		if(StringUtils.isEmpty(orderId)){
			return Collections.emptyList();
		}
    	
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams(GETORDIPAYLIST, orderId));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<OrdPay>>() {
        }.getType());
    }

    public static List<OrdiStatus> getOrdiStatusList(String orderId, String statusType) {
    		if(StringUtils.isEmpty(orderId)){
			return Collections.emptyList();
		}
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams(GETORDISTATUSLIST, orderId, statusType));

        JsonElement jbean = obj.get(BaseService.RESULT_OBJ);

        return new Gson().fromJson(jbean, new TypeToken<List<OrdiStatus>>() {
        }.getType());
    }

    public static OrdDetailDTO getOrderAndItems(String orderId) {
    		if(StringUtils.isEmpty(orderId)){
			return null;
		}
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams(getOrderAndItems, orderId));

        JsonObject jbean = obj.get(BaseService.RESULT_OBJ).getAsJsonObject();

        return new Gson().fromJson(jbean, OrdDetailDTO.class);
    }

    public static PagerControl<OrdDTO> getOrderList(OrderQueryDTO orderQuery,
                                                    PageInfo pageInfo, boolean asc) {

        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams(GETORDERLIST, orderQuery, pageInfo,
                        asc));

        JsonObject jbean = obj.get(BaseService.RESULT_OBJ).getAsJsonObject();

        return new Gson().fromJson(jbean, new TypeToken<PagerControl<OrdDTO>>() {
        }.getType());

    }
    
    /**
     * 订单管理加上，主线状态加入带出库字段
     * @param orderQuery
     * @param pageInfo
     * @param asc
     * @return
     */
    public static PagerControl<OrdDTO> getOrderListBkstage(OrderQueryDTO orderQuery,
            PageInfo pageInfo, boolean asc) {

		JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
		JSONTool.buildParams("orderService/selectOrderListBkstage", orderQuery, pageInfo,
		asc));
		
		JsonObject jbean = obj.get(BaseService.RESULT_OBJ).getAsJsonObject();
		
		return new Gson().fromJson(jbean, new TypeToken<PagerControl<OrdDTO>>() {
		}.getType());
	
	}
    
    
    public static PagerControl<OrdDTO> getOrderListSortCreateTime(OrderQueryDTO orderQuery,
                                                                  PageInfo pageInfo, boolean asc) {

        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams(GETORDERLISTSORTCREATETIME, orderQuery, pageInfo,
                        asc));

        JsonObject jbean = obj.get(BaseService.RESULT_OBJ).getAsJsonObject();

        PagerControl<OrdDTO> pc = new Gson().fromJson(jbean, new TypeToken<PagerControl<OrdDTO>>() {
        }.getType());
        //设置部分支付,个人中心、订单列表用到
        setPartPayAmout(pc);
        //如果已经退换货,设置退换货编号
        setRetOrder(pc);
        return pc;

    }

    private static void setRetOrder(PagerControl<OrdDTO> pc) {
        // TODO Auto-generated method stub

        for (OrdDTO dto : pc.getEntityList()) {
            List<Retord> retList = ReturnOrderClient.getRetordListByOid(dto.getOrderId());
            if (retList == null) {
                break;
            }
            //这里要每条数据都初始化
            long tempTime = 0l;
            for (Retord retord : retList) {
                long cTime = retord.getCreateTime().getTime();
                //最大的时间和不为退换货取消状态
                if (cTime > tempTime && retord.getIsEndNode() !=1) {
                    dto.setRetOrderId(retord.getRetordOrderId());
                    tempTime = cTime;
                }
            }
        }
    }

    /**
     * add by guangde 增加灵活sql语句，查询可投诉订单列表
     * @param orderQuery
     * @param pageInfo
     * @return
     */
    public static PagerControl<OrdDTO> getOrderListSortCreateTime(OrderQueryDTO orderQuery, PageInfo pageInfo) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams("orderService/selectOrderListToApply", orderQuery, pageInfo));
        JsonObject jbean = obj.get(BaseService.RESULT_OBJ).getAsJsonObject();
        return new Gson().fromJson(jbean, new TypeToken<PagerControl<OrdDTO>>() {
        }.getType());
    }

    private static void setPartPayAmout(PagerControl<OrdDTO> pc) {

        for (OrdDTO dto : pc.getEntityList()) {
        	double tempPay = 0;
        	double notPayAmout = 0;
        		if(dto.getOrdPays()!=null&&dto.getOrdPays().size()>0){
        			for (OrdPay pay : dto.getOrdPays()) {
        				if (pay != null && pay.getPayStatus() != null && pay.getPayStatus().equals(OrdIPayStatus.PAY_FINISHED.getCode())) {//增加判断：如果是已支付状态，则做++
                            tempPay = NumberUtil.add(tempPay, pay.getPayAmount());
                        }
        			}
        		}
            dto.setPartPayAmout(tempPay);
            notPayAmout = NumberUtil.add(dto.getRealPayAmount(), -(tempPay));
            dto.setNotPayAmout(notPayAmout);
        }

    }


    public static OrdDetailDTO getDetail(String ordId) {
    		if(StringUtils.isEmpty(ordId)){
    			return null;
    		}
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams(GETORDERDETAIL, ordId));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), OrdDetailDTO.class);


    }

    public static Ord getOrdByOrdId(String ordId) {
    		if(StringUtils.isEmpty(ordId)){
			return null;
		}
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams(GETORDBYORDID, ordId));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), Ord.class);
    }

    /**
     * 批量取消
     * @param dto
     * @return
     */
    public static void updateStatus(OrderStatusDTO dto) {
        // TODO Auto-generated method stub
        IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams(CHANGESTATE, dto));
    }

    /**
     * 支付成功，减库存、修改支付状态
     * @param result
     * @return
     */
    public static void updateSubStockAndPayStatus(PaymentDTO result, PayParam pmr) {
        List<OrdPay> payList = new ArrayList<OrdPay>();
        OrdDetailDTO ordDetailDTO = null;
        String statusCode = null;
        Ord ord = null;
        if (result != null && pmr != null) {
            OrdPay ordPay = buildOrdPay(result, pmr, (short) result.getPayParams().length);
            //订单和支付，分别定义了两套不同的支付code，所以得从支付code转化为订单的code。
            if (result.getPayStatus().compareTo(PayStatus.PAY_FAILURE) == 0) {
                statusCode = OrdIPayStatus.PAY_FAILURE.getCode();
                ordPay.setPayStatus(OrdIPayStatus.PAY_FAILURE.getCode());     //如果是支付失败，则set-> 该条ordPay失败
            } else if (result.getPayStatus().compareTo(PayStatus.PAY_PARTIAL) == 0) {
                statusCode = OrdIPayStatus.PAY_PARTIAL.getCode();
                ordPay.setPayStatus(OrdIPayStatus.PAY_FINISHED.getCode());         //如果是支付部分已成功，则set-> 该条ordPay成功
            } else {
                statusCode = OrdIPayStatus.PAY_FINISHED.getCode();
                ordPay.setPayStatus(OrdIPayStatus.PAY_FINISHED.getCode());     //如果是支付成功，则set-> 该条ordPay成功
            }

            //通过订单ID获得订单头,包括订单行
            ordDetailDTO = getOrderAndItems(result.getOrderId());
            ord = ordDetailDTO.getOrd();
            payList.add(ordPay);
        }
        OrderStatusDTO statusDTO = new OrderStatusDTO();
        statusDTO.setStatusType(OrderStatusType.IP);
        statusDTO.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
        statusDTO.setStatusCode(statusCode);
        statusDTO.setOrdPays(payList);
        statusDTO.setUid(result.getUid());
        statusDTO.setOrderId(result.getOrderId());
        if (result != null && result.getOrderId() != null && ord != null) {
            if (ord.getOrderId().equals(result.getOrderId()) && ordDetailDTO.getOrdiList() != null && ord.getUid().equals(result.getUid())) {
                //调用状态机：修改支付状态
                try {
                    updateStatus(statusDTO);
                } catch (Exception e) {
                    createExceptionOrd(e, ord, ord.getRealPayAmount(), "", BizCode.ORDI_STATUS.name());
                    logger.error(e, "支付回调修改订单状态有异常，已生成异常单，statusDTO" + statusDTO + ")", null);
                }
                //如果整笔订单支付已完成，则减少库存
                //注释掉支付减库存代码：20131101产品5.1.1版本需求变动，减库存放在下单的时候。（这段代码保留）
                /*if (statusCode.equals(OrdIPayStatus.PAY_FINISHED.getCode())) {
                    *//*Map<ActivityProductKey, Integer> actMap = new HashMap<ActivityProductKey, Integer>();
                    Set<PackSaleNumKey> saleNumKeySet = new HashSet<PackSaleNumKey>();*//*
                    List<StockItem> items = new ArrayList<StockItem>();
                    for (Ordi ordi : ordDetailDTO.getOrdiList()) {
                        if (ordi != null) {
                            StockItem si = new StockItem();
                            OrderItem orderItem = new OrderItem();
                            orderItem.setOrderId(ord.getOrderId());
                            orderItem.setOrderItemId(ordi.getOrderItemId());
                            si.setProId(ordi.getProId());
                            si.setBuyNum(ordi.getSaleNum());
                            si.setOrderItem(orderItem);
                            items.add(si);
                        }
                    }
                    try {
                        StockItemsUpdateResult sr = ProStockClient.updateStockOnOrderSubmit(items, ord.getUid(), "user");
                        if (!sr.getStatus() && sr.getStockItemStatusList() != null && sr.getStockItemStatusList().size() > 0) {
                            for (StockItemStatus ss : sr.getStockItemStatusList()) {
                                if (ss != null) {
                                    if (!ss.getStatus()) {
                                        logger.error(null, "支付回调方法，减库存失败，商品ID（" + ss.getStockItem().getProId() + "）", null);
                                        updateOrdiSubStockFail(ss.getStockItem().getOrderItem().getOrderItemId());
                                        createExceptionOrd(new Exception(), ord, ord.getRealPayAmount(), "订单（" + ord.getOrderId() + "）中的商品" + ss.getStockItem().getProId() + "扣减库存失败");
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.error(e, "支付回调减库存出现异常，PayMethodResult" + result.toString(), null);
                        createExceptionOrd(e, ord, ord.getRealPayAmount(), ord.getOrderId() + "：订单扣减库存时出现异常！");
                    }
                }*/
                
                //6.0.0.1-泰国节活动需求 (活动期间，每次消费金额，每满19元可获一次抽奖机会，邮费计入累积金额)
                logger.info("===OrdIPayStatus print.", OrdIPayStatus.PAY_FINISHED.getCode(), IPUtil.getLocalIp());
                if(statusCode.equals(OrdIPayStatus.PAY_FINISHED.getCode())){
                	try{
	                	ConfigOnZk configOnZk = ConfigOnZk.getInstance();
	                	String sTime = configOnZk.getValue("commons/system.properties", "act.send.money.startTime", "2013-04-01 00:00:00");
	        			String eTime = configOnZk.getValue("commons/system.properties","act.send.money.endTime","2013-04-30 23:59:59");
	        			int money = Integer.parseInt(configOnZk.getValue("commons/system.properties", "act.full.send.money","19"));
	        			logger.info("==act.send.money.startTime .", sTime, IPUtil.getLocalIp());
	        			logger.info("==act.send.money.endTime .", eTime, IPUtil.getLocalIp());
	        			logger.info("==act.full.send.money .", money, IPUtil.getLocalIp());
	        			Date startTime = DateUtil.parseDateTime(sTime) ;
	        			Date endTime = DateUtil.parseDateTime(eTime);
	        			Date date = new Date();
	        			if(date.getTime()>startTime.getTime() && date.getTime() < endTime.getTime()){
	        				BigDecimal payment = BigDecimal.valueOf(result.getTotalAmount());
	        				BigDecimal ret = BigDecimal.valueOf(money) ;
	        				BigDecimal eResult = payment.divide(ret, 0, BigDecimal.ROUND_DOWN);
	        				logger.info("==taiguo act times .", eResult.intValue(), IPUtil.getLocalIp());
	        				//如果大于零则调用抽奖增加次数
	        				if(eResult.intValue() > 0){
	        					logger.info("泰国新年活动.", "payment:"+payment.doubleValue()+", ret:"+ret.doubleValue()+", eResult:"+eResult.doubleValue(), IPUtil.getLocalIp());
	        					LotteryUserTimesClient.addTimes(result.getUid(), eResult.intValue(), SOURCE.PAY_FINISH, IPUtil.getLocalIp()) ;
	        				}
	        			}
	        			
                	}catch (Exception e) {
						logger.error(e, "泰国新年活动. configOnZk getValue erro : commons/system.properties" , null);
					}
                }
                
                
            } else {
                createExceptionOrd(null, ord, ord.getRealPayAmount(), "", BizCode.ORD.name());
                logger.warn("订单或用户参数校验不通过,已生成异常单", "ordDetailDTO(" + ordDetailDTO.toString() + ")PayMethodResult(" + result.toString() + ")", null);
            }
        }
    }

    /**
     * add time:2014-2-17
     * version:5.2.3
     * 用户下单走内部支付支时，修改支付状态（该方法只适合美丽湾内部支付的时候调用）
     * @param result
     * @return
     */
    public static PayStatus updatePayStatusByOrder(PaymentDTO result, PayParam[] pmr) {
        String statusCode = null;
        PayStatus status = PayStatus.PAY_FAILURE;
        if (result.getPayStatus().compareTo(PayStatus.PAY_FAILURE) == 0 || result.getPayStatus().compareTo(PayStatus.PAY_WAIT) == 0) {
            return status;//如果支付总状态为失败或任然是待支付，则直接返回失败状态
        }
        //订单和支付，分别定义了两套不同的支付code，所以得从支付code转化为订单的code。
        if (result.getPayStatus().compareTo(PayStatus.PAY_PARTIAL) == 0) {
            statusCode = OrdIPayStatus.PAY_PARTIAL.getCode();
            status = PayStatus.PAY_PARTIAL;
        }
        if (result.getPayStatus().compareTo(PayStatus.PAY_FINISHED) == 0) {
            statusCode = OrdIPayStatus.PAY_FINISHED.getCode();
            status = PayStatus.PAY_FINISHED;
        }
        List<OrdPay> payList = new ArrayList<OrdPay>();
        OrdDetailDTO ordDetailDTO = null;
        Ord ord = null;
        if (result != null && pmr != null) {
            for (PayParam pp : pmr) {
                //如果支付是第三方支付，则排除
                if (PayCode.isThirdPay(pp.getPayCode().name())) {
                    continue;
                }
                OrdPay ordPay = buildOrdPay(result, pp, (short) pmr.length);
                if (pp.getPayStatus().compareTo(PayStatus.PAY_FAILURE) == 0) {
                    ordPay.setPayStatus(OrdIPayStatus.PAY_FAILURE.getCode());//如果是单个支付方式支付失败。则set-> 该条ordPay失败
                    return PayStatus.PAY_FAILURE;//直接返回支付失败提示
                } else {
                    ordPay.setPayStatus(OrdIPayStatus.PAY_FINISHED.getCode());//否则是部分已支付或者支付成功，则set-> 该条ordPay成功，部分已支付也需要执行以下代码，更改订单状态为部分已支付状态
                }
                //通过订单ID获得订单头,包括订单行
                ordDetailDTO = getOrderAndItems(result.getOrderId());
                ord = ordDetailDTO.getOrd();
                payList.add(ordPay);
            }
        } else {
            return PayStatus.PAY_FAILURE;
        }
        OrderStatusDTO statusDTO = new OrderStatusDTO();
        statusDTO.setStatusType(OrderStatusType.IP);
        statusDTO.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
        statusDTO.setStatusCode(statusCode);//总状态code
        statusDTO.setOrdPays(payList);
        statusDTO.setUid(result.getUid());
        statusDTO.setOrderId(result.getOrderId());
        if (result.getOrderId() != null && ord != null && ord.getOrderId().equals(result.getOrderId()) && ordDetailDTO.getOrdiList() != null && ord.getUid().equals(result.getUid())) {
            //调用状态机：修改支付状态
            try {
                updateStatus(statusDTO);
                return status;//返回结果包括成功或者部分已支付
            } catch (Exception e) {
                createExceptionOrd(e, ord, ord.getRealPayAmount(), "", BizCode.ORDI_STATUS.name());
                logger.error(e, "站内支付回调修改订单状态有异常，已生成异常单，该订单可能已完成支付。statusDTO" + statusDTO + ")", null);
                return PayStatus.SERVICE_EXCEPTION;//返回表示失败
            }
        } else {
            createExceptionOrd(null, ord, ord.getRealPayAmount(), "", BizCode.ORD.name());
            logger.warn("订单或用户参数校验不通过,已生成异常单，", "ordDetailDTO(" + ordDetailDTO.toString() + ")PayMethodResult(" + result.toString() + ")", null);
            return PayStatus.SERVICE_EXCEPTION;//返回表示支付失败
        }
    }


    //构建订单支付对象
    private static OrdPay buildOrdPay(PaymentDTO result, PayParam pmr, short payWayCount) {
        Date date = new Date();
        OrdPay ordPay = new OrdPay();
        ordPay.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
        ordPay.setPayTime(date);
        ordPay.setCreatedTime(date);
        ordPay.setOrderId(result.getOrderId());
        ordPay.setPayAmount(pmr.getAmount());
        ordPay.setPayCode(pmr.getPayCode().name());
        ordPay.setPayId(pmr.getPayId());
        ordPay.setPayItemNum(payWayCount);
        ordPay.setPayName(result.getSubject());
        ordPay.setPayType(result.getPayType().name());
        ordPay.setState(GlobalNames.STATE_VALID);
        ordPay.setUid(result.getUid());
        ordPay.setUpdatedTime(date);
        ordPay.setPayTime(DateUtil.getCurrentTimestamp());
        return ordPay;
    }


    /**
     * 添加备注,orderId,和comment必须有
     * @param ord
     * @return
     */
    public static void updateOrderComment(Ord ord) {
        // TODO Auto-generated method stub
        IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams(UPDATEORDERCOMMENT, ord));
    }

    /**
     * 删除订单
     * @param orderId
     * @return
     */
    public static void deleteOrder(String orderId) {
        // TODO Auto-generated method stub
        IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams(DELETEBYORDERID, orderId));
    }

    /**
     * 添加备注,orderId,和comment必须有
     * @param orderItemId
     * @return
     */
    public static Ordi getOrdiByOrderItemId(String orderItemId) {
        // TODO Auto-generated method stub
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams(GETORDIBYORDERITEMID, orderItemId));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), Ordi.class);
    }

    /**
     * 生成订单
     * @param ordDetailDTO
     * @return
     */
    public static OrdDetailDTO createOrd(OrdDetailDTO ordDetailDTO) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams(CREATEORDER, ordDetailDTO));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), OrdDetailDTO.class);
    }

    /**
     * 生成订单,货到付款
     * @param ordDetailDTO
     * @return
     */
    public static OrdDetailDTO createOrdCOD(OrdDetailDTO ordDetailDTO) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams(CREATEORDERCOD, ordDetailDTO));
        JsonObject jbean = obj.get(BaseService.RESULT_OBJ).getAsJsonObject();
        return new Gson().fromJson(jbean, OrdDetailDTO.class);
    }

    public static int getCount(OrderQueryDTO dto) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams(GETCOUNTBYUIDANDSTATUS, dto));
        int jbean = obj.get(BaseService.RESULT_OBJ).getAsInt();
        return jbean;
    }


    /**
     * 生成订单
     * @param dto
     * @param pageInfo
     * @return
     */
    public static PagerControl<SaleProOrdDTO> getSaleProductPager(SaleProOrdDTO dto, PageInfo pageInfo) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams(GETSALEPRODUCTPAGER, dto, pageInfo));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<PagerControl<SaleProOrdDTO>>() {
        }.getType());
    }

    /**
     * 发起投诉时候，选择订单头列表。
     * 简单的查询订单头列表
     * @param ord
     * @param pageInfo
     * @return
     */
    public static PagerControl<Ord> getOrderPagerByAddComplaints(Ord ord, PageInfo pageInfo, String whereSql) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams(GETORDERPAGERBYADDCOMPLAINTS, ord, pageInfo, whereSql));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<PagerControl<Ord>>() {
        }.getType());
    }

    /**
     * 生成异常订单
     * @param e
     * @param ord
     */
    public static void createExceptionOrd(Exception e, Ord ord, double hadPayAmount, String remark, String bizCode) {
        if (ord != null) {
            OrdException ordException = new OrdException();
            ordException.setState(GlobalNames.STATE_VALID);
            ordException.setOrderId(ord.getOrderId());
            ordException.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
            ordException.setBizCode(bizCode);
            ordException.setErrorCode("OMS-updateSubStockAndPayStatus-updateSubStockAndPayStatus");
            if (e == null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                ordException.setErrorMsg("订单信息或支付金额信息校验失败:需支付" + ord.getRealPayAmount() + ",实际支付:" + hadPayAmount + "\n" + sw.toString());
            } else {
                ordException.setErrorMsg("异常信息:(" + remark + ")" + e);
            }
            ordException.setOrdLastStatus(ord.getOrderStatus());
            ordException.setStatusCode(OrdIPayStatus.PAY_FINISHED.getCode());
            ordException.setStatusType(OrderStatusType.IP.name());
            ordException.setUid(ord.getUid());
            Date date = new Date();
            ordException.setUpdateTime(date);
            ordException.setCreateTime(date);
            IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE, JSONTool.buildParams("exceptionOrderService/createOrdException", ordException));
            //修改订单异常状态
            updateOrdExceptionStatusWithException(ord.getOrderId(),bizCode);
        }
    }

    /**
     * 通过订单ID校验订单下的商品库存
     * 返回库存不足对象列表，如果返回空列表，则库存校验通过。
     * 活动购买时：不管是活动库存不足还是活动的商品库存不足，统一返回活动ID
     * 套餐购买时：不管是套餐库存不足还是套餐的商品库存不足，统一返回套餐ID
     * @param orderId
     * @return
     */
    public static List<OrdNoStockDTO> checkStockByOid(String orderId) {
    		if(StringUtils.isEmpty(orderId)){
			return Collections.emptyList();
		}
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams("orderService/checkStockByOid", orderId));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<OrdNoStockDTO>>() {
        }.getType());
    }

    /**
     * @param status
     * @return
     */
    public static boolean updatePayStatus(int status, String oid) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams("orderService/updatePayStatus", status, oid));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 标识订单存在异常订单
     * @param oid
     * @return
     */
    public static boolean updateOrdExceptionStatus(String oid) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams("orderService/updateOrdExceptionStatus", oid));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 标识订单存在异常订单
     * @param oid
     * @return
     */
    public static boolean updateOrdExceptionStatusWithException(String oid,String code) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams("orderService/updateOrdExceptionStatusWithException", oid,code));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }
    /**
     * 标识订单行扣减库存失败
     * @param ordiId
     * @return
     */
    public static boolean updateOrdiSubStockFail(String ordiId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams("orderService/updateOrdiSubStockFail", ordiId));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 根据订单号，获取订单行列表信息
     * @param orderId
     */
    public static List<Ordi> getOrdiListByOrderId(String orderId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams("orderService/selectOrdiListByOrderId", orderId));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<Ordi>>() {
        }.getType());
    }

    /**
     * 后台配送管理页面查询各标签分组状态的数量
     * @param orderQuery
     * @return
     */
    @SuppressWarnings("unchecked")
	public static List getAdminSendGroupCounts(OrderQueryDTO orderQuery) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams("orderService/selectOrderGroupCountByStatusCode", orderQuery));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List>() {
        }.getType());
    }

    /**
     *通过订单ID列表获取一组订单
     */
    public static List<OrdDTO> getOrdDTOListBy(String[] orderIdList){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildArrayParams("orderService/getOrdDTOListBy", orderIdList));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<OrdDTO>>() {
        }.getType());
    }

    /**
     * 获得订单收货地址
     * @param orderId
     * @return
     */
    public static OrdAddr getOrdAddrById(String orderId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams("orderService/getOrdAddrById", orderId));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), OrdAddr.class);
    }
    
    /**
     * 通过条件(OrderQueryDTO)取订单列表
     * @param orderQuery
     * @param pageInfo
     * @return
     */
    public static List<Ord> getOrdAndOrdiByQuery(OrderQueryDTO orderQuery, PageInfo pageInfo) {
    	List<Ord> list = new ArrayList<Ord>() ;
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
				JSONTool.buildParams("orderService/getOrdAndOrdiByQuery", orderQuery, pageInfo));
		list = new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<Ord>>() {}.getType());
		
		return list;
	}
    
    /**
     * 通过订单id获取订单列表
     * @param ids 订单id的list
     * @return
     */
    public static List<Ord> getOrderAndOrdiByIds(List<String> ids){
    	List<Ord> list = new ArrayList<Ord>() ;
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
				JSONTool.buildParams("orderService/getOrderByIds", ids));
		list = new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<Ord>>() {}.getType());
		
		return list;
    }

    /**
     * 同getOrderList方法，但用于大量数据的情况使用
     * @param orderQuery
     * @param pageInfo
     * @return
     */
    public static boolean getOrderListForReport(OrderQueryDTO orderQuery, PageInfo pageInfo, boolean asc) {
    	JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams("orderService/selectOrderListForReport", orderQuery, pageInfo, asc));
		return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
	}
}
