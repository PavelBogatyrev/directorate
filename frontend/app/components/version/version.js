'use strict';

angular.module('bodApp.version', [
  'bodApp.version.interpolate-filter',
  'bodApp.version.version-directive'
])

.value('version', '0.1');
