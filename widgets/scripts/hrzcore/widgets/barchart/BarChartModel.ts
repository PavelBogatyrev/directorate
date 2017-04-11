/**
 * @author rlapin
 */

interface IBarChartModel{
    data: Array<{groupName: string; values:Array<number>}>
    seriesNames: Array<string>;
    title?:string;
    //TEMP
    metaData2?: any;
    metaData?: any;
}
