///<reference path="PieChartModel.ts"/>
///<reference path="PieChartConfiguration.ts"/>
///<reference path="../IWidget.ts"/>
///<reference path="../../../d3/d3.d.ts"/>
///<reference path="../legend/LegendConfiguration.ts"/>
///<reference path="../legend/LegendModel.ts"/>
///<reference path="../legend/Legend.ts"/>

import ObjectUtils = require("../../utils/ObjectUtils");
import StringUtils = require("../../utils/StringUtils");
import BBoxUtils = require("../../utils/BBoxUtils");
import ArrayUtils = require("../../utils/ArrayUtils");
import Legend = require("../legend/Legend");


class PieChart implements IWidget {

    public static  create(trg):PieChart {
        return new PieChart(trg);
    }

    protected configuration:PieChartConfiguration;
    protected parentEl:HTMLElement;
    protected rootSVG:d3.Selection<any>;
    protected model:PieChartModel;
    protected legend:Legend;
    protected fadeInPiesParams:{cx:number; cy:number; currentRadius:number};
    protected piesGroup:d3.Selection<any>;
    protected pies:Array<any>;
    protected d3_selections:Array<{
        shadow?:d3.Selection<any>;
        sectors?:d3.Selection<any>;
        innerLabels?:d3.Selection<any>;
    }>;
    protected startTime:number;
    protected filterKey:string;
    protected maxRadius:number;

    constructor(trg:any) {
        try {
            if (trg instanceof HTMLElement) {
                this.parentEl = trg;
                return;
            }

            if (typeof (trg) == "string") {
                var selectors = document.querySelectorAll(trg);
                this.parentEl = <HTMLElement>selectors[selectors.length - 1];
            }
            this.setConfig({});
            this.configuration.legend.configuration = ObjectUtils.deepExtend({},Legend.defaultConfiguration,this.configuration.legend.configuration);

        } catch (e) {
            console.log(e);
        }
        this.d3_selections = [];
    }

    public setData(data:PieChartModel):void {
        this.model = data;
    }

    public setConfig(config:PieChartConfiguration):void {
        this.configuration = ObjectUtils.deepExtend({}, PieChart.defaultConfiguration, config);
    }

    public getConfig():PieChartConfiguration {
        return this.configuration;
    }

 /**
  * Draw chart without animation
  * */
    public render() {
        this.configuration.animationActive = false;
        this.fadeIn();

    }

  /**
   * Definition of shadow filter
   * */

    protected defineShadow():void {
        //enabling shadow filters
        var that=this;
        var d = 1, D = 2;
        var shadowAttrs = [{dx:d,dy:-D},{dx:D,dy:-d},{dx:D,dy:d},{dx:d,dy:D},{dx:-d,dy:D},{dx:-D,dy:d},{dx:-D,dy:-d},{dx:0,dy:0}];
        this.filterKey = ""+Math.random();
        var defs = this.rootSVG.append("defs");
        var filter = defs.selectAll("filter").data(shadowAttrs).enter().append("filter")
            .attr("x","-4").attr("y","-4")
            .attr("id", function(d,i) {return "drop-shadow"+i+that.filterKey})
            .attr("height", "1000%")
            .attr("width", function(d) { return "1000%"});
        filter.append("feGaussianBlur")
            .attr("in", "SourceAlpha")
            .attr("stdDeviation", 8)
            .attr("result", "blur");
        filter.append("feOffset")
            .attr("in", "blur")
            .attr("dx", function(d){return d.dx})
            .attr("dy",function(d){return d.dy})
            .attr("result", "offsetBlur");
        var feMerge = filter.append("feMerge");
        feMerge.append("feMergeNode")
            .attr("in", "offsetBlur")
        feMerge.append("feMergeNode")
            .attr("in", "SourceGraphic");
    }


    /**
     * Check conditions for chart rendering
     * @returns {boolean}
     */
    protected prerenderValidate():boolean {
        if (!this.parentEl) return false;
        var model = this.model;
        if (!model || !(model.data instanceof Array) || model.data.length == 0 || !(model.data[0].values instanceof Array) || model.data[0].values.length == 0) {
            this.parentEl.innerHTML = "No data to show";
            return false;
        }

        this.clear();
        return true;

    }


    /**
     *Preparation befor fadeIn/render of pieChart
     */
    protected prepare():void {

        if (!this.prerenderValidate()) return;
        var model = this.model;
        var cfg = this.configuration;
        var width = this.parentEl.clientWidth;
        if(width == 0){
            width = window.innerWidth * 0.4;
        }
        var height = this.parentEl.clientHeight;
        var x = 0;
        var y = 0;
        var title;
        var bb;
        var that = this;
        this.pies = [];

        var svg = d3.select(this.parentEl).append("svg").attr({
            width: width,
            height: height
        }).style(ObjectUtils.convertObjectKeysToKebabCase(cfg.baseFont)).append("g").attr("class","mainG");
        this.rootSVG = svg;
        this.defineShadow();

        var that = this;

        if (model.title && cfg.title && cfg.title.show) {
            title = svg.append("text").text(model.title).style(ObjectUtils.convertObjectKeysToKebabCase(cfg.title.font))
                .attr("x", width / 2);
            bb = (<SVGTextElement>(title.node())).getBBox();
            title.attr("y", cfg.title.topPadding + 2 + Math.ceil(bb.height / 2));
            bb = (<SVGGElement>(title.node())).getBBox();
            y = bb.y + bb.height + cfg.title.bottomPadding;
            height = height - y;
        }

        if (cfg.legend && cfg.legend.show) {
            that.renderLegend(x + cfg.legend.marginLeft, y);
            that.legend.hide();
            bb = that.legend.getBBox();
            if (cfg.legend.position == "LEFT") {
                //outerRadius = Math.min(initWidth - bb.width - bb.x - cfg.legend.marginRight, height) / 2;
                d3.select(that.legend.getParentEl()).attr("transform", "translate(" + x + "," + (y + (height - bb.height) / 2) + ")");
                x = bb.x + bb.width + cfg.legend.marginRight;
                width = width - x;
            } else if (cfg.legend.position == "BOTTOM") {
                height -= (bb.height + cfg.legend.marginBottom + cfg.legend.marginTop);
                d3.select(that.legend.getParentEl())
                    .attr("transform", "translate(" + (x + (width - bb.width) / 2 - bb.x) + "," + (y + height + cfg.legend.marginTop) + ")");
            }
        }

        x += cfg.pie.marginLeft;
        y += cfg.pie.marginTop;
        width -= cfg.pie.marginRight;
        height -= cfg.pie.marginBottom;

        var maxRadius = cfg.pie.radiusKoeff*Math.min(width, height) / 2 - 1;
        var cx,cy;

        if (cfg.hAlign.toLowerCase() == "center") {
            cx = x + width / 2;
        } else if (cfg.hAlign.toLowerCase() == "left") {
            cx = x + maxRadius+1;
        } else {
            cx = x + width - maxRadius -1;
        }

        cy = Math.ceil(y + height / 2);
        this.maxRadius = maxRadius;
       this.beforePieRender(x,y,width,height,cx,cy);

        this.fadeInPiesParams = {
            cx:cx,cy:cy,currentRadius:this.maxRadius
        }
        this.piesGroup = svg.append("g");
        this.afterPieRender(x,y,width,height,cx,cy);
    }

    /**
     * This method may be overriden in descendant classes if required some drawing before rendering of piechart
     * @param x - the left edge of avalable place for drawing
     * @param y - the top edge of avalable place for drawing
     * @param width - the width of avalable place for drawing
     * @param height - the height of avalable place for drawing
     * @param cx - the x coordinate of pie center.
     * @param cy - the y coordinate of pie center.
     */
    protected beforePieRender(x,y,width,height,cx,cy) {

    }

    /**
     * This method may be overriden in descendant classes if required some drawing after rendering of piechart
     * @param x - the left edge of avalable place for drawing
     * @param y - the top edge of avalable place for drawing
     * @param width - the width of avalable place for drawing
     * @param height - the height of avalable place for drawing
     * @param cx - the x coordinate of pie center.
     * @param cy - the y coordinate of pie center.
     */
    protected afterPieRender(x,y,width,height,cx,cy) {

    }

    /**
     *Getting colors array with lngth required for rendering of all groups by means repeating configuration colors.
     * * @returns {Array}
     */
    protected getColors():Array<string> {
        var dataLength = Math.max.apply(null,this.model.data.reduce(function(s,d){s.push(d.values.length); return s;},[]));
        var repeatCount = Math.ceil(dataLength/this.configuration.colors.length);
        var colors = [];
        for (var i=0; i<repeatCount;i++){
            colors = colors.concat(this.configuration.colors);
        }
        return  colors;
    }

    /**
     * rendering svg legend with top left at (x,y)
     * @param x
     * @param y
     */
    protected renderLegend(x, y) {
        var cfg = this.configuration;
        var legend:Legend = this.legend = Legend.create(this.rootSVG.append("g").attr("transform", "translate(" + x + "," + y + ")").node());
        legend.setConfig(cfg.legend.configuration);
        var model = this.model;
        var colors = this.getColors();

        if (! (model.seriesNames instanceof Array) || model.seriesNames.length == 0) {
            if (! (model.data[0].values instanceof Array)) {
                return;
            }  else {
                model.seriesNames = [];
                for (var j=0; j<model.data[0].values.length; j++) model.seriesNames.push("");
            }

        }

        var data = model.seriesNames.map((function (d, i) {
            return {
                label: d || "",
                color: colors[i],
                value: model.data[0].values[i] || 0
            }
        }));
        if (cfg.sorting == "ASC" || cfg.sorting == "DSC") {
            var comparator = cfg.sorting == "ASC" ? function (a, b) {
                return a.value - b.value
            } : function (a, b) {
                return b.value - a.value
            };
            data.sort(comparator);
        }
        legend.setData({data: data, formatter: cfg.legend.configuration.value.formatter});
        legend.render();
    }

    /**
     *Render piechart according given parameters
     * @param group - object containing data fom model
     * @param cx - x of pie center
     * @param cy - y of pie center
     * @param R - radius of pie
     * @param shadow - if shadow must be applied to outer edge of pie
     */

    protected renderPie(group:{groupName?:String; values:Array<number>}, cx, cy, R, shadow):void {

        var cfg = this.configuration;
        var pieLayout = d3.layout.pie().sort(cfg.sorting == "ASC" ? d3.ascending : cfg.sorting == "DSC" ? d3.descending : null);
        var pieLayeredData = pieLayout(group.values);
        var dataCount = group.values.length;
        var arcRenderFunc = d3.svg.arc<any>();
        var time = cfg.animationActive ? cfg.animationTime : 0;

        var colors = this.getColors();
        var paddingFromCircleEdge = shadow? cfg.innerLabels.paddingFromCircleEdgeInner : cfg.innerLabels.paddingFromCircleEdge;
        var labelArc = d3.svg.arc<any>().outerRadius(R*(1 - paddingFromCircleEdge)).innerRadius(R*(1 - paddingFromCircleEdge));
        var rootGroup = this.piesGroup.append("g").attr("transform", "translate(" + cx + "," + cy + ")");
        var that = this;
        var pieSelections = {shadow:null,sectors:null, innerLabels:null};
        this.d3_selections.push(pieSelections);


        if (!shadow) {
        } else {
            rootGroup.selectAll("g.shadow").remove();
            var shadowGroups = rootGroup.selectAll("g.shadow")
                .data(pieLayeredData)
                .enter()
                .append("g").attr("class", "shadow");
            var shadowArc = d3.svg.arc<any>().outerRadius(R - 1).innerRadius(R - 20);
            var shad = pieSelections.shadow = shadowGroups.append("path")
                    .style("fill", function (d, i) {
                        return colors[i];
                    })
                    .style("stroke", function (d, i) {
                        return colors[i];
                    })
                    .style("filter", function (d) {
                        return "url(#drop-shadow" + Math.floor((d.startAngle + d.endAngle) * 2 / Math.PI) + that.filterKey + ")"
                    })
                    .attr("d", function (d, i, arr) {
                        var rad = that.getSegmentOuterRadius(R, i, dataCount);
                        return shadowArc.innerRadius(rad - 10).outerRadius(rad - 1)(d);
                    })
                    .attr("visibility", "hidden")
                ;
            setTimeout(function () {
                shad.attr("visibility", "visible")

            }, time + 50)

        }


        rootGroup.selectAll("g.sector").remove();
        var sectorGroups = rootGroup.selectAll("g.sector")
            .data(pieLayeredData)
            .enter()
            .append("g").attr("class","sector");

        pieSelections.sectors = sectorGroups.append("path")

            .style("fill", function (d, i) {
                return colors[i];
            })
            .style(ObjectUtils.convertObjectKeysToKebabCase(cfg.segmentBorderPath));


        pieSelections.sectors.transition().ease("linear")
            .duration(time)
            .attrTween("d", tweenPie)
        ;

        if (cfg.innerLabels && cfg.innerLabels.show) {

            setTimeout(function () {

                var innerLabelFormatter = cfg.innerLabels.formatter;
                var centroids = [];

                pieSelections.innerLabels = sectorGroups.append("text")
                    .attr("transform", function (d, i, arr) {
                        var r = that.getSegmentOuterRadius(R,i,dataCount)*(1 - paddingFromCircleEdge);
                        var c = labelArc.outerRadius(r).innerRadius(r).centroid(d);
                        centroids.push(c);
                        return "translate(" + c + ")";
                    })
                    .attr("text-anchor", "middle")
                    .attr("dominant-baseline","central")
                    .style(ObjectUtils.convertObjectKeysToKebabCase(cfg.innerLabels.font))
                    .style("stroke", "none")
                    .text(function (d) {
                        return StringUtils.formatString(d.value, innerLabelFormatter, 1);
                    }).attr("visibility", function (d, i) {
                        var bb = (this as SVGTextElement).getBBox();
                        bb.x += centroids[i][0];
                        bb.y += centroids[i][1];
                        return BBoxUtils.isBBoxInsideSector(bb, d["outerRadius"] + 5, d["innerRadius"] - 5, d.startAngle, d.endAngle) ? "visible" : "hidden";
                    });
            }, time + 100);

        }


        function tweenPie(b, k) {

            b.innerRadius = 0;
            b.outerRadius = that.getSegmentOuterRadius(R,k,dataCount);
            var i = d3.interpolate({outerRadius: cfg.startingRadius*b.outerRadius, startAngle: 0, endAngle: 0}, b);
            return function (t) {
                return arcRenderFunc(i(t));
            };
        }


    }

    /**
     * fadeOut of pie
     * @param num number of pie (if several one was rendered)
     */
    protected fadeOutPie(num:number) {
        var cfg = this.configuration;
        var time =  cfg.animationTime;
        var that = this;
        var piedef = this.pies[num];
        var arcRenderFunc = d3.svg.arc<any>();

        var pieSelections = this.d3_selections[num];

        pieSelections.innerLabels && pieSelections.innerLabels.remove();
        pieSelections.shadow && pieSelections.shadow.remove();
        pieSelections.sectors
            .transition().ease("linear")
            .duration(time)
            .attrTween("d", tweenPieBack)
        ;

        function tweenPieBack(b, k) {
            b.innerRadius = 0;
            b.outerRadius = that.getSegmentOuterRadius(piedef.R,k,piedef.dataCount);
            var i = d3.interpolate(b,{outerRadius: cfg.startingRadius*b.outerRadius, startAngle: 0, endAngle: 0});
            return function (t) {
                return arcRenderFunc(i(t));
            };
        }

    }


    /**
     * helper for definng of sector shape radius, It may be overriden in order to design pie chart with
     * variative radius
     * @param pieRadius
     * @param segmentNumber
     * @param segmentCount
     * @returns {any}
     */
    //TODO: It looks to be someway redesigned
    protected  getSegmentOuterRadius(pieRadius, segmentNumber, segmentCount) {
        return pieRadius;
    }

    /**
     * fadeIn - drawing chart with animation
     */

    public fadeIn():void {
        try {

        this.prepare();

        var that =this;
        var params = this.fadeInPiesParams;
        var cfg = this.configuration;
        var currentRadius = params.currentRadius;

        this.model.data.forEach(function (d, i) {
            that.pies.push({
                R:currentRadius,
                dataCount:d.values.length
            });
            that.renderPie(d, params.cx, params.cy, currentRadius, i > 0);
            currentRadius *= cfg.radiusDelta;
        });
        setTimeout(function () {
            that.legend && that.legend.show();
            that.fadeInAdditional();
        }, cfg.animationActive ? cfg.animationTime + 100 : 0);
        } catch (e){
            console.log(e);
        }

    }


    /**
     * animated hiding of chart
     */
    public fadeOut():void {
        try {

            var that =this;
            var params = this.fadeInPiesParams;
            var cfg = this.configuration;
            var currentRadius = params.currentRadius;
                that.legend && that.legend.hide();
                that.fadeOutAdditional();
            this.model.data.forEach(function (d, i) {

                that.fadeOutPie(i);
                currentRadius *= cfg.radiusDelta;
            });

        } catch (e){
            console.log(e);
        }

    }


    /**
     * This method may be overriden in descendant classes if additinal ditails need to be animated when arise
     */
    protected fadeInAdditional() {

    }

    /**
     * This method may be overriden in descendant classes if additinal ditails need to be animated when disappear
     */

    protected fadeOutAdditional() {

    }


    /**
     * chart resizer
     */

    public resize() {

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


    public static defaultConfiguration:PieChartConfiguration = <PieChartConfiguration>{
        //Options for basic PieChart

        //sorting mode for PieChart: NONE - default,ASC,DSC
        sorting: "NONE",

        //color palette for segments - may be overridden in IPieChartModel
        colors: ["#12263e", "#2a4e79", "#356dad", "#568ac0", "#68a1cb", "#8ba9d2", "#92b7d8"],

        animationTime: 1500,
        animationActive:true,
        startingRadius:0.1,

        hAlign:"CENTER",

        baseFont: {
            //color of chart title font
            fontFamily: "Open Sans",
            fontSize: "4vmin",
            fontWeight:"300",
            fill:"#2d2b29"
        },

        pie:{
            radiusKoeff:1,
            marginLeft:0,
            marginRight:0,
            marginTop:0,
            marginBottom:0

        },

        //Formatting for title of chart
        title: {
            show: false,
            font: {
                //color of chart title font
                fontFamily: "Open Sans",
                fontSize: "1em",
                fontWeight: "normal",
                textAnchor: "middle"
            },
            topPadding: 5,
            bottomPadding: 10
        },




//Radial and circular borders of segment
        segmentBorderPath: {
            //color of segment borders
            stroke: "#ffffff",

//width of segment borders
            strokeWidth: "2px",
        },




// labels inside segments
        innerLabels: {
            show: false,

//style specification of inner labels font
            font: {
                //color of chart title font
                fill: "#ffffff",
                fontFamily: "Open Sans",
                fontSize: "0.5em",
                fontWeight: "normal",
            },
            paddingFromCircleEdge: 0.1,
            paddingFromCircleEdgeInner: 0.12,
            isPercent: false,
            formatter: "$"
        },


// labels outside segments
        outerLabels: {
            show: false,

//style specification of inner labels font
            font: {
                //color of chart title font

                fontFamily: "Open Sans",
                fontSize: "0.6em",
                fontWeight: "normal",
            },
            isPercent: false,
            formatter: "$",
        },

        legend: {
            show: false,
            marginLeft: 0,
            marginRight: 20,
            marginTop: 0,
            marginBottom: 0,
            position:"LEFT"
        },

        total: {

            show: false,

            //LEFT, RIGHT, CENTER
            position: "CENTER",
            edgeMargin: 10,
            bottomMargin: 5,
            formatter: "$",
            font: {
                //color of chart title font
                fill: "#2d2b29",
                fontFamily: "Open Sans",
                fontSize: "0.7em",
                fontWeight: "bold",
            }
        }
    }
}

export = PieChart;
