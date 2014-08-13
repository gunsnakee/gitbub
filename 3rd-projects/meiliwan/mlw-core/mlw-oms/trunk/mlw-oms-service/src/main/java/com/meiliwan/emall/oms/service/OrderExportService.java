package com.meiliwan.emall.oms.service;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.oms.bean.OrdAddr;
import com.meiliwan.emall.oms.constant.OrderStatusType;
import com.meiliwan.emall.oms.constant.OrderType;
import com.meiliwan.emall.oms.constant.TransportCompany;
import com.meiliwan.emall.oms.dao.OrdAddrDao;
import com.meiliwan.emall.oms.dao.OrdLogisticsDao;
import com.meiliwan.emall.oms.dto.OrdDTO;
import com.meiliwan.emall.oms.dto.OrderQueryDTO;
import com.meiliwan.emall.oms.dto.OrdiDTO;
import com.meiliwan.emall.oms.dto.export.EMSExcelEntity;
import com.meiliwan.emall.oms.dto.export.SFWaybillExcelEntity;
import com.meiliwan.emall.oms.dto.export.SendGoodsListExcelEntity;
import com.meiliwan.emall.pms.bean.ProCategory;
import com.meiliwan.emall.pms.bean.SimpleProduct;
import com.meiliwan.emall.pms.client.ProCategoryClient;
import com.meiliwan.emall.pms.client.ProProductClient;

/**
 * Created by Sean on 13-10-31.
 */
@Service
public class OrderExportService extends OrderService {

	private static MLWLogger logger = MLWLoggerFactory.getLogger(OrderExportService.class);
    @Autowired
    private OrdLogisticsDao ordLogisticsDao;
    @Autowired
    private OrdAddrDao ordAddrDao;


    @IceServiceMethod
    public void exportSendGoodsList(JsonObject resultObj, OrderQueryDTO orderQuery, PageInfo pageInfo, String sortTime, boolean asc, boolean isDeliWaitOutRepository) {
        List<SendGoodsListExcelEntity> rList = new ArrayList<SendGoodsListExcelEntity>();
        orderQuery.setStatusType(OrderStatusType.ID.getType());
        PagerControl<OrdDTO> dtoPc = selectOrderListHandle(orderQuery, pageInfo, sortTime, asc, isDeliWaitOutRepository);
        if (dtoPc.getPageInfo().getTotalCounts() > 0) {
            for (OrdDTO entity : dtoPc.getEntityList()) {
                for (OrdiDTO ordiDTO : entity.getOrdiList()) {
                    rList.add(genSendGoodsListExcelEntity(ordiDTO, entity));
                }
            }
        }
        addToResult(rList, resultObj);
    }

    @IceServiceMethod
    public void exportSendGoodsByIds(JsonObject resultObj, String[] ids) {
        List<SendGoodsListExcelEntity> rList = new ArrayList<SendGoodsListExcelEntity>();
        if (ids != null && ids.length > 0) {
            List<OrdDTO> list = getOrdDTOListByIds(ids);
            if (list != null && list.size() > 0) {
                for (OrdDTO entity : list) {
                    for (OrdiDTO ordiDTO : entity.getOrdiList()) {
                        rList.add(genSendGoodsListExcelEntity(ordiDTO, entity));
                    }
                }
            }
        }
        addToResult(rList, resultObj);
    }

    public SendGoodsListExcelEntity genSendGoodsListExcelEntity(OrdiDTO param, OrdDTO ordDTO) {
        SendGoodsListExcelEntity rObj = new SendGoodsListExcelEntity();
        rObj.setOrderCreateTime(ordDTO.getCreateTime());
        rObj.setStoreHouseSendTime(ordDTO.getStockTime());
        rObj.setOrderId(ordDTO.getOrderId());
        rObj.setWaybillNum(ordDTO.getLogisticsNumber());
        rObj.setProductName(param.getProName());
        SimpleProduct sp = ProProductClient.getProductById(param.getProId());
        if (sp != null) {
            rObj.setProductNum(sp.getBarCode());
        }
        rObj.setCount(param.getSaleNum());
        rObj.setPrice(param.getUintPrice());
        rObj.setCurProductTotal(param.getTotalAmount());
        return rObj;
    }

    @IceServiceMethod
    public void exportSFWaybillByIds(JsonObject resultObj, String[] ids) {
        List<SFWaybillExcelEntity> rList = new ArrayList<SFWaybillExcelEntity>();
        if (ids != null && ids.length > 0) {
            Map<Integer, ProCategory> curMap = new HashMap<Integer, ProCategory>();
            List<OrdDTO> list = getOrdDTOListByIds(ids);
            if (list != null && list.size() > 0) {
                for (OrdDTO entity : list) {
                    if (TransportCompany.SF.getCode().equals(entity.getLogisticsCompany()))
                        rList.add(genSFWaybillObj(entity, curMap));
                }
            }
        }
        addToResult(rList, resultObj);
    }

    @IceServiceMethod
    public void exportEMSExcelByIds(JsonObject resultObj, String[] ids) {
        List<EMSExcelEntity> rList = new ArrayList<EMSExcelEntity>();
        if (ids != null && ids.length > 0) {
            Map<Integer, ProCategory> curMap = new HashMap<Integer, ProCategory>();
            List<OrdDTO> list = getOrdDTOListByIds(ids);
            if (list != null && list.size() > 0) {
                for (OrdDTO entity : list) {
                    if (TransportCompany.EMS.getCode().equals(entity.getLogisticsCompany()))
                        rList.add(genEMSExcelObj(entity, curMap));
                }
            }
        }
        addToResult(rList, resultObj);
    }

    /**
     * 获取EMSExcel导出对象
     *
     * @param param
     * @param curMap
     * @return
     */
    private EMSExcelEntity genEMSExcelObj(OrdDTO param, Map<Integer, ProCategory> curMap) {
        EMSExcelEntity returnObj = new EMSExcelEntity();

        returnObj.setConsigneeName(param.getRecvName());
        returnObj.setConsigneeCompany("");
        OrdAddr addr = ordAddrDao.getEntityById(param.getOrderId());//根据收货地址ID查询收货地址
        if (addr != null) {
            returnObj.setConsigneePhone(linkStr(" 或 ", addr.getMobile(), addr.getPhone()));//收货人联系方式
            returnObj.setConsigneeAddress(new StringBuffer().append(addr.getProvince() == null ? "" : addr.getProvince()).append(" ").append(addr.getCity() == null ? "" : addr.getCity()).append(" ").append(addr.getCounty() == null ? "" : addr.getCounty()).append(" ").append(addr.getTown() == null ? "" : addr.getTown()).append(" ").append(addr.getDetailAddr() == null ? "" : addr.getDetailAddr()).toString());//国家 省 市 县/区 地址
        } else {
            returnObj.setConsigneeAddress("地址查询有误");
        }

        //是否货到付款
        if (OrderType.REAL_ORDER_COD.getCode().equals(param.getOrderType())) {
            returnObj.setIsCOD("Y - 是");//货到付款
            returnObj.setCODTotalPrice(roundDouble(param.getRealPayAmount(), 2));//签收需要付款金额
        } else {
            returnObj.setIsCOD("N - 否");//不是 货到付款
            returnObj.setCODTotalPrice("0.00");
        }
        returnObj.setWaybillNum(param.getLogisticsNumber());//运单号
        returnObj.setOrderId(param.getOrderId());

        return returnObj;
    }

    @IceServiceMethod
    public void exportSFWaybillList(JsonObject resultObj, OrderQueryDTO orderQuery, PageInfo pageInfo, String sortTime, boolean asc, boolean isDeliWaitOutRepository) {
        List<SFWaybillExcelEntity> rList = new ArrayList<SFWaybillExcelEntity>();

        Map<Integer, ProCategory> curMap = new HashMap<Integer, ProCategory>();
        orderQuery.setStatusType(OrderStatusType.ID.getType());
        PagerControl<OrdDTO> dtoPc = selectOrderListHandle(orderQuery, pageInfo, sortTime, asc, isDeliWaitOutRepository);

        if (dtoPc.getPageInfo().getTotalCounts() > 0) {
            for (OrdDTO entity : dtoPc.getEntityList()) {
                if (TransportCompany.SF.getCode().equals(entity.getLogisticsCompany()))
                    rList.add(genSFWaybillObj(entity, curMap));
            }
        }
        addToResult(rList, resultObj);
    }

    @IceServiceMethod
    public void exportEMSExcel(JsonObject resultObj, OrderQueryDTO orderQuery, PageInfo pageInfo, String sortTime, boolean asc, boolean isDeliWaitOutRepository) {
        List<EMSExcelEntity> rList = new ArrayList<EMSExcelEntity>();

        Map<Integer, ProCategory> curMap = new HashMap<Integer, ProCategory>();
        orderQuery.setStatusType(OrderStatusType.ID.getType());
        PagerControl<OrdDTO> dtoPc = selectOrderListHandle(orderQuery, pageInfo, sortTime, asc, isDeliWaitOutRepository);

        if (dtoPc.getPageInfo().getTotalCounts() > 0) {
            for (OrdDTO entity : dtoPc.getEntityList()) {
                if (TransportCompany.EMS.getCode().equals(entity.getLogisticsCompany()))
                    rList.add(genEMSExcelObj(entity, curMap));
            }
        }
        addToResult(rList, resultObj);
    }

    /**
     * ordDTO 转 导出对象 ^_^
     *
     * @param param
     * @return
     */
    public SFWaybillExcelEntity genSFWaybillObj(OrdDTO param, Map<Integer, ProCategory> curMap) {
        SFWaybillExcelEntity returnObj = new SFWaybillExcelEntity();
        //想放到Controller 减少传输，之后要优化这里
        returnObj.setConsignorName("美丽湾");
        returnObj.setConsignorCompany("美丽传说");
        returnObj.setConsignorAddress("中国-东盟企业总部基地二期1号厂房二楼，美丽湾仓储中心(丰达路东门靠近科园东十路)");
        returnObj.setConsignorPhone("4006-887-887");

        returnObj.setOrderId(param.getOrderId());
        //获取订单地址信息
        OrdAddr addr = ordAddrDao.getEntityById(param.getOrderId());//根据收货地址ID查询收货地址
        if (addr != null) {
            returnObj.setConsigneeName(addr.getRecvName());
            returnObj.setConsigneePhone(linkStr(" 或 ", addr.getMobile(), addr.getPhone()));
            StringBuilder add = new StringBuilder();
            add.append(addr.getProvince()).append(addr.getCity())
                    .append(addr.getCounty()).append(addr.getDetailAddr());
            returnObj.setConsigneeAddress(add.toString());
        } else {
            returnObj.setConsigneeAddress("地址查询有误");
        }

        returnObj.setIsDeposit("N-否");

        //是否到货付款
        if (OrderType.REAL_ORDER_COD.getCode().equals(param.getOrderType())) {
            returnObj.setCODTotalPrice(roundDouble(param.getRealPayAmount(), 2));
            returnObj.setPayType("C-到方付");
            returnObj.setIsCOD("Y-是");
        } else {
            returnObj.setPayType("S-寄方付");
            returnObj.setIsCOD("N-否");
            returnObj.setCODTotalPrice("0.00");
        }

        //获取货运单号
        returnObj.setWaybillNum(param.getLogisticsNumber());

        Map<Integer, ProCategory> orderCateMap = new HashMap<Integer, ProCategory>();
        for (OrdiDTO ordi : param.getOrdiList()) {
            ProCategory pc = curMap.get(ordi);
            if (null == pc) {
                pc = ProCategoryClient.getProCategoryById(ordi.getProCateId());
            }
            if (pc != null) {
                orderCateMap.put(pc.getCategoryId(), pc);
            }

        }

        StringBuilder sb = new StringBuilder();
        for (ProCategory entity : orderCateMap.values()) {
            sb.append(entity.getCategoryName()).append("、");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }


        returnObj.setConsignorContent(sb.toString());
        return returnObj;
    }

    /**
     * 保留小数点几位数字
     *
     * @param input
     * @param scale 保留位数
     * @return
     */
    private String roundDouble(double input, int scale) {
        BigDecimal b = new BigDecimal(input);
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP) + "";
    }

    public OrdLogisticsDao getOrdLogisticsDao() {
        return ordLogisticsDao;
    }

    public void setOrdLogisticsDao(OrdLogisticsDao ordLogisticsDao) {
        this.ordLogisticsDao = ordLogisticsDao;
    }

    public static String linkStr(String separator, String... str) {
        if (str == null || str.length == 0) return "";
        StringBuffer result = new StringBuffer();
        for (String temp : str) {
            if(!Strings.isNullOrEmpty(temp))result.append(temp.toString()).append(separator);
        }
        if (result.toString().length() > 0) {
            return result.toString().substring(0, result.toString().lastIndexOf(separator));
        }
        return "";
    }
    
    /**
     * 通过条件获取订单对象列表 公共
     * @param resultObj
     * @param orderQuery
     * @param pageInfo
     * @param sortTime
     * @param asc
     * @param isDeliWaitOutRepository
     */
    public void exportSendList(JsonObject resultObj, OrderQueryDTO orderQuery, PageInfo pageInfo, String sortTime, boolean asc, boolean isDeliWaitOutRepository) {
        orderQuery.setStatusType(OrderStatusType.ID.getType());
        PagerControl<OrdDTO> dtoPc = selectOrderListHandle(orderQuery, pageInfo, sortTime, asc, isDeliWaitOutRepository);
        addToResult(dtoPc, resultObj);
    }
    
    /**
     * 通过条件获取订单对象列表 公共
     * @param resultObj
     * @param ids
     */
    public void exportSendByIds(JsonObject resultObj, String[] ids) {
    	List<OrdDTO> list = new ArrayList<OrdDTO>() ;
        if (ids != null && ids.length > 0) {
            list = getOrdDTOListByIds(ids);
        }
        addToResult(list, resultObj);
        
    }
    
    /**
     * 同exportSendList，但不直接通过ice传输，用于大量数据导出使用
     * @param resultObj
     * @param orderQuery
     * @param pageInfo
     * @param sortTime
     * @param asc
     */
    public void exportSendListForReport(JsonObject resultObj, OrderQueryDTO orderQuery, PageInfo pageInfo, String sortTime, boolean asc) {
        orderQuery.setStatusType(OrderStatusType.ID.getType());
        PagerControl<OrdDTO> dtoPc = selectOrderListHandle(orderQuery, pageInfo, sortTime, asc, true);
        boolean isOk = false ;
    	try{
	    	Gson gson = new Gson();
	    	String pcStr = gson.toJson(dtoPc);
	    	isOk = ShardJedisTool.getInstance().set(JedisKey.oms$report, "exportSendListForReport", pcStr);
    	}catch (Exception e) {
			logger.error(e, "set jedist erro.["+JedisKey.oms$report+", exportSendListForReport]", null);
		}
    	addToResult(isOk, resultObj);
    }

}
