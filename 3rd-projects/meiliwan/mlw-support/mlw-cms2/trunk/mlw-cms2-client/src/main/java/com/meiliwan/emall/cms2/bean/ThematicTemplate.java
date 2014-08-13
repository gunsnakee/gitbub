package com.meiliwan.emall.cms2.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

public class ThematicTemplate extends BaseEntity {
    private static final long serialVersionUID = -1461452441145905300L;
    private Integer templateId;

    private String templateName;

    private String templateFtl;

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName == null ? null : templateName.trim();
    }

    public String getTemplateFtl() {
        return templateFtl;
    }

    public void setTemplateFtl(String templateFtl) {
        this.templateFtl = templateFtl == null ? null : templateFtl.trim();
    }

    @Override
    public Integer getId() {
        return this.templateId;
    }
}