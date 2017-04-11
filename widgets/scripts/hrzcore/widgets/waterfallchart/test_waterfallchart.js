var App = require("../../../../build/build.js");
describe('WaterfallChart tests', function() {
    it('should have correct title',function(){
        browser.driver.get('http://localhost:8000/waterfallchart.html');
        expect(browser.driver.getTitle()).toEqual('WaterfallChart');
    });
    it('shouldnt render', function() {
        browser.driver.get('http://localhost:8000/waterfallchart.html');
        browser.executeScript('chart = App.WaterFallChart.create("#waterfallchart");');

        expect(browser.driver.isElementPresent(by.css("#waterfallchart svg"))).toBe(false);




    });
    it('render without data', function() {
        browser.driver.get('http://localhost:8000/waterfallchart.html');
        browser.executeScript('chart = App.WaterFallChart.create("#waterfallchart");');
        browser.executeScript('chart.render()');
        expect(browser.driver.isElementPresent(by.css("#waterfallchart svg"))).toBe(true);




    });
    it('render with data without fadein, bars are not visible', function() {
        browser.driver.get('http://localhost:8000/waterfallchart.html');
        browser.executeScript('chart = App.WaterFallChart.create("#waterfallchart");');
        browser.executeScript('chart.setData({"data": [100,150,200,30,100,100],"seriesNames":["FX2016 prev","FX infl.","FinS S1","FinSS2","Test","END"]});');
        browser.executeScript('chart.render()');
        expect(browser.driver.isElementPresent(by.css("#waterfallchart svg"))).toBe(true);
        browser.driver.findElements(by.css(".barRect")).then(function(results){
           results.forEach(function(result){
               expect(result.getCssValue("opacity")).toEqual('0');
           })
        });




    });
    it('render with data with fadein, bars are visible', function() {
        browser.driver.get('http://localhost:8000/waterfallchart.html');
        browser.executeScript('chart = App.WaterFallChart.create("#waterfallchart");');
        browser.executeScript('chart.setData({"data": [100,150,200,30,100,100],"seriesNames":["FX2016 prev","FX infl.","FinS S1","FinSS2","Test","END"]});');
        browser.executeScript('chart.render()');
        browser.executeScript('chart.fadeIn()');
        expect(browser.driver.isElementPresent(by.css("#waterfallchart svg"))).toBe(true);
        browser.sleep(5000);
        browser.driver.findElements(by.css(".barRect")).then(function(results){
            results.forEach(function(result){
                expect(result.getCssValue("opacity")).not.toEqual('0');
            })
        });




    });
    it('render + fadein without animation', function() {
        browser.driver.get('http://localhost:8000/waterfallchart.html');
        browser.executeScript('chart = App.WaterFallChart.create("#waterfallchart");');
        browser.executeScript('chart.setData({"data": [100,150,200,30,100,100],"seriesNames":["FX2016 prev","FX infl.","FinS S1","FinSS2","Test","END"]});');
        browser.executeScript('chart.render()');
        browser.executeScript('chart.getConfig().animation.active = false');
        browser.executeScript('chart.fadeIn()');
        expect(browser.driver.isElementPresent(by.css("#waterfallchart svg"))).toBe(true);
        browser.driver.findElements(by.css(".barRect")).then(function(results){
            results.forEach(function(result){
                expect(result.getCssValue("opacity")).not.toEqual('0');
            })
        });




    });
    afterEach(function(){
        browser.driver.manage().logs().get('browser').then(function(browserLog) {
            console.log('log: ' + require('util').inspect(browserLog));
            expect(browserLog.length).toEqual(0);
        });
    })
});