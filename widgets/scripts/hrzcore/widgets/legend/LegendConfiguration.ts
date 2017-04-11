/**
* LegendConfiguration contains:
    * @author ygorenburgov
*/

interface LegendConfiguration {

    //Basic legend settings
    //colors comes with data and defined in LegendModel

    //VERTICAL,HORIZONTAL
    direction?:string;

    columnCount?:number;

    backgroundColor?:string;

    width?:number;

    //The horizontal interval between 2 elements of horizontal legend or 2 columns of vertical legend (in icon sizes)
    hgap?:number;

    //The vertical interval between 2 elements of vertical legend or 2 rows of horizontal legend;
    vgap?:number;

    //Icon settings
    icon?:{
        show?:boolean;

        //RECT,CIRCLE
        shape?:string;

        //deprecated, not used - icon size calculates from real label height
        size?:number;

        style?: {
            fill?:string;
            stroke?:string;
            strokeWidth?:string;
        };
        marginLeft?:number;
    }

    //label settings
    label?:{
        show?:boolean;
        font?:{
            //color of label title font
            fill?:string;
            fontFamily?:string;
            fontSize?:string;
            fontWeight?:string;
        };
        marginLeft?:number;

    }

    //value settings
    value?:{
        show?:boolean;
        formatter?:string;
        font?:{
            //color of value title font
            fill?:string;
            fontFamily?:string;
            fontSize?:string;
            fontWeight?:string;
        };
        marginLeft?:number;

    }
}