bodApp.directive('backAnimation', ['$browser', '$location','$log', function($browser, $location,$log) {
	return {
		link: function(scope, element) {

			$browser.onUrlChange(function(newUrl) {
				if ($location.absUrl() === newUrl) {
					$log.debug('Back');
					element.addClass('reverse');
				}
			});

			scope.__childrenCount = 0;
			scope.$watch(function() {
				scope.__childrenCount = element.children().length;
			});

			scope.$watch('__childrenCount', function(newCount, oldCount) {
				if (newCount !== oldCount && newCount === 1) {
					element.removeClass('reverse');
				}
			});
		}
	};
}]);
