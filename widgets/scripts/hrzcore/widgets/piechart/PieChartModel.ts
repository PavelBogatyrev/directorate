/**
 * Created by ygorenburgov on 21.03.16.
 */

interface PieChartModel {
    data: Array<{groupName?: string; values:Array<number>}>
    seriesNames?: Array<string>;
    title?:string;
    colors?:Array<string>;
    metaData?:any;
}