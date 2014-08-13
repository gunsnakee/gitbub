package com.meiliwan.emall.union.service;

import com.meiliwan.emall.union.dao.EtaoSearchDao;

import java.util.List;

/**
 * User: wuzixin
 * Date: 14-5-14
 * Time: 下午3:25
 */
public class EtaoSearchService {

    private EtaoSearchDao searchDao;

    public EtaoSearchDao getSearchDao() {
        return searchDao;
    }

    public void setSearchDao(EtaoSearchDao searchDao) {
        this.searchDao = searchDao;
    }

    public void getFullIndex() {
        List<String> ids = searchDao.getFullIndex();
        if (ids != null && ids.size() > 0) {
            searchDao.getXmlItems(ids);
        }
    }

    public void getIncrementIndex() {
        List<String> ids = searchDao.getIncrementIndex();
        if (ids != null && ids.size() > 0) {
            searchDao.getXmlItems(ids);
        }
    }

    public void getSellerCats() {
        searchDao.getSellerCats();
    }

}
