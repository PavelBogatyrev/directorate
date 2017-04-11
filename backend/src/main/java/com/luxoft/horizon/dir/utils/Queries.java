package com.luxoft.horizon.dir.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author rlapin
 */
public interface Queries {




    // kpiYear
    String DATE_FILTER21 = "and ((month(timeline) >= 4 and year(timeline)= year(reportdate) - 1 and month(reportdate) > 3)" +
            "or (month(timeline) >= 4 and year(timeline)= year(reportdate)-2 and month(reportdate) <= 4)" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) > 3)" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4))";
    String DATE_FILTER22 = "and ((month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3)" +
            "or (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= '04')" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4))";
    String DATE_FILTER23 = "and ((month(timeline) >= 4  and year(timeline)= year(reportdate) and month(reportdate) > 2)" +
            "or (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 3)" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 2)" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 3))";
    String KPI_YEAR = "SELECT sum([revenue]) as revenue,sum([CM]) as cxzc,sum([EBITDA]) as dsad,sum([Net income]) as zxc, max(year(timeline)) as [year]\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where currency = 'actual' and reporttype = 'lastExecutive' and budget = 'plan' \n" +
            "and (\n" +
            "(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "or (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            ")" +
            "UNION ALL " +
            "SELECT sum([revenue]),sum([CM]),sum([EBITDA]),sum([Net income]), max(year(timeline)) as [year]\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where currency = 'actual' and reporttype = 'lastExecutive' and budget = 'factforecast' \n" +
            "and (\n" +
            "(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "or (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            ")\n" +
            "UNION ALL " +
            "SELECT sum([revenue]) as revenue,sum([CM]) as ds,sum([EBITDA]) as revenue,sum([Net income]) as das, max(year(timeline)) as [year]\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where currency = 'actual' and reporttype = 'lastExecutive' and budget = 'factforecast' \n" +
            "and (\n" +
            "(month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) > 3) \n" +
            "or (month(timeline) >= 4 and year(timeline)= year(reportdate)-2 and month(reportdate) <= 4) \n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) > 3)\n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4)\n" +
            ")";




    String REVENUE_CLIENTS = "SELECT \n" +
            "top 10 cast(fd.client as varchar), sum(fd.revenue) as revenue, max(pfy.revenue) as revenuePrevFY, sum(fd.CM) as CM, max(pfy.CM) as CMPrevFY, max(year(fd.timeline)) as [currentFY], max(pfy.[year]) as prevFY\n" +
            "FROM [FORECAST_DIRECTORATES] fd\n" +
            "left join \n" +
            "(-- revenue&CM previous year\n" +
            "SELECT \n" +
            "client, sum([revenue fact previous FY]) as revenue, sum([CM fact previous FY]) as CM, max(year(timeline)) as [year]\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where currency = 'actual' and reporttype = 'lastExecutive' and budget = 'Previous FY Fact' \n" +
            "and (\n" +
            "(month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) > 3) \n" +
            "or (month(timeline) >= 4 and year(timeline)= year(reportdate)-2 and month(reportdate) <= 4) \n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) > 3)\n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4)\n" +
            ")\n" +
            "and client <> 'Not Defined'\n" +
            "group by Client\n" +
            ") PFY on pfy.Client=fd.client\n" +
            "where fd.currency = 'actual' and fd.reporttype = 'lastExecutive' and fd.budget = 'FactForecast' \n" +
            "and (\n" +
            "(month(fd.timeline) >= 4 and year(fd.timeline)= year(fd.reportdate) and month(fd.reportdate) > 3) \n" +
            "or (month(fd.timeline) >= 4 and year(fd.timeline)= year(fd.reportdate)-1 and month(fd.reportdate) <= 4) \n" +
            "or (month(fd.timeline) < 4 and year(fd.timeline)= year(fd.reportdate)+1 and month(fd.reportdate) > 3)\n" +
            "or (month(fd.timeline) < 4 and year(fd.timeline)= year(fd.reportdate) and month(fd.reportdate) <= 4)\n" +
            ")\n" +
            "and fd.client <> 'Not Defined'\n" +
            "group by fd.Client\n" +
            "order by sum(fd.revenue) desc";

    String KPI_BY_LOBS_REVENUE_CM_FORECAST_CURRENT_YEAR_BIG = "with [V_FORECAST_DIRECTORATES] (LOB, revenue, cm, timeline, reportdate, budget, reporttype, currency)\n" +
            "\tas\n" +
            "\t(\t\t\n" +
            "\tSELECT \n" +
            "\t\tcase when segment in ('FinS Segment X','FinS Segment 2','FinS2 Horizon') then segment else LOB end as LOB,\n" +
            "\t\t[revenue], \n" +
            "\t\t[cm],\n" +
            "\t\ttimeline, reportdate, budget, reporttype, currency\n" +
            "\tFROM [FORECAST_DIRECTORATES]\n" +
            "\twhere reporttype = 'lastExecutive' \n" +
            "\t)\n" +
            "\n" +
            "SELECT\n" +
            "\tcase when LOB = 'FinS Segment X' then 1 when LOB = 'FinS Segment 2' then 2 when LOB = 'AUTOMOTIVE BUSINESS' then 3 \n" +
            "\twhen LOB = 'ENTERPRISE SOLUTIONS' then 4 when LOB = 'TELECOM SERVICES' then 5 when LOB = 'EXCELIAN' then 6 when LOB = 'FinS2 Horizon' then 7 \n" +
            "\twhen LOB = 'GLOBAL CENTERS OF EXPERTISE' then 8 end as sortOrder,\n" +
            "\tcase when LOB = 'FinS Segment X' then 'FinX' when LOB = 'FinS Segment 2' then 'FinS S2' when LOB = 'AUTOMOTIVE BUSINESS' then 'Automotive'\n" +
            "\twhen LOB = 'ENTERPRISE SOLUTIONS' then 'EntS' when LOB = 'TELECOM SERVICES' then 'TelecomS' when LOB = 'EXCELIAN' then 'Excelian' \n" +
            "\twhen LOB = 'FinS2 Horizon' then 'Horizon' when LOB = 'GLOBAL CENTERS OF EXPERTISE' then 'GCoEs' end as LOB,\n" +
            "\tsum([revenue]) as revenue, \n" +
            "\tsum([cm]) as CM,\n" +
            "\tmax(budget) as budget,\n" +
            "\tmax(year(timeline)) as [year]\n" +
            "FROM [V_FORECAST_DIRECTORATES]\n" +
            "where currency = 'actual' and reporttype = 'lastExecutive' and budget = 'factforecast' \n" +
            "\tand (\n" +
            "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t)\n" +
            "\tand LOB in ('AUTOMOTIVE BUSINESS','ENTERPRISE SOLUTIONS','TELECOM SERVICES','EXCELIAN', 'FinS Segment X','FinS Segment 2')\n" +
            "\n" +
            "group by LOB\n" +
            "order by 1\n" +
            "\n" +
            "\n";

    String KPI_BY_LOBS_REVENUE_CM_FORECAST_CURRENT_YEAR_SMALL = "\n" +
            "with [V_FORECAST_DIRECTORATES] (LOB,practice, revenue, cm, timeline, reportdate, budget, reporttype, currency)\n" +
            "\tas\n" +
            "\t(\t\t\n" +
            "\tSELECT \n" +
            "\t\tcase when segment in ('FinS Segment X','FinS Segment 2','FinS2 Horizon') then segment else LOB end as LOB,\n" +
            "\t\tpractice,\n" +
            "\t\t[revenue], \n" +
            "\t\t[cm],\n" +
            "\t\ttimeline, reportdate, budget, reporttype, currency\n" +
            "\tFROM [FORECAST_DIRECTORATES]\n" +
            "\twhere reporttype = 'lastExecutive' \n" +
            "\t)\n" +
            "\n" +
            "SELECT\n" +
            "\t1 as sortOrder,\n" +
            "\t'GCoEs' as LOB,\n" +
            "\tsum([revenue]) as revenue, \n" +
            "\tsum([cm]) as CM,\n" +
            "\tmax(budget) as budget,\n" +
            "\tmax(year(timeline)) as [year]\n" +
            "FROM [V_FORECAST_DIRECTORATES]\n" +
            "where currency = 'actual' and reporttype = 'lastExecutive' and budget = 'factforecast' \n" +
            "\tand (\n" +
            "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t)\n" +
            "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\tand LOB <> 'COST CENTERS'\n" +
            "union all\n" +
            "SELECT\n" +
            "\t2 as sortOrder,\n" +
            "\t'Horizon' as LOB,\n" +
            "\tsum([revenue]) as revenue, \n" +
            "\tsum([cm]) as CM,\n" +
            "\tmax(budget) as budget,\n" +
            "\tmax(year(timeline)) as [year]\n" +
            "FROM [V_FORECAST_DIRECTORATES]\n" +
            "where currency = 'actual' and reporttype = 'lastExecutive' and budget = 'factforecast'\n" +
            "\tand (\n" +
            "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t)\n" +
            "\tand practice = 'Horizon'\n" +
            "\tand LOB <> 'COST CENTERS'\n" +
            "order by 1";


            String KPI_BY_LOBS_REVENUE_CM_FACT_PREVIOUS_YEAR_BIG = "with [V_FORECAST_DIRECTORATES] (LOB, [revenue fact previous FY], [cm fact previous FY], timeline, reportdate, budget, reporttype, currency)\n" +
                    "\tas\n" +
                    "\t(\t\t\n" +
                    "\tSELECT \n" +
                    "\t\tcase when segment in ('FinS Segment X','FinS Segment 2','FinS2 Horizon') then segment else LOB end as LOB,\n" +
                    "\t\t[revenue fact previous FY], \n" +
                    "\t\t[CM fact Previous FY],\n" +
                    "\t\ttimeline, reportdate, budget, reporttype, currency\n" +
                    "\tFROM [FORECAST_DIRECTORATES]\n" +
                    "\twhere reporttype = 'lastExecutive' and budget = 'Previous FY Fact'\n" +
                    "\t)\n" +
                    "\n" +
                    "SELECT\n" +
                    "\tcase when LOB = 'FinS Segment X' then 1 when LOB = 'FinS Segment 2' then 2 when LOB = 'AUTOMOTIVE BUSINESS' then 3 \n" +
                    "\twhen LOB = 'ENTERPRISE SOLUTIONS' then 4 when LOB = 'TELECOM SERVICES' then 5 when LOB = 'EXCELIAN' then 6 when LOB = 'FinS2 Horizon' then 7 \n" +
                    "\twhen LOB = 'GLOBAL CENTERS OF EXPERTISE' then 8 end as sortOrder,\n" +
                    "\tcase when LOB = 'FinS Segment X' then 'FinX' when LOB = 'FinS Segment 2' then 'FinS S2' when LOB = 'AUTOMOTIVE BUSINESS' then 'Automotive'\n" +
                    "\twhen LOB = 'ENTERPRISE SOLUTIONS' then 'EntS' when LOB = 'TELECOM SERVICES' then 'TelecomS' when LOB = 'EXCELIAN' then 'Excelian' \n" +
                    "\twhen LOB = 'FinS2 Horizon' then 'Horizon' when LOB = 'GLOBAL CENTERS OF EXPERTISE' then 'GCoEs' end as LOB,\n" +
                    "\tsum([revenue fact previous FY]) as revenue, \n" +
                    "\tsum([CM fact Previous FY]) as CM,\n" +
                    "\tmax(budget) as budget,\n" +
                    "\tmax(year(timeline)) as [year]\n" +
                    "FROM [V_FORECAST_DIRECTORATES]\n" +
                    "where currency = 'actual' and reporttype = 'lastExecutive'\n" +
                    "\tand (\n" +
                    "\t(month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) > 3) \n" +
                    "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-2 and month(reportdate) <= 4) \n" +
                    "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) > 3)\n" +
                    "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4)\n" +
                    "\t)\n" +
                    "\tand LOB in ('AUTOMOTIVE BUSINESS','ENTERPRISE SOLUTIONS','TELECOM SERVICES','EXCELIAN', 'FinS Segment X','FinS Segment 2')\n" +
                    "\n" +
                    "group by LOB\n" +
                    "order by 1";

    String KPI_BY_LOBS_REVENUE_CM_FACT_PREVIOUS_YEAR_SMALL = "\n" +
            "with [V_FORECAST_DIRECTORATES] (LOB,practice, [revenue fact previous FY], [cm fact previous FY], timeline, reportdate, budget, reporttype, currency)\n" +
            "\tas\n" +
            "\t(\t\t\n" +
            "\tSELECT \n" +
            "\t\tcase when segment in ('FinS Segment X','FinS Segment 2','FinS2 Horizon') then segment else LOB end as LOB,\n" +
            "\t\tpractice,\n" +
            "\t\t[revenue fact previous FY], \n" +
            "\t\t[cm fact previous FY],\n" +
            "\t\ttimeline, reportdate, budget, reporttype, currency\n" +
            "\tFROM [FORECAST_DIRECTORATES]\n" +
            "\twhere reporttype = 'lastExecutive' and budget = 'Previous FY Fact'\n" +
            "\t)\n" +
            "\n" +
            "SELECT\n" +
            "\t1 as sortOrder,\n" +
            "\t'GCoEs' as LOB,\n" +
            "\tsum([revenue fact previous FY]) as revenue, \n" +
            "\tsum([cm fact previous FY]) as CM,\n" +
            "\tmax(budget) as budget,\n" +
            "\tmax(year(timeline)) as [year]\n" +
            "FROM [V_FORECAST_DIRECTORATES]\n" +
            "where currency = 'actual' and reporttype = 'lastExecutive' \n" +
            "\tand (\n" +
            "\t(month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) > 3) \n" +
            "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-2 and month(reportdate) <= 4) \n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) > 3)\n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4)\n" +
            "\t)\n" +
            "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\tand LOB <> 'COST CENTERS'\n" +
            "union all\n" +
            "SELECT\n" +
            "\t2 as sortOrder,\n" +
            "\t'Horizon' as LOB,\n" +
            "\tsum([revenue fact previous FY]) as revenue, \n" +
            "\tsum([cm fact previous FY]) as CM,\n" +
            "\tmax(budget) as budget,\n" +
            "\tmax(year(timeline)) as [year]\n" +
            "FROM [V_FORECAST_DIRECTORATES]\n" +
            "where currency = 'actual' and reporttype = 'lastExecutive'\n" +
            "\tand (\n" +
            "\t(month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) > 3) \n" +
            "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-2 and month(reportdate) <= 4) \n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) > 3)\n" +
            "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4)\n" +
            "\t)\n" +
            "\tand practice = 'Horizon'\n" +
            "\tand LOB <> 'COST CENTERS'\n" +
            "order by 1";



        String REVENUE_CM_LOBS_PLAN_LEFT = "with [V_FORECAST_DIRECTORATES] (LOB, revenue, cm, timeline, reportdate, budget, reporttype, currency)\n" +
                "\tas\n" +
                "\t(\t\t\n" +
                "\tSELECT \n" +
                "\t\tcase when segment in ('FinS Segment X','FinS Segment 2','FinS2 Horizon') then segment else LOB end as LOB,\n" +
                "\t\t[revenue], \n" +
                "\t\t[cm],\n" +
                "\t\ttimeline, reportdate, budget, reporttype, currency\n" +
                "\tFROM [FORECAST_DIRECTORATES]\n" +
                "\twhere reporttype = 'lastExecutive' \n" +
                "\t)\n" +
                "\n" +
                "SELECT\n" +
                "\tcase when LOB = 'FinS Segment X' then 1 when LOB = 'FinS Segment 2' then 2 when LOB = 'AUTOMOTIVE BUSINESS' then 3 \n" +
                "\twhen LOB = 'ENTERPRISE SOLUTIONS' then 4 when LOB = 'TELECOM SERVICES' then 5 when LOB = 'EXCELIAN' then 6 when LOB = 'FinS2 Horizon' then 7 \n" +
                "\twhen LOB = 'GLOBAL CENTERS OF EXPERTISE' then 8 end as sortOrder,\n" +
                "\tcase when LOB = 'FinS Segment X' then 'FinX' when LOB = 'FinS Segment 2' then 'FinS S2' when LOB = 'AUTOMOTIVE BUSINESS' then 'Automotive'\n" +
                "\twhen LOB = 'ENTERPRISE SOLUTIONS' then 'EntS' when LOB = 'TELECOM SERVICES' then 'TelecomS' when LOB = 'EXCELIAN' then 'Excelian' \n" +
                "\twhen LOB = 'FinS2 Horizon' then 'Horizon' when LOB = 'GLOBAL CENTERS OF EXPERTISE' then 'GCoEs' end as LOB,\n" +
                "\tsum([revenue]) as revenue, \n" +
                "\tsum([cm]) as CM,\n" +
                "\tmax(year([timeline])) as year\n" +
                "FROM [V_FORECAST_DIRECTORATES]\n" +
                "where currency = 'plan' and reporttype = 'lastExecutive' and budget = 'plan' \n" +
                "\tand (\n" +
                "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
                "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
                "\t)\n" +
                "\tand LOB in ('AUTOMOTIVE BUSINESS','ENTERPRISE SOLUTIONS','TELECOM SERVICES','EXCELIAN', 'FinS Segment X','FinS Segment 2')\n" +
                "\n" +
                "group by LOB\n" +
                "order by 1\n" +
                "\n";
        String REVENUE_CM_LOBS_PLAN_RIGHT = "\n" +
                "\n" +
                "with [V_FORECAST_DIRECTORATES] (LOB,practice, revenue, cm, timeline, reportdate, budget, reporttype, currency)\n" +
                "\tas\n" +
                "\t(\t\t\n" +
                "\tSELECT \n" +
                "\t\tcase when segment in ('FinS Segment X','FinS Segment 2','FinS2 Horizon') then segment else LOB end as LOB,\n" +
                "\t\tpractice,\n" +
                "\t\t[revenue], \n" +
                "\t\t[cm],\n" +
                "\t\ttimeline, reportdate, budget, reporttype, currency\n" +
                "\tFROM [FORECAST_DIRECTORATES]\n" +
                "\twhere reporttype = 'lastExecutive' \n" +
                "\t)\n" +
                "\n" +
                "SELECT\n" +
                "\t1 as sortOrder,\n" +
                "\t'GCoEs' as LOB,\n" +
                "\tsum([revenue]) as revenue, \n" +
                "\tsum([cm]) as CM,\n" +
                "\tmax(year([timeline])) as year\n" +
                "FROM [V_FORECAST_DIRECTORATES]\n" +
                "where currency = 'plan' and reporttype = 'lastExecutive' and budget = 'plan' \n" +
                "\tand (\n" +
                "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
                "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
                "\t)\n" +
                "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
                "\tand LOB <> 'COST CENTERS'\n" +
                "union all\n" +
                "SELECT\n" +
                "\t2 as sortOrder,\n" +
                "\t'Horizon' as LOB,\n" +
                "\tsum([revenue]) as revenue, \n" +
                "\tsum([cm]) as CM,\n" +
                "\tmax(year([timeline])) as year\n" +
                "FROM [V_FORECAST_DIRECTORATES]\n" +
                "where currency = 'plan' and reporttype = 'lastExecutive' and budget = 'plan' \n" +
                "\tand (\n" +
                "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
                "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
                "\t)\n" +
                "\tand practice = 'Horizon'\n" +
                "\tand LOB <> 'COST CENTERS'\n" +
                "order by 1";
        String REVENUE_CM_LOBS_FACTFORECAST_LEFT = "with [V_FORECAST_DIRECTORATES] (LOB, revenue, cm, timeline, reportdate, budget, reporttype, currency)\n" +
                "\tas\n" +
                "\t(\t\t\n" +
                "\tSELECT \n" +
                "\t\tcase when segment in ('FinS Segment X','FinS Segment 2','FinS2 Horizon') then segment else LOB end as LOB,\n" +
                "\t\t[revenue], \n" +
                "\t\t[cm],\n" +
                "\t\ttimeline, reportdate, budget, reporttype, currency\n" +
                "\tFROM [FORECAST_DIRECTORATES]\n" +
                "\twhere reporttype = 'lastExecutive' \n" +
                "\t)\n" +
                "\n" +
                "SELECT\n" +
                "\tcase when LOB = 'FinS Segment X' then 1 when LOB = 'FinS Segment 2' then 2 when LOB = 'AUTOMOTIVE BUSINESS' then 3 \n" +
                "\twhen LOB = 'ENTERPRISE SOLUTIONS' then 4 when LOB = 'TELECOM SERVICES' then 5 when LOB = 'EXCELIAN' then 6 when LOB = 'FinS2 Horizon' then 7 \n" +
                "\twhen LOB = 'GLOBAL CENTERS OF EXPERTISE' then 8 end as sortOrder,\n" +
                "\tcase when LOB = 'FinS Segment X' then 'FinX' when LOB = 'FinS Segment 2' then 'FinS S2' when LOB = 'AUTOMOTIVE BUSINESS' then 'Automotive'\n" +
                "\twhen LOB = 'ENTERPRISE SOLUTIONS' then 'EntS' when LOB = 'TELECOM SERVICES' then 'TelecomS' when LOB = 'EXCELIAN' then 'Excelian' \n" +
                "\twhen LOB = 'FinS2 Horizon' then 'Horizon' when LOB = 'GLOBAL CENTERS OF EXPERTISE' then 'GCoEs' end as LOB,\n" +
                "\tsum([revenue]) as revenue, \n" +
                "\tsum([cm]) as CM,\n" +
                "\tmax(year([timeline])) as year\n" +
                "FROM [V_FORECAST_DIRECTORATES]\n" +
                "where currency = 'actual' and reporttype = 'lastExecutive' and budget = 'factforecast' \n" +
                "\tand (\n" +
                "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
                "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
                "\t)\n" +
                "\tand LOB in ('AUTOMOTIVE BUSINESS','ENTERPRISE SOLUTIONS','TELECOM SERVICES','EXCELIAN', 'FinS Segment X','FinS Segment 2')\n" +
                "\n" +
                "group by LOB\n" +
                "order by 1\n";
        String REVENUE_CM_LOBS_FACTFORECAST_RIGHT = "with [V_FORECAST_DIRECTORATES] (LOB,practice, revenue, cm, timeline, reportdate, budget, reporttype, currency)\n" +
                "\tas\n" +
                "\t(\t\t\n" +
                "\tSELECT \n" +
                "\t\tcase when segment in ('FinS Segment X','FinS Segment 2','FinS2 Horizon') then segment else LOB end as LOB,\n" +
                "\t\tpractice,\n" +
                "\t\t[revenue], \n" +
                "\t\t[cm],\n" +
                "\t\ttimeline, reportdate, budget, reporttype, currency\n" +
                "\tFROM [FORECAST_DIRECTORATES]\n" +
                "\twhere reporttype = 'lastExecutive' \n" +
                "\t)\n" +
                "\n" +
                "SELECT\n" +
                "\t1 as sortOrder,\n" +
                "\t'GCoEs' as LOB,\n" +
                "\tsum([revenue]) as revenue, \n" +
                "\tsum([cm]) as CM,\n" +
                "\tmax(year([timeline])) as year\n" +
                "FROM [V_FORECAST_DIRECTORATES]\n" +
                "where currency = 'actual' and reporttype = 'lastExecutive' and budget = 'factforecast' \n" +
                "\tand (\n" +
                "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
                "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
                "\t)\n" +
                "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
                "\tand LOB <> 'COST CENTERS'\n" +
                "union all\n" +
                "SELECT\n" +
                "\t2 as sortOrder,\n" +
                "\t'Horizon' as LOB,\n" +
                "\tsum([revenue]) as revenue, \n" +
                "\tsum([cm]) as CM,\n" +
                "\tmax(year([timeline])) as year\n" +
                "FROM [V_FORECAST_DIRECTORATES]\n" +
                "where currency = 'actual' and reporttype = 'lastExecutive' and budget = 'factforecast'\n" +
                "\tand (\n" +
                "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
                "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
                "\t)\n" +
                "\tand practice = 'Horizon'\n" +
                "\tand LOB <> 'COST CENTERS'\n" +
                "order by 1";
        String KPI_REVENUE_CM_CURRENT_YEAR="SELECT \n" +
                "top 10 cast(fd.client as varchar), sum(fd.revenue) as revenue, max(p.revenue) as revenuePlan, sum(fd.CM) as CM, max(p.cm) as CMPlan, max(year(fd.timeline)) as [year]\n" +
                "FROM [FORECAST_DIRECTORATES] fd\n" +
                "left join\n" +
                "(-- revenue&CM current year plan\n" +
                "SELECT \n" +
                "client, sum([revenue]) as revenue, sum([CM]) as CM\n" +
                "FROM [FORECAST_DIRECTORATES]\n" +
                "where currency = 'plan' and reporttype = 'lastExecutive' and budget = 'plan' \n" +
                "and (\n" +
                "(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
                "or (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
                "or (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
                "or (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
                ")\n" +
                "and client <> 'Not Defined'\n" +
                "group by Client\n" +
                ") p on p.Client = fd.Client\n" +
                "where fd.currency = 'actual' and fd.reporttype = 'lastExecutive' and fd.budget = 'factforecast' \n" +
                "and (\n" +
                "(month(fd.timeline) >= 4 and year(fd.timeline)= year(fd.reportdate) and month(fd.reportdate) > 3) \n" +
                "or (month(fd.timeline) >= 4 and year(fd.timeline)= year(fd.reportdate)-1 and month(fd.reportdate) <= 4) \n" +
                "or (month(fd.timeline) < 4 and year(fd.timeline)= year(fd.reportdate)+1 and month(fd.reportdate) > 3)\n" +
                "or (month(fd.timeline) < 4 and year(fd.timeline)= year(fd.reportdate) and month(fd.reportdate) <= 4)\n" +
                ")\n" +
                "and fd.client <> 'Not Defined'\n" +
                "group by fd.Client\n" +
                "order by sum(fd.revenue) desc";


        String REVENUE_CM_COE_PREVIOUS_YEAR_LEFT = "with [V_FORECAST_DIRECTORATES] (KPIType,LOB, [revenue fact previous FY], [cm fact previous FY], timeline, reportdate, budget, reporttype, currency)\n" +
                "\tas\n" +
                "\t(\t\t\n" +
                "\tSELECT \n" +
                "\t\t'LOB' as KPIType,\n" +
                "\t\tcase when segment in ('FinS Segment X','FinS Segment 2','FinS2 Horizon') then segment else LOB end as LOB,\n" +
                "\t\t[revenue fact previous FY], \n" +
                "\t\t[CM fact Previous FY],\n" +
                "\t\ttimeline, reportdate, budget, reporttype, currency\n" +
                "\tFROM [FORECAST_DIRECTORATES]\n" +
                "\twhere reporttype = 'lastExecutive' and budget = 'Previous FY Fact'\n" +
                "\tand practice not in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps', 'Horizon' ,'IoT')\n" +
                "\n" +
                "\tunion all\n" +
                "\n" +
                "\tSELECT \n" +
                "\t\t'CoE' as KPIType,\n" +
                "\t\tcase when segment in ('FinS Segment X','FinS Segment 2','FinS2 Horizon') then segment else LOB end as LOB,\n" +
                "\t\t[revenue fact previous FY], \n" +
                "\t\t[CM fact Previous FY],\n" +
                "\t\ttimeline, reportdate, budget, reporttype, currency\n" +
                "\tFROM [FORECAST_DIRECTORATES]\n" +
                "\twhere reporttype = 'lastExecutive' and budget = 'Previous FY Fact'\n" +
                "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps', 'Horizon' ,'IoT')\n" +
                "\t)\n" +
                "\n" +
                "SELECT\n" +
                "\tcase when LOB = 'FinS Segment X' then 1 when LOB = 'FinS Segment 2' then 2 when LOB = 'AUTOMOTIVE BUSINESS' then 3 \n" +
                "\twhen LOB = 'ENTERPRISE SOLUTIONS' then 4 when LOB = 'TELECOM SERVICES' then 5 when LOB = 'EXCELIAN' then 6 when LOB = 'FinS2 Horizon' then 7 \n" +
                "\twhen LOB = 'GLOBAL CENTERS OF EXPERTISE' then 8 end as sortOrder,\n" +
                "\tcase when LOB = 'FinS Segment X' then 'FinX' when LOB = 'FinS Segment 2' then 'FinS S2' when LOB = 'AUTOMOTIVE BUSINESS' then 'Automotive'\n" +
                "\twhen LOB = 'ENTERPRISE SOLUTIONS' then 'EntS' when LOB = 'TELECOM SERVICES' then 'TelecomS' when LOB = 'EXCELIAN' then 'Excelian' \n" +
                "\twhen LOB = 'FinS2 Horizon' then 'Horizon' when LOB = 'GLOBAL CENTERS OF EXPERTISE' then 'GCoEs' end as LOB,\n" +
                "\tKPIType,\n" +
                "\tsum([revenue fact previous FY]) as revenue, \n" +
                "\tsum([CM fact Previous FY]) as CM,\n" +
                "\tmax(budget) as budget,\n" +
                "\tcase when max(month(reportdate))>3 then max(year(reportdate)) else max(year(reportdate)+1) end as [year]\n" +
                "FROM [V_FORECAST_DIRECTORATES]\n" +
                "where currency = 'actual' and reporttype = 'lastExecutive'\n" +
                "\tand (\n" +
                "\t(month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) > 3) \n" +
                "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-2 and month(reportdate) <= 4) \n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) > 3)\n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4)\n" +
                "\t)\n" +
                "\tand LOB in ('AUTOMOTIVE BUSINESS','ENTERPRISE SOLUTIONS','TELECOM SERVICES','EXCELIAN','GLOBAL CENTERS OF EXPERTISE', \n" +
                "\t\t\t\t'FinS Segment X','FinS Segment 2','FinS2 Horizon')\n" +
                "\n" +
                "group by LOB,KPIType\n" +
                "order by 1\n";
        String REVENUE_CM_COE_CURRENT_YEAR_LEFT = "with [V_FORECAST_DIRECTORATES] (KPIType, LOB, revenue, cm, timeline, reportdate, budget, reporttype, currency)\n" +
                "\tas\n" +
                "\t(\t\t\n" +
                "\tSELECT \n" +
                "\t\t'LOB' as KPIType,\n" +
                "\t\tcase when segment in ('FinS Segment X','FinS Segment 2','FinS2 Horizon') then segment else LOB end as LOB,\n" +
                "\t\t[revenue], \n" +
                "\t\t[cm],\n" +
                "\t\ttimeline, reportdate, budget, reporttype, currency\n" +
                "\tFROM [FORECAST_DIRECTORATES]\n" +
                "\twhere reporttype = 'lastExecutive' \n" +
                "\tand practice not in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps', 'Horizon' ,'IoT')\n" +
                "\t\n" +
                "\tunion all \n" +
                "\n" +
                "\tSELECT \n" +
                "\t\t'CoE' as KPIType,\n" +
                "\t\tcase when segment in ('FinS Segment X','FinS Segment 2','FinS2 Horizon') then segment else LOB end as LOB,\n" +
                "\t\t[revenue], \n" +
                "\t\t[cm],\n" +
                "\t\ttimeline, reportdate, budget, reporttype, currency\n" +
                "\tFROM [FORECAST_DIRECTORATES]\n" +
                "\twhere reporttype = 'lastExecutive' \n" +
                "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps', 'Horizon' ,'IoT')\n" +
                "\t)\n" +
                "\n" +
                "SELECT\n" +
                "\tcase when LOB = 'FinS Segment X' then 1 when LOB = 'FinS Segment 2' then 2 when LOB = 'AUTOMOTIVE BUSINESS' then 3 \n" +
                "\twhen LOB = 'ENTERPRISE SOLUTIONS' then 4 when LOB = 'TELECOM SERVICES' then 5 when LOB = 'EXCELIAN' then 6 when LOB = 'FinS2 Horizon' then 7 \n" +
                "\twhen LOB = 'GLOBAL CENTERS OF EXPERTISE' then 8 end as sortOrder,\n" +
                "\tcase when LOB = 'FinS Segment X' then 'FinX' when LOB = 'FinS Segment 2' then 'FinS S2' when LOB = 'AUTOMOTIVE BUSINESS' then 'Automotive'\n" +
                "\twhen LOB = 'ENTERPRISE SOLUTIONS' then 'EntS' when LOB = 'TELECOM SERVICES' then 'TelecomS' when LOB = 'EXCELIAN' then 'Excelian' \n" +
                "\twhen LOB = 'FinS2 Horizon' then 'Horizon' when LOB = 'GLOBAL CENTERS OF EXPERTISE' then 'GCoEs' end as LOB,\n" +
                "\tKPIType,\n" +
                "\tsum([revenue]) as revenue, \n" +
                "\tsum([cm]) as CM,\n" +
                "\tmax(budget) as budget,\n" +
                "\tmax(year(timeline)) as [year]\n" +
                "FROM [V_FORECAST_DIRECTORATES]\n" +
                "where currency = 'actual' and reporttype = 'lastExecutive' and budget = 'factforecast' \n" +
                "\tand (\n" +
                "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
                "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
                "\t)\n" +
                "\tand LOB in ('AUTOMOTIVE BUSINESS','ENTERPRISE SOLUTIONS','TELECOM SERVICES','EXCELIAN','GLOBAL CENTERS OF EXPERTISE', 'FinS Segment X','FinS Segment 2','FinS2 Horizon')\n" +
                "\n" +
                "group by LOB,KPIType\n" +
                "order by 1\n";
        String REVENUE_CM_COE_PREVIOUS_YEAR_RIGHT = "\n" +
                "with [V_FORECAST_DIRECTORATES] (LOB,practice, [revenue fact previous FY], [cm fact previous FY], timeline, reportdate, budget, reporttype, currency)\n" +
                "\tas\n" +
                "\t(\t\t\n" +
                "\tSELECT \n" +
                "\t\tcase when segment in ('FinS Segment X','FinS Segment 2','FinS2 Horizon') then segment else LOB end as LOB,\n" +
                "\t\tpractice,\n" +
                "\t\t[revenue fact previous FY], \n" +
                "\t\t[cm fact previous FY],\n" +
                "\t\ttimeline, reportdate, budget, reporttype, currency\n" +
                "\tFROM [FORECAST_DIRECTORATES]\n" +
                "\twhere reporttype = 'lastExecutive' and budget = 'Previous FY Fact'\n" +
                "\t)\n" +
                "\n" +
                "SELECT\n" +
                "\t1 as sortOrder,\n" +
                "\t'GCoEs' as LOB,\n" +
                "\tsum([revenue fact previous FY]) as revenue, \n" +
                "\tsum([cm fact previous FY]) as CM,\n" +
                "\tmax(budget) as budget,\n" +
                "\tmax(year(timeline)) as [year]\n" +
                "FROM [V_FORECAST_DIRECTORATES]\n" +
                "where currency = 'actual' and reporttype = 'lastExecutive' \n" +
                "\tand (\n" +
                "\t(month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) > 3) \n" +
                "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-2 and month(reportdate) <= 4) \n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) > 3)\n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4)\n" +
                "\t)\n" +
                "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
                "union all\n" +
                "SELECT\n" +
                "\t2 as sortOrder,\n" +
                "\t'Horizon' as LOB,\n" +
                "\tsum([revenue fact previous FY]) as revenue, \n" +
                "\tsum([cm fact previous FY]) as CM,\n" +
                "\tmax(budget) as budget,\n" +
                "\tmax(year(timeline)) as [year]\n" +
                "FROM [V_FORECAST_DIRECTORATES]\n" +
                "where currency = 'actual' and reporttype = 'lastExecutive'\n" +
                "\tand (\n" +
                "\t(month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) > 3) \n" +
                "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-2 and month(reportdate) <= 4) \n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) > 3)\n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4)\n" +
                "\t)\n" +
                "\tand practice = 'Horizon'\n" +
                "order by 1";
        String REVENUE_CM_COE_CURRENT_YEAR_RIGHT = "\n" +
                "with [V_FORECAST_DIRECTORATES] (LOB,practice, revenue, cm, timeline, reportdate, budget, reporttype, currency)\n" +
                "\tas\n" +
                "\t(\t\t\n" +
                "\tSELECT \n" +
                "\t\tcase when segment in ('FinS Segment X','FinS Segment 2','FinS2 Horizon') then segment else LOB end as LOB,\n" +
                "\t\tpractice,\n" +
                "\t\t[revenue], \n" +
                "\t\t[cm],\n" +
                "\t\ttimeline, reportdate, budget, reporttype, currency\n" +
                "\tFROM [FORECAST_DIRECTORATES]\n" +
                "\twhere reporttype = 'lastExecutive' \n" +
                "\t)\n" +
                "\n" +
                "SELECT\n" +
                "\t1 as sortOrder,\n" +
                "\t'GCoEs' as LOB,\n" +
                "\tsum([revenue]) as revenue, \n" +
                "\tsum([cm]) as CM,\n" +
                "\tmax(budget) as budget,\n" +
                "\tmax(year(timeline)) as [year]\n" +
                "FROM [V_FORECAST_DIRECTORATES]\n" +
                "where currency = 'actual' and reporttype = 'lastExecutive' and budget = 'factforecast' \n" +
                "\tand (\n" +
                "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
                "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
                "\t)\n" +
                "\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
                "union all\n" +
                "SELECT\n" +
                "\t2 as sortOrder,\n" +
                "\t'Horizon' as LOB,\n" +
                "\tsum([revenue]) as revenue, \n" +
                "\tsum([cm]) as CM,\n" +
                "\tmax(budget) as budget,\n" +
                "\tmax(year(timeline)) as [year]\n" +
                "FROM [V_FORECAST_DIRECTORATES]\n" +
                "where currency = 'actual' and reporttype = 'lastExecutive' and budget = 'factforecast'\n" +
                "\tand (\n" +
                "\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
                "\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
                "\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
                "\t)\n" +
                "\tand practice = 'Horizon'\n" +
                "order by 1";
}
