package com.meiliwan.emall.tongji.task.dao.oms;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.tongji.bean.oms.OmsProSales;

import java.util.List;

/**
 * Created by Sean on 13-11-15.
 */
public interface OmsProSalesDao extends IDao<Integer, OmsProSales> {

    List<OmsProSales> getStoreDailySales(OmsProSales omsProSales);
}
