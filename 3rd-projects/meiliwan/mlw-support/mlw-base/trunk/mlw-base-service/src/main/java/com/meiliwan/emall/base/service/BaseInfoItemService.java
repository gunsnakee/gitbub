package com.meiliwan.emall.base.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.base.bean.BaseInfoContent;
import com.meiliwan.emall.base.bean.BaseInfoItem;
import com.meiliwan.emall.base.dao.BaseInfoContentDao;
import com.meiliwan.emall.base.dao.BaseInfoItemDao;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * 资讯类别 业务层
 * User: wuzixin
 * Date: 13-6-13
 * Time: 下午6:30
 */
@Service
public class BaseInfoItemService extends DefaultBaseServiceImpl {

    @Autowired
    private BaseInfoItemDao baseInfoItemDao;
    @Autowired
    private BaseInfoContentDao baseInfoContentDao;

    /**
     * 增加资讯类别
     *
     * @param resultObj
     * @param binfoT
     */
    @IceServiceMethod
    public void addBaseInfoItem(JsonObject resultObj, BaseInfoItem binfoT) {
        if (binfoT != null) {
            int result = baseInfoItemDao.insert(binfoT);
            addToResult(result > 0 ? true : false, resultObj);
        } else {
            addToResult(false, resultObj);
        }
    }

    /**
     * 修改资讯类别
     *
     * @param resultObj
     * @param binfoT
     */
    @IceServiceMethod
    public void updateBaseInfoItem(JsonObject resultObj, BaseInfoItem binfoT) {
        if (binfoT != null) {
            try {
                baseInfoItemDao.update(binfoT);
                addToResult(true, resultObj);
            } catch (Exception e) {
                addToResult(false, resultObj);
                throw new ServiceException("service-base-BaseInfoItemService.updateBaseInfoItem:{}", binfoT == null ? "" : binfoT.toString(), e);
            }
        } else {
            addToResult(false, resultObj);
        }
    }

    public void deleteBaseInfoItem(JsonObject resultObj, int itemId) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPagesize(1);
        pageInfo.setStartIndex(0);
        BaseInfoContent content = new BaseInfoContent();
        content.setInfoItemId(itemId);
        List<BaseInfoContent> contents = baseInfoContentDao.getListByObj(content, pageInfo, null,true);
        if (contents.size() > 0) {
            addToResult(false, resultObj);
        } else {
            try {
                baseInfoItemDao.delete(itemId);
                addToResult(true, resultObj);
            } catch (Exception e) {
                addToResult(false, resultObj);
                throw new ServiceException("service-base-BaseInfoItemService.delBaseInfoItem:{}", itemId + "", e);
            }
        }
    }

    /**
     * 通过ID获取资讯类别
     *
     * @param resultObj
     * @param itemId
     */
    @IceServiceMethod
    public void getBaseInfoItemById(JsonObject resultObj, int itemId) {
        BaseInfoItem baseInfoItem = baseInfoItemDao.getEntityById(itemId);
        addToResult(baseInfoItem, resultObj);
    }

    /**
     * 获取资讯类别列表
     *
     * @param resultObj
     * @param baseIt
     */
    @IceServiceMethod
    public void getListByPager(JsonObject resultObj, BaseInfoItem baseIt, PageInfo pageInfo) {
        PagerControl<BaseInfoItem> baseList = baseInfoItemDao.getPagerByObj(baseIt, pageInfo, null, "order by update_time desc");
        addToResult(baseList, resultObj);
    }

    @IceServiceMethod
    public void getListByBaseIT(JsonObject resultObj, BaseInfoItem baseIT) {
        addToResult(baseInfoItemDao.getListByObj(baseIT, null), resultObj);
    }
}
