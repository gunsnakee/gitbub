package com.meiliwan.emall.union.dao;

import com.jolbox.bonecp.BoneCPDataSource;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.sp2.client.ActivityInterfaceClient;
import com.meiliwan.emall.union.bean.CatItem;
import com.meiliwan.emall.union.bean.EtaoSearchCat;
import com.meiliwan.emall.union.dao.dbtool.DBConn;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * etao 提供搜索数据
 * User: wuzixin
 * Date: 14-5-14
 * Time: 下午3:03
 */
public class EtaoSearchDao {

    private static final MLWLogger LOG = MLWLoggerFactory.getLogger(EtaoSearchDao.class);
    private static final String FullIndexPath = "/data/file/etao/feed/";
    private ConfigOnZk configOnZk = ConfigOnZk.getInstance();
    private static final String POSTFEE_PATH = "commons/system.properties";



    //数据源注入
    private BoneCPDataSource pmsDataSource;

    public BoneCPDataSource getPmsDataSource() {
        return pmsDataSource;
    }

    public void setPmsDataSource(BoneCPDataSource pmsDataSource) {
        this.pmsDataSource = pmsDataSource;
    }

    public List<String> getFullIndex() {
        List<String> ids = new ArrayList<String>();
        String sql = "select pro_id from pro_product where state = 1";
        ResultSet rs = null;
        Statement st = null;
        Connection conn = DBConn.getConn(pmsDataSource);
        if (conn != null) {
            st = DBConn.getSt(conn);
        }

        Document document = DocumentHelper.createDocument();
        Element rootElement = document.addElement("root");
        Element versionElement = rootElement.addElement("version");
        versionElement.setText("1.0");
        Element modifiedElement = rootElement.addElement("modified");
        modifiedElement.setText(DateUtil.getCurrentDateTimeStr());
        Element seller_idElement = rootElement.addElement("seller_id");
        seller_idElement.setText("美丽湾官网");
        Element cat_url_idElement = rootElement.addElement("cat_url");
        cat_url_idElement.setText("http://union.meiliwan.com/etao/feed/SellerCats.xml");
        Element dirElement = rootElement.addElement("dir");
        dirElement.setText("http://union.meiliwan.com/etao/feed/item/");
        Element item_idsElement = rootElement.addElement("item_ids");

        if (st != null) {
            try {
                rs = st.executeQuery(sql);
            } catch (SQLException e) {
                LOG.error(e, "连接数据库失败", "");
            }
        }
        try {
            while (null != rs && rs.next()) {
                Element outer_idElement = item_idsElement.addElement("outer_id");
                outer_idElement.addAttribute("action", "upload");
                String proId = rs.getString("pro_id");
                ids.add(proId);
                outer_idElement.setText(proId);
            }
            //生成文件
            XMLWriter ouput = new XMLWriter(new FileOutputStream(new File(FullIndexPath + "FullIndex.xml")));
            ouput.write(document);
            ouput.close();
        } catch (IOException e) {
            LOG.error(e, "数据写入xml文件失败", "");
        } catch (SQLException e) {
            LOG.error(e, "连接数据库失败", "");
        } finally {
            DBConn.closeRs(rs);
            DBConn.closeSt(st);
            DBConn.closeConn(conn);
        }
        return ids;
    }

    public List<String> getIncrementIndex() {
        List<String> ids = new ArrayList<String>();
        String sql = "select pro_id,state from pro_product where update_time<>create_time and date(update_time) >= (date(NOW()) - 1/48)";
        ResultSet rs = null;
        Statement st = null;
        Connection conn = DBConn.getConn(pmsDataSource);
        if (conn != null) {
            st = DBConn.getSt(conn);
        }
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element rootElement = document.addElement("root");
        Element versionElement = rootElement.addElement("version");
        versionElement.setText("1.0");
        Element modifiedElement = rootElement.addElement("modified");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 30);
        modifiedElement.setText(df.format(cal.getTime()));

        Element seller_idElement = rootElement.addElement("seller_id");
        seller_idElement.setText("美丽湾官网");
        Element cat_url_idElement = rootElement.addElement("cat_url");
        cat_url_idElement.setText("http://union.meiliwan.com/etao/feed/SellerCats.xml");
        Element dirElement = rootElement.addElement("dir");
        dirElement.setText("http://union.meiliwan.com/etao/feed/item/");
        Element item_idsElement = rootElement.addElement("item_ids");

        if (st != null) {
            try {
                rs = st.executeQuery(sql);
            } catch (SQLException e) {
                LOG.error(e, "连接数据库失败", "");
            }
        }
        try {
            while (null != rs && rs.next()) {
                Element outer_idElement = item_idsElement.addElement("outer_id");
                String status = rs.getString("state");
                String proId = rs.getString("pro_id");
                ids.add(proId);
                if (null != status && !"1".equals(status)) {
                    outer_idElement.addAttribute("action", "delete");
                    outer_idElement.setText(proId);
                }
                if (null != status && "1".equals(status)) {
                    outer_idElement.addAttribute("action", "upload");
                    outer_idElement.setText(proId);
                }
            }
            //生成文件
            XMLWriter ouput = new XMLWriter(new FileOutputStream(new File(FullIndexPath + "IncrementIndex.xml")));
            ouput.write(document);
            ouput.close();
        } catch (IOException e) {
            LOG.error(e, "数据写入xml文件失败", "");
        } catch (SQLException e) {
            LOG.error(e, "连接数据库失败", "");
        } finally {
            DBConn.closeRs(rs);
            DBConn.closeSt(st);
            DBConn.closeConn(conn);
        }

        return ids;
    }

    public void getSellerCats() {
        String sql = "select p.parent_id,p.parent_name,c.category_id,c.category_name from (select category_id as parent_id,category_name as parent_name from pro_category WHERE parent_id = 0 and state=1) p LEFT JOIN pro_category c on c.parent_id = p.parent_id WHERE c.state = 1";
        ResultSet rs = null;
        Statement st = null;
        Connection conn = DBConn.getConn(pmsDataSource);
        if (conn != null) {
            st = DBConn.getSt(conn);
        }
        Document document = DocumentHelper.createDocument();
        Element rootElement = document.addElement("root");
        Element versionElement = rootElement.addElement("version");
        versionElement.setText("1.0");
        Element modifiedElement = rootElement.addElement("modified");
        modifiedElement.setText(DateUtil.getCurrentDateTimeStr());
        Element seller_idElement = rootElement.addElement("seller_id");
        seller_idElement.setText("美丽湾官网");
        Element seller_catsElement = rootElement.addElement("seller_cats");
        Element catElement = null;
        Element scid1Element = null;
        Element name1Element = null;
        Element catsElement = null;

        if (st != null) {
            try {
                rs = st.executeQuery(sql);
            } catch (SQLException e) {
                LOG.error(e, "连接数据库失败", "");
            }
        }

        try {
            Map<String, EtaoSearchCat> map = getCatS(rs);
            if (map != null && map.size() > 0) {
                Iterator iterator = map.keySet().iterator();
                while (iterator.hasNext()) {
                    String parentId = (String) iterator.next();
                    EtaoSearchCat cat = map.get(parentId);
                    if (cat != null) {
                        catElement = seller_catsElement.addElement("cat");
                        scid1Element = catElement.addElement("scid");
                        scid1Element.setText(cat.getParentId());
                        name1Element = catElement.addElement("name");
                        name1Element.setText(cat.getParentName());
                        catsElement = catElement.addElement("cats");
                        if (cat.getItems() != null && cat.getItems().size() > 0) {
                            for (CatItem item : cat.getItems()) {
                                Element cat1Element = catsElement.addElement("cat");
                                Element scid2Element = cat1Element.addElement("scid");
                                scid2Element.setText(item.getCategoryId());
                                Element name2Element = cat1Element.addElement("name");
                                name2Element.setText(item.getCategoryName());
                            }
                        }
                    }
                }
            }
            //生成文件
            XMLWriter ouput = new XMLWriter(new FileOutputStream(new File(FullIndexPath + "SellerCats.xml")));
            ouput.write(document);
            ouput.close();
        } catch (IOException e) {
            LOG.error(e, "数据写入xml文件失败", "");
        } catch (SQLException e) {
            LOG.error(e, "连接数据库失败", "");
        } finally {
            DBConn.closeRs(rs);
            DBConn.closeSt(st);
            DBConn.closeConn(conn);
        }

    }

    public void getXmlItems(List<String> ids) {
        String proIds = "(" + ids.toString().replace("[", "").replace("]", "") + ")";
        Statement st = null;
        Connection conn = DBConn.getConn(pmsDataSource);
        if (conn != null) {
            st = DBConn.getSt(conn);
        }
        ResultSet rs = getProdDetails(st,proIds);
        ResultSet rs1 = null;
        try {
            while (null != rs && rs.next()) {
                OutputFormat format = OutputFormat.createPrettyPrint();
                format.setEncoding("UTF-8");
                Document document = DocumentHelper.createDocument();
                document.setXMLEncoding("UTF-8");
                Element itemElement = document.addElement("item");
                Element seller_idElement = itemElement.addElement("seller_id");
                seller_idElement.setText("美丽湾官网");
                Element outer_idElement = itemElement.addElement("outer_id");

                String proId = rs.getString("pro_id");
                outer_idElement.setText(proId);

                Element titleElement = itemElement.addElement("title");

                String proName = rs.getString("pro_name") == null ? "" : rs.getString("pro_name");
                String skuName = rs.getString("sku_name") == null ? "" : rs.getString("sku_name");
                if (StringUtils.isNotEmpty(skuName)) {
                    proName = proName + " " + skuName;
                }
                titleElement.setText(proName);

                Element ProIDElement = itemElement.addElement("product_id");
                ProIDElement.setText(proId);

                Element availableElement = itemElement.addElement("available");
                Date presaleDate = rs.getDate("presale_end_time");
                if (presaleDate != null && DateUtil.compare(new Date(), presaleDate) == -1) {
                    availableElement.setText("3");
                } else {
                    int sellStock = rs.getInt("sell_stock");
                    if (sellStock > 0) {
                        availableElement.setText("1");
                    } else {
                        availableElement.setText("0");
                    }
                }

                Element priceElement = itemElement.addElement("price");
                String priceParam = String.valueOf(rs.getString("mlw_price"));
                if (priceParam.indexOf(".") < 0) {
                    priceParam = priceParam + ".00";
                }
                priceElement.setText(priceParam);

                Element post_feeElement = itemElement.addElement("post_fee");
                post_feeElement.setText("0");
                String[] postFee = getPostFee();
                if (postFee != null && postFee.length > 0) {
                    double mlwprice = Double.parseDouble(priceParam);
                    if (mlwprice >= Integer.parseInt(postFee[0])) {
                        post_feeElement.setText("0");
                    } else {
                        post_feeElement.setText(postFee[1]);
                    }
                }

                //增加商品类型，
                Element typeElement = itemElement.addElement("type");
                typeElement.setText("fixed");

                //判断是否参加活动，参加活动，填充活动信息
                Map<Integer, Map> spMap = ActivityInterfaceClient.getProductPriceDownInfo(Integer.parseInt(proId));
                if (spMap != null && spMap.size() > 0) {
                    Map map = spMap.get(Integer.parseInt(proId));
                    if (map != null && map.size() > 0) {
                        typeElement.setText("discount");
                        //增加活动信息
                        Element discountElement = itemElement.addElement("discount");
                        Element startElement = discountElement.addElement("start");
                        Date startTime = (Date) map.get("startTime");
                        startElement.setText(DateUtil.formatDate(startTime, "yyyy-MM-dd-HH:mm"));

                        Element endElement = discountElement.addElement("end");
                        Date endTime = (Date) map.get("endTime");
                        endElement.setText(DateUtil.formatDate(endTime, "yyyy-MM-dd-HH:mm"));

                        Element dpriceElement = discountElement.addElement("dprice");
                        BigDecimal dprice = (BigDecimal) map.get("actPrice");
                        dpriceElement.setText(dprice.toString());

                        if (dprice.doubleValue() >= Integer.parseInt(postFee[0])) {
                            post_feeElement.setText("0");
                        } else {
                            post_feeElement.setText(postFee[1]);
                        }

                        Element drateElement = discountElement.addElement("drate");
                        Double drate = ((Integer) map.get("discount") / 100.00);
                        drateElement.setText(drate.toString());

                        Element ddescElement = discountElement.addElement("dprice");
                        ddescElement.setText(map.get("actWenan") == null ? "" : map.get("actWenan").toString());
                    }
                }

                Element descpElement = itemElement.addElement("desc");
                descpElement.setText(rs.getString("editor_rec") == null ? "" : rs.getString("editor_rec"));

                Element business_name = itemElement.addElement("business_name");
                business_name.setText("美丽湾");

                Element sortElement = itemElement.addElement("brand");
                String brandName = rs.getString("brand_name") == null ? "" : rs.getString("brand_name");
                sortElement.setText(brandName);

                Element tagsElement = itemElement.addElement("tags");
                String tags = brandName;
                if (StringUtils.isNotEmpty(skuName)) {
                    tags = tags + "/" + skuName;
                }
                tagsElement.setText(tags);

                Element imageElement = itemElement.addElement("image");
                imageElement.setText(rs.getString("default_image_uri") == null ? "" : rs.getString("default_image_uri"));

                String[] detailImages = getDetailImages(rs.getString("image_uris") == null ? "" : rs.getString("image_uris"));
                Element more_imagesElement = null;
                if (detailImages != null && detailImages.length > 0) {
                    more_imagesElement = itemElement.addElement("detail_images");
                    for (String uri : detailImages) {
                        Element imgElement = more_imagesElement.addElement("img");
                        imgElement.setText(uri);
                    }
                }

                Element scidsElement = itemElement.addElement("scids");
                scidsElement.setText(rs.getString("cat_id"));

                Element barcodeElement = itemElement.addElement("barcode");
                barcodeElement.setText(rs.getString("bar_code") == null ? "" : rs.getString("bar_code"));

                Element hrefElement = itemElement.addElement("href");
                hrefElement.setText("http://www.meiliwan.com/" + proId + ".html");
                Element is_mobile = itemElement.addElement("is_mobile");
                is_mobile.setText("1");
                Element wireless_link = itemElement.addElement("wireless_link");
                wireless_link.setText("http://m.meiliwan.com/html/productdetail.html?proId=" + proId);
                Element hd_wireless_link = itemElement.addElement("hd_wireless_link");
                hd_wireless_link.setText("http://m.meiliwan.com/html/productdetail.html?proId=" + proId);

                Element sale_volume = itemElement.addElement("sale_volume");
                sale_volume.setText(rs.getString("show_sale_num"));

                Element comment_count = itemElement.addElement("comment_count");
                int ctCount = getComments(rs.getInt("one_score_num"), rs.getInt("two_score_num"), rs.getInt("three_score_num"), rs.getInt("four_score_num"), rs.getInt("five_score_num"));
                comment_count.setText(String.valueOf(ctCount));

                Element saled_score = itemElement.addElement("saled_score");
                double score = getAvgScore(rs.getInt("one_score_num"), rs.getInt("two_score_num"), rs.getInt("three_score_num"), rs.getInt("four_score_num"), rs.getInt("five_score_num"));
                saled_score.setText(String.valueOf(score));

                Element bread_crumb = itemElement.addElement("bread_crumb");
                bread_crumb.setText(getBreadCrumb(rs.getInt("spu_id")));

                rs1 = getDetailComments(conn,st,proId);
                if (rs1 != null) {
                    Element comments = itemElement.addElement("comments");
                    while (rs1.next()) {
                        Element comment = comments.addElement("comment");
                        Element auth = comment.addElement("auth");
                        auth.setText(rs1.getString("nick_name"));

                        Element content = comment.addElement("content");
                        content.setText(rs1.getString("content") == null ? "" : rs1.getString("content"));
                    }
                }


                try {
                    //循环生成文件
                    XMLWriter output = new XMLWriter(new FileOutputStream(new File(FullIndexPath+"item/" + proId + ".xml")), format);
                    output.write(document);
                    output.close();
                } catch (IOException e) {
                    LOG.error(e, "生成商品对应xml失败", "");
                }
            }
        } catch (SQLException e) {
            LOG.error(e, "连接数据库失败", "");
        } finally {
            DBConn.closeRs(rs1);
            DBConn.closeRs(rs);
            DBConn.closeSt(st);
            DBConn.closeConn(conn);
        }
    }

    private ResultSet getProdDetails(Statement st,String ids) {
        String sql = "select p.pro_id,p.spu_id,p.pro_name,sku_name,p.bar_code,p.mlw_price,p.presale_end_time,p.default_image_uri,p.image_uris,s.sell_stock,b.name as brand_name,c.parent_id as cat_id,d.editor_rec,a.show_sale_num,a.one_score_num,a.two_score_num,a.three_score_num,a.four_score_num,a.five_score_num " +
                "from pro_product p " +
                "left join pro_stock s on s.pro_id = p.pro_id " +
                "left join pro_brand b on p.brand_id = b.brand_id " +
                "left join pro_category c on p.third_category_id = c.category_id " +
                "left join pro_detail d on p.spu_id = d.spu_id " +
                "left join pro_action a on p.pro_id = a.pro_id where p.pro_id in " + ids;
        ResultSet rs = null;
        if (st != null) {
            try {
                rs = st.executeQuery(sql);
            } catch (SQLException e) {
                LOG.error(e, "连接数据库失败", "");
            }
        }
        return rs;
    }

    private ResultSet getDetailComments(Connection conn,Statement st,String proId) {
        String sql = "select nick_name,content from pro_comment where score>=4 and state = 0 and is_web_visible = 0 and is_admin_del= 0 and pro_id =" + proId + " order by sequence desc,comment_time desc limit 0,5";
        ResultSet rs = null;
        if (conn != null) {
            st = DBConn.getSt(conn);
        }
        if (st != null) {
            try {
                rs = st.executeQuery(sql);
            } catch (SQLException e) {
                LOG.error(e, "连接数据库失败", "");
            }
        }
        return rs;
    }

    //获取面包屑
    private String getBreadCrumb(int spuId) {
        String sql = "select c1.category_name as firstName,c2.category_name as secondName,c3.category_name as thirdName " +
                "from pro_spu s " +
                "left join pro_category c1 on s.first_category_id = c1.category_id " +
                "left join pro_category c2 on s.second_category_id = c2.category_id " +
                "left join pro_category c3 on s.third_category_id = c3.category_id " +
                "where spu_id = " + spuId;
        ResultSet rs = null;
        String breadCrumb = "";
        Statement st = null;
        Connection conn = DBConn.getConn(pmsDataSource);
        if (conn != null) {
            st = DBConn.getSt(conn);
        }
        if (st != null) {
            try {
                rs = st.executeQuery(sql);
                while (null != rs && rs.next()) {
                    breadCrumb = rs.getString("firstName") + "/" + rs.getString("secondName") + "/" + rs.getString("thirdName");
                }
            } catch (SQLException e) {
                LOG.error(e, "连接数据库失败", "");
            } finally {
                DBConn.closeRs(rs);
                DBConn.closeSt(st);
                DBConn.closeConn(conn);
            }
        }
        return breadCrumb;
    }

    private Map<String, EtaoSearchCat> getCatS(ResultSet rs) throws SQLException {
        Map<String, EtaoSearchCat> map = new HashMap<String, EtaoSearchCat>();
        while (null != rs && rs.next()) {
            EtaoSearchCat cat = map.get(rs.getString("parent_id"));
            if (cat != null) {
                CatItem item = new CatItem();
                item.setCategoryId(rs.getString("category_id"));
                item.setCategoryName(rs.getString("category_name"));
                cat.getItems().add(item);
            } else {
                EtaoSearchCat ct = new EtaoSearchCat();
                String parentId = rs.getString("parent_id");
                ct.setParentId(parentId);
                ct.setParentName(rs.getString("parent_name"));
                List<CatItem> items = new ArrayList<CatItem>();
                CatItem item = new CatItem();
                item.setCategoryId(rs.getString("category_id"));
                item.setCategoryName(rs.getString("category_name"));
                items.add(item);
                ct.setItems(items);
                map.put(parentId, ct);
            }
        }
        return map;
    }

    private String[] getPostFee() {
        //获取邮费配置
        String postFee = null;
        try {
            postFee = configOnZk.getValue("commons/system.properties",
                    "etao.post.fee");
        } catch (BaseException e) {
            LOG.error(e, "获取zk关于邮费配置失败", "");
        }
        if (StringUtils.isNotEmpty(postFee)) {
            return postFee.split("/");
        }
        return null;
    }

    private String[] getDetailImages(String url) {
        if (StringUtils.isNotEmpty(url)) {
            return url.split(",");
        }
        return null;
    }

    private int getComments(int one, int two, int three, int four, int five) {
        return one + two + three + four + five;
    }

    public double getAvgScore(int one, int two, int three, int four, int five) {
        int total = getComments(one, two, three, four, five);
        double avgScore = total <= 0 ? 0 : (one + two * 2 + three * 3 + four * 4 + five * 5) / (total * 1.0);
        return (Math.round(avgScore * 10)) / 10.0;
    }
}
