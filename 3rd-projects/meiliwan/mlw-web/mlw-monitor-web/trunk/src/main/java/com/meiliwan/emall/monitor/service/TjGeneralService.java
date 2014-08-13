package com.meiliwan.emall.monitor.service;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.monitor.bean.*;
import com.meiliwan.emall.monitor.common.TjGeneralIndex;
import com.meiliwan.emall.monitor.dao.impl.TjGeneralDaoImpl;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 统计服务
 * @author rubi
 *
 */
@Service
public class TjGeneralService {
	public static TjGeneralService tjGeneralService = new TjGeneralService();

    public static TjGeneralService getInstance(){
        return tjGeneralService;
    }
	private static TjGeneralDaoImpl tjGeneralDaoImpl = new TjGeneralDaoImpl();

    MLWLogger logger = MLWLoggerFactory.getLogger(getClass());
    @SuppressWarnings("unchecked")
     public  Map<String, TjGeneralViewVo> getAllTjGeneral(List<String> dateList,TjGeneralIndex tjIndex) throws ServiceException {
        // List<TjGeneralViewVo> tjgvvList = new ArrayList<TjGeneralViewVo>();
        Map<String, TjGeneralViewVo> map = new LinkedHashMap<String, TjGeneralViewVo>();  //保持顺序
        List<List<TjGeneral>>   lists = new ArrayList<List<TjGeneral>>();
        for(String dateStr:dateList){
            List<TjGeneral> list = tjGeneralDaoImpl.getAllTjGeneralByDateStr(dateStr,tjIndex);
            lists.add(list);
            //先准备来源行
            for(TjGeneral tjg:list){
                TjGeneralViewVo tjgv = map.get(tjg.getHost());
                if(tjgv==null){
                    tjgv = new TjGeneralViewVo();
                    List<Integer> dateIndexValue = new ArrayList<Integer>();
                    tjgv.setDateIndexValue(dateIndexValue);
                    tjgv.setHost(tjg.getHost());
                    map.put(tjg.getHost(),tjgv);
                }
            }
        }
        //再给来源行加上每天的指标数据 没有的情况只能补0
        for(int i=0;i<lists.size();i++){
            List<TjGeneral> list = lists.get(i);
            if(list==null || list.size()==0){//当天没有任何指标，则全部补零
                for(TjGeneralViewVo tjgv :map.values())  {
                    tjgv.getDateIndexValue().add(0);
                }
            } else{
                for(TjGeneralViewVo tjgv :map.values())  {
                    boolean addValue = false;
                    for(TjGeneral tjg :list){
                        if(tjgv.getHost().equals(tjg.getHost())){
                            setIndexValue(tjgv,tjg,tjIndex);
                            addValue = true;
                        }
                    }
                    if(!addValue){  //当天没有任何指标，则该来源补零
                        tjgv.getDateIndexValue().add(0);
                    }
                }
            }
        }

        return map;
    }

    /**
     * 获取综合 指标数据
     * @param dateList
     * @param tjIndex
     * @return
     * @throws ServiceException
     */
    @SuppressWarnings("unchecked")
    public  Map<String, TjGeneralViewVo> getAllTjGeneralForDayReport(List<String> dateList,TjGeneralIndex tjIndex) throws ServiceException {
        // List<TjGeneralViewVo> tjgvvList = new ArrayList<TjGeneralViewVo>();
        Map<String, TjGeneralViewVo> map = new LinkedHashMap<String, TjGeneralViewVo>();  //保持顺序
        List<List<TjGeneral>>   lists = new ArrayList<List<TjGeneral>>();
        for(String dateStr:dateList){
            List<TjGeneral> list = tjGeneralDaoImpl.getAllTjGeneralByDateStr(dateStr,tjIndex);
            lists.add(list);
            //先准备来源行
            for(TjGeneral tjg:list){
                TjGeneralViewVo tjgv = map.get(tjg.getHost());
                if(tjgv==null){
                    tjgv = new TjGeneralViewVo();
                    List<TjGeneral> dateIndexValues = new ArrayList<TjGeneral>();
                    tjgv.setDateIndexValues(dateIndexValues);
                    tjgv.setHost(tjg.getHost());
                    map.put(tjg.getHost(),tjgv);
                }
            }
        }
        //再给来源行加上每天的指标数据 没有的情况只能补0
        for(int i=0;i<lists.size();i++){
            List<TjGeneral> list = lists.get(i);
            if(list==null || list.size()==0){//当天没有任何指标，则全部补零
                TjGeneral tjg = new TjGeneral(0,0,0,0,0,0);
                for(TjGeneralViewVo tjgv :map.values())  {
                    tjgv.getDateIndexValues().add(tjg);
                }
            } else{
                for(TjGeneralViewVo tjgv :map.values())  {
                    boolean addValue = false;
                    for(TjGeneral tjg :list){
                        if(tjgv.getHost().equals(tjg.getHost())){
                           // setIndexValue(tjgv,tjg,tjIndex);
                            tjgv.getDateIndexValues().add(tjg);
                            addValue = true;
                        }
                    }
                    if(!addValue){  //当天没有任何指标，则该来源补零
                        TjGeneral tjg = new TjGeneral(0,0,0,0,0,0);
                        tjgv.getDateIndexValues().add(tjg);
                    }
                }
            }
        }

        return map;
    }

    /**
     * 获取综合指标 合计 数据
     * @param dateList
     * @return
     * @throws ServiceException
     */
    @SuppressWarnings("unchecked")
    public  TjGeneralViewVo getAllTjGeneralTotalForDayReport(List<String> dateList) throws ServiceException {
        TjGeneralViewVo tjgv = new TjGeneralViewVo();
        tjgv.setHost("合计");
        tjgv.setDateIndexValues( new ArrayList<TjGeneral>());
        for(String dateStr:dateList){
            TjGeneral tjg = tjGeneralDaoImpl.getIndexsTotalByDateStr(dateStr);
            if(tjg!=null){
               tjgv.getDateIndexValues().add(tjg);
            }else {
                //当天无指标的时候全部补零
                tjg = new TjGeneral(0,0,0,0,0,0);
                tjg.setStatDay(dateStr);
                tjgv.getDateIndexValues().add(tjg);
            }
        }
        return tjgv;
    }

    private void setIndexValue(TjGeneralViewVo tjgv, TjGeneral tjg,TjGeneralIndex tjIndex){
         if(tjgv!=null && tjg!=null){
             int indexValue = getIndexValue(tjg, tjIndex);
             tjgv.getDateIndexValue().add(indexValue);
         }
    }

    private int getIndexValue(TjGeneral tjg,TjGeneralIndex tjIndex){
        int indexValue = 0;
        if (tjg!=null && tjIndex!=null){
            try{
                if(TjGeneralIndex.PV.getCode().equals(tjIndex.getCode())){
                      indexValue = tjg.getPv();
                }else if(TjGeneralIndex.UV.getCode().equals(tjIndex.getCode())){
                    indexValue = tjg.getUv();
                }else if(TjGeneralIndex.LOGIN.getCode().equals(tjIndex.getCode())){
                    indexValue = tjg.getLogin();
                }else if(TjGeneralIndex.REGISTER.getCode().equals(tjIndex.getCode())){
                    indexValue = tjg.getRegister();
                }  else if(TjGeneralIndex.ORD.getCode().equals(tjIndex.getCode())){
                    indexValue = tjg.getOrd();
                }
            }catch (Exception e){
            }
        }
        return indexValue;
    }


}

