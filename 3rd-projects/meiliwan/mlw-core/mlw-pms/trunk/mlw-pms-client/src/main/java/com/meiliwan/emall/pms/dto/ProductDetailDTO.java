package com.meiliwan.emall.pms.dto;

import com.google.gson.Gson;

/**
 * Created by jiawuwu on 14-3-6.
 */
public class ProductDetailDTO {
    private int spuId;
    private String descpMenu;
    private String descp;
    private String editorRec;

    public String getEditorRec() {
        return editorRec;
    }

    public void setEditorRec(String editorRec) {
        this.editorRec = editorRec;
    }

    public int getSpuId() {
        return spuId;
    }

    public void setSpuId(int spuId) {
        this.spuId = spuId;
    }

    public String getDescpMenu() {
        return descpMenu;
    }

    public void setDescpMenu(String descpMenu) {
        this.descpMenu = descpMenu;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
