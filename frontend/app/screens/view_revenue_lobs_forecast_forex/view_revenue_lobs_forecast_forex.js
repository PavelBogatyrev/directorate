'use strict';

angular.module('bodApp.view_revenue_lobs_forecast_forex', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/view_revenue_lobs_forecast_forex', {
            templateUrl: 'screens/view_revenue_lobs_forecast_forex/view_revenue_lobs_forecast_forex.html',
            controller: 'ViewRLFFCtrl'
        });
    }])

    .controller('ViewRLFFCtrl', ['$scope', '$routeParams', '$http', 'ConfigurationService', 'LabelService', '$log','$q',
        function ($scope, $routeParams, $http, ConfigurationService, LabelService, $log,$q) {

            $log.debug(ConfigurationService.getQuarters(function (data) {
                $log.debug(data);
            }));
            var url = ConfigurationService.url;

            var widgets = [
                {url: 'api/revenueByLobs', id: 'widget_4_1'},
                {url: 'api/revenueGCoEByLobs', id: 'widget_4_2'},
                {url: 'api/cmByLobs', id: 'widget_4_3'},
                {url: 'api/cmGCoEByLobs', id: 'widget_4_4'}];
            widgets.forEach(function (widget, index) {
                var widgetView = App.WaterFallChart.create("#" + widget.id);


                var config = widgetView.getConfig();
                config.animation.active = !ConfigurationService.printMode;
                config.axis.y.format = "m";

                config.bar.values.format = "mwf";
                config.tickCount = 4;
                config.axis.y.font.size = "1.2em";
                config.axis.x.font.size = "0.9em";
                config.bar.startValue.font.size = "1.6em";
                config.bar.endValue.font.size = "1.6em";
                config.bar.values.font.size = "1.4em";

                config.baseFont.size = 'default';
                config.baseFont.sizeMultiplier = 0.06;
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
                var promises = widgets.map(function (widget) {
                    return $http.get(ConfigurationService.url+widget.url);
                });


                $q.all(promises).then(function(responses){
                    responses.forEach(function(response,i){
                        widgets[i].view.setData(response.data);
                        widgets[i].view.render();
                        widgets[i].view.fadeIn();
                    })

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
