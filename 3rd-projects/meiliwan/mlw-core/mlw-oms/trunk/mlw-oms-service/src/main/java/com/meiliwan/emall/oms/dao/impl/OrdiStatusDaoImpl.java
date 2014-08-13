package com.meiliwan.emall.oms.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.dao.OrdiStatusDao;
import com.meiliwan.emall.oms.dto.OrdIStatusDTO;

/**
 * Created by nibin on 13-5-23.
 */
@Repository
public class OrdiStatusDaoImpl extends BaseDao<String, OrdiStatus> implements OrdiStatusDao {

	@Override
    public String getMapperNameSpace() {
        return OrdiStatusDao.class.getName();
    }

	@Override
	public OrdiStatus selectByOrdIStatus(String orderItemId, String statusType, String statusCode) {
		return selectByOrdIStatus(orderItemId, statusType, statusCode, false);
	}

    @Override
    public OrdiStatus selectByOrdIStatus(String orderItemId, String statusType, String statusCode, boolean isMainDS) {
        OrdIStatusDTO dto = new OrdIStatusDTO(orderItemId, statusType, statusCode);

        try {
            return getSqlSession().selectOne(getMapperNameSpace() + ".selectByOrdIStatusTypeCode",
                    getShardParam(orderItemId, dto, isMainDS));
        } catch (Exception e) {
            throw new ServiceException("service-"+getMapperNameSpace()+".selectByOrdIStatus: {}", dto.toString(), e);
        }
    }

    @Override
    public List<OrdiStatus> selectByOrderId(String orderId) {
        return selectByOrderId(orderId, false);
    }

    @Override
    public List<OrdiStatus> selectByOrderId(String orderId, boolean isMainDS) {
        OrdIStatusDTO dto = new OrdIStatusDTO();
        dto.setOrderId(orderId);
        try {
            return getSqlSession().selectList(getMapperNameSpace() + ".selectByOrdStatusTypeCode",
                    getShardParam(orderId, dto, isMainDS));
        } catch (Exception e) {
            throw new ServiceException("service-"+getMapperNameSpace()+".selectByOrdIStatus: {}", new String[]{orderId}, e);
        }
    }

    @Override
    public List<OrdiStatus> selectByOrderId(String orderId, String[] statusType) {
        return selectByOrderId(orderId, statusType, false);
    }

    @Override
    public List<OrdiStatus> selectByOrderId(String orderId, String[] statusType, boolean isMainDS) {
        OrdIStatusDTO dto = new OrdIStatusDTO();
        dto.setOrderId(orderId);
        dto.setStatusTypes(statusType);
        try {
            return getSqlSession().selectList(getMapperNameSpace() + ".selectByOrdStatusTypeCode",
                    getShardParam(orderId, dto, isMainDS));
        } catch (Exception e) {
            throw new ServiceException("service-"+getMapperNameSpace()+".selectByOrdIStatus: {}", new String[]{orderId}, e);
        }
    }

    @Override
    public OrdiStatus selectOneByOrderId(String orderId, String statusType, String statusCode) {
        return selectOneByOrderId(orderId, statusType, statusCode, false);
    }

    @Override
    public OrdiStatus selectOneByOrderId(String orderId, String statusType, String statusCode, boolean isMainDS) {
        OrdIStatusDTO dto = new OrdIStatusDTO();
        dto.setOrderId(orderId);
        dto.setStatusType(statusType);
        dto.setStatusCode(statusCode);

        try {
            return getSqlSession().selectOne(getMapperNameSpace() + ".selectOneByOrdStatusTypeCode",
                    getShardParam(orderId, dto, isMainDS));
        } catch (Exception e) {
            throw new ServiceException("service-"+getMapperNameSpace()+".selectOneByOrderId: {},{},{}", new String[]{orderId, statusType, statusCode}, e);
        }
    }

    @Override
    public OrdiStatus selectOneByOrderIdCodes(String orderId, String statusType, String[] statusCodes, boolean isMainDS) {
        OrdIStatusDTO dto = new OrdIStatusDTO();
        dto.setOrderId(orderId);
        dto.setStatusType(statusType);
        dto.setStatusCodes(statusCodes);

        try {
            return getSqlSession().selectOne(getMapperNameSpace() + ".selectOneByOrdStatusTypeCode",
                    getShardParam(orderId, dto, isMainDS));
        } catch (Exception e) {
            throw new ServiceException("service-"+getMapperNameSpace()+".selectOneByOrderId: {}", dto.toString(), e);
        }
    }

    @Override
    public List<String> selectOneOrderIdByOrderIds(List<String> orderIds, String statusType, String statusCode) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderIds", orderIds);
        map.put("statusType", statusType);
        map.put("statusCode", statusCode);
        try {
            return getSqlSession().selectList(getMapperNameSpace() + ".selectOneOrderIdByOrdStatusTypeCodes",
                    getShardParam(orderIds.get(0), map, false));
        } catch (Exception e) {
            throw new ServiceException("service-"+getMapperNameSpace()+".selectOneOrderIdByOrderIds: {},{},{}", map.toString(), e);
        }
    }

    @Override
	public List<OrdiStatus> selectByOrdIStatus(String orderItemId, String statusType) {
		OrdIStatusDTO dto = new OrdIStatusDTO(orderItemId, statusType);
		
		try {
			return getSqlSession().selectList(getMapperNameSpace() + ".selectByOrdIStatusTypeCode", 
					getShardParam(orderItemId, dto, false));
        } catch (Exception e) {
        	throw new ServiceException("service-"+getMapperNameSpace()+".selectByOrdIStatus: {},{}", new String[]{orderItemId, statusType}, e);
        }
	}

    @Override
    public List<OrdiStatus> selectByOrdIStatus(String orderItemId, String statusType, boolean isMainDS) {
        OrdIStatusDTO dto = new OrdIStatusDTO(orderItemId, statusType);

        try {
            return getSqlSession().selectList(getMapperNameSpace() + ".selectByOrdIStatusTypeCode",
                    getShardParam(orderItemId, dto, isMainDS));
        } catch (Exception e) {
            throw new ServiceException("service-"+getMapperNameSpace()+".selectByOrdIStatus: {},{}", new String[]{orderItemId, statusType}, e);
        }
    }

	@Override
	public List<OrdiStatus> selectByOrdIStatus(String orderItemId) {
		return selectByOrdIStatus(orderItemId, false);
	}

    @Override
    public List<OrdiStatus> selectByOrdIStatus(String orderItemId, boolean isMainDS) {
        OrdIStatusDTO dto = new OrdIStatusDTO(orderItemId);
        try {
            return getSqlSession().selectList(getMapperNameSpace() + ".selectByOrdIStatusTypeCode",
                    getShardParam(orderItemId, dto, isMainDS));
        } catch (Exception e) {
            throw new ServiceException("service-"+getMapperNameSpace()+".selectByOrdIStatus: {},{}", new String[]{orderItemId, orderItemId}, e);
        }
    }

	@Override
	public void updateOrdIStatus(String orderItemId, String statusType,
			String statusCode) {
		try {
			OrdIStatusDTO dto = new OrdIStatusDTO(orderItemId, statusType, statusCode);
            dto.setUpdateTime(new Date());
			getSqlSession().update(getMapperNameSpace() + ".updateOrdIStatus", 
					getShardParam(orderItemId, dto, true));
        } catch (Exception e) {
        	throw new ServiceException("service-"+getMapperNameSpace()+".updateOrdIStatus: {},{}", new String[]{orderItemId, statusCode}, e);
        }
	}
	@Override
	public void updateAllOrdIStatus(String orderId, String statusType,
			String statusCode) {
		try {
			OrdIStatusDTO dto = new OrdIStatusDTO();
            dto.setOrderId(orderId);
            dto.setStatusType(statusType);
            dto.setStatusCode(statusCode);
            dto.setUpdateTime(new Date());
			getSqlSession().update(getMapperNameSpace() + ".updateAllOrdIStatus", 
					getShardParam(orderId, dto, true));
        } catch (Exception e) {
        	throw new ServiceException("service-"+getMapperNameSpace()+".updateAllOrdIStatus: {},{},{}", new String[]{orderId, statusType, statusCode}, e);
        }
	}



    @Override
    public void updateAllOrdIStatus(String orderId, String statusType, String statusCode, int adminId) {
        try {
            OrdIStatusDTO dto = new OrdIStatusDTO();
            dto.setOrderId(orderId);
            dto.setStatusType(statusType);
            dto.setStatusCode(statusCode);
            dto.setAdminId(adminId);
            dto.setUpdateTime(new Date());
            getSqlSession().update(getMapperNameSpace() + ".updateAllOrdIStatus",
                    getShardParam(dto.getOrderId(), dto, true));
        } catch (Exception e) {
            throw new ServiceException("service-"+getMapperNameSpace()+".updateAllOrdIStatus: {},{},{},{}", new String[]{orderId, statusType, statusCode, Integer.toString(adminId)}, e);
        }
    }

    @Override
    public void deleteByOrdIdStatusType(String orderId, String statusType, int adminId) {
        try {
            OrdiStatus ordiStatus = new OrdiStatus();
            ordiStatus.setOrderId(orderId);
            ordiStatus.setStatusType(statusType);
            ordiStatus.setAdminId(adminId);
            ordiStatus.setUpdateTime(new Date());
            ordiStatus.setState(GlobalNames.STATE_INVALID);
            getSqlSession().update(getMapperNameSpace() + ".deleteByOrdIdStatusType",
                    getShardParam(ordiStatus.getOrderId(), ordiStatus, true));
        } catch (Exception e) {
            throw new ServiceException("service-"+getMapperNameSpace()+".deleteByOrdIdStatusType: {},{},{}", new String[]{orderId, statusType, Integer.toString(adminId)}, e);
        }
    }
}
