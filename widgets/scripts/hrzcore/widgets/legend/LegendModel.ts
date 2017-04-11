interface LegendModel {
    data: Array<{
        label:string;
        color?:string;
        value?:number;
    }>;
    //Formatter for value if required
    formatter?:string;
}