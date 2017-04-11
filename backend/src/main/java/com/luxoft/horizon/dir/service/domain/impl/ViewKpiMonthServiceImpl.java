package com.luxoft.horizon.dir.service.domain.impl;

import com.luxoft.horizon.dir.service.app.IConfigurationService;
import com.luxoft.horizon.dir.service.domain.IViewKpiMonthService;
import com.luxoft.horizon.dir.service.domain.KPIRepository;
import com.luxoft.horizon.dir.utils.Utils;
import com.luxoft.horizon.dir.widget.BarChartDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * @author rlapin
 */
@Service
public class ViewKpiMonthServiceImpl implements IViewKpiMonthService {
    public static final String PLAN_REVENUE_PERCENT = "PLAN_REVENUE_PERCENT";
    public static final String FACT_REVENUE_PERCENT = "FACT_REVENUE_PERCENT";
    public static final String FORECAST_REVENUE_PERCENT = "FORECAST_REVENUE_PERCENT";
    public static final BigDecimal DEFAULT_VALUE = BigDecimal.valueOf(0);
    @Autowired
    IConfigurationService configurationService;

    @Autowired
    KPIRepository repository;
    @Override
    public List<BarChartDataModel> getKpiMonth(int month, int year) {
        List<BarChartDataModel> result = new LinkedList<>();
        BarChartDataModel revenueModel = new BarChartDataModel();
        BarChartDataModel cmModel = new BarChartDataModel();
        BarChartDataModel netIncomeModel = new BarChartDataModel();
        BarChartDataModel ebitdaModel = new BarChartDataModel();
        List<Object[]> sumValues = repository.findMonthSumValueForKPI(month,year);
        int firstMonth = (int) sumValues.get(0)[4];
        int secondMonth = (int) sumValues.get(1)[4];
        int thirdMonth = (int) sumValues.get(2)[4];

        int firstYear = (int) sumValues.get(0)[5];
        int secondYear = (int) sumValues.get(1)[5];
        int thirdYear = (int) sumValues.get(2)[5];
        double revenueFirst = ((BigDecimal) Utils.defaultIfNull(sumValues, 0, 0, DEFAULT_VALUE)).doubleValue();
        double revenueSecond = ((BigDecimal) Utils.defaultIfNull(sumValues, 1, 0, DEFAULT_VALUE)).doubleValue();
        double revenueThird = ((BigDecimal) Utils.defaultIfNull(sumValues, 2, 0, DEFAULT_VALUE)).doubleValue();
        String firstGroupName = "<b>vs Plan</b><br> (" + Utils.formatMonth(firstMonth) + " YTD'" + firstYear % 100 + " Pl / " + Utils.formatMonth(secondMonth) + " YTD'" + secondYear % 100 + ")";
        String secondGroupName = "<b>vs Forecast</b><br> (" + Utils.formatMonth(thirdMonth) + " YTD'" + thirdYear % 100 + " F / " + Utils.formatMonth(secondMonth) + " YTD'" + secondYear % 100 + ")";

        revenueModel.addData(firstGroupName, revenueFirst);
        revenueModel.addData(firstGroupName, revenueSecond);
        revenueModel.addData(secondGroupName, revenueThird);
        revenueModel.addData(secondGroupName, revenueSecond);

        double kpiFirst = ((BigDecimal) Utils.defaultIfNull(sumValues, 0, 2, DEFAULT_VALUE)).doubleValue();
        double kpiSecond = ((BigDecimal) Utils.defaultIfNull(sumValues, 1, 2, DEFAULT_VALUE)).doubleValue();
        double kpiThird = ((BigDecimal) Utils.defaultIfNull(sumValues, 2, 2, DEFAULT_VALUE)).doubleValue();

        cmModel.addData(firstGroupName, kpiFirst);
        cmModel.addData(firstGroupName, kpiSecond);
        cmModel.addData(secondGroupName, kpiThird);
        cmModel.addData(secondGroupName, kpiSecond);
        cmModel.addMetaData(PLAN_REVENUE_PERCENT, Utils.getSafePercent(kpiFirst, revenueFirst));
        cmModel.addMetaData(FACT_REVENUE_PERCENT, Utils.getSafePercent(kpiSecond, revenueSecond));
        cmModel.addMetaData(FORECAST_REVENUE_PERCENT, Utils.getSafePercent(kpiThird, revenueThird));
        kpiFirst = ((BigDecimal) Utils.defaultIfNull(sumValues, 0, 1, DEFAULT_VALUE)).doubleValue();
        kpiSecond = ((BigDecimal) Utils.defaultIfNull(sumValues, 1, 1, DEFAULT_VALUE)).doubleValue();
        kpiThird = ((BigDecimal) Utils.defaultIfNull(sumValues, 2, 1, DEFAULT_VALUE)).doubleValue();
        ebitdaModel.addData(firstGroupName, kpiFirst);
        ebitdaModel.addData(firstGroupName, kpiSecond);
        ebitdaModel.addData(secondGroupName, kpiThird);
        ebitdaModel.addData(secondGroupName, kpiSecond);
        ebitdaModel.addMetaData(PLAN_REVENUE_PERCENT, Utils.getSafePercent(kpiFirst, revenueFirst));
        ebitdaModel.addMetaData(FACT_REVENUE_PERCENT, Utils.getSafePercent(kpiSecond, revenueSecond));
        ebitdaModel.addMetaData(FORECAST_REVENUE_PERCENT, Utils.getSafePercent(kpiThird, revenueThird));

        kpiFirst = ((BigDecimal) Utils.defaultIfNull(sumValues, 0, 3, DEFAULT_VALUE)).doubleValue();
        kpiSecond = ((BigDecimal) Utils.defaultIfNull(sumValues, 1, 3, DEFAULT_VALUE)).doubleValue();
        kpiThird = ((BigDecimal) Utils.defaultIfNull(sumValues, 2, 3, DEFAULT_VALUE)).doubleValue();
        netIncomeModel.addData(firstGroupName, kpiFirst);
        netIncomeModel.addData(firstGroupName, kpiSecond);
        netIncomeModel.addData(secondGroupName, kpiThird);
        netIncomeModel.addData(secondGroupName, kpiSecond);
        netIncomeModel.addMetaData(PLAN_REVENUE_PERCENT, Utils.getSafePercent(kpiFirst, revenueFirst));
        netIncomeModel.addMetaData(FACT_REVENUE_PERCENT, Utils.getSafePercent(kpiSecond, revenueSecond));
        netIncomeModel
                .addMetaData(FORECAST_REVENUE_PERCENT, Utils.getSafePercent(kpiThird, revenueThird));


        result.add(revenueModel);
        result.add(cmModel);
        result.add(netIncomeModel);
        result.add(ebitdaModel);
        revenueModel.convert();

        netIncomeModel.convert();
        cmModel.convert();
        ebitdaModel.convert();
        return result;
    }
}
