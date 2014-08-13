<div class="cart-info" >
    <div class="btn-wrap">
        <i class="ico-car fl"></i><span class="s-quantity fl">购物车有<b class="fc-red">0</b>件</span><i class="ico-arrow fl"></i>
    </div>
    <div class="cart-list">
        <p class="empty">购物车中还没有商品，赶紧选购吧！</p>
    </div>
</div>
<script>
    $(function(){
            imlw.cart.init();
            $(".btn-wrap").click(function(){
             window.location.href="http://www.meiliwan.com/cart/view";
            });
    });
</script>