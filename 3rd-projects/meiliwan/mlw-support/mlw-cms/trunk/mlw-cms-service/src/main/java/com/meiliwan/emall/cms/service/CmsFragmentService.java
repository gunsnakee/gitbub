package com.meiliwan.emall.cms.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.cms.bean.CmsFragment;
import com.meiliwan.emall.cms.dao.CmsFragmentDao;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wenlepeng on 13-8-22.
 */
@Service
public class CmsFragmentService extends DefaultBaseServiceImpl {

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
     * 插入新的碎片页面
     * @param jsonObject
     * @param newCmsFragment
     */
    public void addFragment(JsonObject jsonObject,CmsFragment newCmsFragment){
        Integer result=cmsFragmentDao.insert(newCmsFragment);
        JSONTool.addToResult(result,jsonObject);
    }

}
