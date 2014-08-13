package com.meiliwan.emall.cms2.bean;

import com.meiliwan.emall.cms2.constant.Constant;
import com.meiliwan.emall.core.bean.BaseEntity;

public class ThematicPageModel extends BaseEntity {
    private static final long serialVersionUID = 4489830159194110400L;

    private Integer id;

    private Integer pageId;

    private Integer modelId;

    private Integer isHide;

    private Integer sequence;

    private Integer type;

    private String jsonData;

    private String descp;

    public boolean isProArea(){
    		if(type!=null&&type.intValue()==Constant.PROD_AREA){
    			return true;
    		}
    		return false;
    }
    public Integer getIsHide() {
        return isHide;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public void setIsHide(Integer isHide) {
        this.isHide = isHide;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData == null ? null : jsonData.trim();
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }
}