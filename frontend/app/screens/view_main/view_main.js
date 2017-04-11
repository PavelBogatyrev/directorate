'use strict';

angular.module('bodApp.view_main', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/view_main', {
            templateUrl: 'screens/view_main/view_main.html',
            controller: 'ViewMainCtrl'
        });
    }])

    .controller('ViewMainCtrl', ['$scope', '$routeParams', '$http', 'ConfigurationService', 'LabelService', '$log',
        function ($scope, $routeParams, $http, ConfigurationService, LabelService, $log) {

        var animationContainer = document.getElementById("animationContainer");
        var widget = new App.FigureAnimationWidget(animationContainer);
        var minValue = Math.min(animationContainer.offsetWidth, animationContainer.offsetHeight);
        var NUMBER_OF_FIGURES = 25;
        widget.setConfig(new App.FigureAnimationConfiguration(NUMBER_OF_FIGURES,
            [App.FigureType.TRIANGLE, App.FigureType.OVAL, App.FigureType.RECT],
            [{type:"RECT",color:"#356dad",opacity:1}, {type:"RECT",color:"#356dad",opacity:0.3},{type:"OVAL",color:"#356dad",opacity:0.7},{type:"TRIANGLE",color:"#356dad",opacity:0.1}], minValue * 0.01, minValue * 0.2));
        widget.render();
        var listener = function () {
            widget.resize();
        };

        window.addEventListener("resize",listener,false);
        $scope.$on('$routeChangeStart', function () {
            window.removeEventListener("resize",listener,false);
            widget.fadeOut();
            widget.clear();
        })

    }]);
