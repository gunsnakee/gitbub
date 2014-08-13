package com.meiliwan.emall.base.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.base.bean.CmsFragment;
import com.meiliwan.emall.base.dao.CmsFragmentDao;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

/**
 * Created by wenlepeng on 13-8-22.
 */
@Service
public class CmsFragmentService extends DefaultBaseServiceImpl {
//    private  final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    @Autowired
    private CmsFragmentDao cmsFragmentDao;


    /**
     * 根据页面Id获取最新的碎片
     * @param jsonObject
     * @param pageId
     */
    public void getLatestFragmentByPageId(JsonObject jsonObject,Integer pageId){
        String content="";
        if(pageId > 0){
            CmsFragment queryCmsFragment=new CmsFragment();
            queryCmsFragment.setPageId(pageId);
            List<CmsFragment> list=cmsFragmentDao.getListByObj(queryCmsFragment,new PageInfo(1,1),null," order by create_time desc ");
            if(list != null && list.size() > 0){
                content=list.get(0).getContent();
            }
        }

        JSONTool.addToResult(content,jsonObject);
    }

    /**
     * 根据页面Id获取最新的碎片
     * @param jsonObject
     * @param pageId
     */
    public void getLatestFragmentByPageName(JsonObject jsonObject,String pageName){
        String content="";

        JSONTool.addToResult(content,jsonObject);
    }
    
}
