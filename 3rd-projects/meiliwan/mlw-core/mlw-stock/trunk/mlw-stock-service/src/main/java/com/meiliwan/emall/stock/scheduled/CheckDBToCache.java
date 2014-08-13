package com.meiliwan.emall.stock.scheduled;


import com.meiliwan.emall.stock.service.ProStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务，执行下缓存库存与数据库库存比对
 *
 * User: wuzixin
 * Date: 13-11-7
 * Time: 上午10:48
 */
@Component
public class CheckDBToCache {

    @Autowired
    private ProStockService stockService;

    @Scheduled(cron = "0 0 3 * * ?")
    public void checkStockByDBToCache(){
        stockService.checkDBWithCacheByStock();
    }
}
