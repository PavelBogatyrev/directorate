///<reference path="../piechart/PieChartConfiguration.ts"/>
interface BODPieChartConfiguration extends  PieChartConfiguration{


    radiusDelta?:number;


    groupTitle?: {
        show?:boolean;
        viewType?:string;
        marginRight?:number;
        marginLeft?:number;
        lineSpacing?:number;
        font?:{
            //color of chart title font
            fill?:string;
            fontFamily?:string;
            fontSize?:string;
            fontWeight?:string;
            textAnchor?: string;
        };
        background?:{
            fill?:string;
            stroke?:string;
            strokeWidth?:string;
        };
        extensionLine?:{
            stroke?:string;
            strokeWidth?:number;
            markerRadius?:number;
            hgap?:number;
        };
    }

    //settings for topGrouping of
    topGrouping?: {
        show?:boolean;
        topLabels?:boolean;
        alignPieByTopLabels?:boolean;
        topCounts?:Array<{top:number; valueCount:number}>;
        radiusStep?:number;
        formatter?:string;
        font?:{
            fill?:string;
            fontFamily?:string;
            fontSize?:string;
            fontWeight?:string;
        }

    };

}