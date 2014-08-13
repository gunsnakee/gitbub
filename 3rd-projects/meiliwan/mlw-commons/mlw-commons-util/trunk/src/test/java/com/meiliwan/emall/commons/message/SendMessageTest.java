package com.meiliwan.emall.commons.message;

import com.meiliwan.emall.commons.BaseTest;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.MessageUtils;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * User: guangdetang
 * Date: 13-5-20
 * Time: 下午2:58
 */
public class SendMessageTest extends BaseTest {

    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    @Test
    public void sendMessage() throws IOException, BaseException {
        int flag = MessageUtils.sendMessage("18176876269", "mytest");
        logger.debug(flag == 605 ? "============成功" : "============失败");
    }
}
