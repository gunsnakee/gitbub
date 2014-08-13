package com.meiliwan.emall.pms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.sql.Timestamp;
import java.util.Date;

public class ProComment extends BaseEntity {
    private static final long serialVersionUID = 4973344704320444623L;
    private Integer id;

    private Integer uid;

    private String nickName;

    private Short score;

    private Integer proId;

    private String proName;

    private String orderId;

    private Timestamp commentTime;

    private Timestamp replyTime;

    private Timestamp createTime;
    
    private Long commentTimeLong;

    private Long replyTimeLong;

    private Long createTimeLong;

    private Integer usefulCount;

    private Integer uselessCount;

    private Short sequence;

    private Short state;

    private Short isWebVisible;

    private Short isUserDel;

    private Short isAdminDel;

    private String content;

    private String replyContent;

    private SimpleProduct product;// 用于商品评价显示列表中显示（在controller端拼凑）

    // 用于商品评价显示列表中显示（在controller端拼凑）
    public SimpleProduct getSimpleProduct() {
        return product;
    }

    public void setSimpleProduct(SimpleProduct product) {
        this.product = product;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent == null ? null : replyContent.trim();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName == null ? null : proName.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public Integer getUsefulCount() {
        return usefulCount;
    }

    public void setUsefulCount(Integer usefulCount) {
        this.usefulCount = usefulCount;
    }

    public Integer getUselessCount() {
        return uselessCount;
    }

    public void setUselessCount(Integer uselessCount) {
        this.uselessCount = uselessCount;
    }

    public Short getScore() {
        return score;
    }

    public void setScore(Short score) {
        this.score = score;
    }

    public Timestamp getCommentTime() {
        return commentTime;
    }
    
    public Long getCommentTimeLong() {
        return commentTimeLong;
    }

    public void setCommentTime(Timestamp commentTime) {
        this.commentTime = commentTime;
        this.commentTimeLong = commentTime.getTime();
    }

    public Timestamp getReplyTime() {
        return replyTime;
    }
    
    public Long getReplyTimeLong() {
        return replyTimeLong;
    }

    public void setReplyTime(Timestamp replyTime) {
        this.replyTime = replyTime;
        this.replyTimeLong = replyTime.getTime();
    }

    public Timestamp getCreateTime() {
        return createTime;
    }
    
    public Long getCreateTimeLong() {
        return createTimeLong;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
        this.createTimeLong = createTime.getTime() ;
    }

    public Short getSequence() {
        return sequence;
    }

    public void setSequence(Short sequence) {
        this.sequence = sequence;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public Short getIsWebVisible() {
        return isWebVisible;
    }

    public void setIsWebVisible(Short isWebVisible) {
        this.isWebVisible = isWebVisible;
    }

    public Short getIsUserDel() {
        return isUserDel;
    }

    public void setIsUserDel(Short isUserDel) {
        this.isUserDel = isUserDel;
    }

    public Short getIsAdminDel() {
        return isAdminDel;
    }

    public void setIsAdminDel(Short isAdminDel) {
        this.isAdminDel = isAdminDel;
    }

	@Override
	public String toString() {
		return "ProComment [id=" + id + ", uid=" + uid + ", nickName="
				+ nickName + ", score=" + score + ", proId=" + proId
				+ ", proName=" + proName + ", orderId=" + orderId
				+ ", commentTime=" + commentTime + ", replyTime=" + replyTime
				+ ", createTime=" + createTime + ", usefulCount=" + usefulCount
				+ ", uselessCount=" + uselessCount + ", sequence=" + sequence
				+ ", state=" + state + ", isWebVisible=" + isWebVisible
				+ ", isUserDel=" + isUserDel + ", isAdminDel=" + isAdminDel
				+ ", content=" + content + ", replyContent=" + replyContent
				+ ", product=" + product + "]";
	}
    
}