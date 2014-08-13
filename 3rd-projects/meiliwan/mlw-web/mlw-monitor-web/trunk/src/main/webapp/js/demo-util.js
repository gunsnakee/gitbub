Demo.namespace("Demo.Util");
Demo.apply(Demo.Util,{
	isSafari : function() {
		var ua = navigator.userAgent.toLowerCase();
		return (/webkit|khtml/).test(ua);
	},

	isGecko : function() {
		var ua = navigator.userAgent.toLowerCase();
		return !isSafari() && ua.indexOf("gecko") > -1;
	},

	Template : function(html){
		if(html instanceof Array){
	        html = html.join("");
	    }else if(arguments.length > 1){
	        html = Array.prototype.join.call(arguments, "");
	    }
	    this.html = html;
	},

	ellipsis : function(value, len){
        if(value && value.length > len){
            return value.substr(0, len-3)+"...";
        }
        return value;
    },

    substr : function(value, start, length){
        return String(value).substr(start, length);
    },

    call : function(value, fn){
        if(arguments.length > 2){
            var args = Array.prototype.slice.call(arguments, 2);
            args.unshift(value);
            return eval(fn).apply(window, args);
        }else{
            return eval(fn).call(window, value);
        }
    },
    autoExtend : function(frameId, width,height) {
    	$("#" + frameId).attr("width", width);
		$("#" + frameId).attr("height", height);
	}
});

Demo.Util.Template.prototype = {
	    applyTemplate : function(values){
	        if(this.compiled){
	            return this.compiled(values);
	        }
	        var useF = this.disableFormats !== true;
	        var fm = AntiSpam.Util, tpl = this;
	        var fn = function(m, name, format, args){
	            if(format && useF){
	                if(format.substr(0, 5) == "this."){
	                    return tpl.call(format.substr(5), values[name], values);
	                }else{
	                    if(args){
	                        var re = /^\s*['"](.*)["']\s*$/;
	                        args = args.split(',');
	                        for(var i = 0, len = args.length; i < len; i++){
	                            args[i] = args[i].replace(re, "$1");
	                        }
	                        args = [values[name]].concat(args);
	                    }else{
	                        args = [values[name]];
	                    }
	                    return fm[format].apply(fm, args);
	                }
	            }else{
	                return values[name] !== undefined ? values[name] : "";
	            }
	        };
	        return this.html.replace(this.re, fn);
	    },

	    set : function(html, compile){
	        this.html = html;
	        this.compiled = null;
	        if(compile){
	            this.compile();
	        }
	        return this;
	    },

	    disableFormats : false,

	    re : /\{([\w-]+)(?:\:([\w\.]*)(?:\((.*?)?\))?)?\}/g,

	    compile : function(){
	        var fm = AntiSpam.Util;
	        var useF = this.disableFormats !== true;
	        var sep = AntiSpam.Util.isGecko ? "+" : ",";
	        var fn = function(m, name, format, args){
	            if(format && useF){
	                args = args ? ',' + args : "";
	                if(format.substr(0, 5) != "this."){
	                    format = "fm." + format + '(';
	                }else{
	                    format = 'this.call("'+ format.substr(5) + '", ';
	                    args = ", values";
	                }
	            }else{
	                args= ''; format = "(values['" + name + "'] == undefined ? '' : ";
	            }
	            return "'"+ sep + format + "values['" + name + "']" + args + ")"+sep+"'";
	        };
	        var body;
	        if(AntiSpam.Util.isGecko){
	            body = "this.compiled = function(values){ return '" +
	                   this.html.replace(/\\/g, '\\\\').replace(/(\r\n|\n)/g, '\\n').replace(/'/g, "\\'").replace(this.re, fn) +
	                    "';};";
	        }else{
	            body = ["this.compiled = function(values){ return ['"];
	            body.push(this.html.replace(/\\/g, '\\\\').replace(/(\r\n|\n)/g, '\\n').replace(/'/g, "\\'").replace(this.re, fn));
	            body.push("'].join('');};");
	            body = body.join('');
	        }
	        eval(body);
	        return this;
	    },

	    call : function(fnName, value, allValues){
	        return this[fnName](value, allValues);
	    }
};
