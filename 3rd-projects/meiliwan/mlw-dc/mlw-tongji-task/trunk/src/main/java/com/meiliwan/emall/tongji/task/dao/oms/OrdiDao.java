package com.meiliwan.emall.tongji.task.dao.oms;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.tongji.bean.oms.OmsProSales;
import com.meiliwan.emall.tongji.dto.oms.OrdQueryDTO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-18
 * Time: 上午9:59
 * To change this template use File | Settings | File Templates.
 */
public interface OrdiDao extends IDao<String, Ordi> {

    List<OmsProSales> getOmsProSalesBy(OrdQueryDTO ordQueryDTO);
}
