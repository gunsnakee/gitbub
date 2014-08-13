package com.meiliwan.emall.service.pms.gifcard;

import com.meiliwan.emall.pms.client.CardClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 礼品卡定时扫描任务
 * User: wuzixin
 * Date: 14-1-20
 * Time: 下午2:57
 */
@Component
public class CardScheduledService {
    /**
     * 定期扫描截止日提前提醒时间，然后发送相关邮件
     */
    @Scheduled(cron = "0 0 9 * * ?")
    private void warnScheduledDate() {
        boolean suc = CardClient.getScheduledCard();
        if (suc) {
            System.out.println("扫描邮件成功");
        }else {
            System.out.println("扫描邮件失敗");
        }
    }
}
