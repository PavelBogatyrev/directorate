///<reference path="../IWidget.ts"/>
///<reference path="../../../d3/d3.d.ts"/>
///<reference path="WaterfallChartModel.ts"/>
///<reference path="WaterfallChartConfiguration.ts"/>
/**
 * Basic WaterFallChart shows values dynamic
 * @author rlapin
 */
import StringUtils = require("../../utils/StringUtils")
import Legend = require("../legend/Legend")
import ChartUtils = require("../../utils/ChartUtils");
import BBoxtUtils = require("../../utils/BBoxUtils");
class WaterfallChart implements IWidget {
    private model:IWaterfallChartModel;
    xGridPadding:number;
    yGridPadding:number;
    config:WaterfallChartConfiguration;
    element:HTMLElement;

    width:number;
    height:number;
    gridWidth:number;
    gridHeight:number;
    yScale;
    xScale;
    //Store trends x
    legend:Legend;
    isEmpty:boolean;
    xValuesTopPadding:number;
    yTickValues;
    yMin:number;
    yMax:number;
    public WaterFallDefaultConfiguration:WaterfallChartConfiguration = {
        baseFont: {
            size: "default",
                sizeMultiplier: 0.04,
            family: "Open Sans:300"
        },
        tickCount: 5,
        grid: {color: "#2d2b29", opacity: 0.3, lineWidth: 2, type: "HORIZONTAL"},
        padding: {"left": 30, "right": 0, "top": 30, "bottom": 40},
        axis: {
            yTextPosition: "left", "fontPadding": {
                left: 20, right: 0, top: 0, bottom: 20
            }, y: {
                bottomPaddingDelta: 0.9,
                topPaddingDelta: 0.2,
                format: "d",
                formatDigits: 0,
                textColor: "#95A2AB", "font": {

                    size: "0.6em"
                }
            }, x: {
                textColor: "#000", "font": {

                    size: "0.6em"
                }
            }, xTextPosition: "bottom"
        },
        showTitle: false,
        "bar": {
            colors: {
                positive: {
                    background: "#28a74a",
                    opacity: 0.8,
                },
                negative: {
                    background: "#c73040",
                    opacity: 0.8
                },
                startValue: {
                    background: "#356dad",
                    opacity: 1
                },
                endValue: {
                    background: "#68a1cb",
                    opacity: 1
                }
            },
            leftOffset: 0.01,
            rightOffset: 0.01,
            padding: 0,
            "showValues": true,

            "values": {
                "textColor": "#000",
                "position": "top",
                format: "$",
                formatDigits: 1,
                font: {size: "0.8em", weight: "600"}
            },
            "startValue": {
                "textColor": "#000",
                "position": "top",
                format: "$",
                formatDigits: 1,
                font: {size: "0.8em", weight: "600"}
            },
            "endValue": {
                "textColor": "#000",
                "position": "top",
                format: "$",
                formatDigits: 1,
                font: {size: "0.8em", weight: "600"}
            }
        }, animation: {
            active: true
        }

    };


    constructor(target) {
        if (target instanceof HTMLElement) {
            this.element = target;
        }

        if (typeof (target) == "string") {
            var selectors = document.querySelectorAll(target);
            this.element = <HTMLElement>selectors[selectors.length - 1];
        }
        this.width = this.element.offsetWidth;
        this.height = this.element.offsetHeight;
    }

    // VERTICAL
    public fadeIn():void {
        if (this.legend) {
            this.legend.show();
        }
        this.fadeInBars();
        this.fadeInValues();
    }

    public fadeInValues():void {
        var widget = this;
        var model = this.model;
        var config = this.config;
        var animationMultiplier = 1;
        var animationDuration = 500;
        if (config.animation) {
            if (!config.animation.active) {
                animationMultiplier = 0;
            }
            if (config.animation.duration) {
                animationDuration = config.animation.duration;
            }
        }

        if (config.bar.showValues) {
            if (this.config.bar.values.position == "top") {
                d3.select(widget.element).selectAll(".barText").attr("transform", function (d, i) {
                    var translate = d3.transform(d3.select(this).attr("transform")).translate;
                    var offset = 0;
                    if (i == 0 || i == model.data.length - 1) {
                        return "translate(" + translate[0] + "," + (-widget.invertValue(0)) + ")";
                    } else {
                        return "translate(" + translate[0] + "," + (-widget.invertValue(model.data[i - 1] - widget.yMin) + widget.invertValue(0) + translate[1]) + ")";
                    }
                }).transition().delay(function (d, i) {
                    return i * animationDuration * animationMultiplier;
                }).attr("opacity", 1).duration(animationDuration * animationMultiplier).attr("transform", function (d, i) {
                    var text = d3.select(this);
                    var translate = d3.transform(d3.select(this).attr("transform")).translate;
                    var offset = 0;
                    if (d < 0) {
                        offset = widget.invertValue(Math.abs(d));
                    }
                    offset -= -(<SVGTextElement>text.node()).getBBox().height * 0.5;
                    return "translate(" + translate[0] + "," + (-widget.invertValue(model.data[i]) - offset + widget.invertValue(widget.yMin)) + ")";
                }).tween("text", function (d) {
                    var i = d3.interpolate(0, d);
                    return function (t) {
                        d3.select(this).text(StringUtils.formatString(i(t), config.bar.values.format, config.bar.values.formatDigits));
                    }
                });
            }
        }
    }

    private fadeInBars():void {
        var widget = this;
        var config = this.config;
        var model = this.model;
        var animationMultiplier = 1;
        var animationDuration = 500;
        if (config.animation) {
            if (!config.animation.active) {
                animationMultiplier = 0;
            }
            if (config.animation.duration) {
                animationDuration = config.animation.duration;
            }
        }
        d3.select(widget.element).selectAll(".bar rect").attr("transform", function (d, i) {
            var translate = d3.transform(d3.select(this).attr("transform")).translate;
            var offset = 0;
            if (i == 0 || i == model.data.length - 1) {
                return "translate(" + translate[0] + "," + (-widget.invertValue(0)) + ")";
            } else {
                return "translate(" + translate[0] + "," + (-widget.invertValue(model.data[i - 1] - widget.yMin) + widget.invertValue(0)) + ")";
            }
        }).transition().delay(function (d, i) {
            return i * animationDuration * animationMultiplier;
        }).duration(animationDuration * animationMultiplier).attr("height", function (d, i) {
            if (d < 0) {
                d = Math.abs(d);
            }
            if(d==0){
                return config.grid.lineWidth;
            }else {
                if (i == 0 || i == model.data.length - 1) {
                    return widget.invertValue(d - widget.yMin);
                } else {
                    return widget.invertValue(d);
                }
            }
        }).attr("transform", function (d, i) {
            var translate = d3.transform(d3.select(this).attr("transform")).translate;
            var offset = 0;
            if (d < 0) {
                offset = widget.invertValue(Math.abs(d));
            }
            return "translate(" + translate[0] + "," + (-widget.invertValue(model.data[i]) - offset + widget.invertValue(widget.yMin)) + ")";
        }).attr("opacity", function (d, i) {
            if (i == 0) {
                return config.bar.colors.startValue.opacity;
            } else if (i == model.data.length - 1) {
                return config.bar.colors.endValue.opacity;
            } else {
                if (d >= 0) {
                    return config.bar.colors.positive.opacity;
                } else {
                    return config.bar.colors.negative.opacity;
                }
            }
        });
        d3.select(widget.element).selectAll(".barLabel").transition().delay(function (d, i) {
            if (i == 0 || i == model.data.length - 1) return 0;
            else {
                return i * animationDuration * animationMultiplier;
            }
        }).duration(animationMultiplier).attr("opacity", 1);
    }


    public fadeOut():void {
        if (this.legend) {
            this.legend.hide();
        }
        this.fadeOutBars();
        this.fadeOutValues();
    }

    public fadeOutBars():void {
        var widget = this;
        var config = this.config;
        var model = this.model;
        var animationMultiplier = 1;
        var animationDuration = 1000;
        if (config.animation) {
            if (!config.animation.active) {
                animationMultiplier = 0;
            }
            if (config.animation.duration) {
                animationDuration = config.animation.duration;
            }
        }

        d3.select(widget.element).selectAll(".bar rect").transition().duration(animationDuration * animationMultiplier).attr("transform", function (d, i) {
            var translate = d3.transform(d3.select(this).attr("transform")).translate;
            return "translate(" + translate[0] + ",0)";
        }).attr("height", 0);
        d3.select(widget.element).selectAll(".barLabel").transition().attr("opacity", 0);
    }

    public fadeOutValues():void {
        var widget = this;
        var model = this.model;
        var config = this.config;
        var animationMultiplier = 1;
        var animationDuration = 1000;
        if (config.animation) {
            if (!config.animation.active) {
                animationMultiplier = 0;
            }
            if (config.animation.duration) {
                animationDuration = config.animation.duration;
            }
        }

        if (config.bar.showValues) {
            if (this.config.bar.values.position == "top") {
                d3.select(widget.element).selectAll(".barText").transition().duration(function (d, i) {
                    return animationDuration * animationMultiplier;
                }).attr("transform", function (d, i) {
                    var translate = d3.transform(d3.select(this).attr("transform")).translate;
                    var offset = 0;
                    return "translate(" + translate[0] + ",0)";
                }).attr("opacity", 0);
            }
        }
    }

    public render():void {
        this.clear();
        var config = this.config;
        var model = this.model;
        var svg = d3.select(this.element).append("svg");
        var width = this.width;
        var height = this.height;

        svg.style("font-size", this.getBaseFontSize()).style("font-family", config.baseFont.family).attr("width", width).attr("height", height).style("background", config.background)
        if (!model || model.data.length == 0 || model == null) {
            this.isEmpty = true;
        } else {
            this.isEmpty = false;
        }
        if (!this.isEmpty && config.showLegend) {
            this.renderLegend(svg);
        }
        this.drawChart();

    }

    private getBaseFontSize():string {
        if(this.config.baseFont.size == "default"){
            var multiplier = 0.05;
            if(this.config.baseFont.sizeMultiplier){
                multiplier = this.config.baseFont.sizeMultiplier;
            }
            return Math.min(this.width,this.height)*multiplier+"px";
        }
        return this.config.baseFont.size;
    }

    /**
     * Render legend(see {@link WaterFallChartConfiguration} <code> .legend.</code> settings)
     * @param svg - svg element, where you draw barchart
     */
    public renderLegend(svg) {
        var config = this.config;
        var model = this.model;
        var widget = this;
        if (model && model.seriesNames) {

            var legendContainer = svg.append("g");
            var legend = widget.legend = Legend.create(legendContainer.classed("legend", true).attr("transform", "translate(" + 0 + "," + 0 + ")").node());
            var legendData = model.seriesNames.map(function (value, index) {
                return {label: value, color: config.bar.colors[index]};
            });
            legend.setConfig(Legend.defaultConfiguration);
            legend.setConfig(config.legend.configuration);
            legend.setData({data: legendData});

            legend.render();
            legend.hide();
            if (config.legend.position == "BOTTOM") {
                var legendOffset = 5;
                legendContainer.attr("transform", "translate(" + (widget.width / 2 - legend.getBBox().width / 2 + config.padding.left) + "," + (widget.height - legend.getBBox().height - legendOffset) + ")")
            }
        }
    }


    dataUpdate():void {
        this.fadeOut();
        var widget = this;
        setTimeout(function () {
            widget.render();
            widget.fadeIn();
        }, 1000);


    }

    setData(model:IWaterfallChartModel):void {
        this.model = model;
    }

    getData():IWaterfallChartModel {
        return this.model;
    }

    setConfig(config:WaterfallChartConfiguration):void {
        this.config = config;
    }


    /**
     * Calculate left padding based on max value
     * @param yMaxValue
     * @returns {number}
     */
    private calculateLeftPadding(yMaxValue:number):number {
        var config = this.config;
        var widget = this;
        var text = d3.select(widget.element).select("svg").append("text").text(StringUtils.formatString(yMaxValue, config.axis.y.format, config.axis.y.formatDigits)).attr("font-size", config.axis.y.font.size).attr("font-weight", config.axis.y.font.weight).style("font-family", config.axis.y.font.family);
        var offsetMultiplier = 1.3;
        var padding = (<SVGTextElement>(text.node())).getBBox().width * offsetMultiplier;
        text.remove();
        return padding;
    }

    private calculateSize() {
        var config = this.config;
        var model = this.model;
        var xGridPadding = config.padding.left;
        var yGridPadding = this.calculateTopPadding();

        var gridHeight = this.height;
        if (!this.isEmpty && config.showLegend && config.legend.position == "BOTTOM") {
            gridHeight -= this.legend.getBBox().height * 1.2;
        }
        gridHeight -= this.calculateBottomPadding();
        if (config.axis.yTextPosition == "left") {
            xGridPadding += config.axis.fontPadding.left;
        }
        gridHeight -= yGridPadding;
        var gridWidth = this.width - config.padding.right - xGridPadding;

        var yMax = 0;
        var yMin = 0;
        if (this.isEmpty) {
            this.xScale = d3.scale.linear().domain([0, 1]).range([0, gridWidth]);
            yMax = 100;
        } else {

            yMax = d3.max(model.data, function (d) {
                return d;
            });
            yMin = d3.min(model.data, function (d) {
                return d;
            });
            xGridPadding = this.calculateLeftPadding(yMax);
            gridWidth = this.width - config.padding.right - xGridPadding;
            this.xScale = d3.scale.linear().domain([0, model.data.length]).range([0, gridWidth]);
        }
        var k = (yMax - yMin) * config.axis.y.bottomPaddingDelta;
        if(yMin < k){
            yMin = 0;
        }else{
            yMin -= k;
        }

        yMax += (yMax-yMin)*config.axis.y.topPaddingDelta;

        var step = (yMax - yMin) / (config.tickCount - 1);
        yMax = step * (config.tickCount - 1) + yMin;
        this.yTickValues = [];
        for (var i = 0; i < config.tickCount; i++) {
            this.yTickValues.push(i * step + yMin);
        }
        this.yScale = d3.scale.linear().domain([yMin, yMax]).range([gridHeight, 0]);
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.xGridPadding = xGridPadding;
        this.yGridPadding = yGridPadding;
        this.yMin = yMin;
        this.yMax = yMax;

    }

    public drawChart():void {

        var config = this.config;

        var model = this.model;

        var element = this.element;
        var widget = this;


        widget.calculateSize();


        var width = this.width;
        var height = this.height;


        var svg = d3.select(element).select("svg");
        var titlePadding = 50;
        if (!this.isEmpty && config.showTitle) {
            svg.append("text").text(this.model.title).attr("x", titlePadding).attr("y", this.yGridPadding - parseFloat(config.baseFont.size) * Math.min(width, height) / 100).style("text-align", "middle");
        }

        var group = svg.append("g").classed("chartRegion", true);

        group.attr("transform", "translate(" + (this.xGridPadding) + "," + (this.yGridPadding) + ")");

        this.drawGrid(group, this.yTickValues);
        if (!this.isEmpty) {
            this.drawBars(group);
        }


    }


    /**
     * Clear widget
     */
    private clear() {
        var svgElement = d3.select(this.element).select("svg");
        if (svgElement) {
            //make clear
            svgElement.remove();
        }
    }

    /**
     * Draw axis and grid using scaling
     * @param group
     * @param yTickValues
     */
    public drawGrid(group, yTickValues) {
        var config = this.config;
        var model = this.model;
        var xScale = this.xScale;
        var yScale = this.yScale;
        var gridWidth = this.gridWidth;
        var gridHeight = this.gridHeight;
        if (config.grid.type != "none") {
            if (config.grid.type == "BOTH" || config.grid.type == "HORIZONTAL") {
                var yAxis = d3.svg.axis().scale(yScale).orient(config.axis.yTextPosition).tickSize(gridWidth).tickValues(yTickValues)
                    .tickFormat(function (value) {
                        return StringUtils.formatString(value, config.axis.y.format, 0);
                    });
                var yGrid = group.append("g").attr("class", "y axis");
                yGrid.call(yAxis);
                yGrid.select("path").remove();
                yGrid.selectAll(".tick line").attr("stroke", config.grid.color).attr("opacity", config.grid.opacity).attr("stroke-width", config.grid.lineWidth).attr("font-color:green");
            }
            if (config.grid.type == "BOTH" || config.grid.type == "VERTICAL") {
                var xAxis = d3.svg.axis().scale(xScale).orient("bottom").ticks(model.data.length).tickSize(gridHeight);
                var xGrid = group.append("g").attr("class", "x axis");
                xGrid.attr("transform", "translate(50,0)").call(xAxis); //TODO remove absolute value
                xGrid.select("path").remove();
                xGrid.selectAll(".tick line").attr("stroke", config.grid.color).attr("stroke-width", config.grid.lineWidth).attr("font-color:green");
            }
        }
        if (config.axis.yTextPosition != "none") {
            if (config.axis.yTextPosition == "left") {
                group.selectAll(".y.axis").attr("text-anchor", "middle").attr("transform", "translate(" + (gridWidth) + ",0)");
                group.selectAll(".y.axis .tick text").attr("text-anchor", "middle").attr("transform", "translate(0,0)");
            } else {
                group.selectAll(".y.axis .tick text").attr("text-anchor", "middle")
            }
            var tickSelection = group.selectAll(".tick text");
            tickSelection.style("fill", config.axis.y.textColor);
            if (this.isEmpty) {
                group.selectAll(".tick text").remove();
            }
            if (config.axis.y.font) {
                group.selectAll(".tick text").attr("font-size", config.axis.y.font.size).style("font-weight", config.axis.y.font.weight).style("font-family", config.axis.y.font.family)
            }
        } else {
            group.selectAll(".tick text").remove();
        }

    }


    public drawBars(group:d3.Selection<any>):void {
        var config = this.config;
        var model = this.model;
        var widget = this;
        var xScale = this.xScale;
        var yScale = this.yScale;
        var leftOffset = config.bar.leftOffset * widget.gridWidth;
        var rightOffset = config.bar.rightOffset * widget.gridWidth;
        var gridHeight = this.gridHeight;
        var barPadding = (widget.gridWidth - leftOffset - rightOffset) * config.bar.padding;
        var barWidth = (widget.gridWidth - leftOffset - rightOffset) / model.data.length
        var widget = this;
        var dataArray = [model.data[0]];
        for (var i = 1; i < model.data.length; i++) {
            if (i == model.data.length - 1) {
                dataArray.push(model.data[model.data.length - 1]);
            } else {
                dataArray.push(model.data[i] - model.data[i - 1]);
            }
        }

        var bars = group.selectAll(".bar").data(dataArray).enter().append("g").attr("transform", function (d, i) {

            return "translate(" + (leftOffset + barPadding / 2 + (barWidth + barPadding) * i) + "," + gridHeight + ")"
        }).classed("bar", true);

        if (config.axis.xTextPosition != "none") {
            this.drawBarText(bars, barWidth);
        }
        if (config.bar.showValues) {
            widget.drawValuesText(bars, barWidth);
        }
        //total value

        this.drawBar(bars, barWidth);
    }


    /**
     * Draw bargroup text(x values)
     * @param bars
     * @param barWidth
     */
    public drawBarText(bars, barWidth) {
        var config = this.config;
        var widget = this;
        var model = this.model;
        var gridHeight = this.gridHeight;
        var spaceSymbol = "|";
        var textItem = bars.append("text").classed("barLabel", true).text(function(d,i) {return model.seriesNames[i]});

        textItem.attr("text-anchor", "middle").attr("transform", function () {
            return "translate(" + barWidth / 2 + "," + widget.xValuesTopPadding + ")"
        });
        textItem.style("fill", config.axis.x.textColor).attr("opacity", 0);
        if (config.axis.x.font) {
            textItem.style("font-size", config.axis.x.font.size).style("font-weight", config.axis.x.font.weight).style("font-family", config.axis.x.font.family);
        }
        textItem.call(wrap, barWidth);


        function wrap(text, width) {
            text.each(function() {
                var text = d3.select(this),
                    words = text.text().split(/\s+/).reverse(),
                    word,
                    line = [],
                    lineNumber = 0,
                    lineHeight = 1, // ems
                    y = text.attr("y"),
                    dy = parseFloat(text.attr("dy")) || 0,
                    tspan = text.text(null).append("tspan").attr("x", 0).attr("y", y).attr("dy", dy + "em");
                while (word = words.pop()) {
                    var brInd = word.indexOf('<br>');
                    if (brInd>- 1) {
                        var head = word.substring(0,brInd);
                        var tail = word.substring(brInd+4);
                         if (head.length > 0) {
                             words.push("<br>"+tail);
                             words.push(head);
                         } else {
                             tspan.text(line.join(" "));
                             line = [];
                             if (tail.length > 0) {
                                 words.push(tail);
                             }
                             tspan = text.append("tspan").attr("x", 0).attr("dy", lineHeight + dy + "em").text("");
                         }
                        continue;
                    }
                        line.push(word);
                        tspan.text(line.join(" "));
                        if ((tspan.node() as SVGTextElement).getComputedTextLength() > width && line.length > 1) {
                            line.pop();
                            tspan.text(line.join(" "));
                            line = [word];
                            tspan = text.append("tspan").attr("x", 0).attr("dy", lineHeight + dy + "em").text(word);
                        }

                }
            });
        }


    }

    /**
     * Draw bar in bargroup
     * @param bars
     * @param barWidth
     */
    public drawBar(bars, barWidth) {
        var model = this.model;
        var config = this.config;
        var widget = this;
        var bar = bars.append("rect");
        bar.attr("width", function (d, i) {
            return barWidth;
        }).attr("height", function (d, i) {
            return 0;
        }).attr("transform", function (d) {
            return "translate(" + 0 + "," + 0 + ")"
        }).attr("fill", function (d, i) {
            if (i == 0) {
                return config.bar.colors.startValue.background;
            } else if (i == model.data.length - 1) {
                return config.bar.colors.endValue.background;
            } else {
                if (d >= 0) {
                    return config.bar.colors.positive.background;
                } else {
                    return config.bar.colors.negative.background;
                }
            }
        }).classed("barRect", true).attr("opacity",0);
        //text

    }


    public drawValuesText(bar, barWidth) {
        var config = this.config;
        var widget = this;
        var textItem = bar.append("text");
        var barCount = bar.size();
        textItem.text(function (d) {
            return StringUtils.formatString(d, config.bar.values.format, config.bar.values.formatDigits);
        }).classed("barText", true).attr("text-anchor", "middle");
        textItem.style("fill", config.bar.values.textColor).attr("opacity", 0);
        if (config.bar.values.font) {
            textItem
                .style("font-size", function(d,i){
                    var fontProp = i == 0 ? config.bar.startValue.font : i < barCount - 1 ? config.bar.values.font : config.bar.endValue.font;
                    return fontProp.size;
                })
                .style("font-weight", function(d,i){
                    var fontProp = i == 0 ? config.bar.startValue.font : i < barCount - 1 ? config.bar.values.font : config.bar.endValue.font;
                    return fontProp.weight;
                })
                .style("font-family", function(d,i){
                    var fontProp = i == 0 ? config.bar.startValue.font : i < barCount - 1 ? config.bar.values.font : config.bar.endValue.font;
                    return fontProp.family;
                });
        }
        textItem.attr("transform", function (d, i) {
            var offset = -(<SVGTextElement>textItem.node()).getBBox().height * 0.5;
            return "translate(" + (barWidth / 2 ) + "," + offset + ")"
        });
    }

    /**
     * Get invert value for yScale as y axis direction is bottom
     * We get 0 in our scale and find difference between this one and our scaled value
     * @param d
     * @returns {number}
     */
    public invertValue(d) {
        return this.yScale(0) - this.yScale(d);
    }


    public getConfig():WaterfallChartConfiguration {
        return this.config;
    }


    public resize():void {
        this.width = this.element.offsetWidth;
        this.height = this.element.offsetHeight;
    }


    private calculateTopPadding():number {
        var config = this.config;
        var model = this.model;
        var incMul = 1.1;
        return config.padding.top;
    }

    private calculateBottomPadding():number {
        var config = this.config;
        var model = this.model;
        var incMul = 1.1;
        var getBB = this.getTextBBox.bind(this);

        if (config.axis.xTextPosition == "bottom") {
            if (model && model.data && model.data.length > 0) {
                var bboxes = model.seriesNames.map(function (d){
                    return getBB(StringUtils.formatPlainTextToSvgText(d, config.axis.x.font.size), config.axis.x.font.size, config.axis.x.font.weight, config.axis.x.font.family)
                });
                var textBBox = BBoxtUtils.getCompositeBBox(bboxes);
                this.xValuesTopPadding = this.getTextBBox("ABC", config.axis.x.font.size, config.axis.x.font.weight, config.axis.x.font.family).height;
                return textBBox.height * incMul + this.xValuesTopPadding;
            }
        }

        return 0;
    }

    /**
     * Get bbox of text
     * @param str
     * @param fontSize
     * @param fontWeight
     * @param fontFamily
     */
    protected getTextBBox(str:string, fontSize:string, fontWeight:string, fontFamily:string) {
        var config = this.config;
        var widget = this;
        var text = d3.select(widget.element).select("svg").append("text").html(str).attr("font-size", fontSize).attr("font-weight", fontWeight).style("font-family", fontFamily);
        var bbox = (<SVGTextElement>(text.node())).getBBox();
        text.remove();
        return bbox;
    }

    /**
     * Create basic bar chart
     * @param target
     * @param model? (optional)
     * @returns {WaterFallChart}
     */
    public static create(target:any):WaterfallChart {
        var waterfall = new WaterfallChart(target);
        waterfall.setConfig(waterfall.WaterFallDefaultConfiguration);
        return waterfall;
    }


}
export = WaterfallChart;
