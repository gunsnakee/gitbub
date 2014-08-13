package com.meiliwan.emall.commons.util;

import java.text.ParseException;  
import java.text.SimpleDateFormat;  
import java.util.Date;

import org.apache.commons.beanutils.Converter;  
  
/** 
 * 
 * @author xiong.yu 
 */  
public class DateConvert implements Converter {  
  
    public Object convert(Class arg0, Object arg1) {
    	if(arg1 instanceof Date){
    		return arg1;
    	}
        String p = (String)arg1;  
        if(p== null || p.trim().length()==0){  
            return null;  
        }     
        try{  
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
            return df.parse(p.trim());  
        }  
        catch(Exception e){  
            try {  
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
                return df.parse(p.trim());  
            } catch (ParseException ex) {  
                return null;  
            }  
        }  
          
    }  
  
}  
