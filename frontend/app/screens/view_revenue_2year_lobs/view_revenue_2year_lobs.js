'use strict';

angular.module('bodApp.view_revenue_2year_lobs', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/view_revenue_2year_lobs', {
            templateUrl: 'screens/view_revenue_2year_lobs/view_revenue_2year_lobs.html',
            controller: 'ViewR2YLCtrl'
        });
    }])

    .controller('ViewR2YLCtrl', ['$scope', '$routeParams', '$http', 'ConfigurationService', 'LabelService', '$log',
        function ($scope, $routeParams, $http, ConfigurationService, LabelService, $log) {

            $log.debug(ConfigurationService.getQuarters(function (data) {
                $log.debug(data);
            }));

            var widgets = [
                {id: 'widget_7_1', url: "api/revenueByLobs2YearsBig"},
                {id: 'widget_7_2', url: "api/revenueByLobs2YearsSmall"},
                {id: 'widget_7_3', url: "api/cmByLobs2YearsBig"},
                {id: 'widget_7_4', url: "api/cmByLobs2YearsSmall"}];
            widgets.forEach(function (widget, index) {
                var widgetView = App.BIChart.createCombineLineBarChart("#" + widget.id);
                var config = widgetView.getConfig();
                config.animation.active = !ConfigurationService.printMode;
                config.bar.padding = 0.2;
                config.baseFont.size = 'default';
                config.baseFont.sizeMultiplier = 0.06;
                config.legend.font.size = "0.5em";
                config.bar.values.font.size = "0.7em";
                widget.view = widgetView;
                widgetView.render();
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

                widgets.forEach(function (widget) {
                $http.get(ConfigurationService.url+widget.url).then(function (response) {
                        var data = response.data;

                        if(widget.id == "widget_7_2" || widget.id=="widget_7_4") {
                            widget.view.getConfig().showLegend = false;
                            widget.view.getConfig().padding.top = 30;
                            widget.view.getConfig().barGroup.padding = 0.5;
                        }else{

                        }

                        widget.view.setData(data);
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
