///<reference path="../barchart/BarChartConfiguration.ts"/>

/**
 * Bar chart that available to draw metadata values under values in vertical mode
 * @author rlapin
 */
import StringUtils = require("../../utils/StringUtils")
import BarChart = require("../barchart/BarChart");
class BODBarChart extends BarChart {

    constructor(target) {
        super(target);
    }

    public HorizontalBasicBarChartConfiguration:BarChartConfiguration = {
        baseFont: {
            size: "3vmin",
            family: "Open Sans:300"
        },
        orientation: "HORIZONTAL",
        type: "BASIC",
        tickCount: 6,
        grid: {color: "#2d2b29", opacity: 0.3, lineWidth: 2, type: "none"},
        padding: {"left": 0, "right": 0, "top": 60, "bottom": 0},
        axis: {
            yTextPosition: "none", "fontPadding": {
                left: 200, right: 0, top: 0, bottom: 0
            }, y: {
                format: "Md",
                formatDigits: 1,
                textColor: "#95A2AB", "font": {

                    size: "0.5em"
                }
            }, x: {
                textColor: "#000", "font": {

                    size: "0.5em"
                }
            }, xTextPosition: "left"
        },
        showShadowBars: true,
        shadowBarColor: "#b8ccd7",
        showTitle: false,
        "barGroup": {"padding": 0.3},
        "bar": {
            colors: ["#356dad"],
            padding: 0,
            "showValues": true,
            "showCaptions": false,
            "values": {
                "padding": 20,
                "textColor": "#000",
                "position": "left",
                format: "d",
                formatDigits: 1,
                font: {size: "0.5em"}
            }
        },
        animation: {
            active: true
        }
    };
    public HorizontalStackedBarChartConfiguration:BarChartConfiguration = {
        baseFont: {
            size: "3vmin",
            family: "Open Sans:300"
        },
        orientation: "HORIZONTAL",
        type: "STACKED",
        tickCount: 6,
        grid: {color: "#2d2b29", opacity: 0.3, lineWidth: 2, type: "none"},
        padding: {"left": 150, "right": 0, "top": 0, "bottom": 0},
        axis: {
            yTextPosition: "none", "fontPadding": {
                left: 150, right: 0, top: 0, bottom: 0
            }, y: {
                format: "Md",
                formatDigits: 1,
                textColor: "#95A2AB", "font": {

                    size: "0.5em"
                }
            }, x: {
                textColor: "#000", "font": {

                    size: "0.7em"
                }
            }, xTextPosition: "left"
        },
        showShadowBars: true,
        shadowBarColor: "#b8ccd7",
        showTitle: false,
        "barGroup": {
            "padding": 0.3,
            "showTotalValue": true,
            "totalValue": {
                "position": "top",
                "textColor": "#000",
                font: {size: "0.7em"}
            }
        },
        "bar": {
            colors: ["#12263e", "#2a4e79", "#356dad", "#568ac0", "#68a1cb", "#8ba9d2", "#92b7d8"],
            padding: 0,
            "showValues": true,
            "showCaptions": true,
            "captions": {
                "textColor": "#000",
                font: {size: "0.5em"}
            },
            "values": {
                "padding": 20,
                "textColor": "#000",
                "position": "left",
                format: "d",
                formatDigits: 1,
                font: {size: "0.5em"}
            }
        },
        animation: {
            active: true
        }
    };
    public HorizontalNegativeBarChartConfiguration:BarChartConfiguration = {
        baseFont: {
            size: "3vmin",
            family: "Open Sans:300"
        },

        orientation: "HORIZONTAL",
        type: "NEGATIVE",
        tickCount: 6,
        grid: {color: "#2d2b29", opacity: 0.3, lineWidth: 2, type: "none"},
        padding: {"left": 250, "right": 0, "top": 30, "bottom": 30},
        axis: {
            yTextPosition: "none", "fontPadding": {
                left: 150, right: 0, top: 0, bottom: 0
            }, y: {
                format: "Md",
                formatDigits: 1,
                textColor: "#95A2AB", "font": {

                    size: "0.5em"
                }
            }, x: {
                textColor: "#000", "font": {

                    size: "0.7em"
                }
            }, xTextPosition: "left"
        },
        showShadowBars: true,
        shadowBarColor: "#b8ccd7",
        showTitle: false,
        "barGroup": {
            "padding": 0.1,
            "showTotalValue": true,
            "totalValue": {
                "position": "top",
                "textColor": "#000",
                font: {size: "0.7em"}
            }
        },
        "bar": {
            colors: ["#c73040", "#28a74a"],
            padding: 0,
            "showValues": true,
            "showCaptions": true,
            "captions": {
                "textColor": "#000",
                font: {size: "0.5em"}
            },
            "values": {
                "padding": 20,
                "textColor": "#000",
                "position": "center",
                format: "d",
                formatDigits: 1,
                font: {size: "0.5em"}
            }
        },
        animation: {
            active: true
        }
    };
    public BasicBarChartConfiguration:BarChartConfiguration = {
        baseFont: {
            size: "3vmin",
            family: "Open Sans:300"
        },
        type: "BASIC",
        tickCount: 6,
        grid: {color: "#2d2b29", opacity: 0.3, lineWidth: 2, type: "HORIZONTAL"},
        padding: {"left": 30, "right": 0, "top": 30, "bottom": 40},
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
        "barGroup": {"padding": 0.3},
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
                font: {size: "0.8em",weight:"600"}
            }
        }, animation: {
            active: true
        }

    };
    public StackedBarChartConfiguration:BarChartConfiguration = {
        baseFont: {
            size: "3vmin",
            family: "Open Sans:300"
        },
        type: "STACKED",
        tickCount: 6,
        grid: {color: "#2d2b29", opacity: 0.3, lineWidth: 2, type: "HORIZONTAL"},
        padding: {"left": 10, "right": 0, "top": 30, "bottom": 40},
        axis: {
            yTextPosition: "left", "fontPadding": {
                left: 20, right: 0, top: 0, bottom: 20
            }, y: {
                format: "d",
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
        "barGroup": {

            "padding": 0.5,
            "showTotalValue": true,
            "totalValue": {
                "position": "top",
                "textColor": "#000",
                font: {size: "0.8em",weight:"600"}
            }

        },
        "bar": {
            colors: ["#12263e", "#12263e", "#356dad", "#568ac0", "#68a1cb", "#8ba9d2", "#92b7d8"],
            "showCaptions": false,
            padding: 0.1,
            "showValues": true,
            "values": {
                "padding": 30,
                "textColor": "#000",
                "position": "center",
                format: "d",
                formatDigits: 1,
                font: {size: "0.8em",weight:"600"}
            }
        },
        animation: {
            active: true
        }
    };
    public TrendLineBarChartConfiguration:BarChartConfiguration = {
        baseFont: {
            size: "3vmin",
            family: "Open Sans:300"
        },
        type: "BASIC",
        orientation: "VERTICAL",
        tickCount: 6,
        grid: {
            color: "#2d2b29", opacity: 0.3, lineWidth: 2, type: "HORIZONTAL"
        },
        padding: {"left": 30, "right": 0, "top": 30, "bottom": 50},
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

        "barGroup": {"padding": 0.3},
        "bar": {
            colors: ["#356dad", "#356dad", "#356dad", "#356dad", "#356dad", "#356dad", "#356dad", "#356dad",
                "#356dad", "#356dad", "#356dad", "#356dad", "#356dad", "#356dad", "#356dad", "#356dad",],
            "showCaptions": false,
            padding: 0.3,
            "showValues": true,
            "captions": {
                "textColor": "#000",
                font: {size: "0.5em"}
            },
            "values": {
                "padding": 20,
                "textColor": "#000",
                "position": "top",
                format: "$M",
                formatDigits: 1,

                font: {size: "0.8em",weight:"600"}
            }
        },  //TRENDLINE SETTINGS
        showTrendLine: true,
        trendLine: {
            positive: {
                background: "#28a74a",
                opacity: 0.8,
            },
            negative: {
                background: "#c73040",
                opacity: 0.8
            },
            circle: {
                background: "#fff",
                innerRadius: 0.015,
                outerRadius: 0.03,
            },
            values: {
                format: "m$",//FP digits
                formatDigits: 1,
                lines: [{
                    font: {
                        size: "1em"
                    },
                    textColor: "#fff"
                },
                    {
                        font: {
                            size: "0.6em"
                        },
                        textColor: "#fff"
                    }
                ]
            }
        },
        animation: {
            active: true
        }

    };

    /**
     * Draw verticalbar groups and add bargroup separator if it necessary
     * @param group
     */
    public drawVerticalBarGroups(group:d3.Selection<any>):void {
        super.drawVerticalBarGroups(group);
        var xScale = this.xScale;
        var config = this.config;
        var widget = this;
        var gridHeight = this.gridHeight;
        var barGroupPadding = (widget.xScale(1) - widget.xScale(0)) * config.barGroup.padding;
        var barGroupWidth = widget.xScale(1) - widget.xScale(0) - barGroupPadding;
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

    public drawBarGroupText(barGroups, barGroupWidth) {
        super.drawBarGroupText(barGroups, barGroupWidth);
        var gridHeight = this.gridHeight;
        var widget = this;
        var config = this.config;
        if (config.axis.xTextPosition == "topLeft") {
            var offset = -gridHeight;
            var groupText = d3.select(this.element).selectAll(".barLabel");
            offset -= (<SVGTextElement>groupText.node()).getBBox().height*0.5;
            groupText.attr("text-anchor", "start").attr("transform", "translate(" + 0 + "," + offset + ")");
        }

    }

    public drawVerticalBarValuesText(group, barPadding, barWidth) {
        var widget = this;
        var model = widget.getData();
        var config = this.config;
        if (widget.isStacked() && config.bar.values.leaderLine) {
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
        if (model.metaData && Object.prototype.toString.call(model.metaData) === '[object Array]' && model.metaData.length != 0) {
            d3.select(widget.element).selectAll(".barGroup").each(function (group, index) {
                d3.select(this).selectAll(".barText").each(function (text, textIndex) {
                    d3.select(this).selectAll(".metaDataValue").data([model.metaData[index].values[textIndex]]).enter().append("tspan").attr("font-size", "0.75em").attr("font-weight", "normal").classed("metaDataValue", true).attr("x", 0).attr("dy", "1.3em").text(function (d) {
                        return "(" + StringUtils.formatString(d, "%", BarChart.FRACTIONAL_DIGITS) + ")";
                    });

                });

            });
        }
        textItem.attr("transform", function (d, i) {
            var offset = -(<SVGTextElement>textItem.node()).getBBox().height*0.5;
            if (widget.isBasic()) {
                return "translate(" + (barPadding / 2 + barWidth / 2 + (barWidth + barPadding) * i) + "," + offset + ")"
            } else {
                return "translate(" + (barPadding / 2 + barWidth / 2) + "," + offset + ")"
            }
        });
    }

    public drawDynamicsTriangles(group, barPadding, barWidth):void {


    }

    /**
     * Draw Equilateral triangle(every angle 60 degree)
     * @param barGroup selector where we draw triangle
     * @param x center point
     * @param y center point
     * @param size side length
     * @param isPositive dynamic of trend
     */
    private drawTriangle(barGroup:d3.Selection<any>, x:number, y:number, size:number, isPositive:boolean, color:string):void {
        var h = Math.sqrt(size * size - size * size * 0.25);
        var config = this.config;
        var background = color;

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
        barGroup.append("path").classed("dynamicTriangle", true).attr("d", "M" + p1[0] + " " + p1[1] + " L" + p2[0] + " " + p2[1] + " L" + p3[0] + " " + p3[1] + " Z").attr("fill", background).attr("opacity", 0);
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
        if (model.data && model.data.length != 0) {
            d3.select(widget.element).selectAll(".barGroup").each(function (group, index) {
                d3.select(this).selectAll(".barText").each(function (text, textIndex) {
                    d3.select(this).selectAll(".metaDataValue").data([model.data[index].values[textIndex]]).enter().append("tspan").attr("alignment-baseline", "middle").attr("font-weight", "normal").classed("metaDataValue", true).attr("x", 0).text(function (d) {
                        return "(" + StringUtils.formatString(d, config.bar.values.format, config.bar.values.formatDigits) + ")";
                    });
                });

            });

        }
        textItem.attr("text-anchor", function (d, i) {
                return "start";
        }).attr("transform", function (d, i) {
            // var offset = -(<SVGTextElement>textItem.node()).getBBox().height;
            var offset = 0;

                return "translate(" + (barPadding / 2 + barWidth + widget.width*0.02) + "," + offset + ")"
        });
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
        if (config.bar.values.leaderLine) {
            d3.select(widget.element).selectAll(".metaDataValue").attr("opacity", 1).transition().duration(1000 * animationMultiplier).tween("text", function (d) {
                var i = d3.interpolate(0, d);
                return function (t) {
                    d3.select(this).text( "(" + StringUtils.formatString(d, config.bar.values.format, config.bar.values.formatDigits) + ")");
                }
            });
        }
        textItem.transition().duration(1000 * animationMultiplier).attr("transform", function (d, i) {
            var translate = d3.transform(d3.select(this).attr("transform")).translate;
            var offsetY = 0;
            if(config.showTrendLine) {
                offsetY = widget.getTrendCircleSize().outerRadius;
            }
            var height = (<SVGTextElement>textItem.node()).getBBox().height;
            if (model.metaData && model.metaData.length != 0) {
                offsetY += height*0.4;
            }
            var offset = -widget.invertValue(d) - offsetY - 10;
            return "translate(" + translate[0] + "," + (offset) + ")";
        });

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
        if (config.showTrendLine && model.metaData && model.metaData.length > 0) {
            d3.select(widget.element).selectAll(".barGroup").each(function (bar, index) {

                d3.select(this).selectAll(".barG").each(function (d, i) {
                    if (i > 0) {
                        var bG = d3.select(this);
                        var text = bG.select(".barText");
                        var textBbox = (<SVGTextElement>(text.node())).getBBox();
                        var offset = widget.calculateSizeOfText(bG.selectAll(".metaDataValue").text());
                        var isPositive = (model.metaData[index].values[i] - model.metaData[index].values[i-1]>=0);
                        widget.drawTriangle(d3.select(this), 0, 0, textBbox.height * 0.3, isPositive,isPositive?config.trendLine.positive.background:config.trendLine.negative.background);
                        var translate = d3.transform(text.attr("transform")).translate;

                        bG.select(".dynamicTriangle").attr("transform", "translate(" + (translate[0] + offset*0.6) + "," + 0 + ")").transition().delay(1000*animationMultiplier).duration(500 * animationMultiplier).each("end",function(){
                          d3.select(this).attr("transform", function (d, i) {
                            var translate = d3.transform(d3.select(this).attr("transform")).translate;
                            var offset = d3.transform(text.attr("transform")).translate[1] + (<SVGTextElement>(text.node())).getBBox().height*0.32;
                            return "translate(" + translate[0] + "," + (offset) + ")";
                        }).transition().duration(animationMultiplier*1000).attr("opacity", isPositive? config.trendLine.positive.opacity : config.trendLine.negative.opacity)});
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
    private calculateSizeOfText(textval: string):number {
        var config = this.config;
        var widget = this;
        var text = d3.select(widget.element).select("svg").append("text").text(textval).attr("font-size", "0.75em").attr("font-weight", "normal").style("font-family", config.axis.y.font.family);
        var padding = (<SVGTextElement>(text.node())).getBBox().width;
        text.remove();
        return padding;
    }

    private calculateSizeOfText2(textval: string):number {
        var config = this.config;
        var widget = this;
        var text = d3.select(widget.element).select("svg").append("text").text(textval).attr("font-size", config.axis.x.font.size);
        var padding = (<SVGTextElement>(text.node())).getBBox().width;
        text.remove();
        return padding;
    }

    public verticalFadeOutValues() {
        if(this.isBasic()){
            super.verticalFadeOutValues()
        }
    }

    //Draw table with total and dynamics
    public drawHorizontalNegativeBarChartTotalTable(group, barGroupHeight, barGroupPadding):void {
        var widget = this;
        var model = widget.getData();
        var config = this.config;

        var table = group.append("text").attr("font-size", config.axis.x.font.size).classed("totalTable", true);

        var gridWidth = this.gridWidth;
        if (model.metaData2) {

            var offset = widget.calculateSizeOfText2(model.metaData2.DATA.reduce(function(a,b){
               return a[0].length>b[0].length?a:b;
            })[0]);
            var offset2 = widget.calculateSizeOfText2(StringUtils.formatString(model.metaData2.DATA.reduce(function(a,b){
                return a[1]>b[1]?a:b;
            })[1],"d",0));
            var width = offset * 1.5 + offset2;
            var padding = widget.getTextBBox("Total HC","0.9em","normal",config.baseFont.family).height;
            //Draw header
            table.attr("transform", "translate(-" + width + ",0)").attr("x", 0).attr("y", -padding/2);
            table.append("tspan").attr("text-anchor", "end").attr("font-size", "0.9em").attr("x", offset * 1.5).text("Total HC");
            table.append("tspan").attr("text-anchor", "middle").attr("font-size", "0.9em").attr("x",gridWidth+width + config.padding.right/2 + 5).text("HC Dynamic");

            //Draw values for each bars
            var data = model.metaData2.DATA;

            data.forEach(function (d, i) {
                if(i==0) {
                    table.append("tspan").attr("x", 0).attr("dy", padding).text(d[0]);
                }else{
                    table.append("tspan").attr("x", 0).attr("dy", barGroupHeight + barGroupPadding).text(d[0]);
                }
                table.append("tspan").attr("text-anchor", "end").attr("font-weight", "bold").attr("x", offset * 1.5).text(StringUtils.formatString(d[1],"d",0));
                table.append("tspan").attr("text-anchor", "end").attr("x", gridWidth+width + config.padding.right/2).text(StringUtils.formatString(d[2], "+d", 0))
                table.append("tspan").attr("x", gridWidth+width + config.padding.right/2).text("(" + StringUtils.formatString(d[3], "%", 0) + ")");
            })

        }
    }


    /**
     * Create basic bar chart
     * @param target
     * @param model? (optional)
     * @returns {BarChart}
     */
    public static createBasicBarChart(target:any):BarChart {
        var barChart = new BODBarChart(target);
        barChart.setConfig(barChart.BasicBarChartConfiguration);
        return barChart;
    }

    public static createTrendLineBarChart(target) {
        var barChart = new BODBarChart(target);
        barChart.setConfig(barChart.TrendLineBarChartConfiguration);
        return barChart;
    }

    public static createHorizontalBasicBarChart(target):BarChart {
        var barChart = new BODBarChart(target);
        barChart.setConfig(barChart.HorizontalBasicBarChartConfiguration);
        return barChart;
    }

    public static createHorizontalStackedBarChart(target):BarChart {
        var barChart = new BODBarChart(target);
        barChart.setConfig(barChart.HorizontalStackedBarChartConfiguration);
        return barChart;
    }

    public static createVerticalStackedBarChart(target):BarChart {
        var barChart = new BODBarChart(target);
        barChart.setConfig(barChart.StackedBarChartConfiguration);


        return barChart;
    }

    public static createHorizontalNegativeBarChart(target):BarChart {
        var barChart = new BODBarChart(target);
        barChart.setConfig(barChart.HorizontalNegativeBarChartConfiguration);
        return barChart;
    }
}
export = BODBarChart;
