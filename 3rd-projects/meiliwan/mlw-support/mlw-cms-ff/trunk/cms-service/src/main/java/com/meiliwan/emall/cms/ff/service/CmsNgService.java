package com.meiliwan.emall.cms.ff.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.cms.ff.bean.*;
import com.meiliwan.emall.cms.ff.dao.*;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-12-18
 * Time: 下午6:40
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CmsNgService extends DefaultBaseServiceImpl {
    private final static MLWLogger logger= MLWLoggerFactory.getLogger(CmsNgService.class);

    @Autowired
    private BlockDao blockDao;

    @Autowired
    private DependentRelationDao dependentRelationDao;

    @Autowired
    private FragmentDao fragmentDao;

    @Autowired
    private OptLogDao optLogDao;

    @Autowired
    private PageDao pageDao;

    @Autowired
    private TemplateDao templateDao;


    /******************************************************* Template ************************************/

    /**
     * add template
     * @param jsonObject
     * @param template
     */
    public void addTemplate(JsonObject jsonObject,Template template){
        int result =0;
        Template queryParam = new Template();
        queryParam.setTemplateName(template.getTemplateName());
        Template queryResult = templateDao.getEntityByObj(queryParam);
        if(queryResult == null){
            result= templateDao.insert(template);
        }
        JSONTool.addToResult(result,jsonObject);
    }

    /**
     * get template by object
     * @param jsonObject
     * @param template
     */
    public void getTemplateByObj(JsonObject jsonObject,Template template){
        Template resultObj = templateDao.getEntityByObj(template);
        JSONTool.addToResult(resultObj,jsonObject);
    }

    /**
     * get template by id
     * @param jsonObject
     * @param id
     */
    public void getTemplateById(JsonObject jsonObject,Integer id){
        Template resultObj = templateDao.getEntityById(id);
        JSONTool.addToResult(resultObj,jsonObject);
    }


    /**
     * delete template
     * @param jsonObject
     * @param id
     */
    public void delTemplateById(JsonObject jsonObject,Integer id){
        int result = templateDao.delete(id);
        JSONTool.addToResult(result,jsonObject);
    }

    /**
     * update template by id
     * @param jsonObject
     * @param template
     */
    public void updateTemplateById(JsonObject jsonObject,Template template){
        int result = templateDao.update(template);
        JSONTool.addToResult(result,jsonObject);
    }


    /**
     * get lists of template by page
     * @param jsonObject
     * @param pageInfo
     * @param template
     */
    public void getTemplateByPage(JsonObject jsonObject,PageInfo pageInfo,Template template){
        List<Template> resultList = templateDao.getListByObj(template,pageInfo,null,"order by template_name");
        JSONTool.addToResult(resultList,jsonObject);
    }

    /***************************************** Block *************************************************/

    /**
     * add block, the block name is unique
     * @param jsonObject
     * @param block
     */
    public void addBlock(JsonObject jsonObject,Block block){
        int result =0;
        Block queryParam = new Block();
        queryParam.setBlockName(block.getBlockName());
        Block queryResult = blockDao.getEntityByObj(queryParam);
        if(queryResult == null){
            result= blockDao.insert(block);
        }
        JSONTool.addToResult(result,jsonObject);
    }

    /**
     * query block by object
     * @param jsonObject
     * @param block
     */
    public void getBlockByObj(JsonObject jsonObject,Block block){
        Block resultObj = blockDao.getEntityByObj(block);
        JSONTool.addToResult(resultObj,jsonObject);
    }

    /**
     * query block by id
     * @param jsonObject
     * @param id
     */
    public void getBlockById(JsonObject jsonObject,Integer id){
        Block resultObj = blockDao.getEntityById(id);
        JSONTool.addToResult(resultObj,jsonObject);
    }

    /**
     * delete block by id
     * @param jsonObject
     * @param id
     */
    public void delBlockById(JsonObject jsonObject,Integer id){
        int result = blockDao.delete(id);
        JSONTool.addToResult(result,jsonObject);
    }

    /**
     * update block by id
     * @param jsonObject
     * @param block
     */
    public void updateBlockById(JsonObject jsonObject,Block block){
        int result = blockDao.update(block);
        JSONTool.addToResult(result,jsonObject);
    }


    /**
     * get lists of block by page
     * @param jsonObject
     * @param pageInfo
     * @param block
     */
    public void getBlockByPage(JsonObject jsonObject,PageInfo pageInfo,Block block){
        List<Block> resultList = blockDao.getListByObj(block,pageInfo,null,"order by block_name");
        JSONTool.addToResult(resultList,jsonObject);
    }


    /*******************Page ******************************/


    /**
     * add page
     * @param jsonObject
     * @param page
     */
    public void addPage(JsonObject jsonObject,Page page){
        int result = pageDao.insert(page);
        JSONTool.addToResult(result,jsonObject);
    }

    /**
     * get page by id
     * @param jsonObject
     * @param id
     */
    public void getPageById(JsonObject jsonObject,Integer id){
        Page resultPage = pageDao.getEntityById(id);
        JSONTool.addToResult(resultPage,jsonObject);
    }

    /**
     * get page by object
     * @param jsonObject
     * @param page
     */
    public void getPageByObj(JsonObject jsonObject,Page page){
        Page resultPage = pageDao.getEntityByObj(page);
        JSONTool.addToResult(resultPage,jsonObject);
    }


    /**
     * delete page by id
     * @param jsonObject
     * @param id
     */
    public void delPageById(JsonObject jsonObject,Integer id){
        int result = pageDao.delete(id);
        JSONTool.addToResult(result,jsonObject);
    }


    /**
     * update page by id
     * @param jsonObject
     * @param page
     */
    public void updatePageById(JsonObject jsonObject,Page  page){
        int result = pageDao.update(page);
        JSONTool.addToResult(result,jsonObject);
    }

    /**
     * get lists of page by page
     * @param jsonObject
     * @param pageInfo
     * @param page
     */
    public void getPageByPage(JsonObject jsonObject,PageInfo pageInfo,Page page){
        List<Page> resultList = pageDao.getListByObj(page,pageInfo,null,"order by page_name");
        JSONTool.addToResult(resultList,jsonObject);
    }

}
