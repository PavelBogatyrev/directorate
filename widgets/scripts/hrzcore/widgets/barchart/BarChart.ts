///<reference path="../IWidget.ts"/>
///<reference path="../../../d3/d3.d.ts"/>
///<reference path="BarChartModel.ts"/>
///<reference path="BarChartConfiguration.ts"/>
/**
 * BarChart with different styles such as Stacked, Comparation, Horizontal, Vertial and so on
 * @author rlapin
 */
import StringUtils = require("../../utils/StringUtils")
import Legend = require("../legend/Legend")
import ChartUtils = require("../../utils/ChartUtils");
class BarChart implements IWidget {
    // used in formatting output
    static FRACTIONAL_DIGITS = 1;
    xGridPadding:number;
    yGridPadding:number;
    config:BIBarChartConfiguration;
    element:HTMLElement;
    private model:IBarChartModel;
    width:number;
    height:number;
    gridWidth:number;
    gridHeight:number;
    yScale;
    xScale;
    //Store trends x
    trendLinesX:any;
    legend:Legend;
    isEmpty:boolean;
    xValuesPadding:number;
    captionsPadding:number;

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

    public getBaseFontSize():string {
        if(this.config.baseFont.size == "default"){
            var multiplier = 0.05;
            if(this.config.baseFont.sizeMultiplier){
                multiplier = this.config.baseFont.sizeMultiplier;
            }
            return Math.min(this.width,this.height)*multiplier+"px";
        }
        return this.config.baseFont.size;
    }

    // VERTICAL
    public verticalFadeIn():void {

        var widget = this;
        var config = this.config;
        var barGroupPadding = (widget.xScale(1) - widget.xScale(0)) * config.barGroup.padding;
        var barGroupWidth = widget.xScale(1) - widget.xScale(0) - barGroupPadding;
        var gridHeight = widget.gridHeight;
        var animationMultiplier = 1;
        if (config.animation && !config.animation.active) {
            animationMultiplier = 0;
        }
        var curHeight = 0;
        var svgleaderLine = d3.svg.line()
            .x(function (d) {
                return d[0];
            })
            .y(function (d) {
                return d[1];
            });
        if (widget.isStacked()) {
            var leaderLine = config.bar.values.leaderLine;
            d3.select(widget.element).selectAll(".barGroup").each(function (d, i) {
                var heightValues = [];
                var sum = 0;
                var data = d;
                var barGroup = d3.select(this);
                d.values.forEach(function (d) {
                    heightValues.push(sum);
                    sum += <number>d;
                });
                barGroup.selectAll(".bar").transition().duration(1000 * animationMultiplier).attr("height", function (d, i) {
                    return widget.invertValue(d);
                }).attr("transform", function (d, i) {
                    var translate = d3.transform(d3.select(this).attr("transform")).translate;
                    var value = 0;
                    return "translate(" + translate[0] + "," + (-widget.invertValue(d) - widget.invertValue(heightValues[i])) + ")";
                }).each(function (value, barIndex) {
                    if (leaderLine) {
                        var offset = 0;
                        var offset2 = 0;
                        var offset3 = 0;
                        var y2 = -((gridHeight) / (heightValues.length+1) * (barIndex+1)) +widget.width*0.01;
                        var y1 = 0;
                        if (barIndex > 0) {
                            y1 = -widget.invertValue(heightValues[barIndex]) - widget.invertValue(value / 2);
                        } else {
                            y1 = -widget.invertValue(value / 2);
                        }
                        offset = barGroupWidth + widget.width*0.010;
                        offset2 = widget.width*0.012;
                        offset3 = widget.width*0.030;
                        barGroup.append("path").attr("d", svgleaderLine([[offset, y1], [offset + offset2, y2], [offset + offset2 + offset3, y2]])).attr("fill", "none").attr("stroke", "#9ca6ca")
                            .attr("stroke-width", 1).attr('opacity', 0).transition().delay(1000 * animationMultiplier).duration(animationMultiplier * 1000).attr("opacity", 1);

                    }
                });
              /*  if(leaderLine) {
                    barGroup.append("line").attr("x1", barGroupWidth + 6).attr("x2", barGroupWidth + 6).attr("y1", 0)
                        .attr("y2", -widget.invertValue(sum)).attr("fill", "none").attr("stroke", "#9ca6ca")
                        .attr("stroke-width", 1).attr('opacity', 0).transition().delay(1000 * animationMultiplier).duration(animationMultiplier * 1000).attr("opacity", 1);
                }*/

                if (config.barGroup.showTotalValue && config.barGroup.totalValue.position == "top") {

                    barGroup.select(".totalValueText").attr("opacity", 1).attr("transform", function () {
                        var translate = d3.transform(d3.select(this).attr("transform")).translate;
                        return "translate(" + translate[0] + "," + (-widget.invertValue(d.values[d.values.length - 1]) - widget.invertValue(heightValues[heightValues.length - 1]) - config.bar.values.padding) + ")";

                    }).selectAll("tspan").each(function (d, i) {
                        if (i == 0) {
                            d3.select(this).transition().duration(1000 * animationMultiplier).tween("text", function () {
                                var i = d3.interpolateRound(0, heightValues[heightValues.length - 1] + data.values[data.values.length - 1]);
                                return function (t) {
                                    d3.select(this).text(StringUtils.formatString(i(t), config.bar.values.format, config.bar.values.formatDigits));
                                }
                            })
                        } else {
                            d3.select(this).transition().delay(1000 * animationMultiplier).attr("opacity", 1).text("(" + data.values.join(" + ") + ")");
                        }
                    });
                }
                if (config.bar.showValues && config.bar.values.position == "center") {

                    barGroup.selectAll(".barText").attr("opacity", 1).transition().duration(1000 * animationMultiplier).attr("transform", function (d, i) {
                        var translate = d3.transform(d3.select(this).attr("transform")).translate;
                        var offset = 0;
                        if (i > 0) {
                            offset = -widget.invertValue(heightValues[i]) - widget.invertValue(d / 2);
                        } else {
                            offset = -widget.invertValue(d / 2);
                        }
                        if (leaderLine) {
                            offset = -((gridHeight) / (heightValues.length+1) * (i+1)) - 10;
                        }
                        return "translate(" + translate[0] + "," + offset + ")";


                    }).selectAll("tspan").each(function (d) {
                        d3.select(this).attr("alignment-baseline", "middle").transition().duration(1000 * animationMultiplier).tween("text", function () {
                            var i = d3.interpolateRound(0, d);
                            return function (t) {
                                //with format hack HACK
                                var format = config.bar.values.format;
                                var formatDigits = config.bar.values.formatDigits;

                                d3.select(this).text(StringUtils.formatString(i(t), format, formatDigits));
                            }
                        })
                    });
                }
            });

        } else {

            d3.select(widget.element).selectAll(".bar").transition().duration(1000 * animationMultiplier).attr("height", function (d, i) {
                return Math.abs(widget.invertValue(d));
            }).attr("transform", function (d, i) {
                var translate = d3.transform(d3.select(this).attr("transform")).translate;
                var value = 0;
                return "translate(" + translate[0] + "," + (d<0?0:(-widget.invertValue(d))) + ")";
            });
            if (config.bar.showValues && this.config.bar.values.position == "top") {
                this.verticalFadeInValues();
            }
            if (config.showTrendLine) {
                var curBar = 0;
                var barGroupIndex;
                var topLine = d3.svg.area().x(function (d, i) {

                    return widget.trendLinesX[barGroupIndex][curBar];
                }).y0(function (d, i) {
                    return -widget.invertValue(d);
                }).y1(function (d, i) {
                    return 0;
                });
                var topLineAnimation = d3.svg.area().x(function (d, i) {

                    return widget.trendLinesX[barGroupIndex][i + curBar];
                }).y0(function (d, i) {
                    return -widget.invertValue(d);
                }).y1(function (d, i) {
                    return 0;
                });
                var val = widget.getTrendCircleSize();

                var trendCircleArcRight = d3.svg.arc().innerRadius(val.innerRadius).outerRadius(val.outerRadius)
                    .startAngle(function (d) {
                        if (!d.startAngle) {
                            return 0;
                        } else {
                            return d.startAngle;
                        }
                    }).endAngle(function (d) {
                        if (!d.endAngle) {
                            return 0;
                        } else {
                            return d.endAngle;
                        }
                    });
                var trendCircleArcLeft = d3.svg.arc().innerRadius(val.innerRadius).outerRadius(val.outerRadius).startAngle(function (d) {
                    if (!d.startAngle) {
                        return 0;
                    } else {
                        return d.startAngle;
                    }
                }).endAngle(function (d) {
                    if (!d.endAngle) {
                        return 0;
                    } else {
                        return d.endAngle;
                    }
                });
                d3.select(this.element).selectAll(".barGroup").each(function (d, i) {

                    if (d.values.length < 2) {
                        return;
                    }
                    curBar = 0;
                    barGroupIndex = i;
                    var barGroup = d3.select(this);
                    d.values.forEach(function (value, index) {

                        var x0 = widget.trendLinesX[barGroupIndex][index];
                        if (index < d.values.length - 1) {
                            var trendArea = barGroup.append("path");
                            trendArea.classed("trendLine", true).attr("d", function (data) {
                                return topLine([d.values[index], d.values[index]]);
                            }).style("fill", function () {
                                return (d.values[index] <= d.values[index + 1] ? config.trendLine.positive.background : config.trendLine.negative.background);
                            }).style("opacity", 0).style("stroke-width", 2).transition().delay(1000 * animationMultiplier).duration(500 * animationMultiplier).style("opacity", function () {
                                return (d.values[index] <= d.values[index + 1] ? config.trendLine.positive.opacity : config.trendLine.negative.opacity);
                            }).delay((1500 + 1000 * index) * animationMultiplier).duration(1000 * animationMultiplier).attr("d", topLineAnimation([d.values[index], d.values[index + 1]]))
                            var trendText = barGroup.append("text").classed("trendText", true);
                            //get trapezoid center
                            var a = -widget.invertValue(d.values[index + 1]);
                            var b = -widget.invertValue(d.values[index]);
                            var textY = (a + b) / 4;
                            var x1 = widget.trendLinesX[barGroupIndex][index + 1];
                            var textX = ((2 * Math.max(a, b) + Math.min(a, b)) / (a + b) * (x1 - x0) / 3);
                            if (a < b) {
                                textX = x1 - textX;
                            } else {
                                textX = x0 + textX;
                            }
                            if (d.values[index] == 0 && d.values[index + 1] == 0) {
                                textX = (x1 + x0) / 2;
                                textY = -20;
                            }

                            trendText.attr("text-anchor", "middle").attr("y", textY);
                            var trendTextSpan1 = trendText.append("tspan").attr("x", textX).attr("dy", 0).attr("fill", config.trendLine.values.lines[0].textColor).attr("font-weight", config.trendLine.values.lines[0].font.weight)
                                .attr("font-size", config.trendLine.values.lines[0].font.size).text("123")
                                .attr("font-family", config.baseFont.family).attr("text-align", "middle")
                                .attr("opacity", 0).transition().delay((2500 + 1000 * index) * animationMultiplier).duration(1000 * animationMultiplier).attr("opacity", 1).duration(1000 * animationMultiplier).tween("text", function (d) {
                                    var i = d3.interpolate(0, (d.values[index + 1] - d.values[index]));
                                    return function (t) {

                                        d3.select(this).text(StringUtils.formatString(i(t), config.trendLine.values.format, config.trendLine.values.formatDigits));
                                    }
                                });
                            trendText.append("tspan").attr("x", textX).attr("dy", (<SVGTextElement>trendTextSpan1.node()).getBBox().height - 10).attr("fill", config.trendLine.values.lines[1].textColor).attr("font-weight", config.trendLine.values.lines[1].font.weight)
                                .attr("font-size", config.trendLine.values.lines[1].font.size)
                                .attr("font-family", config.baseFont.family).attr("text-align", "middle")
                                .attr("opacity", 0).transition().delay((2500 + 1000 * index) * animationMultiplier).duration(1000 * animationMultiplier).attr("opacity", 1).tween("text", function (d) {
                                    var i = d3.interpolate(0, (d.values[index + 1] - d.values[index]) / (d.values[index]) * 100);
                                    if (d.values[index] == 0) {
                                        if (d.values[index + 1] == 0) {
                                            i = d3.interpolate(0, 0);
                                        } else {
                                            i = d3.interpolate(0, 100);
                                        }

                                    }
                                    return function (t) {
                                        d3.select(this).text(StringUtils.formatString(i(t), "%", BarChart.FRACTIONAL_DIGITS));
                                    }
                                });

                        }
                        barGroup.append("circle").classed("trendCircle", true).attr("cx", x0).attr("cy", -widget.invertValue(value)).
                            attr("r", val.innerRadius).attr("fill", config.trendLine.circle.background).attr("opacity", 0).transition().delay((750 + 1000 * index) * animationMultiplier).duration(500 * animationMultiplier).attr("opacity", 1);

                        curBar++;
                        barGroup.append("path").classed("trendCircle", true).attr("d", trendCircleArcLeft).attr("transform", "translate(" + widget.trendLinesX[0][index] + "," + (-widget.invertValue(value)) + ")").attr("fill", function () {
                            if (index == 0 && d.values[index + 1] - d.values[index] < 0 || d.values[index] - d.values[index - 1] < 0)
                                return config.trendLine.negative.background;
                            else
                                return config.trendLine.positive.background;
                        }).transition().delay((750 + 1000 * index) * animationMultiplier).duration(1000 * animationMultiplier).attrTween("d", function (d) {
                            var start = {startAngle: 0, endAngle: 0};
                            var end = {startAngle: 0, endAngle: -Math.PI};
                            var interpolate = d3.interpolate(start, end);
                            return function (t) {
                                return trendCircleArcLeft(interpolate(t));

                            }
                        });

                        barGroup.append("path").classed("trendCircle", true).attr("d", trendCircleArcRight).attr("transform", "translate(" + widget.trendLinesX[0][index] + "," + (-widget.invertValue(value)) + ")").attr("fill", function () {
                            if (index == d.values.length - 1 && d.values[index] - d.values[index - 1] < 0 || d.values[index + 1] - d.values[index] < 0)
                                return config.trendLine.negative.background;
                            else
                                return config.trendLine.positive.background;
                        }).transition().delay((1250 + 1000 * index) * animationMultiplier).duration(1000 * animationMultiplier).attrTween("d", function (d) {
                            var start = {startAngle: Math.PI, endAngle: Math.PI};
                            var end = {startAngle: Math.PI, endAngle: 0};
                            var interpolate = d3.interpolate(start, end);
                            return function (t) {
                                return trendCircleArcRight(interpolate(t));

                            }
                        });

                    });
                });

            }
        }


    }

    public verticalFadeInValues() {
        var widget = this;
        var config = this.config;
        var animationMultiplier = 1;
        if (config.animation && !config.animation.active) {
            animationMultiplier = 0;
        }
        var textItem = d3.select(this.element).selectAll(".barText");
        textItem.attr("opacity", 1).transition().duration(1000 * animationMultiplier).tween("text", function (d) {
            var i = d3.interpolate(0, d);
            return function (t) {
                d3.select(this).text(StringUtils.formatString(i(t), config.bar.values.format, config.bar.values.formatDigits));
            }
        }).attr("height", 0)
            .attr("transform", function (d, i) {
                var translate = d3.transform(d3.select(this).attr("transform")).translate;
                return "translate(" + translate[0] + "," + (-widget.invertValue(d) - (<SVGTextElement>textItem.node()).getBBox().height - 5) + ")";

            });
    }

    public verticalFadeOut():void {
        var widget = this;
        var config = this.config;
        var animationMultiplier = 1;
        if (config.animation && !config.animation.active) {
            animationMultiplier = 0;
        }
        d3.select(this.element).selectAll(".bar").transition().duration(1000 * animationMultiplier).attr("height", 0)
            .attr("transform", function (d, i) {
                var translate = d3.transform(d3.select(this).attr("transform")).translate;
                return "translate(" + translate[0] + ",0)";

            });
        if (this.config.bar.showValues && this.config.bar.values.position == "top") {
            this.verticalFadeOutValues();
        }
        if (this.config.showTrendLine) {
            d3.select(this.element).selectAll(".trendLine").transition().duration(100 * animationMultiplier).attr("opacity", 0).remove();
            d3.select(this.element).selectAll(".trendCircle").transition().duration(100 * animationMultiplier).attr("opacity", 0).remove();
            d3.select(this.element).selectAll(".trendText").transition().duration(100 * animationMultiplier).attr("opacity", 0).remove();
        }
    }

    public verticalFadeOutValues() {
        var config = this.config;
        var animationMultiplier = 1;
        if (config.animation && !config.animation.active) {
            animationMultiplier = 0;
        }
        d3.select(this.element).selectAll(".barText").transition().duration(1000 * animationMultiplier).attr("opacity", 0).attr("height", 0)
            .attr("transform", function (d, i) {
                var translate = d3.transform(d3.select(this).attr("transform")).translate;
                return "translate(" + translate[0] + ",0)";

            });
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
        //if(this.config.legend && this.config.legend.show)
        if (!this.isEmpty && config.showLegend) {
            this.renderLegend(svg);
        }
        if (this.config.orientation == "VERTICAL") {
            this.renderVertical();
        } else {
            this.renderHorizontal();
        }
    }

    /**
     * Render legend(see {@link BarChartConfiguration} <code> .legend.</code> settings)
     * @param svg - svg element, where you draw barchart
     */
    public renderLegend(svg) {
        var config = this.config;
        var model = this.model;
        var widget = this;
        if (model && model.seriesNames) {
            var legendOrientation = config.legend.position;
            var legendConfig = config.legend.configuration;

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

    public fadeOut():void {
        if (this.config.orientation == "VERTICAL") {
            this.verticalFadeOut();
        } else {
            this.horizontalFadeOut();
        }
        if (this.legend) {
            this.legend.hide();
        }
    }

    public fadeIn():void {

        if (this.config.orientation == "VERTICAL") {
            this.verticalFadeIn();
        } else {
            this.horizontalFadeIn();
        }
        if (this.legend) {
            this.legend.show();
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

    setData(model:IBarChartModel):void {
        this.model = model;
    }

    getData():IBarChartModel {
        return this.model;
    }

    setConfig(config:BarChartConfiguration):void {
        this.config = config;
    }

    public renderVertical():void {

        var config = this.config;

        var model = this.model;

        var element = this.element;
        var _this = this;
        var width = this.width;
        var height = this.height;

        var xGridPadding = config.padding.left;
        var yGridPadding = this.calculateTopPadding();

        var gridHeight = this.height;
        if (!this.isEmpty && config.showLegend && config.legend.position == "BOTTOM") {
            gridHeight -= this.legend.getBBox().height*1.2;
        }
        gridHeight -= this.calculateBottomPadding();
        if (config.axis.yTextPosition == "left") {
            xGridPadding += config.axis.fontPadding.left;
        }
        gridHeight -= yGridPadding;
        var gridWidth = this.width - config.padding.right - xGridPadding;

        var yMin = 0;
        var yMax = 0;
        if (this.isEmpty) {
            this.xScale = d3.scale.linear().domain([0, 1]).range([0, gridWidth]);
            yMax = 100;
        } else {

            yMax = d3.max(model.data, function (d) {
                if (_this.isBasic()) {

                    return d3.max(d.values, function (d) {
                        return d
                    });
                } else if (_this.isStacked()) {
                    return d3.sum(d.values, function (d) {
                        return d
                    });
                }
            });
            yMax *= 1.2;
            xGridPadding = this.calculateLeftPadding(yMax);
            gridWidth = this.width - config.padding.right - xGridPadding;
            this.xScale = d3.scale.linear().domain([0, model.data.length]).range([0, gridWidth - config.padding.right]);
        }


        if (config.bar.showValues && config.bar.values.position == "top") {
            yMax += ((yMax * (config.bar.values.padding )) / gridHeight);
        }
        var step = (yMax - yMin) / (config.tickCount - 1);
        yMax = step * (config.tickCount - 1) + yMin;
        var yTickValues = [];
        for (var i = 0; i < config.tickCount; i++) {
            yTickValues.push(i * step + yMin);
        }
        this.yScale = d3.scale.linear().domain([0, yMax]).range([gridHeight, 0]);

        var svg = d3.select(element).select("svg");
        var titlePadding = 50;
        if (!this.isEmpty && config.showTitle) {
            svg.append("text").text(this.model.title).attr("x", titlePadding).attr("y", yGridPadding - parseFloat(config.baseFont.size) * Math.min(width, height) / 100).style("text-align", "middle");
        }

        var group = svg.append("g").classed("chartRegion", true);

        group.attr("transform", "translate(" + (xGridPadding) + "," + (yGridPadding) + ")")
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.drawVerticalBarGrid(group, yTickValues);
        if (!this.isEmpty) {
            this.drawVerticalBarGroups(group);
        }


    }

    /**
     * Calculate left padding based on max value
     * @param yMaxValue
     * @returns {number}
     */
    protected calculateLeftPadding(yMaxValue: number):number {
        var config = this.config;
        var widget = this;
        var text = d3.select(widget.element).select("svg").append("text").text(StringUtils.formatString(yMaxValue, config.axis.y.format, config.axis.y.formatDigits)).attr("font-size",config.axis.y.font.size).attr("font-weight",config.axis.y.font.weight).style("font-family", config.axis.y.font.family);
        var padding = (<SVGTextElement>(text.node())).getBBox().width*1.3;
        text.remove()
        return padding;
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
    public drawVerticalBarGrid(group, yTickValues) {
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
                        return StringUtils.formatString(value, config.axis.y.format, config.axis.y.formatDigits);
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


    public drawVerticalBarGroups(group:d3.Selection<any>):void {
        var config = this.config;
        var model = this.model;
        var xScale = this.xScale;
        var yScale = this.yScale;
        var gridHeight = this.gridHeight;
        var gridWidth = this.gridWidth;

        var barGroupPadding = gridWidth/model.data.length * config.barGroup.padding;
        var barGroupWidth = gridWidth/model.data.length - barGroupPadding;
        var widget = this;
        var barGroups = group.selectAll(".barGroup").data(model.data).enter().append("g").attr("transform", function (d, i) {

            return "translate(" + (barGroupPadding / 2 + (barGroupWidth + barGroupPadding) * i) + "," + gridHeight + ")"
        }).classed("barGroup", true);

        if (config.axis.xTextPosition != "none") {
            this.drawBarGroupText(barGroups, barGroupWidth);
        }
        //total value
        if (widget.isStacked() && config.barGroup.showTotalValue) {
            this.drawTotalValueText(barGroups, barGroupWidth);
        }
        this.drawVerticalBar(barGroups, barGroupWidth);
    }

    /**
     * Draw total value text for bargroup. Atm available only in stack mode
     * You should apply option drawTotalValue in config mode.
     */
    private drawTotalValueText(barGroups, barGroupWidth:number):void {
        var config = this.config;
        var gridHeight = this.gridHeight;
        var textItem = barGroups.append("text").classed("totalValueText", true);
        textItem.attr("text-anchor", "middle").attr("transform", function () {
            var offset = config.axis.fontPadding.bottom;
            if (config.axis.xTextPosition == "top") {
                offset = -gridHeight - config.axis.fontPadding.top;
            }
            return "translate(" + barGroupWidth / 2 + "," + offset + ")"
        });
        textItem.style("fill", config.bar.values.textColor).attr("opacity", 0);
        if (config.barGroup.totalValue.font) {
            textItem.style("font-size", config.barGroup.totalValue.font.size)
                .style("font-weight", config.barGroup.totalValue.font.weight)
                .style("fill", config.barGroup.totalValue.textColor)
                .style("font-family", config.barGroup.totalValue.font.family);
        }
        // Total value
        var firstLine = textItem.append("tspan").text("3123");
        firstLine.attr("x", 0).attr("dy", 0);
        //FOR VALUE UNDER TOTAL VALUE textItem.append("tspan").attr("opacity", 0).attr("x", 0).attr("dy", (<SVGTextElement>(textItem.node())).getBBox().height + "");
        // Sum expression
    }

    /**
     * Draw bargroup text(x values)
     * @param barGroups
     * @param gridHeight
     * @param barGroupWidth
     */
    public drawBarGroupText(barGroups, barGroupWidth) {
        var config = this.config;
        var widget = this;
        var gridHeight = this.gridHeight;
        var textItem = barGroups.append("text").classed("barLabel", true);
        textItem.html(function (d) {
            return StringUtils.formatPlainTextToSvgText(d.groupName, <string>config.axis.x.font.size);
        }).attr("text-anchor", "middle").attr("transform", function () {
            return "translate(" + barGroupWidth / 2 + "," + widget.xValuesPadding + ")"
        });
        textItem.style("fill", config.axis.x.textColor);
        if (config.axis.x.font) {
            textItem.style("font-size", config.axis.x.font.size).style("font-weight", config.axis.x.font.weight).style("font-family", config.axis.x.font.family);
        }

    }

    /**
     * Draw bar in bargroup
     * @param barGroup
     * @param barGroupWidth
     */
    public drawVerticalBar(barGroups, barGroupWidth) {
        var model = this.model;
        var config = this.config;
        var widget = this;
        this.trendLinesX = [];

        barGroups.each(function (d, i) {
            widget.trendLinesX.push([]);
            var currentGroup = i;
            var length = d.values.length;
            if (widget.isStacked()) {
                length = 1;
            }
            var barPadding = barGroupWidth / length * config.bar.padding;
            var barWidth = barGroupWidth / length - barPadding;
            if (config.showTrendLine) {
                d.values.forEach(function (d, i) {
                    widget.trendLinesX[currentGroup].push((barPadding / 2 + (barWidth + barPadding) * i) + barWidth / 2);
                });

            }
            var group = barGroups.selectAll(".bar").data(function (d) {
                return d.values;
            }).enter().append("g").classed("barG", true);
            var bar = group.append("rect");
            bar.attr("width", function (d, i) {
                return barWidth;
            }).attr("height", function (d, i) {
                return 0;
            }).attr("transform", function (d, i) {
                if (widget.isBasic()) {
                    // shift bar if basic
                    return "translate(" + (barPadding / 2 + (barWidth + barPadding) * i) + "," + 0 + ")"
                } else {
                    // save current position for every bar in group
                    return "translate(" + (barPadding / 2) + "," + 0 + ")"
                }
            }).attr("fill", function (d, i) {
                return config.bar.colors[i]
            }).classed("bar", true).attr("opacity", 1);

            //text

            if (config.bar.showCaptions) {
                widget.drawCaptions(group, barPadding, barWidth);
            }
            if (config.bar.showValues) {
                widget.drawVerticalBarValuesText(group, barPadding, barWidth);
            }
            if (model.metaData && model.metaData.length != 0 && config.showTrendLine) {
                widget.drawDynamicsTriangles(group, barPadding, barWidth);
            }


        });

    }

    public drawDynamicsTriangles(group, barPadding, barWidth):void {

    }

    /**
     * Draw captions under bars
     */
    public drawCaptions(group, barPadding, barWidth):void {

        var config = this.config;
        var widget = this;
        var textItem = group.append("text");
        textItem.html(function (d, i) {
            return StringUtils.formatPlainTextToSvgText(widget.model.seriesNames[i], config.bar.captions.font.size);
        }).classed("barCaption", true).attr("text-anchor", "middle");
        textItem.style("fill", config.bar.captions.textColor).attr("opacity", 1);
        if (config.bar.values.font) {
            textItem.style("font-size", config.bar.captions.font.size).style("font-weight", config.bar.captions.font.weight).style("font-family", config.bar.captions.font.family);
        }
        textItem.attr("transform", function (d, i) {
            var offset = widget.captionsPadding;
            return "translate(" + (barPadding / 2 + barWidth / 2 + (barWidth + barPadding) * i) + "," + offset + ")"
        });
    }

    public drawVerticalBarValuesText(group, barPadding, barWidth) {
        var config = this.config;
        var widget = this;
        var textItem = group.append("text");
        textItem.text(function (d) {
            return StringUtils.formatString(d, config.bar.values.format, config.bar.values.formatDigits);
        }).classed("barText", true).attr("text-anchor", "middle");
        textItem.style("fill", config.bar.values.textColor).attr("opacity", 0);
        if (config.bar.values.font) {
            textItem.style("font-size", config.bar.values.font.size).style("font-weight", config.bar.values.font.weight).style("font-family", config.bar.values.font.family);
        }
        textItem.attr("transform", function (d, i) {
            var offset = -(<SVGTextElement>textItem.node()).getBBox().height + 5;
            return "translate(" + (barPadding / 2 + barWidth / 2 + (barWidth + barPadding) * i) + "," + offset + ")"
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


    public getConfig():BarChartConfiguration {
        return this.config;
    }


    public resize():void {
        this.width = this.element.offsetWidth;
        this.height = this.element.offsetHeight;
    }


    //HORIZONTAL
    /**
     * Fadein behaviour for horizontal-orientated barchart.
     * Fire when you need to show values
     */
    public horizontalFadeIn():void {
        var widget = this;
        var config = this.config;
        var xScale = this.xScale;
        var yScale = this.yScale;
        var animationMultiplier = 1;
        if (config.animation && !config.animation.active) {
            animationMultiplier = 0;
        }

        d3.select(widget.element).selectAll(".barGroup").each(function (d, i) {
            var widthValues = [];
            var sum = 0;
            var data = d;
            var barGroup = d3.select(this);
            d.values.forEach(function (d) {
                widthValues.push(sum);
                sum += <number>d;
            });
            if (widget.isStacked()) {
                barGroup.selectAll(".bar").transition().duration(1000 * animationMultiplier).attr("width", function (d, i) {
                    return xScale(d);
                }).attr("transform", function (d, i) {
                    var translate = d3.transform(d3.select(this).attr("transform")).translate;
                    return "translate(" + (xScale(widthValues[i])) + "," + translate[1] + ")";
                });
            } else {
                barGroup.selectAll(".barleft").transition().duration(1000 * animationMultiplier).attr("width", function (d, i) {
                    return xScale(d) - xScale(0);
                }).attr("transform", function (d, i) {
                    var translate = d3.transform(d3.select(this).attr("transform")).translate;
                    return "translate(" + (-(xScale(d) - xScale(0)) + xScale(0)) + "," + translate[1] + ")";
                });
                barGroup.selectAll(".barright").transition().duration(1000 * animationMultiplier).attr("width", function (d, i) {
                    return xScale(d) - xScale(0);
                }).attr("transform", function (d, i) {
                    var translate = d3.transform(d3.select(this).attr("transform")).translate;
                    return "translate(" + xScale(0) + "," + translate[1] + ")";
                });
            }
            if (config.barGroup.showTotalValue && config.barGroup.totalValue.position == "top") {

                barGroup.select(".totalValueText").attr("opacity", 1).selectAll("tspan").each(function (d, i) {
                    if (i == 0) {
                        d3.select(this).transition().duration(1000 * animationMultiplier).tween("text", function () {
                            var i = d3.interpolateRound(0, widthValues[widthValues.length - 1] + data.values[data.values.length - 1]);
                            return function (t) {
                                d3.select(this).text(StringUtils.formatString(i(t), config.bar.values.format, config.bar.values.formatDigits));
                            }
                        })
                    }
                });
            }
        });

        if (config.bar.showValues && this.config.bar.values.position == "left") {
            d3.select(this.element).selectAll(".barText").attr("opacity", 1).transition().duration(1000 * animationMultiplier).tween("text", function (d) {
                var i = d3.interpolateRound(0, d);
                return function (t) {
                    d3.select(this).text(StringUtils.formatString(i(t), config.bar.values.format, config.bar.values.formatDigits));
                }
            });
        }

    }

    /**
     * Fadeout behaviour for horizontal-orientated barchart.
     * Fire when you need to hide values
     */
    public horizontalFadeOut():void {
        var widget = this;
        var config = this.config;

        var animationMultiplier = 1;
        if (config.animation && !config.animation.active) {
            animationMultiplier = 0;
        }
        d3.select(this.element).selectAll(".bar").transition().duration(1000 * animationMultiplier).attr("width", 0);
        if (this.config.bar.showValues && this.config.bar.values.position == "left") {
            d3.select(this.element).selectAll(".barText").transition().duration(1000 * animationMultiplier).attr("opacity", 0);
        }
        if (this.config.showTrendLine) {
            d3.select(this.element).selectAll(".trendLine").transition().duration(100 * animationMultiplier).attr("opacity", 0).remove();
            d3.select(this.element).selectAll(".trendCircle").transition().duration(100 * animationMultiplier).attr("opacity", 0).remove();
            d3.select(this.element).selectAll(".trendText").transition().duration(100 * animationMultiplier).attr("opacity", 0).remove();
        }
    }

    /**
     * Render if orientation is horizontal
     */
    public renderHorizontal():void {
        var config = this.config;

        var model = this.model;

        var element = this.element;
        var width = this.width;
        var height = this.height;
        var widget = this;
        var xGridPadding = config.padding.left;
        var yGridPadding = this.calculateTopHorizontalPadding();
        if (config.showTitle) {
            yGridPadding += parseFloat(config.baseFont.size) * Math.min(width, height) / 100;
        }
        var gridHeight = this.height;
        if (config.axis.xTextPosition == "top") {
            yGridPadding += config.axis.fontPadding.top;
        } else if (config.axis.xTextPosition == "bottom") {
            gridHeight -= config.axis.fontPadding.bottom;
        }
        gridHeight -= config.padding.bottom;
        if (config.axis.xTextPosition == "left") {
            xGridPadding += config.axis.fontPadding.left;
        }
        if (config.bar.values.position == "left") {
            xGridPadding += config.bar.values.padding;
        }
        gridHeight -= yGridPadding;


        var xMin = 0;
        var xMax = 0;
        if (this.isEmpty) {
            xMax = 100;
            this.yScale = d3.scale.linear().domain([0, 1]).range([0, gridHeight]);
        } else {
            this.yScale = d3.scale.linear().domain([0, model.data.length]).range([0, gridHeight]);
            xMax = d3.max(model.data, function (d) {
                if (widget.isBasic() || widget.isNegative()) {
                    return d3.max(d.values, function (d) {
                        return d;
                    });
                } else {
                    return d3.sum(d.values, function (d) {
                        return d;
                    });
                }
            });
        }
        if (widget.isNegative()) {
            xMin = -xMax;
        }
        var step = (xMax - xMin) / config.tickCount;
        xMax = step * config.tickCount + xMin;
        var xTickValues = [];
        for (var i = 0; i < config.tickCount; i++) {
            xTickValues.push(i * step + xMin);
        }
        if(!widget.isNegative()){
            //config.model.
            if(config.axis.xTextPosition != "none") {
                xGridPadding = widget.horizontalCalculateLeftPadding(d3.max(model.data, function (d) {
                        return d3.sum(d.values);

                    }), model.data.reduce(function (a, b) {
                        return a.groupName.length > b.groupName.length ? a : b;
                    }).groupName) * 2;
            }else{
                xGridPadding = 0;
            }
        }else{
            xGridPadding = widget.width*0.22;
            config.padding.right = widget.width*0.14;
        }
        var gridWidth = this.width - config.padding.right - xGridPadding;

        this.xScale = d3.scale.linear().domain([xMin, xMax]).range([0, gridWidth]);

        var svg = d3.select(element).select("svg");
        if (config.showTitle) {
            svg.append("text").text(this.model.title).attr("x", 50).attr("y", yGridPadding - parseFloat(config.baseFont.size) * Math.min(width, height) / 100).style("text-align", "middle");
        }
        var group = svg.style("font-size", config.baseFont.size).style("font-family", config.baseFont.family).attr("width", width).attr("height", height).style("background", config.background).append("g").classed("chartRegion", true);

        group.attr("transform", "translate(" + (xGridPadding) + "," + (yGridPadding) + ")")
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.xGridPadding = xGridPadding;
        this.yGridPadding = yGridPadding;

        this.drawHorizontalBarGrid(group, xTickValues);
        if (!this.isEmpty) {
            this.drawHorizontalBarGroups(group);
        }


    }
    /**
     * Calculate left padding based on max value
     * @param yMaxValue
     * @returns {number}
     */
    private horizontalCalculateLeftPadding(maxValue:number,maxStr:string):number {
        var config = this.config;
        var widget = this;
        var text = d3.select(widget.element).select("svg").append("text").text(StringUtils.formatString(maxValue, config.axis.y.format, config.axis.y.formatDigits)).style("font-size", config.barGroup.totalValue.font.size)
            .style("font-weight", config.barGroup.totalValue.font.weight)
            .style("fill", config.barGroup.totalValue.textColor)
            .style("font-family", config.barGroup.totalValue.font.family);
        var padding = (<SVGTextElement>(text.node())).getBBox().width + 10;
        text.remove();
        text = d3.select(widget.element).select("svg").append("text").text(maxStr).style("font-size", config.axis.x.font.size).style("font-weight", config.axis.x.font.weight).style("font-family", config.axis.x.font.family);
        padding += (<SVGTextElement>(text.node())).getBBox().width + 10;
        text.remove();
        return padding;
    }

    /**
     * Draw bargroups for horizontal-orientated barchart.
     * Fire when you need to show values
     */
    public drawHorizontalBarGroups(group:d3.Selection<any>):void {
        var config = this.config;
        var model = this.model;
        var xScale = this.xScale;
        var xGridPadding = this.xGridPadding;
        var yScale = this.yScale;
        var gridHeight = this.gridHeight;
        var barGroupPadding = (yScale(1) - yScale(0)) * config.barGroup.padding;
        var barGroupHeight = yScale(1) - yScale(0) - barGroupPadding;
        var widget = this;
        var gridWidth = this.gridWidth;

        var barGroups = group.selectAll(".barGroup").data(model.data).enter().append("g").attr("transform", function (d, i) {

            return "translate(" + 0 + "," + (barGroupPadding / 2 + (barGroupHeight + barGroupPadding) * i) + ")"
        }).classed("barGroup", true);
        if (!widget.isEmpty) {
            if (widget.isStacked()) {
                if (config.barGroup.showTotalValue) {
                    widget.drawHorizontalTotalValueText(barGroups, barGroupHeight, barGroupPadding);
                }
            }

            if (config.axis.xTextPosition != "none" && !widget.isNegative()) {
                var totalValueTextItem = barGroups.select(".totalValueText");
                var offsetX = -(<SVGTextElement>(totalValueTextItem.node())).getBBox().width;
                var textItem = barGroups.append("text");
                textItem.html(function (d) {
                    return StringUtils.formatPlainTextToSvgText(d.groupName, <string>config.axis.x.font.size);
                }).attr("text-anchor", "start").attr("alignment-baseline", "middle").attr("transform", function () {
                    var offset = -xGridPadding;


                    return "translate(" + offset + "," + (barGroupHeight / 2 + barGroupPadding / 2) + ")"
                });
                textItem.style("fill", config.axis.x.textColor);
                if (config.axis.x.font) {
                    textItem.style("font-size", config.axis.x.font.size).style("font-weight", config.axis.x.font.weight).style("font-family", config.axis.x.font.family);
                }
            }
        }
        this.drawHorizontalBar(barGroups, barGroupHeight);
        if (!widget.isEmpty) {
            if (widget.isNegative()) {
                this.drawHorizontalNegativeBarChartTotalTable(group, barGroupHeight, barGroupPadding);

            }
        }
        if (widget.isNegative()) {
            group.append("line").attr("x1", gridWidth / 2).attr("x2", gridWidth / 2).attr("y1", 0).attr("y2", gridHeight).attr("stroke", "#061208").attr("stroke-width", 2);
        }

    }

    public drawHorizontalNegativeBarChartTotalTable(group, barGroupHeight, barGroupPadding):void {
        //DO NOTHING
    }

    public drawHorizontalTotalValueText(barGroups:any, barGroupHeight:number, barGroupPadding:number):void {
        var config = this.config;
        var gridHeight = this.gridHeight;
        var textItem = barGroups.append("text").classed("totalValueText", true);
        textItem.attr("text-anchor", "middle").attr("alignment-baseline", "middle").attr("transform", function () {

            return "translate(" + 0 + "," + (barGroupHeight / 2 + barGroupPadding / 2) + ")"
        });
        textItem.style("fill", config.bar.values.textColor).attr("opacity", 0);
        if (config.barGroup.totalValue.font) {
            textItem.style("font-size", config.barGroup.totalValue.font.size)
                .style("font-weight", config.barGroup.totalValue.font.weight)
                .style("fill", config.barGroup.totalValue.textColor)
                .style("font-family", config.barGroup.totalValue.font.family);
        }
        // Total value
        var firstLine = textItem.append("tspan").text("3123");
        firstLine.attr("dx", -(<SVGTextElement>(textItem.node())).getBBox().width);
        //FOR VALUE UNDER TOTAL VALUE textItem.append("tspan").attr("opacity", 0).attr("x", 0).attr("dy", (<SVGTextElement>(textItem.node())).getBBox().height + "");
        // Sum expression
    }

    /**
     * Draw bar in bargroup
     * @param barGroups
     * @param barGroupHeight
     */
    public drawHorizontalBar(barGroups, barGroupHeight) {
        var model = this.model;
        var config = this.config;
        var widget = this;
        var xScale = this.xScale;
        var gridHeight = widget.gridHeight;
        var yScale = this.yScale;
        var shadowBarWidth = this.gridWidth;
        barGroups.each(function (d, i) {
            var currentGroup = i;
            var curBarGroup = d3.select(this);
            var length = d.values.length;
            if (widget.isStacked() || widget.isNegative()) {
                length = 1;
            }
            var barPadding = barGroupHeight / length * config.bar.padding;
            var barHeight = barGroupHeight / length - barPadding;


            if (config.showShadowBars) {
                var shadowBar = curBarGroup.append("rect");
                shadowBar.attr("width", function (d, i) {
                    return shadowBarWidth;
                }).attr("height", function (d, i) {
                    return barHeight;
                }).attr("transform", function (d, i) {
                    if (widget.isBasic()) {
                        return "translate(" + 0 + "," + ((barHeight + barPadding) * i) + ")"
                    } else {
                        return "translate(" + 0 + "," + 0 + ")"
                    }
                }).attr("fill", config.shadowBarColor).classed("shadowBarbar", true).attr("opacity", 1);

            }
            if (widget.isNegative()) {
                var group = curBarGroup.selectAll(".barleft").data(function (d) {
                    return [d.values[0]];
                }).enter().append("g");
                var group2 = curBarGroup.selectAll(".barright").data(function (d) {
                    return [d.values[1]];
                }).enter().append("g");

                var bar = group.append("rect");
                bar.attr("width", function (d, i) {
                    return 0;
                }).attr("height", function (d, i) {
                    return barHeight;
                }).attr("transform", function (d, i) {
                    return "translate(" + xScale(0) + "," + (barPadding / 2) + ")";
                }).attr("fill", function (d, i) {
                    return config.bar.colors[0]
                }).classed("barleft", true).attr("opacity", 1);

                var bar = group2.append("rect");
                bar.attr("width", function (d, i) {
                    return 0;
                }).attr("height", function (d, i) {
                    return barHeight;
                }).attr("transform", function (d, i) {
                    return "translate(" + xScale(0) + "," + (barPadding / 2) + ")";
                }).attr("fill", function (d, i) {
                    return config.bar.colors[1]
                }).classed("barright", true).attr("opacity", 1);

            } else {
                var barGroup = curBarGroup.selectAll(".bar").data(function (d) {
                    return d.values;
                }).enter().append("g");
                var barGroupBar = barGroup.append("rect");
                barGroupBar.attr("width", function (d, i) {
                    return 0;
                }).attr("height", function (d, i) {
                    return barHeight;
                }).attr("transform", function (d, i) {
                    if (widget.isBasic()) {
                        return "translate(" + 0 + "," + (barPadding / 2 + (barHeight + barPadding) * i) + ")"
                    } else if (widget.isNegative()) {
                        return "translate(" + yScale(0) + "," + (barPadding / 2) + ")"
                    }
                    else {
                        return "translate(" + 0 + "," + (barPadding / 2) + ")"
                    }

                }).attr("fill", function (d, i) {
                    return config.bar.colors[i]
                }).classed("bar", true).attr("opacity", 1);
            }
            //text
            if (widget.isStacked() || widget.isNegative()) {

            } else {
                if (config.bar.showValues) {
                    var textItem = group.append("text");
                    textItem.text(function (d) {
                        return StringUtils.formatString(<number>d, config.bar.values.format, config.bar.values.formatDigits);
                    }).classed("barText", true).attr("text-anchor", "end").attr("transform", function (d, i) {
                        var offset = -config.bar.values.padding;
                        var offsetY = 5;
                        return "translate(" + offset + "," + (barPadding / 2 + barHeight / 2 + (barHeight + barPadding) * i + offsetY) + ")"
                    });
                    textItem.style("fill", config.bar.values.textColor).attr("opacity", 1);
                    if (config.bar.values.font) {
                        textItem.style("font-size", config.bar.values.font.size).style("font-weight", config.bar.values.font.weight).style("font-family", config.bar.values.font.family);
                    }
                }
            }


        });

    }


    /**
     * Draw axis and grid using scaling
     * @param group
     * @param xTickValues
     */
    private drawHorizontalBarGrid(group, xTickValues) {
        //TODO NEED TO CREATE GRID FOR HORIZONTAL ORIENTATION
    }


    public isBasic():boolean {
        return this.config.type == "BASIC"
    }

    public isStacked():boolean {
        return this.config.type == "STACKED"
    }

    public isNegative():boolean {
        return this.config.type == "NEGATIVE"
    }


    public getTrendCircleSize():{innerRadius:number,outerRadius:number} {
        var widget = this;
        var config = widget.config;
        var val = Math.min(widget.width, widget.height);
        return {innerRadius: val*config.trendLine.circle.innerRadius,outerRadius:val*config.trendLine.circle.outerRadius};
    }

    protected calculateTopPadding():number{
        var config = this.config;
        var model = this.model;
        var incMul = 1.1;
        if(config.axis.xTextPosition == "topLeft"){
            if(model && model.data && model.data.length > 0) {
                var textBBox = this.getTextBBox(StringUtils.formatPlainTextToSvgText(model.data[0].groupName, config.axis.x.font.size), config.axis.x.font.size, config.axis.x.font.weight, config.axis.x.font.family);
                this.xValuesPadding = this.getTextBBox("ABC",config.axis.x.font.size,config.axis.x.font.weight,config.axis.x.font.family).height;
                return textBBox.height * incMul + this.xValuesPadding;
            }
        }
        return config.padding.top;
    }
    protected calculateBottomPadding():number {
        var config = this.config;
        var model = this.model;
        var incMul = 1.1;

        if(config.axis.xTextPosition == "bottom"){
            if(model && model.data && model.data.length > 0) {
                var textBBox = this.getTextBBox(StringUtils.formatPlainTextToSvgText(model.data[0].groupName, config.axis.x.font.size), config.axis.x.font.size, config.axis.x.font.weight, config.axis.x.font.family);
                this.xValuesPadding = this.getTextBBox("ABC",config.axis.x.font.size,config.axis.x.font.weight,config.axis.x.font.family).height;
                return textBBox.height * incMul + this.xValuesPadding;
            }
        }
        if(config.bar.showCaptions){
            if(model && model.seriesNames && model.seriesNames.length > 0 ) {
                textBBox = this.getTextBBox(StringUtils.formatPlainTextToSvgText(model.seriesNames[0], config.axis.x.font.size), config.bar.captions.font.size, config.bar.captions.font.weight, config.bar.captions.font.family);
                this.captionsPadding = this.getTextBBox("ABC",config.bar.captions.font.size, config.bar.captions.font.weight, config.bar.captions.font.family).height;
                return textBBox.height * incMul + this.captionsPadding;
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
    protected getTextBBox(str:string,fontSize:string,fontWeight:string,fontFamily:string) {
        var config = this.config;
        var widget = this;
        var text = d3.select(widget.element).select("svg").append("text").html(str).attr("font-size",fontSize).attr("font-weight",fontWeight).style("font-family", fontFamily);
        var bbox = (<SVGTextElement>(text.node())).getBBox();
        text.remove()
        return bbox;
    }

    private calculateTopHorizontalPadding():number {
        if(this.isNegative()){
            return this.getTextBBox("abc","0.9em","normal",this.config.baseFont.family).height;
        }else if(this.isStacked()){
            return 0;
        }
    }
}
export = BarChart;
