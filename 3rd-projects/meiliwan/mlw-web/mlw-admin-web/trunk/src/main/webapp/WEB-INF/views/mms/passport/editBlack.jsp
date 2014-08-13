<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>

<h2 class="contentTitle">设置黑名单用户</h2>
<div class="pageContent">

    <form method="post" action="/mms/passport/blackSubmit" class="pageForm required-validate" onsubmit="return validateCallback(this, function(data){
        if(data){
          $.pdialog.closeCurrent();
          navTabSearch($('#queryFormBlacklidst').get(0));
          alertMsg.correct('操作成功！')
        }else{
          alertMsg.error('您提交的数据有误，请检查后重新提交！')
        }

    })">
        <div class="pageFormContent nowrap" layoutH="97">


            <dl>
                <dt>输入用户ID：</dt>
                <dd>
                    <input type="text" name="userName" class="required" alt="请填写ID"/>
                </dd>
            </dl>

        </div>
        <div class="formBar">
            <ul>
                <li><div class="buttonActive"><div class="buttonContent"><button type="submit" target="ajaxTodo">提交</button></div></div></li>
                <li><div class="button"><div class="buttonContent"><button type="button" class="close">关闭</button></div></div></li>
            </ul>
        </div>
    </form>

</div>

<script>

    $(function(){
        $('#div_uids').empty();
        $('input:checked[name="ids"]').each(function(){
            $('<input type="hidden" name="uids" value="'+$(this).val()+'">').appendTo('#div_uids');
        });
    })

</script>

