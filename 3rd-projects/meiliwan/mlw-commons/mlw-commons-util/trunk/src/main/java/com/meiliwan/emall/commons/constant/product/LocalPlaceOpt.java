package com.meiliwan.emall.commons.constant.product;

/**
 * 地方馆 使用
 * User: wuzixin
 * Date: 13-7-10
 * Time: 下午1:40
 */
public enum LocalPlaceOpt {

    /**
     * 泰国
     */
    thailand(3),
    /**
     * 越南
     */
    vietnam(6),
    /**
     * 马来西亚
     */
    malaysia(9),
    /**
     * 印度尼西亚
     */
    indonesia(4),
    /**
     * 老挝
     */
    laos(7),
    /**
     * 柬埔寨
     */
    cambodia(8),
    /**
     * 缅甸
     */
    burma(5),
    /**
     * 广西
     */
    gx(1),
    /**
     * 文莱
     */
    brunei(11),
    /**
     * 菲律宾
     */
    philippin(10),
    /**
     * 新加坡
     */
    singapore(12);

    private int placeId;

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    LocalPlaceOpt(int placeId){
        this.placeId = placeId;
    }
}
