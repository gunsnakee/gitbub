package com.meiliwan.emall.base.service;

import java.util.Date;

import com.google.gson.JsonObject;
import com.meiliwan.emall.base.bean.BaseHelpPages;
import com.meiliwan.emall.base.dao.BaseHelpPagesDao;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-12-6
 * Time: 上午11:19
 */
@Service
public class BaseHelpPagesService extends DefaultBaseServiceImpl {
    private  final static MLWLogger logger = MLWLoggerFactory.getLogger(BaseHelpPagesService.class);

    @Autowired
    private BaseHelpPagesDao baseHelpPagesDao;


    public void getHelpPageById(JsonObject jsonObject,Integer id){
        BaseHelpPages page = baseHelpPagesDao.getEntityById(id);
        JSONTool.addToResult(page,jsonObject);
    }


    public void getHelpPageByShortUrl(JsonObject jsonObject,String shortUrl){
        BaseHelpPages queryObj = new BaseHelpPages();
        queryObj.setShortUrl(shortUrl);
        BaseHelpPages page = baseHelpPagesDao.getEntityByObj(queryObj);
        JSONTool.addToResult(page,jsonObject);
    }

    public void updateHelpPageById(JsonObject jsonObject,BaseHelpPages obj){
        obj.setUpdateTime(new Date());
        int result = baseHelpPagesDao.update(obj);
        JSONTool.addToResult(result,jsonObject);
    }

    public void addHelpPage(JsonObject jsonObject,BaseHelpPages obj){
        int result = 0;
        BaseHelpPages pageByUrl = new BaseHelpPages();
        pageByUrl.setShortUrl(obj.getShortUrl());
        BaseHelpPages queryObj = baseHelpPagesDao.getEntityByObj(pageByUrl,true);
        if(queryObj == null){
        	Date currDate = new Date();
            obj.setCreateTime(currDate);
            obj.setUpdateTime(currDate);
            result = baseHelpPagesDao.insert(obj);
        }else{
            logger.warn("使用者试图插入有相同 url 的帮助页面",null,null);
        }
        JSONTool.addToResult(result,jsonObject);
    }
}
