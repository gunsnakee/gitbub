package dao;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.meiliwan.emall.antispam.bean.AntispamKeyword;
import com.meiliwan.emall.antispam.bean.AntispamKeyword.KeywordType;
import com.meiliwan.emall.antispam.dao.AntispamKeywordDao;
import com.meiliwan.emall.commons.PageInfo;

public class AntispamKeywordDaoTest extends BaseTest {
	
	@Autowired
	AntispamKeywordDao dao;
	
	@Test
	public void addKeyword(){
		AntispamKeyword keyword = new AntispamKeyword();
		keyword.setCreateTime(new Date());
		keyword.setKeywordType(KeywordType.POLITICAL);
		keyword.setWord("薄熙来|正确");
		keyword.setCreator("wuyujing");
		dao.insert(keyword);
	}
	
	@Test
	public void delKeyword(){
		int effect = dao.delete(1282);
		System.out.println(effect);
		Assert.assertNotEquals(effect, 0);
	}
	
	@Test
	public void updateKeyword(){
		AntispamKeyword keyword = new AntispamKeyword();
		keyword.setId(2011);
		keyword.setWord("薄熙来|好人");
		int effect = dao.update(keyword);
		Assert.assertEquals(effect, 1);
	}
	
	@Test
	public void search(){
		String word = "军队";
		PageInfo pageInfo = new PageInfo(0, 20);
		String whereSql = "word like '%" + word + "%'";
		List<AntispamKeyword> result = dao.getListByObj(null, pageInfo, whereSql, " order by create_time desc ");
		System.out.println(result);
	}
	
	@Test
	public void getAll(){
		List<AntispamKeyword> keywords = dao.getAllEntityObj();
		System.out.println(keywords.size());
	}
	
	@Test
	public void getListByObject(){
		AntispamKeyword keyword = new AntispamKeyword();
		keyword.setKeywordType(KeywordType.POLITICAL);
		PageInfo pageInfo = new PageInfo(0, 20);
		List<AntispamKeyword> list = dao.getListByObj(keyword, pageInfo, null, " order by create_time desc ");
		Assert.assertNotNull(list);
		Assert.assertNotEquals(list.size(), 0);
	}
}
