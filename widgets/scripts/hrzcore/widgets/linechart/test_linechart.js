var App = require("../../../../build/build.js");
describe('LineChart tests', function() {


    it('should have correct title',function(){
        browser.driver.get('http://localhost:8000/testlinechart.html');
        expect(browser.driver.getTitle()).toEqual('LineChart');
    });
    it('shouldnt render', function() {
        browser.driver.get('http://localhost:8000/testlinechart.html');
        browser.executeScript('chart = App.LineChart.create("#linechart");');

        expect(browser.driver.isElementPresent(by.css("#linechart svg"))).toBe(false);




    });
    it('render without data', function() {
        browser.driver.get('http://localhost:8000/testlinechart.html');
        browser.executeScript('chart = App.LineChart.create("#linechart");');
        browser.executeScript('chart.render()');
        expect(browser.driver.isElementPresent(by.css("#linechart svg"))).toBe(true);




    });
    it('render with data without fadein', function() {
        browser.driver.get('http://localhost:8000/testlinechart.html');
        browser.executeScript('chart = App.LineChart.create("#linechart");');
        browser.executeScript('chart.setData({"data": [{"values": [10, 20, 15, 30, 40, 10]}],"seriesNames":["May2017", "Jun2017", "Jul2017", "Aug2017", "Sep2017", "Oct2017"]});');
        browser.executeScript('chart.render()');
        expect(browser.driver.isElementPresent(by.css("#linechart svg"))).toBe(true);
        expect(browser.driver.isElementPresent(by.css("#linechart svg .x.axis"))).toBe(true);
        expect(browser.driver.isElementPresent(by.css("#linechart svg .y.axis"))).toBe(true);
        browser.driver.findElements(by.css("#linechart .plot .conline")).then(function(results){
            expect(results.length).toEqual(5);
        });
        browser.driver.findElements(by.css("#linechart .plot .chartpoint")).then(function(results){
            expect(results.length).toEqual(6);
        });


    });


    it('render with data and animation - fadein', function() {
        browser.driver.get('http://localhost:8000/testlinechart.html');
        browser.executeScript('chart = App.LineChart.create("#linechart");');
        browser.executeScript('chart.setData({"data": [{"values": [10, 20, 15, 30, 40, 10]}],"seriesNames":["May2017", "Jun2017", "Jul2017", "Aug2017", "Sep2017", "Oct2017"]});');
        browser.executeScript('chart.getConfig().animation.duration = 1000;');
        browser.executeScript('chart.fadeIn()');
        expect(browser.driver.isElementPresent(by.css("#linechart svg"))).toBe(true);
        expect(browser.driver.isElementPresent(by.css("#linechart svg .x.axis"))).toBe(true);
        expect(browser.driver.isElementPresent(by.css("#linechart svg .y.axis"))).toBe(true);


        browser.driver.findElements(by.css("#linechart .plot .conline")).then(function(results){
            expect(results.length).toEqual(5);
        });
        browser.driver.findElements(by.css("#linechart .plot .chartpoint")).then(function(results){
            expect(results.length).toEqual(6);
            results.forEach(function(result,i){

                expect(result.getAttribute("visibility")).toBe(i==0 ? "visible" : "hidden");
            });

        });

        browser.sleep(2000);


        browser.driver.findElements(by.css("#linechart .plot .conline")).then(function(results){
            expect(results.length).toEqual(5);
            results.forEach(function(result){
                var x1 = result.getAttribute("x1");
                var x2 = result.getAttribute("x2");
                expect(x1!=x2).toBe(true);
            });

        });
        browser.driver.findElements(by.css("#linechart .plot .chartpoint")).then(function(results){
            expect(results.length).toEqual(6);
            results.forEach(function(result){
                expect(result.getAttribute("visibility")).toBe("visible");
            });

        });


    });


    it('fadeout with animation', function() {
        browser.driver.get('http://localhost:8000/testlinechart.html');
        browser.executeScript('chart = App.LineChart.create("#linechart");');
        browser.executeScript('chart.setData({"data": [{"values": [10, 20, 15, 30, 40, 10]}],"seriesNames":["May2017", "Jun2017", "Jul2017", "Aug2017", "Sep2017", "Oct2017"]});');
        browser.executeScript('chart.getConfig().animation.duration = 1000;');
        browser.executeScript('chart.render()');
        expect(browser.driver.isElementPresent(by.css("#linechart svg"))).toBe(true);
        expect(browser.driver.isElementPresent(by.css("#linechart svg .x.axis"))).toBe(true);
        expect(browser.driver.isElementPresent(by.css("#linechart svg .y.axis"))).toBe(true);


        browser.driver.findElements(by.css("#linechart .plot .conline")).then(function(results){
            expect(results.length).toEqual(5);
        });
        browser.driver.findElements(by.css("#linechart .plot .chartpoint")).then(function(results){
            expect(results.length).toEqual(6);
            results.forEach(function(result){
                expect(result.getAttribute("visibility")).toBe("visible");
            });
        });

        browser.executeScript('chart.fadeOut()');

        browser.sleep(2000);

        browser.driver.findElements(by.css("#linechart .plot .chartpoint")).then(function(results){
            expect(results.length).toEqual(6);
            results.forEach(function(result){
                expect(result.getAttribute("visibility")).toBe("hidden");
            });
        });


    });





    afterEach(function(){
        browser.driver.manage().logs().get('browser').then(function(browserLog) {
            console.log('log: ' + require('util').inspect(browserLog));
            expect(browserLog.length).toEqual(0);
        });
    })
});