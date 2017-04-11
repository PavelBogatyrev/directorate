///<reference path="../IWidget.ts"/>
///<reference path="../../../d3/d3.d.ts"/>
/**
 * IWidget that show visualization of falling figures
 * @author rlapin
 */
import FigureAnimationConfiguration = require("./FigureAnimationConfiguration");
import FigureType = require("./FigureType");
class FigureAnimationWidget implements IWidget {
    private width:number;
    private height:number;
    private visible: boolean = true;
    private intervalId:number;
    private data:Array<Figure> = [];
    private element:HTMLElement;
    private config:FigureAnimationConfiguration;

    constructor(element:HTMLElement) {
        this.element = element;
        var widget = this;

    }

    setData(data:any):void {
        throw new Error("setData is not supported by FigureAnimationWidget");
    }

    setConfig(config:FigureAnimationConfiguration):void {
        config.paddingRight = 75;
        this.config = config;
    }

    clear():void {
        var svgElement = d3.select(this.element).select("svg");
        if (svgElement) {
            //make clear
            svgElement.remove();
        }
    }

    render():void {
        this.clear();

        var element:HTMLElement = this.element;
        var widget = this;
        var id = this.element.id;
        var data = this.data;
        var width:number = element.offsetWidth - this.config.paddingRight;
        var height:number = element.offsetHeight;
        this.width = width;
        this.height = height;

        if (id) {
            var svg = d3.select("#" + id).append("svg").classed("animationWidget", true);
            svg.attr(
                "width", width).attr(
                "height", height);

            this.fadeIn();

            widget.intervalId = setInterval(function(){
                var figures = svg.selectAll(".figure");
                var figureElement = figures.data(data).enter().append("g");
                figureElement.attr("class", "figure");
                figureElement.each(function (d) {
                    var element = d3.select(this);
                    switch (d.getType()) {
                        case FigureType.OVAL:
                            widget.createCircle(d, element);
                            break;
                        case FigureType.RECT:
                            widget.createRect(d, element);
                            break;
                        case FigureType.TRIANGLE:
                            widget.createTriangle(d, element);
                            break;
                        default:
                    }
                });
                figures.each(function (d) {
                    var figure = d3.select(this);
                    var dt = 20;

                    var speed = d.getSpeed() / dt;
                    var x = d.getX();
                    var y = d.getY() - speed;
                    var angle = d.getAngle() + d.getRotateSpeed() / (d.getWidth() / 2);
                    if (y + d.getHeight() < 0) {
                        figure.remove();
                        data.splice(data.indexOf(d), 1);
                        widget.createNewFigure();
                    } else {
                        if (x + d.getWidth() / 2 >= width) {
                            d.invertLeft();
                            x -= speed;
                        } else if (x - d.getWidth() / 2 <= 0) {
                            d.invertLeft();
                            x += speed;
                        } else {
                            x += d.getLeft() ? speed : -speed;
                        }
                        d.moveTo(x, y);
                        d.setAngle(angle);
                        figure.attr("transform", "translate(" + x + "," + y + ") rotate(" + angle / Math.PI * 180 + ")");


                    }
                });

            },10);
        }
        else {
            throw Error("Element has no id");
        }


    }
    public fadeOut():void{
        clearInterval(this.intervalId)
        this.visible = false;
    }
    private fadeIn():void {
        this.visible = true;
        var data = this.data;
        var config = this.config;
        while (data.length < config.getFigureCount()) {
            var figure = this.createNewFigure();
            figure.moveTo(Math.random() * this.width, Math.random() * this.height);
        }
    }

    /**
     * Draw triangle in element(group/svg)
     * @param d Figure data
     * @param element group or svg element
     */
    private createTriangle(d:Figure, element:any) {
        var r = Math.min(d.getWidth(), d.getHeight()) / 2;
        var point1 = 0 + "," + ( -r);
        var point2 = (-r * 0.8665) + "," + ( r * 0.5);
        var point3 = (r * 0.8665) + "," + (r * 0.5);
        var trianglePoints = point1 + " " + point2 + " " + point3;
        element.append("polyline").attr('points', trianglePoints).attr("fill", d.getColor()).attr("opacity", 0)
            .transition().duration(1000).attr("opacity", d.getOpacity());
    }

    /**
     * Draw rectangle in element(group/svg)
     * @param d Figure data
     * @param element group or svg element
     */
    private createRect(d:Figure, element:any) {
        element.append("rect").attr({
            "x": -d.getWidth() / 2,
            "y": -d.getWidth() / 2,
            "width": d.getWidth(),
            "height": d.getWidth(),
            "fill": d.getColor(),
            "opacity": 0
        }).transition().duration(1000).attr("opacity", d.getOpacity());
    }

    /**
     * Draw circle in element(group/svg)
     * @param d Figure data
     * @param element group or svg element
     */
    private createCircle(d:Figure, element:any) {
        element.append("circle").attr({
            "cx": 0,
            "cy": 0,
            "r": d.getWidth() / 2,
            "fill": d.getColor(), "opacity": 0
        }).transition().duration(1000).attr("opacity", d.getOpacity());
    }

    private createNewFigure():Figure {
        var width = this.width;
        var config = this.config;
        var item = Figure.randomFigure(config.getMinSize(), config.getMaxSize(), config.getFigureTypes(), config.getFigureColors());
        this.data.push(item);
        item.moveTo(Math.floor(Math.random() * width), this.height);
        return item;
    }


    private resize():void {
        var widget = this;
        if(widget.visible) {
            clearInterval(widget.intervalId);
            if (widget.element) {
                widget.render();
            } else {
                widget.clear();
            }
        }else{
            return;
        }

    }
}
class Figure {
    private type:FigureType;
    private x:number;
    private y:number;
    private speed:number;
    private width:number;
    private height:number;
    private isLeft:boolean;
    private color:string;
    private rotateSpeed:number;
    private angle:number = 0;
    private opacity:number = 0;
    constructor(width, height, type, color,opacity, speed, rotateSpeed, isLeft) {
        this.width = width;
        this.height = height;
        this.type = type;
        this.opacity = opacity;
        this.color = color;
        this.speed = speed;
        this.isLeft = isLeft;
        this.rotateSpeed = rotateSpeed;
    }

    public getType():FigureType {
        return this.type;
    }

    public getWidth():number {
        return this.width;
    }

    public getHeight():number {
        return this.height;
    }

    static randomFigure(minSize:number, maxSize:number, types:Array<FigureType>, colors:Array<any>):Figure {
        //TODO Move to math hrzcore module
        var width = Math.floor(Math.random() * (maxSize - minSize)) + minSize;
        var height = width;
        var speed = Math.random() * 10 + 5;
        var rotateSpeed = Math.random();
        var type = types[Math.floor(Math.random() * types.length)];

        var colorsList = colors.filter(function(d){
            if(type == FigureType.OVAL){
                return d.type == "OVAL";
            }else if(type == FigureType.RECT){
                return d.type == "RECT";
            }else{
                return d.type == "TRIANGLE"
            }
        });
        var index = Math.floor(Math.random()*colorsList.length);
        var color = colorsList[index].color;
        var opacity = colorsList[index].opacity;
        var isLeft = Math.floor(Math.random() * 2);
        return new Figure(width, height, type, color,opacity, speed, rotateSpeed, isLeft);
    }

    public moveTo(x:number, y:number):void {
        this.x = x;
        this.y = y;
    }

    public getX():number {
        return this.x;
    }

    public getY():number {
        return this.y;
    }

    public getColor():string {
        return this.color;
    }
    public getOpacity():number {
        return this.opacity;
    }
    public getSpeed():number {
        return this.speed;
    }

    public getRotateSpeed():number {
        return this.rotateSpeed;
    }

    public getLeft():boolean {
        return this.isLeft;
    }

    public invertLeft():void {
        this.isLeft = !this.isLeft;
    }

    public setAngle(angle:number) {
        this.angle = angle;
        if (this.angle > Math.PI * 2) {
            this.angle -= Math.PI * 2;
        }
    }

    public getAngle():number {
        return this.angle;
    }

}

export = FigureAnimationWidget;