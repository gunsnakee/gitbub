package com.meiliwan.emall.pms.service;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.pms.bean.ProConsult;
import com.meiliwan.emall.pms.bean.SimpleProduct;
import com.meiliwan.emall.pms.dao.ProConsultDao;
import com.meiliwan.emall.pms.dao.ProProductDao;
import com.meiliwan.emall.pms.dto.ConsultDTO;
import com.meiliwan.emall.pms.dto.QueryCountsDTO;
import com.meiliwan.emall.service.BaseService;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * User: guangdetang
 * Date: 13-6-13
 * Time: 下午4:41
 */
@Service
public class ProConsultService extends DefaultBaseServiceImpl implements BaseService {

    @Autowired
    private ProConsultDao consultDao;
    @Autowired
    private ProProductDao productDao;

    /**
     * 针对商品进行咨询
     * @param resultObj
     * @param consult
     */
    public void addConsult(JsonObject resultObj, ProConsult consult) {
        if (consult != null) {
            consult.setCreateTime(DateUtil.getCurrentTimestamp());
            int result = consultDao.insert(consult);
            addToResult(result > 0 ? true : false, resultObj);
        }
    }

    /**
     * 修改一条对象
     * @param resultObj
     * @param consult
     */
    public void updateConsult(JsonObject resultObj, ProConsult consult) {
        if (consult != null) {
            int result = consultDao.update(consult);
            addToResult(result > 0 ? true : false, resultObj);
        }
    }


    /**
     * 管理员修改咨询列表上的各种咨询状态（各种删除、显示）
     * @param resultObj
     * @param ids
     * @param handle
     */
    @IceServiceMethod
    public void updateConsultStateByAdmin(JsonObject resultObj, int[] ids, int handle) {
        if (ids != null && ids.length > 0 && handle > 0) {
            for (Integer i : ids) {
                ProConsult consult = new ProConsult();
                consult.setId(i);
                if (handle == 1 || handle == 2) {
                    consult.setIsWebVisible((short) (handle - 2));
                } else if (handle == 3 || handle == 4) {
                    consult.setIsAdminDel((short) (handle - 4));
                } else if (handle == 5 || handle == 6) {
                    consult.setIsUserDel((short) (handle - 6));
                }
                consultDao.update(consult);
            }
            addToResult(true, resultObj);
        }
    }

    /**
     * 回复购买咨询<br/>
     * @param id           咨询ID
     * @param replyContent 回复内容
     */
    @IceServiceMethod
    public void updateReplyConsult(JsonObject resultObj, int id, String replyContent) {
        if (id > 0 && !Strings.isNullOrEmpty(replyContent)) {
            ProConsult consult = new ProConsult();
            consult.setId(id);
            consult.setReplyContent(replyContent);
            consult.setReplyTime(DateUtil.getCurrentTimestamp());
            int result = consultDao.update(consult);
            addToResult(result > 0 ? true : false, resultObj);
        }
    }

    /** =====================get区===================== */
    /**
     * 获得咨询列表(管理员)
     * @param pageInfo
     * @return
     */
    @IceServiceMethod
    public void getConsultPagerByAdmin(JsonObject resultObj, ConsultDTO consult, PageInfo pageInfo) {
        if (consult != null && pageInfo != null) {
            if (consult.getNickName() != null && !Strings.isNullOrEmpty(consult.getNickName().trim())) {
                consult.setNickName("%" + consult.getNickName() + "%");
            } else {
                consult.setNickName(null);
            }
            if (consult.getContent() != null && !Strings.isNullOrEmpty(consult.getContent().trim())) {
                consult.setContent("%" + consult.getContent() + "%");
            } else {
                consult.setContent(null);
            }
            if (consult.getProName() != null && !Strings.isNullOrEmpty(consult.getProName().trim())) {
                consult.setProName("%" + consult.getProName() + "%");
            } else {
                consult.setProName(null);
            }
            if (consult.getStartTime() != null && !Strings.isNullOrEmpty(consult.getStartTime().trim())) {
                consult.setProName(consult.getProName());
            } else {
                consult.setStartTime(null);
            }
            if (consult.getEndTime() != null && !Strings.isNullOrEmpty(consult.getEndTime().trim())) {
                consult.setEndTime(consult.getEndTime());
            } else {
                consult.setEndTime(null);
            }
            if (consult.getIsReply() != null && !Strings.isNullOrEmpty(consult.getIsReply().trim())) {
                //处理页面传对象数组的情况。页面会出现两个<input name='isReply'>的可能
                if (consult.getIsReply().equals("1,1")) {
                    consult.setIsReply("1");
                } else if (consult.getIsReply().equals("2,2")) {
                    consult.setIsReply("2");
                } else {
                    consult.setIsReply(consult.getIsReply());
                }
            } else {
                consult.setIsReply(null);
            }
            addToResult(consultDao.getPagerByConsultDTO(consult, pageInfo), resultObj);
        }
    }

    /**
     * 获得咨询列表(买家中心)
     * @param uid 用户ID
     * @return
     */
    @IceServiceMethod
    public void getConsultPagerByBuyer(JsonObject resultObj, ProConsult consult, PageInfo pageInfo, int uid) {
        if (uid > 0) {
            consult.setUid(uid);
            consult.setIsAdminDel(GlobalNames.STATE_VALID);
            consult.setIsUserDel(GlobalNames.STATE_VALID);
            PagerControl<ProConsult> pc = consultDao.getPagerByObj(consult, pageInfo, null, " order by create_time desc");
            if (pc != null && pc.getEntityList() != null && pc.getEntityList().size() > 0) {
                for (ProConsult p : pc.getEntityList()) {
                    SimpleProduct product = productDao.getEntityById(p.getProId());
                    if (product != null) {
                        p.setProduct(product);
                    }
                }
            }
            addToResult(pc, resultObj);
        }
    }


    /**
     * 根据商品获得该商品的购买咨询情况
     * @param pageInfo
     * @param proId
     * @return
     */
    @IceServiceMethod
    public void getConsultPagerByProId(JsonObject resultObj, PageInfo pageInfo, int proId, short type) {
        if (pageInfo != null && proId > 0) {
            ProConsult consult = new ProConsult();
            consult.setProId(proId);
            consult.setIsAdminDel(GlobalNames.STATE_VALID);
            consult.setIsWebVisible(GlobalNames.STATE_VALID);
            //传0查type的全部类型
            if (type != 0) {
                consult.setConsultType(type);
            }
            addToResult(consultDao.getPagerByObj(consult, pageInfo, " reply_content is not null", " order by create_time desc"), resultObj);
        }
    }

    /**
     * 根据咨询ID获得咨询详情
     * @param id
     * @return
     */
    @IceServiceMethod
    public void getConsult(JsonObject resultObj, int id) {
        if (id > 0) {
            addToResult(consultDao.getEntityById(id), resultObj);
        }
    }

    /**
     * 获得不同咨询类型的条数
     * @param resultObj
     * @param proId
     */
    @IceServiceMethod
    public void getConsultCounts(JsonObject resultObj, int proId) {
        ProConsult consult = new ProConsult();
        consult.setProId(proId);
        consult.setIsAdminDel(GlobalNames.STATE_VALID);
        consult.setIsWebVisible(GlobalNames.STATE_VALID);
        List<QueryCountsDTO> list = consultDao.getConsultCountsByType(consult, " reply_content is not null", " group by consult_type");
        addToResult(list, resultObj);
    }
}
