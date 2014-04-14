package org.jiawu.io;

import java.io.File;
import java.io.IOException;

/**
 * Created by jiawuwu on 14-4-14.
 */
public class SimpleUse {


    static void printDir(String path){
        File file = new File(path);
        if( file!=null ){
            if(file.isDirectory()){
                for(String p:file.list()){
                    printDir(p);
                }
            }else{
                System.out.println(file.getName());
            }
        }
    }

    public static void main(String[] args){
        // 创建一个新文件
        File file = new File("/path/to/aaa.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // File类的两个常量
        System.out.println(File.separator);// 用来分隔同一个路径字符串中的目录. 例如： test/a.text 中的 /
        System.out.println(File.pathSeparator); // 分隔连续多个路径字符串的分隔符.例如： test/a.text;/test/b.text中的；

        // 删除一个文件
        file.delete();

        // 创建一个文件夹
        File dir = new File("/path/to");
        dir.mkdir();

        // 列车指定目录的全部文件，包括隐藏文件
        String[] list = dir.list();

        // 判断一个指定的路径是否是目录
        boolean directory = dir.isDirectory();

        // 搜索指定路径的全部内容，包括子目录下的内容
        printDir("/path/to");


    }


}
