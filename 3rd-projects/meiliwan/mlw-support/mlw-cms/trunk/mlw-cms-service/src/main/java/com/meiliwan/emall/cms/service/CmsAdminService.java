package com.meiliwan.emall.cms.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.cms.bean.*;
import com.meiliwan.emall.cms.dao.*;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * User: wuzixin
 * Date: 13-6-20
 * Time: 下午2:07
 */
@Service
public class CmsAdminService extends DefaultBaseServiceImpl {
    @Autowired
    private CmsPageDao cmsPageDao;
    @Autowired
    private CmsTemplateDao cmsTemplateDao;
    @Autowired
    private CmsTemplateBlockDao cmsTemplateBlockDao;
    @Autowired
    private CmsTemplatePositionDao cmsTemplatePositionDao;
    @Autowired
    private CmsTemplateLinkDao cmsTemplateLinkDao;
    @Autowired
    private CmsBlockDao cmsBlockDao;
    @Autowired
    private CmsPositionDao cmsPositionDao;
    @Autowired
    private CmsLinkDao cmsLinkDao;
    /**
     * 根据 站点ID 获取页面列表
     * @param siteId
     */
    @IceServiceMethod
    public void getPageBySiteId(JsonObject resultObj,int siteId){
        CmsPage page = new CmsPage();
        page.setSiteId(siteId);
        List<CmsPage> list = cmsPageDao.getListByObj(page,null);
        addToResult(list,resultObj);
    }

    /**
     * 获取所有模板
     * @param resultObj
     */
    @IceServiceMethod
    public void getAllTemplate(JsonObject resultObj) {
        addToResult(this.cmsTemplateDao.getAllEntityObj(),resultObj);
    }

    @IceServiceMethod
    public void addCmsPage(JsonObject resultObj,CmsPage page){
        cmsPageDao.insert(page);
        if (page.getPageId()>0){
            addToResult(page.getPageId(),resultObj);
        }else {
            addToResult(0,resultObj);
        }
    }

    @IceServiceMethod
    public void templateInsert(JsonObject resultObj,int pageId, int templateId) {
        List<CmsTemplateBlock> blocks = this.cmsTemplateBlockDao.getListByTmId(templateId,true);
        for (CmsTemplateBlock tmBlock : blocks) {
            CmsBlock block = new CmsBlock();
            block.setPageId(pageId);
            if (this.cmsBlockDao.insert(exBlock(tmBlock, block)) == 0) {
                addToResult(0,resultObj);
            }
        }

        List<CmsTemplatePosition> positions = this.cmsTemplatePositionDao.getListByTmId(templateId,true);
        for (CmsTemplatePosition tmPosition : positions) {
            CmsPosition position = new CmsPosition();
            position.setPageId(pageId);
            if (this.cmsPositionDao.insert(exPosition(tmPosition, position)) == 0) {
                addToResult(0,resultObj);            }
        }

        List<CmsTemplateLink> links = this.cmsTemplateLinkDao.getListByTmId(templateId,true);
        for (CmsTemplateLink tmLink : links) {
            CmsLink link = new CmsLink();
            link.setPageId(pageId);
            if (this.cmsLinkDao.insert(exLink(tmLink, link)) == 0) {
                addToResult(0,resultObj);
            }
        }
        addToResult(1,resultObj);
    }


    public static CmsBlock exBlock(CmsTemplateBlock tmBlock,CmsBlock block){
        block.setBlockId(tmBlock.getBlockId());
        block.setBlockName(tmBlock.getBlockName());
        return block;
    }

    public static CmsPosition exPosition(CmsTemplatePosition tmPosition,CmsPosition position){
        position.setBlockId(tmPosition.getBlockId());
        position.setPositionName(tmPosition.getPositionName());
        position.setPositionId(tmPosition.getPositionId());
        position.setPositionType(tmPosition.getPositionType());
        position.setCarouseFlag(tmPosition.getCarouseFlag());
        return position;
    }

    public static CmsLink exLink(CmsTemplateLink tmLink,CmsLink link){
        link.setPositionId(tmLink.getPositionId());
        link.setLinkName(tmLink.getLinkName());
        link.setLinkUrl(tmLink.getLinkUrl());
        link.setLinkDesc(tmLink.getLinkDesc());
        link.setPicUrl(tmLink.getPicUrl());
        link.setBeginTime(tmLink.getBeginTime());
        link.setEndTime(tmLink.getEndTime());
        link.setModifyTime(tmLink.getUpdateTime());
        link.setModifyUser(tmLink.getUpdateUser());
        link.setOpenFlag(tmLink.getOpenFlag());
        link.setMlwPrice(tmLink.getMlwPrice());
        link.setMarketPrice(tmLink.getMarketPrice());
        link.setSaleNum(tmLink.getSaleNum());
        link.setComNum(tmLink.getComNum());
        link.setScore(tmLink.getScore());
        link.setHideFlag(tmLink.getHideFlag());
        link.setOnlineFlag(tmLink.getOnlineFlag());
        link.setMark(tmLink.getMark());
        return link;
    }
}
