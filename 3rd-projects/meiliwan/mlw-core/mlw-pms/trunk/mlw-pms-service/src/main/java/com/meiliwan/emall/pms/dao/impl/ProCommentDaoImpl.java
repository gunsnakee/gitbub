package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.ProComment;
import com.meiliwan.emall.pms.bean.ProCommentVo;
import com.meiliwan.emall.pms.dao.ProCommentDao;
import com.meiliwan.emall.pms.dto.CommentDTO;
import com.meiliwan.emall.pms.dto.ProCommentPage;
import com.meiliwan.emall.pms.dto.QueryCountsDTO;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: guangdetang
 * Date: 13-5-29
 * Time: 下午4:22
 */
@Repository
public class ProCommentDaoImpl extends BaseDao<Integer, ProComment> implements ProCommentDao {
    @Override
    public String getMapperNameSpace() {
        return ProCommentDao.class.getName();
    }

    public PagerControl<ProComment> getPagerByCommentView(CommentDTO commentView, PageInfo pageInfo) {

        PagerControl<ProComment> pagerControl = new PagerControl<ProComment>();
        pageInfo.startTime();
        List<ProComment> list = null;
        int total = 0;
        list = getSqlSession().selectList(getMapperNameSpace() + getListByEntityAndPageInfo + "CommentView",
                getShardParam(null, getMapParamsCommentView(commentView, pageInfo, getOrderBySql(pageInfo)), false));
        total = getCountByCommentView(commentView);

        pageInfo.endTime();
        pageInfo.setTotalCounts(total);
        if (list != null) {
            pagerControl.setEntityList(list);
        }
        pagerControl.setPageInfo(pageInfo);
        return pagerControl;
    }

    @Override
    public int updateComByParam(Integer id, String param) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("param", param);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateComByParam", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateComByParam: {},{}", new String[]{id.toString(), param.toString()}, e);
        }
    }

    @Override
    public List<ProCommentVo> getListByProIds(Integer[] ids) {
        Map<String,Integer[]> map = new HashMap<String, Integer[]>();
        map.put("ids",ids);
        try {
            return getSqlSession().selectList(getMapperNameSpace() + ".getListByProIds", getShardParam(null, map, false));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getListByProIds: {},{}", ids.toString(), e);
        }
    }

    @Override
    public List<QueryCountsDTO> getCountsGroupByScore(ProComment comment, String orderBy) {
        try {
            List<QueryCountsDTO> list =  getSqlSession().selectList(getMapperNameSpace() + getListByEntityAndPageInfo + "QueryCountsDTO", getShardParam(comment != null ? comment.getId() : null, getMapParams(comment, null, null, orderBy), true));
            return list;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getListByObj: {}", "");
        }
    }

    private int getCountByCommentView(CommentDTO commentView) {
        Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + getTotalByEntity + "CommentView",
                getShardParam(null, getMapParamsCommentView(commentView, null, null), false));
        return (Integer) selectOne;
    }

    private Map<String, Object> getMapParamsCommentView(CommentDTO entity, PageInfo pageInfo, String orderBySql) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != entity) map.put("entity", entity);
        if (null != pageInfo) map.put("pageInfo", pageInfo);
        if (null != orderBySql) map.put("orderBy", orderBySql);
        return map;
    }

	@Override
	public PagerControl<ProCommentPage> getPagerForBkstage(
			CommentDTO commentView, PageInfo pageInfo) {
		// TODO Auto-generated method stub
		 PagerControl<ProCommentPage> pagerControl = new PagerControl<ProCommentPage>();
	        pageInfo.startTime();
	        List<ProCommentPage> list = null;
	        int total = 0;
	        list = getSqlSession().selectList(getMapperNameSpace() + ".getPageForBkstage",
	                getShardParam(null, getMapParamsCommentView(commentView, pageInfo, getOrderBySql(pageInfo)), false));
	        Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + ".getTotalForBkstage" ,
	                getShardParam(null, getMapParamsCommentView(commentView, null, null), false));
	        total = (Integer) selectOne;

	        pageInfo.endTime();
	        pageInfo.setTotalCounts(total);
	        if (list != null) {
	            pagerControl.setEntityList(list);
	        }
	        pagerControl.setPageInfo(pageInfo);
	        return pagerControl;
	}

    @Override
    public List<String> getOrderIsCommentListByOrderCenter(String[] orderIds) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", orderIds);
        List<String> list = null;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + ".getOrderIsCommentListByOrderCenter", getShardParam(null, map, false));
            return list;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getOrderIsCommentListByOrderCenter: {}", orderIds.toString(), e);
        }
    }

    @Override
    public List getOrderAllCommentListByOrderCenter(String[] orderIds) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", orderIds);
        List<String> list = null;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + ".getOrderAllCommentListByOrderCenter", getShardParam(null, map, false));
            return list;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getOrderAllCommentListByOrderCenter: {}", orderIds.toString(), e);
        }
    }

}
