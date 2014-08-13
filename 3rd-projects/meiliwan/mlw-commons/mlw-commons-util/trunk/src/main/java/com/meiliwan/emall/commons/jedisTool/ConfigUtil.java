package com.meiliwan.emall.commons.jedisTool;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ZKClient;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.KeeperException;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: jiawuwu
 * Date: 13-10-4
 * Time: 上午9:54
 * To change this template use File | Settings | File Templates.
 */
public class ConfigUtil {

    private static final MLWLogger LOGGER = MLWLoggerFactory.getLogger(ConfigUtil.class);


    /**
     * redis可用节点列表
     */
    private volatile static List<ServerInfo> redisNodes = Collections.synchronizedList(new ArrayList<ServerInfo>(20));
    /**
     * 持久化存储可用节点列表
     */
    private volatile static List<ServerInfo> presistNodes = Collections.synchronizedList(new ArrayList<ServerInfo>(20));


    private static final Pattern nodePattern = Pattern.compile("^.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}:[0-9]+$");

    public static List<ServerInfo> getRedisNodes() {
        return redisNodes;
    }

    public static List<ServerInfo> getPresistNodes() {
        return presistNodes;
    }





    /**
     * 截取节点名称中的ip地址和端口
     *
     * @param nodeName
     * @return
     */
    public  static ServerInfo transTo(String nodeName) {

        if (StringUtils.isBlank(nodeName)) {
            return null;
        }
        if( ! nodePattern.matcher(nodeName).find() ){
            return null;
        }
        String ipAndP = nodeName.substring(1, nodeName.length());
        if (StringUtils.isBlank(ipAndP)) {
            return null;
        }
        String[] ipAndPArrs = ipAndP.split(":");
        if (ipAndPArrs.length != 2) {
            return null;
        }
        ServerInfo s = new ServerInfo();
        s.setName(nodeName);
        s.setHost(ipAndPArrs[0]);
        s.setPort(Integer.parseInt(ipAndPArrs[1]));
        return s;
    }

    /**
     * 添加节点到可用列表
     *
     * @param nodeName
     * @param s
     */
    public static void addNoteToList(String nodeName, ServerInfo s) {
        if (s == null) {
            return;
        }

        if (nodeName.startsWith("r")) {
            List<ServerInfo> rlist = ConfigUtil.getRedisNodes();
            boolean isHas = false;
            for (Iterator<ServerInfo> it = rlist.iterator(); it.hasNext(); ) {

                if (nodeName.equals(it.next().getName())) {
                    isHas = true;
                    break;
                }
            }
            if (!isHas) {
                LOGGER.debug(" ~~~~~~ add node "+nodeName);
                rlist.add(s);
            }
        } else if (nodeName.startsWith("p")) {

            List<ServerInfo> plist = ConfigUtil.getPresistNodes();
            boolean isHas = false;
            for (Iterator<ServerInfo> it = plist.iterator(); it.hasNext(); ) {

                if (nodeName.equals(it.next().getName())) {
                    isHas = true;
                    break;
                }
            }
            if (!isHas) {
                LOGGER.debug(" ~~~~~~ add node "+nodeName);
                plist.add(s);
            }
        }

    }

    /**
     * 从可用列表移出节点
     *
     * @param nodeName
     */
    public static void removeNodeFromList(String nodeName) {
        if (nodeName == null) {
            return;
        }
        if (nodeName.startsWith("r")) {
            List<ServerInfo> rlist = ConfigUtil.getRedisNodes();
            for (Iterator<ServerInfo> it = rlist.iterator(); it.hasNext(); ) {
                if (nodeName.equals(it.next().getName())) {
                    it.remove();
                    LOGGER.debug(" ~~~~~~ remove node "+nodeName);
                }
            }
        } else if (nodeName.startsWith("p")) {

            List<ServerInfo> plist = ConfigUtil.getPresistNodes();
            for (Iterator<ServerInfo> it = plist.iterator(); it.hasNext(); ) {
                if (nodeName.equals(it.next().getName())) {
                    it.remove();
                    LOGGER.debug(" ~~~~~~ remove node "+nodeName);
                }
            }
        }
    }


    /**
     * 初始化redis可用节点列表和持久化存储节点列表
     *
     * @param path
     * @throws org.apache.zookeeper.KeeperException
     * @throws InterruptedException
     */
    public static void initList(String path) throws InterruptedException, KeeperException {

        List<String> nodeList = null;
        try {
            nodeList = ZKClient.get().getChildren(path);
        } catch (InterruptedException e) {
            Map<String,String> para = new HashMap<String,String>();
            para.put("ZKClient.get().getChildren",path);
            LOGGER.error(e,para,"");
            throw e;
        } catch (KeeperException e) {
            Map<String,String> para = new HashMap<String,String>();
            para.put("ZKClient.get().getChildren",path);
            LOGGER.error(e, para, "");
            throw e;
        }


        if (nodeList == null || nodeList.size() == 0) {
           return;
        }

        for (String nodeName : nodeList) {
            if (nodeName.startsWith("r") || nodeName.startsWith("p") ) {
                String cont = null;

                try {
                    cont = ZKClient.get().getStringData(path + "/" + nodeName);
                } catch (KeeperException e) {
                    Map<String,String> para = new HashMap<String,String>();
                    para.put("ZKClient.get().getStringData",path + "/" + nodeName);
                    LOGGER.error(e, para, "");
                    throw e;
                } catch (InterruptedException e) {
                    Map<String,String> para = new HashMap<String,String>();
                    para.put("ZKClient.get().getStringData",path + "/" + nodeName);
                    LOGGER.error(e, para, "");
                    throw e;
                }

                if ("true".equals(cont)) {
                    ServerInfo s = transTo(nodeName);
                    addNoteToList(nodeName,s);
                }else{
                    ServerInfo s = transTo(nodeName);
                    if(s!=null){
                        removeNodeFromList(nodeName);
                    }
                }
            }
        }

    }


}


