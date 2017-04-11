/**
 * Created by andrii on 4/14/16.
 */
bodApp.directive('keypressEvents', [
    '$document',
    '$rootScope',
    function($document, $rootScope) {
        return {
            restrict: 'A',
            link: function() {
                $document.bind('keydown', function(e) {
                    $rootScope.$broadcast('keydown', e);
                    $rootScope.$broadcast('keydown:' + e.which, e);
                });
            }
        };
    }
]);