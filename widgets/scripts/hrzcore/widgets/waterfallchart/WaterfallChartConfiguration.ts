///<reference path="../legend/LegendConfiguration.ts"/>
/**
 * BarChartConfiguration contains:
 * - Grid settings(Show grid/or not, grid color and so on)
 * @author rlapin
 */
interface WaterfallChartConfiguration {

    // Tick count for grid
    tickCount?: number;
    // Base font used if is not override
    baseFont?:{
        family?:string;
        size?:string;
        sizeMultiplier?:number;
    }
    //Legend settings
    showLegend?: boolean;
    //legend configuration is in ILegendConfiguration
    legend?: {
        //Position inside widget svg :  LEFT,RIGHT,TOP,BOTTOM
        position?:string;
        configuration?:LegendConfiguration;
    };

    // Show chart title
    showTitle?: boolean;

    // grid settings
    grid?:{
        color?: string
        opacity?: number;
        lineWidth?: number;
        //NONE/BOTH/HORIZONTAL/VERTICAL
        type?: string;
    };
    background?:string;
    //PADDING IN PX
    padding?:{
        left?: number;
        right?: number;
        top?: number;
        bottom?: number;
    };
    //AXIS SETTINGS
    axis?:{
        // AXIS LABELS OFFSET

        fontPadding?:{
            left?: number;
            right?: number;
            top?: number;
            bottom?: number;
        }
        // X AXIS SETTINGS
        x?:{

            textColor?: string;
            font?:{
                size?: string;
                weight?: string;
                family?: string;
            }
        };
        // Y AXIS SETTINGS
        y?:{
            //percent of height
            bottomPaddingDelta: number;
            topPaddingDelta: number;
            //value format
            format?: string,
            //FP digits
            formatDigits?: number,
            textColor?: string;
            font?:{
                size?: string;
                weight?: string;
                family?: string;
            }
        },

        textColor?: string;
        //TYPE NONE, BOTH , HORIZONTAL, VERTICAL
        type?: string;
        color?: string;
        yTextPosition?: string;
        xTextPosition?: string;
    }



    //BAR SETTINGS
    bar?:{
        colors:{
            positive?:{
                background?: string;
                opacity?: number;
            }
            negative?:{
                background?: string;
                opacity?: number;
            }
            startValue?:{
                background?: string;
                opacity?: number;
            }
            endValue?:{
                background?: string;
                opacity?: number;
            }
         }

//PADDING & OFFSET % OF INITIAL BAR WIDTH
    leftOffset?: number;
    rightOffset?: number;
    padding ? : number;
    showValues ? : boolean;

    values ? : {
        textColor ? : string;
        format ? : string;
//FP digits
        formatDigits ? : number,
        position ? : string;
        font ? : {
            weight? : string;
            size ? : string;
            family ? : string;
        }

    },

        startValue ? : {
            textColor ? : string;
            format ? : string;
//FP digits
            formatDigits ? : number,
            position ? : string;
            font ? : {
                weight? : string;
                size ? : string;
                family ? : string;
            }

        },

        endValue ? : {
            textColor ? : string;
            format ? : string;
//FP digits
            formatDigits ? : number,
            position ? : string;
            font ? : {
                weight? : string;
                size ? : string;
                family ? : string;
            }

        },

}

animation: {
    duration?: number;
    active ? : boolean;
}
}
