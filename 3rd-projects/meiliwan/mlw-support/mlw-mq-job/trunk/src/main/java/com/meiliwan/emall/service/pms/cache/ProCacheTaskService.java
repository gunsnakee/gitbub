package com.meiliwan.emall.service.pms.cache;

import com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter.MsgTaskCenter;
import com.meiliwan.emall.service.BackgroundService;

/**
 * User: wuzixin
 * Date: 14-3-12
 * Time: 上午10:18
 */
public class ProCacheTaskService implements BackgroundService {
    @Override
    public void bgRun() {
        MsgTaskCenter.initTaskCenter("mqconf/pms-mq-config.xml");
    }
}
