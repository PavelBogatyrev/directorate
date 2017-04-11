///<reference path="FigureType.ts"/>
/**
 * Configuration for widget figure animation
 * Contains figureCount, figureTypes, figureColors, background, minSize, maxSize
 * @author rlapin
 */
import FigureType = require("./FigureType");
class FigureAnimationConfiguration {
    /**
     * number of max figure that visible on animation widget
     */
    private figureCount:number;
    /**
     * Figure types that used in animation
     */
    private figureTypes:Array<FigureType>;
    /**
     * Figure types that used in animation
     */
    private figureColors:Array<any>;
    /**
     * Color in argb(format)
     */
    private background:string;
    /**
     * Use as min value for random generation
     */
    private minSize:number;
    /**
     * Use as max value for random generation
     */
    private maxSize:number;

    public paddingRight:number;
    constructor(figureCount:number, figureTypes:Array<FigureType>, figureColors:Array<any>, minSize:number,maxSize:number) {
        this.figureCount = figureCount;
        this.figureTypes = figureTypes;
        this.figureColors = figureColors;
        this.maxSize = maxSize;
        this.minSize = minSize;
    }


    public getFigureCount():number {
        return this.figureCount;
    }

    public setFigureCount(value:number) {
        this.figureCount = value;
    }

    public getFigureTypes():Array<FigureType> {
        return this.figureTypes;
    }

    public setFigureTypes(value:Array<FigureType>) {
        this.figureTypes = value;
    }

    public getFigureColors():Array<any> {
        return this.figureColors;
    }

    public setFigureColors(value:Array<any>) {
        this.figureColors = value;
    }

    public getBackground():string {
        return this.background;
    }

    public setBackground(value:string) {
        this.background = value;
    }
    public getMinSize():number{
        return this.minSize;
    }
    public getMaxSize():number{
        return this.maxSize;
    }
    public setMinSize(minSize:number){
        this.minSize = minSize;
    }
    public setMaxSize(maxSize:number){
        this.maxSize = maxSize;
    }
}

export = FigureAnimationConfiguration;
