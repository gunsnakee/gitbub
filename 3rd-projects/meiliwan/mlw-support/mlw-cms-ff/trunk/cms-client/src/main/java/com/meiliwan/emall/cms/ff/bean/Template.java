package com.meiliwan.emall.cms.ff.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

public class Template extends BaseEntity implements Cloneable{
    private Integer id;

    private String templateName;

    private String codeName;

    private String templateJs;

    private String templateHtml;

    private String templateCss;

    private String templateDescParam;


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
        this.templateName = templateName == null ? null : templateName.trim();
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName == null ? null : codeName.trim();
    }

    public String getTemplateJs() {
        return templateJs;
    }

    public void setTemplateJs(String templateJs) {
        this.templateJs = templateJs;
    }

    public String getTemplateDescParam() {
        return templateDescParam;
    }

    public void setTemplateDescParam(String templateDescParam) {
        this.templateDescParam = templateDescParam;
    }

    public String getTemplateCss() {
        return templateCss;
    }

    public void setTemplateCss(String templateCss) {
        this.templateCss = templateCss;
    }

    public String getTemplateHtml() {
        return templateHtml;
    }

    public void setTemplateHtml(String templateHtml) {
        this.templateHtml = templateHtml;
    }
}