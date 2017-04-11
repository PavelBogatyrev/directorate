///<reference path="../IWidget.ts"/>
///<reference path="WorldMapModel.ts"/>
///<reference path="../../../d3/d3.d.ts"/>
/**
 * @author rlapin
 */


class WorldMap implements IWidget {
    private data:WorldMapModel;
    private config = {animation:{active:true}};
    private element:HTMLElement;
    private width:number;
    private height:number;
    private mapSvg;
    private isMapLoad:boolean;

    constructor(target) {
        this.isMapLoad = false;
        if (target instanceof HTMLElement) {
            this.element = target;
            return;
        }

        if (typeof (target) == "string") {
            this.element = <HTMLElement>document.querySelector(target);
        }
        this.width = this.element.offsetWidth;
        this.height = this.element.offsetHeight;
    }

    setData(data:WorldMapModel):void {
        this.data = data;
    }

    setConfig(config:any):void {
        this.config = config;
    }

    render():void {
        var widget = this;
        var data:WorldMapModel = this.data;
        if (!widget.mapSvg) {
            widget.renderEmpty();
        } else {
            d3.xml(widget.mapSvg, "image/svg+xml", function (error, xml) {
                if (error) throw error;
                var svgElement = xml.documentElement;
                widget.element.appendChild(svgElement);
                var svg = d3.select(svgElement);
                svg.classed("worldMap", true).attr("opacity", 0);
                svg.selectAll("polyline").attr("opacity", 0);
                svg.selectAll("rect").attr("opacity", 0);
                svg.selectAll("text").attr("opacity", 0);
                data.values.forEach(function (d) {
                    svg.select("#VALUE_" + d.id).text(d.value);
                    svg.select("#TEXT_" + d.id).text(d.country);
                });
                widget.isMapLoad = true;
                widget.fadeIn();
            });
        }
    }

    fadeIn():void {
        var widget = this;
        var data = this.data;
        var animationMultiplier = 1;
        var config = this.config;
        if (config.animation && !config.animation.active) {
            animationMultiplier = 0;
        }

        if (widget.isMapLoad) {
            var svg = d3.select(widget.element).select(".worldMap");
            svg.transition().duration(1000 * animationMultiplier).attr("opacity", 1);
            data.values.forEach(function (d) {
                svg.select("#POLYLINE_" + d.id).attr("opacity", 0).transition().duration(2000 * animationMultiplier).attr("opacity", 1);
                svg.select("#RECTANGLE_" + d.id).select("rect").attr("opacity", 1);
                svg.select("#RECTANGLE_" + d.id).attr("transform", "translate(" + -widget.width + "," + 0 + ")").transition().duration(2000 * animationMultiplier).attr("transform", "translate(" + -0 + "," + 0 + ")");
                svg.select("#TEXT_" + d.id).attr("opacity", 1).each(function (d) {
                    var text = d3.select(this);
                    var transform = d3.transform(d3.select(this).attr("transform")).translate;
                    text.attr("transform", "translate(" + (transform[0] - widget.width) + "," + transform[1] + ")").transition().delay(1000 * animationMultiplier).duration(2000 * animationMultiplier).attr("transform", "translate(" + (transform[0]) + "," + (transform[1]) + ")");
                });
                svg.select("#VALUE_" + d.id).attr("opacity", 1).each(function (d) {
                    var text = d3.select(this);
                    var transform = d3.transform(d3.select(this).attr("transform")).translate;
                    text.attr("transform", "translate(" + (transform[0] - widget.width) + "," + transform[1] + ")").transition().delay(2000 * animationMultiplier).duration(2000 * animationMultiplier).attr("transform", "translate(" + (transform[0]) + "," + (transform[1]) + ")");
                });
            });

        }
    }

    private renderEmpty():void {
        var widget = this;
        var svg = d3.select(widget.element).select("svg");
        svg.append("rect").attr({
            x: 0,
            y: 0,
            width: widget.width,
            height: widget.height,
        }).style("fill", "white");
        svg.append("text").text("NO DATA").attr("text-anchor", "middle").attr("x", widget.width / 2).attr("y", widget.height / 2).attr("font-size", "2em");

    }

    private setMap(map:any):void {
        this.mapSvg = map;
    }

    static createWorldMap(target, map):WorldMap {
        var worldMap = new WorldMap(target);
        worldMap.setMap(map);
        return worldMap;
    }

    getConfig() {
        return this.config;
    }

}
export = WorldMap;