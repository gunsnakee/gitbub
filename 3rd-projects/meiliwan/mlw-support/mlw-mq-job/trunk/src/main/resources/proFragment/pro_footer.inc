<!--footer-->
<div class="footer" >
    #footerContent
</div>

<div class="c-wrap" id="J-dialogCollect" style="display:none;">
    <!--收藏成功状态-->
    <div class="c-box-wrap" id="scDiv">
        <div class="c-box box-s">
            <b class="ico"></b>
            <p class="tip">商品收藏成功!^_^</p>
            <p class="btnP">
                <a href="http://www.meiliwan.com/uCenter/userCollect/list" class="fc-blue back fl">查看收藏夹>></a>
            </p>
        </div>
    </div>
    <!--已收藏后的状态-->
    <div class="c-box-wrap" style="display: none" id="fcDiv">
        <div class="c-box box-c">
            <p class="tip"><span class="ico-light"></span>您已收藏过该商品</p>
            <p class="btnP">
                <a href="http://www.meiliwan.com/uCenter/userCollect/list" class="fc-blue back fl">查看收藏夹>></a>
            </p>
        </div>
    </div>
    <div class="fn-fgx"></div>
    <div class="grid-recommend">
        <p class="t">为您推荐：</p>
        <div class="twP clearfix" id="popRecDiv">
           <!-- <ul class="twC">
                <li class="pic"><a href="#"><img src="../images/img100x100.jpg" width="100px" height="100px" alt=""></a></li>
                <li class="text title"><a href="#">东南亚家居饰品泰式工艺品摆件木雕</a></li>
                <li class="text"><b class="fn-rmb fc-red">￥80.00</b></li>
            </ul>


            <ul class="twC">
                <li class="pic"><a href="#"><img src="../images/img100x100.jpg" width="100px" height="100px" alt=""></a></li>
                <li class="text title"><a href="#">东南亚家居饰品泰式工艺品摆件木雕</a></li>
                <li class="text"><b class="fn-rmb fc-red">￥80.00</b></li>
            </ul>
            <ul class="twC">
                <li class="pic"><a href="#"><img src="../images/img100x100.jpg" width="100px" height="100px" alt=""></a></li>
                <li class="text title"><a href="#">东南亚家居饰品泰式工艺品摆件木雕</a></li>
                <li class="text"><b class="fn-rmb fc-red">￥80.00</b></li>
            </ul>
            <ul class="twC">
                <li class="pic"><a href="#"><img src="../images/img100x100.jpg" width="100px" height="100px" alt=""></a></li>
                <li class="text title"><a href="#">东南亚家居饰品泰式工艺品摆件木雕</a></li>
                <li class="text"><b class="fn-rmb fc-red">￥80.00</b></li>
            </ul>
            -->
        </div>
    </div>
</div>

<div id="saveBtn" align="center" style="display:none; position: fixed; top: 100px; right: 20px; z-index: 8888; width:30px;height: 190px;font-size:16px; color:red;">
  <div style="background-color: gray;"><a href="javascript:void(0);" onclick="publishPro('on');"><br/>发<br/>布</a></div>
  <div style="background-color: gray; height: 70px;"><a href="javascript:void(0);" onclick="publishPro('off');"><br/>撤<br/>消</a></div>
  <div style="background-color: gray; height: 70px;"><a href="javascript:void(0);" onclick='window.open("about:blank","_self").close();'><br/>关<br/>闭</a></div>
</div>

<script src="http://www.meiliwan.com/js/cart/cart.js"></script>
<link rel="stylesheet" href="http://www.meiliwan.com/js/jpages/css/jPages.css">
<style>
   .hide{display:none;}
</style>
<script src="http://www.meiliwan.com/js/jpages/js/jPages.js"></script>
<script src="http://www.meiliwan.com/js/history_product.js"></script>
<script src="http://www.meiliwan.com/js/collect/collect.js"></script>
<script src="http://www.meiliwan.com/js/format.js"></script>
<script>
    window.onload=function(){
        var NavStick=new MLW.Stick();
        NavStick.Init({
            sObj:$('#Jtab-mainTitle'),
            tObj:$('div.footer')
        });
        
    };
    
    var AnchorMap=function(){
        var defaults={
            wrap:$('#J-anchor'),
            anchorItems:$('#J-anchor a.anchor-item')
        }
        var assembly=function(){
            var items=defaults.anchorItems,_len=defaults.anchorItems.length;
            for(var i=0;i<_len;i++){
                if(i==0){
                    items.eq(i).addClass('anchor-itemFirst');
                }
                if(i==_len-1){
                    items.eq(i).addClass('anchor-itemLast');
                }
            }
        }
        this.init=function(op){
            $.extend(defaults,op||{});
            assembly();
            return this;
        }
        this.autoSelect=function(){
            defaults.wrap.click(function(e){
                var $target= $(e.target);
                defaults.anchorItems.removeClass('anchor-itemOn anchor-itemFirstOn anchor-itemLastOn');
                if($target.hasClass('anchor-itemFirst')){
                    $target.addClass('anchor-itemFirstOn');
                }
                else if($target.hasClass('anchor-itemLast')){
                    $target.addClass('anchor-itemLastOn');
                }
                else{
                    $target.addClass('anchor-itemOn');
                }
            });
            return this;
        }
    }
    
    $(function () {
        var mlwAnchorMap=new AnchorMap();
        mlwAnchorMap.init().autoSelect();
        
        $('#zoom').jqzoom({
            zoomType:'standard',
            zoomWidth:400,
            zoomHeight:400,
            title:false
        });
        
        var tabMain = new MLW.TabSwitch();
        tabMain.Init({
            titleItems: $('#Jtab-main div.Jtab-title:eq(0) ul.tab li'),
            contentItems: $('#Jtab-main div.Jtab-content:eq(0) > div.content-cell')
        });
        var tabAdvisory=new MLW.TabSwitch();
        tabAdvisory.Init({
            titleItems: $('#Jtab-advisory div.Jtab-title li'),
            contentItems: $('#Jtab-advisory div.Jtab-content div.content-cell')
        });
        var tabComment=new MLW.TabSwitch();
        tabComment.Init({
            titleItems: $('#Jtab-comment div.Jtab-title li'),
            contentItems: $('#Jtab-comment div.Jtab-content div.content-cell')
        });
        var tabStoreSelector=new MLW.TabSwitch();
        tabStoreSelector.Init({
            titleItems: $('#J-storeSelector div.Jtab-title li'),
            contentItems: $('#J-storeSelector div.Jtab-content div.content-cell')
        });
        $('#J-storeSelector').Jdropdown({delay:200});

        $('#J-miniCart').Jdropdown({current:'on',delay:200});   //test(需要ajax请求)

        $('#J-miniCart').hover(function(){
          $(this).addClass('on');
          $("#miniProImg").attr("src", $("#previewImg").attr("src"));
          $("#miniProName").text($("#pName").text());
          $("#miniProPrice").html("&yen;" + $("#realPrice").text());
        },function(){
          $(this).removeClass('on');
        });

        //$('a.J-collect').click(function(e){
        //   e.preventDefault();
        //   $('#J-dialogCollect').mlwbox();
       // });
    });
</script>

<script language="javascript">
    function submitIssue(){
                       var data = {};
                       data.content = $("#cltContent").val();
                       if(!data.content){
                         $.mlwbox.remind("咨询内容不能为空！");
                         return ;
                       }else if("字数长度请在5-1000个字符之间" == $.trim(data.content)){
                         $.mlwbox.remind("咨询内容不能为默认字符！");
                         return ;
                       }else if($.trim(data.content).length < 5 || $.trim(data.content).length > 1000){
                         $.mlwbox.remind("字数长度请在5-1000个字符之间");
                         return ;
                       }

                       data.proId = $("#proId").val();
                       data.consultType = $("#cltType input:checked").val();
                       data.proName = $("#pName").text();

                       $.post("http://www.meiliwan.com/consult/add", data, function(result){
            		       if(result.status == "0"){
            		         $.mlwbox.remind("您的咨询信息已提交成功，美丽湾客服将尽快通过站内信的形式通知您，请随时关注个人中心的站内信！",null);
            		       }else if(result.status == "-2"){
            		         $.mlwbox.remind("对不起，只有登录之后才能发表咨询！",null);
            		       }else{
            		         $.mlwbox.remind("对不起，服务器繁忙，请稍后再试！",null);
            		       }
            		   }, "json");

     }

	$.when( 
	    $.getScript('http://www.meiliwan.com/js/user/user.js')
	).then(function(data, textStatus, jqXHR){
	
	    user.bindLoginDialogFunc($('#J-issue'),function(obj){
          //e.preventDefault();
          var box='<div class="i-wrap" id="J-dialogIssue" style="display:block;">' +
                      '<p class="t">发表咨询</p>' +
                      '<div class="con gap-mb10">' +
                          '<div class="c1 gap-mb10">' +
                              '<span class="item-t">问题类型：</span>'+
                              '<div class="item-c" id="cltType">' +
                                  '<label><input type="radio" value="1" checked name="cltType">商品咨询</label>' +
                                  '<label><input type="radio" value="2" name="cltType">库存及配送</label>' +
                                  '<label><input type="radio" value="3" name="cltType">支付问题</label>' +
                                  '<label><input type="radio" value="4" name="cltType">促销及赠品</label>' +
                                  '<label><input type="radio" value="5" name="cltType">其他</label>' +
                              '</div>' +
                          '</div>' +
                          '<div class="c2 gap-mb10 clearfix">' +
                              '<span class="item-t">咨询内容：</span>' +
                              '<textarea id="cltContent" onfocus="this.value=\'\'">字数长度请在5-1000个字符之间</textarea>' +
                          '</div>' +
                          '<div class="c3">' +
                              '<a href="javascript:void(0);" class="anC7" onclick="submitIssue();" id="J-submitIssue"><span>提交</span></a>' +
                          '</div>' +
                      '</div>' +
                  '</div>';
          //$('#cltContent').val('字数长度请在5-1000个字符之间');
          $.mlwbox(box);
          
          $.mlwbox(box,{onclose:function(){
                                  $('#J-dialogIssue').remove();
                                }
                        });
        });

        user.bindLoginDialogFunc($('#cmtBtn'),function(obj){
            $.get("http://www.meiliwan.com/uCenter/proComment/getUserCommentProductStatus?proId=" + $("#proId").val(), function(result){
		        if("unbuy" == result){
		          $.mlwbox.remind('您未购买该商品或交易未成功，暂不能评论',null);
		        }else if("commentOver" == result){
		          $.mlwbox.remind("您已评论过该商品了",null);
		        }else if("uncomment" == result){
		          var pId = $("#proId").val();
		          window.location.href="/uCenter/proComment/list?proId=" + pId;
		        }
		    });
        });
        
        user.bindLoginDialogFunc($('#collectHref'),function(obj){
          var pId = $('#proId').val();
          imlw.collect.addSingleCollectOpt(pId, function(msg){
               if ("success" == msg) {
                  $("#scDiv").css("display", "block");
                  $("#fcDiv").css("display", "none");
                  $('#J-dialogCollect').mlwbox();
               } else if("collect_exist" == msg){
                  $("#scDiv").css("display", "none");
                  $("#fcDiv").css("display", "block");
                  $('#J-dialogCollect').mlwbox();
               } else if ("unlogin" == msg) {
                  $.mlwbox.remind("对不起，请先登录");
               } else if ("withoutproIds"==msg) {

               }

          });
        });

       user.bindLoginDialogFunc('.fulOrLess',function(obj){
                       var url = "http://www.meiliwan.com/uCenter/proComment/usefullOrLess";
                       var comId = $(obj).attr('key');
                       var type = $(obj).attr('type');
                       $.ajax({
                           url:url,
                           data:{comId:comId,type:type},
                           dataType:'jsonp',
                           success:function(data){
                               if(data.suc=='exist'){
                                   $.mlwbox.remind('对不起，您已经点击过！');
                               }else if(data.suc == 'ok'){
                               var t={useful:"+",less:"-"},t1={useful:"usefulCnt",less:"uselessCnt"};
                                 if(window.commentlistmap){
                               // var  index=["allCmtDiv","1CmtDiv","2CmtDiv","3CmtDiv"];
                                   // $.each(index,function(i,n){
                                           $.each(commentlistmap["allCmtDiv"],function(){
                                                if(this.cmtId==comId){
                                                    this[t1[type]]++;
                                                }
                                           });
                                   // });
                                 }
                                $("#"+type+"_" + comId ).html(t[type]+(Math.abs(Number($("#"+type+"_" + comId ).html())+1)));

                                   $.mlwbox.remind('操作成功，谢谢您的支持！');
                               }else{
                                   $.mlwbox.remind('操作失败！');
                               }
                           }
                       });
                   });

   });

   function fillPopRecDiv(){
     var recContent = $("#leftRecDiv").html();
     $("#popRecDiv").empty();
     $("#popRecDiv").html(recContent);

     var uls = $("#popRecDiv ul");
     if(uls.length > 4){
       $("#popRecDiv ul:last").remove();
     }

     $("#popRecDiv img").attr("width", "100px");
     $("#popRecDiv img").attr("height", "100px");
     $("#popRecDiv b").removeClass("fn-rmb fc-orange");
     $("#popRecDiv b").addClass("fn-rmb fc-red");
     $("#popRecDiv span.priceTit").text('');
   }
   fillPopRecDiv();

</script>


<script language="javascript">

   //MLW.initIncResource();
   
   $.get("http://www.meiliwan.com/pub/promise_div.inc" , function(data){
     $("#promiseDiv").empty();
     $("#promiseDiv").html(data);
   });
   
  /* $.get("http://www.meiliwan.com/pub/nav_div.inc" , function(data){
     $("#navDiv").empty();
     $("#navDiv").html(data);
  });

  $.get("http://www.meiliwan.com/pub/top_bar.inc" , function(data){
     $("#J-topbar").empty();
     $("#J-topbar").html(data);
  });
  $.get("http://www.meiliwan.com/pub/search.inc" , function(data){
     $("#searchDiv").empty();
     $("#searchDiv").html(data);
  });
  $.get("http://www.meiliwan.com/pub/cart_div.inc" , function(data){
     $("#cartDiv").empty();
     $("#cartDiv").html(data);
  });
  $.get("http://www.meiliwan.com/pub/footer.inc" , function(data){
     $("#footerDiv").empty();
     $("#footerDiv").html(data);
  });
  $.get("http://www.meiliwan.com/pub/promise_div.inc" , function(data){
     $("#promiseDiv").empty();
     $("#promiseDiv").html(data);
  });*/
</script>

<script language="javascript">
  function opNum(num){
    var byNum = $("#buyNum").val();
    byNum = byNum ? parseInt(byNum) : 0;
    
    byNum = byNum + num;
    
    if(byNum > 99){
      $("#buyNum").val(99);
    }
    
    var pstock = $("#proStock").val();
    var stock = parseInt(pstock ? pstock : "0");
    if(byNum > stock){
      $.mlwbox.remind("对不起，库存不足");
      $("#buyNum").val(byNum < 0 ? 0 : byNum);
    }else{
	  $("#buyNum").val(byNum < 0 ? 0 : byNum);
    }
  }
  
  function checkInputByNum(){
    var byNum = $("#buyNum").val();
    byNum = parseInt(byNum);
    
    if(isNaN(byNum)){
      $.mlwbox.remind("数量只能为纯数字");
      $("#buyNum").val(1);
    }else if(byNum < 1){
      $.mlwbox.remind("数量必须大于等于1，小于等于999");
      $("#buyNum").val(1);
    }else if( byNum > 999){
      $.mlwbox.remind("数量必须大于等于1，小于等于999");
      $("#buyNum").val(99);
    }
  }

  function addProToCart(){
    var pId = $("#proId").val();
    if(pId){
      var byNum = $("#buyNum").val();
      if(byNum && byNum >= 1){
        byNum = parseInt(byNum);
        if(byNum > 999){
          $.mlwbox.remind("最大购买数量不能超过999件");
        }
        var pstock = $("#proStock").val();
        var stock = parseInt(pstock ? pstock : "0");
        if(byNum > stock){
          $.mlwbox.remind("对不起，库存不足");
        }else{
	      imlw.cart.addCartOpt("product", pId, byNum, imlw.cart.addDefaultCallback);
        }
      }else{
        $.mlwbox.remind("请选择购买数量");
      }
    }else{
      $.mlwbox.remind("对不起，页面出错了！");
    }
  }
  
  function addPackToCart(packId){
    if(packId){
      imlw.cart.addCartOpt("pack", packId, 1, imlw.cart.addDefaultCallback);
    }else{
      $.mlwbox.remind("对不起，页面出错了！");
    }
  }
  
  function showAddToCart(msg){
    if ("success" == msg) {
      $.mlwbox.remind("成功加入购物车！");
    } else if ("fail" == msg) {
      $.mlwbox.remind("加入购物车失败");
    } else if ("full" == msg) {
      $.mlwbox.remind("购物车已经满了");
    }else{
      $.mlwbox.remind("对不起，服务器发生了未知的错误，添加失败!");
    }
  }
</script>

<script language="javascript">
  var currCountry = null;
  var currPvc = null;
  var currCity = null;
  var currTown = null;

  var currIP = null;
  $.ajax({
     url:"http://www.meiliwan.com/area/ipaddr",
     async:false,
     dataType: "text",
     success: function(data){
       currIP = data;
     }
  });

  if(currIP){
      currIP = currIP.split(",")[0];
      $.getScript('http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip=' + currIP, function(_result){
         if (remote_ip_info && remote_ip_info.ret == '1'){
                 currCountry =  remote_ip_info.country;
                 currPvc = remote_ip_info.province;
                 currCity = remote_ip_info.city;
                 currTown = remote_ip_info.district;
                 // alert('IP 详细信息：IP：61.135.169.105<BR>国家：'+remote_ip_info.country+'<BR>省份：'+remote_ip_info.province+'<BR>城市：'+remote_ip_info.city+'<BR>区：'+remote_ip_info.district+'<BR>ISP：'+remote_ip_info.isp+'<BR>类型：'+remote_ip_info.type+'<BR>其他：'+remote_ip_info.desc);
         }

         initAreaInfo();
      });
  } else {
      initAreaInfo();
  }



  function initAreaInfo(){
    /*var actType =  $("#actType").val();
    if(actType && actType.indexOf("my") >= 0){
       $("#feeLi").hide();
       return;
    }*/

    $("#proviceDiv").bind("click", proviceClick);

    $("#cityDiv").bind("click", cityClick);
    $("#townDiv").bind("click", townClick);

    getAreaElem("proviceDiv", currPvc).trigger("click");
  }

  function getAreaElem(areaDivId, areaName){
     var areaElem = $("#" + areaDivId + " a:first");
         if(areaName){
            $("#" + areaDivId + " a").each(function(index, elem){
               var tmpPvc = $(elem).text();
               if(tmpPvc && (tmpPvc.indexOf(areaName) >= 0 || areaName.indexOf(tmpPvc) >= 0)){
                  areaElem = $(elem);
               }
            });
         }
     return areaElem;
  }


  function proviceClick(e){
    e.stopPropagation();
    var currElem = $(e.target);
    if(currElem.attr("id") == 'proviceDiv'){
        return;
    }
    var name = currElem.text();
    var areaCode = currElem.attr("areaCode");
    var areaId = currElem.attr("areaId");

    $("#cityDiv").empty();
    $("#cityDiv").html($("#" + areaCode).html());

    var firstCityElem = getAreaElem("cityDiv", currCity);

    $("#townDiv").empty();
    $("#townDiv").html($("#" + firstCityElem.attr("areaCode")).html());

    var firstTownElem = getAreaElem("townDiv", currTown);

    $("#proviceLi").empty();
    $("#cityLi").empty();
    $("#townLi").empty();
  
    $("#proviceLi").text(name);
    $("#cityLi").text(firstCityElem.text());
    $("#townLi").text(firstTownElem.text());
    
    showDetailArea();
    
    loadFee(firstTownElem.attr('areaCode'));
    
    //bindAreaClick();
  }
  
  
  function showDetailArea(){
    $("#areaScrName").empty();
    
    $("#areaScrName").text($("#proviceLi").text() + $("#cityLi").text() + $("#townLi").text());
  }

  
  function loadFee(code){
    $.get("http://www.meiliwan.com/base/transRef/getAreaPayByKey?areaCode=" + code, function(data){
      if(data){
        var actType =  $("#actType").val();
        $("#myInfo").empty();
        if(data.freeSupport == 1 && (!actType || actType.indexOf("my") < 0) && data.price > 0){
          $("#myInfo").html("(<span class='fc-red'>满<em class='fc-red'>" + fmoney(data.fullFreeSupport,2) + "</em> 包邮</span>)");
        } 
        $("#areaFee").empty();
        if(data.price > 0 && (!actType || actType.indexOf("my") < 0)){
          $("#areaFee").html("&yen; " + fmoney(data.price, 2));
        }else{
          $("#myInfo").text("");
          $("#areaFee").html("免运费");
        }
      }else{
        $("#areaFee").html("很抱歉，该商品暂时不能送达");
        $("#myInfo").empty();
      }
    }, "json");
  }
  
  function cityClick(e){
    e.stopPropagation();
    var currElem = $(e.target);
    if(currElem.attr("id") == 'cityDiv'){
                 return;
        }
    var name = currElem.text();
    var areaCode = currElem.attr("areaCode");
    var areaId = currElem.attr("areaId");
    
    $("#townDiv").empty();
    $("#townDiv").html($("#" + areaCode).html());

    var firstTownElem = getAreaElem("townDiv", currTown);

    $("#cityLi").empty();
    $("#townLi").empty();
  
    $("#cityLi").text(name);
    $("#townLi").text(firstTownElem.text());
    
    showDetailArea();
    
    loadFee(firstTownElem.attr('areaCode'));
    
    //bindAreaClick();
  }
  
  function townClick(e){
    e.stopPropagation();
    var currElem = $(e.target);
    if(currElem.attr("id") == 'townDiv'){
                 return;
    }
    var name = currElem.text();
    var areaCode = currElem.attr("areaCode");
    var areaId = currElem.attr("areaId");
    
    $("#townLi").empty();
    $("#townLi").text(name);
    
    showDetailArea();
    
    loadFee(areaCode);
    
    //bindAreaClick();
  }
  
  var currLoc = window.location.href;
  if(currLoc.indexOf("pd_preview") >= 0){
    $("#saveBtn").css("display", "block");
  }
  
  function publishPro(action){
    var pubUrl = "http://admin.meiliwan.com/pms/publish?proId=" + $("#proId").val() + "&action=" + action;
    
    $.ajax({ 
        type: "get",        //使用get方法访问后台 
        dataType: "jsonp",  //返回json格式的数据 
        jsonp: "callback",
        url: pubUrl,   //要访问的后台地址 
        async: true,
        success: function(data){ 
           if(data && data.status == "0"){
             $.mlwbox.remind("该商品终端页保存成功!");
           }else{
             $.mlwbox.remind("操作失败，请稍后重试或与管理员联系! 错误原因：" + (data.msg ? data.msg : "原因未知"));
           }
        } 
    });
    
  }
  
</script>


<script language="javascript">
   //写商品浏览历史记录
  function showHisPro(result){
     $("#historyDiv").empty();
     result = eval(result);
     for(var pro in result){
        var elem = "<ul class='twC'>" + 
			        "<li class='pic'><a href='http://www.meiliwan.com/"+result[pro].proId+".html'><img width='120px' height='120px' src='" + result[pro].defaultImg + "120x120.jpg' alt=''/></a></li>" +
			        "<li class='text title'>" +
			          "<h3><a href='http://www.meiliwan.com/"+result[pro].proId+".html'>" + result[pro].proName + "</a></h3>" +
			        "</li>" +
			        "<li class='text price'>" +
			          "美丽价：<b class='fn-rmb fc-orange'>&yen;"+fmoney(result[pro].mlwPrice,2)+"</b>" +
			        "</li>" +
			       "</ul>";
			       
	    $("#historyDiv").append(elem);
     }
  }

  MLW.history.product.setProduct($("#proId").val());
  
  MLW.history.product.getProducts(showHisPro);
  
</script>

<script>
    var pageConfig = {
        containerID  : "allCltUl",
        perPage      : 10,
        startPage    : 1,
        startRange   : 1,
        midRange     : 5,
        endRange     : 1,
        callback:function(){
           if (arguments[1].showing[0]) {
              //$(arguments[1].showing[0]).parents(".comment-list").css("min-height","0px");
              var top = $(arguments[1].showing[0]).offset().top
              if ((document.documentElement.scrollTop + document.body.scrollTop )> top) {
                 document.documentElement.scrollTop = document.body.scrollTop = ($(arguments[1].showing[0]).offset().top - 100);
              }
           }
        }
    };
    
  $(function(){
    /* initiate the plugin */
   // $("#allCltPage").jPages(pageConfig);
    
    pageContent("all", "CltUl", "CltPage");
  });
  
  function pageContent(currIndex, ul, page){
    pageConfig.containerID = currIndex + ul;
    $("#"+currIndex+page).jPages(pageConfig);

    var content = $("#" + currIndex + page + " > a");
    if(content.length > 2){
       $("div.page > div.inner > a").css("cursor", "pointer");
    }else{
       $("#" + currIndex + page).empty();
    }
    
    $("#" + pageConfig.containerID).show();
  }
  
  $("#Jtab-advisory > .Jtab-title > .tab > li").bind("click", function(){
    var currIndex = $(this).index();
    if(currIndex == 0){
        currIndex = "all";
    }
    setTimeout(function(){
		 pageContent(currIndex, 'CmtDiv', 'CmtPage');
	}, 300);
  });

  var goodCmtContent = {};
  var midCmtContent = {};
  var poorCmtContent = {};

       $.get("http://www.meiliwan.com/product/getComments/" + $("#proId").val(), function(cmtData){
           goodCmtContent = eval(cmtData.goodComments);
           midCmtContent = eval(cmtData.midComments);
           poorCmtContent = eval(cmtData.poorComments);

           $("#gCNum").text(goodCmtContent.length);
           $("#mCNum").text(midCmtContent.length);
           $("#pCNum").text(poorCmtContent.length);

           $("#allCNum").text(goodCmtContent.length + midCmtContent.length + poorCmtContent.length);


  window.commentlistmap = {
                   "allCmtDiv": [].concat(goodCmtContent,midCmtContent,poorCmtContent),
                   "1CmtDiv": goodCmtContent,
                   "2CmtDiv": midCmtContent,
                   "3CmtDiv": poorCmtContent
               }, dom = '<div class="comment-box" id="cmtDiv_${cmtId}" usefulCnt="${usefulCnt}" isTop="${isTop}" createTime="${cmtTime}"><div class="userP">' +
               '<a href="javascript:void(0);" style="cursor:text;" class="pic"><img width="50px" height="50px" src="${avatar}50x50.jpg" alt=""></a><p class="text">' +
               '<a href="javascript:void(0);" style="cursor:text;" class="nickname">${nickName}</a>' +
               '</p></div><div class="cP"><div class="p-1"><div class="stars"><span style="width: ${score}%;"></span>' +
               '</div><span class="time fc-gray">${cmtTime}</span></div>' +
               '<div class="p-2"><p>${cmtContent}</p></div>' +
               '<div class="reply-box ${hide}"><p>美丽湾客服回复：</p><p>${replyContent}</p></div>' +
               '<div class="p-3">' +
               '<a class="e-btn useful fulOrLess" href="javascript:void(0);" key="${cmtId}" type="useful">有用</a>' +
               '<span id="useful_${cmtId}" class="count">+${usefulCnt}</span>' +
               '<a class="e-btn useless fulOrLess" href="javascript:void(0);" key="${cmtId}" type="less">无用</a>' +
               '<span id="less_${cmtId}" class="count">-${uselessCnt}</span></div>' +
               '<div class="ui-arrow"><em>◆</em><span>◆</span></div></div></div>';
  			 $.substitute = function(template, map){
                   return template.replace(/\$\{([^\s\:\}]+)(?:\:([^\s\:\}]+))?\}/g, function(match, key, format){
                       if (map[key]) {
                           return map[key];
                       }else if (map[key]===0){
                                return "0";
                       }else {
                           return "";
                       }
                   });
               },sort=[function(a,b){
  			 	if (a.isTop != b.isTop) {
  					return (a.isTop - b.isTop)*-1;
  				}
  				else {
  					var a1=a.cmtTime.split(" "),b1=b.cmtTime.split(" ");
  					var a2=a1[0].split("-"),a3=a1[1].split(":"),b2=b1[0].split("-"),b3=b1[1].split(":");
  					return (new Date(a2[0],a2[1],a2[2],a3[0],a3[1],a3[2])-new Date(b2[0],b2[1],b2[2],b3[0],b3[1],b3[2]))*-1;
  				}
  			 },function(a,b){
  			 	if(a.isTop!=b.isTop){
  					return a.isTop-b.isTop;
  				}else{
  					return (a.usefulCnt-b.usefulCnt)*-1;
  				}
  			 }],addto=function(that,commentlist,currIndex){
               			 	 commentlist.html("").hide();
                 			var val = $(that).val(),index=["allCmtDiv","1CmtDiv","2CmtDiv","3CmtDiv"],i;
               			currIndex?i=index[currIndex=="all"?0:currIndex]:i=commentlist.attr("id");
                 			$.each(commentlistmap[i].sort(sort[val]),function(i,n){
                 			!this.replyContent?this.hide='hide':this.hide='';
               				var comm=$($.substitute(dom,this)).appendTo(commentlist);

                 			});
               			 };
           $(".sort").change(function(){
  		 	var commentlist = $(".Jtab-content .content_sort.current .comment-list");
  			 addto(this,commentlist);
  			 var currIndex = $("#Jtab-comment .Jtab-title .tab li.hover").index();
               if (currIndex == 0) {
                   currIndex = "all";
               }
              setTimeout(function(){
                  pageContent(currIndex, 'CmtDiv', 'CmtPage');
              }, 300);
           });
  
  $("#Jtab-comment > .Jtab-title > .tab > li").bind("click", function(){
    var currIndex = $(this).index();
    var commentlist = $(".Jtab-content .comment-list").eq(currIndex);
    if(currIndex == 0){
        currIndex = "all";
    }
	
   
    	addto(this,commentlist ,currIndex);
    	 setTimeout(function(){
    	    commentlist.show();
    		pageContent(currIndex, 'CmtDiv', 'CmtPage');
    	}, 300);
    //setTimeout("pageContent(" + currIndex + ", 'CmtDiv', 'CmtPage')", 300);
  });
  
  $("#cmtLi").bind("click", function(){
    $("#cmtAreaNum").text($("#cmtNum").text());
    $("#cmtScore").text($("#proAvgScore").val());
    $("#cmtStar").css("width", $("#proAvgStar").css("width"));
    var commentlist = $(".Jtab-content .content_sort.current .comment-list");
    	addto(this,commentlist);
    	 setTimeout(function(){
    	 	commentlist.show();
    		pageContent('all', 'CmtDiv', 'CmtPage');
    	}, 300);
    //setTimeout("pageContent('all', 'CmtDiv', 'CmtPage')", 300);
  });
   if(window.location.href.indexOf("#cmtArea") > 0){
     $("#cmtLi").trigger("click");
     
     //pageConfig.containerID = "allCmtDiv";
     //$("#allCmtPage").jPages(pageConfig);
     
     pageContent("all", "CmtDiv", "CmtPage");
   }
  }, "json");
</script>

<script>
  $(function(){
   if(window.location.href.indexOf("#cmtArea") > 0){
     $("#cmtLi").trigger("click");

     //pageConfig.containerID = "allCmtDiv";
     //$("#allCmtPage").jPages(pageConfig);

     pageContent("all", "CmtDiv", "CmtPage");
   }

   $("#mainCmtA").bind("click", function(){
     $("#cmtLi").trigger("click");
     pageContent("all", "CmtDiv", "CmtPage");

   });

   //添加商品的基本属性
   $("#propList").prepend("<li>产地："+$("#proPlace").text()+"</li>");
   if($("#brand").val()){
     $("#propList").prepend("<li>品牌："+$("#brand").val()+"</li>");
   }
   $("#propList").prepend("<li>商品编号："+$("#proId").val()+"</li>");
   //$("#propList").prepend("<li>商品名称："+$("#pName").text()+"</li>");

  });

  $("#J-favorableSuit").hide();
  $.get("http://www.meiliwan.com/sp/getProPacks/" + $("#proId").val(), function(data){
     if(data && data.length > 0){
       var tcInfo = "";
       for(pack in data){
          var currTaocan = '<div class="slide-cube">' +
                             '<div class="twP">';

          var packPros = data[pack].packPros;
          var index = 0;
          for(pro in packPros){
              currTaocan = currTaocan +  '<ul class="twC">' +
                     '<li class="pic"><a href="http://www.meiliwan.com/'+packPros[pro].proId+'.html"><img width="120px" height="120px" src="'+packPros[pro].proImgUrl+'120x120.jpg" alt=""></a></li>' +
                     '<li class="text price">原价：<span class="fn-rmb">&yen;' + packPros[pro].proPrice +'</span></li>' +
                     '<li class="text title">' +
                       '<h3><a href="http://www.meiliwan.com/' + packPros[pro].proId + '.html">' + packPros[pro].proName + '</a></h3>' +
                     '</li>' +
                  '</ul>';
              if(++index < packPros.length){
                currTaocan = currTaocan + '<b class="add"></b>';
              }
          }

          currTaocan = currTaocan + '<b class="equal"></b>' +
                '<ul class="twC infos" id="result_2">' +
                  '<li class="text title">' +
                     '<h3>' + data[pack].tcName + '</h3>' +
                  '</li>' +
                  '<li class="text price mlwP">' +
                     '美丽价：<b class="fn-rmb fc-red">¥' + data[pack].mlwPrice + '</b>' +
                  '</li>' +
                  '<li class="text"><del class="fl fn-rmb">原价：¥' + data[pack].originPrice + '</del></li>' +
                  '<li class="text price save">' +
                    '立即节省：<b class="fn-rmb fc-red">¥' + data[pack].savePrice + '</b>' +
                  '</li>' +
                  '<li class="buy"><a href="javascript:void(0);" onclick="addPackToCart('+data[pack].tcId+');" class="anC"><span>购买优惠套餐</span></a></li>' +
                '</ul>' +
              '</div>' +
           '</div>';

           tcInfo = tcInfo + currTaocan;

       }

       $("#taocanArea").empty();
       $("#taocanArea").html(tcInfo);

       $("#J-favorableSuit").show();
       
       $('#J-favorableSuit').slide({prevCell:'.btn-prev',nextCell:'.btn-next',mainCell:'.bar-inner',effect:'left'});
     }
  }, "json");
</script>

<!-- ShareTo Button BEGIN -->
<script type="text/javascript" src="http://s.shareto.com.cn/js/shareto_button.js" charset="utf-8"></script>
<!-- ShareTo Button END -->

</body>
</html>