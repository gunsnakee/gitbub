package org.jiawu.freemaker;

import freemarker.core.Environment;
import freemarker.template.*;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiawuwu on 14-4-29.
 */
public class MyTag implements TemplateDirectiveModel {

    @Override
    public void execute(Environment env, Map params, TemplateModel[] templateModels, TemplateDirectiveBody body) throws TemplateException, IOException {
        Writer out = env.getOut();

        //将模版里的数字参数转化成int类型的方法，，其它类型的转换请看freemarker文档
        TemplateModel paramValue = (TemplateModel) params.get("num");
        int num = ((TemplateNumberModel) paramValue).getAsNumber().intValue();

        out.write("Akishimo num=" + params.get("num") + "的类型为:" + paramValue.getClass());
        if (body != null) {
            body.render(env.getOut());
        } else {
            throw new RuntimeException("标签内部至少要加一个空格");
        }
    }

    public static void main(String[] args) throws IOException, TemplateException {
        Configuration cfg = new Configuration();
        //将写好的标签加入，起名叫label
        cfg.setSharedVariable("jiawu", new MyTag());
        cfg.setDirectoryForTemplateLoading(new File("/data/mygit/oschina/src/main/resources/ftl"));
        cfg.setObjectWrapper(new DefaultObjectWrapper());

        Template temp = cfg.getTemplate("zh-test.ftl");

        Map root = new HashMap();
        root.put("user", "rzy");

        StringWriter writer = new StringWriter();

        temp.process(root, writer);
        System.out.println(writer.toString());
    }
}
