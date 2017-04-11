///<reference path="../barchart/BarChartConfiguration.ts"/>

/**
 * Bar chart that available to draw metadata values under values in vertical mode
 * @author rlapin
 */
import StringUtils = require("../../utils/StringUtils")
import BarChart = require("../barchart/BarChart");
class BIBarChart extends BarChart {
    /**
     * Scale that used for drawing lines
     */
    private yScale2:any;
    /**
     * Chart padding used for second axis captions
     */
    private rightPadding;
    private legendHeight;
    private legendWidth;
    /**
     * min barchart value
     */
    private yMin;

    constructor(target) {
        super(target);
    }

    public CombinedLineBarChartConfiguration:BIBarChartConfiguration = {
        baseFont: {
            size: "3vmin",
            family: "Open Sans:300"
        },
        type: "BASIC",
        tickCount: 6,
        grid: {color: "#2d2b29", opacity: 0.3, lineWidth: 2, type: "HORIZONTAL"},
        padding: {"left": 30, "right": 0, "top": 10, "bottom": 40},
        axis: {
            yTextPosition: "left", "fontPadding": {
                left: 20, right: 0, top: 0, bottom: 20
            }, y: {
                format: "Md",
                formatDigits: 1,
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
        orientation: "VERTICAL",
        "barGroup": {"padding": 0.1},
        "bar": {
            colors: ["#356dad", "#68a1cb"],
            padding: 0,
            "showCaptions": false,
            "showValues": true,
            "captions": {
                "textColor": "#000",
                font: {size: "0.5em"}
            },
            "values": {
                "padding": 10,
                "textColor": "#000",
                "position": "top",
                format: "$M",
                formatDigits: 1,
                font: {size: "0.8em", weight: "600"}
            }
        }, animation: {
            active: true
        },
        showLegend: true,
        showShadowBars: false,
        shadowBarColors: ["#c4dae8", "#cbdeed"],
        lineChart: {
            padding: 0.1,
            colors: ["#922a00", "#eb571c"],
            figures: [{type: "circle", size: 0.015, color: "#922a00"}, {type: "circle", size: 0.015, color: "#eb571c"}],
            values: {
                font: {size: "0.6em"},
                format: "%",
                formatDigits: 1
            },
            relSize: 0.4

        },
        legend: {
            font: {size: "0.6em"},
            padding: 0.04,
            position: 'center'
        }

    };

    /**
     * Draw verticalbar groups and add bargroup separator if it necessary
     * @param group
     */
    public drawVerticalBarGroups(group:d3.Selection<any>):void {

        var xScale = this.xScale;
        var config = this.config;
        var widget = this;
        var gridHeight = this.gridHeight;
        var gridWidth = this.gridWidth;
        var model = this.getData();
        var barGroupPadding = gridWidth / model.data.length * config.barGroup.padding;
        var barGroupWidth = gridWidth / model.data.length - barGroupPadding;

        if (config.showShadowBars) {
            var barGroupCount = model.data.length;
            var gridWidth = this.gridWidth;
            var shadowBarWidth = gridWidth / barGroupCount;
            for (var i = 0; i < barGroupCount; i++) {
                group.append("rect").attr("x", shadowBarWidth * i).attr('y', 0).attr("width", shadowBarWidth).attr("height", gridHeight).attr('fill', config.shadowBarColors[i % config.shadowBarColors.length]);
            }

        }
        if (model.metaData && model.metaData.lines && model.metaData.lines.length != 0) {
            this.drawLines(group, barGroupWidth, barGroupPadding);
        }

        super.drawVerticalBarGroups(group);
        var barGroups = d3.select(widget.element).selectAll(".barGroup").attr("transform", function () {
            var translate = d3.transform(d3.select(this).attr("transform")).translate;
            return "translate(" + translate[0] + "," + widget.yScale(0) + ")"

        });
        barGroups.select(".barLabel").attr("transform", function () {
            var translate = d3.transform(d3.select(this).attr("transform")).translate;
            return "translate(" + translate[0] + "," + (translate[1] - widget.yScale(0) + widget.gridHeight) + ")"
        });
        barGroups.select(".captionDynamicTriangle").attr("opacity", 1).attr("transform", function () {
            var label = d3.select(this.parentNode).select(".barLabel");
            var tspan = label.selectAll("tspan");
            var spanWidth = widget.getTextBBox((<any>tspan[0][tspan[0].length - 1]).innerHTML, config.bar.values.font.size, config.bar.values.font.weight, config.bar.values.font.family).width;
            var translate = d3.transform(label.attr("transform")).translate;
            var bbox = (<any>tspan.node()).getBBox();

            return "translate(" + (translate[0] + spanWidth * 0.5) + "," + (translate[1] + bbox.height / 2) + ")";
        }).attr("opacity", 1);
        if (config.barGroup.showSeparator) {
            var barGroupSelection = group.selectAll(".barGroup");
            barGroupSelection.each(function (data, i) {
                if (i < barGroupSelection.size() - 1) {
                    var curBarGroup = d3.select(this);
                    curBarGroup.append("line").attr("x1", 0)
                        .attr("y1", config.padding.bottom)
                        .attr("x2", 0)
                        .attr("y2", -gridHeight - config.padding.top)
                        .attr("stroke", config.barGroup.separator.stroke)
                        .attr("stroke-width", config.barGroup.separator.strokeWidth).attr("transform", "translate(" + (barGroupWidth + barGroupPadding / 2) + "," + 0 + ")");
                }
            });

        }
    }

    /**
     * Render legend(see {@link BarChartConfiguration} <code> .legend.</code> settings)
     * @param svg - svg element, where you draw barchart
     */
    public renderLegend(svg) {
        var config = this.config;
        var model = this.getData();
        var widget = this;
        var legendGroup = svg.append("g").attr("opacity", 0);
        legendGroup.classed("legend", true);
        var legendModel = [];

        if (model) {
            var lineNames = [];
            if (model.metaData.lines && model.metaData.lines.length != 0) {
                lineNames = model.metaData.lines.map(d=>d.name);
            } else {

            }
            var colorsCount = config.bar.colors.length;
            legendModel = model.seriesNames.concat(lineNames);
            var legendItems = legendGroup.selectAll(".legendItem").data(legendModel).enter().append("g").classed("legendItem", true);

            var legendText = legendItems.append("text").attr("text-anchor", "start").attr("alignment-baseline", "middle");
            if (config.legend.font) {
                legendText.style("font-size", config.legend.font.size).style("font-weight", config.legend.font.weight).style("font-family", config.legend.font.family);
            }
            legendText.text((d)=>d);
            var size = legendText.node().getBBox().height;
            legendItems.each(function (d, i) {
                var legendItem = d3.select(this);
                if (i < colorsCount) {
                    legendItem.insert("rect", ":first-child").attr("x", size * 0.1).attr("y", size * 0.03).attr("width", size * 0.8).attr("height", size * 0.8).attr("fill", (d, j)=>i < colorsCount ? config.bar.colors[i % colorsCount] : config.lineChart.colors[i % colorsCount]);
                } else {
                    var figure = config.lineChart.figures[i % colorsCount].type;
                    if (figure == "circle") {
                        legendItem.insert("circle", ":first-child").attr("cx", size * 0.5).attr("cy", size * 0.4).attr("r", size * 0.3).attr("fill", (d, j)=>i < colorsCount ? config.bar.colors[i % colorsCount] : config.lineChart.colors[i % colorsCount]);
                        legendItem.insert("line", ":first-child").attr("x1", 0).attr("y1", size * 0.4).attr("x2", size).attr("y2", size * 0.4).attr("stroke-width", size * 0.1).attr("stroke", (d, j)=>i < colorsCount ? config.bar.colors[i % colorsCount] : config.lineChart.colors[i % colorsCount]);
                    }

                }
            });
            legendText.attr("transform", "translate(" + size * 1.2 + "," + size / 2 + ")");
            //contains width of each group
            var legendWidth = 0;
            var padding = Math.min(widget.width, widget.height) * config.legend.padding;
            legendItems.each(function () {
                var item = d3.select(this);
                item.attr('transform', "translate(" + legendWidth + ",0)");
                legendWidth += (<any>item.node()).getBBox().width + padding;
            })
        }
        this.legendWidth = legendWidth;
        this.legendHeight = size;


    }

    public drawBarGroupText(barGroups, barGroupWidth) {
        super.drawBarGroupText(barGroups, barGroupWidth);
        var gridHeight = this.gridHeight;
        var widget = this;
        var config = this.config;
        var groupText;
        if (config.axis.xTextPosition == "topLeft") {
            var offset = -gridHeight;
            groupText = d3.select(this.element).selectAll(".barLabel");
            offset -= (<SVGTextElement>groupText.node()).getBBox().height * 0.5;
            groupText.attr("text-anchor", "start").attr("transform", "translate(" + 0 + "," + offset + ")");
        }
        groupText = barGroups.selectAll(".barLabel").each(function () {
            var label = d3.select(this);
            var barGroup = d3.select(this.parentNode);
            var text = label.html();
            if (text.indexOf("{{") != -1) {
                var triangle = text.slice(text.indexOf("{{") + 2, text.indexOf("}}"));
                label.html(text.replace("{{" + triangle + "}}", ""));
                var size = (<SVGTextElement>label.node()).getBBox().height * 0.35;
                if (triangle == "up" || triangle == "down") {
                    var translate = d3.transform(label.attr("transform")).translate;

                    widget.drawTriangle(barGroup, size / 2, -size * 0.42, size, triangle == "up", triangle == "up" ? "#28a74a" : "#c73040", "captionDynamicTriangle");

                }
            }

        });


    }

    public drawVerticalBarValuesText(group, barPadding, barWidth) {
        var widget = this;
        var model = widget.getData();
        if (widget.isStacked() && (model.metaData && model.metaData.length != 0)) {
            this.drawOnlyMetaData(group, barPadding, barWidth);
        } else {
            this.drawValuesAndMetaData(group, barPadding, barWidth);
        }


    }

    private drawValuesAndMetaData(group:any, barPadding:number, barWidth:number):void {
        var config = this.config;
        var widget = this;
        var model = widget.getData();
        var textItem = group.append("text").attr("opacity", 0).classed("barText", true).attr("text-anchor", "middle");
        textItem.style("fill", config.bar.values.textColor).attr("opacity", 0);
        if (config.bar.values.font) {
            textItem.style("font-size", config.bar.values.font.size).style("font-weight", config.bar.values.font.weight).style("font-family", config.bar.values.font.family);
        }
        var valueText = textItem.append("tspan").classed("dataValue", true);
        valueText.text(function (d) {
            return StringUtils.formatString(d, config.bar.values.format, config.bar.values.formatDigits);
        }).attr("x", 0);
        textItem.attr("transform", function (d, i) {
            var offset = -(<SVGTextElement>textItem.node()).getBBox().height * 0.5;
            if (widget.isBasic()) {
                if (d < 0) {
                    offset *= -1;
                }
                return "translate(" + (barPadding / 2 + barWidth / 2 + (barWidth + barPadding) * i) + "," + offset + ")"
            } else {
                return "translate(" + (barPadding / 2 + barWidth / 2) + "," + offset + ")"
            }
        });
    }

    public drawDynamicsTriangles(group, barPadding, barWidth):void {


    }

    public renderCombineLineChart():void {
        var config:BIBarChartConfiguration = this.config;

        var model = this.getData();

        var element = this.element;
        var _this = this;
        var width = this.width;
        var height = this.height;

        var xGridPadding = config.padding.left;
        var yGridPadding = this.calculateTopPadding();

        var gridHeight = this.height;
        if (!this.isEmpty && config.showLegend) {
            yGridPadding += this.legendHeight * 1.2;
        }
        gridHeight -= this.calculateBottomPadding();
        if (config.axis.yTextPosition == "left") {
            xGridPadding += config.axis.fontPadding.left;
        }
        gridHeight -= yGridPadding;

        var gridWidth = this.width - config.padding.right - xGridPadding;
        if (model.metaData.lines && model.metaData.lines.length != 0) {
            var linesData:Array<any> = model.metaData.lines;

            var yMax2 = d3.max(linesData, function (d) {

                return d3.max((<any>d).values, function (g) {
                    return <any>g;
                });

            });
            yMax2 *= 1.5;
            var yMin2 = 0;
            var step = (yMax2 - yMin2) / (config.tickCount - 1);
            yMax2 = step * (config.tickCount - 1) + yMin2;
            var yTickValues2 = [];
            for (var i = 0; i < config.tickCount; i++) {
                yTickValues2.push(i * step + yMin2);
            }
            var yMin2 = d3.min(linesData, function (d) {
                return d3.min((<any>d).values, function (g) {
                    return <any>g;
                })
            });
            yMin2 = Math.min(yMin2, 0);
            this.rightPadding = 0;
            //this.calculateRightPadding(yMax2) * 3.2;
        } else {
            this.rightPadding = 0;
            config.lineChart.relSize = 0;

        }

        var yMin = 0;
        var yMax = 0;
        if (this.isEmpty) {
            this.xScale = d3.scale.linear().domain([0, 1]).range([0, gridWidth]);
            yMax = 100;
        } else {

            yMax = d3.max(model.data, function (d) {
                return d3.max(d.values, function (d) {
                    return d;
                });
            });
            yMin = d3.min(model.data, function (d) {
                return d3.min(d.values, function (d) {
                    return d;
                });
            });
            yMin = Math.min(yMin, 0);
            xGridPadding = this.calculateLeftPadding(yMax) * 1.2;
            gridWidth = this.width - config.padding.right - xGridPadding;
            this.xScale = d3.scale.linear().domain([0, model.data.length]).range([0, gridWidth]);
        }
        var range2 = ( Math.abs(yMax2) + Math.abs(yMin2));
        yMin2 -= range2 * 0.3;
        yMax2 += range2 * 0.3;
        if (config.bar.showValues && config.bar.values.position == "top") {
            yMax += ((yMax * (config.bar.values.padding )) / gridHeight);
        }


        var range = ( Math.abs(yMax) + Math.abs(yMin));

        var step = 0;
        var yTickValues = [];
        yMax += range * 0.3;
        if (yMin < 0) {

            yMin -= range * 0.5;
            range = ( Math.abs(yMax) + Math.abs(yMin));
            step = Math.ceil(Math.abs(yMin) / Math.floor(Math.abs(yMin) / range * (config.tickCount)));

            for (var i = 0; i < config.tickCount; i++) {
                yTickValues.push(i * step + yMin);
            }
            yMax = yTickValues[yTickValues.length - 1];
        } else {
            step = (yMax - yMin) / (config.tickCount - 1);
            yMax = step * (config.tickCount - 1) + yMin;

            for (var i = 0; i < config.tickCount; i++) {
                yTickValues.push(i * step + yMin);
            }
        }

        var relSize = config.lineChart.relSize;
        this.yScale = d3.scale.linear().domain([yMin, yMax]).range([gridHeight, gridHeight * relSize + gridHeight * config.lineChart.padding]);


        gridWidth -= this.rightPadding;

        this.yScale2 = d3.scale.linear().domain([yMin2, yMax2]).range([gridHeight * relSize, 0]);
        var svg = d3.select(element).select("svg");
        var titlePadding = 50;
        if (!this.isEmpty && config.showTitle) {
            svg.append("text").text(model.title).attr("x", titlePadding).attr("y", yGridPadding - parseFloat(config.baseFont.size) * Math.min(width, height) / 100).style("text-align", "middle");
        }

        var group = svg.append("g").classed("chartRegion", true);

        group.attr("transform", "translate(" + (xGridPadding) + "," + (yGridPadding) + ")")
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.yMin = yMin;
        this.drawVerticalBarGrid(group, yTickValues);
        if (model.metaData.lines && model.metaData.lines.length != 0) {
            this.drawVerticalBarGridForLines(group, yTickValues2);
        }
        if (!this.isEmpty) {
            this.drawVerticalBarGroups(group);
        }
    }

    private calculateRightPadding(yMax:number):number {
        return this.calculateLeftPadding(yMax);
    }

    public renderVertical() {
        var config = this.config;
        if (!this.isEmpty) {
            this.renderCombineLineChart();
        } else {
            super.renderVertical();
        }
    }

    /**
     * Draw Equilateral triangle(every angle 60 degree)
     * @param barGroup selector where we draw triangle
     * @param x center point
     * @param y center point
     * @param size side length
     * @param isPositive dynamic of trend
     */
    private drawTriangle(barGroup:d3.Selection<any>, x:number, y:number, size:number, isPositive:boolean, color:string, className:string):void {
        var h = Math.sqrt(size * size - size * size * 0.25);
        var config = this.config;
        var background = color;
        if (!className) {
            className = "dynamicTriangle";
        }
        var p1 = [0, 0];
        var p2 = [0, 0];
        var p3 = [0, 0];
        if (isPositive) {
            p1 = [x - size / 2, y + h / 2];
            p2 = [x, y - h / 2];
            p3 = [x + size / 2, y + h / 2];
        } else {
            p1 = [x - size / 2, y - h / 2];
            p2 = [x, y + h / 2];
            p3 = [x + size / 2, y - h / 2];
        }
        barGroup.append("path").classed(className, true).attr("d", "M" + p1[0] + " " + p1[1] + " L" + p2[0] + " " + p2[1] + " L" + p3[0] + " " + p3[1] + " Z").attr("fill", background).attr("opacity", 0);
    }


    private drawOnlyMetaData(group:any, barPadding:number, barWidth:number):void {
        var config = this.config;
        var widget = this;

        var gridHeight = widget.gridHeight;
        var model = widget.getData();
        var textItem = group.append("text").attr("opacity", 0).classed("barText", true).attr("text-anchor", "start").attr("alignment-baseline", "middle");
        textItem.style("fill", config.bar.values.textColor).attr("opacity", 0);
        if (config.bar.values.font) {
            textItem.style("font-size", config.bar.values.font.size).style("font-weight", config.bar.values.font.weight).style("font-family", config.bar.values.font.family);
        }
        if (model.metaData && model.metaData.length != 0) {
            d3.select(widget.element).selectAll(".barGroup").each(function (group, index) {
                d3.select(this).selectAll(".barText").each(function (text, textIndex) {
                    d3.select(this).selectAll(".metaDataValue").data([model.metaData[index].values[textIndex]]).enter().append("tspan").attr("alignment-baseline", "middle").attr("font-weight", "normal").classed("metaDataValue", true).attr("x", 0).text(function (d) {
                        return "(" + StringUtils.formatString(d, "d%", BarChart.FRACTIONAL_DIGITS) + ")";
                    });
                });

            });

        }
        textItem.attr("text-anchor", function (d, i) {
            return "start";
        }).attr("transform", function (d, i) {
            // var offset = -(<SVGTextElement>textItem.node()).getBBox().height;
            var offset = 0;

            return "translate(" + (barPadding / 2 + barWidth + widget.width * 0.02) + "," + offset + ")"
        });
    }


    private drawVerticalBarGridForLines(group:any, yTickValues:any):void {
        var config = this.config;
        var model = this.getData();
        var xScale = this.xScale;
        var yScale = this.yScale2;
        var gridWidth = this.gridWidth;
        var gridHeight = this.gridHeight;
        var lineGridType = "none"
        if (lineGridType != "none") {//config.grid.type != "none"
            if (config.grid.type == "BOTH" || config.grid.type == "HORIZONTAL") {
                var yAxis = d3.svg.axis().scale(yScale).orient(config.axis.yTextPosition).tickSize(gridWidth).tickValues(yTickValues)
                    .tickFormat(function (value) {
                        return StringUtils.formatString(value, "%d", 0);
                    });
                var yGrid = group.append("g").attr("class", "y axis2");
                yGrid.call(yAxis);
                yGrid.select("path").remove();

                yGrid.selectAll(".tick line").attr("stroke", config.grid.color).attr("opacity", function (d, i) {
                    if (i == 0) {
                        return 0;
                    } else {
                        return config.grid.opacity
                    }
                }).attr("stroke-width", config.grid.lineWidth).attr("font-color:green");
            }

        }
        if (config.axis.yTextPosition != "none") {

            group.selectAll(".y.axis2").attr("text-anchor", "middle").attr("transform", "translate(" + (gridWidth) + ",0)");
            group.selectAll(".y.axis2 .tick text").attr("text-anchor", "end").attr("transform", "translate(" + (gridWidth + this.rightPadding) + ",0)");

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

    public verticalFadeInValues() {
        var widget = this;

        var config = this.config;
        var model = widget.getData();
        var animationMultiplier = 1;
        if (config.animation && !config.animation.active) {
            animationMultiplier = 0;
        }
        var textItem = d3.select(this.element).selectAll(".barText").attr("opacity", 1);
        textItem.select(".dataValue").attr("opacity", 1).transition().duration(1000 * animationMultiplier).tween("text", function (d) {
            var i = d3.interpolate(0, d);
            return function (t) {
                d3.select(this).text(StringUtils.formatString(i(t), config.bar.values.format, config.bar.values.formatDigits));
            }
        });
        if (model.metaData && model.metaData.length != 0) {
            d3.select(widget.element).selectAll(".metaDataValue").attr("opacity", 1).transition().duration(1000 * animationMultiplier).tween("text", function (d) {
                var i = d3.interpolate(0, d);
                return function (t) {
                    d3.select(this).text("(" + StringUtils.formatString(i(t), "%", BarChart.FRACTIONAL_DIGITS) + ")");
                }
            });
        }
        textItem.transition().duration(1000 * animationMultiplier).attr("transform", function (d, i) {
            var translate = d3.transform(d3.select(this).attr("transform")).translate;
            var offsetY = 0;
            if (config.showTrendLine) {
                offsetY = widget.getTrendCircleSize().outerRadius;
            }
            var height = (<SVGTextElement>textItem.node()).getBBox().height;
            if (model.metaData && model.metaData.length != 0) {
                offsetY += height * 0.4;
            }
            var offset = -widget.invertValue(d)
            if (d < 0) {
                offset -= -offsetY - 10;
            } else {
                offset += -offsetY - 10;
            }
            return "translate(" + translate[0] + "," + (offset) + ")";
        });

    }

    public invertValue(d) {
        return this.yScale(0) - this.yScale(d);
    }

    public verticalFadeIn() {
        super.verticalFadeIn();

        var widget = this;
        var config = this.config;
        var model = widget.getData();
        var animationMultiplier = 1;
        if (config.animation && !config.animation.active) {
            animationMultiplier = 0;
        }
        var sizeMultiplier = Math.min(widget.width, widget.height);
        if (config.showLegend) {

            d3.select(widget.element).select(".legend").attr("transform", function () {
                if (config.legend.position) {
                    var translateX = 0;
                    var translateY = 0;
                    if (config.legend.position.indexOf('center') != -1) {
                        translateX = (widget.width - widget.legendWidth) / 2;
                    } else {
                        translateX = (widget.width - widget.legendWidth);
                    }
                    if (config.legend.position.indexOf("bottom") != -1) {
                        translateY = widget.height - widget.legendHeight;
                    } else {
                        translateY = widget.legendHeight * 0.5
                    }
                    return "translate(" + translateX + ", " + translateY + ")"
                }
            }).transition().duration(1000 * animationMultiplier).attr("opacity", 1);

        }
        if (model.metaData.lines && model.metaData.lines.length != 0) {

            d3.select(widget.element).selectAll(".line").select(".lineStroke").attr("stroke-dasharray", function () {
                var totalLength = (<any>(d3.select(this).node())).getTotalLength();
                return totalLength + " " + totalLength;
            }).attr("stroke-dashoffset", function () {
                return (<any>(d3.select(this).node())).getTotalLength()
            }).attr("opacity", 1).transition().delay(1000 * animationMultiplier).duration(1500 * animationMultiplier).ease("linear").attr("stroke-dashoffset", 0);
            d3.select(widget.element).selectAll(".figure").attr("r", 0).attr("opacity", 1).transition().ease("bounce").delay((d, i)=>1500 * animationMultiplier).duration(2000 * animationMultiplier).attr("r", (d)=>d.figure.size * sizeMultiplier);
            d3.select(widget.element).selectAll(".value").attr("font-size", 0).attr("opacity", 1).transition().ease("linear").delay((d, i)=>2000 * animationMultiplier).duration(500 * animationMultiplier).attr("font-size", config.lineChart.values.font.size);
        }
        if (config.showTrendLine && model.metaData && model.metaData.length > 0) {
            d3.select(widget.element).selectAll(".barGroup").each(function (bar, index) {

                d3.select(this).selectAll(".barG").each(function (d, i) {
                    if (i > 0) {

                        var bG = d3.select(this);
                        var text = bG.select(".barText");
                        var textBbox = (<SVGTextElement>(text.node())).getBBox();
                        var offset = widget.calculateSizeOfText(bG.selectAll(".metaDataValue").text());
                        var isPositive = (model.metaData[index].values[i] - model.metaData[index].values[i - 1] >= 0);
                        widget.drawTriangle(d3.select(this), 0, 0, textBbox.height * 0.3, isPositive, isPositive ? config.trendLine.positive.background : config.trendLine.negative.background, "dynamicTriangle");
                        var translate = d3.transform(text.attr("transform")).translate;

                        bG.select(".dynamicTriangle").attr("transform", "translate(" + (translate[0] + offset / 2) + "," + 0 + ")").attr("transform", function (d, i) {
                            var translate = d3.transform(d3.select(this).attr("transform")).translate;
                            var offset = (-widget.invertValue(d) - (<SVGTextElement>text.node()).getBBox().height * 0.62 + textBbox.height * 0.15);

                            return "translate(" + translate[0] + "," + (offset) + ")";
                        }).transition().delay(1000 * animationMultiplier).duration(500 * animationMultiplier).attr("opacity", isPositive ? config.trendLine.positive.opacity : config.trendLine.negative.opacity);
                    }
                });
            });
        }
    }

    /**
     * Calculate size of text
     * @param textval
     * @returns {number}
     */
    private calculateSizeOfText(textval:string):number {
        var config = this.config;
        var widget = this;
        var text = d3.select(widget.element).select("svg").append("text").text(textval).attr("font-size", "0.75em").attr("font-weight", "normal").style("font-family", config.axis.y.font.family);
        var padding = (<SVGTextElement>(text.node())).getBBox().width;
        text.remove();
        return padding;
    }

    private calculateSizeOfText2(textval:string):number {
        var config = this.config;
        var widget = this;
        var text = d3.select(widget.element).select("svg").append("text").text(textval).attr("font-size", config.axis.x.font.size);
        var padding = (<SVGTextElement>(text.node())).getBBox().width;
        text.remove();
        return padding;
    }

    public verticalFadeOutValues() {

    }

    //Draw table with total and dynamics
    public drawHorizontalNegativeBarChartTotalTable(group, barGroupHeight, barGroupPadding):void {
        var widget = this;
        var model = widget.getData();
        var config = this.config;

        var table = group.append("text").attr("font-size", config.axis.x.font.size).classed("totalTable", true);

        var gridWidth = this.gridWidth;
        if (model.metaData2) {

            var offset = widget.calculateSizeOfText2(model.metaData2.DATA.reduce(function (a, b) {
                return a[0].length > b[0].length ? a : b;
            })[0]);
            var offset2 = widget.calculateSizeOfText2(StringUtils.formatString(model.metaData2.DATA.reduce(function (a, b) {
                return a[1] > b[1] ? a : b;
            })[1], "d", 0));
            var width = offset * 1.5 + offset2;
            var padding = widget.getTextBBox("Total HC", "0.9em", "normal", config.baseFont.family).height;
            //Draw header
            table.attr("transform", "translate(-" + width + ",0)").attr("x", 0).attr("y", -padding / 2);
            table.append("tspan").attr("text-anchor", "end").attr("font-size", "0.9em").attr("x", offset * 1.5).text("Total HC");
            table.append("tspan").attr("text-anchor", "middle").attr("font-size", "0.9em").attr("x", gridWidth + width + config.padding.right / 2 + 5).text("HC Dynamic");

            //Draw values for each bars
            var data = model.metaData2.DATA;

            data.forEach(function (d, i) {
                if (i == 0) {
                    table.append("tspan").attr("x", 0).attr("dy", padding).text(d[0]);
                } else {
                    table.append("tspan").attr("x", 0).attr("dy", barGroupHeight + barGroupPadding).text(d[0]);
                }
                table.append("tspan").attr("text-anchor", "end").attr("font-weight", "bold").attr("x", offset * 1.5).text(StringUtils.formatString(d[1], "d", 0));
                table.append("tspan").attr("text-anchor", "end").attr("x", gridWidth + width + config.padding.right / 2).text(StringUtils.formatString(d[2], "+d", 0))
                table.append("tspan").attr("x", gridWidth + width + config.padding.right / 2).text("(" + StringUtils.formatString(d[3], "%", 0) + ")");
            })

        }
    }


    /**
     * Create combinelinebar chart
     * @param target
     * @param model? (optional)
     * @returns {BarChart}
     */
    public static createCombineLineBarChart(target:any):BarChart {
        var barChart = new BIBarChart(target);
        barChart.setConfig(barChart.CombinedLineBarChartConfiguration);
        return barChart;
    }

    /**
     *
     * @param group element where we append our lines
     * @param barGroupWidth
     * @param barGroupPadding
     */
    private drawLines(group:d3.Selection<any>, barGroupWidth:number, barGroupPadding:number):void {
        var model = this.getData();
        var config = this.config;
        var yScale2 = this.yScale2;
        var widget = this;

        var lineLayout = d3.svg.line().x(function (d:any) {
            return d.x
        }).y(function (d:any) {
            return d.y;
        });
        var lines:any = [];
        model.metaData.lines.forEach(function (d:any, j) {
            var line:any = [];
            d.values.forEach(function (val, i) {
                var textAbove = false;
                if (model.metaData.lines.length == 1) {
                    textAbove = true;
                } else {
                    if (j == 0) {
                        textAbove = model.metaData.lines[0].values[i] >= model.metaData.lines[1].values[i];
                    } else {
                        textAbove = model.metaData.lines[1].values[i] >= model.metaData.lines[0].values[i];
                    }
                }
                line.push({
                    x: (barGroupWidth + barGroupPadding) * (i + 0.5),
                    y: (yScale2(val)),
                    color: config.lineChart.colors[j],
                    figure: config.lineChart.figures[j],
                    textAbove: textAbove,
                    value: val
                });
            });
            lines.push(line);
        });
        var sizeMultiplier = Math.min(widget.width, widget.height);
        var lineGroup = group.selectAll(".line").data(lines).enter().append("g").classed("line", true);
        lineGroup.append("path").attr("d", lineLayout).attr("stroke", function (d, i) {
            return config.lineChart.colors[i]
        }).attr("fill", "transparent").classed("lineStroke", true).attr("stroke-width", 2).attr("opacity", 0);
        ;
        lineGroup.selectAll(".figure").data((d)=>d).enter().append("circle").attr("cx", function (d) {
            return (<any>d).x
        }).attr("cy", function (d) {
            return (<any>d).y;
        }).attr("r", (d, i)=>d.figure.size * sizeMultiplier * 2).classed("figure", true).attr("fill", (d, i)=>d.figure.color).attr("opacity", 0);
        var textHeight = this.getTextBBox("123", config.lineChart.values.font.size, config.lineChart.values.font.weight, config.lineChart.values.font.family).height * 1.2;
        var lineText = lineGroup.selectAll(".value").data((d)=>d).enter().append("text");
        lineText.attr("text-anchor", "middle").attr("alignment-baseline", "middle").attr("x", d=>(<any>d).x).attr("y", d=>d.y + (d.textAbove ? -1 : 1) * textHeight).text(function (d) {
            return StringUtils.formatString(d.value, config.lineChart.values.format, config.lineChart.values.formatDigits);
        }).classed("value", true).attr("fill", d=>d.color);
        if (config.lineChart.values.font) {
            lineText.attr("font-size", config.lineChart.values.font.size).attr("opacity", 0).attr("font-family", config.lineChart.values.font.family).attr("font-weight", config.lineChart.values.font.weight);
        }
        //check whether text will be place above or below

    }
}
export = BIBarChart;
