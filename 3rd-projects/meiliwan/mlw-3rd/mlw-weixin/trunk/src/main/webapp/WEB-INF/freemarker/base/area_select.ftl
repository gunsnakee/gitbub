<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <#include "/WEB-INF/freemarker/inc/site_pub_js.ftl">
</head>
<body class="uCenter w990">
<div>
<!-- 默认值 hidden-- >




<!-- -->
<!--topbar-->
<div >
   <input id="default_country" type="hidden" value="1000">
   国家：<select id="sel_country" name="sel_country">
        <option value="0" selected="true">请选国家</option>
        <option value="1000">中国</option>
    </select> &nbsp;

    <input id="default_province" type="hidden" value="100045">
    省：<select id="sel_province" name="">
        <option value="0">请选省</option>
    </select> &nbsp;

    市:<input id="default_city" type="hidden" value="10004501">
    <select id="sel_city" name="sel_city">
        <option value="0">请选市</option>
    </select> &nbsp;

    <input id="default_county" type="hidden" value="1000450107">
    区/县：<select id="sel_county" name="sel_county">
        <option value="0">请选区/县</option>
    </select> &nbsp;

    <input id="default_town" type="hidden" value="1000450107001">
    街道/乡镇：<select id="sel_town" name="sel_town">
        <option value="0">请选街道/乡镇</option>
    </select>
</div>

</div>
<script>
    $(document).ready(function(){
        // 设置默认选中个 级地域
//        default_select();

        $("#sel_country").change(function(){
            var parentCode = $("#sel_country").val();
            if(parentCode!="0"){
                $.post("/area/getAreaByParentCode",{parentCode:parentCode},function(data){
                    $("#sel_province").html("<option value='0'>请选择省</option>"+data);
                });
            }
        });
        $("#sel_province").change(function(){
            var parentCode = $("#sel_province").val();
            if(parentCode!="0"){
                $.post("/area/getAreaByParentCode",{parentCode:parentCode},function(data){
                    $("#sel_city").html("<option value='0'>请选择市</option>"+data);
                });
            }
        });

        $("#sel_city").change(function(){
            var parentCode = $("#sel_city").val();
            if(parentCode!="0"){
                $.post("/area/getAreaByParentCode",{parentCode:parentCode},function(data){
                    $("#sel_county").html("<option value='0'>请选择区/县</option>"+data);
                });
            }
        });

        $("#sel_county").change(function(){
            var parentCode = $("#sel_county").val();
            if(parentCode!="0"){
                $.post("/area/getAreaByParentCode",{parentCode:parentCode},function(data){
                    $("#sel_town").html("<option value='0'>请选择街道/乡镇</option>"+data);
                });
            }
        });
    });
    /**
     * 默认选中各级地域
     */
    function default_select(){
        //默认选中国家
        var defaultCountry = $("#default_country").val();
        $("#sel_country").val(defaultCountry);

        //加载省级下拉菜单 && 默认选中省
        var defaultProvince = $("#default_province").val();
        $.post("/area/getAreaByParentCode",{parentCode:defaultCountry},function(data){
            $("#sel_province").html('<option value="-1">请选择省</option>'+data);
            $("#sel_province").val(defaultProvince);
        });

        //加载市级下拉菜单 && 默认选中市
        var defaultCity = $("#default_city").val();
        $.post("/area/getAreaByParentCode",{parentCode:defaultProvince},function(data){
            $("#sel_city").html('<option value="-1">请选择市</option>'+data);
            $("#sel_city").val(defaultCity);
        });

        //加载区/县级下拉菜单 && 默认选中区/县
        var defaultCounty = $("#default_county").val();
        $.post("/area/getAreaByParentCode",{parentCode:defaultCity},function(data){
            $("#sel_county").html('<option value="-1">请选择区/县</option>'+data);
            $("#sel_county").val(defaultCounty);
        });

        //加载街道/乡镇级下拉菜单 && 默认选中街道/乡镇
        var defaultTown = $("#default_town").val();
        $.post("/area/getAreaByParentCode",{parentCode:defaultCounty},function(data){
            $("#sel_town").html('<option value="-1">请选择街道/乡镇</option>'+data);
            $("#sel_town").val(defaultTown);
        });

        /*modify*/
//        var AreaSelect=function(){
//            var defaults = {
//                url:'/area/getAreaByParentCode',//数据请求路径
//                sItem:null,//下拉框元素(jquery对象)
//                tip:['请选择省','请选择市','请选择区/县','请选择街道/乡镇'],//select框内的默认提示
//                defaultCode:[$("#default_country"),$("#default_province"),$("#default_city"),$("#default_county"),$("#default_town")]//存放默认地址code的input元素，同tip对应
//            }
//            this.init=function(op){
//                $.extend(defaults,op||{});
//                return this;
//            }
//            this.autoSelect=function(callback){
//                var Sobj=this;
//                defaults.sItem.each(function(index){
//                    var $self=$(this);
//                    $self.on('change',function(){
//                        $self.val()!='0' && (index+1) !=defaults.sItem.length ?($.post(defaults.url,{parentCode:$self.val()},function(data){
//                            defaults.sItem.eq(index+1).html("<option value='0'>"+defaults.tip[index+1]+"</option>"+data);
//                        }),Sobj.resetSelect(index+2),callback.call(this)):callback.call(this);
//                    });
//                });
//                return this;
//            }
//            this.defaultSelect=function(){
//                defaults.sItem.each(function(index){
//                    var $self=$(this);
//                    var _code=defaults.defaultCode[index].val();
//                    _code!='undefined'?$.post(defaults.url,{parentCode:_code},function(data){
//                        $self.html("<option value='-1'>"+defaults.tip[index]+"</option>"+data);
//                    }):false;
//                });
//                return this;
//            }
//            this.resetSelect=function(s){
//                for(var i= s,_len=defaults.sItem.length;i<_len;i++){
//                    defaults.sItem.eq(i).empty().html("<option value='0'>"+defaults.tip[i]+"</option>");
//                }
//            }
//        }
//        /*用例*/
//        var testS=new AreaSelect();
//        testS.init({sItem:$('div.select-grid select')}).defaultSelect().autoSelect(function(){
//            //this 指向当前的select对象
//        });
    }
</script>
</body>
</html>