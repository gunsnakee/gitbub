package com.meiliwan.emall.base.client;

import java.util.Date;

import com.meiliwan.emall.base.bean.CmsFragment;

/**
 * Created by wenlepeng on 13-8-22.
 */
public class CmsFragmentClientTest {

    public static void main(String [] args){

        CmsFragment cmsFragment =new CmsFragment();
        cmsFragment.setPageId(9824);
        cmsFragment.setAdminId(12);
        cmsFragment.setContent("test");
        cmsFragment.setCreateTime(new Date());

        System.out.println(CmsFragmentServiceClient.getCmsFragmentByPageId(9824));

    }
}
