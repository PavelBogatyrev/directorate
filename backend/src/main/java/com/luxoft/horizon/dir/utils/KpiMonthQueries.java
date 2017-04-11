package com.luxoft.horizon.dir.utils;

/**
 * @author rlapin
 */
public interface KpiMonthQueries {
    // kpiMonth
    String DATE_FILTER11 = "and (month(timeline) <= ?1\n" +
            "and month(timeline) >= 4) \n" +
            "and year(timeline) = ?2";
    String DATE_FILTER12 = "and (month(timeline) <= ?1\n" +
            "and month(timeline) >= 4) \n" +
            "and year(timeline) = ?2";
    String DATE_FILTER13 = "and (month(timeline) <= ?1\n" +
            "and month(timeline) >= 4) \n" +
            "and year(timeline) = ?2";
    String MONTH_YEAR_SELECTION = "max(month(timeline)) as [month], case when max(month(timeline))>3 then max(year(timeline))+1 else max(year(timeline)) end as [year]\n";

    String KPI_MONTH = "select sum([revenue plan]) as rfc,sum([EBITDA plan]) as efc ,sum([CM]) as cmfc,sum([Net Income plan]) as nifc, " +
            MONTH_YEAR_SELECTION +
            "from FORECAST_DIRECTORATES " +
            "where 1 = 1" +
            "and currency = 'plan' " +
            "and reporttype = 'lastExecutive' " +
            "and budget = 'plan' " +
            DATE_FILTER11 +
            " UNION ALL " +
            "select sum([Revenue Fact]) as rfc,sum([EBITDA Fact]) as efc,sum([CM Fact]) as cmfc,sum([Net Income Fact]) as nifc," +
            MONTH_YEAR_SELECTION +
            "from FORECAST_DIRECTORATES " +
            "where 1 = 1" +
            "and currency = 'actual' " +
            "and reporttype = 'lastExecutive' " +
            "and budget = 'fact' " +
            DATE_FILTER12 + " UNION ALL " +
            "select sum([Revenue Previous FactForecast]) as rfc,sum([EBITDA Previous FactForecast]) as efc,sum([CM Previous FactForecast]) as cmfc,sum([Net Income Previous FactForecast]) as nifc," +
            MONTH_YEAR_SELECTION +
            "from FORECAST_DIRECTORATES " +
            "where 1 = 1" +
            "and currency = 'actual' " +
            "and reporttype = 'previousExecutive' " +
            "and budget = 'previous factforecast' " +
            DATE_FILTER13;
}
