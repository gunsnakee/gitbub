package com.meiliwan.emall.pms.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.pms.bean.ProPlace;
import com.meiliwan.emall.pms.dao.ProPlaceDao;
import com.meiliwan.emall.service.BaseService;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * User: guangdetang
 * Date: 13-6-3
 * Time: 下午2:52
 */
@Service
public class ProPlaceService extends DefaultBaseServiceImpl implements BaseService {

    @Autowired
    private ProPlaceDao proPlaceDao;

    /**
     * 根据产地ID获得产地对象
     *
     * @param placeId
     * @param resultObj
     */
    @IceServiceMethod
    public void getProPlaceById(JsonObject resultObj, int placeId) {
        if (placeId > 0) {
            addToResult(proPlaceDao.getEntityById(placeId), resultObj);
        }
    }

    /**
     * 获得所有未删除的商品产地分页列表。
     * 运营后台列表管理
     *
     * @param place
     * @param pageInfo
     * @param resultObj
     */
    @IceServiceMethod
    public void getAllProPlacePager(JsonObject resultObj, ProPlace place, PageInfo pageInfo) {
        if (place != null && pageInfo != null) {
            addToResult(proPlaceDao.getPagerByObj(place, pageInfo, null, "order by create_time desc"), resultObj);
        }
    }

    /**
     * 获得所有未删除的商品产地list列表。
     * 用于添加商品的时候查询出所有商品产地下拉列表
     *
     * @param place
     * @param resultObj
     */
    @IceServiceMethod
    public void getAllProPlaceList(JsonObject resultObj, ProPlace place) {
        if (place != null) {
            place.setIsDel((int) GlobalNames.STATE_VALID);
            addToResult(proPlaceDao.getListByObj(place, "", "order by create_time desc"), resultObj);
        }
    }

    /**
     * 添加商品产地
     *
     * @param place
     * @param resultObj
     */
    @IceServiceMethod
    public void saveProPlace(JsonObject resultObj, ProPlace place) {
        if (place != null) {
            place.setCreateTime(DateUtil.getCurrentTimestamp());
            int result = proPlaceDao.insert(place);
            addToResult(result > 0 ? true : false, resultObj);
        }
    }

    /**
     * 修改商品产地
     *
     * @param place
     * @param resultObj
     */
    @IceServiceMethod
    public void updateProPlace(JsonObject resultObj, ProPlace place) {
        if (place != null) {
            int result = proPlaceDao.update(place);
            addToResult(result > 0 ? true : false, resultObj);
        }
    }
}
