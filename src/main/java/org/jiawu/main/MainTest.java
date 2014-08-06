package org.jiawu.main;

/**
 * Created by jiawuwu on 14-7-23.
 */
public class MainTest {

    public static void main(String[] args) throws Exception{
        double[] dd = {1.0,3.9,1.6,9.8};
        double max = dd[0];
        for(double d:dd){
            if(d>max){
                max=d;
            }
        }
        System.out.println("max is "+max);
    }
}
