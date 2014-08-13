package com.meiliwan.emall.antispam.bean;

import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import com.meiliwan.emall.core.bean.BaseEntity;

/**
 * 敏感词
 * 敏感词类型、创建人目前未进行细粒度处理
 * 目前只对敏感词进行非空校验
 * @author yj
 *
 */
public class AntispamKeyword extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private KeywordType keywordType;
	
	@NotBlank(message="敏感词不能为空!")
    private String word;

    private String creator;

    private Date createTime;

    private Date updateTime;
    
    @SuppressWarnings("unchecked")
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public KeywordType getKeywordType() {
		return keywordType;
	}

	public void setKeywordType(KeywordType keywordType) {
		this.keywordType = keywordType;
	}

	public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word == null ? null : word.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public enum KeywordType {
    		POLITICAL, PORN, AD, OTHER
    }
}