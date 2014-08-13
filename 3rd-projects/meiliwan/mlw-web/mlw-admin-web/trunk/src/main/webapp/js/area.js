(function ($) {

    function MLW_area(options) {

        $.fn.extend(this, {
            parentCode: 1000,
            level: 4,
            url: "/base/transRef/getCODAreaByPid",
            provinceCode: 100045,
            cityCode: 10004501,
            countyCode: 1000450107
        }, options);
        this.init();
    }

    MLW_area.prototype = {
        init: function () {
            var s1 = '<select id="sel_province" name="adProvinceCode"></select>';
            var s2 = '<select id="sel_city" name="adCityCode"></select>';
            var s3 = '<select id="sel_county" name="adCountyCode"></select>';
            this.input.append(s1);
            this.input.append(s2);
            this.input.append(s3);
            this.province = $("#sel_province");
            this.city = $("#sel_city");
            this.county = $("#sel_county");

            this.initProvince();
            this.initCity();
            this.initCounty();
            this.changeProvince();
            var $this = this;
            this.city.change(function () {
                $this.changeCity();
            });
        },
        initProvince: function () {
            var $this = this;
            $.ajax({
                type: "POST",
                url: $this.url,
                data: { parentCode: $this.parentCode}
            }).done(function (msg) {
                    var len = msg.length;

                    var html = [];
                    //html[0]='<option value="0">请选省</option>';
                    for (var i = 0; i < len; i++) {
                        if (msg[i].areaCode == $this.provinceCode) {
                            html[i] = '<option value="' + msg[i].areaCode + '" selected>' + msg[i].areaName + '</option>';
                        } else {
                            html[i] = '<option value="' + msg[i].areaCode + '">' + msg[i].areaName + '</option>';
                        }
                    }
                    $this.province.html(html.join("")).css('zoom', '1');

                });


        },
        initCity: function () {
            var $this = this;
            $.ajax({
                type: "POST",
                url: $this.url,
                data: { parentCode: $this.provinceCode}
            }).done(function (msg) {
                    $.each(msg, function (i, n) {
                        var y = document.createElement("option");
                        y.value = n.areaCode;
                        y.text = n.areaName;
                        $this.city.append(y);
                    });
                    $this.city.val($this.cityCode);
                });
        },
        initCounty: function () {
            var $this = this;
            $.ajax({
                type: "POST",
                url: $this.url,
                data: { parentCode: $this.cityCode}
            }).done(function (msg) {
                    var len = msg.length;
                    var html = [];
                    //html[0]='<option value="0">请选区/县</option>';
                    for (var i = 0; i < len; i++) {
                        html[i] = '<option value="' + msg[i].areaCode + '">' + msg[i].areaName + '</option>';
                    }
                    $this.county.html(html.join("")).css('zoom', '1');
                    $this.county.val($this.countyCode);
                });
        },
        changeProvince: function () {
            var $this = this;
            this.province.change(function () {
                var parentCode = $this.province.val();
                if (parentCode) {
                    $.post($this.url, {parentCode: parentCode}, function (msg) {
                        $this.city[0].innerHTML = "";
                        $.each(msg, function (i, n) {
                            var y = document.createElement("option");
                            y.value = n.areaCode;
                            y.text = n.areaName;
                            $this.city.append(y);
                        });
                        if (msg.length) {
                            $this.changeCity(msg[0].areaCode);
                        }
                    });
                }
            });
        },
        //parent父亲ID
        changeCity: function (parent) {
            var $this = this;

            var parentCode = $this.city.val();

            parentCode = !!parent ? parent : parentCode;

            if (parentCode) {
                $.post($this.url, {parentCode: parentCode}, function (msg) {
                    $this.county[0].innerHTML = "";
                    $.each(msg, function (i, n) {
                        var y = document.createElement("option");
                        y.value = n.areaCode;
                        y.text = n.areaName;
                        $this.county.append(y);
                    });
                });
            }
        }
    }

    $.fn.extend({

        //插件名字
        mlwAreaPicker: function (options) {
            var $this = $(this);
            options.input = $this;
            var area = new MLW_area(options);

            return this;
        }
    });

})(jQuery); 