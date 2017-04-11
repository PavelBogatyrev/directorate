package com.luxoft.horizon.dir.utils;

/**
 * @author rlapin
 */
public interface FourPracticesQueries {
    String FOUR_PRACTICES_DATA = "declare @practice varchar(50), @currency varchar(50)\n" +
            "--set @practice = 'Horizon'\n" +
            "set @currency = 'actual'\n" +
            "\n" +
            "---plan\n" +
            "SELECT\n" +
            "\tcast(practice as varchar),\n" +
            "\tsum([revenue]) as [Revenue], \n" +
            "\tsum([cm]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tcase when max(month(reportdate))>3 then max(year(reportdate)+1) else max(year(reportdate)) end as [year],\n" +
            "\tcast(currency as varchar) as varchar\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where currency = 'plan' and reporttype = 'lastExecutive' and budget = 'plan' \n" +
            "\tand (\n" +
            "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t)\n" +
            "\tand practice in ('Creative Labs','IoT', 'Atlassian and Agile', 'Big Data')\n" +
            "group by currency, practice\n" +
            "\n" +
            "---forecast\n" +
            "union all\n" +
            "SELECT\n" +
            "\tcast(practice as varchar),\n" +
            "\tsum([revenue]) as [Revenue], \n" +
            "\tsum([cm]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tcase when max(month(reportdate))>3 then max(year(reportdate)+1) else max(year(reportdate)) end as [year],\n" +
            "\tcast(currency as varchar) as varchar\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where  currency = @currency and reporttype = 'lastExecutive' and budget = 'factforecast'\n" +
            "\tand (\n" +
            "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t)\n" +
            "\tand practice in ('Creative Labs','IoT', 'Atlassian and Agile', 'Big Data')\n" +
            "group by currency, practice\n" +
            "\n" +
            "---previous FY fact\n" +
            "union all\n" +
            "SELECT\n" +
            "\tcast(practice as varchar),\n" +
            "\tsum([revenue fact previous FY]) as [Revenue], \n" +
            "\tsum([cm fact previous FY]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tcase when max(month(reportdate))>3 then max(year(reportdate)) else max(year(reportdate))-1 end as [year],\n" +
            "\tcast(currency as varchar) as varchar\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where  currency = 'actual' and reporttype = 'lastExecutive' and budget = 'Previous FY Fact'\n" +
            "\tand (\n" +
            "\t(month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) > 3) \n" +
            "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-2 and month(reportdate) <= 4) \n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) > 3)\n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4)\n" +
            "\t)\n" +
            "\tand practice in ('Creative Labs','IoT', 'Atlassian and Agile', 'Big Data')\n" +
            "group by currency, practice\n" +
            "\n" +
            "--- previous factforecast\n" +
            "union all\n" +
            "SELECT\n" +
            "\tcast(practice as varchar),\n" +
            "\tsum([Revenue Previous FactForecast]) as [Revenue], \n" +
            "\tsum([CM Previous FactForecast]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tcase when max(month(reportdate))>3 then max(year(reportdate)+1) else max(year(reportdate)) end as [year],\n" +
            "\tcast(currency as varchar) as varchar\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where  currency = 'actual' and reporttype = 'previousExecutive' and budget = 'previous factforecast'\n" +
            "\tand (\n" +
            "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t)\n" +
            "\tand practice in ('Creative Labs','IoT', 'Atlassian and Agile', 'Big Data')\n" +
            "group by currency, practice";
}
