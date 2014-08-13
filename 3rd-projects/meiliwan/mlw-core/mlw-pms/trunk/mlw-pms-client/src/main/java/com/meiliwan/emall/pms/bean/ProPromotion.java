package com.meiliwan.emall.pms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

/**
 * 促销推荐商品
 * @author guangde.tang
 */
public class ProPromotion extends BaseEntity {
    private static final long serialVersionUID = 674531470498341216L;
    private Integer id;

    private Integer proId;

    private Integer thirdCategoryId;

    private Short sequence;

    private Short isDel;

    private SimpleProduct product;

    private ProCategory category;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public Integer getThirdCategoryId() {
        return thirdCategoryId;
    }

    public void setThirdCategoryId(Integer thirdCategoryId) {
        this.thirdCategoryId = thirdCategoryId;
    }

    public Short getSequence() {
        return sequence;
    }

    public void setSequence(Short sequence) {
        this.sequence = sequence;
    }

    public Short getIsDel() {
        return isDel;
    }

    public void setIsDel(Short isDel) {
        this.isDel = isDel;
    }

    public SimpleProduct getProduct() {
        return product;
    }

    public void setProduct(SimpleProduct product) {
        this.product = product;
    }

    public ProCategory getCategory() {
        return category;
    }

    public void setCategory(ProCategory category) {
        this.category = category;
    }
}