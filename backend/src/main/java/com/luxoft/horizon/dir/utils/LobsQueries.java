package com.luxoft.horizon.dir.utils;

/**
 * @author rlapin
 */
public interface LobsQueries {

    String HEADCOUNT = "declare @LOB varchar(50), @LOBlabel varchar(50), @currency varchar(50), @isLOB int\n" +
            "set @LOB = ?1\n" +
            "\n" +
            "set @isLOB = case when @LOB like '%FinS%' then 0 else 1 end\n" +
            "\n" +
            "select \n" +
            "\ttop 5 timeline, \n" +
            "\tcase when month(timeline) = 3 then 'Mar' when month(timeline) = 6 then 'Jun'\n" +
            "\t\twhen month(timeline) = 9 then 'Sep' when month(timeline) = 12 then 'Dec' end\n" +
            "\t+ ' ' +\n" +
            "\tcase when month(timeline)>3 then cast (year(timeline)+1 as varchar) else cast(year(timeline) as varchar) end as [MMFY],\n" +
            "\tsum(headcount_cognos) as HC FROM [FORECAST_DIRECTORATES]\n" +
            "where currency = 'actual' and reporttype = 'lastExecutive' and budget = 'factforecast' \n" +
            "\tand ((segment = @LOB and @isLOB = 0) or (LOB = @LOB and @isLOB = 1))\n" +
            "\tand month(timeline) in (3,6,9,12)\n" +
            "group by timeline\n" +
            "order by timeline desc\n";
    String YEAR = "declare @LOB varchar(50), @currency varchar(50), @isLOB int\n" +
            "set @LOB = ?1\n" +
            "set @currency = ?2\n" +
            "\n" +
            "set @isLOB = case when @LOB like '%FinS%' then 0 else 1 end\n" +
            "\n" +
            "-----------------LoB\n" +
            "\n" +
            "---plan\n" +
            "SELECT\n" +
            "\t'LOB' as KPItype,\n" +
            "\tsum([revenue]) as [Revenue], \n" +
            "\tsum([cm]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tcase when max(month(reportdate))>3 then max(year(reportdate)+1) else max(year(reportdate)) end as [year],\n" +
            "\tcast(currency as varchar) as currency\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where currency = 'plan' and reporttype = 'lastExecutive' and budget = 'plan' \n" +
            "\tand (\n" +
            "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t)\n" +
            "\tand ((segment = @LOB and @isLOB = 0) or (LOB = @LOB and @isLOB = 1))\n" +
            "\tand practice not in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps', 'Horizon' ,'IoT')\n" +
            "\t--and program <> 'Sberbank_Murex'\n" +
            "group by currency\n" +
            "\n" +
            "union all\n" +
            "---forecast\n" +
            "SELECT\n" +
            "\t'LOB' as KPItype,\n" +
            "\tsum([revenue]) as [Revenue], \n" +
            "\tsum([cm]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tcase when max(month(reportdate))>3 then max(year(reportdate)+1) else max(year(reportdate)) end as [year],\n" +
            "\tcast(currency as varchar) as currency\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where  currency = @currency and reporttype = 'lastExecutive' and budget = 'factforecast'\n" +
            "\tand (\n" +
            "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t)\n" +
            "\tand ((segment = @LOB and @isLOB = 0) or (LOB = @LOB and @isLOB = 1))\n" +
            "\tand practice not in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps', 'Horizon' ,'IoT')\n" +
            "\t--and program <> 'Sberbank_Murex'\n" +
            "group by currency\n" +
            "\n" +
            "---previous FY fact\n" +
            "union all\n" +
            "SELECT\n" +
            "\t'LOB' as KPItype,\n" +
            "\tsum([revenue fact previous FY]) as [Revenue], \n" +
            "\tsum([cm fact previous FY]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tcase when max(month(reportdate))>3 then max(year(reportdate)) else max(year(reportdate)-1) end as [year],\n" +
            "\tcast(currency as varchar) as currency\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where  currency = 'actual' and reporttype = 'lastExecutive' and budget = 'Previous FY Fact'\n" +
            "\tand (\n" +
            "\t(month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) > 3)\n" +
            "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-2 and month(reportdate) <= 4)\n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) > 3)\n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4)\n" +
            "\t)\n" +
            "\tand ((segment = @LOB and @isLOB = 0) or (LOB = @LOB and @isLOB = 1))\n" +
            "\tand practice not in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps', 'Horizon' ,'IoT')\n" +
            "\t--and program <> 'Sberbank_Murex'\n" +
            "group by currency\n" +
            "\n" +
            "--- previous factforecast\n" +
            "union all\n" +
            "SELECT\n" +
            "\t'LOB' as KPItype,\n" +
            "\tsum([Revenue Previous FactForecast]) as [Revenue], \n" +
            "\tsum([CM Previous FactForecast]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tcase when max(month(reportdate))>3 then max(year(reportdate)+1) else max(year(reportdate)) end as [year],\n" +
            "\tcast(currency as varchar) as currency\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where  currency = 'actual' and reporttype = 'previousExecutive' and budget = 'previous factforecast'\n" +
            "\tand (\n" +
            "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t)\n" +
            "\tand ((segment = @LOB and @isLOB = 0) or (LOB = @LOB and @isLOB = 1))\n" +
            "\tand practice not in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps', 'Horizon' ,'IoT')\n" +
            "\t--and program <> 'Sberbank_Murex'\n" +
            "group by currency\n" +
            "\n" +
            "\n" +
            "union all\n" +
            "---------------CoE\n" +
            "\n" +
            "---plan\n" +
            "SELECT\n" +
            "\t'CoE' as KPItype,\n" +
            "\tsum([revenue]) as [Revenue], \n" +
            "\tsum([cm]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tcase when max(month(reportdate))>3 then max(year(reportdate)+1) else max(year(reportdate)) end as [year],\n" +
            "\tcast(currency as varchar) as currency\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where currency = 'plan' and reporttype = 'lastExecutive' and budget = 'plan' \n" +
            "\tand (\n" +
            "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t)\n" +
            "\tand ((segment = @LOB and @isLOB = 0) or (LOB = @LOB and @isLOB = 1))\n" +
            "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps', 'Horizon','IoT')\n" +
            "\t--and program <> 'Sberbank_Murex'\n" +
            "group by currency\n" +
            "\n" +
            "---factforecast\n" +
            "union all\n" +
            "SELECT\n" +
            "\t'CoE' as KPItype,\n" +
            "\tsum([revenue]) as [Revenue], \n" +
            "\tsum([cm]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tcase when max(month(reportdate))>3 then max(year(reportdate)+1) else max(year(reportdate)) end as [year],\n" +
            "\tcast(currency as varchar) as currency\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where  currency = @currency and reporttype = 'lastExecutive' and budget = 'factforecast' \n" +
            "\tand (\n" +
            "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t)\n" +
            "\tand ((segment = @LOB and @isLOB = 0) or (LOB = @LOB and @isLOB = 1))\n" +
            "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps', 'Horizon','IoT')\n" +
            "\t--and program <> 'Sberbank_Murex'\n" +
            "group by currency\n" +
            "\n" +
            "---previous FY fact\n" +
            "union all\n" +
            "SELECT\n" +
            "\t'CoE' as KPItype,\n" +
            "\tsum([revenue fact previous FY]) as [Revenue], \n" +
            "\tsum([cm fact previous FY]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tcase when max(month(reportdate))>3 then max(year(reportdate)) else max(year(reportdate)-1) end as [year],\n" +
            "\tcast(currency as varchar) as currency\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where  currency = 'actual' and reporttype = 'lastExecutive' and budget = 'Previous FY Fact'\n" +
            "\tand (\n" +
            "\t(month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) > 3) \n" +
            "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-2 and month(reportdate) <= 4) \n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) > 3)\n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4)\n" +
            "\t)\n" +
            "\tand ((segment = @LOB and @isLOB = 0) or (LOB = @LOB and @isLOB = 1))\n" +
            "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps', 'Horizon','IoT')\n" +
            "\t--and program <> 'Sberbank_Murex'\n" +
            "group by currency\n" +
            "\n" +
            "--- previous factforecast\n" +
            "union all\n" +
            "SELECT\n" +
            "\t'CoE' as KPItype,\n" +
            "\tsum([Revenue Previous FactForecast]) as [Revenue], \n" +
            "\tsum([CM Previous FactForecast]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tcase when max(month(reportdate))>3 then max(year(reportdate)+1) else max(year(reportdate)) end as [year],\n" +
            "\tcast(currency as varchar) as currency\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where  currency = 'actual' and reporttype = 'previousExecutive' and budget = 'previous factforecast'\n" +
            "\tand (\n" +
            "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t)\n" +
            "\tand ((segment = @LOB and @isLOB = 0) or (LOB = @LOB and @isLOB = 1))\n" +
            "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps', 'Horizon','IoT')\n" +
            "\t--and program <> 'Sberbank_Murex'\n" +
            "group by currency\n" +
            "\n" +
            "\n";
    String MONTH = "declare @LOB varchar(50), @currency varchar(50), @isLOB int, @ReportMonth int, @ReportYear int\n" +
            "set @ReportMonth = ?1\n" +
            "set @ReportYear = ?2\n" +
            "set @LOB = ?3\n" +
            "set @currency = ?4\n" +
            "\n" +
            "set @isLOB = case when @LOB like '%FinS%' then 0 else 1 end\n" +
            "\n" +
            "-----------------LoB\n" +
            "SELECT\n" +
            "\t'LOB' as KPItype,\n" +
            "\tsum([revenue]) as [Revenue], \n" +
            "\tsum([cm]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tmax(month(timeline)) as [month],\n" +
            "\tcase when max(month(timeline))>3 then max(year(timeline)+1) else max(year(timeline)) end as [year],\n" +
            "\tcast(currency as varchar) as currency\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where currency = 'plan' and reporttype = 'lastExecutive' and budget = 'plan' \n" +
            "\tand (\n" +
            "\t\t(month(timeline) <= @ReportMonth \n" +
            "\t\tand month(timeline) >= 4) \n" +
            "\t\tand year(timeline) = @ReportYear\n" +
            "\t\t)\n" +
            "\tand ((segment = @LOB and @isLOB = 0) or (LOB = @LOB and @isLOB = 1))\n" +
            "\tand practice not in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps', 'Horizon' ,'IoT')\n" +
            "\t--and program <> 'Sberbank_Murex'\n" +
            "group by currency\n" +
            "\n" +
            "union all\n" +
            "SELECT\n" +
            "\t'LOB' as KPItype,\n" +
            "\tsum([revenue]) as [Revenue], \n" +
            "\tsum([cm]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tmax(month(timeline)) as [month],\n" +
            "\tcase when max(month(timeline))>3 then max(year(timeline)+1) else max(year(timeline)) end as [year],\n" +
            "\tcast(currency as varchar) as currency\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where  currency = @currency and reporttype = 'lastExecutive' and budget = 'fact'\n" +
            "\tand (\n" +
            "\t\t(month(timeline) <= @ReportMonth \n" +
            "\t\tand month(timeline) >= 4) \n" +
            "\t\tand year(timeline) = @ReportYear\n" +
            "\t\t)\n" +
            "\tand ((segment = @LOB and @isLOB = 0) or (LOB = @LOB and @isLOB = 1))\n" +
            "\tand practice not in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps', 'Horizon' ,'IoT')\n" +
            "\t--and program <> 'Sberbank_Murex'\n" +
            "group by currency\n" +
            "\n" +
            "union all\n" +
            "SELECT\n" +
            "\t'LOB' as KPItype,\n" +
            "\tsum([Revenue Previous FactForecast]) as [Revenue], \n" +
            "\tsum([CM Previous FactForecast]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tmax(month(timeline)) as [month],\n" +
            "\tcase when max(month(timeline))>3 then max(year(timeline)+1) else max(year(timeline)) end as [year],\n" +
            "\tcast(currency as varchar) as currency\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where  currency = 'actual' and reporttype = 'previousExecutive' and budget = 'previous factforecast'\n" +
            "\tand (\n" +
            "\t\t(month(timeline) <= @ReportMonth \n" +
            "\t\tand month(timeline) >= 4) \n" +
            "\t\tand year(timeline) = @ReportYear\n" +
            "\t\t)\n" +
            "\tand ((segment = @LOB and @isLOB = 0) or (LOB = @LOB and @isLOB = 1))\n" +
            "\tand practice not in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps', 'Horizon' ,'IoT')\n" +
            "\t--and program <> 'Sberbank_Murex'\n" +
            "group by currency\n" +
            "\n" +
            "union all\n" +
            "---------------CoE\n" +
            "SELECT\n" +
            "\t'CoE' as KPItype,\n" +
            "\tsum([revenue]) as [Revenue], \n" +
            "\tsum([cm]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tmax(month(timeline)) as [month],\n" +
            "\tcase when max(month(timeline))>3 then max(year(timeline)+1) else max(year(timeline)) end as [year],\n" +
            "\tcast(currency as varchar) as currency\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where currency = 'plan' and reporttype = 'lastExecutive' and budget = 'plan' \n" +
            "\tand (\n" +
            "\t\t(month(timeline) <= @ReportMonth \n" +
            "\t\tand month(timeline) >= 4) \n" +
            "\t\tand year(timeline) = @ReportYear\n" +
            "\t\t)\n" +
            "\tand ((segment = @LOB and @isLOB = 0) or (LOB = @LOB and @isLOB = 1))\n" +
            "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps', 'Horizon','IoT')\n" +
            "\t--and program <> 'Sberbank_Murex'\n" +
            "group by currency\n" +
            "\n" +
            "union all\n" +
            "SELECT\n" +
            "\t'CoE' as KPItype,\n" +
            "\tsum([revenue]) as [Revenue], \n" +
            "\tsum([cm]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tmax(month(timeline)) as [month],\n" +
            "\tcase when max(month(timeline))>3 then max(year(timeline)+1) else max(year(timeline)) end as [year],\n" +
            "\tcast(currency as varchar) as currency\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where  currency = @currency and reporttype = 'lastExecutive' and budget = 'fact' \n" +
            "\tand (\n" +
            "\t\t(month(timeline) <= @ReportMonth \n" +
            "\t\tand month(timeline) >= 4) \n" +
            "\t\tand year(timeline) = @ReportYear\n" +
            "\t\t)\n" +
            "\tand ((segment = @LOB and @isLOB = 0) or (LOB = @LOB and @isLOB = 1))\n" +
            "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps', 'Horizon','IoT')\n" +
            "\t--and program <> 'Sberbank_Murex'\n" +
            "group by currency\n" +
            "\n" +
            "union all\n" +
            "SELECT\n" +
            "\t'CoE' as KPItype,\n" +
            "\tsum([Revenue Previous FactForecast]) as [Revenue], \n" +
            "\tsum([CM Previous FactForecast]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tmax(month(timeline)) as [month],\n" +
            "\tcase when max(month(timeline))>3 then max(year(timeline)+1) else max(year(timeline)) end as [year],\n" +
            "\tcast(currency as varchar) as currency\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where  currency = 'actual' and reporttype = 'previousExecutive' and budget = 'previous factforecast'\n" +
            "\tand (\n" +
            "\t\t(month(timeline) <= @ReportMonth \n" +
            "\t\tand month(timeline) >= 4) \n" +
            "\t\tand year(timeline) = @ReportYear\n" +
            "\t\t)\n" +
            "\tand ((segment = @LOB and @isLOB = 0) or (LOB = @LOB and @isLOB = 1))\n" +
            "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps', 'Horizon' ,'IoT')\n" +
            "\t--and program <> 'Sberbank_Murex'\n" +
            "group by currency";
    String HORIZON_YEAR = "declare @LOB varchar(50), @currency varchar(50), @isLOB int\n" +
            "--set @LOB = 'Horizon'\n" +
            "set @currency = ?1\n" +
            "\n" +
            "---plan\n" +
            "SELECT\n" +
            "\tcase LOB when 'ENTERPRISE SOLUTIONS' then 'EntS' when 'FINANCIAL SOLUTIONS' then 'FinS' else LOB end as LOB,\n" +
            "\tsum([revenue]) as [Revenue], \n" +
            "\tsum([cm]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tcase when max(month(reportdate))>3 then max(year(reportdate)+1) else max(year(reportdate)) end as [year],\n" +
            "\tcast(currency as varchar) as currency\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where currency = 'plan' and reporttype = 'lastExecutive' and budget = 'plan' \n" +
            "\tand (\n" +
            "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t)\n" +
            "\tand practice in ('Horizon')\n" +
            "\tand LOB in ('ENTERPRISE SOLUTIONS','FINANCIAL SOLUTIONS')\n" +
            "group by currency, LOB\n" +
            "\n" +
            "---forecast\n" +
            "union all\n" +
            "SELECT\n" +
            "\tcase LOB when 'ENTERPRISE SOLUTIONS' then 'EntS' when 'FINANCIAL SOLUTIONS' then 'FinS' else LOB end as LOB,\n" +
            "\tsum([revenue]) as [Revenue], \n" +
            "\tsum([cm]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tcase when max(month(reportdate))>3 then max(year(reportdate)+1) else max(year(reportdate)) end as [year],\n" +
            "\tcast(currency as varchar) as currency\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where  currency = @currency and reporttype = 'lastExecutive' and budget = 'factforecast'\n" +
            "\tand (\n" +
            "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t)\n" +
            "\tand practice in ('Horizon')\n" +
            "\tand LOB in ('ENTERPRISE SOLUTIONS','FINANCIAL SOLUTIONS')\n" +
            "group by currency, LOB\n" +
            "\n" +
            "---previous FY fact\n" +
            "union all\n" +
            "SELECT\n" +
            "\tcase LOB when 'ENTERPRISE SOLUTIONS' then 'EntS' when 'FINANCIAL SOLUTIONS' then 'FinS' else LOB end as LOB,\n" +
            "\tsum([revenue fact previous FY]) as [Revenue], \n" +
            "\tsum([cm fact previous FY]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tcase when max(month(reportdate))>3 then max(year(reportdate)) else max(year(reportdate))-1 end as [year],\n" +
            "\tcast(currency as varchar) as currency\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where  currency = 'actual' and reporttype = 'lastExecutive' and budget = 'Previous FY Fact'\n" +
            "\tand (\n" +
            "\t(month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) > 3) \n" +
            "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-2 and month(reportdate) <= 4) \n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) > 3)\n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4)\n" +
            "\t)\n" +
            "\tand practice in ('Horizon')\n" +
            "\tand LOB in ('ENTERPRISE SOLUTIONS','FINANCIAL SOLUTIONS')\n" +
            "group by currency, LOB\n" +
            "\n" +
            "--- previous factforecast\n" +
            "union all\n" +
            "SELECT\n" +
            "\tcase LOB when 'ENTERPRISE SOLUTIONS' then 'EntS' when 'FINANCIAL SOLUTIONS' then 'FinS' else LOB end as LOB,\n" +
            "\tsum([Revenue Previous FactForecast]) as [Revenue], \n" +
            "\tsum([CM Previous FactForecast]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tcase when max(month(reportdate))>3 then max(year(reportdate)+1) else max(year(reportdate)) end as [year],\n" +
            "\tcast(currency as varchar) as currency\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where  currency = 'actual' and reporttype = 'previousExecutive' and budget = 'previous factforecast'\n" +
            "\tand (\n" +
            "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t)\n" +
            "\tand practice in ('Horizon')\n" +
            "\tand LOB in ('ENTERPRISE SOLUTIONS','FINANCIAL SOLUTIONS')\n" +
            "group by currency, LOB";
    String HORIZON_MONTH = "declare @LOB varchar(50), @currency varchar(50), @isLOB int, @ReportMonth int, @ReportYear int\n" +
            "set @ReportMonth = ?1\n" +
            "set @ReportYear = ?2\n" +
            "set @currency = ?3\n" +
            "\n" +
            "set @isLOB = case when @LOB like '%FinS%' then 0 else 1 end\n" +
            "\n" +
            "-----------------LoB\n" +
            "SELECT\n" +
            "\tcase LOB when 'ENTERPRISE SOLUTIONS' then 'EntS' when 'FINANCIAL SOLUTIONS' then 'FinS' else LOB end as LOB,\n" +
            "\tsum([revenue]) as [Revenue], \n" +
            "\tsum([cm]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tmax(month(timeline)) as [month],\n" +
            "\tcase when max(month(timeline))>3 then max(year(timeline)+1) else max(year(timeline)) end as [year],\n" +
            "\tcast(currency as varchar) as currency\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where currency = 'plan' and reporttype = 'lastExecutive' and budget = 'plan' \n" +
            "\tand (\n" +
            "\t\t(month(timeline) <= @ReportMonth \n" +
            "\t\tand month(timeline) >= 4) \n" +
            "\t\tand year(timeline) = @ReportYear\n" +
            "\t\t)\n" +
            "\tand practice in ('Horizon')\n" +
            "\tand LOB in ('ENTERPRISE SOLUTIONS','FINANCIAL SOLUTIONS')\n" +
            "\tand revenue <> 0\n" +
            "group by currency, LOB\n" +
            "\n" +
            "union all\n" +
            "SELECT\n" +
            "\tcase LOB when 'ENTERPRISE SOLUTIONS' then 'EntS' when 'FINANCIAL SOLUTIONS' then 'FinS' else LOB end as LOB,\n" +
            "\tsum([revenue]) as [Revenue], \n" +
            "\tsum([cm]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tmax(month(timeline)) as [month],\n" +
            "\tcase when max(month(timeline))>3 then max(year(timeline)+1) else max(year(timeline)) end as [year],\n" +
            "\tcast(currency as varchar) as currency\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where  currency = @currency and reporttype = 'lastExecutive' and budget = 'fact'\n" +
            "\tand (\n" +
            "\t\t(month(timeline) <= @ReportMonth  \n" +
            "\t\tand month(timeline) >= 4) \n" +
            "\t\tand year(timeline) = @ReportYear\n" +
            "\t\t)\n" +
            "\tand practice in ('Horizon')\n" +
            "\tand LOB in ('ENTERPRISE SOLUTIONS','FINANCIAL SOLUTIONS')\n" +
            "\tand revenue <> 0\n" +
            "group by currency, LOB\n" +
            "\n" +
            "union all\n" +
            "SELECT\n" +
            "\tcase LOB when 'ENTERPRISE SOLUTIONS' then 'EntS' when 'FINANCIAL SOLUTIONS' then 'FinS' else LOB end as LOB,\n" +
            "\tsum([Revenue Previous FactForecast]) as [Revenue], \n" +
            "\tsum([CM Previous FactForecast]) as [CM],\n" +
            "\tmax(budget) as budget,\n" +
            "\tmax(month(timeline)) as [month],\n" +
            "\tcase when max(month(timeline))>3 then max(year(timeline)+1) else max(year(timeline)) end as [year],\n" +
            "\tcast(currency as varchar) as currency\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where  currency = 'actual' and reporttype = 'previousExecutive' and budget = 'previous factforecast'\n" +
            "\tand (\n" +
            "\t\t(month(timeline) <= @ReportMonth  \n" +
            "\t\tand month(timeline) >= 4) \n" +
            "\t\tand year(timeline) = @ReportYear\n" +
            "\t\t)\n" +
            "\tand practice in ('Horizon')\n" +
            "\tand LOB in ('ENTERPRISE SOLUTIONS','FINANCIAL SOLUTIONS')\n" +
            "\tand revenue <> 0\n" +
            "group by currency, LOB";
        String GLOBAL_YEAR = "declare @LOB varchar(50), @currency varchar(50), @isLOB int\n" +
                "\n" +
                "set @currency = ?1\n" +
                "\n" +
                "---plan\n" +
                "SELECT\n" +
                "\tcase LOB when 'ENTERPRISE SOLUTIONS' then 'EntS' when 'TELECOM SERVICES' then 'Telecom' \n" +
                "\twhen 'AUTOMOTIVE BUSINESS' then 'Auto' when 'GLOBAL CENTERS OF EXPERTISE' then 'GCoEs NewB'\n" +
                "\telse LOB end as LOB,\n" +
                "\tsum([revenue]) as [Revenue], \n" +
                "\tsum([cm]) as [CM],\n" +
                "\tmax(budget) as budget,\n" +
                "\tcase when max(month(reportdate))>3 then max(year(reportdate)+1) else max(year(reportdate)) end as [year],\n" +
                "\tcast(currency as varchar) as currency\n" +
                "FROM [FORECAST_DIRECTORATES]\n" +
                "where currency = 'plan' and reporttype = 'lastExecutive' and budget = 'plan'\n" +
                "\tand (\n" +
                "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3)\n" +
                "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4)\n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
                "\t)\n" +
                "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps' ,'IoT')\n" +
                "\tand LOB not in ('COST CENTERS','FINANCIAL SOLUTIONS')\n" +
                "group by currency, LOB\n" +
                "\n" +
                "------------------segments\n" +
                "union all\n" +
                "SELECT\n" +
                "\tcase Segment when  'FinS Segment 2' then 'FinS2' \n" +
                "\twhen 'FinS Segment X' then 'FinX'\n" +
                "\telse Segment end as LOB,\n" +
                "\tsum([revenue]) as [Revenue], \n" +
                "\tsum([cm]) as [CM],\n" +
                "\tmax(budget) as budget,\n" +
                "\tcase when max(month(reportdate))>3 then max(year(reportdate)+1) else max(year(reportdate)) end as [year],\n" +
                "\tcast(currency as varchar) as currency\n" +
                "FROM [FORECAST_DIRECTORATES]\n" +
                "where currency = 'plan' and reporttype = 'lastExecutive' and budget = 'plan'\n" +
                "\tand (\n" +
                "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3)\n" +
                "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4)\n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
                "\t)\n" +
                "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps' ,'IoT')\n" +
                "\tand segment in ('FinS Segment 2','FinS Segment X')\n" +
                "group by currency, segment\n" +
                "\n" +
                "---forecast\n" +
                "union all\n" +
                "SELECT\n" +
                "\tcase LOB when 'ENTERPRISE SOLUTIONS' then 'EntS' when 'TELECOM SERVICES' then 'Telecom' \n" +
                "\twhen 'AUTOMOTIVE BUSINESS' then 'Auto' when 'GLOBAL CENTERS OF EXPERTISE' then 'GCoEs NewB'\n" +
                "\telse LOB end as LOB,\n" +
                "\tsum([revenue]) as [Revenue], \n" +
                "\tsum([cm]) as [CM],\n" +
                "\tmax(budget) as budget,\n" +
                "\tcase when max(month(reportdate))>3 then max(year(reportdate)+1) else max(year(reportdate)) end as [year],\n" +
                "\tcast(currency as varchar) as currency\n" +
                "FROM [FORECAST_DIRECTORATES]\n" +
                "where  currency = @currency and reporttype = 'lastExecutive' and budget = 'factforecast'\n" +
                "\tand (\n" +
                "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
                "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
                "\t)\n" +
                "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps' ,'IoT')\n" +
                "\tand LOB not in ('COST CENTERS','FINANCIAL SOLUTIONS')\n" +
                "group by currency, LOB\n" +
                "\n" +
                "------------segments\n" +
                "union all\n" +
                "SELECT\n" +
                "\tcase Segment when  'FinS Segment 2' then 'FinS2' \n" +
                "\twhen 'FinS Segment X' then 'FinX'\n" +
                "\telse Segment end as LOB,\n" +
                "\tsum([revenue]) as [Revenue], \n" +
                "\tsum([cm]) as [CM],\n" +
                "\tmax(budget) as budget,\n" +
                "\tcase when max(month(reportdate))>3 then max(year(reportdate)+1) else max(year(reportdate)) end as [year],\n" +
                "\tcast(currency as varchar) as currency\n" +
                "FROM [FORECAST_DIRECTORATES]\n" +
                "where  currency = @currency and reporttype = 'lastExecutive' and budget = 'factforecast'\n" +
                "\tand (\n" +
                "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
                "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
                "\t)\n" +
                "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps' ,'IoT')\n" +
                "\tand segment in ('FinS Segment 2','FinS Segment X')\n" +
                "group by currency, segment\n" +
                "\n" +
                "---previous FY fact\n" +
                "union all\n" +
                "SELECT\n" +
                "\tcase LOB when 'ENTERPRISE SOLUTIONS' then 'EntS' when 'TELECOM SERVICES' then 'Telecom' \n" +
                "\twhen 'AUTOMOTIVE BUSINESS' then 'Auto' when 'GLOBAL CENTERS OF EXPERTISE' then 'GCoEs NewB'\n" +
                "\telse LOB end as LOB,\n" +
                "\tsum([revenue fact previous FY]) as [Revenue], \n" +
                "\tsum([cm fact previous FY]) as [CM],\n" +
                "\tmax(budget) as budget,\n" +
                "\tcase when max(month(reportdate))>3 then max(year(reportdate)) else max(year(reportdate))-1 end as [year],\n" +
                "\tcast(currency as varchar) as currency\n" +
                "FROM [FORECAST_DIRECTORATES]\n" +
                "where  currency = 'actual' and reporttype = 'lastExecutive' and budget = 'Previous FY Fact'\n" +
                "\tand (\n" +
                "\t(month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) > 3) \n" +
                "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-2 and month(reportdate) <= 4) \n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) > 3)\n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4)\n" +
                "\t)\n" +
                "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps' ,'IoT')\n" +
                "\tand LOB not in ('COST CENTERS','FINANCIAL SOLUTIONS')\n" +
                "group by currency, LOB\n" +
                "\n" +
                "---------------segments\n" +
                "union all\n" +
                "SELECT\n" +
                "\tcase Segment when  'FinS Segment 2' then 'FinS2' \n" +
                "\twhen 'FinS Segment X' then 'FinX'\n" +
                "\telse Segment end as LOB,\n" +
                "\tsum([revenue fact previous FY]) as [Revenue], \n" +
                "\tsum([cm fact previous FY]) as [CM],\n" +
                "\tmax(budget) as budget,\n" +
                "\tcase when max(month(reportdate))>3 then max(year(reportdate)) else max(year(reportdate))-1 end as [year],\n" +
                "\tcast(currency as varchar) as currency\n" +
                "FROM [FORECAST_DIRECTORATES]\n" +
                "where  currency = 'actual' and reporttype = 'lastExecutive' and budget = 'Previous FY Fact'\n" +
                "\tand (\n" +
                "\t(month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) > 3) \n" +
                "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-2 and month(reportdate) <= 4) \n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) > 3)\n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4)\n" +
                "\t)\n" +
                "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps' ,'IoT')\n" +
                "\tand segment in ('FinS Segment 2','FinS Segment X')\n" +
                "group by currency, segment\n" +
                "\n" +
                "--- previous factforecast\n" +
                "union all\n" +
                "SELECT\n" +
                "\tcase LOB when 'ENTERPRISE SOLUTIONS' then 'EntS' when 'TELECOM SERVICES' then 'Telecom' \n" +
                "\twhen 'AUTOMOTIVE BUSINESS' then 'Auto' when 'GLOBAL CENTERS OF EXPERTISE' then 'GCoEs NewB'\n" +
                "\telse LOB end as LOB,\n" +
                "\tsum([Revenue Previous FactForecast]) as [Revenue], \n" +
                "\tsum([CM Previous FactForecast]) as [CM],\n" +
                "\tmax(budget) as budget,\n" +
                "\tcase when max(month(reportdate))>3 then max(year(reportdate)+1) else max(year(reportdate)) end as [year],\n" +
                "\tcast(currency as varchar) as currency\n" +
                "FROM [FORECAST_DIRECTORATES]\n" +
                "where  currency = 'actual' and reporttype = 'previousExecutive' and budget = 'previous factforecast'\n" +
                "\tand (\n" +
                "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
                "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
                "\t)\n" +
                "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps' ,'IoT')\n" +
                "\tand LOB not in ('COST CENTERS','FINANCIAL SOLUTIONS')\n" +
                "group by currency, LOB\n" +
                "\n" +
                "--------------segments\n" +
                "union all\n" +
                "SELECT\n" +
                "\tcase Segment when  'FinS Segment 2' then 'FinS2' \n" +
                "\twhen 'FinS Segment X' then 'FinX'\n" +
                "\telse Segment end as LOB,\n" +
                "\tsum([Revenue Previous FactForecast]) as [Revenue], \n" +
                "\tsum([CM Previous FactForecast]) as [CM],\n" +
                "\tmax(budget) as budget,\n" +
                "\tcase when max(month(reportdate))>3 then max(year(reportdate)+1) else max(year(reportdate)) end as [year],\n" +
                "\tcast(currency as varchar) as currency\n" +
                "FROM [FORECAST_DIRECTORATES]\n" +
                "where  currency = 'actual' and reporttype = 'previousExecutive' and budget = 'previous factforecast'\n" +
                "\tand (\n" +
                "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
                "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
                "\t)\n" +
                "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps' ,'IoT')\n" +
                "\tand segment in ('FinS Segment 2','FinS Segment X')\n" +
                "group by currency, segment";
        String GLOBAL_MONTH = "declare @LOB varchar(50), @currency varchar(50), @isLOB int, @ReportMonth int, @ReportYear int\n" +
                "set @ReportMonth = ?1\n" +
                "set @ReportYear = ?2\n" +
                "set @currency = ?3\n" +
                "\n" +
                "---plan\n" +
                "SELECT\n" +
                "\tcase LOB when 'ENTERPRISE SOLUTIONS' then 'EntS' when 'TELECOM SERVICES' then 'Telecom' \n" +
                "\twhen 'AUTOMOTIVE BUSINESS' then 'Auto' when 'GLOBAL CENTERS OF EXPERTISE' then 'GCoEs NewB'\n" +
                "\telse LOB end as LOB,\n" +
                "\tsum([revenue]) as [Revenue], \n" +
                "\tsum([cm]) as [CM],\n" +
                "\tmax(budget) as budget,\n" +
                "\tmax(month(timeline)) as [month],\n" +
                "\tcase when max(month(timeline))>3 then max(year(timeline)+1) else max(year(timeline)) end as [year],\n" +
                "\tcast(currency as varchar) as currency\n" +
                "FROM [FORECAST_DIRECTORATES]\n" +
                "where currency = 'plan' and reporttype = 'lastExecutive' and budget = 'plan' \n" +
                "\tand (\n" +
                "\t\t(month(timeline) <= @ReportMonth \n" +
                "\t\tand month(timeline) >= 4) \n" +
                "\t\tand year(timeline) = @ReportYear\n" +
                "\t\t)\n" +
                "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps' ,'IoT')\n" +
                "\tand LOB not in ('COST CENTERS','FINANCIAL SOLUTIONS')\n" +
                "group by currency, LOB\n" +
                "\n" +
                "----------segment\n" +
                "union all\n" +
                "SELECT\n" +
                "\tcase Segment when  'FinS Segment 2' then 'FinS2' \n" +
                "\twhen 'FinS Segment X' then 'FinX'\n" +
                "\telse Segment end as LOB,\n" +
                "\tsum([revenue]) as [Revenue], \n" +
                "\tsum([cm]) as [CM],\n" +
                "\tmax(budget) as budget,\n" +
                "\tmax(month(timeline)) as [month],\n" +
                "\tcase when max(month(timeline))>3 then max(year(timeline)+1) else max(year(timeline)) end as [year],\n" +
                "\tcast(currency as varchar) as currency\n" +
                "FROM [FORECAST_DIRECTORATES]\n" +
                "where currency = 'plan' and reporttype = 'lastExecutive' and budget = 'plan' \n" +
                "\tand (\n" +
                "\t\t(month(timeline) <= @ReportMonth  \n" +
                "\t\tand month(timeline) >= 4) \n" +
                "\t\tand year(timeline) = @ReportYear\n" +
                "\t\t)\n" +
                "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps' ,'IoT')\n" +
                "\tand segment in ('FinS Segment 2','FinS Segment X')\n" +
                "group by currency, segment\n" +
                "\n" +
                "\n" +
                "---fact\n" +
                "union all\n" +
                "SELECT\n" +
                "\tcase LOB when 'ENTERPRISE SOLUTIONS' then 'EntS' when 'TELECOM SERVICES' then 'Telecom' \n" +
                "\twhen 'AUTOMOTIVE BUSINESS' then 'Auto' when 'GLOBAL CENTERS OF EXPERTISE' then 'GCoEs NewB'\n" +
                "\telse LOB end as LOB,\n" +
                "\tsum([revenue]) as [Revenue], \n" +
                "\tsum([cm]) as [CM],\n" +
                "\tmax(budget) as budget,\n" +
                "\tmax(month(timeline)) as [month],\n" +
                "\tcase when max(month(timeline))>3 then max(year(timeline)+1) else max(year(timeline)) end as [year],\n" +
                "\tcast(currency as varchar) as currency\n" +
                "FROM [FORECAST_DIRECTORATES]\n" +
                "where  currency = @currency and reporttype = 'lastExecutive' and budget = 'fact'\n" +
                "\tand (\n" +
                "\t\t(month(timeline) <= @ReportMonth  \n" +
                "\t\tand month(timeline) >= 4) \n" +
                "\t\tand year(timeline) = @ReportYear\n" +
                "\t\t)\n" +
                "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps' ,'IoT')\n" +
                "\tand LOB not in ('COST CENTERS','FINANCIAL SOLUTIONS')\n" +
                "group by currency, LOB\n" +
                "\n" +
                "---------------segment\n" +
                "union all\n" +
                "SELECT\n" +
                "\tcase Segment when  'FinS Segment 2' then 'FinS2' \n" +
                "\twhen 'FinS Segment X' then 'FinX'\n" +
                "\telse Segment end as LOB,\n" +
                "\tsum([revenue]) as [Revenue], \n" +
                "\tsum([cm]) as [CM],\n" +
                "\tmax(budget) as budget,\n" +
                "\tmax(month(timeline)) as [month],\n" +
                "\tcase when max(month(timeline))>3 then max(year(timeline)+1) else max(year(timeline)) end as [year],\n" +
                "\tcast(currency as varchar) as currency\n" +
                "FROM [FORECAST_DIRECTORATES]\n" +
                "where  currency = @currency and reporttype = 'lastExecutive' and budget = 'fact'\n" +
                "\tand (\n" +
                "\t\t(month(timeline) <= @ReportMonth \n" +
                "\t\tand month(timeline) >= 4) \n" +
                "\t\tand year(timeline) = @ReportYear\n" +
                "\t\t)\n" +
                "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps' ,'IoT')\n" +
                "\tand segment in ('FinS Segment 2','FinS Segment X')\n" +
                "group by currency, segment\n" +
                "\n" +
                "---previous factforecast\n" +
                "union all\n" +
                "SELECT\n" +
                "\tcase LOB when 'ENTERPRISE SOLUTIONS' then 'EntS' when 'TELECOM SERVICES' then 'Telecom' \n" +
                "\twhen 'AUTOMOTIVE BUSINESS' then 'Auto' when 'GLOBAL CENTERS OF EXPERTISE' then 'GCoEs NewB'\n" +
                "\telse LOB end as LOB,\n" +
                "\tsum([Revenue Previous FactForecast]) as [Revenue], \n" +
                "\tsum([CM Previous FactForecast]) as [CM],\n" +
                "\tmax(budget) as budget,\n" +
                "\tmax(month(timeline)) as [month],\n" +
                "\tcase when max(month(timeline))>3 then max(year(timeline)+1) else max(year(timeline)) end as [year],\n" +
                "\tcast(currency as varchar) as currency\n" +
                "FROM [FORECAST_DIRECTORATES]\n" +
                "where  currency = 'actual' and reporttype = 'previousExecutive' and budget = 'previous factforecast'\n" +
                "\tand (\n" +
                "\t\t(month(timeline) <= @ReportMonth \n" +
                "\t\tand month(timeline) >= 4) \n" +
                "\t\tand year(timeline) = @ReportYear\n" +
                "\t\t)\n" +
                "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps' ,'IoT')\n" +
                "\tand LOB not in ('COST CENTERS','FINANCIAL SOLUTIONS')\n" +
                "group by currency, LOB\n" +
                "\n" +
                "---------segment\n" +
                "union all\n" +
                "SELECT\n" +
                "\tcase Segment when  'FinS Segment 2' then 'FinS2' \n" +
                "\twhen 'FinS Segment X' then 'FinX'\n" +
                "\telse Segment end as LOB,\n" +
                "\tsum([Revenue Previous FactForecast]) as [Revenue], \n" +
                "\tsum([CM Previous FactForecast]) as [CM],\n" +
                "\tmax(budget) as budget,\n" +
                "\tmax(month(timeline)) as [month],\n" +
                "\tcase when max(month(timeline))>3 then max(year(timeline)+1) else max(year(timeline)) end as [year],\n" +
                "\tcast(currency as varchar) as currency\n" +
                "FROM [FORECAST_DIRECTORATES]\n" +
                "where  currency = 'actual' and reporttype = 'previousExecutive' and budget = 'previous factforecast'\n" +
                "\tand (\n" +
                "\t\t(month(timeline) <= @ReportMonth \n" +
                "\t\tand month(timeline) >= 4) \n" +
                "\t\tand year(timeline) = @ReportYear\n" +
                "\t\t)\n" +
                "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps' ,'IoT')\n" +
                "\tand segment in ('FinS Segment 2','FinS Segment X')\n" +
                "group by currency, segment\n" +
                "\n";
}
