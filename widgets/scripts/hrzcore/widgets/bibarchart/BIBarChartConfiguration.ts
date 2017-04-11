///<reference path="../legend/LegendConfiguration.ts"/>
/**
 * BarChartConfiguration contains:
 * - Grid settings(Show grid/or not, grid color and so on)
 * @author rlapin
 */
interface BIBarChartConfiguration extends BarChartConfiguration {
    lineChart?: {
        padding: number;
        relSize: number; // size relative to barchart
        colors: Array<string>;
        figures: Array<{
            type: string;
            size: number;
            color: string;
        }>;
        values: {
            font?: {
                weight?: string;
                size?: string;
                family?: string;

            }
            format?: string;
            formatDigits?: number;
        }
    },

    shadowBarColors?: Array<string>;


}
