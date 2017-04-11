'use strict';

angular.module('bodApp.view_revenue_2year_clients', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/view_revenue_2year_clients', {
            templateUrl: 'screens/view_revenue_2year_clients/view_revenue_2year_clients.html',
            controller: 'ViewR2YCCtrl'
        });
    }])

    .controller('ViewR2YCCtrl', ['$scope', '$routeParams', '$http', 'ConfigurationService', 'LabelService', '$log',
        function ($scope, $routeParams, $http, ConfigurationService, LabelService, $log) {

            $log.debug(ConfigurationService.getQuarters(function (data) {
                $log.debug(data);
            }));
            var url = ConfigurationService.url;

            var widgets = [
                {id: 'widget_8_1'},
                {id: 'widget_8_2'}];
            widgets.forEach(function (widget, index) {
                var widgetView = App.BIChart.createCombineLineBarChart("#" + widget.id);


                var config = widgetView.getConfig();
                config.animation.active = !ConfigurationService.printMode;
                config.bar.padding = 0.1;
                config.baseFont.size = 'default';
                config.baseFont.sizeMultiplier = 0.06;
                config.legend.font.size = "0.5em";
                widgetView.render();
                widget.view = widgetView;
            });

            var listener = function () {
                widgets.forEach(function (widget) {
                    var widgetView = widget.view;
                    widgetView.resize();
                    widgetView.render();
                    widgetView.fadeIn();
                });
            };

            window.addEventListener("resize", listener, false);


            var fadeInListener = function () {
                $log.debug("transition end start rendering widgets");


                    $http.get(url + "api/clientsRevenue").then(function (response) {
                        widgets.forEach(function (widget) {
                            if(widget.id=="widget_8_1") {
                                widget.view.setData(response.data[0]);
                            }else{
                                widget.view.setData(response.data[1]);
                            }

                            widget.view.render();
                            widget.view.fadeIn();
                        });


                });
            };
            var page = document.getElementsByClassName("page")[0];
            page.addEventListener("transitionend", fadeInListener, false);
            $scope.$on('$routeChangeStart', function () {
                $log.debug("remove");
                window.removeEventListener("resize", listener, false);
                page.removeEventListener("resize", fadeInListener, false);
            })
        }]);
