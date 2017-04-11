/**
 * Created by ygorenburgov on 13.04.16.
 */
'use strict';

/* https://github.com/angular/protractor/blob/master/docs/toc.md */

describe('my app', function() {


    browser.get('index.html');
    describe('resolveLabels', function () {

        it('should correctly resolve {fin_year}', function () {
            expect(browser.executeScript("return window.resolveLabels('aa{fin_year }b',{year:2015, quarter:2})")).toMatch("aaFY15b")
        });
        it('should correctly resolve {fin_year-n}',function () { expect(browser.executeScript("return window.resolveLabels('aa{fin_year - 2}b',{year:2015, quarter:2})")).toMatch("aaFY13b")});
        it('should correctly resolve{fin_year-k} {fin_year-n} (several occurencies)',function () { expect(browser.executeScript("return window.resolveLabels('aa{fin_year - 2}b{fin_year - 1}',{year:2015, quarter:2})")).toMatch("aaFY13bFY14")});
         it('should correctly resolve {report_period}', function () {expect(browser.executeScript("return window.resolveLabels('aa{report_period}b',{year:2015, quarter:2})")).toMatch("aaQ2 FY15b")});
         it('should correctly resolve {fact_sum_period}', function () {expect(browser.executeScript("return window.resolveLabels('aa{fact_sum_period}b',{year:2015, quarter:1})")).toMatch("aaQ1 FY15b")});
         it('should correctly resolve {fact_sum_period}', function () {expect(browser.executeScript("return window.resolveLabels('aa{fact_sum_period}b',{year:2015, quarter:2})")).toMatch("aa1H FY15b")});
         it('should correctly resolve {fact_sum_period}', function () {expect(browser.executeScript("return window.resolveLabels('aa{fact_sum_period}b',{year:2015, quarter:3})")).toMatch("aa9M FY15b")});
         it('should correctly resolve {fact_sum_period}', function () {expect(browser.executeScript("return window.resolveLabels('aa{fact_sum_period}b',{year:2015, quarter:4})")).toMatch("aaFY15b")});

    })
})
