bodAppControllers.controller('RootController', ['$scope', '$rootScope', '$location', 'ConfigurationService', 'localStorageService', 'LabelService', '$http', '$routeParams', '$log',
    function ($scope, $rootScope, $location, ConfigurationService, localStorageService, LabelService, $http, $routeParams, $log) {

        $scope.url = ConfigurationService.url;

        $scope.currentQuarter = ConfigurationService.getCurrentQuarter();

        //ConfigurationService.togglePrintMode($routeParams.mode, $routeParams.currentQuarter);

        //if ('print' == $routeParams.mode) {
        //    $log.debug("Print mode: ");
        //    $scope.printMode = true;
        //    angular.element(document.querySelector("body")).addClass('printmode');
        //    if (currentQuarter && currentQuarter != "") {
        //        config.currentQuarter = currentQuarter;
        //    }
        //} else {
        //    $scope.printMode = false;
        //    angular.element(document.querySelector("body")).removeClass('printmode');
        //}
        //console.log("Print mode: " + $scope.printMode );

        $scope.$on('$routeChangeSuccess', function() {
            // Toggle print mode
            ConfigurationService.togglePrintMode($routeParams.mode, $routeParams.currentQuarter);
            ConfigurationService.printMode = $routeParams.mode == "print";

            if ($routeParams.activePeriod){
              ConfigurationService.activePeriodOverride = $routeParams.activePeriod;
            } else ConfigurationService.activePeriodOverride = '';
          });

        // Apply labels across the application
        $rootScope.labels = Array.apply(null, Array(500)).map(function (_, i) {return i;});
        $rootScope.labels.forEach(function (item) {
            LabelService.getLabel(item, function (label) {
                $rootScope['label_id_' + item] = label;
            });
        });

        // initialize admin mode
        $scope.adminMode = function (){
            return (ConfigurationService.adminMode)
        };



    }]);
