/**
 * Use as helper in work with charts
 * @author rlapin
 */


class ChartUtils {
    /**
     * Calculate nice chart range
     * @param min
     * @param max
     * @param ticksCount
     * @returns {{min: number, max: number}}
     */
    static calculate(min:number, max:number, ticksCount:number) {
        var result = [];
        var range = ChartUtils.niceNum(max - min, false);
        var tickSpacing = ChartUtils.niceNum(range / (ticksCount - 1), true);
        for (var cur = min; result.length != ticksCount; cur += tickSpacing) {
            result.push(cur);
        }
        return result;
    }


    static niceNum(range:number, round:boolean):number {
        var exponent:number;
        /** exponent of range */
        var fraction:number;
        /** fractional part of range */
        var niceFraction:number;
        /** nice, rounded fraction */

        exponent = Math.floor(Math.log(range) / Math.LN10);
        fraction = range / Math.pow(10, exponent);

        if (round) {
            if (fraction < 1.5)
                niceFraction = 1;
            else if (fraction < 3)
                niceFraction = 2;
            else if (fraction < 7)
                niceFraction = 5;
            else
                niceFraction = 10;
        } else {
            if (fraction <= 1)
                niceFraction = 1;
            else if (fraction <= 2)
                niceFraction = 2;
            else if (fraction <= 5)
                niceFraction = 5;
            else
                niceFraction = 10;
        }

        return niceFraction * Math.pow(10, exponent);
    }

    /**
     * returns units of css value, For ex  getCssUnit("1.1em")  --->  "em"
     * @param value
     * @return unit
     */

    static getCssUnit(value:string) : string{
        value = value.trim();
        var numLength = parseFloat(value).toString().length;
        return numLength == value.length ? "" : value.substr(numLength).trim();
    }
}
console.log(ChartUtils.calculate(-10, 10, 5));
export = ChartUtils;