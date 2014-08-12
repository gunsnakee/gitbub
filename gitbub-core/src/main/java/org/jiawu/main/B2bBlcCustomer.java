package org.jiawu.main;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jiawuwu on 14-7-23.
 */
public class B2bBlcCustomer {
    public static void main(String[] args) throws ConfigurationException {
        XMLConfiguration config = new XMLConfiguration("/data/mygit/gitbub/src/main/resources/BLC_CUSTOMER.xml");

        XMLConfiguration xmlRole = new XMLConfiguration("/data/mygit/gitbub/src/main/resources/BLC_CUSTOMER_ROLE.xml");
        int initRoleId = 3500;

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        int initId = 296933;
        int initEmailNo = 1200;
        for(int i=0;i<300;i++){


            config.setProperty("RECORD("+i+").CUSTOMER_ID",initId+i);
            config.setProperty("RECORD("+i+").CREATED_BY",0);
            config.setProperty("RECORD("+i+").DATE_CREATED",date);
            config.setProperty("RECORD("+i+").DEACTIVATED",0);
            config.setProperty("RECORD("+i+").EMAIL_ADDRESS","mlb"+(initEmailNo+i)+"@meiliwan.com");
            config.setProperty("RECORD("+i+").PASSWORD","e19d5cd5af0378da05f63f891c7467af");
            config.setProperty("RECORD("+i+").PASSWORD_CHANGE_REQUIRED",0);
            config.setProperty("RECORD("+i+").RECEIVE_EMAIL",1);
            config.setProperty("RECORD("+i+").IS_REGISTERED",1);
            config.setProperty("RECORD("+i+").USER_NAME","mlb"+(initEmailNo+i)+"@meiliwan.com");
            config.setProperty("RECORD("+i+").LOCALE_CODE","zh");
            config.setProperty("RECORD("+i+").NICK_NAME","mlb"+(initEmailNo+i));
            config.setProperty("RECORD("+i+").IDENTITY","ROLE_USER");

            xmlRole.setProperty("RECORD("+i+").CUSTOMER_ROLE_ID",initRoleId+i);
            xmlRole.setProperty("RECORD("+i+").CUSTOMER_ID",initId+i);
            xmlRole.setProperty("RECORD("+i+").ROLE_ID",1);

        }

        config.save();
        xmlRole.save();




    }
}
