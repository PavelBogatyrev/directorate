'use strict';

angular.module('bodApp.view_lobs_global', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/view_lobs_global', {
            templateUrl: 'screens/view_lobs_global/view_lobs_global.html',
            controller: 'ViewLobsGlobalCtrl'
        });
    }])

    .controller('ViewLobsGlobalCtrl', ['$scope', '$routeParams', '$http', 'ConfigurationService', 'LabelService', '$log','$q',
        function ($scope, $routeParams, $http, ConfigurationService, LabelService, $log,$q) {

            $log.debug(ConfigurationService.getQuarters(function (data) {
                $log.debug(data);
            }));
            var requests = [
                {url: "api/globalLobYear", widgets:['widget_16_1','widget_16_2'], deviationsStartNum:1},
                {url: "api/globalLobMonth/" + ConfigurationService.activePeriodOverride,widgets:['widget_16_4','widget_16_5'], deviationsStartNum:7},
                {url: "api/globalLobHC", widgets:['widget_16_7']}];


            var widgets = [
                { id:'widget_16_1',  key:"revenue", factoryMethod: App.BODBarChart.createVerticalStackedBarChart,
                    configFunc: function(cfg) {
                        cfg.bar.values.leaderLine = true;
                        cfg.axis.x.font.weight = "600";
                        cfg.axis.y.format="M";
                        cfg.axis.y.formatDigits = 0;

                        cfg.axis.y.font.size = "0.6em";
                        cfg.axis.x.font.size = "0.5em";
                        cfg.barGroup.padding = 0.7;
                        cfg.bar.values.padding = 10;
                        cfg.padding.right = 20;
                        cfg.showLegend = true;
                        cfg.barGroup.totalValue.font.size = "0.75em";
                        cfg.bar.values.font.size="0.55em";

                        cfg.bar.values.format = "M";
                        cfg.bar.values.formatDigits = 1;
                        cfg.bar.colors = ["#12263e","#2a4e79","#356dad","#568ac0","#68a1cb","#8ba9d2","#92b7d8"];
                        cfg.baseFont.size = 'default';
                        cfg.baseFont.sizeMultiplier = 0.08;
                        cfg.legend = {
                            position: "BOTTOM",
                            configuration: {
                                direction: "HORIZONTAL",
                                hgap: 0.5,
                                label: {
                                    font: {
                                        fontSize: "0.5em"
                                    }
                                }
                            }
                        };
                        //   config.bar.values.leaderLine = true;
                        cfg.bar.padding = 0;
                    }, callRender:true},

                {id: 'widget_16_4',  key:"revenue", factoryMethod: App.BODBarChart.createVerticalStackedBarChart,
                    configFunc: function(cfg) {
                        cfg.bar.values.leaderLine = true;
                        cfg.axis.x.font.weight = "600";
                        cfg.axis.y.format="M";
                        cfg.bar.values.padding = 10;
                        cfg.axis.y.formatDigits = 0;
                        cfg.axis.y.font.size = "0.6em";
                        cfg.axis.x.font.size = "0.5em";
                        cfg.barGroup.padding = 0.7;
                        cfg.showLegend = true;
                        cfg.barGroup.totalValue.font.size = "0.75em";
                        cfg.bar.values.font.size="0.6em";

                        cfg.bar.values.format = "M";

                        cfg.bar.colors = ["#12263e","#2a4e79","#356dad","#568ac0","#68a1cb","#8ba9d2","#92b7d8"];
                        cfg.showLegend = false;

                        //   config.bar.values.leaderLine = true;
                        cfg.bar.padding = 0;
                    }, callRender:true},


                {id: 'widget_16_2',  key:"cm", factoryMethod: App.LineChart.create, mockModel:{
                    data: [{groupName: "LOB",  values:[40.0546, 36.546, 36.002,36.001]},
                        {groupName: "LOB without COE",  values:[41.0546, 38.546, 36.002,32.001]}  ],
                    seriesNames: ["2016", "2017 Pl", "2017F","2017??"],
                },
                    configFunc:function(cfg) {


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
                        cfg.axis.x.lineSpaceMultiplier = 0.1;
                        cfg.legend.show = true;

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
                    }
                },
                {id: 'widget_16_5',  key:"cm", factoryMethod: App.LineChart.create,
                    configFunc:function(cfg) {


                        cfg.value.font.fontSize = "0.7em";
                        cfg.value.font.fontWeight = "600";
                        cfg.value.vMargin = "0.5em";
                        cfg.value.format = "";

                        cfg.value.formatDigits = 1;
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
                        cfg.point.radius = "0.2em";


                    }},

                {id: 'widget_16_7', factoryMethod: App.LineChart.create,
                    configFunc:function(cfg) {


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


                    }}



            ];
            widgets.forEach(function (widget) {
                var widgetView = widget.factoryMethod("#" + widget.id);
                var config = widgetView.getConfig();
                config.animation.active = !ConfigurationService.printMode;
                widget.configFunc && widget.configFunc(config);
                widgetView.showDefaultGrid && widgetView.showDefaultGrid();
                if (widget.callRender) {
                    widgetView.render();
                }
                widget.view = widgetView;
                widget.view.render();
            });

            var listener = function () {
                widgets.forEach(function (widget) {
                    var widgetView = widget.view;
                    widgetView.resize();
                    widgetView.fadeIn();
                });
            };

            window.addEventListener("resize", listener, false);


            var fadeInListener = function () {
                $log.debug("transition end start rendering widgets");

                var promises = requests.map(function (request) {
                    return $http.get(ConfigurationService.url+request.url);
                });


                $q.all(promises).then(function(responses){
                    responses.forEach(function (response,i) {
                        var request = requests[i];
                        request.widgets.forEach(function(id){
                            var widget = widgets.find(function(w){return w.id == id;});
                            var data = widget.key ? response.data[widget.key] : response.data;
                            //var data = widget.mockModel;
                            widget.view.setData(data);
                            if (widget.callRender) {
                                widget.view.render();
                            }
                            widget.view.fadeIn();
                        });
                        if (response.data.deviations) {
                            var shift = request.deviationsStartNum;
                            response.data.deviations.forEach(function(d,i){
                                var num = shift + i;
                                var format1 = ((i+1)%3 == 0) ? "":"$m";
                                var format2 = ((i+1)%3 == 0) ? "":"m";

                                $scope["asIs_"+num] = App.StringUtils.formatString(d[0],format1,1)+" / "+App.StringUtils.formatString(d[1],format2,1);
                                $scope["asPlan_"+num] = App.StringUtils.formatString(d[2],format1,1)+" / "+App.StringUtils.formatString(d[3],format2,1);

                            })
                        }

                    });
                })


            };
            var page = document.getElementsByClassName("page")[0];
            page.addEventListener("transitionend", fadeInListener, false);
            $scope.$on('$routeChangeStart', function () {
                $log.debug("remove");
                window.removeEventListener("resize", listener, false);
                page.removeEventListener("resize", fadeInListener, false);
            });



        }]);
