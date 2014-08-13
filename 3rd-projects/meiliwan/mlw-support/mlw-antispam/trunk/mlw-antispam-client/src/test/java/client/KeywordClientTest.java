package client;

import java.util.Date;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.meiliwan.emall.antispam.bean.AntispamKeyword;
import com.meiliwan.emall.antispam.bean.KeywordAdminResult;
import com.meiliwan.emall.antispam.bean.AntispamKeyword.KeywordType;
import com.meiliwan.emall.antispam.client.KeywordClient;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;

public class KeywordClientTest {
	
	@Test
	public void addKeyword(){
		AntispamKeyword keyword = new AntispamKeyword();
		keyword.setKeywordType(KeywordType.POLITICAL);
		keyword.setWord("薄熙来|蛋疼");
		keyword.setCreator("test");
		keyword.setCreateTime(new Date());
		KeywordAdminResult result = KeywordClient.addKeyword(keyword);
		System.out.println(result.name());
	}
	
	@Test
	public void delKeyword(){
		KeywordAdminResult result = KeywordClient.delKeyword(259);
		Assert.assertEquals(result, KeywordAdminResult.ADD_SUCC);;
	}
	
	@Test
	public void updateKeyword(){
		AntispamKeyword keyword = new AntispamKeyword();
		keyword.setId(513);
		keyword.setKeywordType(KeywordType.POLITICAL);
		keyword.setWord("军队起义");
		keyword.setCreator("admin");
		KeywordAdminResult result = KeywordClient.updateKeyword(keyword);
		Assert.assertEquals(result, KeywordAdminResult.ADD_SUCC);
	}
	
	@Test
	public void searchKeyword(){
		AntispamKeyword entity = new AntispamKeyword();
		entity.setKeywordType(KeywordType.POLITICAL);
		PageInfo pageInfo = new PageInfo(0, 20);
		PagerControl<AntispamKeyword> control = KeywordClient.search(entity, "军队", pageInfo);
		Assert.assertNotNull(control);
		Assert.assertNotEquals(control.getEntityList().size(), 0);
	}
	
	@Test
	public void getKeywordList(){
		AntispamKeyword keyword = new AntispamKeyword();
		keyword.setKeywordType(KeywordType.POLITICAL);
		PageInfo pageInfo = new PageInfo(0, 20);
		PagerControl<AntispamKeyword> controller = KeywordClient.getKeywordList(keyword, pageInfo);
		Assert.assertNotNull(controller);
		Assert.assertNotEquals(controller.getEntityList().size(), 0);
	}
}
