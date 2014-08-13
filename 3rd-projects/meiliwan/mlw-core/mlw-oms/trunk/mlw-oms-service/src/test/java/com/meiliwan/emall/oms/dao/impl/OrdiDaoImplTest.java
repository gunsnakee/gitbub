package com.meiliwan.emall.oms.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.meiliwan.emall.oms.BaseTest;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.dao.OrdiDao;
import com.meiliwan.emall.oms.dto.OrdiDTO;

/**
 * Created with IntelliJ IDEA.
 * User: Sean
 * Date: 13-5-17
 * Time: 下午4:29
 */
public class OrdiDaoImplTest extends BaseTest {


    @Autowired
    private OrdiDao ordiDao;

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
    	Ordi o1 = new Ordi();
        o1.setOrderId("12345");
        o1.setUid(11);
        ordiDao.insert(o1);
    }


    @Test
    public void testGetOrdIDTOListByOrdId() {
//    	 Code: service-com.meiliwan.emall.oms.dao.OrdiDao.getOrdIDTOListByOrdId: {},{}
//    	 Message: 000000164741
	    	List<OrdiDTO> list = ordiDao.getOrdIDTOListByOrdId("000000164741");
	    	System.out.println(list);
    }
}
