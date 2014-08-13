package com.meiliwan.emall.antispam.dao;

import java.util.List;
import com.meiliwan.emall.antispam.bean.AntispamKeyword;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.core.db.IDao;

public interface AntispamKeywordDao extends IDao<Integer, AntispamKeyword>{
    
    List<AntispamKeyword> getPageKeywords(PageInfo pageInfo, AntispamKeyword entity);
}