package com.meiliwan.emall.sp2.bean.view;

import java.io.Serializable;

/**
 * Created by Sean on 13-12-24.
 */
public class UserProVO implements Serializable,Comparable {

    private static final long serialVersionUID = 24994514571362884L;
    /**
     * 商品Id
     */
    protected int productId;

    public int getUserIsSelected() {
        return userIsSelected;
    }

    public void setUserIsSelected(int userIsSelected) {
        this.userIsSelected = userIsSelected;
    }

    protected long createCartTime ;


    public long getCreateCartTime() {
        return createCartTime;
    }

    public void setCreateCartTime(long createCartTime) {
        this.createCartTime = createCartTime;
    }

    /**
     * 默认是选择
     */
    protected int userIsSelected = 1;

    protected int buyCount;
    protected String buyType="NML";

    @Override
    public String toString() {
        return "{" +
                "productId='" + this.getProductId() +'\'' +
                ", buyCount='" + buyCount +'\'' +
                "}";
    }

    public UserProVO() {
    }

    public UserProVO(int productId, int buyCount, int selectedSingleActId, int selectedMultiActId,int userIsSelected,long createCartTime) {
        this.productId = productId;
        this.buyCount = buyCount;
        this.selectedSingleActId = selectedSingleActId;
        this.selectedMultiActId = selectedMultiActId;
        this.userIsSelected =userIsSelected;
        this.createCartTime =createCartTime;
    }

    public UserProVO(int productId, int buyCount, int selectedSingleActId, int selectedMultiActId,int userIsSelected) {
        this.productId = productId;
        this.buyCount = buyCount;
        this.selectedSingleActId = selectedSingleActId;
        this.selectedMultiActId = selectedMultiActId;
        this.userIsSelected =userIsSelected;
    }

    public UserProVO(int productId, int buyCount, int selectedSingleActId, int selectedMultiActId) {
        this.productId = productId;
        this.buyCount = buyCount;
        this.selectedSingleActId = selectedSingleActId;
        this.selectedMultiActId = selectedMultiActId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserProVO up2 = (UserProVO)o;
        if (productId != up2.getProductId()) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return productId;
    }

    @Override
    public int compareTo(Object o) {
        UserProVO up2 = (UserProVO)o;
        return (this.createCartTime > up2.getCreateCartTime()) ? -1 : (createCartTime == up2.getCreateCartTime() && this.equals(up2)) ? 0 : 1;
    }

    /**
     * 表示用户单品优惠的选择  默认-1 表示没有选择
     */
    private int selectedSingleActId = -1;
    /**
     * 表示用户多品优惠的选择 默认-1 表示没有选择
     */
    private int selectedMultiActId = -1;

    public String getBuyType() {
        return buyType;
    }

    public void setBuyType(String buyType) {
        this.buyType = buyType;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

    public int getSelectedSingleActId() {
        return selectedSingleActId;
    }

    public void setSelectedSingleActId(int selectedSingleActId) {
        this.selectedSingleActId = selectedSingleActId;
    }

    public int getSelectedMultiActId() {
        return selectedMultiActId;
    }

    public void setSelectedMultiActId(int selectedMultiActId) {
        this.selectedMultiActId = selectedMultiActId;
    }
}
