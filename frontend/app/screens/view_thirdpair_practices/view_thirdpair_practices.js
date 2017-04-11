'use strict';

angular.module('bodApp.view_thirdpair_practices', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/view_thirdpair_practices', {
            templateUrl: 'screens/view_thirdpair_practices/view_thirdpair_practices.html',
            controller: 'ViewTPPCtrl'
        });
    }])

    .controller('ViewTPPCtrl', ['$scope', '$routeParams', '$http', 'ConfigurationService', 'LabelService', '$log','$q',
        function ($scope, $routeParams, $http, ConfigurationService, LabelService, $log,$q) {

            $log.debug(ConfigurationService.getQuarters(function (data) {
                $log.debug(data);
            }));
            var widgetsConfig = [{id:"1",chartType:"combine",colors:["#356dad"]},{id:"2",chartType:"combine",colors:["#68A1CB"]},{id:"7",chartType:"line"}];
            var urls = ["api/practiceData","api/practiceHC"];
            var practices = ["DevOps"];
            var widgets = [];
            widgetsConfig.forEach(function(widgetConfig){
                var widgetID = "#widget_p3_"+widgetConfig.id;
                var widget;
                var config;
                if(widgetConfig.chartType === "combine"){
                    widget = App.BIChart.createCombineLineBarChart(widgetID);
                    config = widget.getConfig();
                    config.animation.active = !ConfigurationService.printMode;
                    config.baseFont.size = 'default';
                    config.baseFont.sizeMultiplier = 0.08;
                    config.bar.colors =  widgetConfig.colors;
                    config.legend.position = "bottom-center";
                    config.axis.y.formatDigits = 1;
                    config.axis.y.format = "m";
                    config.legend.font.size = "0.5em";
                    config.barGroup.padding = 0.7;
                }else{
                    widget =App.LineChart.create(widgetID);
                    config = widget.getConfig();
                    config.animation.active = !ConfigurationService.printMode;
                    ConfigurationService.hcLineChartConfiguration(config);
                }


                widget.render();
                widgets.push(widget);
            });

            var listener = function () {
                widgets.forEach(function (widget) {
                    widget.resize();
                    if (widget.callRender) {
                        widget.getConfig().animation.active = false;
                        widget.render();
                        widget.fadeIn();
                    } else {
                        widget.render();
                    }

                });
            };

            window.addEventListener("resize", listener, false);


            var fadeInListener = function () {
                $log.debug("transition end start rendering widgets");
                var requests = [];
                practices.forEach(function(practice){
                    urls.forEach(function(url){
                        requests.push($http.get(ConfigurationService.url+url+"/"+practice));
                    });

                })
                $q.all(requests).then(function(response){
                    practices.forEach(function(practice,i){

                        widgets[3*i].setData(response[2*i].data.revenue);
                        widgets[3*i+1].setData(response[2*i].data.cm);
                        widgets[3*i+2].setData(response[2*i+1].data);
                        if (response[2*i].data.deviations) {
                            response[2*i].data.deviations.forEach(function(d,j){
                                var num = i*6 + 1;
                                var format2 = ((j+1)%3 == 0) ? "":"$m";

                                $scope["asIs_"+(num+j)] = " - / "+App.StringUtils.formatString(d[1],format2,1);
                                $scope["asPlan_"+(num+j)] = " - / "+App.StringUtils.formatString(d[3],format2,1);

                            })
                        }




                    });
                    widgets.forEach(function(widget){
                        widget.render();
                        widget.fadeIn();
                    })
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
