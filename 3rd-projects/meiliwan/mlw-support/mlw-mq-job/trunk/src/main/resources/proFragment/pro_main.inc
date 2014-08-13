<input type="hidden" name="proId" id="proId" value="#proId" />
<input type="hidden" name="catId" id="catId" value="#thirdLevelCatId" />
<input type="hidden" name="brandName" id="brandName" value="#brandName" />

<!--breadcrumb-->
<div class="area">
    <div class="breadcrumb">
        <strong><a href="http://www.meiliwan.com/cat/#topLevelCatId.html">#topLevelName</a></strong>
        <span>&nbsp;>&nbsp;<a href="http://www.meiliwan.com/cat/#topLevelCatId-#secLevelCatId.html">#secLevelName</a></span>
        <span>&nbsp;>&nbsp;<a href="http://www.meiliwan.com/product/#thirdLevelCatId-0-0-0-0-0-1-0-0-0-0-.html">#thirdLevelName</a></span>
        <span>&nbsp;>&nbsp;#shortName</span>
    </div>
</div>
<!--area-->
<div class="area">
    <div class="product-intro gap-mt10 clearfix">
        <!--商品信息-->
        <div class="p-info">
            <div class="name">
                <h2 id="pName">#proName</h2>
                <strong><span id="advNm">#advName</span></strong>
            </div>
            <div class="summary">
                <ul class="clearfix">
                    <li>
                        <div class="tit" >产&nbsp;&nbsp;&nbsp;&nbsp;地：</div>
                        <div class="con"><span id="proPlace">#place</span></div>
                    </li>
                    <li>
                        <div class="tit">商品编号：</div>
                        <div class="con">#proId</div>
                    </li>
                    <li class="s-1">
                        <div class="tit" id="delTitle">市&nbsp;场&nbsp;价：</div>
                        <div class="con">
                            <del class="fn-rmb">&yen;<span id="delPrice">&nbsp;</span></del>
                        </div>
                    </li>
                    <li class="s-2">
                        <div class="tit" id="realTitle">美&nbsp;丽&nbsp;价：</div>
                        <div class="con"><b class="fn-rmb">&yen;<span id="realPrice">&nbsp;</span></b> <span class="time" style="display:none;" id="zkTime">剩余<b id="hour">00</b>时<b id="minute">00</b>分<b id="second">00</b>秒</span></div>
                    </li>
                    <li class="s-3">
                        <div class="tit">商品评分：</div>
                        <div class="con">
                            <input type="hidden" id="proAvgScore" value="">
                            <div class="stars"><span id="proAvgStar" style="width: 50%;"></span></div>
                            <span class="fl gap-pl10"><b class="fc-red" id="pas"></b>&nbsp;&nbsp;分</span>
                            <a class="fl gap-pl10" id="mainCmtA" href="#cmtArea">( <span id="cmtNum" class='fc-red'>#commentNum</span> 条商品评论 )</a>
                        </div>
                    </li>
                    <li class="s-4" style="display:none;" id="actArea">
                        <div class="tit">活&nbsp;&nbsp;&nbsp;&nbsp;动：</div>
                        <input type="hidden" id="actType" value="">
                        <div class="con" id="actContent">
                          
                        </div>
                    </li>
                    <li class="s-5 clearfix" id="feeLi">
                        <div class="tit">运&nbsp;&nbsp;&nbsp;&nbsp;费：</div>
                        <div class="con">
                            <p class="fl">
                                <span class="fn-rmb" id="areaFee">很抱歉，该商品暂时不能送达</span> <i id="myInfo"></i> <span class="gap-mr10">配送至</span> </p>
                            <div class="store-selector" id="J-storeSelector">
                                <div class="text" ><span id="areaScrName"></span><b></b></div>
                                <div class="content">
                                    <div class="Jtab">
                                        <div class="Jtab-title">
                                            <ul>
                                                <li class="hover" ><span id="proviceLi">#provice</span><b></b></li>
                                                <li ><span id="cityLi">#city</span><b></b></li>
                                                <li ><span id="townLi">#town</span><b></b></li>
                                            </ul>
                                        </div>
                                        <div class="Jtab-content">
                                            <div id="cityAreaDiv" style="display:none;">
                                                #cities
                                            </div>
                                            <div id="townAreaDiv" style="display:none;">
                                                #towns
                                            </div>
                                               <!-- #provinces   #cities  #towns  -->
                                            <div id="proviceDiv" class="content-cell current provice">
                                                #provinces
                                            </div>
                                            <div id="cityDiv" class="content-cell city">

                                            </div>
                                            <div id="townDiv" class="content-cell town">

                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </li>
                    <li class="s-6" id="toCartDiv">
                        <div class="tit">购买数量：</div>
                        <div class="con">
                            <div class="s-input clearfix">
                                <a href="javascript:void(0);" onclick="opNum(-1);" class="reduce">减少</a>
                                <input type="text" value="1" id="buyNum" class="input-bar" onblur="checkInputByNum();">
                                <a href="javascript:void(0);" onclick="opNum(1);" class="add">增加</a>
                            </div>
                            <div class="btnP clearfix">
                                <input type="hidden" id="proStock" value="0" />
                                <input type="hidden" id="actStock" value="0" />
                                <a href="javascript:void(0);" onclick="addProToCart();" class="J-add">加入购物车</a>
                                <a href="javascript:void(0);" id="collectHref" class="J-collect">收藏</a>
                            </div>
                        </div>
                    </li>
                    <li>
                       <div id="payInfo"></div>
                    </li>
                </ul>
            </div>
        </div>
 <script>
           //是否显示活动信息
           function showAct(){
              var yh = $("#yhInput").val();
              if(yh && yh > 0){
                $("#yhDiv").show();
                $("#actArea").show();
              }
              var jf = $("#jfInput").val();
              if(jf && jf > 0){
                $("#jfDiv").show();
                $("#actArea").show();
              }
           }

           function initTimelyData(data){
              $("#delPrice").text(data.marketPrice);
              $("#realPrice").text(data.mlwPrice);
              $("#proAvgScore").val(data.score);

              var scorePercent =  (parseFloat(data.score)*20) + "%";
              $("#proAvgStar").css("width", scorePercent );
              $("#pas").text(data.score);
              $("#cmtNum").text(data.scoreNum);
              $("#cmtScore").text($("#proAvgScore").val());
              $("#cmtStar").css("width", scorePercent );
              $("#cmtAreaNum").text(data.scoreNum);

              $("#payInfo").text((data.isPay === 0 ? "注意：该商品不支持货到付款" : ""));
           }

           //var mode = "";
           //if(window.location.href.indexOf("pd_preview") > 0){
           //  mode = "preview";
           //}
           $.get("http://www.meiliwan.com/product/getTimelyProInfo?proId=#proId", function(data){
             if(data.status == "0"){
               if(data.advName){
                 $("#advNm").text("'" + data.advName + "'");
               }
               $("#proStock").val(data.stock);
               $("#actStock").val(data.actStock);

               initTimelyData(data);

               if(data.actInfos){
                 $("#actType").val(data.actType);
                 $("#actContent").empty();
                 $("#actContent").html(data.actInfos);
                 $("#actArea").show();
               }
               if(data.zkPrice){
                 $("#realPrice").text(data.zkPrice);
                 $("#delPrice").text(data.mlwPrice);
                 $("#realTitle").text("促销价");
                 $("#delTitle").text("美丽价");
               }
               if(data.endTime){
                 MLW.cDown($("#zkTime"), data.currTime, data.endTime);

                 $("#zkTime").show();
               }
             }else if(data.status == "-2"){
               $("#toCartDiv").empty();
               $("#toCartDiv").html("<p class='empty'>此商品已下架</p>");

               initTimelyData(data);
             }else{
               $("#toCartDiv").empty();
              // $("#toCartDiv").html("<p class='empty'>很抱歉，暂时无法获取该商品的信息</p>");
              $("#toCartDiv").html("<p class='empty'>此商品已下架</p>");
             }
           }, "json");

</script>
        <!--preview-->
        <div class="preview">
            <div class="imgCube">
                <a id="zoom" rel="gall" href="#pro_main_img_uri"><img id="previewImg" src="#pro_main_img_uri350x350.jpg" width="350px" height="350px" alt=""></a>
            </div>
             <div class="Jslide-bar">
                <div class="inn">
                    <ol>