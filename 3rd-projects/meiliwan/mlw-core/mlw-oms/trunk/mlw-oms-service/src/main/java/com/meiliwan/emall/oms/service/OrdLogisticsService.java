package com.meiliwan.emall.oms.service;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.oms.bean.OrdLogistics;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.dao.OrdLogisticsDao;
import com.meiliwan.emall.oms.dao.OrdiDao;
import com.meiliwan.emall.service.BaseService;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * Created by guangdetang on 13-8-26.
 */
@Service
public class OrdLogisticsService extends DefaultBaseServiceImpl implements BaseService {

    @Autowired
    private OrdLogisticsDao ordLogisticsDao;
    @Autowired
    private OrdiDao ordiDao;

    /**
     * 写入订单物流表
     * @param resultObj
     * @param ordLogistics
     */
    @IceServiceMethod
    public void addOrdLogistics(JsonObject resultObj, OrdLogistics ordLogistics) {
        if (ordLogistics != null && !Strings.isNullOrEmpty(ordLogistics.getOrderId()) && !Strings.isNullOrEmpty(ordLogistics.getLogisticsCompany()) && !Strings.isNullOrEmpty(ordLogistics.getLogisticsNumber())) {
            ordLogistics.setCreateTime(DateUtil.getCurrentTimestamp());
            ordLogistics.setUpdateTime(DateUtil.getCurrentTimestamp());
            List<Ordi> list = ordiDao.getOrdIListByOrdId(ordLogistics.getOrderId(), true);
            boolean flag = false;
            try {
                if (list != null && list.size() > 0) {
                    for (Ordi ordi : list) {
                        ordLogistics.setOrdiId(ordi.getOrderItemId());
                        ordLogisticsDao.insert(ordLogistics);
                    }
                }
                flag = true;
            }catch (Exception e){
                flag = false;
            }

            addToResult(flag, resultObj);
        }
    }

    /**
     * 通过ID获得详情
     * @param resultObj
     * @param id
     */
    @IceServiceMethod
    public void getOrdLogisticsById(JsonObject resultObj, int id) {
        if (id > 0) {
            addToResult(ordLogisticsDao.getEntityById(id), resultObj);
        }
    }

    /**
     * 通过用户ID获得详情
     * @param resultObj
     * @param uid
     */
    @IceServiceMethod
    public void getOrdLogisticsByUid(JsonObject resultObj, int uid) {
        if (uid > 0) {
            OrdLogistics ordLogistics = new OrdLogistics();
            ordLogistics.setUid(uid);
            addToResult(ordLogisticsDao.getEntityByObj(ordLogistics), resultObj);
        }
    }

    /**
     * 根据订单id查找配送信息
     * @param resultObj
     * @param ordId
     */
    @IceServiceMethod
    public void getOrdLogisticsByOrdId(JsonObject resultObj,String ordId){
        if (!Strings.isNullOrEmpty(ordId)) {
            OrdLogistics ordLogistics = new OrdLogistics();
            ordLogistics.setOrderId(ordId);
            List<OrdLogistics> logist = ordLogisticsDao.getListByObj(ordLogistics);
            if(null != logist && logist.size() > 0){
                addToResult(logist.get(0), resultObj);
            }
        }
    }

    /**
     * 通过运单号获得运单列表。不同的物流公司可能存在相同的运单号！
     * @param resultObj
     * @param number
     */
    @IceServiceMethod
    public void getLogisticsListByNumber(JsonObject resultObj, String number){
        if (!Strings.isNullOrEmpty(number)) {
            OrdLogistics logistics = new OrdLogistics();
            logistics.setLogisticsNumber(number);
            addToResult(ordLogisticsDao.getListByObj(logistics), resultObj);
        }
    }

    /**
     * 通过物流公司名称获得运单列表
     * @param resultObj
     * @param company
     */
    @IceServiceMethod
    public void getLogisticsListByCompany(JsonObject resultObj, String company) {
        if (!Strings.isNullOrEmpty(company)) {
            OrdLogistics logistics = new OrdLogistics();
            logistics.setLogisticsCompany(company);
            addToResult(ordLogisticsDao.getListByObj(logistics), resultObj);
        }
    }

    /**
     * 通过物流公司和运单号获得运单列表
     * @param resultObj
     * @param company
     * @param number
     */
    @IceServiceMethod
    public void getLogisticsListByCompanyAndNum(JsonObject resultObj, String company, String number) {
        if (!Strings.isNullOrEmpty(company) && !Strings.isNullOrEmpty(number)) {
            OrdLogistics logistics = new OrdLogistics();
            logistics.setLogisticsCompany(company);
            logistics.setLogisticsNumber(number);
            addToResult(ordLogisticsDao.getListByObj(logistics), resultObj);
        }
    }

}
