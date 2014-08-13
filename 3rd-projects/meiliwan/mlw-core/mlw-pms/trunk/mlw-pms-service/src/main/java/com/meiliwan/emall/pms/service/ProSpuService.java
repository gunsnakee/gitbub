package com.meiliwan.emall.pms.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.RedisSearchInfoUtil;
import com.meiliwan.emall.pms.bean.*;
import com.meiliwan.emall.pms.constant.Constant;
import com.meiliwan.emall.pms.dao.*;
import com.meiliwan.emall.pms.dto.ProductDetailDTO;
import com.meiliwan.emall.pms.dto.ProductNamesDto;
import com.meiliwan.emall.pms.dto.ProductPublicParasDto;
import com.meiliwan.emall.pms.dto.SpuListDTO;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * jiawu.wu
 */
@Service
public class ProSpuService extends DefaultBaseServiceImpl {

    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProSpuDao proSpuDao;
    @Autowired
    private ProProductDao proProductDao;
    @Autowired
    private ProDetailDao proDetailDao;
    @Autowired
    private ProSelfPropertyDao proSelfPropertyDao;
    @Autowired
    private ProProductPropertyDao proProductPropertyDao;
    @Autowired
    private ProImagesDao proImagesDao;
    @Autowired
    private ProStoreCategoryDao proStoreCategoryDao;
    @Autowired
    private ProStoreProdDao proStoreProdDao;
    @Autowired
    private ProStoreDao proStoreDao;
    @Autowired
    private ProImagesDao imagesDao;
    @Autowired
    private ProductCacheDao productCacheDao;
    @Autowired
    private ProPropertyDao propertyDao;

    /**
     * 商品SPU增加业务实现接口
     *
     * @param resultObj
     * @param spu       商品SPU对象
     */
    public void addSpu(JsonObject resultObj, ProSpu spu) {
        int spuId = 0;
        if (spu != null) {
            try {
                proSpuDao.insert(spu);
                spuId = spu.getSpuId();
                if (spuId > 0) {
                    //增加SPU详情信息
                    ProDetail detail = spu.getDetail();
                    detail.setSpuId(spuId);
                    proDetailDao.insert(detail);
                    //增加商品固有属性
                    if (spu.getPsplist() != null && spu.getPsplist().size() > 0) {
                        proSelfPropertyDao.insertByBatch(spuId, spu.getPsplist());
                    }
                    //增加SPU与商品属性的关系
                    if (spu.getPppropertyList() != null && spu.getPppropertyList().size() > 0) {
                        proProductPropertyDao.insertByBatch(spuId, spu.getPppropertyList());
                    }
                    try {
                        //判断商品所有的三级类目是否在店铺里面，如果存在，则增加商品和店铺的关系
                        ProStoreCategory psc = new ProStoreCategory();
                        psc.setThirdCategoryId(spu.getThirdCategoryId());
                        ProStoreCategory dbPsc = proStoreCategoryDao.getEntityByObj(psc);
                        if (dbPsc != null) {
                            ProStore store = proStoreDao.getEntityById(dbPsc.getStoreId());
                            ProStoreProd storeProd = new ProStoreProd();
                            storeProd.setSpuId(spu.getSpuId());
                            storeProd.setStoreName(store.getStoreName());
                            storeProd.setEnName(store.getEnName());
                            storeProd.setStoreId(store.getStoreId());
                            storeProd.setState(1);
                            storeProd.setIsDel(0);
                            storeProd.setIsHide(0);
                            storeProd.setStorePrice(new BigDecimal(0));
                            storeProd.setThirdCategoryId(spu.getThirdCategoryId());
                            proStoreProdDao.insert(storeProd);
                        }
                    } catch (Exception e) {
                        throw new ServiceException("service-pms-ProProductService.addProProduct:{}", "添加商品与店铺对应关系失败", e);
                    }
                }
            } catch (Exception e) {
                throw new ServiceException("service-pms-ProProductService.addSpu:{}", spu == null ? "" : spu.toString(), e);
            }
        }
        addToResult(spuId, resultObj);
    }

    /**
     * 增加SPU对应的图片地址
     *
     * @param resultObj
     * @param imageses
     */
    public void addImage(JsonObject resultObj, int spuId, ProImages[] imageses) {
        List<ProImages> list = Arrays.asList(imageses);
        int count = imagesDao.insertByBatch(list);
        if (count > 0) {
            //修改SPU对应的商品状态
            ProSpu spu = new ProSpu();
            spu.setSpuId(spuId);
            spu.setIsUploadImg((short) 1);
            proSpuDao.update(spu);
        }
        addToResult(count, resultObj);
    }

    /**
     * 根据spuId删除spu信息，同时查找对应spu下得sku，置为删除状态
     *
     * @param resultObj
     * @param spuId
     */
    public void deleteSpu(JsonObject resultObj, int spuId) {
        ProSpu spu = new ProSpu();
        boolean suc = false;
        spu.setSpuId(spuId);
        spu.setState(Constant.COD_UNVALID);
        spu.setDeleteTime(new Date());
        try {
            int count = proSpuDao.update(spu);
            if (count > 0) {
                SimpleProduct product = new SimpleProduct();
                product.setSpuId(spuId);
                List<SimpleProduct> list = proProductDao.getListByObj(product);
                if (list != null && list.size() > 0) {
                    //批量修改spuID对应下得sku
                    int[] ids = new int[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        ids[i] = list.get(i).getProId().intValue();
                    }
                    int total = proProductDao.delSkuByIds(ids);
                    if (total > 0) {
                        RedisSearchInfoUtil.addSearchInfo(ids);
                    }
                }
            }
            suc = true;
        } catch (Exception e) {
            throw new ServiceException("service-pms-ProProductService.deleteSpu:{}", String.valueOf(spuId), e);
        }
        addToResult(suc, resultObj);
    }

    /**
     * 根据条件，分页获取SPU列表，主要管理后台使用
     *
     * @param resultObj
     * @param dto
     * @param pageInfo
     * @param order_name
     * @param order_form
     */
    public void getSpuPageByObj(JsonObject resultObj, SpuListDTO dto, PageInfo pageInfo, String order_name, String order_form) {

        if (StringUtils.isNotEmpty(dto.getProName())) {
            dto.setProName("%" + dto.getProName().trim() + "%");
        } else {
            dto.setProName(null);
        }
        StringBuilder orderBySql = new StringBuilder();
        if (!"".equals(order_name) && null != order_name
                && !"".equals(order_form) && null != order_form) {
            orderBySql.append(" order by ").append(order_name)
                    .append(" ").append(order_form);
        }

        PagerControl<SpuListDTO> pages = proSpuDao.getListSpuPage(dto, pageInfo, null, orderBySql.toString());
        addToResult(pages, resultObj);
    }

    /**
     * 通过商品ID获取商品详细信息，包括商品描述等
     *
     * @param resultObj
     * @param proId
     */
    public void getProDetailByProId(JsonObject resultObj, int proId) {
        addToResult(proDetailDao.getEntityById(proId), resultObj);
    }

    /**
     * 修改商品详细信息，detail表
     *
     * @param resultObj
     * @param detail
     */
    public void updateProDetail(JsonObject resultObj, ProDetail detail) {
        boolean suc = false;
        if (detail != null) {
            int count = proDetailDao.update(detail);
            if (count > 0) {
                suc = true;
            }
        }
        addToResult(suc, resultObj);
    }

    /**
     * 根据spu ID获取spu信息
     *
     * @param resultObj
     * @param spuId
     */
    public void getSpuById(JsonObject resultObj, int spuId) {
        ProSpu spu = proSpuDao.getEntityById(spuId);
        addToResult(spu, resultObj);
    }

    /**
     * 根据SPU ID 获取SPU相关信息
     * 业务场景：商品编辑 信息包括：spu信息、商品详情、图片详情、固有属性
     *
     * @param resultObj
     * @param spuId
     */
    public void getAllSpuById(JsonObject resultObj, int spuId) {
        ProSpu spu = null;
        if (spuId > 0) {
            spu = getSpuDetail(spuId);
        }
        addToResult(spu, resultObj);
    }

    /**
     * 获取SPU详情数据，包括SPU、detail、images、固有属性等
     *
     * @param spuId
     * @return
     */
    private ProSpu getSpuDetail(int spuId) {
        ProSpu spu = proSpuDao.getEntityById(spuId);
        if (spu != null) {
            //获取SPU 详情
            ProDetail detail = proDetailDao.getEntityById(spuId);
            spu.setDetail(detail);
            //获取图片详情
            ProImages images = new ProImages();
            images.setSpuId(spuId);
            List<ProImages> imageses = imagesDao.getListByObj(images);
            spu.setImageses(imageses);
            List<ProSelfProperty> properties = productCacheDao.getSpuSelfPropCacheBySpuId(spuId);
            spu.setPsplist(properties);
        }
        return spu;
    }

    /**
     * 通过三级类目ID获取对应类目下的SPU列表
     *
     * @param resultObj
     * @param categoryId
     */
    public void getListByThridCatId(JsonObject resultObj, Integer categoryId) {
        ProSpu product = new ProSpu();
        product.setThirdCategoryId(categoryId);
        List<ProSpu> list = proSpuDao.getListByObj(product);
        addToResult(list, resultObj);
    }

    /**
     * 通过商品SPU ID 和属性值ID 获取对应的图片地址
     *
     * @param resultObj
     * @param spuId
     * @param provId    属性值ID
     */
    public void getImagesById(JsonObject resultObj, int spuId, int provId) {
        ProImagesKey key = new ProImages();
        key.setSpuId(spuId);
        key.setProProvId(provId);
        ProImages images = imagesDao.getEntityById(key);
        addToResult(images, resultObj);
    }

    /**
     * 更新商品标题
     *
     * @param resultObj
     * @param dto
     */
    public void updateProductNames(JsonObject resultObj, ProductNamesDto dto) {
        boolean rs = proSpuDao.updateSpuProductNames(dto) > 0 && proProductDao.updateSkuProductNamesBySpuId(dto) > 0;
        addToResult(rs, resultObj);
    }

    /**
     * 更新公共属性
     *
     * @param resultObj
     * @param dto
     */
    public void updatePublicParas(JsonObject resultObj, ProductPublicParasDto dto) {
        boolean rs = proSpuDao.updateSpuProductPublicParas(dto) > 0 && proProductDao.updateSkuProductPublicParas(dto) > 0;
        addToResult(rs, resultObj);
    }

    /**
     * 更新商品详情
     *
     * @param resultObj
     * @param detailDTO
     */
    public void updateProDetailByDto(JsonObject resultObj, ProductDetailDTO detailDTO) {
        addToResult(proDetailDao.updateProductDetailFromDto(detailDTO) > 0, resultObj);
    }

    public void updateSelfProperties(JsonObject resultObj, Map<String, Object> mapDto) {
        int spuId = Integer.parseInt((String) mapDto.get("spuId"));
        String editorRec = (String) mapDto.get("editorRec");

        ProductDetailDTO dto = new ProductDetailDTO();
        dto.setSpuId(spuId);
        dto.setEditorRec(editorRec);

        proDetailDao.updateEditorRec(dto);

        List<String> pspids = (List<String>) mapDto.get("pspids");

        if (pspids != null && pspids.size() > 0) {
            int lth = pspids.size();
            List<String> selfPropNames = (List<String>) mapDto.get("selfPropNames");
            List<String> selfPropValues = (List<String>) mapDto.get("selfPropValues");
            for (int i = 0; i < lth; i++) {
                int pspId = Integer.parseInt(pspids.get(i));
                String selfPropName = selfPropNames.get(i);
                String selfPropValue = selfPropValues.get(i);
                if (pspId > 0) {
                    if (StringUtils.isNotBlank(selfPropName) && StringUtils.isNotBlank(selfPropValue)) {
                        ProSelfProperty proSelfProperty = new ProSelfProperty();
                        proSelfProperty.setId(pspId);
                        proSelfProperty.setSelfPropName(selfPropName);
                        proSelfProperty.setSelfPropValue(selfPropValue);
                        proSelfPropertyDao.update(proSelfProperty);
                    } else {
                        proSelfPropertyDao.delete(pspId);
                    }

                } else {
                    if (StringUtils.isNotBlank(selfPropName) && StringUtils.isNotBlank(selfPropValue)) {
                        ProSelfProperty proSelfProperty = new ProSelfProperty();
                        proSelfProperty.setId(null);
                        proSelfProperty.setSelfPropName(selfPropName);
                        proSelfProperty.setSelfPropValue(selfPropValue);
                        proSelfProperty.setCreateTime(new Date());
                        proSelfProperty.setSpuId(spuId);
                        proSelfPropertyDao.insert(proSelfProperty);
                    }
                }
            }

            productCacheDao.setSpuSelfPropCacheBySpuId(spuId);

        }

        addToResult(true, resultObj);

    }

    /**
     * 更改规格属性
     *
     * @param mapDto
     */
    private void updateForSku(Map<String, Object> mapDto){
        int spuId = Integer.parseInt((String) mapDto.get("spuId"));

        List<String> proPropIds = (List<String>) mapDto.get("proPropIds");
        List<String> proProProValues = (List<String>) mapDto.get("proProProValues");
        List<String> proPropIsmages = (List<String>) mapDto.get("proPropIsmages");
        List<String> proPropIsSkus = (List<String>) mapDto.get("proPropIsSkus");

        List<ProProductProperty> addList = new ArrayList<ProProductProperty>();

        List<String> proStrList = new ArrayList<String>();
        List<ProProductProperty> proPropertiesSku = new ArrayList<ProProductProperty>();
        List<String> oldList = new ArrayList<String>();
        List<String> delList = new ArrayList<String>();

        int lth = 0;

        if (spuId > 0 && proPropIds != null && (lth = proPropIds.size()) > 0) {
            // 查询所有该规格下原有的商品属性值，放到oldlist中
            ProProductProperty quertDto = new ProProductProperty();
            quertDto.setIsSku(Constant.IS_SKU_YES);
            quertDto.setSpuId(spuId);
            proPropertiesSku = proProductPropertyDao.getListByObj(quertDto);
            for (ProProductProperty p : proPropertiesSku) {
                String vids = p.getValueId();
                if (StringUtils.isNotBlank(vids)) {
                    String[] vidsArray = vids.split(",");
                    for (String vid : vidsArray) {
                        oldList.add(p.getProPropId() + ":" + vid);
                    }
                }
            }
            // 查询该规格下得所有商品
            List<SimpleProduct> listBySpuId = proProductDao.getListBySpuId(spuId);
            // 这段for循环 主要是 根据 前台页面传过来的spuId,属性ID，来更新 pro_product_property ，如果更新影响的记录条数为0，则需要插入，把要插入的数据，放到 addList
            for (int i = 0; i < lth; i++) {

                String proPropId = proPropIds.get(i);
                String proProProValue = proProProValues.get(i);
                String isImage = proPropIsmages.get(i);
                String isSku = proPropIsSkus.get(i);

                if (StringUtils.isNotBlank(proProProValue)) {
                    String[] vids = proProProValue.split(",");
                    for (String vid : vids) {
                        proStrList.add(proPropId + ":" + vid);
                    }

                }
                int upRows = proProductPropertyDao.updateBySpuAndPropId(spuId, Integer.parseInt(proPropId), proProProValue,null);
                if (upRows == 0 && StringUtils.isNotBlank(proProProValue)) {
                    ProProductProperty property = new ProProductProperty();
                    property.setSpuId(spuId);
                    property.setIsImage(Short.parseShort(isImage));
                    property.setIsSku(Short.parseShort(isSku));
                    property.setProPropId(Integer.parseInt(proPropId));
                    property.setValueId(proProProValue);
                    addList.add(property);
                }
            }

            if (addList.size() > 0) {
                proProductPropertyDao.insertByBatch(spuId, addList);
            }

            // 找出要下架的商品ID,并到到delList中
            if (oldList.size() > 0) {
                for (String oldstr : oldList) {
                    if (!proStrList.contains(oldstr)) {
                        delList.add(oldstr);
                    }
                }
            }
            if (delList.size() > 0) {
                List<Integer> delProIds = new ArrayList<Integer>();
                for (SimpleProduct pro : listBySpuId) {
                    String skuProStr = pro.getSkuPropertyStr();
                    if (StringUtils.isNotBlank(skuProStr)) {
                        for (String delStr : delList) {
                            if (Arrays.asList(skuProStr.split(";")).contains(delStr)) {
                                delProIds.add(pro.getId());
                                break;
                            }
                        }
                    }
                }
                if (delProIds != null && delProIds.size() > 0) {
                    List<SimpleProduct> list = proProductDao.getSimpleListByIds(ArrayUtils.toPrimitive(delProIds.toArray(new Integer[delProIds.size()])));
                    if (list != null && list.size() > 0) {
                        List<Integer> ids = new ArrayList<Integer>();
                        for (SimpleProduct sp : list) {
                            if (sp.getState() == Constant.PRODUCT_ON) {
                                ids.add(sp.getProId());
                            }
                        }
                        if (ids != null && ids.size() > 0) {
                            proProductDao.updateStateToOffByIds(ArrayUtils.toPrimitive(ids.toArray(new Integer[ids.size()])));
                        }
                    }
                }
            }
            productCacheDao.setSpuWithSkuPropsCacheById(spuId);
        }
    }

    /**
     *
     * 更改类目属性
     *
     * @param mapDto
     */
    private void updateForCate(Map<String, Object> mapDto){
        int spuId = Integer.parseInt((String) mapDto.get("spuId"));

        List<String> proPropIds = (List<String>) mapDto.get("proPropIds");
        List<String> proProProValues = (List<String>) mapDto.get("proProProValues");
        List<String> proPropIsmages = (List<String>) mapDto.get("proPropIsmages");
        List<String> proPropIsSkus = (List<String>) mapDto.get("proPropIsSkus");
        List<String> proPropTypes = (List<String>) mapDto.get("proPropTypes");

        List<String> propertyStrList = new ArrayList<String>();
        List<String> proStrListCate =  new ArrayList<String>();

        int lth = 0;

        List<ProProductProperty> addList = new ArrayList<ProProductProperty>();

        if (spuId > 0 && proPropIds != null && (lth = proPropIds.size()) > 0) {

            // 查询该规格下得所有商品
            List<SimpleProduct> listBySpuId = proProductDao.getListBySpuId(spuId);
            // 这段for循环 主要是 根据 前台页面传过来的spuId,属性ID，来更新 pro_product_property ，如果更新影响的记录条数为0，则需要插入，把要插入的数据，放到 addList
            for (int i = 0; i < lth; i++) {

                String proPropId = proPropIds.get(i);
                String proProProValue = proProProValues.get(i);
                short isSku = Short.parseShort(proPropIsSkus.get(i));
                String propType = proPropTypes.get(i);

                if ( StringUtils.isNotBlank(proProProValue) && ( "1".equals(propType) || "2".equals(propType) ) ) {
                    String[] vids = proProProValue.split(",");
                    for (String vid : vids) {
                        propertyStrList.add(proPropId + ":" + vid);
                        ProProperty property = propertyDao.getEntityById(Integer.parseInt(proPropId));
                        if (property != null && property.getIsFilter() == 1 && property.getIsSku() == Constant.IS_SKU_NO) {
                            proStrListCate.add(proPropId + ":" + vid);
                        }
                    }
                }

                if( "1".equals(propType) || "2".equals(propType) ){
                    int upRows = proProductPropertyDao.updateBySpuAndPropId(spuId, Integer.parseInt(proPropId), proProProValue,null);
                    if (upRows == 0 && StringUtils.isNotBlank(proProProValue) ) {
                        ProProductProperty property = new ProProductProperty();
                        property.setSpuId(spuId);
                        property.setIsImage(Short.parseShort(proPropIsmages.get(i)));
                        property.setIsSku(isSku);
                        property.setProPropId(Integer.parseInt(proPropId));
                        property.setValueId(proProProValue);
                        addList.add(property);
                    }
                }else{
                    int upRows = proProductPropertyDao.updateBySpuAndPropId(spuId, Integer.parseInt(proPropId), null,proProProValue);
                    if (upRows == 0 && StringUtils.isNotBlank(proProProValue) ) {
                        ProProductProperty property = new ProProductProperty();
                        property.setSpuId(spuId);
                        property.setIsImage(Short.parseShort(proPropIsmages.get(i)));
                        property.setIsSku(isSku);
                        property.setProPropId(Integer.parseInt(proPropId));
                        property.setValue(proProProValue);
                        addList.add(property);
                    }

                }

            }

            if (addList.size() > 0) {
                proProductPropertyDao.insertByBatch(spuId, addList);
            }

            String propertyStr = (proStrListCate.size() == 0) ? "" : StringUtils.join(proStrListCate, ',');
            ProSpu spuBean = new ProSpu();
            spuBean.setSpuId(spuId);
            spuBean.setPropertyString(propertyStr);
            proSpuDao.update(spuBean);


        }
    }


    /**
     * 更改商品属性
     *
     * @param resultObj
     * @param mapDto
     */
    public void updateProProdProperty(JsonObject resultObj, Map<String, Object> mapDto) {
        boolean updateSku = (Boolean) mapDto.get("updateSku");
        if(updateSku){
            updateForSku(mapDto);
        }else{
            updateForCate(mapDto);
        }
        addToResult(true, resultObj);
    }

    public void updatePrimaryImage(JsonObject resultObj, Map<String, Object> mapDto) {
        int spuId = Integer.parseInt((String) mapDto.get("spuId"));
        int proPid = Integer.parseInt((String) mapDto.get("proPid"));
        int proVid = Integer.parseInt((String) mapDto.get("proVid"));
        String primaryImageUrl = (String) mapDto.get("primaryImageUrl");
        int rs = 0;
        if (StringUtils.isNotBlank(primaryImageUrl)) {
            Map<String, Object> dataDto = new HashMap<String, Object>();
            dataDto.put("spuId", spuId);
            dataDto.put("proPid", proPid);
            dataDto.put("proVid", proVid);
            dataDto.put("primaryImageUrl", primaryImageUrl);
            dataDto.put("nowTime", new Date());
            rs = proImagesDao.updateDefaultImageUriByPkIds(dataDto);
        }
        addToResult(rs, resultObj);

    }

    public void updateOrInsertImages(JsonObject resultObj, Map<String, Object> mapDto) {
        int spuId = Integer.parseInt((String) mapDto.get("spuId"));
        int proPid = Integer.parseInt((String) mapDto.get("proPid"));
        int proVid = Integer.parseInt((String) mapDto.get("proVid"));
        String imageUrls = (String) mapDto.get("imageUrls");
        if (spuId == 0 || StringUtils.isBlank(imageUrls)) {
            addToResult(0, resultObj);
            return;
        }

        String[] imageUrlArray = imageUrls.split(",");

        ProImages images = new ProImages();
        images.setSpuId(spuId);
        images.setProPropId(proPid);
        images.setProProvId(proVid);
        images.setDefaultImageUri(imageUrlArray[0]);
        images.setImageUris(imageUrls);
        images.setCreateTime(new Date());
        images.setUpdateTime(new Date());

        int rs = 0;

        rs = imagesDao.update(images);
        if (rs == 0) {
            rs = imagesDao.insert(images);
        }
        if (rs > 0){
            ProSpu spu = new ProSpu();
            spu.setSpuId(spuId);
            spu.setIsUploadImg((short)1);
            proSpuDao.update(spu);

            Map<String, Object> skuDto = new HashMap<String, Object>();
            skuDto.put("spuId", spuId);
            skuDto.put("proPropVid", proVid);
            skuDto.put("defaultImageUri", images.getDefaultImageUri());
            skuDto.put("imageUris", images.getImageUris());
            proProductDao.updateImageBySpuAndPropVid(skuDto);
        }

        addToResult(rs, resultObj);

    }

    public List<ProSpu> getAllListSpu(){
        List<ProSpu> list = proSpuDao.getAllEntityObj();
        return list;
    }

    public boolean updatePropStr(int spuId,String propStr){
        int count = proSpuDao.updatePropStr(spuId,propStr);
        if (count>0){
            return true;
        }else {
            return false;
        }
    }

    public void getImages(JsonObject resultObj, Map<String, Object> mapDto) {
        int spuId = Integer.parseInt((String) mapDto.get("spuId"));
        int proVid = Integer.parseInt((String) mapDto.get("proVid"));
        ProImagesKey proImagesKey = new ProImagesKey();
        proImagesKey.setSpuId(spuId);
        proImagesKey.setProProvId(proVid);
        addToResult(imagesDao.getEntityById(proImagesKey), resultObj);
    }

}
