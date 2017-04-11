/**
 * PieChartConfiguration contains:
 * @author ygorenburgov
 */

///<reference path="../legend/LegendConfiguration.ts"/>

interface LineChartConfiguration {

    defaultGridPadding: {
       top: number;
       bottom:number;
    },

    padding:
        {"left": number;
         "right": number;
         "top": number;
         "bottom": number
    },

    colors:Array<string>;

    //line settings
    line:{

        //width of line
        strokeWidth:string;

    };

    //point circle settings
    point:{

        //radius of point circle
        radius:string;

        //width of circle edge
        strokeWidth:string;

        //color inside circle
        fill:string
    };

    //point value label settings
    value:{

        //font style
        font:{

            fontFamily:string;
            fontSize:string;
            fontWeight:string;

            //stroke of font, usually it is 'none'
            stroke:string;

            //color of font
            fill:string;
        };

        //vertical margin between point and value label
        vMargin:string;

        //value format
        format: string,

        //digits after decimal point
        formatDigits: number,
        
    };

    // Base font used if is not override
    baseFont:{

        fontFamily:string;
        fontSize:string;
        fontWeight:string;
        fill:string;
    }

    //AXIS SETTINGS
    axis:{
                
        // X AXIS SETTINGS
        x:{
            //line interval 
            lineSpaceMultiplier:number;
            
            //color of font
            textColor: string;
            
            //font style 
            font:{
                fontSize: string;
                fontWeight: string;
                fontFamily: string;
            };
            
            //tick style
            tick : {
                fill: string;
                stroke: string;
                strokeWidth:string;
                shapeRendering: string;
            };

            //axe line style
            line : {
                fill: string;
                stroke: string;
                strokeWidth:string;
                shapeRendering: string;
            };

            //margin between axeLine and axe values
            textTopMargin:string;

            //margin between axe values and bottom plot area edge
            textBottomMargin:string;
        };
        // Y AXIS SETTINGS
        y:{
            
            //used to increase extent scale to move max and min values from edge inside plot
            domainExtentIncrease:number;
            
            
            //tick count on axe
            tickCount:number;

            //line spacing
            lineSpaceMultiplier:number;

            //value format
            format: string,

            //digits after decimal point
            formatDigits: number,

            //font color
            textColor: string;

            //margin between axe values and left plot area edge
            textLeftMargin:string;


            //margin between axeLine and axe values
            textRightMargin:string;

            //font style
            font:{
                fontSize: string;
                fontWeight: string;
                fontFamily: string;
            };

            //tick style
            tick : {
                fill: string;
                stroke: string;
                strokeWidth:string;
                shapeRendering: string;
            };

            //axe line style
            line : {
                fill: string;
                stroke: string;
                strokeWidth:string;
                shapeRendering: string;
            }
        }
    };
    //legend configuration is in ILegendConfiguration
    legend?: {
        show?:boolean;

        marginLeft?:number;
        marginRight?:number;
        marginTop?:number;
        marginBottom?:number;

        //Position inside widget svg :  LEFT,RIGHT,TOP,BOTTOM
        position?:string;
        configuration?:LegendConfiguration;
    };

    //animation duration
    animation: {

        duration: number;
        active: boolean;
    }


}