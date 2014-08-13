<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">修改折扣</h2>

<div class="pageContent">
    <form action="/sp/activity/updateActPro?handle=1&action=1" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)" novalidate="novalidate">
    <div class="pageFormContent" layoutH="120">
        <input name="actProId"  type="hidden" value="${entity.id}">
        <input name="actId"  type="hidden" value="${entity.actId}">
        <fieldset>
            <legend>修改折扣</legend>
            <dl>
                <dt>美丽价：</dt>
                <dd>￥<span id="mlwPrice">${entity.mlwPrice}</span></dd>
            </dl>
            <dl>
                <dt>成本价：</dt>
                <dd>￥${costPrice}</dd>
            </dl>
            <dl>
                <dt>折扣价：</dt>
                <dd><input type="text" name="discountPrice" value="" class="required number" id="discountPrice" <c:if test="${entity.mlwPrice >= 0.01}">min="0.01" max="${entity.mlwPrice}"</c:if><c:if test="${entity.mlwPrice < 0.01}">min="0.001" max="${entity.mlwPrice}"</c:if>/>元</dd>
            </dl>
            <dl>
                <dt>已降：</dt>
                <input type="hidden" name="lostPrice" value="" id="lostPriceVal"/>
                <dd><span id="lostPrice"></span>元</dd>
            </dl>
            <dl>
                <dt>折扣：</dt>
                <input type="hidden" name="discount" value="" id="hideD"/>
                <dd><span id="discount"></span>折</dd>
            </dl>
        </fieldset>
    </fieldset>
    </div>
    <div class="formBar">
        <ul>
            <li><div class="buttonActive"><div class="buttonContent"><button type="submit">提交</button></div></div></li>
            <li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
        </ul>
    </div>
    </form>
</div>
<script>
    $(document).ready(function(){
        $("#discountPrice").keyup(function(){
            var mlw = $("#mlwPrice").html();
            var discount = $(this).val();
            var sub = FloatSub(mlw, discount);
            $("#lostPrice").html(sub);
            $("#lostPriceVal").val(sub);
            var div = FloatDiv(discount, mlw);
            $("#discount").html(div);
            $("#hideD").val(div);
        });

        //浮点数减法运算
        function FloatSub(arg1, arg2) {
            var r1, r2, m, n;
            try {
                r1 = arg1.toString().split(".")[1].length
            } catch(e) {
                r1 = 0
            }
            try {
                r2 = arg2.toString().split(".")[1].length
            } catch(e) {
                r2 = 0
            }
            m = Math.pow(10, Math.max(r1, r2));
            //动态控制精度长度
            n = (r1 >= r2) ? r1: r2;
            return ((arg1 * m - arg2 * m) / m).toFixed(n);
        }
        //浮点数除法运算.计算折扣
        function FloatDiv(arg1, arg2) {
            var t1 = 0, t2 = 0, r1, r2;
            try {
                t1 = arg1.toString().split(".")[1].length
            } catch(e) {}
            try {
                t2 = arg2.toString().split(".")[1].length
            } catch(e) {}
            with(Math) {
                r1 = Number(arg1.toString().replace(".", ""));
                r2 = Number(arg2.toString().replace(".", ""));
                var result = (r1 / r2) * pow(10, t2 - t1)*10;
                return Math.round(result*10)/10;
            }
        }

    });
</script>