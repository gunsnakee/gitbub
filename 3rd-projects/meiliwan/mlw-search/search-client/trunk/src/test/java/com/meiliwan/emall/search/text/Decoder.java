package com.meiliwan.emall.search.text;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Decoder {
public static void main(String[] args) throws UnsupportedEncodingException {
	String sssString = "http://10.249.15.164:8983/solr/product_6.10249151648983/select?q\u003dsp_price%3A%5B*+TO+*%5D+AND+state%3A1\u0026defType\u003dedismax\u0026qf\u003dcontents%5E0.8+tags%5E3.0\u0026fq\u003d\u0026sort\u003dsp_price+desc\u0026fl\u003did,default_image_uri,pro_name,short_name,adv_name,stock,sold_volume,comment_volume,store_ids,sp_ids,mlw_prices,sp_prices,avg_score\u0026start\u003d0\u0026rows\u003d32\u0026facet\u003dtrue\u0026facet.mincount\u003d1\u0026facet.limit\u003d100\u0026facet.field\u003dbrand_id\u0026facet.field\u003dplace_id\u0026facet.field\u003dproperty_str_ids\u0026fq\u003d%7B%21tag%3Dpivot%7Dthird_category_id%3A*\u0026facet.field\u003d%7B%21ex%3Dpivot%7Dcategory_tree_str\u0026fq\u003d%7B%21tag%3Dstore%7Dstore_ids%3A%28crafts%29\u0026facet.field\u003d%7B%21ex%3Dstore%7Dstore_ids\u0026group\u003dtrue\u0026group.ngroups\u003dtrue\u0026group.facet\u003dtrue\u0026group.field\u003dspu_id\u0026group.limit\u003d16\u0026group.sort\u003dsp_price+desc,sku_image_i+desc";
	sssString = "http://10.249.15.141:8983/solr/product_6.10249151418983/select?q\u003dstate%3A1+AND+stock%3A%5B1+TO+9999999%5D\u0026sort\u003drandom_696desc\u0026rows\u003d999\u0026fl\u003did";
	System.out.println(URLDecoder.decode(sssString, "utf-8"));
}
}
