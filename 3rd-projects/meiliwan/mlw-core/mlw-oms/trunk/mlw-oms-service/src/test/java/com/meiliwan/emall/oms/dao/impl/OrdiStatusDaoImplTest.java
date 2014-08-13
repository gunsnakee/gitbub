package com.meiliwan.emall.oms.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.meiliwan.emall.oms.BaseTest;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.constant.Constant;
import com.meiliwan.emall.oms.dao.OrdiStatusDao;

/**
 * Created with IntelliJ IDEA.
 * User: Sean
 * Date: 13-5-17
 * Time: 下午4:29
 */
public class OrdiStatusDaoImplTest extends BaseTest {


    @Autowired
    private OrdiStatusDao ordiStatusDao;

//    @Test
//    public void queryData() {
//        OrdersExample o1 = new OrdersExample();
//        o1.setOrderName("test" + Math.random());
//        o1.setPrice(new BigDecimal(1));
//        List<OrdersExample> list = ordersExampleDao.getAllEntityObj();
//        for (OrdersExample oe : list) {
//            System.out.println(oe);
//        }
//    }

    @Test
    public void add() {
    	OrdiStatus o1 = new OrdiStatus();
        o1.setOrderId("12345");
        o1.setOrderItemId("1234501");
        o1.setUid(11);
        o1.setBillType((byte) Constant.ORDER_BILL_TYPE_FORWARD);
        ordiStatusDao.insert(o1);
    }
    @Test
    public void select() {

        List<OrdiStatus> ordiStatuses = ordiStatusDao.selectByOrderId("0121000511", new String[]{"IS", "IP"}) ;
        for (OrdiStatus os : ordiStatuses){
            System.out.println(os);
        }
    }

//    @Test
//    public void pager() {
//        OrdersExample o1 = new OrdersExample();
//        o1.setOrderName("test" + Math.random());
//        o1.setPrice(new BigDecimal(1));
//        PagerControl<OrdersExample> pc = ordersExampleDao.getPagerByObj(null, new PageInfo(2, 2), null, null);
//        System.out.println(pc.toString());
//        for (OrdersExample oe : pc.getEntityList()) {
//            System.out.println(oe);
//        }
//    }
}
