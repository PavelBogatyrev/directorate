/**
* Created by ygorenburgov on 21.03.16.
*/

interface LineChartModel {
    data: Array<{groupName: string; values:Array<number>}>;
    seriesNames: Array<string>;

}