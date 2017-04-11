package com.luxoft.horizon.dir.utils;

/**
 * @author rlapin
 */
public interface RevenueLobsForecastForexQueries {
    String KPI_BY_LOBS_CM_SECONDVALUE = "--FX infl\n" +
            "\tselect\n" +
            "\t( \n" +
            "\t\t--diff b/w current factforecast in actual currency & previous factforecast in plan currency\n" +
            "\t\tselect\n" +
            "\t\tisnull((--current factforecast in actual currency\n" +
            "\t\t\tSELECT \n" +
            "\t\t\t\tsum([CM]) as CM\n" +
            "\t\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\t\twhere \n" +
            "\t\t\t\treporttype = 'lastExecutive' \n" +
            "\t\t\t\tand currency = 'actual' \n" +
            "\t\t\t\tand budget = 'factforecast' \n" +
            "\t\t\t\tand (\n" +
            "\t\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t\t)\n" +
            "\t\t),0)\n" +
            "\t\t-\n" +
            "\t\tisnull((--previous factforecast in actual currency\n" +
            "\t\t\tSELECT \n" +
            "\t\t\t\tsum([CM Previous FactForecast]) as CM\n" +
            "\t\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\t\twhere \n" +
            "\t\t\t \n" +
            "\t\t\t\tcurrency = 'actual' \n" +
            "\t\t\t\tand budget = 'previous factforecast' \n" +
            "\t\t\t\tand (\n" +
            "\t\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t\t)\n" +
            "\t\t),0)\n" +
            "\t) \n" +
            "\t-\n" +
            "\t(\n" +
            "\t\t--diff b/w current factforecast in plan currency & previous factforecast in plan currency\n" +
            "\t\tselect\n" +
            "\t\tisnull((--current factforecast in plan currency\n" +
            "\t\t\tSELECT \n" +
            "\t\t\t\tsum([CM]) as CM\n" +
            "\t\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\t\twhere \n" +
            "\t\t\t\treporttype = 'lastExecutive' \n" +
            "\t\t\t\tand currency = 'plan' \n" +
            "\t\t\t\tand budget = 'factforecast' \n" +
            "\t\t\t\tand (\n" +
            "\t\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t\t)\n" +
            "\t\t),0)\n" +
            "\t\t-\n" +
            "\t\tisnull((--previous factforecast in plan currency\n" +
            "\t\t\tSELECT \n" +
            "\t\t\t\tsum([CM Previous FactForecast]) as CM\n" +
            "\t\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\t\twhere \n" +
            "\t\t\t \n" +
            "\t\t\t\tcurrency = 'plan' \n" +
            "\t\t\t\tand budget = 'previous factforecast' \n" +
            "\t\t\t\tand (\n" +
            "\t\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t\t)\n" +
            "\t\t),0)\n" +
            "\t) as [FX infl.]";
    String KPI_BY_LOBS_REVENUE_SECONDVALUE = "--FX infl\n" +
            "\tselect\n" +
            "\t( \n" +
            "\t\t--diff b/w current factforecast in actual currency & previous factforecast in plan currency\n" +
            "\t\tselect\n" +
            "\t\tisnull((--current factforecast in actual currency\n" +
            "\t\t\tSELECT \n" +
            "\t\t\t\tsum([revenue]) as revenue\n" +
            "\t\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\t\twhere \n" +
            "\t\t\t\treporttype = 'lastExecutive' \n" +
            "\t\t\t\tand currency = 'actual' \n" +
            "\t\t\t\tand budget = 'factforecast' \n" +
            "\t\t\t\tand (\n" +
            "\t\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t\t)\n" +
            "\t\t),0)\n" +
            "\t\t-\n" +
            "\t\tisnull((--previous factforecast in actual currency\n" +
            "\t\t\tSELECT \n" +
            "\t\t\t\tsum([Revenue Previous FactForecast]) as revenue\n" +
            "\t\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\t\twhere \n" +
            "\t\t\t \n" +
            "\t\t\t\tcurrency = 'actual' \n" +
            "\t\t\t\tand budget = 'previous factforecast' \n" +
            "\t\t\t\tand (\n" +
            "\t\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t\t)\n" +
            "\t\t),0)\n" +
            "\t) \n" +
            "\t-\n" +
            "\t(\n" +
            "\t\t--diff b/w current factforecast in plan currency & previous factforecast in plan currency\n" +
            "\t\tselect\n" +
            "\t\tisnull((--current factforecast in plan currency\n" +
            "\t\t\tSELECT \n" +
            "\t\t\t\tsum([revenue]) as revenue\n" +
            "\t\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\t\twhere \n" +
            "\t\t\t\treporttype = 'lastExecutive' \n" +
            "\t\t\t\tand currency = 'plan' \n" +
            "\t\t\t\tand budget = 'factforecast' \n" +
            "\t\t\t\tand (\n" +
            "\t\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t\t)\n" +
            "\t\t),0)\n" +
            "\t\t-\n" +
            "\t\tisnull((--previous factforecast in plan currency\n" +
            "\t\t\tSELECT \n" +
            "\t\t\t\tsum([Revenue Previous FactForecast]) as revenue\n" +
            "\t\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\t\twhere \n" +
            "\t\t\t \n" +
            "\t\t\t\tcurrency = 'plan' \n" +
            "\t\t\t\tand budget = 'previous factforecast' \n" +
            "\t\t\t\tand (\n" +
            "\t\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t\t)\n" +
            "\t\t),0)\n" +
            "\t) \n" +
            "\t\n" +
            "\tas [FX infl.]";
    String KPI_BY_LOBS_REVENUE_GCOE_SECONDVALUE = "--FX infl\n" +
            "\tselect\n" +
            "\t( \n" +
            "\t\t--diff b/w current factforecast in actual currency & previous factforecast in plan currency\n" +
            "\t\tselect\n" +
            "\t\tisnull((--current factforecast in actual currency\n" +
            "\t\t\tSELECT \n" +
            "\t\t\t\tsum([revenue]) as revenue\n" +
            "\t\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\t\twhere \n" +
            "\t\t\t\treporttype = 'lastExecutive' \n" +
            "\t\t\t\tand currency = 'actual' \n" +
            "\t\t\t\tand budget = 'factforecast' \n" +
            "\t\t\t\tand (\n" +
            "\t\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t\t)\n" +
            "\t\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t\t\t\tand\t(LOB in ('AUTOMOTIVE BUSINESS','ENTERPRISE SOLUTIONS','TELECOM SERVICES','GLOBAL CENTERS OF EXPERTISE')\n" +
            "\t\t\t\t\t\tor segment in ('FinS Segment X','FinS Segment 2'))\n" +
            "\t\t),0)\n" +
            "\t\t-\n" +
            "\t\tisnull((--previous factforecast in actual currency\n" +
            "\t\t\tSELECT \n" +
            "\t\t\t\tsum([Revenue Previous FactForecast]) as revenue\n" +
            "\t\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\t\twhere \n" +
            "\t\t\t \n" +
            "\t\t\t\tcurrency = 'actual' \n" +
            "\t\t\t\tand budget = 'previous factforecast' \n" +
            "\t\t\t\tand (\n" +
            "\t\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t\t)\n" +
            "\t\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t\t\t\tand\t(LOB in ('AUTOMOTIVE BUSINESS','ENTERPRISE SOLUTIONS','TELECOM SERVICES','GLOBAL CENTERS OF EXPERTISE')\n" +
            "\t\t\t\t\t\tor segment in ('FinS Segment X','FinS Segment 2'))\n" +
            "\t\t),0)\n" +
            "\t) \n" +
            "\t-\n" +
            "\t(\n" +
            "\t\t--diff b/w current factforecast in plan currency & previous factforecast in plan currency\n" +
            "\t\tselect\n" +
            "\t\tisnull((--current factforecast in plan currency\n" +
            "\t\t\tSELECT \n" +
            "\t\t\t\tsum([revenue]) as revenue\n" +
            "\t\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\t\twhere \n" +
            "\t\t\t\treporttype = 'lastExecutive' \n" +
            "\t\t\t\tand currency = 'plan' \n" +
            "\t\t\t\tand budget = 'factforecast' \n" +
            "\t\t\t\tand (\n" +
            "\t\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t\t)\n" +
            "\t\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t\t\t\tand\t(LOB in ('AUTOMOTIVE BUSINESS','ENTERPRISE SOLUTIONS','TELECOM SERVICES','GLOBAL CENTERS OF EXPERTISE')\n" +
            "\t\t\t\t\t\tor segment in ('FinS Segment X','FinS Segment 2'))\n" +
            "\t\t),0)\n" +
            "\t\t-\n" +
            "\t\tisnull((--previous factforecast in plan currency\n" +
            "\t\t\tSELECT \n" +
            "\t\t\t\tsum([Revenue Previous FactForecast]) as revenue\n" +
            "\t\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\t\twhere \n" +
            "\t\t\t \n" +
            "\t\t\t\tcurrency = 'plan' \n" +
            "\t\t\t\tand budget = 'previous factforecast' \n" +
            "\t\t\t\tand (\n" +
            "\t\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t\t)\n" +
            "\t\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t\t\t\tand\t(LOB in ('AUTOMOTIVE BUSINESS','ENTERPRISE SOLUTIONS','TELECOM SERVICES','GLOBAL CENTERS OF EXPERTISE')\n" +
            "\t\t\t\t\t\tor segment in ('FinS Segment X','FinS Segment 2'))\n" +
            "\t\t),0)\n" +
            "\t) as [FX infl.]";
    String KPI_BY_LOBS_CM_GCOE_SECONDVALUE = "--FX infl\n" +
            "\tselect\n" +
            "\t( \n" +
            "\t\t--diff b/w current factforecast in actual currency & previous factforecast in plan currency\n" +
            "\t\tselect\n" +
            "\t\tisnull((--current factforecast in actual currency\n" +
            "\t\t\tSELECT \n" +
            "\t\t\t\tsum([CM]) as CM\n" +
            "\t\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\t\twhere \n" +
            "\t\t\t\treporttype = 'lastExecutive' \n" +
            "\t\t\t\tand currency = 'actual' \n" +
            "\t\t\t\tand budget = 'factforecast' \n" +
            "\t\t\t\tand (\n" +
            "\t\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t\t)\n" +
            "\t\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t\t\t\tand\t(LOB in ('AUTOMOTIVE BUSINESS','ENTERPRISE SOLUTIONS','TELECOM SERVICES','GLOBAL CENTERS OF EXPERTISE')\n" +
            "\t\t\t\t\t\tor segment in ('FinS Segment X','FinS Segment 2'))\n" +
            "\t\t),0)\n" +
            "\t\t-\n" +
            "\t\tisnull((--previous factforecast in actual currency\n" +
            "\t\t\tSELECT \n" +
            "\t\t\t\tsum([CM Previous FactForecast]) as CM\n" +
            "\t\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\t\twhere \n" +
            "\t\t\t \n" +
            "\t\t\t\tcurrency = 'actual' \n" +
            "\t\t\t\tand budget = 'previous factforecast' \n" +
            "\t\t\t\tand (\n" +
            "\t\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t\t)\n" +
            "\t\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t\t\t\tand\t(LOB in ('AUTOMOTIVE BUSINESS','ENTERPRISE SOLUTIONS','TELECOM SERVICES','GLOBAL CENTERS OF EXPERTISE')\n" +
            "\t\t\t\t\t\tor segment in ('FinS Segment X','FinS Segment 2'))\n" +
            "\t\t),0)\n" +
            "\t) \n" +
            "\t-\n" +
            "\t(\n" +
            "\t\t--diff b/w current factforecast in plan currency & previous factforecast in plan currency\n" +
            "\t\tselect\n" +
            "\t\tisnull((--current factforecast in plan currency\n" +
            "\t\t\tSELECT \n" +
            "\t\t\t\tsum([CM]) as CM\n" +
            "\t\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\t\twhere \n" +
            "\t\t\t\treporttype = 'lastExecutive' \n" +
            "\t\t\t\tand currency = 'plan' \n" +
            "\t\t\t\tand budget = 'factforecast' \n" +
            "\t\t\t\tand (\n" +
            "\t\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t\t)\n" +
            "\t\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t\t\t\tand\t(LOB in ('AUTOMOTIVE BUSINESS','ENTERPRISE SOLUTIONS','TELECOM SERVICES','GLOBAL CENTERS OF EXPERTISE')\n" +
            "\t\t\t\t\t\tor segment in ('FinS Segment X','FinS Segment 2'))\n" +
            "\t\t),0)\n" +
            "\t\t-\n" +
            "\t\tisnull((--previous factforecast in plan currency\n" +
            "\t\t\tSELECT \n" +
            "\t\t\t\tsum([CM Previous FactForecast]) as CM\n" +
            "\t\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\t\twhere \n" +
            "\t\t\t \n" +
            "\t\t\t\tcurrency = 'plan' \n" +
            "\t\t\t\tand budget = 'previous factforecast' \n" +
            "\t\t\t\tand (\n" +
            "\t\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t\t)\n" +
            "\t\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t\t\t\tand\t(LOB in ('AUTOMOTIVE BUSINESS','ENTERPRISE SOLUTIONS','TELECOM SERVICES','GLOBAL CENTERS OF EXPERTISE')\n" +
            "\t\t\t\t\t\tor segment in ('FinS Segment X','FinS Segment 2'))\n" +
            "\t\t),0)\n" +
            "\t) as [FX infl.]";
    String KPI_BY_LOBS_CM_GCOE_DELTAS = "\tdeclare \n" +
            "\t@bar1 varchar(50), @bar2 varchar(50), @bar3 varchar(50), @bar4 varchar(50), @bar5 varchar(50), \n" +
            "\t@bar6 varchar(50), @bar7 varchar(50), @bar8 varchar(50),\n" +
            "\t@currencyPlan varchar(10), @currencyAccounting varchar(10),\n" +
            "\t@reporttypePrevious varchar(20), @reporttypeCurrent varchar(20),\n" +
            "\t@budgetCurrent varchar(50), @budgetPrevious varchar(50)\n" +
            "\t\n" +
            "\tset @reporttypePrevious = 'previousExecutive'\n" +
            "\tset @reporttypeCurrent = 'lastExecutive'\n" +
            "\tset @currencyPlan = 'plan'\n" +
            "\t--set @currencyAccounting = 'actual'\n" +
            "\tset @budgetCurrent = 'factforecast'\n" +
            "\tset @budgetPrevious = 'previous factforecast'\n" +
            "\n" +
            "\tset @bar1 = 'FinS Segment X' \n" +
            "\tset @bar2 = 'FinS Segment 2'\n" +
            "\tset @bar3 = 'AUTOMOTIVE BUSINESS'\n" +
            "\tset @bar4 = 'ENTERPRISE SOLUTIONS'\n" +
            "\tset @bar5 = 'TELECOM SERVICES'\n" +
            "\t--set @bar6 = 'EXCELIAN'\n" +
            "\t--set @bar7 = 'FinS2 Horizon'\n" +
            "\tset @bar8 = 'GLOBAL CENTERS OF EXPERTISE'\n" +
            "\t\n" +
            "\t\t\n" +
            "\tselect 1 as sortOrder, 'FinX' as LOB,\n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([cm])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand segment = @bar1\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([cm previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand segment = @bar1\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t),0) as cm\n" +
            "\t\n" +
            "union all\n" +
            "\tselect 2 as sortOrder, 'FinS S2' as LOB,\n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([cm])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand segment = @bar2\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([cm previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand segment = @bar2\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t),0) as cm\n" +
            "\n" +
            "union all\n" +
            "\t\tselect 3 as sortOrder, 'Automotive' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([cm])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar3\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([cm previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar3\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t),0)\n" +
            "\n" +
            "\tunion all\n" +
            "\t\tselect 4 as sortOrder, 'EntS' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([cm])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar4\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([cm previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar4\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t),0)\n" +
            "\n" +
            "\t\n" +
            "\tunion all\n" +
            "\t\tselect 5 as sortOrder, 'TelecomS' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([cm])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar5\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([cm previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar5\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t),0)\n" +
            "\n" +
            "union all\n" +
            "\t\tselect 8 as sortOrder, 'GCoEs outside LOB' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([cm])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar8\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([cm previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar8\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t),0)";
    String KPI_BY_LOBS_REVENUE_GCOE_DELTAS = "\tdeclare \n" +
            "\t@bar1 varchar(50), @bar2 varchar(50), @bar3 varchar(50), @bar4 varchar(50), @bar5 varchar(50), \n" +
            "\t@bar6 varchar(50), @bar7 varchar(50), @bar8 varchar(50),\n" +
            "\t@currencyPlan varchar(10), @currencyAccounting varchar(10),\n" +
            "\t@reporttypePrevious varchar(20), @reporttypeCurrent varchar(20),\n" +
            "\t@budgetCurrent varchar(50), @budgetPrevious varchar(50)\n" +
            "\t\n" +
            "\tset @reporttypePrevious = 'previousExecutive'\n" +
            "\tset @reporttypeCurrent = 'lastExecutive'\n" +
            "\tset @currencyPlan = 'plan'\n" +
            "\t--set @currencyAccounting = 'actual'\n" +
            "\tset @budgetCurrent = 'factforecast'\n" +
            "\tset @budgetPrevious = 'previous factforecast'\n" +
            "\n" +
            "\tset @bar1 = 'FinS Segment X' \n" +
            "\tset @bar2 = 'FinS Segment 2'\n" +
            "\tset @bar3 = 'AUTOMOTIVE BUSINESS'\n" +
            "\tset @bar4 = 'ENTERPRISE SOLUTIONS'\n" +
            "\tset @bar5 = 'TELECOM SERVICES'\n" +
            "\t--set @bar6 = 'EXCELIAN'\n" +
            "\t--set @bar7 = 'FinS2 Horizon'\n" +
            "\tset @bar8 = 'GLOBAL CENTERS OF EXPERTISE'\n" +
            "\t\n" +
            "\t\t\n" +
            "\tselect 1 as sortOrder, 'FinX' as LOB,\n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand segment = @bar1\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t\t\tand program <> 'Sberbank_Murex'\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand segment = @bar1\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t\t\tand program <> 'Sberbank_Murex'\n" +
            "\t),0) as revenue\n" +
            "\n" +
            "union all\n" +
            "select 2 as sortOrder, 'FinS S2' as LOB,\n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand segment = @bar2\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand segment = @bar2\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\n" +
            "\t),0) as revenue\n" +
            "\n" +
            "union all\n" +
            "\t\tselect 3 as sortOrder, 'Automotive' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar3\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar3\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t),0)\n" +
            "\n" +
            "\tunion all\n" +
            "\t\tselect 4 as sortOrder, 'EntS' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar4\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar4\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t),0)\n" +
            "\n" +
            "\t\n" +
            "\tunion all\n" +
            "\t\tselect 5 as sortOrder, 'TelecomS' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar5\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar5\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t),0)\n" +
            "\n" +
            "union all\n" +
            "\t\tselect 8 as sortOrder, 'GCoEs outside LOB' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar8\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar8\n" +
            "\t\t\tand practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "\t),0)";
    String KPI_BY_LOBS_CM_DELTAS = "\tdeclare \n" +
            "\t@bar1 varchar(50), @bar2 varchar(50), @bar3 varchar(50), @bar4 varchar(50), @bar5 varchar(50), \n" +
            "\t@bar6 varchar(50), @bar7 varchar(50), @bar8 varchar(50),\n" +
            "\t@currencyPlan varchar(10), @currencyAccounting varchar(10),\n" +
            "\t@reporttypePrevious varchar(20), @reporttypeCurrent varchar(20),\n" +
            "\t@budgetCurrent varchar(50), @budgetPrevious varchar(50)\n" +
            "\t\n" +
            "\tset @reporttypePrevious = 'previousExecutive'\n" +
            "\tset @reporttypeCurrent = 'lastExecutive'\n" +
            "\tset @currencyPlan = 'plan'\n" +
            "\t--set @currencyAccounting = 'actual'\n" +
            "\tset @budgetCurrent = 'factforecast'\n" +
            "\tset @budgetPrevious = 'previous factforecast'\n" +
            "\n" +
            "\tset @bar1 = 'FinS Segment X' \n" +
            "\tset @bar2 = 'FinS Segment 2'\n" +
            "\tset @bar3 = 'AUTOMOTIVE BUSINESS'\n" +
            "\tset @bar4 = 'ENTERPRISE SOLUTIONS'\n" +
            "\tset @bar5 = 'TELECOM SERVICES'\n" +
            "\tset @bar6 = 'EXCELIAN'\n" +
            "\tset @bar7 = 'FinS2 Horizon'\n" +
            "\tset @bar8 = 'GLOBAL CENTERS OF EXPERTISE'\n" +
            "\t\n" +
            "\t\t\n" +
            "\tselect 1 as sortOrder, 'FinX' as LOB,\n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([CM])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand segment = @bar1\n" +
            "\t\t\tand program <> 'Sberbank_Murex'\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([CM previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand segment = @bar1\n" +
            "\t\t\tand program <> 'Sberbank_Murex'\n" +
            "\t),0) as CM\n" +
            "\t\n" +
            "\n" +
            "\n" +
            "union all\n" +
            "\t\tselect 2 as sortOrder, 'FinS S2' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([CM])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand segment = @bar2\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([CM previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand segment = @bar2\n" +
            "\t),0)\n" +
            "\n" +
            "\n" +
            "union all\n" +
            "\t\tselect 3 as sortOrder, 'Automotive' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([CM])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar3\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([CM previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar3\n" +
            "\t),0)\n" +
            "\n" +
            "\tunion all\n" +
            "\t\tselect 4 as sortOrder, 'EntS' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([CM])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar4\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([CM previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar4\n" +
            "\t),0)\n" +
            "\n" +
            "\t\n" +
            "\tunion all\n" +
            "\t\tselect 5 as sortOrder, 'TelecomS' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([CM])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar5\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([CM previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar5\n" +
            "\t),0)\n" +
            "\n" +
            "\t\n" +
            "\tunion all\n" +
            "\t\tselect 6 as sortOrder, 'Excelian' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([CM])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar6\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([CM previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar6\n" +
            "\t),0)\n" +
            "\t\n" +
            "\t\tunion all\n" +
            "\t\tselect 7 as sortOrder, 'Horizon' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([CM])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand segment = @bar7\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([CM previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand segment = @bar7\n" +
            "\t),0)\n" +
            "\n" +
            "\t\tunion all\n" +
            "\t\tselect 8 as sortOrder, 'GCoEs outside LOB' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([CM])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar8\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([CM previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar8\n" +
            "\t),0)\n" +
            "\n" +
            "\n" +
            "union all\n" +
            "\t\tselect 9 as sortOrder, 'Other' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([CM])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand \n" +
            "\t\t\t(\n" +
            "\t\t\t\t(LOB in ('COST CENTERS','RADIUS USA','PRODUCT AND PRACTICES','OTHER_PC','TECHNOLOGY SERVICES')\n" +
            "\t\t\t\tor segment in ('SALES ORGANIZATION','FINANCIAL SOLUTIONS')\n" +
            "\t\t\t\t)\n" +
            "\t\t\tor\n" +
            "\t\t\t\t(segment = 'FinS Segment X' and program = 'Sberbank_Murex')\n" +
            "\t\t\t\t)\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([CM previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand \n" +
            "\t\t\t(\n" +
            "\t\t\t\t(LOB in ('COST CENTERS','RADIUS USA','PRODUCT AND PRACTICES','OTHER_PC','TECHNOLOGY SERVICES')\n" +
            "\t\t\t\tor segment in ('SALES ORGANIZATION','FINANCIAL SOLUTIONS')\n" +
            "\t\t\t\t)\n" +
            "\t\t\tor\n" +
            "\t\t\t\t(segment = 'FinS Segment X' and program = 'Sberbank_Murex')\n" +
            "\t\t\t\t)\n" +
            "\t),0)";
    String KPI_BY_LOBS_REVENUE_DELTAS = "\tdeclare \n" +
            "\t@bar1 varchar(50), @bar2 varchar(50), @bar3 varchar(50), @bar4 varchar(50), @bar5 varchar(50), \n" +
            "\t@bar6 varchar(50), @bar7 varchar(50), @bar8 varchar(50),\n" +
            "\t@currencyPlan varchar(10), @currencyAccounting varchar(10),\n" +
            "\t@reporttypePrevious varchar(20), @reporttypeCurrent varchar(20),\n" +
            "\t@budgetCurrent varchar(50), @budgetPrevious varchar(50)\n" +
            "\t\n" +
            "\tset @reporttypePrevious = 'previousExecutive'\n" +
            "\tset @reporttypeCurrent = 'lastExecutive'\n" +
            "\tset @currencyPlan = 'plan'\n" +
            "\t--set @currencyAccounting = 'actual'\n" +
            "\tset @budgetCurrent = 'factforecast'\n" +
            "\tset @budgetPrevious = 'previous factforecast'\n" +
            "\n" +
            "\tset @bar1 = 'FinS Segment X' \n" +
            "\tset @bar2 = 'FinS Segment 2'\n" +
            "\tset @bar3 = 'AUTOMOTIVE BUSINESS'\n" +
            "\tset @bar4 = 'ENTERPRISE SOLUTIONS'\n" +
            "\tset @bar5 = 'TELECOM SERVICES'\n" +
            "\tset @bar6 = 'EXCELIAN'\n" +
            "\tset @bar7 = 'FinS2 Horizon'\n" +
            "\tset @bar8 = 'GLOBAL CENTERS OF EXPERTISE'\n" +
            "\t\n" +
            "\t\t\n" +
            "\tselect 1 as sortOrder, 'FinX' as LOB,\n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand segment = @bar1\n" +
            "\t\t\tand program <> 'Sberbank_Murex'\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand segment = @bar1\n" +
            "\t\t\tand program <> 'Sberbank_Murex'\n" +
            "\t),0) as revenue\n" +
            "\t\n" +
            "\n" +
            "\n" +
            "union all\n" +
            "\t\tselect 2 as sortOrder, 'FinS S2' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand segment = @bar2\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand segment = @bar2\n" +
            "\t),0)\n" +
            "\n" +
            "\n" +
            "union all\n" +
            "\t\tselect 3 as sortOrder, 'Automotive' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar3\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar3\n" +
            "\t),0)\n" +
            "\n" +
            "\tunion all\n" +
            "\t\tselect 4 as sortOrder, 'EntS' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar4\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar4\n" +
            "\t),0)\n" +
            "\n" +
            "\t\n" +
            "\tunion all\n" +
            "\t\tselect 5 as sortOrder, 'TelecomS' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar5\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar5\n" +
            "\t),0)\n" +
            "\n" +
            "\t\n" +
            "\tunion all\n" +
            "\t\tselect 6 as sortOrder, 'Excelian' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar6\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar6\n" +
            "\t),0)\n" +
            "\t\n" +
            "\t\tunion all\n" +
            "\t\tselect 7 as sortOrder, 'Horizon' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand segment = @bar7\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand segment = @bar7\n" +
            "\t),0)\n" +
            "\n" +
            "\t\tunion all\n" +
            "\t\tselect 8 as sortOrder, 'GCoEs outside LOB' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar8\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand LOB = @bar8\n" +
            "\t),0)\n" +
            "\n" +
            "\n" +
            "union all\n" +
            "\t\tselect 9 as sortOrder, 'Other' as LOB, \n" +
            "\tisnull((--current factforecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypeCurrent and budget = @budgetCurrent \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            "\t\t\t)\n" +
            "\t\t\tand \n" +
            "\t\t\t(\n" +
            "\t\t\t\t(LOB in ('COST CENTERS','RADIUS USA','PRODUCT AND PRACTICES','OTHER_PC','TECHNOLOGY SERVICES')\n" +
            "\t\t\t\tor segment in ('SALES ORGANIZATION','FINANCIAL SOLUTIONS')\n" +
            "\t\t\t\t)\n" +
            "\t\t\tor\n" +
            "\t\t\t\t(segment = 'FinS Segment X' and program = 'Sberbank_Murex')\n" +
            "\t\t\t)\n" +
            "\t),0)\n" +
            "\t-\n" +
            "\tisnull((--previous forecast\n" +
            "\t\tSELECT \n" +
            "\t\t\tsum([revenue previous factforecast])\n" +
            "\t\tFROM [FORECAST_DIRECTORATES]\n" +
            "\t\twhere currency = @currencyPlan and reporttype = @reporttypePrevious and budget = @budgetPrevious \n" +
            "\t\t\tand (\n" +
            "\t\t\t(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "\t\t\tor (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "\t\t\tor (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            "\t\t\t)\n" +
            "\t\t\tand \n" +
            "\t\t\t(\n" +
            "\t\t\t\t(LOB in ('COST CENTERS','RADIUS USA','PRODUCT AND PRACTICES','OTHER_PC','TECHNOLOGY SERVICES')\n" +
            "\t\t\t\tor segment in ('SALES ORGANIZATION','FINANCIAL SOLUTIONS')\n" +
            "\t\t\t\t)\n" +
            "\t\t\tor\n" +
            "\t\t\t\t(segment = 'FinS Segment X' and program = 'Sberbank_Murex')\n" +
            "\t\t\t)\n" +
            "\t),0)";
    String KPI_BY_LOBS_CM_GCOE_FIRSTVALUE = "SELECT sum([CM Previous FactForecast]) as CM, max(year(timeline)) as [year], 'previous Forecast' as budget\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where currency = 'actual' and budget = 'previous factforecast' \n" +
            "and (\n" +
            "(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "or (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            ")\n" +
            "and practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "and (LOB in ('AUTOMOTIVE BUSINESS','ENTERPRISE SOLUTIONS','TELECOM SERVICES','GLOBAL CENTERS OF EXPERTISE')\n" +
            "or segment in ('FinS Segment X','FinS Segment 2')\n" +
            ")";
    String KPI_BY_LOBS_REVENUE_GCOE_FIRSTVALUE = "SELECT sum([revenue Previous FactForecast]) as Revenue, max(year(timeline)) as [year], 'previous Forecast' as budget\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where currency = 'actual' and budget = 'previous factforecast' \n" +
            "and (\n" +
            "(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "or (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            ")\n" +
            "and practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "and (LOB in ('AUTOMOTIVE BUSINESS','ENTERPRISE SOLUTIONS','TELECOM SERVICES','GLOBAL CENTERS OF EXPERTISE')\n" +
            "or segment in ('FinS Segment X','FinS Segment 2')\n" +
            ")";
    String KPI_BY_LOBS_CM_FIRSTVALUE = "SELECT sum([CM Previous FactForecast]) as CM, max(year(timeline)) as [year], 'previous Forecast' as budget\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where currency = 'actual' and budget = 'previous factforecast' \n" +
            "and (\n" +
            "(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "or (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            ")";
    String KPI_BY_LOBS_REVENUE_FIRSTVALUE = "SELECT sum([Revenue Previous FactForecast]) as Revenue, max(year(timeline)) as [year], 'previous Forecast' as budget\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where currency = 'actual' and budget = 'previous factforecast' \n" +
            "and (\n" +
            "(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3-1) \n" +
            "or (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4-1) \n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3-1)\n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4-1)\n" +
            ")";
    String KPI_BY_LOBS_CM_GCOE_LASTVALUE = "SELECT sum([CM]) as CM, max(year(timeline)) as [year], 'current Forecast' as budget\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where currency = 'actual' and reporttype = 'lastExecutive' and budget = 'factforecast' \n" +
            "and (\n" +
            "(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "or (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            ")\n" +
            "and practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "and (LOB in ('AUTOMOTIVE BUSINESS','ENTERPRISE SOLUTIONS','TELECOM SERVICES','GLOBAL CENTERS OF EXPERTISE')\n" +
            "or segment in ('FinS Segment X','FinS Segment 2')\n" +
            ")";
    String KPI_BY_LOBS_REVENUE_GCOE_LASTVALUE = "SELECT sum([revenue]) as Revenue, max(year(timeline)) as [year], 'current Forecast' as budget\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where currency = 'actual' and reporttype = 'lastExecutive' and budget = 'factforecast' \n" +
            "and (\n" +
            "(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "or (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            ")\n" +
            "and practice in ('Atlassian and Agile','Big Data','CoEs Global','Creative Labs','DevOps','IoT')\n" +
            "and (LOB in ('AUTOMOTIVE BUSINESS','ENTERPRISE SOLUTIONS','TELECOM SERVICES','GLOBAL CENTERS OF EXPERTISE')\n" +
            "or segment in ('FinS Segment X','FinS Segment 2')\n" +
            ")";
    String KPI_BY_LOBS_CM_LASTVALUE = "SELECT sum([CM]) as CM, max(year(timeline)) as [year], 'current Forecast' as budget\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where currency = 'actual' and reporttype = 'lastExecutive' and budget = 'factforecast' \n" +
            "and (\n" +
            "(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "or (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            ")";
    String KPI_BY_LOBS_REVENUE_LASTVALUE = "SELECT sum([revenue]) as Revenue, max(year(timeline)) as [year], 'current Forecast' as budget\n" +
            "FROM [FORECAST_DIRECTORATES]\n" +
            "where currency = 'actual' and reporttype = 'lastExecutive' and budget = 'factforecast' \n" +
            "and (\n" +
            "(month(timeline) >= 4 and year(timeline)= year(reportdate) and month(reportdate) > 3) \n" +
            "or (month(timeline) >= 4 and year(timeline)= year(reportdate)-1 and month(reportdate) <= 4) \n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate)+1 and month(reportdate) > 3)\n" +
            "or (month(timeline) < 4 and year(timeline)= year(reportdate) and month(reportdate) <= 4)\n" +
            ")";
}
