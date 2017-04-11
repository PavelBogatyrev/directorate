/**
 * Basic LineChart shows values dynamic
 * @author ygorenburgov
 */


//references to TypeScript definitions
///<reference path="../IWidget.ts"/>
///<reference path="../../../d3/d3.d.ts"/>
///<reference path="LineChartModel.ts"/>
///<reference path="LineChartConfiguration.ts"/>
///<reference path="../legend/LegendConfiguration.ts"/>
///<reference path="../legend/LegendModel.ts"/>
///<reference path="../legend/Legend.ts"/>

import StringUtils = require("../../utils/StringUtils")
import ChartUtils = require("../../utils/ChartUtils");
import ObjectUtils = require("../../utils/ObjectUtils");
import Legend = require("../legend/Legend");


class LineChart implements IWidget {

    /**
     * The Data Model
     */
    protected model:LineChartModel;

    /**
     * Configuration of chart
     */
    protected configuration:LineChartConfiguration;

    /**
     * The html container for chart
     */
    protected parentEl:HTMLElement;

    /**
     * root svg created in parentEl
     */
    protected rootSVG:d3.Selection<any>;

    /**
     * width of root svg element
     */
    protected width:number;

    /**
     * height of root svg element
     */
    protected height:number;

    /**
     * Cached data layout for drawing points and
     */
    protected pointLayout:Array<Array<any>>;

    /**
     * Cached data layout for drawing lines and
     */
    protected lineLayout:Array<Array<any>>;

    /**
     * xScaling function
     */
    protected xScale;

    /**
     * yScaling function
     */
    protected yScale;


    /**
     * d3.selection of linechart lines connected points
     */
    protected linesSelections;


    /**
     * d3.selection of linechart points
     */
    protected pointSelection;

    /**
     * d3 selection of SVGGElement containing xAxis assets
     */
    protected xAxisSelection;


    /**
     * d3 selection of SVGGElement containing yAxis assets
     */
    protected yAxisSelection;


    /**
     * The width of plotting area - it's not contain area for yAxis
     */
    protected plotWidth;

    /**
     * The height of plotting area - it's not contain area for xAxis
     */
    protected plotHeight;

    /**
     * The left and bottom margins for axes rendering
     */
    //TODO: The possibility different placement of axes nice to have
    protected axisMargins;


    /**
     * Factory method for chart creation
     * @param trg  -   may be HTML element or string css query. It will be a parent DOM element for SVG element containing chart
     * @returns {LineChart}
     */
    public static  create(trg):LineChart {
        return new LineChart(trg);
    }


    /**
     * Default configuration for line chart
     * @type {LineChartConfiguration}
     */

    public static defaultConfiguration:LineChartConfiguration = <LineChartConfiguration>{
        padding: {"left": 0, "right": 0, "top": 25, "bottom": 0},
        defaultGridPadding:{top:0.1, bottom:0.1},
        colors: ["#12263e", "#2a4e79", "#356dad", "#568ac0", "#68a1cb", "#8ba9d2", "#92b7d8"],
        line: {
            strokeWidth: "0.1em",
        },

        point: {
            radius: "0.1em",
            strokeWidth: "0.1em",
            fill: "white"
        },
        value: {
            font: {
                //color of chart title font
                fontFamily: "Open Sans:300",
                fontSize: "1em",
                fontWeight: "400",
                fill: "black",
                stroke: "none"
            },

            vMargin: "1em",
            format: "d",
            formatDigits: 1

        },


// Base font used if is not override
        baseFont: { //color of chart title font
            fontFamily: "Open Sans:300",
            fontSize: "3vmin",
            fontWeight: "400",
            fill: "#2d2b29"
        },

//AXIS SETTINGS
        axis: {
            y: {
                domainExtentIncrease:0.3,
                tickCount:5,
                lineSpaceMultiplier:0.15,
                format: "d",
                formatDigits: 0,
                textColor: "#95A2AB",
                font: {
                    fontSize: "0.5em",
                    fontWeight: "normal",
                    fontFamily:"Open Sans:300"
                },
                tick: {
                    fill: "none",
                    stroke: "#000",
                    strokeWidth:"1px",
                    shapeRendering: "crispEdges",
                },
                line: {
                    fill: "none",
                    stroke: "#000",
                    strokeWidth:"1px",
                    shapeRendering: "crispEdges",
                },
                textLeftMargin: "0.2em",
                textRightMargin: "0.5em"
            },
            x: {

                lineSpaceMultiplier:0.17,
                textColor: "#000",
                font: {
                    fontSize: "0.6em",
                    fontWeight: "normal",
                    fontFamily:"Open Sans:300"
                },
                tick: {
                    fill: "none",
                    stroke: "#000",
                    strokeWidth:"1px",
                    shapeRendering: "crispEdges",
                },
                line: {
                    fill: "none",
                    stroke: "#000",
                    strokeWidth:"1px",
                    shapeRendering: "crispEdges",
                },

                textTopMargin: "1.5em",
                textBottomMargin: "0"
            }
        },

        legend: {
            show: false,
            marginLeft: 0,
            marginRight: 0,
            marginTop: 0,
            marginBottom: 2,
            position:"BOTTOM"
        },

        animation: {
            duration: 2000,
            active: true
        }

    }


    /**
     * Instantiates chart to particular target
     * @param target -   may be HTML element or string css query. It will be a parent DOM element for SVG element containing chart
     */
    constructor(target) {

        if (target instanceof HTMLElement) {
            this.parentEl = target;
        }

        if (typeof (target) == "string") {
            var selectors = document.querySelectorAll(target);
            this.parentEl = <HTMLElement>selectors[selectors.length - 1];
        }
        this.width = this.parentEl.offsetWidth;
        this.height = this.parentEl.offsetHeight;
        this.setConfig(LineChart.defaultConfiguration);
        this.createRootSVG();





    }

    /**
     * Shows default grid while data is not received
     */

    public showDefaultGrid():void {

        var that = this;
        var cfg = this.configuration;

        var yMax = (1-cfg.defaultGridPadding.top)*this.height;
        var yMin = cfg.defaultGridPadding.bottom*this.height;

        var yScale = d3.scale.linear().range([yMax, yMin]);
        yScale.domain([0,5]);

        var yAxis = d3.svg.axis()
            .scale(yScale)
            .orient("right").tickValues([0.01,1,2,3,4,5]).tickSize(this.width);


        var yAxisGroup = this.yAxisSelection = this.rootSVG.append("g")
            .attr("class", "y axis").attr("transform", "translate(" + 0+ "," + 0 + ")")
            .call(yAxis);
        yAxisGroup.call(customizeYAxis);

        function customizeYAxis(g) {
            g.selectAll("text").style("display","none");

            g.selectAll("line").style(ObjectUtils.convertObjectKeysToKebabCase(cfg.axis.y.tick));
            g.selectAll("path").attr("display", "none");

            g.append("line")
                .attr("x1", 0).attr("y1", yMin).attr("x2", 0).attr("y2", yMax).style(ObjectUtils.convertObjectKeysToKebabCase(cfg.axis.y.line)).classed("axis-line",true);
        }
    }


    /**
     * Setting data for visualization
     * @param data
     */
    public setData(data:LineChartModel):void {
        this.model = data;
    }


    /**
     * Set configuration
     * @param config
     */
    public setConfig(config:LineChartConfiguration):void {
        this.configuration = ObjectUtils.deepExtend({}, config);
    }

    /**
     * get configuration
     * @returns {LineChartConfiguration}
     */
    public getConfig():LineChartConfiguration {
        return this.configuration;
    }

    /**
     * Data layout for point drawing
     * The definition of conception of data layout see  http://github.com/d3/d3/wiki/Layouts
     *  @returns {{xValue: Date, yValue: number}[]}  -  data layout for producing points and labels
     */

    protected createPointDataLayout():Array<Array<{xValue:String; yValue:Number}>> {
        var model = this.model;
        var xData = model.seriesNames;
        var yData = model.data;
        return yData.map(function (data) { return xData.map(function (d, i) {
            return {
                xValue: xData[i],
                yValue: data.values[i]
            }
        })})
    }


    /**
     * Data layout for line drawing
     * @param pointLayout
     * @returns {*}
     */

    protected createLineDataLayout() {
        var layouts = this.pointLayout.map(function(pointLayout) {
            var l = pointLayout.length;
            var ret = pointLayout.map(function (d, i, arr) {
                return (i == l - 1) ? {} : {
                    xValue: d.xValue,
                    yValue: d.yValue,
                    xNextValue: arr[i + 1].xValue,
                    yNextValue: arr[i + 1].yValue
                }
            });
            ret.splice(ret.length - 1, 1);
            return ret;
        });
        return layouts;
    }


    /**
     * Onit lines of chart in provided trg
     * @param trg - d3 selected group element
     */

    protected initLines(trg):void {
        var layeredData = this.lineLayout;
        var cfg = this.configuration;
        var xScale = this.xScale;
        var yScale = this.yScale;
        var linesSelection = this.linesSelections = [];
        layeredData.forEach(function(d,i) {
            var lineSelection = trg.selectAll(".conline.l"+i).data(d).enter()
                .append("line").attr("visibility", "hidden").classed("conline", true).classed("l"+i, true)
                .attr("x1", function (d) {
                    return xScale(d.xValue)
                })
                .attr("y1", function (d) {
                    return yScale(d.yValue)
                })
                .attr("x2", function (d) {
                    return xScale(d.xValue)
                })
                .attr("y2", function (d) {
                    return yScale(d.yValue)
                });

            lineSelection.style("stroke-width", cfg.line.strokeWidth).style("stroke", cfg.colors[i]);
            linesSelection.push(lineSelection);
        });

    };


    /**
     * Instantly render lines without animation
     */

    protected renderLines() {
        var xScale = this.xScale;
        var yScale = this.yScale;
        this.linesSelections.forEach(function(line) {
            line.attr("visibility", "visible")
                .attr("x2", function (d) {
                    return xScale(d.xNextValue)
                })
                .attr("y2", function (d) {
                    return yScale(d.yNextValue)
                })
        });

    }


    /**
     * fadeIn lines with particular animation
     */

    protected fadeInLines() {
        var xScale = this.xScale;
        var yScale = this.yScale;
        var timeStep = this.configuration.animation.active ? this.configuration.animation.duration : 0 / (this.model.seriesNames.length-1);
        this.linesSelections.forEach(function (line) {
            line.attr("visibility", "visible")
                .transition().ease("linear").delay(function (d, i) {
                    return timeStep * i
                }).duration(timeStep)
                .attr("x2", function (d) {
                    return xScale(d.xNextValue)
                })
                .attr("y2", function (d) {
                    return yScale(d.yNextValue)
                });
        });

    }


    /**
     * fadeOut lines with particular animation
     */

    protected fadeOutLines() {
        var xScale = this.xScale;
        var yScale = this.yScale;
        var linesCount = this.model.seriesNames.length-1;
        var timeStep = linesCount > 0 ? this.configuration.animation.active ? this.configuration.animation.duration : 0 / linesCount : 0;
        this.linesSelections.forEach(function(line){
            line.attr("visibility", "visible")
                .transition().ease("linear").delay(function (d, i) {
                    return (timeStep * i)
                }).duration(timeStep)
                .attr("x1", function (d) {
                    return xScale(d.xNextValue)
                })
                .attr("y1", function (d) {
                    return yScale(d.yNextValue)
                })
        })


    }


    /**
     * Init points and labels of chart in provided trg
     * @param trg - d3 selected group element
     */

    protected initPoints(trg):void {
        var layeredData = this.pointLayout;
        var colors = this.configuration.colors;
        var cfg = this.configuration.point;
        var cfgValue = this.configuration.value;
        var xScale = this.xScale;
        var yScale = this.yScale;
        var pointSelection = this.pointSelection = [];
        layeredData.forEach(function(data, i){
            var g = trg.selectAll(".chartpoint.l"+i).data(data).enter().append("g")
                .attr("transform", function (d) {
                    return "translate(" + xScale(d.xValue) + "," + yScale(d.yValue) + ")";
                }).attr("visibility", "hidden").classed("chartpoint",true).classed("l"+i,true);

            g.append("circle")
                .attr("r", cfg.radius)
                .attr("stroke", colors[i])
                .attr("stroke-width", cfg.strokeWidth)
                .attr("fill", colors[i])
            g.append("text")
                .style("font-size",cfgValue.font.fontSize)
                .style("font-family",cfgValue.font.fontFamily)
                .style("font-weight",cfgValue.font.fontWeight)
                .style("fill", colors[i])
                .text(function (d) {
                    return "" + StringUtils.formatString(StringUtils.truncateDecimals(d.yValue,cfgValue.formatDigits+1), cfgValue.format, cfgValue.formatDigits);
                }).attr("y", function(d,j) {
                    var isTop = true;
                    var ialt = 0; !i ? ialt = 1 : ialt;
                    if (layeredData[ialt] && layeredData[ialt][j]) {
                        if (yScale(layeredData[ialt][j].yValue) < yScale(d.yValue)) { isTop = false;}
                    }
                    return isTop ? "-"+cfgValue.vMargin : parseFloat(cfgValue.vMargin) + parseFloat(cfgValue.font.fontSize)*1.1 + ChartUtils.getCssUnit(cfgValue.vMargin);
                }).style("text-anchor", "middle");
            pointSelection.push(g);
        });

    };


    /**
     * Render points instantly without animation
     */

    protected renderPoints() {
        this.pointSelection.forEach(function (sel) {
            sel.attr("visibility", "visible");
        })
    }


    /**
     * fadeIn points with particular animation
     */

    protected fadeInPoints() {
        var timeStep = this.configuration.animation.active ? this.configuration.animation.duration : 0 / (this.model.seriesNames.length - 1);
        this.pointSelection.forEach(function(sel){
            sel.transition().ease("linear").delay(function (d, i) {
                return timeStep * i
            }).duration(0)
                .attr("visibility", "visible")
        })

    }


    /**
     * fadeOut points with particular animation
     */

    protected fadeOutPoints() {
        var pointCount = this.model.seriesNames.length;
        var timeStep = pointCount > 1 ? this.configuration.animation.active ? this.configuration.animation.duration : 0 / (pointCount - 1) : 0;
        this.pointSelection.forEach(function(sel){
            sel.transition().ease("linear").delay(function (d, i) {
            return timeStep * i
        }).duration(0)
            .attr("visibility", "hidden")
    });
    }


    /**
     * defines margins required for axis placement
     * @returns {{w: number, h: number}}
     */

    protected defineAxisMargins() {
        //TODO: it may be defined  more precisely ?
        var cfg = this.configuration;
        var svg = this.rootSVG;
        var g = this.rootSVG.append("g");
        var yMax = d3.max(this.model.data[0].values, function (d) {
            return d;
        });

        g.append("text").text("" + StringUtils.formatString(yMax, cfg.axis.y.format, cfg.axis.y.formatDigits))
            .style(ObjectUtils.convertObjectKeysToKebabCase(cfg.axis.y.font))
        ;

        var bb = (<SVGGElement>(g.node())).getBBox();
        var yAxisWidth = bb.width;
        var yAxisLabelHeight = bb.height;
        g.remove();

        g = this.rootSVG.append("g");
        var svgHTML = StringUtils.formatPlainTextToSvgText(this.model.seriesNames[0], cfg.axis.x.font.fontSize, cfg.axis.x.lineSpaceMultiplier);
        g.append("text").html(svgHTML)
            .style(ObjectUtils.convertObjectKeysToKebabCase(cfg.axis.x.font))
        ;
        bb = (<SVGGElement>(g.node())).getBBox();
        var xAxisHeight = bb.height;
        g.remove();

        var topPadding =  defineLineLength(cfg.axis.x.textTopMargin);
        return {
            w: yAxisWidth + defineLineLength(cfg.axis.y.textLeftMargin) + defineLineLength(cfg.axis.y.textRightMargin),
            h: xAxisHeight + defineLineLength("" +(parseFloat(cfg.axis.x.textTopMargin) - 2.2*parseFloat(cfg.axis.x.font.fontSize))+"em") + defineLineLength(cfg.axis.x.textBottomMargin),
            h2: yAxisLabelHeight/2
        }

        function defineLineLength(length) {
            g = svg.append("g");
            g.append("line").attr("x2", length)
            var ret = (<SVGGElement>(g.node())).getBBox().width;
            g.remove();
            return ret;
        }
    }


    /**
     * Prepare scales, axises and grid  and render them instantly
      */
    protected prepareAxes() {

        //TODO: Some refactoring may be required - breaking this method in several ones
        //TODO: The grid and axes animation nice to be implemented

        var width = this.plotWidth;
        var height = this.plotHeight;
        var svg = this.rootSVG;
        var axisMargins = this.axisMargins;
        var cfg = this.configuration.axis;
        var cfgValue = this.configuration.value;

        var x = this.xScale = d3.scale.ordinal().rangePoints([0, width]);
        var y = this.yScale = d3.scale.linear().range([height, 0]);
        var pointLayout = this.pointLayout = this.createPointDataLayout();
        this.lineLayout = this.createLineDataLayout();

        const FONT_CENTRING_SHIFT = "0.4em";


        var allYValues = [];
        this.pointLayout.forEach(function(layout) {layout.forEach(function(d){allYValues.push(d.yValue)})});
        var yExtent = d3.extent(allYValues);
        var increasedExtent = getMultipliedNumberExtent(yExtent, cfg.y.domainExtentIncrease);
        y.domain(increasedExtent);

        //var plotHeight =


        var tickValues = [];
        pointLayout[0].forEach(function (d, i) {
            tickValues.push("stub"+i);
            tickValues.push(d.xValue);
        });

        tickValues.push("stub");

        var xAxis = d3.svg.axis()
            .scale(x)
            .orient("top").tickValues(tickValues).tickSize(height)

        var yMin = increasedExtent[0], yMax = increasedExtent[1];
        var yTickStep =  (yMax - yMin)/cfg.y.tickCount;
        var yTickValues = [yMin];
        for (var i=1;i<cfg.y.tickCount;i++) {
            yTickValues.push(yMin + i*yTickStep);
        }

        yTickValues.push(yMax);

        var yAxis = d3.svg.axis()
            .scale(y)
            .orient("right").tickValues(yTickValues).tickSize(width).tickFormat(function (v) {
                return StringUtils.formatString(StringUtils.truncateDecimals(v,cfgValue.formatDigits+1), cfg.y.format, cfg.y.formatDigits)
            });

        x.domain(tickValues);





        var xAxisGroup = this.xAxisSelection = svg.append("g")
            .attr("class", "x axis").attr("transform", "translate(" + axisMargins.w + "," + height + ")")
            .call(xAxis);
        xAxisGroup.call(customizeXAxis);
        this.yAxisSelection && this.yAxisSelection.remove();

        var yAxisGroup = this.yAxisSelection = svg.append("g")
            .attr("class", "y axis").attr("transform", "translate(" + axisMargins.w + "," + 0 + ")")
            .call(yAxis);
        yAxisGroup.call(customizeYAxis);

        function customizeYAxis(g) {
            g.selectAll("text")
                .attr("x", "-" + cfg.y.textRightMargin)
                .attr("dy", FONT_CENTRING_SHIFT)
                .style("text-anchor", "end")
                .style("fill", cfg.y.textColor)
                .style("font-size",cfg.y.font.fontSize)
                .style("font-family",cfg.y.font.fontFamily)
                .style("font-weight",cfg.y.font.fontWeight)
            ;

            g.selectAll("line").style(ObjectUtils.convertObjectKeysToKebabCase(cfg.y.tick))
            ;

            g.selectAll("path").attr("display", "none");

            g.append("line")
                .attr("x1", 0).attr("y1", 0).attr("x2", 0).attr("y2", height).style(ObjectUtils.convertObjectKeysToKebabCase(cfg.y.line)).classed("axis-line",true);
        }

        function customizeXAxis(g) {
            g.selectAll("text")
                .attr("y", cfg.x.textTopMargin)
                .attr("dx", 0)
                .html(function (d, i) {
                    return StringUtils.formatPlainTextToSvgText(this.textContent, cfg.x.font.fontSize, cfg.x.lineSpaceMultiplier);
                });
            g.selectAll("path").attr("display", "none");
            g.append("line")
                .attr("x1", 0).attr("y1", 0).attr("x2", width).attr("y2", 0).style(cfg.x.line).classed("axis-line");

            g.selectAll("line").style(cfg.x.tick);
            g.selectAll(".tick text")
                .style("font-size",cfg.x.font.fontSize)
                .style("font-family",cfg.x.font.fontFamily)
                .style("font-weight",cfg.x.font.fontWeight)
                .style("fill", cfg.x.textColor)
            g.selectAll(".tick:nth-child(odd) text").attr("display", "none");
            g.selectAll(".tick:nth-child(even) line").attr("display", "none");

        }

        function getMultipliedNumberExtent(extent, multiplier) {
            var delta = (extent[1] - extent[0]) * multiplier;
            return [extent[0] - delta < 0 && extent[0] >= 0 ? 0 : Math.floor(extent[0] - delta), Math.ceil(extent[1] + delta)];
        }

    }

    /**
     * check if widget has data and can be rendered
     */

    protected checkData():boolean {

        if (!this.model || !this.model.data || !this.model.data.length || !this.model.data[0].values || !this.model.data[0].values.length) {
            //this.rootSVG.append("text").text("No data").attr("transform","translate("+this.width/2+","+this.height/2+")").style("fill","black");
            return false;
        }
        return true;
    }



    protected plotLeft;
    protected plotTop;


    /**
     * Create svg for chart rendering
     */
    protected createRootSVG():void {
        var width = this.width;
        var height = this.height;
        var cfg = this.configuration;
        this.clear();
        var svg = d3.select(this.parentEl).append("svg").attr({
            width: width,
            height: height
        }).style(ObjectUtils.convertObjectKeysToKebabCase(cfg.baseFont)).append("g").attr("class", "mainG");
        this.rootSVG = svg;

    }




    /**
     * Render line chart without animation
     */

    public init():void {
        var cfg = this.configuration;
        var svg = this.rootSVG;
        svg.attr("transform","translate("+cfg.padding.left+","+cfg.padding.top+")");
        this.plotTop = 0;
        this.plotLeft = 0;
        this.plotWidth = this.width - cfg.padding.left - cfg.padding.right;
        this.plotHeight = this.height - cfg.padding.top - cfg.padding.bottom;

        if (cfg.legend && cfg.legend.show) {
            this.renderLegend();
            this.legend.hide();
            var bb = this.legend.getBBox();
            this.plotHeight -= (bb.height + cfg.legend.marginBottom + cfg.legend.marginTop);
            d3.select(this.legend.getParentEl())
                .attr("transform", "translate(" +  (this.width - bb.width) / 2 + "," +  (this.plotHeight + cfg.legend.marginTop) + ")");

        }

        var axisMargins = this.axisMargins = this.defineAxisMargins();
        this.plotLeft += axisMargins.w;
        this.plotWidth -= axisMargins.w ;
        this.plotHeight -= (axisMargins.h + axisMargins.h2);
        this.plotTop += axisMargins.h2;
        this.prepareAxes();
        var chartLineG = this.rootSVG.append("g").attr("transform", "translate(" + this.plotLeft + ",0)").classed("plot",true);
        this.initLines(chartLineG);
        this.initPoints(chartLineG);

    }


    protected legend:Legend;


    /**
     * rendering svg legend with top left at (x,y)
     * @param x
     * @param y
     */
    protected renderLegend() {
        var cfg = this.configuration;
        var legend:Legend = this.legend = Legend.create(this.rootSVG.append("g").attr("transform", "translate(" + 0 + "," + 0 + ")").node());
        legend.setConfig(cfg.legend.configuration);
        var model = this.model;
        var colors = cfg.colors;

        var data = model.data.map((function (d, i) {
            return {
                label: d.groupName || "",
                color: colors[i],

            }
        }));

        legend.setData({data: data});
        legend.render();
    }



    /**
     * draw chart instantly, without animation
     */
    public render() {

        if (!this.checkData()) {
            return;
        }
        this.createRootSVG();
        this.init();
        this.legend && this.legend.show();
        this.renderLines();
        this.renderPoints();

    }


    /**
     * Animated fadeIn of chart
     */
    public fadeIn():void {
        if (!this.checkData()) {
            return;
        }
        this.createRootSVG();
        this.init();
        this.legend && this.legend.show();
        this.fadeInLines();
        this.fadeInPoints();

    }


    /**
     * Animated fadeout of chart
     */
    public fadeOut():void {
        this.fadeOutLines();
        this.fadeOutPoints();
        this.xAxisSelection.transition().delay(this.configuration.animation.active ? this.configuration.animation.duration : 0).duration(0).attr("visibility", "hidden");
        this.yAxisSelection.transition().delay(this.configuration.animation.active ? this.configuration.animation.duration : 0).duration(0).attr("visibility", "hidden");

    }


    /**
     * Clear widget
     */
    protected clear() {
        var svgElement = d3.select(this.parentEl).select("svg");
        if (svgElement) {
            //make clear
            svgElement.remove();
        }
    }



    public resize():void {
        this.width = this.parentEl.offsetWidth;
        this.height = this.parentEl.offsetHeight;
    }

}

export = LineChart;
