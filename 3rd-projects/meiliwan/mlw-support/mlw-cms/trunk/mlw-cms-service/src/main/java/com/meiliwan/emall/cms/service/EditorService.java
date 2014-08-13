package com.meiliwan.emall.cms.service;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.cms.bean.*;
import com.meiliwan.emall.cms.dao.*;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 编辑、预览业务层
 * User: wuzixin
 * Date: 13-6-18
 * Time: 下午8:58
 */
@Service
public class EditorService extends DefaultBaseServiceImpl {
    @Autowired
    private CmsSiteDao cmsSiteDao;

    @Autowired
    private CmsLinkDao cmsLinkDao;

    @Autowired
    private CmsPageDao cmsPageDao;
    
    @Autowired
    private CmsPositionDao cmsPositionDao;

    @Autowired
    private CmsBlockDao cmsBlockDao;

    @IceServiceMethod
    public void deleteBySiteId(JsonObject resultObj, int siteId) {
        int count = cmsSiteDao.deleteBySiteId(siteId);
        JSONTool.addToResult(count, resultObj);
    }

    @IceServiceMethod
    public void addCmsLink(JsonObject resultObj,CmsLink cmsLink){
        int result=cmsLinkDao.insert(cmsLink);
        JSONTool.addToResult(result, resultObj);
    }

    /**
     * 根据 pageId linkId 更新link
     * @param resultObj
     * @param cmsLink
     */
    @IceServiceMethod
    public void updateCmsLink(JsonObject resultObj,CmsLink cmsLink){
        if(cmsLink.getPageId()!=null && cmsLink.getId()!=null) {
            int result=cmsLinkDao.update(cmsLink);
            JSONTool.addToResult(result, resultObj);
        }
    }

    /**
     * 根据 pageId linkId 删除 link
     * @param resultObj
     * @param cmsLink
     */
    @IceServiceMethod
    public void delCmsLink(JsonObject resultObj,CmsLink cmsLink){
        if(cmsLink.getPageId()!=null && cmsLink.getId()!=null){
            int result=cmsLinkDao.del(cmsLink.getId(),cmsLink.getPageId());
            JSONTool.addToResult(result, resultObj);
        }
    }

    @IceServiceMethod
   public void getCmsPageById(JsonObject resultObj,Integer pageId){
       if(pageId!=null){
          CmsPage result=cmsPageDao.getCmsPageById(pageId,false);
          JSONTool.addToResult(result, resultObj);
       }
   }

    @IceServiceMethod
   public void getPositionByBlock(JsonObject resultObj,int blockId,int pageId,String time){
       List<CmsPosition> positionList=cmsPositionDao.getByBlockId(blockId,pageId,false);
       List<CmsLink> links = this.cmsLinkDao.getListByPageId(time, pageId,false);
       Map<Integer,List<CmsLink>> positionMap = this.getPositionMap(links);
       Map<Integer, CmsPosition> map = new TreeMap<Integer, CmsPosition>();
       if(positionList!=null){
           for (CmsPosition p : positionList) {
               int pid = p.getPositionId();
               List<CmsLink> linkList = getPositionContent(positionMap.get(pid), pid);
               p.setLinkList(linkList);
               map.put(pid, p);
           }
       }
       JSONTool.addToResult(new Gson().toJson(map), resultObj);
   }

    @IceServiceMethod
   public void getBlockByPageId(JsonObject resultObj,int pageId){
       List<CmsBlock> blockList=this.cmsBlockDao.getByPageId(pageId);
       JSONTool.addToResult(blockList, resultObj);
   }

    @IceServiceMethod
    public void getLinkByLinkId(JsonObject resultObj,int linkId, int pageId){
        CmsLink linkList=this.cmsLinkDao.get(linkId,pageId,false);
        JSONTool.addToResult(linkList, resultObj);

    }

    @IceServiceMethod
    public void getPositionById(JsonObject resultObj,int positionId,int pageId,String time){
        CmsPosition position=new CmsPosition();
        List<CmsLink> linkList=this.cmsLinkDao.getListByPositionId(positionId,pageId,time,false);
        position.setLinkList(linkList);

        JSONTool.addToResult(position, resultObj);
    }

    @IceServiceMethod
    public void getSite(JsonObject resultObj,int siteId){
        CmsSite site=this.cmsSiteDao.getSiteBySId(siteId,false);
        JSONTool.addToResult(site, resultObj);
    }


    @IceServiceMethod
    public void addCmsBlock(JsonObject resultObj, CmsBlock cmsBlock){
        int result=this.cmsBlockDao.insert(cmsBlock);
        JSONTool.addToResult(result, resultObj);
    }

    @IceServiceMethod
    public void addCmsPosition(JsonObject resultObj,CmsPosition cmsPosition){
        int result=this.cmsPositionDao.insert(cmsPosition);
        JSONTool.addToResult(result, resultObj);
    }

    @IceServiceMethod
    public void  updatePageStatus(JsonObject resultObj,int pageId,Date time){
        int result=0;
        List<CmsBlock> blocks = this.cmsBlockDao.getByPageId(pageId);
        List<Integer> publishList = new ArrayList<Integer>();
        for (CmsBlock block : blocks) {
            List<CmsPosition> positions = this.cmsPositionDao.getByBlockId(block.getBlockId(),
                    block.getPageId(),true);
            for (CmsPosition position : positions) {
                CmsLink link = this.cmsLinkDao.getByPositionId(position.getPositionId(), time,
                        position.getPageId(),true);
                if (link!=null && link.getStatus() != 1) {
                    publishList.add(link.getLinkId());
                }
            }

        }
        if (publishList!=null && publishList.size() > 0) {
            for(Integer linkId:publishList){
                result= this.cmsLinkDao.updateStatus(linkId, pageId);
                if(result <=0 ) break;
            }
        }
        JSONTool.addToResult(result, resultObj);
    }

    //私有方法
    private Map<Integer,List<CmsLink>> getPositionMap(List<CmsLink> links){
        Map<Integer,List<CmsLink>> map = new HashMap<Integer,List<CmsLink>>();
        for(CmsLink link : links){
            int pid = link.getPositionId();
            List<CmsLink> list = map.get(pid);
            if( list == null ){
                List<CmsLink> l = new ArrayList<CmsLink>();

                l.add(link);
                map.put(pid, l);
            }else{
                list.add(link);
            }
        }
        return map;
    }

    private List<CmsLink> getPositionContent(List<CmsLink> links, int positionId) {
        List<CmsLink> l = new ArrayList<CmsLink>();
        List<CmsLink> carouses = new ArrayList<CmsLink>();
        if(links !=null && links.size()>0){
            for (CmsLink link : links) {
                if (link.getPositionId() == positionId) {
                    //首先取处于 固定 状态的link记录
//                    if (link.getFixFlag() == 1) {
//                        l.add(link);
//                        return l;
//                        //如果第一种没取到数据 则按位置取轮播取数据
//                    } else if (link.getRollFlag() == 1) {
//                        carouses.add(link);
//                    }
                    l.add(link);
                }
            }
        }
        //如果第二种没取到轮播数据 则取最近的一条数据
        if (carouses.size() == 0 && l.size() > 0) {
            carouses.add(l.get(0));
            //如果数据库里面没数据，则设置默认值
        } else if (carouses.size() == 0 && l.size() == 0) {
            CmsLink newLink = new CmsLink();
            newLink.setPositionId(positionId);
            newLink.setLinkName("请添加内容!");
            newLink.setLinkDesc("请添加内容!");
            carouses.add(newLink);
        }
        return carouses;
    }
}
