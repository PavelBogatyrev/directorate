'use strict';

angular.module('bodApp.view_lobs_global_4practices', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/view_lobs_global_4practices', {
            templateUrl: 'screens/view_lobs_global_4practices/view_lobs_global_4practices.html',
            controller: 'ViewLG4PCtrl'
        });
    }])

    .controller('ViewLG4PCtrl', ['$scope', '$routeParams', '$http', 'ConfigurationService', 'LabelService', '$log','$q',
        function ($scope, $routeParams, $http, ConfigurationService, LabelService, $log,$q) {

            $log.debug(ConfigurationService.getQuarters(function (data) {
                $log.debug(data);
            }));

            var request = "api/fourPractices";
            var PRACTICES = ["Creative Labs","IoT","Atlassian and Agile","Big Data"];

            var widgets = [];
                for(var i=0;i<8;i++){
                    var widgetID = "#widget_p_"+(i+1);
                    var widget;
                    var config;
                    if(i%2==0){
                        widget = App.BIChart.createCombineLineBarChart(widgetID);
                        config = widget.getConfig();
                        config.bar.colors =  ["#356dad"];
                    }else{
                        widget =App.BIChart.createCombineLineBarChart(widgetID);

                        config = widget.getConfig();
                        config.bar.colors = ["#68a1cb"];
                        config.lineChart.colors = ["#922a00"];
                        config.axis.y.format = "m";
                        config.axis.y.formatDigits = 1;
                    }
                    config.legend.position = "bottom-center";
                    config.legend.font.size = "0.5em";
                    config.barGroup.padding = 0.5;
                    config.animation.active = !ConfigurationService.printMode;
                    widget.render();
                    widgets.push(widget);
                }

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

                $http.get(ConfigurationService.url+request).then(function(response){
                    PRACTICES.forEach(function(practice,i){
                        if(practice in response.data) {
                            var data = response.data[practice];
                            widgets[2*i].setData(data[0]);
                            widgets[2*i+1].setData(data[1]);
                            data[2].forEach(function(d,j){
                                var format1 = (j==2) ? "%":"$m";

                                $scope["asIs_"+(i+1)+"_"+(j+1)] = App.StringUtils.formatString(d[0],format1,1);
                                $scope["asPlan_"+(i+1)+"_"+(j+1)] = App.StringUtils.formatString(d[1],format1,1);

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
