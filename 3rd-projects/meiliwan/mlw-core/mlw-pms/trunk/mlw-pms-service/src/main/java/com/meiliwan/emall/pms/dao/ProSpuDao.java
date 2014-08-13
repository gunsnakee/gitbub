package com.meiliwan.emall.pms.dao;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.pms.bean.ProSpu;
import com.meiliwan.emall.pms.dto.ProductNamesDto;
import com.meiliwan.emall.pms.dto.ProductPublicParasDto;
import com.meiliwan.emall.pms.dto.SpuListDTO;

public interface ProSpuDao extends IDao<Integer, ProSpu> {


    int getSpuListTotal(SpuListDTO dto, String whereSql, boolean isMain);

    PagerControl<SpuListDTO> getListSpuPage(SpuListDTO entity,
                                            PageInfo pageInfo, Object object, String orderBySql);

    int updateSpuProductNames(ProductNamesDto dto);

    int updateSpuProductPublicParas(ProductPublicParasDto dto);

    int updatePropStr(int spuId, String propStr);
}