<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/pms/promotion/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}">
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}">
    <input type="hidden" name="orderField" value="${pc.pageInfo.orderField}" />
    <input type="hidden" name="orderDirection" value="${pc.pageInfo.orderDirection}" />
</form>
<div class="pageContent">
        <div class="panelBar">
            <ul class="toolBar">
                <li><a title="刷新站内所有推荐位商品，将全部更新为系统推荐商品，慎用！" val="1" rel="ids" class="edit updateAll" ><span>更新所有类目商品</span></a></li>
            </ul>
        </div>
        <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/pms/promotion/list" method="post" id="queryForm">
            <input type="hidden" id="firstCategory" value="${firstLevel}">
            <div class="searchBar">
                <table class="searchContent pageFormContent">
                    <input type="hidden" value=" ${firstLevel}" id="hiddenFirstLevel">
                    <tr>
                        <td>
                            <label>一级类目</label>
                            <select name="firstLevelId" id="firstLevelPromotion">
                                <option value="-1">全部</option>
                                ${firstLevel}
                            </select>
                        </td>
                        <td>
                            <label>二级类目</label>
                            <select name="secondLevelId" id="secondLevelPromotion">
                                <option value="-1">全部</option>
                                ${secondLevel}
                            </select>
                        </td>
                        <td>
                            <label>三级类目</label>
                            <select name="thirdLevelId" id="thirdLevelPromotion">
                                <option value="-1">全部</option>
                                ${thirdLevel}
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td> </td><td> </td><td> </td><td> </td>
                        <td>
                            <div class="subBar">
                                <div class="buttonActive"><div class="buttonContent"><button type="button" id="selectB">筛选</button></div> </div>
                                <div class="buttonActive"><div class="buttonContent" id="revertSelect"><button type="button">重置</button></div></div>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </form>
        <form action="/pms/promotion/update" id="promotionForm" method="post" class="pageForm" onsubmit="return validateCallback(this, navTabAjaxDone)">
        <input type="hidden" name="thirdId" id="thirdId" value=""/>
        <input type="hidden" name="handle" id="handle" value=""/>
        <div class="panelBar">
            <ul class="toolBar">
                <li><a title="更新选中的三级类目下的所有推荐位商品" val="0" class="edit updateThird"><span>更新该三级类目商品</span></a></li>
            </ul>
        </div>
        <table class="table" width="100%"  layoutH="170">
            <thead>
            <tr align="center">
                <th width="2%"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
                <th width="10%" orderField="pro_id" class="<c:if test='${pc.pageInfo.orderField=="pro_id"}' >${pc.pageInfo.orderDirection}</c:if>">商品ID</th>
                <th width="10%" orderField="third_category_id" class="<c:if test='${pc.pageInfo.orderField=="third_category_id"}' >${pc.pageInfo.orderDirection}</c:if>">三级类目</th>
                <th width="37%">商品标题</th>
                <th width="10%">美丽价</th>
                <th width="8%">库存</th>
                <th width="8%">使用状态</th>
                <th width="5%" orderField="sequence" class="<c:if test='${pc.pageInfo.orderField=="sequence"}' >${pc.pageInfo.orderDirection}</c:if>">序号</th>
                <th width="10%">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:if test="${empty pc.entityList}">
                <tr>
                    <td style="text-align: center;"><font color="#dc153c">暂无数据</font></td>
                </tr>
            </c:if>
            <c:if test="${!empty pc.entityList}">
                <c:forEach var="entity" items="${pc.entityList}">
                    <tr align="center">
                        <td><input name="ids" value="${entity.id}" type="checkbox"></td>
                        <td title="${entity.proId}">${entity.proId}</td>
                        <td>${entity.category.categoryName}</td>
                        <td title="${entity.product.proName}" style=" overflow:hidden;word-break: break-all; word-wrap: break-word;">${entity.product.proName}</td>
                        <td>${entity.product.mlwPrice}</td>
                        <td>${entity.product.stock}</td>
                        <td>
                            <c:if test="${entity.product.stock<=0||entity.product.state!=1}"><span style="color: red" title="商品库存不足或者商品不在上架状态，则自动停用">已停用</span></c:if>
                            <c:if test="${entity.product.stock>0&&entity.product.state==1}"><span style="color: #0000ff" title="商品库存不足或者商品不在上架状态，则自动停用">使用中</span></c:if>
                        </td>
                        <td>${entity.sequence}</td>
                        <td>
                            <a target="dialog" title="重新上传商品？" href="/pms/promotion/updateOne?id=${entity.id}"><span style="color: #0000ff">重新上传</span> </a>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
            </tbody>
        </table>
    </form>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $(".updateThird").click(function () {
            var thirdId = $("#thirdLevelPromotion").val();
            if(thirdId>0){
                alertMsg.confirm('确实要执行?', {
                    okCall: function () {
                        $("#thirdId").val(thirdId);
                        $("#handle").val($(".updateThird").attr("val"));
                        $("#promotionForm").submit();
                    }
                });
            }else{
                alertMsg.confirm('请选择到三级类目！', {});
            }
        });

        $(".updateAll").click(function(){
            alertMsg.confirm('确实要更新全站推荐位商品?', {
                okCall: function () {
                    $("#handle").val($(".updateAll").attr("val"));
                    $("#promotionForm").submit();
                }
            });
        });

        $("#firstLevelPromotion").change(function(){
            var level = $("#firstLevelPromotion").val();
            $("#thirdLevelPromotion").html("<option value='-1'>全部</option>");
            $.post("/common/pms/child-category",{level:level},function(data){
                $("#secondLevelPromotion").html("<option value='-1'>全部</option>"+data);
            });
        });

        $("#secondLevelPromotion").change(function(){
            var level = $("#secondLevelPromotion").val();
            $.post("/common/pms/child-category",{level:level},function(data){
                $("#thirdLevelPromotion").html("<option value='-1'>全部</option>"+data);
            });
        });


        $("#revertSelect").click(function(){
            var str=$("#firstCategory").val().replace(/selected/g,"");
            $("#firstLevelPromotion").html("<option value='-1'>全部</option>"+str);
            $("#secondLevelPromotion").html("<option value='-1'>全部</option>");
            $("#thirdLevelPromotion").html("<option value='-1'>全部</option>");
        });

        $("#selectB").click(function(){
            var thirdId = $("#thirdLevelPromotion").val();
            if(thirdId>0){
                $("#queryForm").submit();
            }else{
                alertMsg.confirm('请选择到三级类目！', {});
            }
        });
    });
</script>