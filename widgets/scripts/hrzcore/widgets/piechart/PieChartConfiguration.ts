///<reference path="../legend/LegendConfiguration.ts"/>

/**
 * PieChartConfiguration contains:
 * @author ygorenburgov
 */

interface PieChartConfiguration {

    //Options for basic PieChart

    //sorting mode for PieChart: NONE - default,ASC,DSC
    sorting?:string;

    //color palette for segments - may be overridden in PieChartModel
    colors?:Array<string>;

    //Duration of segment openning
    animationTime?:number;
    startingRadius?:number;

    animationActive?:boolean;



    //center - default, left, right
    hAlign?:string;

    //delta betwwen radiuses of different pies
    radiusDelta?:number;

    baseFont?:{
        //color of chart title font
        fontFamily?:string;
        fontSize?:string;
        fontWeight?:string;
        fill?:string;
    };

    pie?: {
        radiusKoeff?:number;
        marginLeft?:number;
        marginRight?:number;
        marginTop?:number;
        marginBottom?:number;
    };

    //Formatting for title of chart
    title?:{
        show?:boolean
        font?:{
            //color of chart title font
            fill?:string;
            fontFamily?:string;
            fontSize?:string;
            fontWeight?:string;
            textAnchor?: string;
        };
        topPadding?:number;
        bottomPadding?:number;
    };


    //Radial and circular borders of segment
    segmentBorderPath?:{
        //color of segment borders
        stroke?:string;

        //width of segment borders
        strokeWidth?:string;
    };




    // labels inside segments
    innerLabels?: {
        show?:boolean;

        //style specification of inner labels font
        font?:{
            //color of chart title font
            fill?:string;
            fontFamily?:string;
            fontSize?:string;
            fontWeight?:string;
        }
        paddingFromCircleEdge?:number;
        paddingFromCircleEdgeInner?:number
        isPercent?:boolean;
        formatter?:string;
    };


    // labels outside segments
    outerLabels?: {
        show?:boolean;

        //style specification of inner labels font
        font?:{
            //color of chart title font
            fill?:string;
            fontFamily?:string;
            fontSize?:string;
            fontWeight?:string;
        }
        isPercent?:boolean;
        formatter?:string;
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

    total?: {

        show?:boolean;

        //LEFT, RIGHT, CENTER
        position?:string;
        //Number of pixels from left edge if position = LEFT, from right edge if position = RIGHT
        edgeMargin?:number;
        bottomMargin?:number;
        formatter?:string;
        font?:{
            //color of chart title font
            fill?:string;
            fontFamily?:string;
            fontSize?:string;
            fontWeight?:string;
        }
    }


}
