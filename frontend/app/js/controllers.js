var bodAppControllers = angular.module('bodAppControllers', []);

bodAppControllers.controller('indexController', ['$scope', '$rootScope', '$route', '$location', '$routeParams',
    function ($scope, $rootScope, $route, $location, $routeParams) {
        // All effects list
        $scope.effects = [
            {name: 'Slide', className: 'slide'},
            {name: 'Slidedown', className: 'slidedown'},
            {name: 'Slideup', className: 'slideup'},
            {name: 'Pop in/out', className: 'pop'},
            {name: 'Fade in/out', className: 'fade'},
            {name: 'Flip', className: 'flip'},
            {name: 'Rotate', className: 'rotate'},
            {name: 'Slide + Pop in', className: 'slide-pop'}
        ];

        $scope.effect = $scope.effects[0].className;

        $scope.pageId = $routeParams.pageId || 1;

        $scope.nextPage = function () {
            $scope.slideClass = "";
            var loc = $location.$$url.split('/view_')[1],
                visible = $rootScope.settings,
                current = $.inArray(loc, visible),
                next = visible[current+1];
            if (next){
                $location.path("/view_" + next);
            }
        };

        $scope.prevPage = function () {
            $scope.slideClass = "prev";
            var loc = $location.$$url.split('/view_')[1],
                visible = $rootScope.settings,
                current = $.inArray(loc, visible),
                prev = visible[current-1];
            if (prev){
                $location.path("/view_" + prev);
            }
        };

        $scope.$on('keydown', function (event, data) {
            if (data.keyCode == 39) {
                if ($scope.lastScreen()) {
                    angular.element(document.querySelector(".bx-next")).triggerHandler('click');
                }
            }
            if (data.keyCode == 37) {
                if ($scope.firstScreen()) {
                    angular.element(document.querySelector(".bx-prev")).triggerHandler('click');
                }
            }
            if (angular.element(document.querySelector(".login input:focus")).length && data.keyCode == 13) {
                angular.element(document.querySelector("input[type='button']")).triggerHandler('click');
            }
        });
        $scope.firstScreen = function () {
            var visible = $rootScope.settings,
                firstPage = visible[0];
            var loc = $location.$$url.split('/view_')[1];
            if (loc == firstPage || !loc) {
                return false;
            } else {
                return true;
            }
        };
        $scope.lastScreen = function () {
            var visible = $rootScope.settings,
                lastPage = visible[visible.length-1];
            var loc = $location.$$url.split('/view_')[1];
            if (loc == lastPage || !loc) {
                return false;
            } else {
                return true;
            }
        };
        $scope.$on('$routeChangeSuccess', function (next, current) {
            if (current.$$route.originalPath=='/login') {
                angular.element(document.querySelector("body")).addClass('login');
                angular.element(document.querySelector("body")).removeClass('app');
            } else {
                angular.element(document.querySelector("body")).removeClass('login');
                angular.element(document.querySelector("body")).addClass('app');
            }
        });
    }]);


bodAppControllers.controller('actionsController', ['$scope', '$rootScope', '$http', 'ConfigurationService', 'localStorageService' , 'fileUpload', '$q', '$route', '$log',
    function ($scope, $rootScope, $http, ConfigurationService, localStorageService, fileUpload, $q, $route, $log) {
        $scope.menus = function ($event) {
            if (angular.element($event.target).hasClass('menu')) {
                angular.element(document.querySelector('#settings')).removeClass('opened');
                angular.element(document.querySelector('.menu-drop')).toggleClass('opened');
            }
            if (angular.element($event.target).hasClass('settings')) {
                angular.element(document.querySelector('#settings')).toggleClass('opened');
                angular.element(document.querySelector('.menu-drop')).removeClass('opened');
            }
        }

        $scope.changeView = function () {
            angular.element(document.querySelector('.menu-drop')).removeClass('opened');
        }

        $scope.status = "";
        $scope.adminMode = function () {
            return (ConfigurationService.adminMode)
        };

        $scope.toggleClass = function ($event) {
            $($event.target).closest('li').toggleClass('active');
        };

        $scope.print = function ($event) {
            var url = ConfigurationService.url,
                pageIndex = [],
                params = {pages : []};

            // In case user already has some settings saved in local storage
            var loc = location.href.substring(0, location.href.lastIndexOf("/"));
            var screens = localStorageService.get('screens');
            if (screens === null) screens = $rootScope.settings;
            for (var i = 0; i < screens.length; ++i){
              params.pages.push('view_' + screens[i]);
            }
            params.activePeriod = '' + $rootScope.activePeriodYear + $rootScope.activePeriodMonth;
            $scope.percents = 0;
            $scope.canceller = null;
            angular.element($event.target).parent('li').addClass('active');
            var state = setInterval(function () {
                $http.get(url + 'pdf/status/' + ConfigurationService.config.currentPeriod).then(
                    function (response, error) {
                        if (response.data.total != 0) {
                            var anim = setInterval(function () {
                                if ($scope.percents == Math.round(response.data.done * 100 / response.data.total)) {
                                    clearInterval(anim);
                                } else {
                                    $scope.percents++;
                                }
                            }, 10)

                        } else {
                            alert('No screenshots available!');
                            $scope.cancel();
                        }
                        if ($scope.percents == 100) {
                            clearInterval(state);
                            $scope.canceller = $q.defer();
                            $http({
                                url: url + 'pdf/print/',
                                method: 'POST',
                                data: params,
                                responseType: 'arraybuffer',
                                timeout: $scope.canceller.promise
                            }).success(function (data, status, headers, config) {
                                var file = new Blob([data], {
                                    type: 'application/csv'
                                });
                                var fileURL = URL.createObjectURL(file);
                                var a = document.createElement('a');
                                a.href = fileURL;
                                a.target = '_blank';
                                a.download = 'dir.pdf';
                                document.body.appendChild(a);
                                a.click();
                                a.remove();
                                $scope.cancel();
                            }).error(function (response) {
                                alert("Can't download PDF!");
                                $scope.cancel();
                            })
                        }
                    }, function (error) {
                        alert('Generation error!');
                        $scope.cancel();
                    });
            }, 2000);
            $scope.cancel = function () {
                clearInterval(state);
                $scope.percents = 0;
                angular.element($event.target).parent('li').removeClass('active');
                if ($scope.canceller) {
                    $scope.canceller.resolve("user cancelled");
                }
            };
        };

        $scope.uploadFile = function () {
            $scope.isUpload = true;

            var file = $scope.file;
            $log.debug('file is ');
            $log.debug(file);
            var uploadUrl = ConfigurationService.url + "fileUpload";
            fileUpload.uploadFileToUrl(file, uploadUrl, function (value) {
                $scope.status = value;
                $scope.isUpload = false;
            });
        };
        $scope.popupShow = function ($event) {
            $($event.target).closest('li').toggleClass('active');
            /*if ($scope.status == 'OK'){
             $route.reload();
             }*/
            angular.element(document.querySelector(".popup input[type='file']")).val(null);
            $scope.status = '';
        };
    }]);

bodAppControllers.controller('settingsController', ['$scope', '$rootScope', '$location', 'ConfigurationService', 'localStorageService', 'LabelService', '$http',
    function ($scope, $rootScope, $location, ConfigurationService, localStorageService, LabelService, $http) {

        LabelService.getLabel(0, function(){  // Waiting for labels
            $scope.settingsListInitial = [    // Initial settings list
                {
                    link:'main',
                    image:'1',
                    text:$scope.label_id_1,
                    hidden:''
                },
                {
                    link:'kpi_month',
                    image:'2',
                    text:$scope.label_id_3,
                    hidden:''
                },
                {
                    link:'kpi_year',
                    image:'3',
                    text:$scope.label_id_12,
                    hidden:''
                },
                {
                    link:'revenue_lobs_forecast_forex',
                    image:'4',
                    text:$scope.label_id_21,
                    hidden:''
                },
                {
                    link:'highlights',
                    image:'5',
                    text:$scope.label_id_32,
                    hidden:''
                },
                {
                    link:'revenue_lobs_pl',
                    image:'6',
                    text:$scope.label_id_57,
                    hidden:''
                },
                {
                    link:'revenue_clients_pl',
                    image:'7',
                    text:$scope.label_id_66,
                    hidden:''
                },
                {
                    link:'revenue_2year_lobs',
                    image:'8',
                    text:$scope.label_id_71,
                    hidden:''
                },
                {
                    link:'revenue_2year_clients',
                    image:'9',
                    text:$scope.label_id_80,
                    hidden:''
                },
                {
                    link:'revenue_lobs_vsplan_forex',
                    image:'12',
                    text:$scope.label_id_221,
                    hidden:''
                },
                {
                    link:'lobs_finx',
                    image:'13',
                    text:$scope.label_id_85,
                    hidden:''
                },
                {
                    link:'lobs_fins_s_2',
                    image:'14',
                    text:$scope.label_id_102,
                    hidden:''
                },
                {
                    link:'lobs_auto',
                    image:'15',
                    text:$scope.label_id_119,
                    hidden:''
                },
                {
                    link:'lobs_enterprise',
                    image:'16',
                    text:$scope.label_id_136,
                    hidden:''
                },
                {
                    link:'lobs_telecom',
                    image:'17',
                    text:$scope.label_id_170,
                    hidden:''
                },
                {
                    link:'lobs_excelian',
                    image:'18',
                    text:$scope.label_id_153,
                    hidden:''
                },
                {
                    link:'lobs_horizon',
                    image:'19',
                    text:$scope.label_id_187,
                    hidden:''
                },
                {
                    link:'lobs_global',
                    image:'20',
                    text:$scope.label_id_204,
                    hidden:''
                },
                // {
                //     link:'lobs_global_4practices',
                //     image:'21',
                //     text:$scope.label_id_375,
                //     hidden:''
                // },
                {
                    link:'firstpair_practices',
                    image:'21',
                    text:$scope.label_id_414,
                    hidden:''
                },
                {
                    link:'secondpair_practices',
                    image:'22',
                    text:$scope.label_id_435,
                    hidden:''
                },
                {
                    link:'thirdpair_practices',
                    image:'23',
                    text:$scope.label_id_456,
                    hidden:''
                }
            ]
            var c = localStorageService.get('sortable'),
                s = localStorageService.get('screens');
            var newOrder = [];
            if (c) {
                // reordering thumbnails according to localStorage if sortable was applied
                newOrder.push($scope.settingsListInitial[0])
                angular.forEach(c.split('&'), function(item,index){
                    var itemIndex = item.replace('[]=0','');
                    angular.forEach($scope.settingsListInitial, function(item, index){
                        if (item.link == itemIndex){
                            itemIndex = index;
                        }
                    })
                    var order = $.inArray(item.replace('[]=0',''), s)+1;
                    if (s) {
                        if (order == 0) {
                            $scope.settingsListInitial[itemIndex].hidden='active'
                        }
                    }
                    newOrder.push($scope.settingsListInitial[itemIndex])
                })
                $scope.settingsList = newOrder;
                $rootScope.settings = s;
            } else {
                if (s) {
                    angular.forEach($scope.settingsListInitial, function(item, index){
                        var order = $.inArray(item.link, s)+1;
                        if (order == 0) {
                            $scope.settingsListInitial[index].hidden='active'
                        }
                    })
                }
                $scope.settingsList = $scope.settingsListInitial;
                $rootScope.settings=[];
                angular.forEach($scope.settingsListInitial, function(item, index){
                    if (item.hidden != 'active'){
                        $rootScope.settings.push(item.link)
                    }
                })
            }
        })

        // Waiting for ng-repeat is done
        $scope.RenderComplete = function () {
            var sortList = $( "#settings .settings-list" );
            // enable sortable plugin
            sortList.sortable({
                items: "li:not(:first-child)",
                'update': function (event, ui) {
                    var order = $(this).sortable('serialize');
                    localStorageService.set('sortable', order);
                }
            });
        }

        $scope.toggleClass = function ($event) {
            if ($($event.target).closest('li').index()>0){
                $($event.target).closest('li').toggleClass('active');
            }
        };

        $scope.formatCurrent = function () {
            var year = ConfigurationService.config.currentQuarter.substring(0, 4),
                quater = ConfigurationService.config.currentQuarter.substring(4, 6);
            $scope.currentQuarter = quater + ' FY' + year;
        };

        function getConfig(config) {
            $scope.confId = config.id;
            $scope.currentPeriod = ConfigurationService.config.currentPeriod = config.currentPeriod;
        }
        function Periods (config){
            $scope.periods = config;
        }
        $scope.$on('$routeChangeSuccess', function (next, current) {
            if (!$scope.periods){
                ConfigurationService.getPeriod(Periods);
            }
        });

        ConfigurationService.getQuarters(function(config) {
            $scope.confId = config.id;
            $scope.currentPeriod = ConfigurationService.config.currentPeriod = config.currentPeriod;
            $rootScope.labels.forEach(function (item) {
                LabelService.getLabel(item, function (label) {
                    $rootScope['label_id_' + item] = label;
                });
            });
        });

        $scope.currentUpdate = function(period){
            $scope.currentPeriod = ConfigurationService.config.currentPeriod = period;
        }

        $scope.collectInfo = function ($event) {
            var visibleItems = [];
            $('#settings .settings-list li').each(function () {
                if (!$(this).hasClass('active')) {
                    visibleItems.push($(this).attr('data-attr'));
                }
            });
            $rootScope.settings = visibleItems;

            if (angular.element(document.querySelector(".bgcolor span.active")).length) {
                ConfigurationService.bgColor = angular.element(document.querySelector(".bgcolor span.active")).attr('data-attr');
            } else {
                $scope.bg = ConfigurationService.bgColor;
            }
            if (ConfigurationService.bgColor == 'white') {
                angular.element(document.querySelector("body")).addClass('white');
            } else {
                angular.element(document.querySelector("body")).removeClass('white');
            }
            ConfigurationService.currentQuarter = angular.element(document.querySelector(".options .drop-holder .active")).attr('data-attr');

            if ($event) {
                ConfigurationService.config.currentQuarter = ConfigurationService.currentQuarter;
                localStorageService.set('currentQuarter', ConfigurationService.config.currentQuarter);
                localStorageService.set('screens', visibleItems);
                localStorageService.set('bg', ConfigurationService.bgColor);
                var period = $scope.currentPeriod;
                ConfigurationService.saveCurrentPeriod(period);
            }

            angular.element(document.querySelector('#settings')).removeClass('opened');
        };
        $scope.collectInfo();
        $scope.formatCurrent();

        $scope.regenerate = function () {
            $scope.disabled = true;
            $http.get(ConfigurationService.url + 'pdf/regenerate/' + ConfigurationService.config.currentPeriod).success(function (response) {
                $scope.disabled = false;
            }).error(function () {
                $scope.disabled = false;
            })
        }

        $scope.regenerateAll = function () {
            $scope.disabled = true;
            $http.get(ConfigurationService.url + 'pdf/regenerateAll/').success(function (response) {
                $scope.disabled = false;
            }).error(function () {
                $scope.disabled = false;
            })
        }

        $scope.adminMode = function () {
            return (ConfigurationService.adminMode)
        };
    }]);
