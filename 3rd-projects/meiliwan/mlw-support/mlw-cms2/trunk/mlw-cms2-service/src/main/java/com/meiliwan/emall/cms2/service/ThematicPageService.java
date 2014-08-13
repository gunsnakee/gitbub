package com.meiliwan.emall.cms2.service;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meiliwan.emall.cms2.bean.ThematicModel;
import com.meiliwan.emall.cms2.bean.ThematicPage;
import com.meiliwan.emall.cms2.bean.ThematicPageModel;
import com.meiliwan.emall.cms2.bean.ThematicTemplate;
import com.meiliwan.emall.cms2.dao.ThematicModelDao;
import com.meiliwan.emall.cms2.constant.Constant;
import com.meiliwan.emall.cms2.dao.ThematicPageDao;
import com.meiliwan.emall.cms2.dao.ThematicPageModelDao;
import com.meiliwan.emall.cms2.dao.ThematicTemplateDao;
import com.meiliwan.emall.cms2.util.ParseFtlUtil;
import com.meiliwan.emall.cms2.vo.*;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.bean.FragmentName;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.pms.client.ProProductClient;
import com.meiliwan.emall.pms.dto.SimpleProIfStock;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import com.meiliwan.emall.sp2.client.ActivityInterfaceClient;

import freemarker.template.TemplateException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * User: wuzixin
 * Date: 14-4-14
 * Time: 上午10:44
 */
@Service
public class ThematicPageService extends DefaultBaseServiceImpl {

    @Autowired
    private ThematicPageDao pageDao;
    @Autowired
    private ThematicTemplateDao templateDao;
    @Autowired
    private ThematicPageModelDao pageModelDao;
    @Autowired
    private ThematicModelDao modelDao;
    @Autowired
    private ThematicModelDao thematicModelDao;
    @Autowired
    private ThematicTemplateDao thematicTemplateDao;

    private final static MLWLogger  logger = MLWLoggerFactory.getLogger(ThematicPageService.class);
    private final static String cacheIdKey = "THEMATIC";
    private final static String THEMATIC_HTML5 = "THEMATIC_HTML5";
    private final static String THEMATIC_ANDROID = "THEMATIC_ANDROID";

    /**
     * 增加专题页
     *
     * @param resultObj
     * @param page
     */
    public void addPage(JsonObject resultObj, ThematicPage page) {
        int pageId = 0;
        if (page != null) {
            pageDao.insert(page);
            pageId = page.getPageId();
        }
        addToResult(pageId, resultObj);
    }

    /**
     * 增加专题页对应区域，列如：图片区或者商品区
     */
    public void addPageModel(JsonObject resultObj, ThematicPageModel model) {
        int id = 0;
        if (model != null) {
            pageModelDao.insert(model);
            id = model.getId();
        }
        addToResult(id, resultObj);
    }

    /**
     * 修改专题页
     *
     * @param resultObj
     * @param page
     */
    public void updatePage(JsonObject resultObj, ThematicPage page) {
        int count = pageDao.update(page);
        addToResult(count, resultObj);
    }

    /**
     * 修改专题页与模块关系表，也就是pageModel表
     *
     * @param resultObj
     * @param model
     */
    public void updatePageModel(JsonObject resultObj, ThematicPageModel model) {
        int count = pageModelDao.update(model);
        addToResult(count, resultObj);
    }
    /**
     * 修改专题页与模块关系表，也就是pageModel表
     *
     * @param resultObj
     * @param model
     */
    public void updateThematicModel(JsonObject resultObj, ThematicModel model) {
    	int count = thematicModelDao.update(model);
    	addToResult(count, resultObj);
    }

    /**
     * 根据关系ID，删除专题页相关区域
     *
     * @param resultObj
     * @param id
     */
    public void updatePMHide(JsonObject resultObj, int id) {
        int count = pageModelDao.updatePMHide(id);
        addToResult(count, resultObj);
    }

    /**
     * 修改专题页jsonData 字段数据
     *
     * @param resultObj
     * @param pageId
     * @param json
     */
    public void updataPagdJsonData(JsonObject resultObj, int pageId, String json) {
        int count = pageDao.updataPagdJsonData(pageId, json);
        addToResult(count, resultObj);
    }

    /**
     * 修改专题页与区域关系表的json数据
     *
     * @param resultObj
     * @param id
     * @param json
     */
    public void updatePageModelJsonData(JsonObject resultObj, int id, String json) {
        int count = pageModelDao.updatePageModelJsonData(id, json);
        addToResult(count, resultObj);
    }

    /**
     * 根据专题页ID，修改专题包含的商品IDS
     *
     * @param resultObj
     * @param pageId
     * @param ids
     */
    public void updataPageProIds(JsonObject resultObj, int pageId, String ids) {
        int count = pageDao.updataPageProIds(pageId, ids);
        addToResult(count, resultObj);
    }

    /**
     * 通过pageId获取专题实体
     *
     * @param resultObj
     * @param pageId
     */
    public void getPageById(JsonObject resultObj, int pageId) {
        addToResult(pageDao.getEntityById(pageId), resultObj);
    }

    /**
     * 通过pageId获取专题实体
     *
     * @param resultObj
     * @param pageId
     */
    public void getPageByEnName(JsonObject resultObj, String enName) {
    		ThematicPage page = new ThematicPage();
    		page.setEnName(enName);
        addToResult(pageDao.getEntityByObj(page), resultObj);
    }
    
    /**
     * 获取专题列表，分页查询
     *
     * @param resultObj
     * @param page
     * @param pageInfo
     * @param order_name
     * @param order_form
     */
    public void getPageByObj(JsonObject resultObj, ThematicPage page, PageInfo pageInfo, String order_name, String order_form) {

        if (page.getPageName() != null && !Strings.isNullOrEmpty(page.getPageName().trim())) {
            page.setPageName("%" + page.getPageName().trim() + "%");
        }
        StringBuilder orderBySql = new StringBuilder();
        if (!"".equals(order_name) && null != order_name
                && !"".equals(order_form) && null != order_form) {
            orderBySql.append(" order by ").append(order_name)
                    .append(" ").append(order_form);
        }

        PagerControl<ThematicPage> pc = pageDao.getPagerByObj(page, pageInfo, null, orderBySql.toString());
        addToResult(pc, resultObj);
    }

    /**
     * 获取全部专题模板列表
     *
     * @param resultObj
     */
    public void getTemplateList(JsonObject resultObj) {
        List<ThematicTemplate> list = templateDao.getAllEntityObj();
        addToResult(list, resultObj);
    }

    /**
     * 删除专题页
     *
     * @param resultObj
     * @param pageId
     */
    public void delPage(JsonObject resultObj, int pageId) {
        int count = pageDao.deletePage(pageId);
        addToResult(count, resultObj);
    }

    /**
     * 根据专题ID获取专题对应的区域列表
     *
     * @param resultObj
     * @param pageId
     */
    public void getPMListByPageId(JsonObject resultObj, int pageId) {
        List<ThematicPageModel> list = pageModelDao.getPMListByPageId(pageId);
        addToResult(list, resultObj);
    }

    /**
     * 根据专题页与区域关系id，获取页面相关信息,返回PageMode实体
     *
     * @param resultObj
     * @param id
     */
    public void getPageModeById(JsonObject resultObj, int id) {
        addToResult(pageModelDao.getEntityById(id), resultObj);
    }

    /**
     * 根据模块实体，获取模块列表
     *
     * @param resultObj
     * @param model
     */
    public void getModelListByEntity(JsonObject resultObj, ThematicModel model) {
        List<ThematicModel> list = modelDao.getListByObj(model);
        addToResult(list, resultObj);
    }

    public void getModelByModelName(JsonObject resultObj, String name) {
    		ThematicModel model = new ThematicModel();
    		model.setModelName(name);
        List<ThematicModel> list = modelDao.getListByObj(model);
        if(list!=null&&list.size()>0){
        		addToResult(list.get(0), resultObj);
        		return ;
        }
        addToResult(model, resultObj);
    }
    public void getModelByModelId(JsonObject resultObj, int modelId) {
	    	ThematicModel model = thematicModelDao.getEntityById(modelId);
	    	addToResult(model, resultObj);
    }
    
    /**
     * 
     * @param pageState 页面状态
     * @param html5HeadImg html5头图
     * @param pageModelList pageModelList
     * @param proIfStockMap 商品列表
     */
    public void setThematicPageForHtml5(ThematicPage page,List<ThematicPageModel> pageModelList,Map<Integer,SimpleProIfStock> proIfStockMap){
    		
    		if(pageModelList==null||pageModelList.size()==0){
    			return ;
    		}
    		if(proIfStockMap == null){
    			return ;
    		}
    		List<ProductJsonVo> productJsonVoListAll = new ArrayList<ProductJsonVo>();
    		
    		for (ThematicPageModel thematicPageModel : pageModelList) {
    			if(thematicPageModel.isProArea()){
                ProductAreaJsonVo productAreaJsonVo = new Gson().fromJson(thematicPageModel.getJsonData(),ProductAreaJsonVo.class);
                if (productAreaJsonVo == null){
             	   		continue;
                }
                List<ProductJsonVo> productJsonVoList = productAreaJsonVo.getProList();
                if(productJsonVoList != null && productJsonVoList.size() > 0){
                    for(ProductJsonVo item:productJsonVoList){
                        SimpleProIfStock simpleProIfStock = proIfStockMap.get(item.getProId());
                        if(simpleProIfStock != null){
                        		//重新设置商品
                        		toProductJsonVo(item,simpleProIfStock);
                        		productJsonVoListAll.add(item);
                        }
                    }
                }
    			}
		}
    		 //获取对应的model
    		ThematicModel model = new ThematicModel();
    		model.setModelName("productAreaHtml5");
        ThematicModel thematicModel = thematicModelDao.getEntityByObj(model);
        
        String html = null;
        html=parseToHtml(page , productJsonVoListAll,
				thematicModel);
        logger.debug(html);    
        
        long setTime = System.currentTimeMillis();
        ThematicKV thematicKV = new ThematicKV();
        thematicKV.setContent(html);
        thematicKV.setSetTime(setTime);
        short pageState = getPageState(page);
        thematicKV.setPageState(pageState);
        	try {
			ShardJedisTool.getInstance().hset(JedisKey.global$inc, THEMATIC_HTML5, page.getEnName(), new Gson().toJson(thematicKV));
		} catch (JedisClientException e) {
			// TODO Auto-generated catch block
			logger.error(e, thematicKV, null);
		}
    }

    /**
     * 
     * @param pageState 页面状态
     * @param html5HeadImg html5头图
     * @param pageModelList pageModelList
     * @param proIfStockMap 商品列表
     */
    public void setThematicPageForAndroid(ThematicPage page,List<ThematicPageModel> pageModelList,Map<Integer,SimpleProIfStock> proIfStockMap){
    		
    		if(pageModelList==null||pageModelList.size()==0){
    			return ;
    		}
    		if(proIfStockMap == null){
    			return ;
    		}
    		List<ProductJsonVo> productJsonVoListAll = new ArrayList<ProductJsonVo>();
    		
    		for (ThematicPageModel thematicPageModel : pageModelList) {
    			if(thematicPageModel.isProArea()){
                ProductAreaJsonVo productAreaJsonVo = new Gson().fromJson(thematicPageModel.getJsonData(),ProductAreaJsonVo.class);
                if (productAreaJsonVo == null){
             	   		continue;
                }
                List<ProductJsonVo> productJsonVoList = productAreaJsonVo.getProList();
                if(productJsonVoList != null && productJsonVoList.size() > 0){
                    for(ProductJsonVo item:productJsonVoList){
                        SimpleProIfStock simpleProIfStock = proIfStockMap.get(item.getProId());
                        if(simpleProIfStock != null){
                        		//重新设置商品
                        		toProductJsonVo(item,simpleProIfStock);
                        		productJsonVoListAll.add(item);
                        }
                    }
                }
    			}
		}
    		 //获取对应的model
    		ThematicModel model = new ThematicModel();
    		model.setModelName("productAreaAndroid");
        ThematicModel thematicModel = thematicModelDao.getEntityByObj(model);
        
        String html = null;
        html=parseToHtml(page , productJsonVoListAll,
				thematicModel);
        logger.debug(html);    
        
        long setTime = System.currentTimeMillis();
        ThematicKV thematicKV = new ThematicKV();
        thematicKV.setContent(html);
        thematicKV.setSetTime(setTime);
        short pageState = getPageState(page);
        thematicKV.setPageState(pageState);
        	try {
			ShardJedisTool.getInstance().hset(JedisKey.global$inc, THEMATIC_ANDROID, page.getEnName(), new Gson().toJson(thematicKV));
		} catch (JedisClientException e) {
			// TODO Auto-generated catch block
			logger.error(e, thematicKV, null);
		}
    }
    
	private String parseToHtml(ThematicPage page,
			List<ProductJsonVo> productJsonVoListAll,
			ThematicModel thematicModel) {
		String html=null;
		PageImgJsonVo pageImgJsonVo = new Gson().fromJson(page.getJsonData(),PageImgJsonVo.class);
		short pageState = getPageState(page);
		String html5HeadImg=pageImgJsonVo.getHtml5HeadImg();
		
		Map<String,Object> data = new HashMap<String, Object>();
        data.put("productJsonVoListAll",productJsonVoListAll);
        data.put("html5HeadImg",html5HeadImg);
        data.put("pageState",pageState);
        data.put("title",page.getPageName());
        try {
 	   		if(thematicModel!=null && !StringUtils.isBlank(thematicModel.getModelFtl())){
 	   			return html = ParseFtlUtil.parseFtl(thematicModel.getModelFtl(),data);
 	   		}
        } catch (IOException e) {
            logger.info(e.toString(),null,"");
        } catch (TemplateException e) {
           logger.info(e.toString(),null,"");
        }
        return null;
	}
    //获取
    public void getThematicPageHtml(JsonObject jsonObject,String thematicPageEnName){

        boolean flushFlag = false;
       // 获取页面
       ThematicPage queryPage = new ThematicPage();
       queryPage.setEnName(thematicPageEnName);
       ThematicPage page = pageDao.getEntityByObj(queryPage);

        if(page == null || page.getPageId() <=0){
        		addToResult(flushFlag,jsonObject);
        		return ;
        }
        short pageState = getPageState(page);

        //存储锚点图片
        List<String> anchorList = new ArrayList<String>();
        PageImgJsonVo pageImgJsonVo = new Gson().fromJson(page.getJsonData(),PageImgJsonVo.class);
        if(pageImgJsonVo==null){
        		logger.info("pageImgJsonVo is null", null, null);
        		return ;
        }
        anchorList.add(pageImgJsonVo.getMdHeadImg());
        int anchorIndex = 1;
        //获取商品信息
        Map<Integer,SimpleProIfStock> proIfStockMap = getProductList(page.getProIds());
        StringBuilder stringBuilder = new StringBuilder(1024);
        //获取页面和Model的关系表
        List<ThematicPageModel> pageModelList = pageModelDao.getPMListByPageIdAndState(page.getPageId());
        //设置html5
        setThematicPageForHtml5(page,pageModelList,proIfStockMap);
        setThematicPageForAndroid(page,pageModelList,proIfStockMap);
        if(pageModelList != null && pageModelList.size() > 0){
            for(ThematicPageModel thematicPageModel:pageModelList){
                //商品区域
               if(thematicPageModel.getType()== Constant.PROD_AREA){
                   ProductAreaJsonVo productAreaJsonVo = new Gson().fromJson(thematicPageModel.getJsonData(),ProductAreaJsonVo.class);
                   if (productAreaJsonVo == null){
                	   		continue;
                   }
                   List<ProductJsonVo> productJsonVoList = productAreaJsonVo.getProList();
                   if(productJsonVoList != null && productJsonVoList.size() > 0){
                       for(ProductJsonVo item:productJsonVoList){
                           if(proIfStockMap != null){
                               SimpleProIfStock simpleProIfStock = proIfStockMap.get(item.getProId());
                               if(simpleProIfStock != null){
                            	   		toProductJsonVo(item,simpleProIfStock);
                               }
                           }
                       }
                   }
                   //获取对应的model
                   ThematicModel thematicModel = thematicModelDao.getEntityById(thematicPageModel.getModelId());
                   Map<String,Object> data = new HashMap<String, Object>();
                   data.put("productAreaJsonVo",productAreaJsonVo);
                   data.put("anchorId",anchorIndex);
                   anchorIndex ++;
                   String html = "";
                   try {
                	   		if(thematicModel!=null){
                	   			html = ParseFtlUtil.parseFtl(thematicModel.getModelFtl(),data);
                	   		}
                   } catch (IOException e) {
                       logger.error(e,productAreaJsonVo,"");
                   } catch (TemplateException e) {
                       logger.error(e,productAreaJsonVo,"");
                   }
                  stringBuilder.append(html);
                  anchorList.add(productAreaJsonVo.getAnchorPic());
               }else if(thematicPageModel.getType() == Constant.PIC_AREA){
                   //图片区域
                   PicAreaJsonVo picAreaJsonVo = new Gson().fromJson(thematicPageModel.getJsonData(),PicAreaJsonVo.class);
                   if(picAreaJsonVo==null){
                	   		continue;
                   }
                   //获取对应的model
                   ThematicModel thematicModel = thematicModelDao.getEntityById(thematicPageModel.getModelId());
                   Map<String,Object> data = new HashMap<String, Object>();
                   data.put("picAreaJsonVo",picAreaJsonVo);
                   
                   anchorList.add(picAreaJsonVo.getAnchorPic());
                   data.put("anchorId",anchorIndex);
                   anchorIndex ++;
                   String html = "";
                   try {
                	   		if(thematicModel!=null){
                	   			html = ParseFtlUtil.parseFtl(thematicModel.getModelFtl(),data);
                	   		}
                   } catch (IOException e) {
                       logger.error(e,picAreaJsonVo,"");
                   } catch (TemplateException e) {
                       logger.error(e,picAreaJsonVo,"");
                   }
                   stringBuilder.append(html);
               }
            }
        }

        //拼装页面
        anchorList.add(pageImgJsonVo.getReturnTopImg());
        Map<String,Object> pageData = new HashMap<String, Object>();
        pageData.put("title",page.getTitle());
        pageData.put("keyword",page.getKeyword());
        pageData.put("desc",page.getDescp());
        pageData.put("pageParam",pageImgJsonVo);
        pageData.put("areaContent",stringBuilder.toString());
        pageData.put("anchorList",anchorList);
        pageData.put("pageState",pageState);

        //TODO 获取通头和通底 封装碎片的获取方式,避免代码重复
        String topbar = getTopBar();
        pageData.put("topbar",topbar);
        String footer = getFooter();
        pageData.put("footer",footer);

        //获取简版的 数据
        ThematicTemplate template = thematicTemplateDao.getEntityById(page.getTemplateId());
        if(template != null){
            String pageHtml = "";
            try {
                pageHtml = ParseFtlUtil.parseFtl(template.getTemplateFtl(),pageData);
            } catch (Exception e) {
                logger.error(e, pageData, "");
            } 

            //写缓存和时间戳
            if(StringUtils.isNotEmpty(pageHtml)){
                try {
                    long setTime = System.currentTimeMillis();
                    ThematicKV thematicKV = new ThematicKV();
                    thematicKV.setContent(pageHtml);
                    thematicKV.setSetTime(setTime);
                    thematicKV.setPageState(pageState);
                    //因为活动到期也可以刷出页面,所以缓存置为永久有效
                    flushFlag = ShardJedisTool.getInstance().hset(JedisKey.global$inc, cacheIdKey, page.getEnName(), new Gson().toJson(thematicKV));
                } catch (JedisClientException e) {
                    logger.error(e,"专题页" + thematicPageEnName +"更新redis 异常","");
                }
            }
        }

       addToResult(flushFlag,jsonObject);
    }

	private String getFooter() {
		FragmentName fragmentName = FragmentName.valueOf("inc_footer");
		String footer = null;
		try {
		    footer = ShardJedisTool.getInstance().hget(JedisKey.global$inc,fragmentName.getFirstCode(),fragmentName.getSecondCode());
		} catch (JedisClientException e) {
		    logger.error(e,"专题页获取通底redis异常","");
		}
		return footer;
	}

	private String getTopBar() {
		String topbar=null;
		FragmentName fragmentName = FragmentName.valueOf("topbar");
		try {
		    topbar = ShardJedisTool.getInstance().hget(JedisKey.global$inc,fragmentName.getFirstCode(),fragmentName.getSecondCode());
		} catch (JedisClientException e) {
		    logger.error(e,"专题页获取通顶redis异常","");
		}
		return topbar;
	}

	private short getPageState(ThematicPage page) {
		short pageState = Constant.ON_ING;
		long startTime = page.getBeginTime()== null?0:page.getBeginTime().getTime();
		long endTime = page.getEndTime()==null?Long.MAX_VALUE:page.getEndTime().getTime();

		long now = System.currentTimeMillis();
		if(now - startTime < 0){
		    pageState = Constant.NOT_BEIGN;
		}else if(now - endTime > 0){
		    pageState = Constant.HAS_ENDED;
		}
		return pageState;
	}

    //动态获取商品信息并替换
    private Map<Integer,SimpleProIfStock> getProductList(String proIds){
       Map<Integer,SimpleProIfStock> map = new HashMap<Integer, SimpleProIfStock>();
       if(StringUtils.isBlank(proIds)){
    	   		return map;
       }
       String[] idsStr = proIds.split(",");
       if(idsStr.length > 0){
           int [] ids = new int[idsStr.length];
           for(int i = 0; i < idsStr.length; i++){
               ids[i] = Integer.valueOf(idsStr[i]);
           }
           // 获取商品信息
           List<SimpleProIfStock>  proList = ProProductClient.getSimpleListWithStockByIds(ids);
           // 获取活动价格
           Map<Integer,BigDecimal> activityPriceMap = ActivityInterfaceClient.getActProByProIdsForShowPrice(ids);
           if(proList != null && proList.size() > 0){
              for(SimpleProIfStock simpleProIfStock:proList){
                  if(activityPriceMap != null && activityPriceMap.size() > 0 ){
                      BigDecimal activityPrice = activityPriceMap.get(simpleProIfStock.getProId());
                      if(activityPrice != null){
                          simpleProIfStock.getProduct().setMlwPrice(activityPrice);
                      }
                  }
                  map.put(simpleProIfStock.getProId(),simpleProIfStock);
              }
           }
       }
        return map;
    }


    //转换商品实体对象
    private ProductJsonVo toProductJsonVo(ProductJsonVo productJsonVo, SimpleProIfStock simpleProIfStock){
      //假如有商品标题，商品标题不改变，使用他们编辑的
      if(StringUtils.isEmpty(productJsonVo.getProName())){
          productJsonVo.setProName(simpleProIfStock.getProName());
      }
        //重新设置 价格
        productJsonVo.setMlwPrice(simpleProIfStock.getMlwPrice().doubleValue());

        //重新设置库存
        productJsonVo.setStock(simpleProIfStock.getStock());

        //设置图片
        if(StringUtils.isEmpty(productJsonVo.getPicUrl())){
            productJsonVo.setPicUrl(simpleProIfStock.getProduct().getDefaultImageUri());
        }

        productJsonVo.setMarketPrice(simpleProIfStock.getMarketPrice().doubleValue());
        productJsonVo.setProId(simpleProIfStock.getProId());
        //重新设置商品状态
        productJsonVo.setState(simpleProIfStock.getState());

        if(StringUtils.isEmpty(productJsonVo.getAdvName())){
            productJsonVo.setAdvName(simpleProIfStock.getAdvName());
        }

        return productJsonVo;
    }

  //获取
    public void getThematicPageHtmlRedis(JsonObject resultObj)  {

       // 获取页面
       List<ThematicPage> list = pageDao.getAllEntityObj();
       for (ThematicPage page : list) {
    	   		if(page==null||StringUtils.isBlank(page.getProIds())){
    	   			continue;
    	   		}
    	   		try {
				getThematicPageHtml(resultObj,page.getEnName());
			} catch (Exception e) {
				logger.error(e, page, null);
			}
       }
    }
    
    
}
