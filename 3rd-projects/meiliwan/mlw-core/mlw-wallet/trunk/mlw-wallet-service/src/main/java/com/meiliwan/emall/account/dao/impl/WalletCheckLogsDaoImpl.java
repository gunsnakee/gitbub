package com.meiliwan.emall.account.dao.impl;

import com.meiliwan.emall.account.bean.WalletCheckLogs;
import com.meiliwan.emall.account.dao.WalletCheckLogsDao;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-11-15
 * Time: 下午3:28
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class WalletCheckLogsDaoImpl extends BaseDao<Integer,WalletCheckLogs> implements WalletCheckLogsDao {
    @Override
    public String getMapperNameSpace() {
        return WalletCheckLogsDao.class.getName();
    }
}
