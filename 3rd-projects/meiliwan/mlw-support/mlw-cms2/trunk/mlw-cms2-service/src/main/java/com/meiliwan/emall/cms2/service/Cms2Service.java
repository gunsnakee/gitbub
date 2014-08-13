package com.meiliwan.emall.cms2.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.cms2.bean.*;
import com.meiliwan.emall.cms2.dao.*;
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
 * Date: 13-12-24
 * Time: 下午5:05
 * To change this template use File | Settings | File Templates.
 */
@Service
public class Cms2Service extends DefaultBaseServiceImpl {

    private final  static MLWLogger logger = MLWLoggerFactory.getLogger(Cms2Service.class);

    @Autowired
    private CmsFragmentDao cmsFragmentDao;

    @Autowired
    private CmsLinkDao cmsLinkDao;

    @Autowired
    private CmsPageBlockDao cmsPageBlockDao;

    @Autowired
    private CmsPageDao cmsPageDao;

    @Autowired
    private CmsPositionDao cmsPositionDao;

    /************************************************ cms_fragment ***************************/
    public void addFragment(JsonObject jsonObject,CmsFragment cmsFragment){
        int result;
        result = cmsFragmentDao.insert(cmsFragment);
         //unique fragmentName
      /*  CmsFragment queryParam = new CmsFragment();
        queryParam.setFragmentName(cmsFragment.getFragmentName());

        CmsFragment resultFragment = cmsFragmentDao.getEntityByObj(queryParam,true);
        if(resultFragment == null){
          result = cmsFragmentDao.insert(cmsFragment);
        }else{
          result = -1;
          logger.warn("试图插入fragment_name相同的记录,fragment_name 为"+cmsFragment.getFragmentName(),null,null);
        }*/
        JSONTool.addToResult(result,jsonObject);
    }


    public void updateFragment(JsonObject jsonObject,CmsFragment cmsFragment){
        int result = cmsFragmentDao.update(cmsFragment);
        JSONTool.addToResult(result,jsonObject);
    }
    
    public void getFragmentByObj(JsonObject jsonObject,CmsFragment cmsFragment){
    		List<CmsFragment> list = cmsFragmentDao.getListByObj(cmsFragment, "", " order by id desc");
    		CmsFragment resultFragment=null;
    		if(list!=null&&list.size()>0){
    			resultFragment=list.get(0);
    		}
        JSONTool.addToResult(resultFragment,jsonObject);
    }

    public void getFragmentListByObj(JsonObject jsonObject,CmsFragment cmsFragment,PageInfo pageInfo){
        List<CmsFragment> resultList = cmsFragmentDao.getListByObj(cmsFragment,pageInfo,null);
        JSONTool.addToResult(resultList,jsonObject);
    }

   /****************************** cms_page ********************************************************/

   public void addPage(JsonObject jsonObject,CmsPage cmsPage){
       int result;
       CmsPage queryPage = new CmsPage();
       queryPage.setPageName(cmsPage.getPageName());
       CmsPage resultPage = cmsPageDao.getEntityByObj(queryPage,true);
       if(resultPage == null ){
           result = cmsPageDao.insert(cmsPage);
       }else{
           result = -1 ;
           logger.warn("试图插入page_name相同的页面,page_name 为"+queryPage.getPageName(),null,null);
       }
       JSONTool.addToResult(result,jsonObject);
   }


   public void getPageByObj(JsonObject jsonObject,CmsPage cmsPage){
       CmsPage resultPage = cmsPageDao.getEntityByObj(cmsPage);
       JSONTool.addToResult(resultPage,jsonObject);
   }

   public void getPageById(JsonObject jsonObject,Integer pageId){
       CmsPage resultPage = cmsPageDao.getEntityById(pageId);
       JSONTool.addToResult(resultPage,jsonObject);
   }

   public void updatePage(JsonObject jsonObject,CmsPage cmsPage){
       int result = cmsPageDao.update(cmsPage);
       JSONTool.addToResult(result,jsonObject);
   }

   public void getPageLists(JsonObject jsonObject,CmsPage cmsPage){
       List<CmsPage> list = cmsPageDao.getListByObj(cmsPage);
       JSONTool.addToResult(list,jsonObject);
   }

   public void blurSeachByPageName(JsonObject jsonObject,String pageName){
       List<CmsPage> list = cmsPageDao.blurSeachByPageName(pageName);
       JSONTool.addToResult(list,jsonObject);
   }


  /***************************************** cms_page_block ******************************************/

    public void addBlock(JsonObject jsonObject,CmsPageBlock cmsPageBlock){
       int result;
        CmsPageBlock queryCmsPageBlock = new CmsPageBlock();
        queryCmsPageBlock.setBlockName(cmsPageBlock.getBlockName());

        CmsPageBlock resultCmsPageBlock = cmsPageBlockDao.getEntityByObj(queryCmsPageBlock);
        if(resultCmsPageBlock == null){
            result = cmsPageBlockDao.insert(cmsPageBlock);
        }else{
            result = -1;
            logger.warn("试图插入block_name相同的块,block_name 为"+cmsPageBlock.getBlockName(),null,null);
        }
        JSONTool.addToResult(result,jsonObject);
    }

    public void getBlockByObj(JsonObject jsonObject,CmsPageBlock cmsPageBlock){
        CmsPageBlock resultCmsPageBlock = cmsPageBlockDao.getEntityByObj(cmsPageBlock);
        JSONTool.addToResult(resultCmsPageBlock,jsonObject);
    }

    public void getBlockById(JsonObject jsonObject,Integer blockId){
        CmsPageBlock resultCmsPageBlock = cmsPageBlockDao.getEntityById(blockId);
        JSONTool.addToResult(resultCmsPageBlock,jsonObject);
    }

    public void updateBlock(JsonObject jsonObject,CmsPageBlock cmsPageBlock){
        int result = cmsPageBlockDao.update(cmsPageBlock);
        JSONTool.addToResult(result,jsonObject);
    }

    public void getBlockListByObj(JsonObject jsonObject,CmsPageBlock cmsPageBlock){
        List<CmsPageBlock> blockList = cmsPageBlockDao.getListByObj(cmsPageBlock);
        JSONTool.addToResult(blockList,jsonObject);
    }

    /********************************************* cms_positon ****************************************************/
    public void addPosition(JsonObject jsonObject, CmsPosition cmsPosition){
        int result = cmsPositionDao.insert(cmsPosition);
        JSONTool.addToResult(result,jsonObject);
    }

    public void getPositionByObj(JsonObject jsonObject,CmsPosition cmsPosition){
        CmsPosition resultCmsPosition = cmsPositionDao.getEntityByObj(cmsPosition);
        JSONTool.addToResult(resultCmsPosition,jsonObject);
    }

    public void getPositionById(JsonObject jsonObject,Integer positionId){
        CmsPosition resultCmsPosition = cmsPositionDao.getEntityById(positionId);
        JSONTool.addToResult(resultCmsPosition,jsonObject);
    }

    public void updatePosition(JsonObject jsonObject, CmsPosition cmsPosition){
        int result = cmsPositionDao.update(cmsPosition);
        JSONTool.addToResult(result,jsonObject);
    }

    public void getPositionListByGroup(JsonObject jsonObject,Integer blockId, Integer groupId){
        CmsPosition cmsPosition = new CmsPosition();
        cmsPosition.setGroupId(groupId);
        cmsPosition.setBlockId(blockId);
        List<CmsPosition> list = cmsPositionDao.getListByObj(cmsPosition);
        JSONTool.addToResult(list,jsonObject);
    }

    public void getAllGroupByBlockId(JsonObject jsonObject,Integer blockId){
        List<Integer> groupList = cmsPositionDao.getAllGroupByBlockId(blockId);
        JSONTool.addToResult(groupList,jsonObject);
    }

    /**************************************** cms_link *****************************************************/
    public void addLink(JsonObject jsonObject, CmsLink cmsLink){
        int result = cmsLinkDao.insert(cmsLink);
        JSONTool.addToResult(result,jsonObject);
    }

    public void getLinkByObj(JsonObject jsonObject,CmsLink cmsLink){
        List<CmsLink> list = cmsLinkDao.getListByObj(cmsLink," 1=1","order by create_time desc");
        CmsLink link = null;
        if(list != null && list.size() > 0){
            link = list.get(0);
        }
        JSONTool.addToResult(link,jsonObject);
    }
    
    public void getLinkById(JsonObject jsonObject,int id){
        CmsLink cmsLink = cmsLinkDao.getEntityById(id);
        JSONTool.addToResult(cmsLink,jsonObject);
    }

    
    public void getLinkList(JsonObject jsonObject,Integer blockId, Integer groupId){
        CmsLink cmsLink = new CmsLink();
        cmsLink.setGroupId(groupId);
        cmsLink.setBlockId(blockId);
        List<CmsLink> list = cmsLinkDao.getListByObj(cmsLink," 1=1","order by create_time desc");
        JSONTool.addToResult(list,jsonObject);
    }

    public void getLinkList(JsonObject jsonObject,Integer blockId){
    		getLinkList(jsonObject,blockId,null);
    }
    
    
    public void updateLink(JsonObject jsonObject,CmsLink cmsLink){
        int result = cmsLinkDao.update(cmsLink);
        JSONTool.addToResult(result,jsonObject);
    }

    /*************************** publish ******************************/
    /**
     * 发布状态 1 已发布  0 未发布
     * @param jsonObject
     * @param cmsFragment
     */
    public void publish(JsonObject jsonObject,CmsFragment cmsFragment){
        boolean flag = false;

        CmsFragment queryParam = new CmsFragment();
        queryParam.setCacheCode(cmsFragment.getCacheCode());
        queryParam.setIsPublished(1);
        List<CmsFragment> cmsFragmentList = cmsFragmentDao.getListByObj(queryParam);
        if(cmsFragmentList != null && cmsFragmentList.size() > 0){
            for(CmsFragment item:cmsFragmentList){
            	item.setIsPublished(0);
            	cmsFragmentDao.update(item);
            }
        }
        
        //插入到cms_fragment
        int result = cmsFragmentDao.insert(cmsFragment);
		flag = result > 0 ? true : false;
        JSONTool.addToResult(flag,jsonObject);
    }

}
