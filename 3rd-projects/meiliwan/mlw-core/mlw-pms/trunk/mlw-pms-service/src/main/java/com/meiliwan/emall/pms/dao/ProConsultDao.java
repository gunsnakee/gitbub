package com.meiliwan.emall.pms.dao;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.pms.bean.ProConsult;
import com.meiliwan.emall.pms.dto.ConsultDTO;
import com.meiliwan.emall.pms.dto.QueryCountsDTO;

import java.util.List;

public interface ProConsultDao extends IDao<Integer, ProConsult> {

    /**
     * 应用场景：管理后台列表页
     * @param commentView
     * @param pageInfo
     * @return
     */
    PagerControl<ProConsult> getPagerByConsultDTO(ConsultDTO commentView, PageInfo pageInfo);

    /**
     * 应用场景：商品详情页展示不同咨询类型的条数
     * @param consult
     * @param orderBy
     * @return
     */
    List<QueryCountsDTO> getConsultCountsByType(ProConsult consult, String whereSql, String orderBy);
}