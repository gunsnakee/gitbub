<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageHeader" style="text-align: center">
    <form action="/pms/product/add" method="post" id="J-addForm">
        <input type="hidden" name="step" value="2">
        <div class="searchBar">
            <table class="searchContent" style="height: 200px">
                <tr>
                    <td>
                        <label>一级类目</label>
                        <select name="firstLevel" id="add-first" style="width: 120px;">
                            <option value="-1">全部</option>
                            ${firstLevel}
                        </select>
                    </td>
                    <td>
                        <label>二级类目</label>
                        <select name="secondLevel" id="add-second" style="width: 120px;">
                            <option value="-1">全部</option>
                            ${secondLevel}
                        </select>
                    </td>
                    <td>
                        <label>三级类目</label>
                        <select name="thirdLevel" id="add-third" style="width: 120px;">
                            <option value="-1">全部</option>
                            ${thirdLevel}
                        </select>
                    </td>
                </tr>
            </table>
            <fieldset>
                <dt></dt>
                <dd><a title="添加商品" rel="73" target="navTab" href="#" id="J-pro-add"></a><div class="button"><div class="buttonContent"><span style= "cursor: pointer;" id="J-next">下 一 步</span></div></div></dd>
                <address></address>
            </fieldset>
        </div>
    </form>

</div>
<script>
    $(document).ready(function(){
        $("#add-first").change(function(){
            var level = $("#add-first").val();
            $.post("/common/pms/child-category",{level:level},function(data){
                $("#add-second").html('<option value="-1">全部</option>'+data);
            });
        });

        $("#add-second").change(function(){
            var level = $("#add-second").val();
            $.post("/common/pms/child-category",{level:level},function(data){
                $("#add-third").html('<option value="-1">全部</option>'+data);
            });
        });

        $("#J-next").click(function(){
            var first = $("#add-first").val();
            var second = $("#add-second").val();
            var third = $("#add-third").val();
            if(third < 0){
                alert("请选择三级类目");
                return;
            }else{
                var hrefStr = '/pms/product/add?step=2&firstLevel='+first+"&secondLevel="+second+"&thirdLevel="+third;
                $("#J-pro-add").attr('href',hrefStr).trigger('click');
            }
        });
    });
</script>