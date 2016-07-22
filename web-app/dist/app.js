(function (angular) {
	'use strict';

	var module;
	
	function BasicController() { 
		var self = this;

		function worry() {
			console.log(':(');
		}

		function activate() {
			// nothing yet
			self.worry = worry;
		}

		activate();
	}

	function basicDirective() {
		return {
			restrict: 'EA',
			templateUrl: 'basic.html',
			scope: {},
			bindToController: {
				data: '='
			},
			controller: BasicController,
			controllerAs: 'basicController'
		};
	}

	module = angular.module(
		'app',
		['uiGmapgoogle-maps']
	).run(
		[
			'$rootScope',
			function ($rootScope) { 
				$rootScope.map = {
					center: {
						latitude: 41,
						longitude: 2
					},
					zoom: 8 
				};
			}
		]
	);

	module.directive('basicDirective', basicDirective);

})(window.angular);