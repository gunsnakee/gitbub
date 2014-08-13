package com.meiliwan.emall.sp2.cache;

import java.util.Date;
import java.util.Map;

import org.testng.annotations.Test;

import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.sp2.bean.ActivityBean;
import com.meiliwan.emall.sp2.bean.ActivityProductBean;
import com.meiliwan.emall.sp2.cache.ActCacheTool;
import com.meiliwan.emall.sp2.constant.ActType;

public class ActCacheToolTest {

//	@Test
	public void testAddAct(){
		ActivityBean act = new ActivityBean();
		act.setActId(12345);
		act.setActName("直降活动");
		act.setActType(ActType.DISCOUNT.name());
		act.setStartTime(DateUtil.timeAddByDays(new Date(), 1));
		act.setEndTime(DateUtil.timeAddByDays(new Date(), 10));
		act.setState((short)0);
		
		ActCacheTool.addAct(act);
	}
	
//	@Test
	public void testUpdateAct(){
//		ActVO act = ActCacheTool.getActVO(12345);
//		System.out.println(new Gson().toJson(act));
//		
//		ActCacheTool.updateAct(12345, ActState.DOWN);
//		
//	    act = ActCacheTool.getActVO(12345);
//		System.out.println(new Gson().toJson(act));
	}
	
//	@Test
	public void testAddActPro(){
		ActivityProductBean actPro = new ActivityProductBean();
		actPro.setActId(12345);
		actPro.setProId(3241);
		ActCacheTool.addActPro(actPro);
	}
	
	//@Test
	public void testDelActIdForPro(){
		ActCacheTool.delActIdForPro(12345, 3241);
	}

   // @Test
    public void getAct(){
    //   ActivityBean act1 = ActCacheTool.getActivityBean(1);
    //    ActivityBean act2 = ActCacheTool.getActivityBean(2);
    //   ActivityBean act3 = ActCacheTool.getActivityBean(3);
       Map map = ActCacheTool.getActInfoByProIds(10235391) ;
        System.out.println(map.size());
    }

    @Test
    public void getActInfoByProIds(){
        //   ActivityBean act1 = ActCacheTool.getActivityBean(1);
        //    ActivityBean act2 = ActCacheTool.getActivityBean(2);
        //   ActivityBean act3 = ActCacheTool.getActivityBean(3);
        Map map = ActCacheTool.getActInfoByProIds(10235873) ;
        System.out.println(map.size());
    }
}
