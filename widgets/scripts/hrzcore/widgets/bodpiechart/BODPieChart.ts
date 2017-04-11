///<reference path="../piechart/PieChartConfiguration.ts"/>
///<reference path="BODPieChartConfiguration.ts"/>

import ObjectUtils = require("../../utils/ObjectUtils");
import StringUtils = require("../../utils/StringUtils");
import BBoxUtils = require("../../utils/BBoxUtils");
import ArrayUtils = require("../../utils/ArrayUtils");
import Legend = require("../legend/Legend");

import PieChart = require("../piechart/PieChart");
class BODPieChart extends PieChart {

    public static  create(trg):BODPieChart {
        return new BODPieChart(trg);
    }

    constructor(target) {
        super(target);
    }

    protected groupNames:d3.Selection<any>;
    protected configuration:BODPieChartConfiguration;


    public setConfig(config:BODPieChartConfiguration):void {
        super.setConfig({});
        this.configuration = ObjectUtils.deepExtend(this.configuration, BODPieChart.defaultBODConfiguration, config);
    }


    /**
     * defining radius of segent according to topgroup index
     * @param pieRadius
     * @param segmentNumber
     * @param segmentCount
     * @returns radius of particular segment
     */
    protected  getSegmentOuterRadius(pieRadius, segmentNumber, segmentCount) {
        var cfg = this.configuration;
        var tg = cfg.topGrouping;

        return tg && tg.show ? pieRadius * Math.pow(cfg.topGrouping.radiusStep,getTopGroupIndex(segmentNumber, segmentCount)) : pieRadius;

        function getTopGroupIndex(valueIndex,count) {

            var levels = cfg.topGrouping.topCounts;
            //the radius index of sector Others
            if (valueIndex + 1 == count) return valueIndex+1 <= levels[levels.length-1].valueCount ? levels.length : levels.length +1;

            return valueIndex+1 > levels[levels.length-1].valueCount ? levels.length : ArrayUtils.findIndex(levels, function (d) {
                return d.valueCount > valueIndex;
            });
        }


    }


    protected afterPieRender(x,y,width,height,cx,cy) {
        var cfg = this.configuration;
        if (cfg.groupTitle && cfg.groupTitle.show) {
            this.renderGroupNames(width,height,cx,cy,this.maxRadius);

        }

    }


    /**
     * group names rendering
     * @param w
     * @param h
     * @param cx
     * @param cy
     * @param inputRadius
     */
    private renderGroupNames(w, h, cx, cy, inputRadius):void{
        var cfg = this.configuration,
            model = this.model,
            svg = this.rootSVG,
            gt = cfg.groupTitle,
            bb,bg,groupTexts
            ;
        var groupNames = this.groupNames = svg.append("g").style("visibility", "hidden");

            bg = prepareBG();
            groupTexts = prepareText();
            bb = (<SVGGElement>(groupNames.node())).getBBox();
            arrangeTextForFlags();
            positionBG();



        function arrangeTextForFlags() {
            var height = groupTexts.node().getBBox().height*0.8;
            groupTexts.attr("transform","translate("+(cx + gt.marginLeft)+","+height+")")
                .attr("text-anchor", "start").attr("alignment-baseline","center").attr("dy","0.35em").attr("y", function (d, i) {
                    return cy - inputRadius*Math.pow(cfg.radiusDelta,i);
                });
        }

        function prepareText() {
            return groupNames.selectAll("text").data(model.data).enter()
                .append("text").style(ObjectUtils.convertObjectKeysToKebabCase(gt.font)).style("z-index", 10)
                .html(function (d) {
                    return StringUtils.formatPlainTextToSvgText(d.groupName,gt.font.fontSize,gt.lineSpacing);
                })
        }

        function prepareBG(){
            return groupNames.selectAll("rect").data(model.data).enter()
                .append("rect").attr(ObjectUtils.convertObjectKeysToKebabCase(gt.background));
        }

        function positionBG(){
            bg.attr({
                x: cx,
                width: bb.width+13,
                height: parseFloat(cfg.groupTitle.font.fontSize)*parseFloat(cfg.baseFont.fontSize)*Math.min(window.innerHeight,window.innerWidth)*0.015,
            }).attr("y", function (d, i) {
                return cy - inputRadius*Math.pow(cfg.radiusDelta,i);
            });
        }

    }



    protected fadeInAdditional() {

        this.groupNames && this.groupNames.style("visibility", "visible");


    }

    protected fadeOutAdditional() {

        this.groupNames && this.groupNames.remove();


    }


    private static defaultBODConfiguration:BODPieChartConfiguration = <BODPieChartConfiguration>{
        startingRadius:0,

        radiusDelta:65,
        topGrouping: {
            show: false,
            topCounts: [{top:2, valueCount:2},{top:3, valueCount:3}, {top:5, valueCount:5}, {top:10,valueCount:6}],
            topLabels:false,
            alignPieByTopLabels:false,
            radiusStep: 0.85,
            formatter: "%",
            font: {
                fontFamily: "Open Sans",
                fontSize: "0.45em",
                fontWeight: "normal",
            },
        },

        groupTitle: {
            show: false,
            //may be "FLAG"
            viewType:"EXTENSION_LINE",
            marginRight:10,
            marginLeft:7,
            lineSpacing:1.2,
            font: {
                //color of chart title font
                fontFamily: "Open Sans",
                fontSize: "0.4em",
                fontWeight: "normal",
            },
            background: {
                fill:"#ffffff",
                stroke:"none",
                strokeWidth:"0"
            },
            extensionLine:{
                stroke:"#68a1cb",
                strokeWidth:2,
                markerRadius:3,
                hgap:15
            }
        }

    }

}

export = BODPieChart;
