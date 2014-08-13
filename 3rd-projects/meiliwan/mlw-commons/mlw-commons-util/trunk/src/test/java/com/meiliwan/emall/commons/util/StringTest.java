package com.meiliwan.emall.commons.util;

import com.meiliwan.emall.commons.BaseTest;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: jiawuwu
 * Date: 13-10-10
 * Time: 上午10:43
 * To change this template use File | Settings | File Templates.
 */
public class StringTest extends BaseTest{


    @Test
    public void test(){
       String sa = "||";
       String sb = "123||";
        String sc = "|ere|";
        String sd = "||erer";
        String se = "1213|334|";
        String sf = "1213||3434";
        String sg = "|18176876269|keepUserName";
        String sh = "3434|dfdf|3434";


        System.out.println("~~~sa "+sa.split("\\|",-1).length);
        System.out.println("~~~sb "+sb.split("\\|",-1).length);
        System.out.println("~~~sc "+sc.split("\\|",-1).length);
        System.out.println("~~~sd "+sd.split("\\|",-1).length);
        System.out.println("~~~se "+se.split("\\|",-1).length);
        System.out.println("~~~sf "+sf.split("\\|",-1).length);
        System.out.println("~~~sg "+sg.split("\\|",-1).length);
        System.out.println("~~~sh "+sh.split("\\|",-1).length);

    }

}
