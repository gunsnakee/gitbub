package com.meiliwan.emall.base.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.base.bean.BaseMsgTemplate;
import com.meiliwan.emall.base.dao.BaseMsgTemplateDao;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * 基本消息发送模板
 * User: wuzixin
 * Date: 14-5-28
 * Time: 下午5:40
 */
@Service
public class BaseMsgTemplateService extends DefaultBaseServiceImpl {

    @Autowired
    private BaseMsgTemplateDao templateDao;

    /**
     * 修改发送消息的内容模板
     *
     * @param resultObj
     * @param template
     */
    public void update(JsonObject resultObj, BaseMsgTemplate template) {
        int count = templateDao.update(template);
        addToResult(count, resultObj);
    }

    /**
     * 根据模板ID获取消息发送信息
     *
     * @param resultObj
     * @param tmpId
     */
    public void getTmpById(JsonObject resultObj, int tmpId) {
        addToResult(templateDao.getEntityById(tmpId), resultObj);
    }

    /**
     * 根据消息模板类型获取消息模板列表
     *
     * @param resultObj
     * @param tmpType   模板类型
     */
    public void getListByType(JsonObject resultObj, int tmpType) {
        List<BaseMsgTemplate> list = null;
        BaseMsgTemplate tmp = new BaseMsgTemplate();
        tmp.setTmpType(tmpType);
        list = templateDao.getListByObj(tmp);

        addToResult(list, resultObj);
    }


    /**
     * 根据消息模板类型和内容类型获取消息模板
     *
     * @param resultObj
     * @param tmpType   模板类型
     * @param contType  内容类型
     */
    public void getMsgByType(JsonObject resultObj, int tmpType, int contType) {
        BaseMsgTemplate tmp = new BaseMsgTemplate();
        tmp.setTmpType(tmpType);
        tmp.setContentType(contType);
        addToResult(templateDao.getEntityByObj(tmp), resultObj);
    }

}
