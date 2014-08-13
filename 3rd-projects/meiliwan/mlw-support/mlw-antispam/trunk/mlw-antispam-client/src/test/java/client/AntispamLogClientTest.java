package client;

import java.util.Date;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.meiliwan.emall.antispam.bean.AntispamLog;
import com.meiliwan.emall.antispam.bean.AuditResultType;
import com.meiliwan.emall.antispam.client.AntispamLogClient;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.util.DateUtil;

public class AntispamLogClientTest {
	
	@Test
	public void getPageList(){
		AntispamLog antispamLog = new AntispamLog();
		antispamLog.setBusinessName("mlw-pms/comment");
		antispamLog.setResult(AuditResultType.FORBIDDEN);
		Date startTime = DateUtil.parseDateTime("2013-09-11 17:00:00");
		Date endTime = DateUtil.parseDateTime("2013-09-11 18:00:00");
		PageInfo pageInfo = new PageInfo(0, 20);
		PagerControl<AntispamLog> control = AntispamLogClient.getPageList(antispamLog, startTime, endTime, pageInfo);
		Assert.assertNotNull(control);
		Assert.assertNotEquals(control.getEntityList().size(), 0);
	}
}
