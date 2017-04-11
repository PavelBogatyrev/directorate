import LineChart = require("../linechart/LineChart");
class BILineChart extends LineChart {



    /**
     * Factory method for BILineChart creation
     * @param trg  -   may be HTML element or string css query. It will be a parent DOM element for SVG element containing chart
     * @returns {LineChart}
     */
    public static  create(trg):BILineChart {
        return new BILineChart(trg);
    }


    /**
     * custom logic: hiding of repeating y-axis tick labels appearing due rounding
     */
    protected prepareAxes() {
        super.prepareAxes();
        var lastYTick = "";
        var sel = this.yAxisSelection.selectAll("text");
        sel[0].reverse();
        var l = sel.size();
        sel.attr("visibility",function(d,i) {
            var ret = "visible";
            var txt = this.textContent;
            if (txt != lastYTick) {
                lastYTick = txt;
            } else if (i < l-1) {
                ret = "hidden";
            }
            return ret;
        })
    }

}

export = BILineChart;