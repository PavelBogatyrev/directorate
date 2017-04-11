/**
 * Created by ygorenburgov on 30.05.16.
 */

'use strict';

describe('LabelService Unit tests', function() {
    beforeEach(function() {
        module('bodApp');
    });

    var LabelService;
    beforeEach(inject(function (_LabelService_) {
        LabelService = _LabelService_;
    }));

    var curFinYear = (function(){
        var y = (new Date()).getFullYear();
        var m = (new Date()).getMonth();
        return m < 3 ? y : y+1;})();
    var curMonth = (new Date()).getMonth();

    function getMonthName(monthInd) {
        return ["January","February","March","April","May","June","July","August","September","October","November","December"][(monthInd+12)%12];
    }

    function getStringYear(year) {
        return year.toString().substr(-2, 2);
    }

    describe ('resolveLabels test',function() {

            it('should correctly parse {fin_year+n}', function () {
                expect(LabelService.resolveLabels("a{fin_year}b")).toBe("a"+getStringYear(curFinYear)+"b");
                expect(LabelService.resolveLabels("{fin_year+2}bcc")).toBe(""+getStringYear(curFinYear+2)+"bcc");
                expect(LabelService.resolveLabels("cab{fin_year-1}")).toBe("cab"+getStringYear(curFinYear-1));
            });
            it('should correctly parse {month+n}', function () {
                expect(LabelService.resolveLabels("a{month}b")).toBe("a"+getMonthName(curMonth)+"b");
                expect(LabelService.resolveLabels("{month+2}b")).toBe(getMonthName(curMonth+2)+"b");
                expect(LabelService.resolveLabels("a{month-3}")).toBe("a"+getMonthName(curMonth-3));

            })
            it('should correctly parse {fin_year+n} and {month+n} in the same string', function () {
                expect(LabelService.resolveLabels("{fin_year+2}vv{month-3}bcc")).toBe(getStringYear(curFinYear+2)+"vv"+getMonthName(curMonth-3)+"bcc");
            })

        }
    )


});