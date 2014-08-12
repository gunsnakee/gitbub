package org.jiawu.gitbub.core.thymeleaf.dto;

/**
 * Created by jiawuwu on 14-7-22.
 */
public class ElementDto {

    private Integer id;
    private String templateName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
