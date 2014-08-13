package com.meiliwan.emall.sp2.client;

import com.meiliwan.emall.sp2.dto.TicketImportVo;
import com.meiliwan.emall.sp2.dto.TicketParms;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * User: wuzixin
 * Date: 14-6-4
 * Time: 上午11:24
 */
public class SpTicketBatchClientTest {
    @Test
    public void testGetTkSendToUser() throws Exception {
        List<TicketParms> parmses = new ArrayList<TicketParms>();
        TicketParms parms = new TicketParms();
        parms.setAcountNum("420014425@qq.com");
        parms.setType(0);
        parmses.add(parms);

        TicketImportVo vo = new TicketImportVo();
        vo.setBatchId(5);
        vo.setAdminId(73);
        vo.setAdminName("管理员");

        boolean suc = SpTicketBatchClient.getTkSendToUser(parmses,vo);
        if (suc){
            System.out.println("啊啊啊啊啊啊啊");
        }
    }
}
