package com.luxoft.horizon.dir.utils;

/**
 * @author rlapin
 */
public interface ReportPeriodQueries {

    String REPORT_PERIODS = "select convert(date,dateadd(month,1,(select max(timeline) FROM [FORECAST_DIRECTORATES] where budget = 'fact')),102) as ReportDate\n" +
            "union\n" +
            "select convert(date,(select max(timeline) FROM [FORECAST_DIRECTORATES] where budget = 'fact'),102) as ReportDate\n" +
            "union\n" +
            "select convert(date,dateadd(month,-1,(select max(timeline) FROM [FORECAST_DIRECTORATES] where budget = 'fact')),102) as ReportDate";
}
