package com.meiliwan.emall.cms2.service;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.cms2.bean.TvPage;
import com.meiliwan.emall.cms2.dao.TvPageDao;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

@Service
public class TvPageService extends DefaultBaseServiceImpl {

    @Autowired
    private TvPageDao tvPageDao;
   
    public void getPageByObj(JsonObject resultObj, int id,String name, PageInfo pageInfo) {
    		
    		TvPage page = new TvPage();
    		if(id>0){
    			page.setId(id);
    		}
    		if(StringUtils.isNotBlank(name)){
    			page.setPageName("%" + name + "%");
    		}
        String orderBySql = " order by id desc";

        PagerControl<TvPage> pc = tvPageDao.getPagerByObj(page, pageInfo, null, orderBySql);
        addToResult(pc, resultObj);
    }
    
    public void getListValid(JsonObject resultObj) {
    	
	    	TvPage page = new TvPage();
	    	page.setState(GlobalNames.STATE_VALID);
	    	String orderBySql = " order by id desc";
	    	
	    	List<TvPage> pc = tvPageDao.getListByObj(page,"", orderBySql);
	    	addToResult(pc, resultObj);
    }
    
    public void getById(JsonObject resultObj, int id) {
		
		if(id<=0){
			throw new ServiceException("id can not less than zero");
		}
		TvPage tv = tvPageDao.getEntityById(id);
		addToResult(tv, resultObj);
}
    
    
    public void add(JsonObject resultObj, TvPage page) {
		
    		if(page==null){
    			throw new ServiceException("TvPage can not be null");
    		}
    		if(StringUtils.isBlank(page.getPageName())){
    			throw new ServiceException(" page.getPageName can not be empty");
    		}
    		tvPageDao.insert(page);
    }
    
    public void update(JsonObject resultObj, TvPage page) {
		
		if(page==null){
			throw new ServiceException("TvPage can not be null");
		}
		if(page.getId()<=0){
			throw new ServiceException(" page.id can not less than zero");
		}
		if(StringUtils.isBlank(page.getPageName())){
			throw new ServiceException(" page.getPageName can not be empty");
		}
		tvPageDao.update(page);
    }
    
    public void delete(JsonObject resultObj, int id) {
		
		if(id<=0){
			throw new ServiceException(" page.id can not less than zero");
		}
		TvPage page = new TvPage();
		page.setId(id);
		page.setState(GlobalNames.STATE_INVALID);
		tvPageDao.update(page);
    }

	 public void updateProIds(JsonObject resultObj, int id,String proIds) {
			
			if(id<=0){
				throw new ServiceException("id can not less than zero");
			}
			TvPage tv = tvPageDao.getEntityById(id);
			String dbIds = tv.getProIds()==null?"":tv.getProIds();
			StringBuilder sb = new StringBuilder();
			sb.append(dbIds);
			if(sb.capacity()<=0){
				sb.append(proIds);
			}else{
				String pids[] = proIds.split(",");
				for (String pid : pids) {
					if(!dbIds.contains(pid)){
						sb.append(",").append(pid);
					}
				}
			}
			TvPage tvPage = new TvPage();
			tvPage.setId(id);
			if(sb.indexOf(",")==0){
				sb.deleteCharAt(0);
			}
			tvPage.setProIds(sb.toString());
			tvPageDao.update(tvPage);
	}
	 public static void main(String[] args) {
		 StringBuilder sb = new StringBuilder(",");
		 System.out.println(sb.indexOf(","));
	}
	public void updateUp(JsonObject resultObj, int id,String proId) {
		 updateUpOrDown(id,proId,true);
	}
	 
	public void updateDown(JsonObject resultObj, int id,String proId) {
		 updateUpOrDown(id,proId,false);
	}
	 
	 public void updateUpOrDown( int id,String proId,boolean up) {
			
			if(id<=0){
				throw new ServiceException("id can not less than zero");
			}
			TvPage tv = tvPageDao.getEntityById(id);
			String prosIds = tv.getProIds();
			if(StringUtils.isBlank(prosIds)){
				return ;
			}
			String ids[] = prosIds.split(",");
			for (int i = 0; i < ids.length; i++) {
				if(ids[i].equals(proId)){
					if(i>0&&up){
						String tempContent=ids[i-1];
						ids[i-1]=proId;
						ids[i]=tempContent;
						break;
					}
					//向下
					if(i<ids.length-1&&!up){
						String tempContent=ids[i+1];
						ids[i+1]=proId;
						ids[i]=tempContent;
						break;
					}
				}
			}
			StringBuilder sb = new StringBuilder();
			for (String string : ids) {
				sb.append(string).append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			tv.setProIds(sb.toString());
			tvPageDao.update(tv);
	}
	 
	 public void delPro(JsonObject resultObj, int id,String proId) {
		 if(id<=0){
				throw new ServiceException("id can not less than zero");
		}
		TvPage tv = tvPageDao.getEntityById(id);
		String prosIds = tv.getProIds();
		if(StringUtils.isBlank(prosIds)){
			return ;
		}
		if(prosIds.contains(proId)){
			prosIds=prosIds.replace(","+proId, "");
			prosIds=prosIds.replace(proId, "");
			if(prosIds.indexOf(",")==0){
				prosIds=prosIds.substring(1, prosIds.length());
			}
			if(prosIds.lastIndexOf(",")==prosIds.length()){
				prosIds=prosIds.substring(prosIds.length()-1);
			}
		}
		tv.setProIds(prosIds);
		tvPageDao.update(tv);
	}
	 
}
