package com.meiliwan.emall.commons.bean;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-9-25
 * Time: 下午4:42
 */
public enum FragmentName {
    topbar("1","01_01"),
    inc_footer("1","01_03"),
    nav_list("1","01_02"),
    header("1","01_04"),
    nav_bar("1","01_05"),
    top_banner("1", "01_09"),
    nav_list_990("1","01_06"),
    topbar_990("1","01_10"),
    map_990("1","01_13"),
    map_index("1","01_12"),
    inc_footer_990("1","01_11"),
    inc_footer_https_990("1","01_14"),
    uCenter_menu("2","01_01"),
    thailand_prod("3","01_01"),
    vietnam_prod("3","01_02"),
    malaysia_prod("3","01_03"),
    indonesia_prod("3","01_04"),
    singapore_prod("3","01_05"),
    philippin_prod("3","01_06"),
    brunei_prod("3","01_07"),
    laos_prod("3","01_08"),
    cambodia_prod("3","01_09"),
    burma_prod("3","01_10"),
    gx_prod("3","01_11"),
    thailand_zx("3","02_01"),
    vietnam_zx("3","02_02"),
    malaysia_zx("3","02_03"),
    indonesia_zx("3","02_04"),
    singapore_zx("3","02_05"),
    philippin_zx("3","02_06"),
    brunei_zx("3","02_07"),
    laos_zx("3","02_08"),
    cambodia_zx("3","02_09"),
    burma_zx("3","02_10"),
    gx_zx("3","02_11"),
    shop_list("4","00_01"),
    country_map("4","00_02"),
    sns_weibo("4","00_03"),
    snack_header("4","01_01"),
    snack_left("4","01_02"),
    snack_pro("4","01_03"),
    jadeite_header("4","02_01"),
    jadeite_left("4","02_02"),
    jadeite_pro("4","02_03"),
    crafts_header("4","03_01"),
    crafts_left("4","03_02"),
    crafts_pro("4","03_03"),
    aroma_header("4","04_01"),
    aroma_left("4","04_02"),
    aroma_pro("4","04_03"),
    seafood_header("4","05_01"),
    seafood_left("4","05_02"),
    seafood_pro("4","05_03"),
    grains_header("4","06_01"),
    grains_left("4","06_02"),
    grains_pro("4","06_03"),
    seasoner_header("4","07_01"),
    seasoner_left("4","07_02"),
    seasoner_pro("4","07_03"),
    coffee_header("4","08_01"),
    coffee_left("4","08_02"),
    coffee_pro("4","08_03"),
    durian_header("4","09_01"),
    durian_left("4","09_02"),
    durian_pro("4","09_03"),
    rubber_header("4","10_01"),
    rubber_left("4","10_02"),
    rubber_pro("4","10_03"),
    help_left("5", "00_01"),
    help_about_us("5","01_01"),
    help_contact_us("5","01_02"),
    help_shopping("5","01_03"),
    help_order("5","01_04"),
    help_member("5","01_05"),
    help_question("5","01_06"),
    help_shipping("5","01_07"),
    help_time("5","01_08"),
    help_pickup("5","01_09"),
    help_onlinepay("5","01_10"),
    help_bank("5","01_11"),
    help_postal("5","01_12"),
    help_paylater("5","01_13"),
    help_return("5","01_14"),
    help_clause("5","01_15"),
    help_money("5","01_16"),
    help_links("5","01_17"),
    help_recruitment("5","01_18"),
    index("6","01_01"),
    android_index("7","01_01")
    ;

    private String firstCode;
    private String secondCode;
    private int id;

    private FragmentName(String firstCode,String secondCode){
        this.firstCode  = firstCode;
        this.secondCode = secondCode;
    }

    public String getFirstCode() {
        return firstCode;
    }

    public void setFirstCode(String firstCode) {
        this.firstCode = firstCode;
    }

    public String getSecondCode() {
        return secondCode;
    }

    public void setSecondCode(String secondCode) {
        this.secondCode = secondCode;
    }

    public int getId(){
        String ids = this.firstCode+this.getSecondCode().replace("_","");
        return Integer.valueOf(ids);
    }
}
