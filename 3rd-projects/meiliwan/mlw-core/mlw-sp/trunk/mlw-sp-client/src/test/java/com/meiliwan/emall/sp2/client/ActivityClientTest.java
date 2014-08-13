package com.meiliwan.emall.sp2.client;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.sp2.bean.ActivityBean;
import com.meiliwan.emall.sp2.bean.ActivityProductBean;
import com.meiliwan.emall.sp2.constant.ActState;
import com.meiliwan.emall.sp2.constant.ActType;
import com.meiliwan.emall.sp2.dto.ActivityDTO;
import org.testng.annotations.Test;

import java.util.List;

/**
 * 活动bean 测试类
 * User: yiyou.luo
 * Date: 13-9-6
 * Time: 下午5:05
 */
public class ActivityClientTest {

    /**
     * 添加活动
     */
   //@Test
    public void saveSpActivity() {
        ActivityBean act = new ActivityBean();
        act.setActName("每日折扣");
        act.setActDesc("美丽湾最牛逼折扣，史上最牛逼的哦！");
        act.setAdmin("admin");
        act.setActType(ActType.DISCOUNT.toString());
        Integer id = ActivityClient.saveSpActivity(act);
        System.out.println(id);

    }

    /**
     * 添加活动
     */
  // @Test
    public void getSpActivityById() {
        ActivityClient.getSpActivityById(41);
    }

    /**
     * 修改活动
     *
     */
    //@Test
    public void  updateSpActivity() {
        ActivityBean act = new ActivityBean();
        act.setActId(3);
        act.setActName("每日折扣");
        act.setActDesc("美丽湾最牛逼折扣，史上最牛逼的哦！");
        act.setAdmin("admin00000000");
        System.out.println("updateNum="+ ActivityClient.updateSpActivity(act));
    }

    /**
     * 活动活动完成上架
     * 在创建完活动时执行该方法
     * 1、更新数据库
     * 2、更新缓存
     */
   // @Test
    public void upSpActivity() {
        ActivityBean spActivity = ActivityClient.getSpActivityById(3);
        spActivity.setState(ActState.UP.getState());
        ActivityClient.upSpActivity(spActivity);
    }

    /**
     * 活动活动下架
     * 在活动取消时执行该方法
     * 1、更新数据库
     * 2、更新缓存
     */
   // @Test
    public void downSpActivity() {
        ActivityBean spActivity = ActivityClient.getSpActivityById(10);
        spActivity.setState(ActState.DOWN.getState());
        ActivityClient.downSpActivity(spActivity);
    }

    /**
     * 通过活动查询DTO 实体参数获取对应的实体列表包含物理分页
     *
     */
   // @Test
    public void getSpActivityPaperByActivityDTO() {
        ActivityDTO activityDTO = new ActivityDTO();
        //activityDTO.setActName("测");
        PageInfo pageInfo = new PageInfo();
        PagerControl<ActivityBean> pager = ActivityClient.getSpActivityPaperByActivityDTO( activityDTO,  pageInfo);
        pager.getEntityList();
    }


    /**
     * 删除一个未上线过的活动
     * 当前活动的state必须等于 0
     */
   // @Test
     public void deleteUnOnlineSpActivity() {
        boolean succ = ActivityClient.deleteUnOnlineSpActivity(4);
        System.out.println(succ);
    }

    /**
     * 根据活动名称获取当前最新上线的活动
     */
    @Test
    public void getNewActByName(){
        ActivityBean activityBean = ActivityClient.getNewActByName("美日特价");
        System.out.println("activityBean"+activityBean.toString());
        if(activityBean!=null &&  activityBean.getActId()>0){
            List<ActivityProductBean>  list = ActivityClient.getActProByActId(activityBean.getActId(), 3);
            System.out.println("list"+list.size());
        }

    }
}
