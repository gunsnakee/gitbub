package com.meiliwan.emall.pms.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.pms.bean.ProLocation;
import com.meiliwan.emall.pms.dao.ProLocationDao;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * 商品存储位置业务层
 * User: wuzixin
 * Date: 13-11-15
 * Time: 下午4:20
 */
@Service
public class ProLocationService extends DefaultBaseServiceImpl {

    @Autowired
    private ProLocationDao proLocationDao;

    /**
     * 增加商品的存储位置
     *
     * @param resultObj
     * @param location
     */
    public void addProLocation(JsonObject resultObj, ProLocation location) {
        addToResult(proLocationDao.insert(location), resultObj);
    }

    /**
     * 修改商品的存储位置
     *
     * @param resultObj
     * @param location
     */
    public void updateProLocation(JsonObject resultObj, ProLocation location) {
        int count = proLocationDao.update(location);
        if (count > 0) {
            addToResult(true, resultObj);
        } else {
            addToResult(false, resultObj);
        }
    }

    /**
     * 根据pk获取商品存储位置
     *
     * @param resultObj
     * @param locationId
     */
    public void getLocationById(JsonObject resultObj, int locationId) {
        addToResult(proLocationDao.getEntityById(locationId), resultObj);
    }

    /**
     * 获取查询列表
     *
     * @param resultObj
     * @param dto
     * @param pageInfo
     * @param order_name
     * @param order_form
     */
    public void pageByObj(JsonObject resultObj, ProLocation dto, PageInfo pageInfo, String order_name, String order_form) {

        if (!StringUtils.isEmpty(dto.getProName())) {
            dto.setProName("%" + dto.getProName() + "%");
        }
        StringBuilder orderBySql = new StringBuilder();
        if (!StringUtils.isEmpty(order_name) && !StringUtils.isEmpty(order_form)) {
            orderBySql.append(" order by ").append(order_name)
                    .append(" ").append(order_form);
        }

        PagerControl<ProLocation> pages = proLocationDao.getPagerByObj(dto, pageInfo, null, orderBySql.toString());
        addToResult(pages, resultObj);
    }

    /**
     * 通过商品条形码获取商品存储位置
     *
     * @param resultObj
     * @param codes
     */
    public void getListByBarCode(JsonObject resultObj, String[] codes) {
        List<ProLocation> list = null;
        if (codes != null && codes.length > 0) {
            list = proLocationDao.getListByBarCode(codes);
        }
        addToResult(list, resultObj);
    }

    /**
     * 根据商品条形码修改商品对应的储位,用于导入商品储位
     *
     * @param resultObj
     * @param locations
     */
    public void updateLocationByBarcode(JsonObject resultObj, ProLocation[] locations) {
        boolean suc = false;
        if (locations != null && locations.length > 0) {
            for (ProLocation location : locations) {
                if (!StringUtils.isEmpty(location.getBarCode()) && !StringUtils.isEmpty(location.getLocationName())) {
                    ProLocation lc = proLocationDao.getLocationByBarCode(location.getBarCode());
                    if (lc != null) {
                        //存在进行修改
                        proLocationDao.updateLocationByBarcode(location.getBarCode(), location.getLocationName());
                    } else {
                        //不存在进行增加
                        ProLocation lt = new ProLocation();
                        lt.setBarCode(location.getBarCode());
                        lt.setLocationName(location.getLocationName());
                        proLocationDao.insert(lt);
                    }

                }
            }
            suc = true;
        }
        addToResult(suc, resultObj);
    }

    /**
     * 根据商品条形码获取商品对应的储位
     *
     * @param resultObj
     * @param barCode
     */
    public void getLocationByBarCode(JsonObject resultObj, String barCode) {
        ProLocation location = null;
        if (!StringUtils.isEmpty(barCode)) {
            location = proLocationDao.getLocationByBarCode(barCode);
        }
        addToResult(location, resultObj);
    }
}
