package com.meiliwan.emall.tongji.task.task.oms;

import com.meiliwan.emall.tongji.task.job.oms.OrderJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 13-11-19.
 */
@Service
public class OrderTask {

    @Autowired
    private OrderJob orderJob;

    /**
     * ************ 定时任务 暂且卸载Service********************
     */
    public void tongjiYesterdayProSales() {
        orderJob.addProSalesJob();
    }

    public void mailYesterdayProSalesExcel(){
        orderJob.mailYesterdayProSalesExcel();
    }
}
