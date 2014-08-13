package com.meiliwan.emall.oms.service;


import static com.meiliwan.emall.icetool.JSONTool.addToResult;
import static com.meiliwan.emall.oms.constant.OrdIPayStatus.PAY_WAIT;
import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_CANCEL;
import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_COMMITTED;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IP;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.meiliwan.emall.mms.bean.UserPassportSimple;
import com.meiliwan.emall.mms.client.UserPassportClient;
import com.meiliwan.emall.oms.bean.*;
import com.meiliwan.emall.oms.dao.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.bean.PayCode;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.IDGenerator;
import com.meiliwan.emall.oms.constant.BizCode;
import com.meiliwan.emall.oms.constant.Constant;
import com.meiliwan.emall.oms.constant.OrdIDeliverStatus;
import com.meiliwan.emall.oms.constant.OrdIPayStatus;
import com.meiliwan.emall.oms.constant.OrdITotalStatus;
import com.meiliwan.emall.oms.constant.OrderConstants;
import com.meiliwan.emall.oms.constant.OrderStatusType;
import com.meiliwan.emall.oms.constant.OrderType;
import com.meiliwan.emall.oms.constant.TransportCompany;
import com.meiliwan.emall.oms.dto.OrdDTO;
import com.meiliwan.emall.oms.dto.OrdDetailDTO;
import com.meiliwan.emall.oms.dto.OrdNoStockDTO;
import com.meiliwan.emall.oms.dto.OrderQueryDTO;
import com.meiliwan.emall.oms.dto.OrdiDTO;
import com.meiliwan.emall.oms.dto.SaleProOrdDTO;
import com.meiliwan.emall.pms.bean.SimpleProduct;
import com.meiliwan.emall.pms.client.ProProductClient;
import com.meiliwan.emall.stock.bean.OrderItem;
import com.meiliwan.emall.stock.bean.StockItem;
import com.meiliwan.emall.stock.bean.StockItemStatus;
import com.meiliwan.emall.stock.client.ProStockClient;


/**
 */
@Service
public class OrderService extends BaseOrderService {

    private static MLWLogger logger = MLWLoggerFactory.getLogger(OrderService.class);
    @Autowired
    private OrdiDao ordiDao;
    @Autowired
    private OrdInvoiceDao ordInvoiceDao;
    @Autowired
    private OrdAddrDao ordAddrDao;
    @Autowired
    private OrdPayDao ordPayDao;
    @Autowired
    private OrdiStatusDao ordiStatusDao;
    @Autowired
    private OrdLogDao ordLogDao;
    @Autowired
    private RetordDao retordDao;
    @Autowired
    private OrdRemarkDao ordRemarkDao;
    @Autowired
    private OrdPrintLogsDao ordPrintLogsDao;
    @Autowired
    private OrdDescDao ordDescDao;



    /**
     * 根据订单ID获取备注list
     */
    @IceServiceMethod
    public void getRemarksByOrderId(JsonObject resultObj,String orderId){
        OrdRemark ordRemark = new OrdRemark();
        ordRemark.setOrderId(orderId);
        List<OrdRemark> list = ordRemarkDao.getListByObj(ordRemark,false);
        Collections.reverse(list);
        addToResult(list,resultObj);
    }

    /**
     * 添加备注
     * 1、备注表添加一条记录
     * 2、把订单表remark_count增加1
     * @param resultObj
     * @param ordRemark
     */
    @IceServiceMethod
    public void insertRemark(JsonObject resultObj, OrdRemark ordRemark) {
        if (ordRemark == null || StringUtils.isEmpty(ordRemark.getOrderId())||StringUtils.isEmpty(ordRemark.getContent())) {
            addToResult(0, resultObj);
            return;
        }

        try {
            //1、备注表添加一备注记录
            ordRemarkDao.insert(ordRemark);
            //2、更新备注订单的remark_count字段
            if (ordRemark.getId() > 0) {
                Ord remarkOrd = ordDao.getEntityById(ordRemark.getOrderId(),true);
                remarkOrd.setRemarkCount(remarkOrd.getRemarkCount() + 1);
                ordDao.update(remarkOrd);
            }
        } catch (Exception e) {
            addToResult(0,resultObj);
            throw new ServiceException("service-oms-OrderService.insertRemark:{}", ordRemark == null ? "" : ordRemark.toString(), e);
        }
        addToResult(1, resultObj);
    }

    /**
     * 取得OrdPay的List
     * @param resultObj
     * @param orderId
     */
    public void selectOrdiStatusList(JsonObject resultObj, String orderId, String statusType) {
        String[] st = new String[1];
        st[0] = statusType;
        List<OrdiStatus> list = ordiStatusDao.selectByOrderId(orderId, st);
        addToResult(list, resultObj);
    }

    /**
     * 取得OrdPay的List
     * @param resultObj
     * @param oid
     */
    public void selectOrdPayList(JsonObject resultObj, String oid) {
        if (!Strings.isNullOrEmpty(oid)) {
            OrdPay pay = new OrdPay();
            pay.setOrderId(oid);
            addToResult(ordPayDao.getListByObj(pay), resultObj);
        } else {
            addToResult(Collections.emptyList(), resultObj);
        }
    }

    /**
     * 根据订单号，拿订单的信息
     * @param resultObj
     * @param orderId
     */
    public void selectOrderByOrderId(JsonObject resultObj, String orderId) {
        if (!Strings.isNullOrEmpty(orderId)) {
            addToResult(ordDao.getEntityById(orderId), resultObj);
        }
    }

    /**
     * 根据订单行号，拿订单行的信息
     * @param resultObj
     * @param orderItemId
     */
    public void selectOrderItemByOrdIId(JsonObject resultObj, String orderItemId) {
        if (!Strings.isNullOrEmpty(orderItemId)) {
            addToResult(ordiDao.getEntityById(orderItemId), resultObj);
        }
    }

    /**
     * 根据订单号，获取订单及订单行商品信息
     * @param resultObj
     * @param orderId
     */
    public void selectOrderAndItems(JsonObject resultObj, String orderId) {
        if (StringUtils.isEmpty(orderId)) {
            addToResult(orderId, resultObj);
            return;
        }
        //查询订单信息
        Ord ord = ordDao.getEntityById(orderId);
        if (ord == null) throw new ServiceException("OrderService-getOrderAndItems {}", orderId);

        //订单行列表
        List<Ordi> ordiList = ordiDao.getOrdIListByOrdId(orderId);

        OrdDetailDTO dto = new OrdDetailDTO();
        dto.setOrd(ord);
        dto.setOrdiList(ordiList);
        addToResult(dto, resultObj);
    }

    /**
     * 根据订单号拿订单详情
     * @param resultObj
     * @param orderId
     */
    public void selectOrderDetail(JsonObject resultObj, String orderId) {
        if (StringUtils.isEmpty(orderId)) {
            addToResult(new JsonArray(), resultObj);
            return;
        }
        //查询订单信息
        Ord ord = ordDao.getEntityById(orderId);
        if (ord == null) {
            addToResult(new JsonArray(), resultObj);
            return;
        }
        //支付类型
        List<OrdPay> ordPayList = ordPayDao.getOrdPayListByOrderId(orderId);
        //订单行列表
        List<Ordi> ordiList = ordiDao.getOrdIListByOrdId(orderId);
        //发票
        OrdInvoice invoice = ordInvoiceDao.getEntityById(orderId);
        //日志
        OrdLog log = new OrdLog();
        log.setOrderId(orderId);
        List<OrdLog> logList = ordLogDao.getListByObj(log);
        OrdDetailDTO dto = new OrdDetailDTO();
        dto.setOrd(ord);
        dto.setOrdiList(ordiList);
        dto.setInvoice(invoice);
        dto.setOrdPayList(ordPayList);
        dto.setOrdlogList(logList);
        addToResult(dto, resultObj);
    }

    private List<OrdDTO> selectOrdDIOList(List<OrdiDTO> ordiList, String statusType, OrderQueryDTO orderQuery, boolean isDeliWaitOutRepository) {
        //这里改为LinkedHashMap的原因是为了排序
        Map<String, OrdDTO> map = new LinkedHashMap<String, OrdDTO>();
        if (!StringUtils.isEmpty(statusType)
                && !statusType.equals(OrderStatusType.IS.getType())) {
            for (OrdiDTO ordi : ordiList) {
                OrdDTO dto = null;
                List<OrdiDTO> list = null;
                if (!map.containsKey(ordi.getOrderId())) {
                    dto = new OrdDTO();
                    dto.setBillType(ordi.getBillType());
                    dto.setOrderId(ordi.getOrderId());
                    dto.setCreateTime(ordi.getCreateTime());
                    dto.setUid(ordi.getUid());
                    dto.setPayTime(ordi.getPayTime());
                    dto.setRecvName(ordi.getRecvName());
                    dto.setRealPayAmount(ordi.getOrderRealPayAmount());
                    dto.setOrderStatus(ordi.getStatusCode());
                    dto.setStatusCode(ordi.getStatusCode());
                    if (!StringUtils.isEmpty(orderQuery.getOrderId())) {
                        dto.setOrderStatus(orderQuery.getOrderItemStatus());
                        ordi.setStatusType(orderQuery.getStatusType());
                        ordi.setStatusCode(orderQuery.getOrderItemStatus());
                    }
                    //
                    dto.setPayCode(ordi.getPayCode());
                    dto.setPayName(ordi.getPayName());
                    dto.setPayTime(ordi.getPayTime());
                    list = new ArrayList<OrdiDTO>();
                } else {
                    dto = map.get(ordi.getOrderId());
                    list = dto.getOrdiList();
                }
                list.add(ordi);
                dto.setOrdiList(list);
                dto.setOrderType(ordi.getOrderType());
                //判断ordDTO里带不带paycode和payname，如果不带，则意味着查询条件里不带支付工具的查询条件，那么则需要去load一次ord_pay表
                if (StringUtils.isEmpty(dto.getPayCode())) {
                    dto.setOrdPays(ordPayDao.getOrdPayListByOrderId(dto.getOrderId()));
                }
                //如果是货到付款订单，则去查询它的配送状态，如果是待出库则设置待出库状态true
                if (isDeliWaitOutRepository && dto.getOrderType().equals(OrderType.REAL_ORDER_COD.getCode())) {
                    OrdiStatus os = ordiStatusDao.selectOneByOrderId(dto.getOrderId(), OrderStatusType.ID.getType(), OrdIDeliverStatus.DELI_WAIT_OUT_REPOSITORY.getCode());
                    dto.setDeliWaitOutRepositoryFlag(os != null ? true : false);
                }
                map.put(dto.getOrderId(), dto);
            }
        } else {
            for (OrdiDTO ordi : ordiList) {
                OrdDTO dto = null;
                List<OrdiDTO> list = null;
                if (!map.containsKey(ordi.getOrderId())) {
                    dto = new OrdDTO();
                    dto.setBillType(ordi.getBillType());
                    dto.setOrderId(ordi.getOrderId());
                    dto.setCreateTime(ordi.getCreateTime());
                    dto.setUid(ordi.getUid());
                    dto.setPayTime(ordi.getPayTime());
                    dto.setRecvName(ordi.getRecvName());
                    dto.setRealPayAmount(ordi.getOrderRealPayAmount());
                    dto.setOrderStatus(ordi.getOrderItemStatus());
                    dto.setPayCode(ordi.getPayCode());
                    dto.setPayName(ordi.getPayName());
                    dto.setPayTime(ordi.getPayTime());
                    list = new ArrayList<OrdiDTO>();
                } else {
                    dto = map.get(ordi.getOrderId());
                    list = dto.getOrdiList();
                }
                list.add(ordi);
                dto.setOrdiList(list);
                dto.setOrderType(ordi.getOrderType());
                //判断ordDTO里带不带paycode和payname，如果不带，则意味着查询条件里不带支付工具的查询条件，那么则需要去load一次ord_pay表
                if (StringUtils.isEmpty(dto.getPayCode())) {
                    dto.setOrdPays(ordPayDao.getOrdPayListByOrderId(dto.getOrderId()));
                }
                //如果是货到付款订单，则去查询它的配送状态，如果是待出库则设置待出库状态true
                if (isDeliWaitOutRepository && dto.getOrderType().equals(OrderType.REAL_ORDER_COD.getCode())) {
                    OrdiStatus os = ordiStatusDao.selectOneByOrderId(dto.getOrderId(), OrderStatusType.ID.getType(), OrdIDeliverStatus.DELI_WAIT_OUT_REPOSITORY.getCode());
                    dto.setDeliWaitOutRepositoryFlag(os != null ? true : false);
                }
                map.put(dto.getOrderId(), dto);
            }
        }

        OrdDTO[] dtoArr = new OrdDTO[map.size()];
        dtoArr = map.values().toArray(dtoArr);
        return Arrays.asList(dtoArr);
    }

    private List<OrdDTO> selectOrdDIOListForAdmin(List<OrdiDTO> ordiList, String statusType, OrderQueryDTO orderQuery, boolean isDeliWaitOutRepository) {
        //这里改为LinkedHashMap的原因是为了排序
        Map<String, OrdDTO> map = new LinkedHashMap<String, OrdDTO>();
        for (OrdiDTO ordi : ordiList) {
            OrdDTO dto = null;
            List<OrdiDTO> list = null;
            if (!map.containsKey(ordi.getOrderId())) {
                //每次单独去load一次ord表
                Ord ord = ordDao.getEntityById(ordi.getOrderId());
                if(ord == null){
                    continue;
                }
                dto = new OrdDTO();
                dto.setBillType(ord.getBillType());
                dto.setOrderId(ord.getOrderId());
                dto.setCreateTime(ord.getCreateTime());
                dto.setUid(ord.getUid());
                dto.setPayTime(ord.getPayTime());
                dto.setRecvName(ord.getRecvName());
                dto.setRealPayAmount(ord.getRealPayAmount());
                dto.setOrderStatus(ord.getOrderStatus());
                dto.setStatusCode(ord.getOrderStatus());
                if (!StringUtils.isEmpty(orderQuery.getOrderId())) {
                    dto.setOrderStatus(orderQuery.getOrderItemStatus());
                    ordi.setStatusType(orderQuery.getStatusType());
                    ordi.setStatusCode(orderQuery.getOrderItemStatus());
                }
                //
                dto.setPayCode(ordi.getPayCode());
                dto.setPayName(ordi.getPayName());
                dto.setPayTime(ordi.getPayTime());
                dto.setRemarkCount(ord.getRemarkCount());
                dto.setStockTime(ord.getStockTime());
                dto.setExceptionCode(ord.getExceptionCode());
                dto.setPrintPickCount(ord.getPrintPickCount());
                dto.setPrintSendCount(ord.getPrintSendCount());
                list = new ArrayList<OrdiDTO>();
            } else {
                dto = map.get(ordi.getOrderId());
                list = dto.getOrdiList();
            }
            list.add(ordi);
            dto.setOrdiList(list);
            dto.setOrderType(ordi.getOrderType());
            //判断ordDTO里带不带paycode和payname，如果不带，则意味着查询条件里不带支付工具的查询条件，那么则需要去load一次ord_pay表
            if (StringUtils.isEmpty(dto.getPayCode())) {
                dto.setOrdPays(ordPayDao.getOrdPayListByOrderId(dto.getOrderId()));
            }
            //如果查询它的配送状态，如果是待出库则设置待出库状态true
            if (isDeliWaitOutRepository) {
                OrdiStatus os = ordiStatusDao.selectOneByOrderId(dto.getOrderId(), OrderStatusType.ID.getType(), OrdIDeliverStatus.DELI_WAIT_OUT_REPOSITORY.getCode());
                dto.setDeliWaitOutRepositoryFlag(os != null ? true : false);
            }
            map.put(dto.getOrderId(), dto);
        }

        OrdDTO[] dtoArr = new OrdDTO[map.size()];
        dtoArr = map.values().toArray(dtoArr);
        return Arrays.asList(dtoArr);
    }

    /**
     * 依据传入的条件，查询订单行表或订单头表
     * 条件判断依据如下：
     * 查询订单行：
     * 依据订单ID做查询 || 依据商品ID  || 商品名做查询  || 订单的商品类别：代销、代购，  || 订单的状态类型  || 逆向订单， 只单纯查询订单行表
     * <p/>
     * 其他：
     * 查询订单头
     * @param resultObj
     * @param orderQuery 封装常规SQL查询参数
     * @param pageInfo   分页信息
     * @param asc
     * @return
     */
    public void selectOrderList(JsonObject resultObj, OrderQueryDTO orderQuery,
                                PageInfo pageInfo, boolean asc) {
        selectOrderList(resultObj, orderQuery, pageInfo, "update_time", asc, false);
    }

    /**
     * 依据传入的条件，查询订单行表或订单头表
     * 条件判断依据如下：
     * 查询订单行：
     * 依据订单ID做查询 || 依据商品ID  || 商品名做查询  || 订单的商品类别：代销、代购，  || 订单的状态类型  || 逆向订单， 只单纯查询订单行表
     * <p/>
     * 其他：
     * 查询订单头
     * @param resultObj
     * @param orderQuery 封装常规SQL查询参数
     * @param pageInfo   分页信息
     * @param asc
     * @return
     */
    public void selectOrderListBkstage(JsonObject resultObj, OrderQueryDTO orderQuery,
                                PageInfo pageInfo, boolean asc) {
        selectOrderList(resultObj, orderQuery, pageInfo, "update_time", asc, true);
    }

    
    /**
     * 依据传入的条件，查询订单行表或订单头表
     * 条件判断依据如下：
     * 查询订单行：
     * 依据订单ID做查询 || 依据商品ID  || 商品名做查询  || 订单的商品类别：代销、代购，  || 订单的状态类型  || 逆向订单， 只单纯查询订单行表
     * <p/>
     * 其他：
     * 查询订单头
     * @param resultObj
     * @param orderQuery 封装常规SQL查询参数
     * @param pageInfo   分页信息
     * @param asc
     * @return
     */
    public void selectOrderListSortCreateTime(JsonObject resultObj, OrderQueryDTO orderQuery,
                                              PageInfo pageInfo, boolean asc) {


        String sort = asc ? " asc" : " desc";
        String orderBy = "oi.create_time";
        if (orderQuery.getProName() != null && !Strings.isNullOrEmpty(orderQuery.getProName().trim())) {
            orderQuery.setProName("%" + orderQuery.getProName().trim() + "%");
        }

        //依据订单ID做查询 || 依据商品ID  || 商品名做查询  || 订单的商品类别：代销、代购，  || 订单的状态类型  || 逆向订单， 只单纯查询订单行表
        if ( !StringUtils.isEmpty(orderQuery.getOrderId())) {

            PagerControl<OrdiDTO> ordiPC = null;
            //如果有订单状态类型的查询条件，则走ORDI和ORDI_STATUS表的联合查询
            if (!StringUtils.isEmpty(orderQuery.getStatusType())) {
                ordiPC = ordiDao.getOrdIListByStatusType(orderQuery, pageInfo, orderBy + sort);
            } else {
                ordiPC = ordiDao.getOrdIListByOrdIStatus(orderQuery, pageInfo, orderBy + sort);
            }
            PagerControl<OrdDTO> ordDTOPC = new PagerControl<OrdDTO>();
            List<OrdDTO> list = selectOrdDIOList(ordiPC.getEntityList(), orderQuery.getStatusType(), orderQuery, true);
            ordDTOPC.setEntityList(list);
            ordDTOPC.setPageInfo(ordiPC.getPageInfo());
            addToResult(ordDTOPC, resultObj);
            return;
        } else {
            orderBy = "o.create_time";
            PagerControl<OrdDTO> ordPC = ordDao.getOrdListByOrdStatusForFrontUser(orderQuery, pageInfo, null, orderBy + sort);
            if (ordPC != null) {
                List<OrdDTO> ordList = ordPC.getEntityList();
                List<String> orderIds = new ArrayList<String>(ordList.size());
                List<String> orderCODIds = new ArrayList<String>(ordList.size());
                Map<String, OrdDTO> ordMap = new HashMap<String, OrdDTO>();
                for (OrdDTO ordDTO : ordList) {
                    orderIds.add(ordDTO.getOrderId());
                    ordMap.put(ordDTO.getOrderId(), ordDTO);
                    if (ordDTO.getOrderType().equals(OrderType.REAL_ORDER_COD.getCode())) {
                        orderCODIds.add(ordDTO.getOrderId());
                    }
                    ordDTO.setOrdiList(new ArrayList<OrdiDTO>());
                }

                if(orderIds!=null && !orderIds.isEmpty()) {
                    //去拿每个订单的订单行
                    List<OrdiDTO> ordiList = ordiDao.getOrdIDTOListByOrdIds(orderIds);
                    if(ordiList!=null && !ordiList.isEmpty()) {
                        for (OrdiDTO ordiDTO : ordiList) {
                            OrdDTO ordDTO = ordMap.get(ordiDTO.getOrderId());
                            List<OrdiDTO> ordOrdiList = ordDTO.getOrdiList();
                            ordOrdiList.add(ordiDTO);
                        }
                    }
                    //判断ordDTO里带不带paycode和payname，如果不带，则意味着查询条件里不带支付工具的查询条件，那么则需要去load一次ord_pay表
                    if(StringUtils.isEmpty(orderQuery.getPayCode())) {
                        List<OrdPay> ordPayList = ordPayDao.getOrdPayListByOrderIds(orderIds);
                        if(ordPayList!=null && !ordPayList.isEmpty()) {
                            for (OrdPay ordPay : ordPayList) {
                            	// 如果是优惠券支付的话则不需要计算在内 lzl 0620
                            	if(ordPay.getPayCode().equals(PayCode.MLW_T.getCode())){
                            		continue ;
                            	}
                                OrdDTO ordDTO = ordMap.get(ordPay.getOrderId());
                                if(ordDTO.getOrdPays() == null){
                                    ordDTO.setOrdPays(new ArrayList<OrdPay>());
                                }
                                List<OrdPay> ordOrdPayList = ordDTO.getOrdPays();
                                ordOrdPayList.add(ordPay);
                            }
                        }
                    }
                    //如果查询它的配送状态，如果是待出库则设置待出库状态true
                    if(orderCODIds!=null && !orderCODIds.isEmpty()) {
                        List<String> osList = ordiStatusDao.selectOneOrderIdByOrderIds(orderCODIds, OrderStatusType.ID.getType(), OrdIDeliverStatus.DELI_WAIT_OUT_REPOSITORY.getCode());
                        if(osList!=null && !osList.isEmpty()) {
                            for (String osOrderId : osList) {
                                if(!StringUtils.isEmpty(osOrderId)){
                                    OrdDTO ordDTO = ordMap.get(osOrderId);
                                    ordDTO.setDeliWaitOutRepositoryFlag(true);
                                }
                            }
                        }
                    }

                }
            }
            addToResult(ordPC, resultObj);
            return;
        }

    }

    /**
     * 依据传入的条件，查询订单行表或订单头表
     * 条件判断依据如下：
     * 查询订单行：
     * 依据订单ID做查询 || 依据商品ID  || 商品名做查询  || 订单的商品类别：代销、代购，  || 订单的状态类型  || 逆向订单， 只单纯查询订单行表
     * <p/>
     * 其他：
     * 查询订单头
     * @param resultObj
     * @param orderQuery 封装常规SQL查询参数
     * @param pageInfo   分页信息
     * @param asc
     * @return
     */
    private void selectOrderList(JsonObject resultObj, OrderQueryDTO orderQuery,
                                 PageInfo pageInfo, String sortTime, boolean asc, boolean isDeliWaitOutRepository) {
            addToResult(selectOrderListHandle(orderQuery, pageInfo, sortTime, asc, isDeliWaitOutRepository), resultObj);
            return;
    }

    /**
     * 依据传入的条件，分组统计当前状态下订单的总计数
     * @param resultObj
     * @param orderQuery 封装常规SQL查询参数
     * @return
     */
    public void selectOrderGroupCountByStatusCode(JsonObject resultObj, OrderQueryDTO orderQuery) {
        orderQuery.setOrderItemStatus(null);//去掉标签条件。保证点击所有标签都显示相同的数字
        if (orderQuery.getProName() != null && !Strings.isNullOrEmpty(orderQuery.getProName().trim())) {
            orderQuery.setProName("%" + orderQuery.getProName().trim() + "%");//商品名称
        }
        if (orderQuery.getRecvName() != null && !Strings.isNullOrEmpty(orderQuery.getRecvName().trim())) {
            orderQuery.setRecvName("%" + orderQuery.getRecvName().trim() + "%");//收货人姓名
        }
        selectOrderHandleSortForAdmin(orderQuery);
        List list = ordDao.getOrdGroupCountByOrdStatus(orderQuery);//返回一个Map的List
        addToResult(list, resultObj);
        return;
    }

    /**
     * 查询订单（后台订单列表&订单配送列表）
     * @param orderQuery
     * @param pageInfo
     * @param sortTime
     * @param asc
     * @param isDeliWaitOutRepository
     * @return
     */
    protected PagerControl<OrdDTO> selectOrderListHandle(OrderQueryDTO orderQuery, PageInfo pageInfo, String sortTime, boolean asc, boolean isDeliWaitOutRepository) {

        if (orderQuery.getProName() != null && !Strings.isNullOrEmpty(orderQuery.getProName().trim())) {
            orderQuery.setProName("%" + orderQuery.getProName().trim() + "%");//商品名
        }
        //查询订单头表
        if (orderQuery.getRecvName() != null && !Strings.isNullOrEmpty(orderQuery.getRecvName().trim())) {
            orderQuery.setRecvName("%" + orderQuery.getRecvName().trim() + "%");//收货人名称
        }

        //订单的商品类别：代销、代购，  || 逆向订单， 只单纯查询订单行表
        if (orderQuery.getBillType() == Constant.ORDER_BILL_TYPE_REVERSE ) {//逆向订单
            String orderBy = "oi." + sortTime;//订单行的排序字段
            String sort = asc ? " asc" : " desc";
            PagerControl<OrdiDTO> ordiPC = null;
            //如果有订单状态类型的查询条件，则走ORDI和ORDI_STATUS表的联合查询
            if (!StringUtils.isEmpty(orderQuery.getStatusType())) {
                ordiPC = ordiDao.getOrdIListByStatusType(orderQuery, pageInfo, orderBy + sort);
            } else {
                ordiPC = ordiDao.getOrdIListByOrdIStatus(orderQuery, pageInfo, orderBy + sort);
            }

            PagerControl<OrdDTO> ordDTOPC = new PagerControl<OrdDTO>();
            //整合订单行，支付列表，配送列表
            List<OrdDTO> list = selectOrdDIOListForAdmin(ordiPC.getEntityList(), orderQuery.getStatusType(), orderQuery, isDeliWaitOutRepository);
            ordDTOPC.setEntityList(list);
            ordDTOPC.setPageInfo(ordiPC.getPageInfo());//分页信息
            return ordDTOPC;
        } else {//正向订单
            //定义组合排序字段的实现
            String orderBy = selectOrderHandleSortForAdmin(orderQuery);
            PagerControl<OrdDTO> ordPC = ordDao.getOrdListByOrdStatus(orderQuery, pageInfo, orderBy);
            //封装PC中每个OrdDTO里边的ordiList和OrdPayList等
            if (ordPC != null) {
                List<OrdDTO> ordList = ordPC.getEntityList();
                List<String> orderIds = new ArrayList<String>(ordList.size());//订单ID列表
                Map<String, OrdDTO> ordMap = new HashMap<String, OrdDTO>();
                //按照订单ID，OrdDTO存放所有订单,赋值orderIds
                for (OrdDTO ordDTO : ordList) {//此时ordiList为空
                    orderIds.add(ordDTO.getOrderId());
                    ordMap.put(ordDTO.getOrderId(), ordDTO);
                    ordDTO.setOrdiList(new ArrayList<OrdiDTO>());
                }
                //订单Ids列表不为空
                if(orderIds!=null && !orderIds.isEmpty()) {
                    //in查询，搜索出所有orderId的ordiList
                    List<OrdiDTO> ordiList = ordiDao.getOrdIDTOListByOrdIds(orderIds);
                    if(ordiList!=null && !ordiList.isEmpty()) {
                        for (OrdiDTO ordiDTO : ordiList) {
                            OrdDTO ordDTO = ordMap.get(ordiDTO.getOrderId());
                            List<OrdiDTO> ordOrdiList = ordDTO.getOrdiList();
                            ordOrdiList.add(ordiDTO);
                        }
                    }
                    //判断ordDTO里带不带paycode和payname，如果不带，则意味着查询条件里不带支付工具的查询条件，那么则需要去load一次ord_pay表
                    if(StringUtils.isEmpty(orderQuery.getPayCode())) {
                        List<OrdPay> ordPayList = ordPayDao.getOrdPayListByOrderIds(orderIds);
                        if(ordPayList!=null && !ordPayList.isEmpty()) {
                            for (OrdPay ordPay : ordPayList) {//循环OrdMap拿出每个OrdDTO，然后把OrdPayList设置进去
                                OrdDTO ordDTO = ordMap.get(ordPay.getOrderId());
                                if(ordDTO.getOrdPays() == null){
                                    ordDTO.setOrdPays(new ArrayList<OrdPay>());
                                }
                                List<OrdPay> ordOrdPayList = ordDTO.getOrdPays();
                                ordOrdPayList.add(ordPay);
                            }
                        }
                    }
                    //如果查询它的配送状态，如果是待出库则设置待出库状态true
                    if (isDeliWaitOutRepository) {
                        //in查询，查询orderIds内，发货状态为待出库的订单ID列表
                        List<String> osList = ordiStatusDao.selectOneOrderIdByOrderIds(orderIds, OrderStatusType.ID.getType(), OrdIDeliverStatus.DELI_WAIT_OUT_REPOSITORY.getCode());
                        if(osList!=null && !osList.isEmpty()) {
                            for (String osOrderId : osList) {
                                if(!StringUtils.isEmpty(osOrderId)){
                                    OrdDTO ordDTO = ordMap.get(osOrderId);
                                    ordDTO.setDeliWaitOutRepositoryFlag(true);
                                }
                            }
                        }
                    }
                }
            }
            return ordPC;
        }
    }
    private String selectOrderHandleSortForAdmin(OrderQueryDTO orderQuery) {
        String orderBy = null;
        if (!Strings.isNullOrEmpty(orderQuery.getSort())) {
            if (orderQuery.getSort().equals("create_asc")) {
                orderBy = "o.create_time asc";
            }
            if (orderQuery.getSort().equals("create_desc")) {
                orderBy = "o.create_time desc";
            }
            if (orderQuery.getSort().equals("update_asc")) {
                orderBy = "o.update_time asc";
            }
            if (orderQuery.getSort().equals("update_desc")) {
                orderBy = "o.update_time desc";
            }
            if (orderQuery.getSort().equals("ems_asc")) {
                orderBy = "ol.logistics_number asc";
                orderQuery.setLogisticsCompany(TransportCompany.EMS.getCode());
            }
            if (orderQuery.getSort().equals("ems_desc")) {
                orderBy = "ol.logistics_number desc";
                orderQuery.setLogisticsCompany(TransportCompany.EMS.getCode());
            }
            if (orderQuery.getSort().equals("sf_asc")) {
                orderBy = "ol.logistics_number asc";
                orderQuery.setLogisticsCompany(TransportCompany.SF.getCode());
            }
            if (orderQuery.getSort().equals("sf_desc")) {
                orderBy = "ol.logistics_number desc";
                orderQuery.setLogisticsCompany(TransportCompany.SF.getCode());
            }
        }
        return orderBy;
    }


    /**
     * 查询订单去申请退换货
     * 查看可以申请退换货的订单列表的业务用到
     * @param orderQuery
     * @param pageInfo
     */
    @IceServiceMethod
    public void selectOrderListToApply(JsonObject resultObj, OrderQueryDTO orderQuery, PageInfo pageInfo) {
        //订单已经完成，等待确认收货，已收货已付款的状态下的订单可退换货
        PagerControl<OrdDTO> ordPC = ordDao.getOrdListByOrdStatusForFrontUser(orderQuery, pageInfo, " (o.order_status=" + OrdITotalStatus.ORDI_FINISHED.getCode() + " or o.order_status=" + OrdITotalStatus.ORDI_CONSINGMENT.getCode() + " or o.order_status=" + OrdITotalStatus.ORDI_RECEIPTED.getCode() + ")", "o.create_time desc");
        if (ordPC != null) {
            //德哥，为毛要新建一个ordDTOPC对象？直接用ordPC不可以吗！
            PagerControl<OrdDTO> ordDTOPC = new PagerControl<OrdDTO>();
            ordDTOPC.setPageInfo(ordPC.getPageInfo());//设置分页信息
            List<OrdDTO> ordList = ordPC.getEntityList();//拿出ordDTO列表
            for (OrdDTO ordDTO : ordList) {
                //去拿每个订单的订单行
                List<OrdiDTO> ordiList = ordiDao.getOrdIDTOListByOrdId(ordDTO.getOrderId());
                ordDTO.setOrdiList(ordiList);
                Retord ret = new Retord();
                ret.setOldOrderId(ordDTO.getOrderId());
                //查询是否已经申请过退换货
                List<Retord> retList = retordDao.getListByObj(ret);
                if (retList != null && retList.size() > 0) {
                    ordDTO.setState((short) 1);//用来表示订单已经申请过退换货了
                    for (Retord retord : retList) {
                        if (retord.getIsEndNode() == 0) {
                            ordDTO.setState((short) 2);//表示申请退换货的订单还在处理中
                        }
                    }
                } else {
                    ordDTO.setState((short) 0);//用来表示订单还未申请过退换货
                }
            }
            ordDTOPC.setEntityList(ordList);
            addToResult(ordDTOPC, resultObj);
            return;
        }
        addToResult(ordPC, resultObj);
    }

    /**
     * 根据订单号更新订单备注
     * (注：现在订单备注为追加式添加，只增不减，所以不存在更新订单备注；2013年11月11日 16:11:08 王志明)
     * @param resultObj
     * @param ord
     */
    public void updateOrderComment(JsonObject resultObj, Ord ord) {
        if (ord == null) {
            saveOrdException("", "OMS-OrderService-updateOrderComment {}", "ord is null", null);
        }
        //查询订单信息
        String ordId = ord.getOrderId();
        String comment = ord.getOrderComments();
        if (StringUtils.isEmpty(ordId)) {
            saveOrdException("", "OMS-OrderService-updateOrderComment {}", ord.toString(), ord.getUid());
        }


        Ord o = new Ord();
        o.setOrderId(ordId);
        //可以为空
        o.setOrderComments(comment);
        o.setUpdateTime(new Date());//加上更新时间，by yuxiong
        int result = ordDao.update(o);
        if (result <= 0) {
            saveOrdException(ordId, "OMS-OrderService-updateOrderComment {}", "update Ord Comment fail", ord.getUid());
        }
    }

    /**
     * 生成订单ID
     * 规则：获取数据库序列表，如果大于等于10位（>=10亿）则不变，否则不足的位数用0补
     */
    private String genOrderId(){
        int id = ordDao.nextOrdSeq();
        return IDGenerator.getId(id, ORDERID_LENGTH);
    }

    /**
     * 创建订单
     * @param resultObj
     * @param ordDetailDTO
     */
    public void createOrder(JsonObject resultObj, OrdDetailDTO ordDetailDTO) {
        Ord ord = ordDetailDTO.getOrd();
        List<Ordi> ordis = ordDetailDTO.getOrdiList();//订单行s
        OrdInvoice invoice = ordDetailDTO.getInvoice();//订单发票
        OrdAddr addr = ordDetailDTO.getOrdAddr();//订单收货地址
        List<OrdPay> ordPays = ordDetailDTO.getOrdPayList();//获取订单支付方式s
        OrdDesc ordDesc = ordDetailDTO.getOrdDesc();
        //订单的购物车项
        if (ordDesc == null) {
            saveOrdException(ord, "OMS-OrderService-createOrder", "订单的购物车项不可为空");
        }
        //构造订单ID
        ord.setOrderId(genOrderId());
        //创建订单，查询走主库
        if (ordDao.getEntityById(ord.getOrderId(), true) != null) {
            saveOrdException(ord, "OMS-OrderService-createOrder", "订单ID已存在");
        }
        //校验订单行的总条数是否正确
        if (ordis == null || ord.getTotalItem() != ordis.size() || ordis.size() == 0) {
            saveOrdException(ord, "OMS-OrderService-createOrder", "订单行总条数不正确");
        }
        //校验订单的支付
        if (ordPays == null || ordPays.size() == 0) {
            saveOrdException(ord, "OMS-OrderService-createOrder", "订单的支付方式不可为空");
        }

        //校验订单行和头的数据
        List<Ordi> dbOrdis = ordiDao.getOrdIListByOrdId(ord.getOrderId(), true);
        if (dbOrdis != null && dbOrdis.size() > 0) {
            saveOrdException(ord, "OMS-OrderService-createOrder", "订单行ID已存在");
        }
        List<OrdiStatus> oisList = ordiStatusDao.selectByOrderId(ord.getOrderId(), true);
        if (oisList != null && oisList.size() > 0) {
            saveOrdException(ord, "OMS-OrderService-createOrder", "订单行状态已存在");
        }
        //去查询订单的支付信息做校验
        OrdPay op = ordPayDao.getEntityById(new OrdPayKey(ord.getOrderId()), true);
        if (op != null) {
            saveOrdException(BizCode.ORD_PAY, ord, "OMS-OrderService-createOrder", "该订单ID支付已存在");
        }


        try {
            //保存发票
            if (invoice != null && ord.getIsInvoice()!=null &&  ord.getIsInvoice()== OrderConstants.ORD_INVOICE_HAVE) {
                ord.setIsInvoice(OrderConstants.ORD_INVOICE_HAVE);//有发票
                invoice.setOrderId(ord.getOrderId());//设置订单ID，前边已经生成
                invoice.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);//正向订单
                invoice.setState(GlobalNames.STATE_VALID);//发票有效
                ordInvoiceDao.insert(invoice);
            } else {
                ord.setIsInvoice(OrderConstants.ORD_INVOICE_NO);//无发票
            }
            //保存订单头
            ord.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);//正向订单
            ord.setOrderStatus(ORDI_COMMITTED.getCode());//等待付款
            ord.setState(GlobalNames.STATE_VALID);//有效
            ord.setCreateTime(new Date());
            ordDao.insert(ord);
            ordDesc.setOrderId(ord.getOrderId());
            ordDesc.setCreateTime(ord.getCreateTime());
            ordDescDao.insert(ordDesc);
            //保存收货地址
            if (addr != null) {
                addr.setOrderId(ord.getOrderId());
                addr.setCreateTime(ord.getCreateTime());
                ordAddrDao.insert(addr);
            }
            //保存订单的支付方式
            for (OrdPay ordPay : ordPays) {
                if (ordPay.getPayCode().equals(PayCode.MLW_W.name()) || ordPay.getPayCode().equals(PayCode.MLW_C.name()) || ordPay.getPayCode().equals(PayCode.MLW_T.name())) {//支付方式为：美丽湾钱包
                    ordPay.setOrderId(ord.getOrderId());
                    ordPay.setPayTime(ord.getCreateTime());
                    ordPay.setState(GlobalNames.STATE_VALID);
                    ordPay.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);//正向
                    ordPay.setCreatedTime(ord.getCreateTime());
                    ordPay.setUid(ord.getUid());//用户ID
                    ordPay.setUserName(ord.getUserName());
                    ordPay.setPayStatus(OrdIPayStatus.PAY_WAIT.getCode());//待支付金额
                    ordPayDao.insert(ordPay);
                }
            }
        } catch (ServiceException e) {
            saveOrdException(ord, "OMS-OrderService-createOrder", e.getMessage());
        }

        //保存订单行
        int orderItemNum = 1;   //订单行数，该值用来定义订单行的行ID，每次循环后+1
        for (Ordi ordi : ordis) {
            try {
                ordi.setTransportFee(ord.getTransportFee());//运费
                ordi.setOrderRealPayAmount(ord.getRealPayAmount());//实际支付金额
                //订单行的优惠金额使用从客户端提交过来的ordi本身的值 modify by yuxiong 2013.8.6
                //ordi.setFavorableAmount(ord.getFavorableTotalAmount());
                ordi.setOrderId(ord.getOrderId());
                ordi.setOrderItemId(getOrderItemId(ord.getOrderId(), orderItemNum));
                ordi.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
                ordi.setOrderItemStatus(ORDI_COMMITTED.getCode());//订单行状态：等待付款
                //为了保持条形码，只为获取条形码，是否可以优化，通过Dao方法根据ProdId获取Prodcut对象
                SimpleProduct product = ProProductClient.getProductById(ordi.getProId());
                if (product != null) {
                    ordi.setProBarCode(product.getBarCode());
                }
                ordiDao.insert(ordi);
            } catch (ServiceException e) {
                saveOrdIException(ordi, "OMS-OrderService-createOrder", e.getMessage(),ord.getUid());
            }

            //保存订单行: 提交状态
            OrdiStatus ordiStatus = new OrdiStatus();
            ordiStatus.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);    //正向订单
            ordiStatus.setOrderId(ordi.getOrderId());
            ordiStatus.setOrderItemId(ordi.getOrderItemId());
            ordiStatus.setStatusCode(ORDI_COMMITTED.getCode());//IS状态：等待支付
            ordiStatus.setStatusType(IS.getType());
            ordiStatus.setUid(ordi.getUid());
            try {
                ordiStatusDao.insert(ordiStatus);
            } catch (ServiceException e) {
                saveOrdIStatusException(ordiStatus, "OMS-OrderService-createOrder", e.getMessage(),ord.getUid());
            }

            //保存订单行: 待支付状态
            OrdiStatus ordiStatus2 = new OrdiStatus();
            ordiStatus2.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);    //正向
            ordiStatus2.setOrderId(ordi.getOrderId());
            ordiStatus2.setOrderItemId(ordi.getOrderItemId());
            ordiStatus2.setStatusCode(PAY_WAIT.getCode());//IP状态：待支付
            ordiStatus2.setStatusType(IP.getType());
            ordiStatus2.setUid(ordi.getUid());
            try {
                ordiStatusDao.insert(ordiStatus2);
            } catch (ServiceException e) {
                saveOrdIStatusException(ordiStatus2, "OMS-OrderService-createOrder", e.getMessage(),ord.getUid());
            }

            orderItemNum++;//加一为下一个ordi的ID生成做准备
        }
        //---检查是否顺丰SF用户 并做备注 lzl 0701
        checkUserForRemark(ord.getUid(), ord.getOrderId());

        //创建一个订单行的操作记录
        Ordi log = new Ordi();
        log.setUserName(ord.getUserName());
        log.setUid(ord.getUid());
        log.setOrderId(ord.getOrderId());
        log.setOrderType(OrderType.REAL_ORDER.getCode());//实体订单
        try {
            ordLogDao.insertOrderCreate(log);
        } catch (ServiceException e) {
            saveOrdException(BizCode.ORD_LOG, ord, "OMS-OrderService-createOrder", e.getMessage());
        }
//        createOrdSubStock(ordis, ord);//创建订单时候，需要减少库存，5.1.5后移到OrderController
        addToResult(ordDetailDTO, resultObj);
    }

    /**
     * 创建货到付款订单
     * @param resultObj
     * @param ordDetailDTO
     */
    public void createOrderCOD(JsonObject resultObj, OrdDetailDTO ordDetailDTO) {
        Ord ord = ordDetailDTO.getOrd();
        List<Ordi> ordis = ordDetailDTO.getOrdiList();
        OrdInvoice invoice = ordDetailDTO.getInvoice();
        OrdAddr addr= ordDetailDTO.getOrdAddr();
        List<OrdPay> ordPays = ordDetailDTO.getOrdPayList();
        OrdDesc ordDesc = ordDetailDTO.getOrdDesc();
        //订单的购物车项
        if (ordDesc == null) {
            saveOrdException(ord, "OMS-OrderService-createOrder", "订单的购物车项不可为空");
        }
        //构造订单ID
        ord.setOrderId(genOrderId());

        //创建订单，查询走主库
        if (ordDao.getEntityById(ord.getOrderId(), true) != null) {
            saveOrdException(ord, "OMS-OrderService-createOrder", "订单ID已存在");
        }
        //校验订单行的总条数是否正确
        if (ordis == null || ord.getTotalItem() != ordis.size() || ordis.size() == 0) {
            saveOrdException(ord, "OMS-OrderService-createOrder", "订单行总条数不正确");
        }

        //校验订单行和头的数据
        List<Ordi> dbOrdis = ordiDao.getOrdIListByOrdId(ord.getOrderId(), true);
        if (dbOrdis != null && dbOrdis.size() > 0) {
            saveOrdException(ord, "OMS-OrderService-createOrder", "订单行ID已存在");
        }
        List<OrdiStatus> oisList = ordiStatusDao.selectByOrderId(ord.getOrderId(), true);
        if (oisList != null && oisList.size() > 0) {
            saveOrdException(ord, "OMS-OrderService-createOrder", "订单行状态已存在");
        }
        //校验订单的支付,货到付款方式
        if (ordPays == null || ordPays.size() == 0) {
            saveOrdException(ord, "OMS-OrderService-createOrder", "订单的支付方式不可为空");
        }

        try {
            //保存发票
            if (invoice != null && ord.getIsInvoice()!=null &&  ord.getIsInvoice()== OrderConstants.ORD_INVOICE_HAVE) {
                invoice.setOrderId(ord.getOrderId());
                invoice.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
                invoice.setState(GlobalNames.STATE_VALID);
                ordInvoiceDao.insert(invoice);
            } else {
                ord.setIsInvoice(OrderConstants.ORD_INVOICE_NO);
            }
            //保存订单头
            ord.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
            ord.setOrderStatus(OrdITotalStatus.ORDI_EFFECTIVED.getCode());
            ord.setState(GlobalNames.STATE_VALID);
            ord.setOrderType(OrderType.REAL_ORDER_COD.getCode());
            ord.setCreateTime(new Date());
            ordDao.insert(ord);
            ordDesc.setOrderId(ord.getOrderId());
            ordDesc.setCreateTime(ord.getCreateTime());
            ordDescDao.insert(ordDesc);
            //保存收货地址
            if (addr != null) {
                addr.setOrderId(ord.getOrderId());
                addr.setCreateTime(ord.getCreateTime());
                ordAddrDao.insert(addr);
            }
            //保存订单的支付方式
            for (OrdPay ordPay : ordPays) {
                ordPay.setOrderId(ord.getOrderId());
                ordPay.setPayTime(ord.getCreateTime());
                ordPay.setState(GlobalNames.STATE_VALID);
                ordPay.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
                ordPay.setCreatedTime(ord.getCreateTime());
                ordPay.setUid(ord.getUid());
                ordPay.setUserName(ord.getUserName());
                ordPayDao.insert(ordPay);
            }
        } catch (ServiceException e) {
            saveOrdException(ord, "OMS-OrderService-createOrder", e.getMessage());
        }

        //保存订单行
        int orderItemNum = 1;   //订单行数，该值用来定义订单行的行ID
        for (Ordi ordi : ordis) {
            try {
                ordi.setOrderType(ord.getOrderType());
                ordi.setOrderRealPayAmount(ord.getRealPayAmount());
                ordi.setFavorableAmount(ord.getFavorableTotalAmount());
                ordi.setOrderId(ord.getOrderId());
                ordi.setTransportFee(ord.getTransportFee());
                ordi.setOrderItemId(getOrderItemId(ord.getOrderId(), orderItemNum));
                ordi.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
                ordi.setOrderItemStatus(OrdITotalStatus.ORDI_EFFECTIVED.getCode());
                //为了保持条形码
                SimpleProduct product = ProProductClient.getProductById(ordi.getProId());
                if (product != null) {
                    ordi.setProBarCode(product.getBarCode());
                }
                ordiDao.insert(ordi);
            } catch (ServiceException e) {
                saveOrdIException(ordi, "OMS-OrderService-createOrder", e.getMessage(),ord.getUid());
            }

            //保存订单行: 提交状态
            OrdiStatus ordiStatus = new OrdiStatus();
            ordiStatus.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);    //写逆向订单
            ordiStatus.setOrderId(ordi.getOrderId());
            ordiStatus.setOrderItemId(ordi.getOrderItemId());
            ordiStatus.setStatusCode(OrdITotalStatus.ORDI_EFFECTIVED.getCode());
            ordiStatus.setStatusType(IS.getType());
            ordiStatus.setUid(ordi.getUid());
            try {
                ordiStatusDao.insert(ordiStatus);
            } catch (ServiceException e) {
                saveOrdIStatusException(ordiStatus, "OMS-OrderService-createOrder", e.getMessage(),ord.getUid());
            }

            //保存订单行: 待支付状态
            OrdiStatus ordiStatus2 = new OrdiStatus();
            ordiStatus2.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);    //写逆向订单
            ordiStatus2.setOrderId(ordi.getOrderId());
            ordiStatus2.setOrderItemId(ordi.getOrderItemId());
            ordiStatus2.setStatusCode(OrdIDeliverStatus.DELI_WAIT_OUT_REPOSITORY.getCode());
            ordiStatus2.setStatusType(OrderStatusType.ID.getType());
            ordiStatus2.setUid(ordi.getUid());
            try {
                ordiStatusDao.insert(ordiStatus2);
            } catch (ServiceException e) {
                saveOrdIStatusException(ordiStatus2, "OMS-OrderService-createOrder", e.getMessage(),ord.getUid());
            }

            orderItemNum++;
        }
        
        //---检查是否顺丰SF用户 并做备注 lzl 0701
        checkUserForRemark(ord.getUid(), ord.getOrderId());

        Ordi log = new Ordi();
        log.setUserName(ord.getUserName());
        log.setUid(ord.getUid());
        log.setOrderId(ord.getOrderId());
        log.setOrderType(OrderType.REAL_ORDER_COD.getCode());
        try {
            ordLogDao.insertOrderCreate(log);
            ordLogDao.insertOrderCreateCOD(log);
        } catch (ServiceException e) {
            saveOrdException(BizCode.ORD_LOG, ord, "OMS-OrderService-createOrder", e.getMessage());
        }
        //减库存
//        createOrdSubStock(ordis, ord);//5.1.5移动到OrderController
        addToResult(ordDetailDTO, resultObj);
    }

    /**
     * 减少订单行对应的商品库存
     */
    private void createOrdSubStock(List<Ordi> ordis, Ord ord){
        try {
            List<StockItem> items = new ArrayList<StockItem>();
            for (Ordi ordi : ordis) {
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
            //调用产品库存client减少商品库存
            ProStockClient.updateStockOnOrderCODSubmit(items, ord.getUid(), "user");
        } catch (Exception e) {//失败需要记录一个异常订单
            Date date = new Date();
            OrdException ordException = new OrdException();
            ordException.setBizCode(BizCode.SUB_STOCK.name());
            ordException.setOrderId(ord.getOrderId());
            ordException.setUid(ord.getUid());
            ordException.setBillType(ord.getBillType());
            ordException.setCreateTime(date);
            ordException.setErrorCode("OMS-OrderService-createOrdSubStock");
            ordException.setErrorMsg(e.getMessage());
            ordException.setStatusType(OrderStatusType.IS.name());
            ordException.setStatusCode(OrdITotalStatus.ORDI_COMMITTED.getCode());
            ordException.setState(GlobalNames.STATE_VALID);
            ordException.setUpdateTime(date);
            ordExceptionDao.insert(ordException);

            Ord order = new Ord();
            order.setOrderId(ord.getOrderId());
            order.setExceptionCode(BizCode.SUB_STOCK.name());
            ordDao.update(order);
            logger.error(e, "用户(uid:" + ord.getUid() + ")下货到付款订单时，扣减库存出现异常", null);
        }
    }

    /**
     * 删除订单
     * @param resultObj
     * @param ordDTO
     */
    public void removeOrder(JsonObject resultObj, OrdDTO ordDTO) {
        //校验订单行和头的数据
        Ord ord = ordDao.getEntityById(ordDTO.getOrderId(), true);//查询主库是否存在该订单
        if (ord == null) {//失败需要记录一条异常订单
            saveOrdException(ordDTO.getOrderId(), "OMS-OrderService-removeOrder", "订单ID不存在", ordDTO.getUid());
        } else if (ord.getUid() != ordDTO.getUid()) {
            saveOrdException(ordDTO.getOrderId(), "OMS-OrderService-removeOrder", "只能删除自己的订单", ordDTO.getUid());
        } else if (!ord.getOrderStatus().equals(ORDI_CANCEL.getCode())) {
            saveOrdException(ordDTO.getOrderId(), "OMS-OrderService-removeOrder", "只能删除已取消的订单", ordDTO.getUid());
        }

        try {
            //伪删除订单头
            ord = new Ord();
            ord.setOrderId(ordDTO.getOrderId());
            ord.setState(GlobalNames.STATE_INVALID);//进行软删除，把订单置为无效-1
            ordDao.deleteByEntity(ord);
            //伪删除订单行
            Ordi ordi = new Ordi();
            ordi.setOrderId(ordDTO.getOrderId());
            ordi.setState(GlobalNames.STATE_INVALID);//进行订单行软删除，把订单行状态置为无效-1
            ordiDao.deleteByEntity(ordi);
        } catch (ServiceException e) {
            saveOrdException(ordDTO.getOrderId(), "OMS-OrderService-removeOrder", e.getMessage(), ordDTO.getUid());
        }
    }

    /**
     * 保存一个订单头的异常订单
     * @param ord
     * @param errorCode
     * @param errorMsg
     */
    protected void saveOrdException(Ord ord, String errorCode, String errorMsg) {
        saveOrdException(BizCode.ORD.name(), ord.getOrderId(), Constant.ORDER_BILL_TYPE_FORWARD, errorCode, errorMsg, IS.getType(),
                ORDI_COMMITTED.getCode(), GlobalNames.STATE_VALID, ord.getUid());
    }


    /**
     * 保存一个订单头的异常订单
     * @param ordId
     * @param errorCode
     * @param errorMsg
     */
    protected void saveOrdException(String ordId, String errorCode, String errorMsg, Integer uid) {
        saveOrdException(BizCode.ORD.name(), ordId, Constant.ORDER_BILL_TYPE_FORWARD, errorCode, errorMsg, IS.getType(),
                ORDI_COMMITTED.getCode(), GlobalNames.STATE_VALID, uid);
    }

    /**
     * 保存一个订单头的异常订单
     * @param ord
     * @param errorCode
     * @param errorMsg
     */
    protected void saveOrdException(BizCode BizCode, Ord ord, String errorCode, String errorMsg) {
        saveOrdException(BizCode.name(), ord.getOrderId(), Constant.ORDER_BILL_TYPE_FORWARD, errorCode, errorMsg, IS.getType(),
                ORDI_COMMITTED.getCode(), GlobalNames.STATE_VALID, ord.getUid());
    }

    /**
     * 保存一个订单行的异常订单
     * @param ordi
     * @param errorCode
     * @param errorMsg
     */
    protected void saveOrdIException(Ordi ordi, String errorCode, String errorMsg,Integer uid) {
        saveOrdException(BizCode.ORDI.name(), ordi.getOrderId(), ordi.getOrderItemId(), Constant.ORDER_BILL_TYPE_FORWARD, errorCode,
                errorMsg, IS.getType(), ORDI_COMMITTED.getCode(), GlobalNames.STATE_VALID,uid);
    }

    /**
     * 保存一个订单行状态的异常订单
     * @param ordiStatus
     * @param errorCode
     * @param errorMsg
     */
    protected void saveOrdIStatusException(OrdiStatus ordiStatus, String errorCode, String errorMsg,Integer uid) {
        saveOrdException(BizCode.ORDI_STATUS.name(), ordiStatus.getOrderId(), ordiStatus.getOrderItemId(), Constant.ORDER_BILL_TYPE_FORWARD, errorCode,
                errorMsg, IS.getType(), ORDI_COMMITTED.getCode(), GlobalNames.STATE_VALID,uid);
    }

    /**
     * 根据订单行号，获取订单及订单行商品信息
     * @param resultObj
     * @param orderItemId
     */
    public void selectOrdiByOrderItemId(JsonObject resultObj, String orderItemId) {

        Ordi ordi = ordiDao.getEntityById(orderItemId);
        addToResult(ordi, resultObj);
    }

    /**
     * 根据订单号，获取订单行列表信息
     * @param resultObj
     * @param orderId
     */
    @IceServiceMethod
    public void selectOrdiListByOrderId(JsonObject resultObj, String orderId) {
        if (!Strings.isNullOrEmpty(orderId)) {
            Ordi ordi = new Ordi();
            ordi.setOrderId(orderId);
            addToResult(ordiDao.getListByObj(ordi), resultObj);
        } else {
            addToResult(Collections.emptyList(), resultObj);
        }
    }

    /**
     * 后台获得已卖出商品列表
     * @param resultObj
     * @param dto
     * @param pageInfo
     */
    public void selectSaleProductPager(JsonObject resultObj, SaleProOrdDTO dto, PageInfo pageInfo) {
        dto.setBillType((int) Constant.ORDER_BILL_TYPE_FORWARD);
        dto.setState((int) GlobalNames.STATE_VALID);
        dto.setOrderItemStatus(Integer.parseInt(OrdITotalStatus.ORDI_FINISHED.getCode()));
        PagerControl<SaleProOrdDTO> pc = ordiDao.getSaleProductPager(dto, pageInfo, "order by pay_time desc");
        addToResult(pc, resultObj);
    }

    /**
     * 删除订单
     * @param resultObj
     * @param orderId
     */
    public void deleteByOrderId(JsonObject resultObj, String orderId) {
        Ord ord = ordDao.getEntityById(orderId, true);
        if (ord == null) {
            throw new ServiceException("can not find the order by orderId" + orderId);
        }
        if (!ord.getOrderStatus().equals(OrdITotalStatus.ORDI_CANCEL.getCode())) {
            throw new ServiceException("the ord not equal " + OrdITotalStatus.ORDI_CANCEL.getCode());
        }
        try {
            Ord neword = new Ord();
            neword.setOrderId(orderId);
            neword.setState(GlobalNames.STATE_INVALID);
            ordDao.update(neword);
            List<Ordi> list = ordiDao.getOrdIListByOrdId(orderId, true);
            for (Ordi ordi : list) {
                ordi.setState(GlobalNames.STATE_INVALID);
                ordiDao.update(ordi);
            }
        } catch (ServiceException e) {
            saveOrdException(ord.getOrderId(), "OMS-OrderService-deleteByOrderId", e.getMessage(), ord.getUid());
        }
    }

    /**
     * 根据用户ID和订单状态获取数量 for 前台
     * @param resultObj
     */
    public void selectCountByUidAndStatus(JsonObject resultObj, OrderQueryDTO dto) {
        int count = ordDao.getCountByObj(dto, null);
        addToResult(count, resultObj);
    }

    /**
     * 增加投诉时候，查询订单列表，选择一个订单进行投诉
     * @param resultObj
     * @param ord
     * @param pageInfo
     */
    @IceServiceMethod
    public void selectOrderPagerByAddComplaints(JsonObject resultObj, Ord ord, PageInfo pageInfo, String whereSql) {
        PagerControl<Ord> pc = ordDao.getPagerByBean(ord, pageInfo, whereSql, " order by create_time desc");
        if (ord.getBillType().equals((short) 1) && pc != null && pc.getEntityList() != null && pc.getEntityList().size() > 0) {
            for (Ord o : pc.getEntityList()) {
                List<OrdiDTO> ordiList = ordiDao.getOrdIDTOListByOrdId(o.getOrderId());
                o.setListOrdiDTO(ordiList);
            }
        }
        addToResult(pc, resultObj);
    }

    /**
     * 通过订单ID校验订单行库存
     * 应用场景：支付订单前，校验订单行库存是否支持
     * @param resultObj
     * @param orderId
     */
    @IceServiceMethod
    public void checkStockByOid(JsonObject resultObj, String orderId) {
        if (!Strings.isNullOrEmpty(orderId)) {
            List<OrdNoStockDTO> dtoList = new ArrayList<OrdNoStockDTO>();
            List<Ordi> ordiList = ordiDao.getOrdIListByOrdId(orderId);
            if (ordiList != null && ordiList.size() > 0) {
                List<StockItem> items = new ArrayList<StockItem>();
                for (Ordi oi : ordiList) {
                    StockItem si = new StockItem();
                    si.setProId(oi.getProId());
                    si.setBuyNum(oi.getSaleNum());
                    items.add(si);
                }
                List<StockItemStatus> listSIS = ProStockClient.checkSellStockIfSub(items);
                for (StockItemStatus sis : listSIS) {
                    if (!sis.getStatus()) {
                        OrdNoStockDTO dto = new OrdNoStockDTO();
                        dto.setId(sis.getStockItem().getProId());
                        dtoList.add(dto);
                    }
                }
                addToResult(dtoList, resultObj);
            } else {
                addToResult((JsonObject) null, resultObj);
            }
        } else {
            addToResult((JsonObject) null, resultObj);
        }
    }

    @IceServiceMethod
    public void updatePayStatus(JsonObject resultObj, short status, String oid) {
        OrdPay ordPay = new OrdPay();
        ordPay.setState(status);
        ordPay.setOrderId(oid);
        ordPay.setPayCode(PayCode.MLW_W.name());
        try {
            ordPayDao.update(ordPay);
            addToResult(true, resultObj);
        } catch (Exception e) {
            addToResult(false, resultObj);
            throw new ServiceException("OrderService-updatePayStatus {},{}", new String[]{oid, Short.toString(status)}, e);
        }
    }


    /**
     * 查看订单详情，返回包括订单行列表
     * 支付成功后调用
     * @param orderId
     * @return
     */
    public OrdDetailDTO selectOrderAndItemsByPayEnd(String orderId) {
        if (StringUtils.isEmpty(orderId)) {
            return null;
        }
        //查询订单信息
        Ord ord = ordDao.getEntityById(orderId);
        if (ord == null) throw new ServiceException("OrderService-getOrderAndItems {}", orderId);

        //订单行列表
        List<Ordi> ordiList = ordiDao.getOrdIListByOrdId(orderId);

        OrdDetailDTO dto = new OrdDetailDTO();
        dto.setOrd(ord);
        dto.setOrdiList(ordiList);
        return dto;
    }

    /**
     * 修改订单状态为存在异常订单
     * @param resultObj
     * @param oid
     */
    @IceServiceMethod
    public void updateOrdExceptionStatus(JsonObject resultObj, String oid) {
        Ord ord = new Ord();
        ord.setOrderId(oid);
        ord.setExceptionCode(BizCode.SUB_STOCK.name());
        int result = ordDao.update(ord);
        addToResult(result > 0 ? true : false, resultObj);
    }


    /**
     * 修改订单状态为存在异常订单
     * @param resultObj
     * @param oid
     */
    @IceServiceMethod
    public void updateOrdExceptionStatusWithException(JsonObject resultObj, String oid,String exceptionCode) {
        Ord ord = new Ord();
        ord.setOrderId(oid);
        ord.setExceptionCode(exceptionCode);
        int result = ordDao.update(ord);
        addToResult(result > 0 ? true : false, resultObj);
    }

    /**
     * 标识订单行扣减库存失败
     * @param resultObj
     * @param ordiId
     */
    @IceServiceMethod
    public void updateOrdiSubStockFail(JsonObject resultObj, String ordiId) {
        Ordi ordi = new Ordi();
        ordi.setOrderItemId(ordiId);
        ordi.setSubStockFlag(GlobalNames.STATE_INVALID);
        int result = ordiDao.update(ordi);
        addToResult(result > 0 ? true : false, resultObj);
    }

    /**
     *获取（发货单|拣货单）打印记录列表
     */
    @IceServiceMethod
    public void getPrintLogListBy(JsonObject resultObj,OrdPrintLogs ordPrintLogs){
        List<OrdPrintLogs> list =ordPrintLogsDao.getListByObj(ordPrintLogs);
        Collections.reverse(list);
        addToResult(list, resultObj);
    }

    /**
     *添加（发货单|拣货单）打印记录 (0:失败，1:成功)
     * 1、插入打印表记录
     * 2、更新订单相印打印数量记录
     */
    @IceServiceMethod
    public void insertPrintLog(JsonObject resultObj,OrdPrintLogs ordPrintLogs){
        if(ordPrintLogs == null || StringUtils.isEmpty(ordPrintLogs.getOrderId())){
            addToResult(0,resultObj);
            return;
        }

        try{
            //1、插入打印表
            ordPrintLogsDao.insert(ordPrintLogs);
            //2、更新订单打印字段
            if(ordPrintLogs.getId() > 0){
                Ord updateOrd = ordDao.getEntityById(ordPrintLogs.getOrderId(),true);
                if(ordPrintLogs.getPrintType() == 1){
                    updateOrd.setPrintSendCount(updateOrd.getPrintSendCount()+1);
                } else if(ordPrintLogs.getPrintType() == 0){
                    updateOrd.setPrintPickCount(updateOrd.getPrintPickCount()+1);
                }
                ordDao.update(updateOrd);
            }
        }catch (Exception e){
            addToResult(0,resultObj);
            throw new ServiceException("service-oms-OrderService.insertPrintLog:{}", ordPrintLogs == null ? "" : ordPrintLogs.toString(), e);
        }
        addToResult(1,resultObj);
    }


    /**
     * 通过list订单id，返回封装好的OrdDTO对象list
     * 1、查询OrdDTO   List
     * 2、获取有效的OrdId List
     * 3、查询OrdiDTO List
     *      1、用ListMultimap分组OrdiDTO    List
     * 4、查询OrdPay   List
     *      1、用ListMultimap分组OrdPay   List
     * 5、循环OrdDTO   List合并每个OrdDTO对象
     */
    @IceServiceMethod
    public void getOrdDTOListBy(JsonObject resultObj,String[] orderIdList){
        addToResult(getOrdDTOListByIds(orderIdList),resultObj);
    }

    public List<OrdDTO> getOrdDTOListByIds(String[] orderIdList) {
        List<OrdDTO> resultList = ordDao.getOrdDTOListByIds(Arrays.asList(orderIdList));
        if (resultList != null && resultList.size() > 0) {
            mergeOrdiAndOrdPay(resultList);
        }
        return resultList;
    }

    /**
     * 获得订单收货地址
     * @param resultObj
     * @param orderId
     */
    @IceServiceMethod
    public void getOrdAddrById(JsonObject resultObj,String orderId){
        if (!Strings.isNullOrEmpty(orderId)) {
            addToResult(ordAddrDao.getEntityById(orderId), resultObj);
        }
    }


    private void mergeOrdiAndOrdPay(List<OrdDTO> resultList) {
        ListMultimap<String,OrdiDTO> ordiDTOListMultimap = ArrayListMultimap.create();
        ListMultimap<String,OrdPay> ordPayArrayListMultimap = ArrayListMultimap.create();
        List<String> orderIds = new ArrayList<String>();

        for (OrdDTO temp:resultList){
            orderIds.add(temp.getOrderId());
        }

        List<OrdiDTO> ordiList = ordiDao.getOrdIDTOListByOrdIds(orderIds);
        if (ordiList != null && !ordiList.isEmpty()) {
            for (OrdiDTO temp : ordiList) {
                ordiDTOListMultimap.put(temp.getOrderId(), temp);
            }
        }

        List<OrdPay> ordPayList = ordPayDao.getOrdPayListByOrderIds(orderIds);
        if(ordPayList != null && !ordPayList.isEmpty()){
            for (OrdPay temp:ordPayList){
                ordPayArrayListMultimap.put(temp.getOrderId(),temp);
            }
        }

        for (OrdDTO temp:resultList){
            temp.setOrdiList(ordiDTOListMultimap.get(temp.getOrderId()));
            temp.setOrdPays(ordPayArrayListMultimap.get(temp.getOrderId()));
        }
    }

    
    public void getOrdAndOrdiByQuery(JsonObject resultObj, OrderQueryDTO orderQuery, PageInfo pageInfo){
    	List<Ord> list = ordDao.getOrdAndOrdiByQuery(orderQuery, pageInfo);
    	List<Ord> resultList = new ArrayList<Ord>() ;
    	for(Ord ord:list){
    		if(ord != null){
    			Ordi ordi = new Ordi() ;
    			ordi.setOrderId(ord.getOrderId());
    			List<Ordi> orderis = ordiDao.getListByObj(ordi) ;
    			ord.setListOrdi(orderis) ;
    			resultList.add(ord);
    		}
    	}
    	addToResult(resultList, resultObj);
    }
    
    public void getOrderByIds(JsonObject resultObj, List<String> ids){
    	List<Ord> list = ordDao.getEntityByIds(ids);
    	List<Ord> resultList = new ArrayList<Ord>() ;
    	for(Ord ord:list){
    		if(ord != null){
    			Ordi ordi = new Ordi() ;
    			ordi.setOrderId(ord.getOrderId());
    			List<Ordi> orderis = ordiDao.getListByObj(ordi) ;
    			ord.setListOrdi(orderis) ;
    			resultList.add(ord);
    		}
    	}
    	addToResult(resultList, resultObj);
    }
    
    /**
     * 检查用户是否需要做备注标记
     * @param uid
     */
    private void checkUserForRemark(Integer uid, String orderId){
    	Integer[] uids = {uid} ;
    	List<UserPassportSimple> listu = UserPassportClient.getPassportSimpleByUids(uids) ;
		if(listu!= null && listu.size()>0){
			UserPassportSimple simpleu = listu.get(0);
			if(simpleu != null && !StringUtils.isBlank(simpleu.getEmail()) && simpleu.getEmail().indexOf("@sf-express.com") != -1){
				OrdRemark ordRemark = new OrdRemark() ;
				ordRemark.setContent("顺丰合作订单");
				ordRemark.setCreateTime(new Date());
				ordRemark.setOrderId(orderId);
				ordRemark.setUid(0);
				ordRemark.setUserName("系统");
				//1、备注表添加一备注记录
	            ordRemarkDao.insert(ordRemark);
	            //2、更新备注订单的remark_count字段
	            if (ordRemark.getId() > 0) {
	                Ord remarkOrd = ordDao.getEntityById(ordRemark.getOrderId(),true);
	                remarkOrd.setRemarkCount(remarkOrd.getRemarkCount() + 1);
	                ordDao.update(remarkOrd);
	            }
			}
		}
    }
    /**
     * 用于大量数据的导出，不通过ice传输，直接转成字符串后扔到缓存中
     * @param resultObj
     * @param orderQuery
     * @param pageInfo
     * @param sortTime
     * @param asc
     * @param isDeliWaitOutRepository
     */
    public void selectOrderListForReport(JsonObject resultObj, OrderQueryDTO orderQuery, PageInfo pageInfo, boolean asc) {
    	PagerControl<OrdDTO> pc = selectOrderListHandle(orderQuery, pageInfo, "update_time", asc, false);
    	boolean isOk = false ;
    	try{
	    	Gson gson = new Gson();
	    	String pcStr = gson.toJson(pc);
	    	isOk = ShardJedisTool.getInstance().set(JedisKey.oms$report, "selectOrderListForReport", pcStr);
    	}catch (Exception e) {
			logger.error(e, "set jedist erro.["+JedisKey.oms$report+", selectOrderListForReport]", null);
		}
    	addToResult(isOk, resultObj);
	}
}