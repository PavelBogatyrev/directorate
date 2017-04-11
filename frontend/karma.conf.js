module.exports = function(config){
  config.set({

    basePath : './',

    files : [
      'app/bower_components/angular/angular.js',
      'app/bower_components/angular-route/angular-route.js',
      'app/bower_components/angular-mocks/angular-mocks.js',
      'app/bower_components/angular-animate/angular-animate.js',
      'app/bower_components/angular-touch/angular-touch.min.js',
      'app/app.js',
      'app/bower_components/angular-local-storage/dist/angular-local-storage.js',
      'app/js/controllers.js',
      'app/js/directives/ngRepeatWatcher.js',
      'app/screens/**/*.js',
      'app/js/services/*.js',
      /*'app/components/!**!/!*.js',
      'app/view*!/!**!/!*.js'*/
    ],

    autoWatch : true,

    frameworks: ['jasmine'],

    browsers : ['Chrome'],

    plugins : [
            'karma-chrome-launcher',
            'karma-firefox-launcher',
            'karma-jasmine',
            'karma-junit-reporter'
            ],

    junitReporter : {
      outputFile: 'test_out/unit.xml',
      suite: 'unit'
    }

  });
};
