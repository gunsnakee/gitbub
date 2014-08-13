package com.meiliwan.emall.antispam.pojo;

/**
 * 词位置
 * 
 * @author mac
 *
 */
public class KeywordPosition {

    private int    offsize;
    private int    length;
    private String keyword;

    // ---------------------------- 
    public int getOffsize() {
        return offsize;
    }

    public void setOffsize(int offsize) {
        this.offsize = offsize;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "<" + keyword + "offsize:" + offsize + "length:" + length + ">";
    }

}
