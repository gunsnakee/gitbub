package com.meiliwan.emall.cms2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meiliwan.emall.cms2.dao.CmsFragmentDao;
import com.meiliwan.emall.cms2.dao.CmsLinkDao;
import com.meiliwan.emall.cms2.dao.CmsPageBlockDao;
import com.meiliwan.emall.cms2.dao.CmsPageDao;
import com.meiliwan.emall.cms2.dao.CmsPositionDao;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

@Service
public class ViewEditService extends DefaultBaseServiceImpl{

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
    
}
