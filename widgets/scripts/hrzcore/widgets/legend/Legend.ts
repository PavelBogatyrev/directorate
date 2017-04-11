///<reference path="LegendModel.ts"/>
///<reference path="LegendConfiguration.ts"/>
///<reference path="../IWidget.ts"/>
///<reference path="../../../d3/d3.d.ts"/>
///<reference path="../../../lodash/lodash.d.ts"/>
import ObjectUtils = require("../../utils/ObjectUtils");
import BBoxUtils = require("../../utils/BBoxUtils");
import StringUtils = require("../../utils/StringUtils");

class Legend implements IWidget {

    public static  create(trg):Legend {
        return new Legend(trg);
    }

    private model:LegendModel;
    private configuration:LegendConfiguration;
    private parentEl:SVGGElement;

    public getBBox() {
        return this.parentEl.getBBox();
    }

    public getParentEl() {
        return this.parentEl;
    }

    constructor(trg) {
        if (trg instanceof SVGGElement) {
            this.parentEl = trg;
        }
    }

    setData(data:LegendModel):void {
        this.model = data;
    }

    setConfig(config:LegendConfiguration):void {
        this.configuration = ObjectUtils.deepExtend({}, Legend.defaultConfiguration, config);
    }

    getConfig():LegendConfiguration{
        return this.configuration;
    }
    render():void {
        try {
            var cfg = this.configuration;
            var model = this.model;
            var that = this;
            var bg = d3.select(this.parentEl).append("g");
            var elements = createElements();
            var bb:SVGRect, elHeight, elWidth, xPos;
            var elCount = model.data.length;
            var labels, icons;
            var iconSize, oldIconSize = cfg.icon.size,delta;
            var bboxes:Array<SVGRect>;



            if (cfg.direction == "VERTICAL") {


                xPos = 0;
                if (cfg.icon.show) {
                    xPos += cfg.icon.marginLeft;
                    icons = createIcons(elements,cfg.icon.size).attr("x", xPos);

                    xPos += cfg.icon.size;
                }

                if (cfg.label.show) {
                    xPos += cfg.label.marginLeft;
                    labels = createLabels(elements).attr("x", xPos);
                    bboxes =  [] as Array<SVGRect>;
                    labels.each(function(){
                        bboxes.push((this as SVGTextElement).getBBox());
                    });

                    iconSize = BBoxUtils.getCompositeBBox(bboxes).height;
                    delta = iconSize - oldIconSize;
                    icons.attr({
                        width:iconSize,
                        height:iconSize,
                        dx: - delta/2,
                        dy: - delta/2
                    });

                    var bb = this.getBBox();
                    xPos = bb.x + bb.width;
                }

                if (cfg.value.show) {
                    xPos += cfg.value.marginLeft;
                    createValues(elements).attr("x", xPos);
                }

                var columnSize = Math.ceil(model.data.length / cfg.columnCount);
                var colX = 0;
                elHeight = (elements.node() as SVGGElement).getBBox().height + cfg.vgap;

                for (var col = 0; col < cfg.columnCount; col++) {
                    var startInd = col * columnSize;
                    var endInd = Math.min((col + 1) * columnSize - 1, elCount - 1);
                    var colSelection = elements.filter(function (d, i) {
                        return i >= startInd && i <= endInd;
                    });
                    bboxes = [] as Array<SVGRect>;
                    colSelection.each(function (d, i) {
                        bboxes.push(this.getBBox());
                    });
                    var bb = BBoxUtils.getCompositeBBox(bboxes);

                    colSelection.attr("transform", function (d, i) {
                        return "translate(" + colX + "," + (col * columnSize + i - startInd) * elHeight + ")";
                    });

                    colX += (cfg.hgap + bb.width + bb.x);

                }


            } else {

                var xPosArr = model.data.map(function () {
                    return 0;
                });

                if (cfg.label.show) {

                    labels = createLabels(elements);

                    bboxes = [] as Array<SVGRect>;
                    labels.each(function () {
                        bboxes.push((this as SVGTextElement).getBBox());
                    });

                    bboxes = [] as Array<SVGRect>;
                    labels.each(function () {
                        bboxes.push((this as SVGTextElement).getBBox());
                    });

                    iconSize = BBoxUtils.getCompositeBBox(bboxes).height *0.6;

                } else {
                    iconSize = cfg.icon.size;
                }


                if (cfg.icon.show) {
                    createIcons(elements,iconSize).attr("x", 0).each(function (d, i) {
                        xPosArr[i] += iconSize;
                    });
                }

                if (cfg.label.show) {
                    xPosArr.forEach(function (d, i) {
                        xPosArr[i] += iconSize*0.2;
                    });
                    labels.attr("x", function (d, i) {
                        return xPosArr[i];
                    }).each(function (d, i) {
                        xPosArr[i] += (this.getBBox().width + iconSize);
                    });


                }

                if (cfg.value.show) {
                    xPosArr.forEach(function (d) {
                        d += cfg.value.marginLeft;
                    });
                    createValues(elements).attr("x", function (d, i) {
                        return xPosArr[i];
                    }).each(function (d, i) {
                        xPosArr[i] += this.getBBox().width;
                    });
                }

                xPos = 0;
                elements.attr("transform", function (d, i) {
                    var ret = "translate(" + xPos + ",0)";
                    bb = (this as SVGGElement).getBBox();
                    xPos += (bb.width + iconSize*cfg.hgap);
                    return ret;
                })

            }

            if (cfg.backgroundColor && cfg.backgroundColor.toLowerCase() != "none") {
                bb = (d3.select(this.parentEl).node() as SVGGElement).getBBox();
                bg.append("rect").style("fill", cfg.backgroundColor).style("stroke", "none").attr({
                    x: bb.x - 50,
                    y: bb.y - 20,
                    width: bb.width + 100,
                    height: bb.height + 40,
                })
            }
        }catch (e){
            console.log(e);
        }


        function createElements():d3.Selection<any> {
            return d3.select(that.parentEl).selectAll("g.legends").data(model.data).enter().append("g").attr("class","legends");
        }

        function createIcons(sel:d3.Selection<any>, iconSize):d3.Selection<any> {
            var shape = that.configuration.icon.shape.toLowerCase();
            var icons = sel.append(shape).style("fill", function (d) {
                return d.color
            }).style(ObjectUtils.convertObjectKeysToKebabCase(cfg.icon.style));

            if (shape == "rect") {
                icons.attr({x: 0, y: -iconSize/2, width: iconSize, height: iconSize})
            }

            if (shape == "circle") {
                icons.attr({cx: iconSize/2, cy: 0, r: iconSize/2})
            }

            return sel;


        }
        function createLabels(sel:d3.Selection<any>):d3.Selection<any> {
            return sel.append("text").text(function (d) {
                return d ? d.label : ""
            }).attr({x: 0, y:0 })
                .style(ObjectUtils.convertObjectKeysToKebabCase(cfg.label.font))
                //.attr("dy",""+parseFloat(cfg.label.font.fontSize)*0.8+"em");
                .attr("dy","0.325em");
        }

        function createValues(sel:d3.Selection<any>):d3.Selection<any> {
            var formatter;
            if (model.formatter && model.formatter != "") {
                formatter = model.formatter;
            }
            return sel.append("text").text(function (d) {
                var str = d.value || "";
                return formatter && str!="" ? StringUtils.formatString(str,formatter,1) : str;
            }).style(ObjectUtils.convertObjectKeysToKebabCase(cfg.value.font))
              .attr("dy",""+cfg.icon.size+"px");
        }

    }

    public static defaultConfiguration:LegendConfiguration = {
        direction: "VERTICAL",

        columnCount: 1,

        backgroundColor: "none",

        width:0,

        hgap:1,

        vgap:0,

        icon: {
            show: true,

            //RECT,CIRCLE
            shape: "rect",

            size: 20,

            style: {

                stroke: "none",
                strokeWidth: "0px"
            },
            marginLeft:0
        },


        label: {
            show: true,
            font: {
                //color of label title font
                fill: "#000000",
                fontFamily: "Open Sans:300",
                fontSize: "0.7em",
                fontWeight: "normal",
            },
            marginLeft:10

        },

        value: {
            show: false,
            formatter: "%",
            font: {
                //color of value title font
                fill: "#000000",
                fontFamily: "Open Sans:300",
                fontSize: "0.7em",
                fontWeight: "normal",
            },
            marginLeft:10
        }

    }




    public hide(){
        this.parentEl.style.visibility = "hidden";
    }

    public show(){
        this.parentEl.style.visibility = "visible";
    }

}

export = Legend;
