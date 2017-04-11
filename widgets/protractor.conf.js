exports.config = {
    allScriptsTimeout: 11000,

    specs: [
        'scripts/hrzcore/widgets/*/test_*.js'
    ],

    capabilities: {
        'browserName': 'chrome'
    },

    baseUrl: 'http://localhost:8000/',

    framework: 'jasmine',

    jasmineNodeOpts: {
        defaultTimeoutInterval: 30000
    }
};
