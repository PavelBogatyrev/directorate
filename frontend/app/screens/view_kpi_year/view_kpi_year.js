'use strict';

angular.module('bodApp.view_kpi_year', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/view_kpi_year', {
            templateUrl: 'screens/view_kpi_year/view_kpi_year.html',
            controller: 'ViewKPIYearCtrl'
        });
    }])

    .controller('ViewKPIYearCtrl', ['$scope', '$routeParams', '$http', 'ConfigurationService', 'LabelService', '$log',
        function ($scope, $routeParams, $http, ConfigurationService, LabelService, $log) {

            // Toggle print mode
            ConfigurationService.togglePrintMode($routeParams.mode, $routeParams.currentQuarter);
            var printMode = $routeParams.mode == "print";
            $log.debug(ConfigurationService.getQuarters(function (data) {
                $log.debug(data);
            }));

            var url = ConfigurationService.url + "api/kpiYear";

            var widgets = [
                {id: 'widget_3_1'},
                {id: 'widget_3_2'},
                {id: 'widget_3_3'},
                {id: 'widget_3_4'}];
            widgets.forEach(function (widget, index) {
                var widgetView = App.BODBarChart.createTrendLineBarChart("#" + widget.id);
                var config = widgetView.getConfig();
                config.animation.active = !ConfigurationService.printMode;
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


                $http.get(url).then(function (response) {

                    widgets.forEach(function (widget, i) {
                        var data = response.data[i];
                        var metaData = response.data[i].metaData;
                        data.metaData = [];
                        if(i>0) {
                            data.metaData.push({"values":[],groupName:""});
                            data.metaData.push({"values":[],groupName:""});
                            data.metaData[0].values.push(metaData.PLAN_REVENUE_PERCENT);
                            data.metaData[0].values.push(metaData.FACT_REVENUE_PERCENT);
                            data.metaData[1].values.push(metaData.FORECAST_REVENUE_PERCENT);
                            data.metaData[1].values.push(metaData.FACT_REVENUE_PERCENT);
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
