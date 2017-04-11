/**
 * @author rlapin
 */

interface IWaterfallChartModel{
    data: Array<number>
    seriesNames: Array<string>; // series names count = data.length+1(as we should have end)
    metaData?: any;
    title?:string;
}
