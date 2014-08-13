package com.meiliwan.emall.cms.ff.test;

import com.meiliwan.emall.cms.ff.bean.Block;
import com.meiliwan.emall.cms.ff.bean.Page;
import com.meiliwan.emall.cms.ff.bean.Template;
import com.meiliwan.emall.cms.ff.client.CmsNgClient;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-12-19
 * Time: 上午9:43
 */
public class CmsNgClientTest {

    @Test
    public void templateOptTest(){
        Template template = new Template();
        template.setCodeName("queryProduct");
        template.setTemplateCss("css");
        template.setTemplateDescParam("{'x:ls'}");
        template.setTemplateHtml("html");
        template.setTemplateJs("scrit");
        template.setTemplateName("test template1");
        CmsNgClient.addTemplate(template);

        List<Template> list=CmsNgClient.getTemplateByPage(null, null);
        System.out.println(list);

        List<Page> pageList = CmsNgClient.getPageByPage(null,null);
        System.out.println(pageList);

        List<Block> blockList = CmsNgClient.getBlockByPage(null,null);
        System.out.println(blockList);
    }

}
