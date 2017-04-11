'use strict';

angular.module('bodApp.view_lobs_telecom', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/view_lobs_telecom', {
            templateUrl: 'screens/view_lobs_telecom/view_lobs_telecom.html',
            controller: 'ViewLobsTelecomCtrl'
        });
    }])

    .controller('ViewLobsTelecomCtrl', ['$scope', '$routeParams', '$http', 'ConfigurationService', 'LabelService', '$log', '$q',
        function ($scope, $routeParams, $http, ConfigurationService, LabelService, $log, $q) {

            $log.debug(ConfigurationService.getQuarters(function (data) {
                $log.debug(data);
            }));

            var requests = [
                {url: "api/lobsYear/TELECOM%20SERVICES", widgets:['widget_14_1','widget_14_2'], deviationsStartNum:1},
                {url: "api/lobsMonth/TELECOM%20SERVICES/" + ConfigurationService.activePeriodOverride,widgets:['widget_14_4','widget_14_5'], deviationsStartNum:7},
                {url: "api/lobsHC/TELECOM%20SERVICES", widgets:['widget_14_7']}];

            var widgets = [
                { id:'widget_14_1',  key:"revenue", factoryMethod: App.BODBarChart.createVerticalStackedBarChart,
                    configFunc: function(cfg) { ConfigurationService.revenueStackedBarChartConfiguration(cfg, {hasLegend: true}) },
                    callRender:true},

                {id: 'widget_14_4',  key:"revenue", factoryMethod: App.BODBarChart.createVerticalStackedBarChart,
                    configFunc: function(cfg) { ConfigurationService.revenueStackedBarChartConfiguration(cfg, {}) },
                    callRender:true},


                {id: 'widget_14_2',  key:"cm", factoryMethod: App.LineChart.create,
                    configFunc: function(cfg) { ConfigurationService.cmLineChartConfiguration(cfg, {hasLegend: true}) }
                },
                {id: 'widget_14_5',  key:"cm", factoryMethod: App.LineChart.create,
                    configFunc: function(cfg) { ConfigurationService.cmLineChartConfiguration(cfg, {}) }
                },
                {id: 'widget_14_7', factoryMethod: App.LineChart.create,
                    configFunc:ConfigurationService.hcLineChartConfiguration
                }

            ];
            widgets.forEach(function (widget) {
                var widgetView = widget.factoryMethod("#" + widget.id);
                var config = widgetView.getConfig();
                config.animation.active = !ConfigurationService.printMode;
                config.baseFont.size = 'default';
                config.baseFont.sizeMultiplier = 0.08;
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
                    if (widget.callRender) {
                        widgetView.getConfig().animation.active = false;
                        widgetView.render();
                        widgetView.fadeIn();
                    } else {
                        widgetView.render();
                    }

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
