//菜单效果
/*联动select框代码，a{id:[需要绑定的下拉框ID，如4个select框，则需传入前3个select框ID进行联动]，url：联动的URL，callBack：select点击事件，默认f(）函数}*/
var mlwCAreas = function(a) {
    var options =$.extend( {id:["J-country","J-province","J-city"],url:"/api/area/list?apid=",callBack:""}, a || {});
    if( !options.callBack && !$.isFunction(options.callBack )){
        var f= function(){
            for(var i =0;i<options.id.length;i++){
                $("#" + options.id[i]).change(function() {
                    var nextObj = $(this).next();
                    $(this).nextAll("select").empty().append("<option value='-22' selected>请选择</option>");
                    k(nextObj,options.url+$(this).val());
                });
            }
        } ;
        options.callBack = f();
    }
    this.init = function() {
        k($("#" + options.id[0]),options.url+"0");
        options.callBack  ;
    }
    var k = function(obj,a) {
        $.ajax({
            url: a,
            success: function(msg) {
                if (msg) {
                    for(var i=0 ;i<msg.length;i++){
                        obj.append("<option value='" + msg[i].id + "'>" + msg[i].areaName + "</option>")
                    }
                }
            }
        })
    }
    init();
}

var alterCAreas   = function(data,objs){
    var url =   "/api/area/user-select?country_id="+data.countryId+"&province_id="+data.provinceId+"&city_id="+data.cityId+"&zone_id="+data.zoneId;
    if(data.countryId && "-22" != data.countryId){
        $.ajax({
            url: url,
            success: function(msg) {
                if (msg) {
                    $("#"+objs[0]).empty().append("<option value='-22' selected>请选择</option>");
                    $("#"+objs[0]).nextAll("select").empty().append("<option value='-22' selected>请选择</option>");
                    if(msg.provinceList)
                    {
                        for(var j=0;j<msg.provinceList.length;j++){
                            $("#"+objs[1]).append("<option value='" + msg.provinceList[j].id + "'>" + msg.provinceList[j].areaName + "</option>");
                            if(msg.provinceId == msg.provinceList[j].id){
                                document.getElementById(objs[1]).getElementsByTagName("option")[j+1].selected = true;
                            }
                        }
                    }
                    if(msg.cityList)
                    {
                        for(var j=0;j<msg.cityList.length;j++){
                            $("#"+objs[2]).append("<option value='" + msg.cityList[j].id + "'>" + msg.cityList[j].areaName + "</option>");
                            if(msg.cityId == msg.cityList[j].id){
                                document.getElementById(objs[2]).getElementsByTagName("option")[j+1].selected = true;
                            }
                        }
                    }

                    if(msg.zoneList)
                    {
                        for(var j=0;j<msg.zoneList.length;j++){
                            $("#"+objs[3]).append("<option value='" + msg.zoneList[j].id + "'>" + msg.zoneList[j].areaName + "</option>");
                            if(msg.zoneId == msg.zoneList[j].id){
                                document.getElementById(objs[3]).getElementsByTagName("option")[j+1].selected = true;
                            }
                        }
                    }

                    if(msg.countryList)
                    {
                        for(var j=0;j<msg.countryList.length;j++){
                            $("#"+objs[0]).append("<option value='" + msg.countryList[j].id + "'>" + msg.countryList[j].areaName + "</option>");
                            if(msg.countryId == msg.countryList[j].id){
                                document.getElementById(objs[0]).getElementsByTagName("option")[j+1].selected = true;
                            }
                        }
                    }

                }
            }
        })
    }
}


/*productId：产品ID，opt：操作类型（addCollect为单商品收藏，addCollects为选择多商品收藏）*/
var addCollect = function(productId,opt){
    if("addCollect" == opt){
        $.post('/api/collect/add',{product:productId},function(data){
            if(data == '1'){
                //$.mlwbox.remind("收藏成功！",null,{title:"提示"});
                $.mlwbox('<div class="wrap_remind">收藏成功！<p><a class="fontblue" href="/buyer/collect">去收藏夹看看>> </a></p></div>',{title:'提示',onclose:function(){window.location.href=window.location.href;}});
            }else if(data == '2'){
                // $.mlwbox.remind("您收藏夹已经收藏了！",null,{title:"提示"});
                $.mlwbox('<div class="wrap_remind">您收藏夹已经收藏了！<p><a class="fontblue" href="/buyer/collect">去收藏夹看看>> </a></p></div>',{title:'提示',onclose:function(){window.location.href=window.location.href;}});
            }else if(data == '0') {
                //$.mlwbox.remind("收藏失败！",null,{title:"提示"});
                $.mlwbox('<div class="wrap_remind">收藏失败！<p><a class="fontblue" href="/buyer/collect">去收藏夹看看>> </a></p></div>',{title:'提示',onclose:function(){window.location.href=window.location.href;}});
            }else if(data == 'seller'){
                //$.mlwbox.remind("对不起，您是卖家，无法操作！",null,{title:"提示"});
                $.mlwbox('<div class="wrap_remind">对不起，您是卖家，无法操作！<p><a class="fontblue" href="/buyer/collect">去收藏夹看看>> </a></p></div>',{title:'提示',onclose:function(){window.location.href=window.location.href;}});
            }else if (data == 'onehundred') {
                $.mlwbox('<div class="wrap_remind">您的收藏夹已满了！<p><a class="fontblue" href="/buyer/collect">去收藏夹删除一些? </a></p></div>',{title:'提示',onclose:function(){window.location.href=window.location.href;}});
            }else{
                MLW.setCookie("pId",productId);
                MLW.setCookie("opt",opt);
                $.mlwbox.ajax("/passport/box-login",{},{title:"登陆"});
            }
        },'html');
    }else if("addCollects" == opt){
        $.ajax({
            url: "/api/collect/adds",
            data: productId,
            success: function (data) {
                if (data.msg=='success') {
                    // $.mlwbox.remind("收藏成功！",null,{title:"购物车"});
                    $.mlwbox('<div class="wrap_remind">收藏成功！<p><a class="fontblue" href="/buyer/collect">去收藏夹看看>> </a></p></div>',{title:'提示',onclose:function(){window.location.href=window.location.href;}});
                } else if(data.msg=="nologin"){
                    MLW.setCookie("pId",productId);
                    MLW.setCookie("opt",opt);
                    $.mlwbox.ajax("/passport/box-login","",{title:"登陆"});
                } else{
                    $.mlwbox('<div class="wrap_remind">收藏失败！<p><a class="fontblue" href="/buyer/collect">去收藏夹看看>> </a></p></div>',{title:'提示',onclose:function(){window.location.href=window.location.href;}});
                }
            }
        });
    }
}
