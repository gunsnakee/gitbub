/*!
* mlwbox.js
* v1.0 2013-01-24
*弹出窗JS
*/
(function($) {
	//给页面装载CSS样式
	var wa="";
	        window.location.href.match(/^https\:/i)?wa='.wrap_close a{display:inline-block;width:30px;height:30px;overflow:hidden;text-align:center;color:#34538b;text-decoration:none;cursor:pointer;position:absolute;top:5px;right:5px;background:url(/images/bg_index2.png) 0 -965px no-repeat;z-index:99;}':wa='.wrap_close a{display:inline-block;width:30px;height:30px;overflow:hidden;text-align:center;color:#34538b;text-decoration:none;cursor:pointer;position:absolute;top:5px;right:5px;background:url(http://www.meiliwan.com/images/bg_index2.png) 0 -965px no-repeat;z-index:99;}'; 

	var LG = 'linear-gradient(top, #fafafa, #eee)', CSS = '<style type="text/css">' +
		'#mlwBlank{position:absolute;z-index:3000;left:0;top:0;width:100%;height:0;background:black;}' +
		'.wrap_out{padding:4px;position:absolute;z-index:4000;left:-9999px;border-radius:3px;}' +
		'.wrap_in{background:#fff;border:1px solid #d6d6d6;border-radius:2px;padding: 10px;overflow:hidden;}' +
		'.wrap_bar{height:30px;line-height:30px;}' +
		'.wrap_title{line-height:30px;padding-left:10px;margin:0;font-weight:normal;font-size:1em;}' +
		wa+
        '.wrap_close a:hover{text-decoration:none;background-position:0 -921px}' +
        '.wrap_body{background:#fff;clear:both;min-width:20em;}' +
		'.wrap_remind{margin:0 15px 20px 15px;text-align:center}' +
		'.wrap_remind .btnP{margin:20px 0 0;}' +
        '.wrap_remind .btn{ *text-indent: 0;}'+
        '.dialogBoxCancelBtn { margin-left:20px; }'+
        '.dialogBoxSubmitBtn span, .dialogBoxCancelBtn span{ padding: 0 8px 0 13px; }'+
	'</style>';
	$("head").append(CSS);

	var WRAP = '<div id="mlwBlank" onselectstart="return false;"></div>' +
		'<div class="wrap_out" id="wrapOut">' +
        '<div style="position: absolute;z-index: -1;top: 0px;width:100%;height:100%;opacity: 0.3;background: #000;left: 0;filter:alpha(opacity=30);border-radius:2px;_display:none;"></div>'+
			'<div class="wrap_in" id="wrapIn">' +
				'<div id="wrapBar" class="wrap_bar fl">' +
					'<h4 id="wrapTitle" class="wrap_title fl"></h4>' +
					'<div class="wrap_close"><a href="#" id="wrapClose" title="关闭"></a></div>' +
				'</div>' +
				'<div class="wrap_body" id="wrapBody"></div>' +
			'</div>' +
		'</div>';

	$.fn.mlwbox = function(options) {
		options = options || {};
		var s = $.extend({}, mlwboxDefault, options);
		return this.each(function() {
			var node = this.nodeName.toLowerCase();
			if (node === "a" && s.ajaxTagA) {
				$(this).click(function() {
					var href = $.trim($(this).attr("href"));
					if (href && href.indexOf("javascript:") != 0) {
						if (href.indexOf('#') === 0) {
							$.mlwbox($(href), options);
						} else {
							//加载图片
							$.mlwbox.loading();
							var myImg = new Image(), element;
							myImg.onload = function() {
                                var w=myImg.width,h=myImg.height,_imgW,_imgH;
                                var _maxWidth=800,_maxHeight=500;
								var _ratio=w/h;
                                if(_ratio>=1){//如果宽度大于高度
                                    _imgW=(w<=_maxWidth)?w:_maxWidth;
                                    _imgH=_imgW/_ratio;

                                }else{//如果宽度小于高度
                                    _imgH=(h<=_maxHeight)?h:_maxHeight;
                                    _imgW=_imgH*_ratio;
                                }
                                var element = $('<img src="'+ href +'" width="'+ _imgW +'" height="'+ _imgH +'">');
                                options.protect = false;
                                $.mlwbox(element, options);
							};
							myImg.onerror = function() {
								//显示加载图片失败
								$.mlwbox.ajax(href, {}, options);
							};
							myImg.src = href;
						}
					}
					return false;
				});
			} else {
				$.mlwbox($(this), options);
			}
		});
	};

	$.mlwbox = function(elements, options) {
		if (!elements) {
			return;
		}

		var s = $.extend({}, mlwboxDefault, options || {});

		//弹框的显示
		var eleOut = $("#wrapOut"), eleBlank = $("#mlwBlank");

		if (eleOut.size()) {
			eleOut.show();
			eleBlank[s.bg? "show": "hide"]();
		} else {
			$("body").append(WRAP);
		}


		if (typeof(elements) === "object") {
			elements.show();
		} else {
			elements = $(elements);
		}
		//一些元素对象
		$.o = {
			s: s,
			ele: elements,
			bg: eleBlank.size()? eleBlank: $("#mlwBlank"),
			out: eleOut.size()? eleOut: $("#wrapOut"),
			tit: $("#wrapTitle"),
			bar: $("#wrapBar"),
			clo: $("#wrapClose"),
			bd: $("#wrapBody")
		};

		// 标题以及关闭内容
		$.o.tit.html(s.title);
		$.o.clo.html(s.shut);

		//装载元素
		$.o.bd.empty().append(elements);

		if ($.isFunction(s.onshow)) {
			s.onshow();
		}
		//尺寸
		$.mlwbox.setSize();
		//定位
		$.mlwbox.setPosition();

		if (s.fix) {
			$.mlwbox.setFixed();
		}
		if (s.drag) {
			$.mlwbox.drag();
		} else {
			$(window).resize(function() {
				$.mlwbox.setPosition();
			});
		}
		if (!s.bar) {
			$.mlwbox.barHide();
		} else {
			$.mlwbox.barShow();
		}
		if (!s.bg) {
			$.mlwbox.bgHide();
		} else {
			$.mlwbox.bgShow();
		}
		if (!s.btnclose) {
			$.mlwbox.closeBtnHide();
		} else {
			$.o.clo.click(function() {
				$.mlwbox.hide();
				return false;
			});
		}
		if (s.bgclose) {
			$.mlwbox.bgClickable();
		}
		if (s.delay > 0) {
			setTimeout($.mlwbox.hide, s.delay);
		}
	};
	$.extend($.mlwbox, {
		setSize: function() {
			if (!$.o.bd.size() || !$.o.ele.size() || !$.o.bd.size()) {
				return;
			}
			//主体内容的尺寸
			$.o.out.css({
				"width": $.o.s.width,
				"height:": $.o.s.height
			});

			return $.o.out;
		},
		setPosition: function(flag) {
			flag = flag || false;
			if (!$.o.bg.size() || !$.o.ele.size() || !$.o.out.size()) {
				return;
			}
			var w = $(window).width(), h = $(window).height(), st = $(window).scrollTop(), ph = $("body").height();
			if (ph < h) {
				ph = h;
			}
			$.o.bg.width(w).height(ph).css({
				"opacity": $.o.s.opacity,
				"filter": "alpha(opacity = "+($.o.s.opacity*100)+")"
			});
			//主体内容的位置
			//获取当前主体元素的尺寸
			var xh = $.o.out.outerHeight(), xw = $.o.out.outerWidth();
			var t = st + (h - xh)/2, l = (w - xw)/2;

			if ($.o.s.fix && window.XMLHttpRequest) {
				t = (h - xh)/2;
			}
			if (flag === true) {
				$.o.out.animate({
					top: t,
					left: l
				});
			} else {
				$.o.out.css({
					top: t,
					left: l,
					zIndex: $.o.s.index
				});
			}
			return $.o.out;
		},
		//定位
		setFixed: function() {
			if (!$.o.out || !$.o.out.size()) {
				return;
			}
			if (window.XMLHttpRequest) {
				$.mlwbox.setPosition().css({
					position: "fixed"
				});
			} else {
				$(window).scroll(function() {
					$.mlwbox.setPosition();
				});
			}
			return $.o.out;
		},
		//背景可点击
		bgClickable: function() {
			if ($.o.bg && $.o.bg.size()) {
				$.o.bg.click(function() {
					$.mlwbox.hide();
				});
			}
		},
		//背景隐藏
		bgHide: function() {
			if ($.o.bg && $.o.bg.size()) {
				$.o.bg.hide();
			}
		},
		//背景层显示
		bgShow: function() {
			if ($.o.bg && $.o.bg.size()) {
				$.o.bg.show();
			} else {
				$('<div id="mlwBlank"></div>').prependTo("body");
			}
		},
		//标题栏隐藏
		barHide: function() {
			if ($.o.bar && $.o.bar.size()) {
				$.o.bar.hide();
			}
		},
		//标题栏显示
		barShow: function() {
			if ($.o.bar && $.o.bar.size()) {
				$.o.bar.show();
			}
		},
		//关闭按钮隐藏
		closeBtnHide: function() {
			if ($.o.clo && $.o.clo.size()) {
				$.o.clo.hide();
			}
		},
		//弹框隐藏
		hide: function() {
			if ($.o.ele && $.o.out.size() && $.o.out.css("display") !== "none") {
				$.o.out.fadeOut(10,function() {
					if ($.o.s.protect && (!$.o.ele.hasClass("wrap_remind") || $.o.ele.attr("id"))) {
						$.o.ele.hide().appendTo($("body"));
					}
					$(this).remove();
					if ($.isFunction($.o.s.onclose)) {
						$.o.s.onclose();
					}
				});
				if ($.o.bg.size()) {
//                    $.o.bg.fadeOut(10,function() {
//						$(this).remove();
//					});
                    $.o.bg.remove();
				}
			}
			return false;
		},
		//拖拽
		drag: function() {
			if (!$.o.out.size() || !$.o.bar.size()) {
				$(document).unbind("mouseover").unbind("mouseup");
				return;
			}
			var bar = $.o.bar, out = $.o.out;
			var drag = false;
			var currentX = 0, currentY = 0, posX = out.css("left"), posY = out.css("top");
			bar.mousedown(function(e) {
				drag = true;
				currentX = e.pageX;
				currentY = e.pageY;
			}).css("cursor", "move");
			$(document).mousemove(function(e) {
				if (drag) {
					var nowX = e.pageX, nowY = e.pageY;
					var disX = nowX - currentX, disY = nowY - currentY;
					out.css("left", parseInt(posX) + disX).css("top", parseInt(posY) + disY);
				}
			});
			$(document).mouseup(function() {
				drag = false;
				posX = out.css("left");
				posY = out.css("top");
			});
		},
		//预载
		loading: function() {
			var element = $('<div class="wrap_remind">加载中...</div>');
			$.mlwbox(element, { bar: false });
		},
		//ask询问方法
		ask: function(message,sureCall,cancelCall,options,sureBtn,cancelBtn) {
            var sBtn=arguments[4]==null ? '<a id="J-mlwAskSureBtn" class="dialogBoxSubmitBtn btn btn-sure"></a>':'<a id="J-mlwAskSureBtn" class="anC7 dialogBoxSubmitBtn" href="#"><span>'+sureBtn+'</span></a>';
            var cBtn=arguments[5]==null ? '<a id="J-mlwAskCancelBtn" class="dialogBoxCancelBtn btn btn-cancel"></a>':'<a id="J-mlwAskCancelBtn" class="anC7 dialogBoxCancelBtn" href="#"><span>'+cancelBtn+'</span></a>';
			var element = $('<div class="wrap_remind"><div style="font-size:14px;">'+message+'</div><p class="btnP">'+sBtn+cBtn+'</p></div>');
			$.mlwbox(element, options);
			//回调方法
			$("#J-mlwAskSureBtn").click(function(e) {
                e.preventDefault();
				if ($.isFunction(sureCall)) {
					sureCall.call(this);
				}
                $.mlwbox.hide();
            });
			$("#J-mlwAskCancelBtn").click(function(e) {
                e.preventDefault();
				if (cancelCall && $.isFunction(cancelCall)) {
					cancelCall.call(this);
				}
				$.mlwbox.hide();
			});
		},
		//remind提醒方法
		remind: function(message, callback, options,btnText) {
            var _text=arguments[3]==null?'确认':btnText;
            var element = $('<div class="wrap_remind">' +
                '<div style="font-size: 14px;">'+message+'</div>' +
                '<p class="btnP"><a id="mlwSubmitBtn" class="anC7 dialogBoxSubmitBtn"><span>'+_text+'</span></a></p>' +
                '</div>');
			$.mlwbox(element, options);
			$("#mlwSubmitBtn").click(function(e) {
                e.preventDefault();
				//回调方法
				if (callback && $.isFunction(callback)) {
					callback.call(this);
				}
				$.mlwbox.hide();
			});

		},
		//uri Ajax方法
		ajax: function(uri, params, options,callback) {
			if (uri) {
				$.mlwbox.loading();
				options = options || {};
				options.protect = false;
				$.ajax({
					url: uri,
					data: params || {},
					success: function(html, other) {
						$.mlwbox(html, options);
					},
					error: function() {
						$.mlwbox.remind("加载出了点问题。");
					}
				});
			}
		},
        //uri Ajax方法  jsonp
        ajaxP: function(uri, params, options) {
            if (uri) {
                $.mlwbox.loading();
                options = options || {};
                options.protect = false;
                $.ajax({
                    url: uri,
                    dataType:'jsonp',
                    jsonP: 'callback',
                    data: params || {},
                    success: function(html, other) {
                        $.mlwbox(html, options);
                    },
                    error: function() {
                        $.mlwbox.remind("加载出了点问题。");
                    }
                });
            }
        }
    });
	var mlwboxDefault = {
		title: "",
		shut: "",
		index: 3013,
		opacity: 0.4,

		width: "auto",
		height: "auto",

		bar: true, //是否显示标题栏
		bg: true, //是否显示半透明背景
		btnclose:true, //是否显示关闭按钮

		fix: true, //是否弹出框固定在页面上
		bgclose: false, //是否点击半透明背景隐藏弹出框
		drag: false, //是否可拖拽

		ajaxTagA: true, //是否a标签默认Ajax操作

		protect: "auto", //保护装载的内容

		onshow: $.noop, //弹窗显示后触发事件
		onclose: $.noop, //弹窗关闭后触发事件

		delay: 0 //弹窗打开后关闭的时间, 0和负值不触发
	};
})(jQuery);

