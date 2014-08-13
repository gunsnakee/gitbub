/**
 * Created with JetBrains PhpStorm.
 * User: Administrator
 * Date: 13-6-21
 * Time: 下午3:53
 */
(function(a) {
    //显示、隐藏菜单
    //event:事件参数（click或者mouseover）;delay:延时时间;obj:隐藏的对象;changeObj:需要更改css或者文字的对象;openTit:显示时的文字;closeTit:隐藏时的文字;
    a.fn.Jdropdown = function(d, e) {
        if (!this.length) {
            return
        }
        if (typeof d == "function") {
            e = d;
            d = {}
        }
        var c = a.extend({event: "mouseover",current: "hover",delay: 0,backgroundUrlOver: "white"}, d || {});
        var objevent = c.event;
        var b = (objevent == "mouseover") ? "mouseout" : "mouseleave";
        a.each(this, function() {
            var h = null, g = null, f = false;
            if ("mouseover" == objevent) {
                a(this).on(objevent, function() {
                    if (f) {
                        clearTimeout(g);
                    } else {
                        if (c.obj) {
                            var j = $(c.obj);
                        } else {
                            var j = a(this);
                        }

                        h = setTimeout(function() {
                            j.addClass(c.current);
                            f = true;
                            if (e) {
                                e(j)
                            }
                        }, c.delay)
                    }
                }).on(b, function() {
                        if (f) {
                            if (c.obj) {
                                var j = $(c.obj);
                            } else {
                                var j = a(this);
                            }
                            g = setTimeout(function() {
                                j.removeClass(c.current);
                                f = false;
                            }, c.delay)
                        } else {
                            clearTimeout(h)
                        }
                    })
            } else if ("click" == objevent) {
                a(this).on(objevent, function() {
                    var clickObj = a(this);
                    var changeObj = c.changeObj;
                    var obj = c.obj;
                    $(obj).slideToggle(c.delay, function() {
                        if (changeObj) {
                            if ($(obj).is(":visible")) {
                                $(changeObj).attr("class", c.closeCss);
                                $(changeObj).html(c.closeTit);
                            } else {
                                $(changeObj).attr("class", c.openCss);
                                $(changeObj).html(c.openTit);
                            }

                        }
                    });
                })
            }
        })
    }
    /*浮框(event：mouseover为浮框不跟随鼠标移动，mousemove为跟随鼠标移动，默认不跟随)
     msg为提示框内容*/
    a.fn.boxTip = function(option){
        var options = a.extend({event: "mouseover",msg:"提示信息"}, option || {});
        var objevent = options.event;
        var b = (objevent == "mouseover") || (objevent == "mousemove") ? "mouseout" : "mouseleave";
        a.each(this,function(){
            a(this).on(objevent,function(e){
                var src = options.msg;
                var eleOut = $("#mjstip");
                if(eleOut.length  >0){
                    eleOut.html(src);
                }else{
                    $("body").append('<div id="mjstip" class="boxtip" style="position:absolute;left:0;top:0;display: none;border: 1px solid #878787;padding: 10px; background-color: #fff; box-shadow: 0 0 5px #555;">'+src+'</div>');
                }
                var mouse = a.mousePos(e);
                var cssObj = {
                    'left' : mouse.x + 10 + 'px',
                    'top' : mouse.y  + 'px',
                    'display' : 'block'
                }
                $("#mjstip").css(cssObj);
            }).on(b,function(e){
                    $("#mjstip").css("display","none");
                })
        })
    }
    a.mousePos = function(e){
        var x,y;
        var e = e||window.event;
        return{x:e.clientX+document.body.scrollLeft+document.documentElement.scrollLeft,y:e.clientY+document.body.scrollTop+document.documentElement.scrollTop};
    }
})(jQuery);