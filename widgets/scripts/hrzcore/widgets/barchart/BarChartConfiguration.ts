///<reference path="../legend/LegendConfiguration.ts"/>
/**
 * BarChartConfiguration contains:
 * - Grid settings(Show grid/or not, grid color and so on)
 * @author rlapin
 */
interface BarChartConfiguration {

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
        font?: {
            weight?: string;
            size?: string;
            family?: string;
        }
        padding?: number;
        //Position inside widget svg :  LEFT,RIGHT,TOP,BOTTOM
        position?:string;
        configuration?:LegendConfiguration;
    };
    // Fill bar with color even there is no value
    showShadowBars?: boolean;
    shadowBarColor?: string;
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
        };
        textColor?: string;
        //TYPE NONE, BOTH , HORIZONTAL, VERTICAL
        type?: string;
        color?: string;
        xTextPosition?: string;
        yTextPosition?: string;
    }
    type: string;
    //HORIZONTAL, VERTICAL
    orientation: string;
    //BARGROUP SETTINGS
    barGroup?:{
        //PADDING % OF INITIAL BARGROUP WIDTH
        padding?: number;
        showSeparator?: boolean;
        separator?:{
            stroke?:string;
            strokeWidth?: number;
        };
        showTotalValue?: boolean;
        totalValue?:{
            position?: string;
            textColor?: string;
            //FP digits
            formatDigits?: number,
            format?: string;
            font?:{
                size?: string;
                weight?: string;
                family?: string;
            }
        }
    }

    //BAR SETTINGS
    bar?:{
        colors?: Array<string>;
        //PADDING % OF INITIAL BAR WIDTH
        padding?: number;
        showCaptions?: boolean;
        showValues?: boolean;
        captions?: {
            textColor?: string;
            font?: {
                weight?: string;
                size?: string;
                family?: string;
            }

        }
        values?: {
            leaderLine?: boolean;
            textColor?: string;
            padding?: number;
            format?: string;
            //FP digits
            formatDigits?: number,
            position?: string;
            font?: {
                weight?: string;
                size?: string;
                family?: string;
            }

        }
    }
    //TRENDLINE SETTINGS
    showTrendLine ?: boolean;
    trendLine?:{
        circle?:{
            innerRadius: number;
            outerRadius: number;
            background: string;

        }
        values:{
            format: string;
            //FP digits
            formatDigits?: number,
            lines:[{
                textColor?: string;
                padding?: number;
                format?: string;
                //FP digits
                formatDigits?: number,

                font?: {
                    weight?: string;
                    size?: string;
                    family?: string;
                }}
                ];
        }
        positive?:{
            background?: string;
            opacity?: number;
        }
        negative?:{
            background?: string;
            opacity?: number;
        }
    }
    animation: {
        active?: boolean;
    }
}
