package com.meiliwan.emall.cms.ff.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

public class Block extends BaseEntity implements Cloneable{
    private Integer id;

    private String blockName;

    private Integer templateId;

    private Integer blockType;

    private Integer idCode;

    private String blockDescParam;

    private String jsonContent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName == null ? null : blockName.trim();
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getBlockType() {
        return blockType;
    }

    public void setBlockType(Integer blockType) {
        this.blockType = blockType;
    }

    public Integer getIdCode() {
        return idCode;
    }

    public void setIdCode(Integer idCode) {
        this.idCode = idCode;
    }

    public String getJsonContent() {
        return jsonContent;
    }

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }

    public String getBlockDescParam() {
        return blockDescParam;
    }

    public void setBlockDescParam(String blockDescParam) {
        this.blockDescParam = blockDescParam;
    }
}