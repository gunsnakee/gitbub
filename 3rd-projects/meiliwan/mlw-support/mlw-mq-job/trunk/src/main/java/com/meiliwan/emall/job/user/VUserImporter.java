package com.meiliwan.emall.job.user;

import com.meiliwan.emall.commons.web.UserLoginUtil;
import com.meiliwan.emall.mms.bean.UserPassport;
import com.meiliwan.emall.mms.client.UserPassportClient;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: jiawuwu
 * Date: 13-10-22
 * Time: 上午11:40
 * To change this template use File | Settings | File Templates.
 */
public class VUserImporter {


    private static final String SPLIT = "#@#@";

    private VUserImporter(){

    }

    public static final void main(String[] args) throws InterruptedException, IOException {

        String filePath="";

        if(args == null || args.length <=0){

            ClassLoader loader = VUserImporter.class.getClassLoader();

            URL url = loader.getResource("conf/vuser/user.txt");

            filePath = url.getPath();
        }else{
            filePath = args[0];
        }

        File f = new File(filePath);

        if(f==null || !f.exists() ||f.isDirectory()){
            System.out.println("===> 找不到文件 ");
            return;
        }

        BufferedReader bffReader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));


        String line = null;


        int idx = 0;

        while((line = bffReader.readLine())!=null){

            String[] cts = line.split(SPLIT);

            if(cts.length==2){

              idx++;

              String email = cts[0];
              String nickName = email;
              String pwd = cts[1];

              UserPassport user = new UserPassport();
              user.setUserName("mlw_"+ StringUtils.leftPad(UserPassportClient.getSeqId(), 10, '0'));
              user.setNickName(nickName);
              user.setPassword(UserLoginUtil.encrypt(pwd));
              user.setEmail(email);
              user.setState((short)1);
              user.setUserLevel((short)1);

              UserPassportClient.save(user);

             System.out.println("===> "+idx+" "+email);



            }
        }


        System.exit(0);
    }
}
