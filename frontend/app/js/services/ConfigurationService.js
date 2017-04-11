bodApp.factory('ConfigurationService', ['$http', 'localStorageService', '$rootScope', function ($http, localStorageService, $rootScope) {
    //var url = "http://localhost:8080/";
    var url = "";

    var config = {};
    config.currentQuarter = "";

    var adminMode = false;
    var printMode = false;
    var bgColor = 'blue';
    var colorPalette = ["#12263e", "#2a4e79", "#505e93","#356dad", "#568ac0", "#68a1cb", "#8ba9d2", "#92b7d8","#b8cee5","b2d8ee"];

    var configLoaded = false;

    var lsScreens = localStorageService.get('screens');
    var lsCurrentQuarter = localStorageService.get('currentQuarter');
    if (lsScreens && lsCurrentQuarter) {
        config.currentQuarter = lsCurrentQuarter;
        bgColor = localStorageService.get('bg');
    }
    if (lsScreens){
        $rootScope.settings = lsScreens;
    }

    var numberToMonth = function(str){
      return ["January","February","March","April","May","June","July","August","September","October","November","December"][parseInt(str) - 1];
    };
    return {
        getPeriod: function(callback){
            if (localStorageService.get('token')){
                $http.defaults.headers.common.Authorization = localStorageService.get('token');
                $http({
                    method: 'GET',
                    url: url + 'api/periods',
                    cache: false
                }).then(function (response) {
                    config.periods = [];
                    for (var i = 0; i < response.data.length; ++i){
                        config.periods.push(response.data[i].value);
                    }
                    callback(config.periods);
                });
            }
        },
        saveCurrentPeriod: function(data){
            $http({
                method: 'PUT',
                data:data,
                url: url + 'configuration',
                cache: false
            }).then(function(){
                location.reload();
            })
        },
        getQuarters: function (callback) {
            if (!configLoaded) {
                $http({
                    method: 'GET',
                    url: url + 'configuration',
                    cache: false
                }).then(function (response) {
                    $rootScope.activePeriodMonth = response.data.currentPeriod.slice(-2);
                    $rootScope.activePeriodYear = response.data.currentPeriod.substring(0, 4);
                    config = response.data;
                    callback(config)
                });
            }
        },
        save: function () {
            $http({
                method: 'PUT',
                url: url + 'configuration',
                cache: false
            }).then(function (response) {
                config.quarters = response.data.quarters.split(',');
                config.currentQuarter = response.data.currentQuarter;
                callback(config.quarters)
            });
        },
        togglePrintMode: function (mode, currentQuarter) {
            if ('print' == mode) {
                angular.element(document.querySelector("body")).addClass('printmode');
                if (currentQuarter && currentQuarter != "") {
                    config.currentQuarter = currentQuarter;
                }
            }
        },

        getCurrentQuarter: function () {
            config.currentQuarter='';
            return  config.currentQuarter;
        },

        //returns color palette equal or larger than minSize (by repeating existing colors)
        getColorPalette:function(minSize) {
            if (!minSize) return colorPalette;
            minSize = parseInt(minSize);
            var repeatCount = Math.ceil(minSize/colorPalette.length);
            var colors = [];
            for (var i=0; i<repeatCount;i++){
                colors = colors.concat(colorPalette);
            }
            return  colors;
        },

        getCurrentMonth: function () {
            return (new Date()).getMonth();
        },

        getCurrentFinYear: function () {
            var y = (new Date()).getFullYear();
            var m = (new Date()).getMonth();
            return m < 3 ? y : y+1;

        },
        revenueBasicBarChartConfiguration: function(cfg, additionalCfg) {
            cfg.bar.values.leaderLine = true;
            cfg.axis.x.font.weight = "600";
            cfg.axis.y.format="M";
            cfg.axis.y.formatDigits = 0;
            cfg.axis.y.font.size = "0.6em";
            cfg.axis.x.font.size = "0.5em";
            cfg.barGroup.padding = 0.7;
            cfg.showLegend = additionalCfg.hasLegend || false;
            cfg.bar.values.font.size="0.75em";
            cfg.bar.values.format = "M";
            cfg.bar.values.formatDigits = 1;
            cfg.bar.colors = ["#196A9D","#60A6D2"];
            cfg.legend = {
                position: "BOTTOM",
                configuration: {
                    direction: "HORIZONTAL",
                    hgap: 0.5,
                    label: {
                        font: {
                            fontSize: "0.6em"
                        }
                    }
                }
            };
            cfg.bar.padding = 0;
        },
        revenueStackedBarChartConfiguration: function(cfg, additionalCfg) {
            cfg.bar.values.leaderLine = true;
            cfg.axis.x.font.weight = "600";
            cfg.axis.y.format="M";
            cfg.axis.y.formatDigits = 0;
            cfg.axis.y.font.size = "0.6em";
            cfg.axis.x.font.size = "0.5em";
            cfg.barGroup.padding = 0.7;
            cfg.bar.values.padding = 10;
            cfg.padding.right = 20;
            cfg.showLegend = additionalCfg.hasLegend || false;
            cfg.barGroup.totalValue.font.size = "0.75em";
            cfg.bar.values.font.size="0.55em";
            cfg.bar.values.format = "M";
            cfg.bar.values.formatDigits = 1;
            cfg.bar.colors = ["#196A9D","#60A6D2"];
            cfg.legend = {
                position: "BOTTOM",
                configuration: {
                    direction: "HORIZONTAL",
                    hgap: 0.5,
                    label: {
                        font: {
                            fontSize: "0.6em"
                        }
                    }
                }
            };
            cfg.bar.padding = 0;
        },
        cmLineChartConfiguration: function(cfg, additionalCfg) {
            cfg.value.vMargin = "0.5em";
            cfg.value.format = "";
            cfg.value.font.fontSize = "0.7em";
            cfg.value.font.fontWeight = "600";
            cfg.value.formatDigits = 1;
            cfg.point.radius = "0.2em";
            cfg.colors = ["#EB571C","#9F4520"];
            cfg.axis.x.tick.stroke = "#9FADB6";
            cfg.axis.x.font.fontSize = "0.5em";
            cfg.axis.x.font.fontWeight = "600";
            cfg.axis.y.font.fontSize = "0.6em";
            cfg.axis.y.tick.stroke = "#9FADB6";
            cfg.axis.y.tickCount = 4;
            cfg.axis.y.line.strokeWidth = "0.15em";
            cfg.axis.y.line.stroke = "#9FADB6";
            cfg.axis.y.format = "";
            cfg.axis.y.formatDigits = 0;
            cfg.axis.x.lineSpaceMultiplier = 1.5;
            cfg.legend.show = additionalCfg.hasLegend || false;
            cfg.animation.duration = 1000;
            cfg.legend.configuration = {
                direction: "HORIZONTAL",
                hgap: 1,
                icon:{shape:"circle"},
                label: {
                    font: {
                        fontSize: "0.6em"
                    }
                }
            }
        },
        hcLineChartConfiguration: function(cfg) {
            cfg.value.font.fontSize = "0.7em";
            cfg.value.font.fontWeight = "600";
            cfg.value.vMargin = "0.5em";
            cfg.value.format = "";
            cfg.value.formatDigits = 0;
            cfg.colors = ["#196A9D","#9F4520"];
            cfg.axis.x.tick.stroke = "#9FADB6";
            cfg.axis.x.font.fontSize = "0.6em";
            cfg.axis.x.font.fontWeight = "600";
            cfg.axis.y.font.fontSize = "0.6em";
            cfg.axis.y.tick.stroke = "#9FADB6";
            cfg.axis.y.tickCount = 4;
            cfg.axis.y.line.strokeWidth = "0.15em";
            cfg.axis.y.line.stroke = "#9FADB6";
            cfg.axis.y.format = "";
            cfg.axis.y.formatDigits = 0;
            cfg.axis.x.lineSpaceMultiplier = 1;
            cfg.point.radius = "0.2em";
            cfg.animation.duration = 1000;
        },
        numberToMonth : numberToMonth,
        config: config,
        url: url,
        adminMode: adminMode,
        printMode: printMode,
        bgColor: bgColor,
        colorPalette: colorPalette
    }
}]);
