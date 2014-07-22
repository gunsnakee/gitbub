package org.jiawu.thymeleaf.processors;

import org.jiawu.thymeleaf.dto.ElementDto;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiawuwu on 14-7-21.
 */
public class WjDemoProcessor extends WjBaseProcessor {
    public WjDemoProcessor() {
        super("demo");
    }

    @Override
    protected void modifyModelAttributes(Arguments arguments, Element element) {
        String resultVar = element.getAttributeValue("resultVar");
        String elementId = element.getAttributeValue("elementId");
        List<ElementDto> childs = new ArrayList<ElementDto>();

        if("0".equals(elementId)){
            for(int i=0;i<3;i++){
                ElementDto dto = new ElementDto();
                dto.setId(100+i);
                dto.setTemplateName("zh-"+dto.getId());
                childs.add(dto);
            }
        }else{
            for(int i=0;i<3;i++){
                ElementDto dto = new ElementDto();
                dto.setId(Integer.parseInt(elementId)*100+i);
                dto.setTemplateName("zh-"+dto.getId());
                childs.add(dto);
            }
        }
        addToModel(arguments,resultVar,childs);
        //addToModel(arguments,"elementId",elementId);
    }
}
