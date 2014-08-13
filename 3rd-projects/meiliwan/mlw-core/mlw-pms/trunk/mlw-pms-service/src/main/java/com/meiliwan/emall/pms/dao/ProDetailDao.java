package com.meiliwan.emall.pms.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.pms.bean.ProDetail;
import com.meiliwan.emall.pms.dto.ProductDetailDTO;

public interface ProDetailDao extends IDao<Integer,ProDetail>{

    int updateProductDetailFromDto(ProductDetailDTO detailDTO);

    int updateEditorRec(ProductDetailDTO detailDTO);

}