///<reference path="../IWidget.ts"/>
///<reference path="WorldMapModel.ts"/>
/**
 * @author rlapin
 */
var WorldMap = (function () {
    function WorldMap(target) {
        if (target instanceof HTMLElement) {
            this.element = target;
            return;
        }
        if (typeof (target) == "string") {
            this.element = document.querySelector(target);
        }
        this.width = this.element.offsetWidth;
        this.height = this.element.offsetHeight;
    }
    WorldMap.prototype.setData = function (data) {
        this.data = data;
    };
    WorldMap.prototype.setConfig = function (config) {
        this.config = config;
    };
    WorldMap.prototype.render = function () {
    };
    WorldMap.prototype.renderEmpty = function () {
        var widget = this;
        var svg = d3.select(widget.element).select("svg");
        svg.append("rect").attr({
            x: 0,
            y: 0,
            width: widget.width,
            height: widget.height
        }).style("fill", "white");
        svg.append("text").text("NO DATA").attr("text-anchor", "middle").attr("x", widget.width / 2).attr("y", widget.height / 2).attr("font-size", "2em");
    };
    return WorldMap;
})();
//# sourceMappingURL=WorldMap.js.map