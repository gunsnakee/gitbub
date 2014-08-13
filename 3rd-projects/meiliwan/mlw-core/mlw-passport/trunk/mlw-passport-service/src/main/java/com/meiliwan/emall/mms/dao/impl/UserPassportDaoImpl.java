package com.meiliwan.emall.mms.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.mms.bean.UserPassport;
import com.meiliwan.emall.mms.bean.UserPassportPara;
import com.meiliwan.emall.mms.bean.UserPassportSimple;
import com.meiliwan.emall.mms.dao.UserPassportDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserPassportDaoImpl   extends BaseDao<Integer,UserPassport > implements UserPassportDao {

	@Override
	public String getMapperNameSpace() {
		return UserPassportDao.class.getName();
	}

    @Override
    public JedisKey getUseJedisCacheKey() {
        return JedisKey.mms$passport;
    }

	@Override
	public int updateEmailActive(UserPassport entity) {
        deleteCacheByPk(entity.getId());
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateEmailActive",  getShardParam(entity != null ? entity.getId() : null, entity, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateEmailActive: {}", entity == null ? "" : entity.toString(), e);
        }

	}

	@Override
	public int updateMobileActive(UserPassport entity) {
        deleteCacheByPk(entity.getId());
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateMobileActive", getShardParam(entity != null ? entity.getId() : null, entity, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateMobileActive: {}", entity == null ? "" : entity.toString(), e);
        }

	}

	@Override
	public List<UserPassport> getListByPara(UserPassportPara para) {
        try {
            return getSqlSession().selectList(getMapperNameSpace()+".getListByPara", getShardParam(para!=null? para.getId():null,para,true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getListByPara: {}", para == null ? "" : para.toString(), e);
        }
	}

    @Override
    public List<UserPassportSimple> getSimpleListByPara(UserPassportPara para) {
        try {
            return getSqlSession().selectList(getMapperNameSpace()+".getSimpleListByPara", getShardParam(para!=null? para.getId():null,para,true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getSimpleListByPara: {}", para == null ? "" : para.toString(), e);
        }
    }

    @Override
	public UserPassport getBeanByPara(UserPassportPara para) {
        try {
            return getSqlSession().selectOne(getMapperNameSpace()+".getBeanByPara", getShardParam(para!=null? para.getId():null,para,true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getBeanByPara: {}", para == null ? "" : para.toString(), e);
        }

	}

    @Override
    public int getCountExcludeSelf(UserPassport para) {
        try {
            return (Integer)getSqlSession().selectOne(getMapperNameSpace()+".getCountExcludeSelf", getShardParam(para!=null? para.getId():null,para,true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getCountExcludeSelf: {}", para == null ? "" : para.toString(), e);
        }

    }

    @Override
    public String getSeqId() {
        try {
            return (String)getSqlSession().selectOne(getMapperNameSpace()+".getSeqId", getShardParam(null,null,true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getSeqId: {}", "", e);
        }

    }

    @Override
    public int updateSeqId(String s) {
        try {
            return getSqlSession().update(getMapperNameSpace()+".updateSeqId", getShardParam(null,s,true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateSeqId: {}", s, e);
        }

    }

    @Override
    public int addSeqId(String s) {
        try {
            return getSqlSession().update(getMapperNameSpace()+".addSeqId", getShardParam(null,s,true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".addSeqId: {}", s, e);
        }

    }

}