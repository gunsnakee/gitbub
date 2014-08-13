package com.meiliwan.emall.pms.dto;

import com.meiliwan.emall.pms.bean.ProComment;

/**
 * 后台分页
 * @author rubi
 *
 */
public class ProCommentPage extends ProComment {

	private Integer excellentCommentId;
	
	public Integer getExcellentCommentId() {
		return excellentCommentId;
	}

	public void setExcellentCommentId(Integer excellentCommentId) {
		this.excellentCommentId = excellentCommentId;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2471651962124232603L;

}
