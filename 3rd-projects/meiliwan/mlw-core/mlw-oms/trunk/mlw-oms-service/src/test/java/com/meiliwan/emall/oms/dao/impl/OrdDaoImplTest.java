package com.meiliwan.emall.oms.dao.impl;

import com.meiliwan.emall.oms.dto.OrdDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.oms.BaseTest;
import com.meiliwan.emall.oms.bean.Ord;
import com.meiliwan.emall.oms.dao.OrdDao;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author yinggao.zhuo
 * @date 2013-6-13
 */
public class OrdDaoImplTest extends BaseTest {


    @Autowired
    private OrdDao ordDao;


    @Test
    public void add() {
    	Ord o1 = new Ord();
        o1.setOrderId("12345");
        o1.setUid(11);
        ordDao.insert(o1);
    }

    @Test
    public void update() {
    	Ord o1 = new Ord();
        o1.setOrderId("12345");
        o1.setUid(11);
        o1.setOrderComments("hihihi");
        ordDao.update(o1);
    }
    
    @Test
    public void query() {
    	Ord o1 = new Ord();
        o1.setOrderId("12345");
        o1.setUid(11);
        PageInfo pageInfo = new PageInfo();
        PagerControl<Ord> pc = ordDao.getPagerByObj(o1, pageInfo, "");
        logger.info("pc"+pc);
    }

    @Test
    public void getOrdDTOListByIds(){
        List<String> list = new ArrayList<String>();
        list.add("000004258188");
        list.add("000004258196");
        list.add("000004258197");
        list.add("000004258198");
        list.add("000004258203");
        list.add("000004258221");

        List<OrdDTO> result = ordDao.getOrdDTOListByIds(list);
        System.out.println("*********************************");
        System.out.println(result);
        System.out.println("*********************************");
    }

}
