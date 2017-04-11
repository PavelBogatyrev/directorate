package com.luxoft.horizon.dir.service.domain.impl;

import com.luxoft.horizon.dir.service.domain.IViewKpiYearService;
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
public class ViewKpiYearServiceImpl implements IViewKpiYearService {
    public static final String PLAN_REVENUE_PERCENT = "PLAN_REVENUE_PERCENT";
    public static final String FACT_REVENUE_PERCENT = "FACT_REVENUE_PERCENT";
    public static final String FORECAST_REVENUE_PERCENT = "FORECAST_REVENUE_PERCENT";
    public static final BigDecimal DEFAULT_VALUE = BigDecimal.valueOf(0);

    @Autowired
    KPIRepository repository;
    @Override
    public List<BarChartDataModel> getKpiYear() {

        List<BarChartDataModel> result = new LinkedList<>();
        BarChartDataModel revenueModel = new BarChartDataModel();
        BarChartDataModel cmModel = new BarChartDataModel();
        BarChartDataModel netIncomeModel = new BarChartDataModel();
        BarChartDataModel ebitdaModel = new BarChartDataModel();
        List<Object[]> sumValues = repository.findYearSumValueForKPI();

        double revenueFirst = ((BigDecimal) Utils.defaultIfNull(sumValues, 0, 0, DEFAULT_VALUE)).doubleValue();
        double revenueSecond = ((BigDecimal) Utils.defaultIfNull(sumValues, 1, 0, DEFAULT_VALUE)).doubleValue();
        double revenueThird = ((BigDecimal) Utils.defaultIfNull(sumValues, 2, 0, DEFAULT_VALUE)).doubleValue();
        int firstYear = (int) sumValues.get(0)[4];
        int secondYear = (int) sumValues.get(1)[4];
        int thirdYear = (int) sumValues.get(2)[4];

        String firstGroupName = "<b>vs Plan</b><br> (" + Utils.formatYear(firstYear) + " Pl / " + Utils.formatYear(secondYear) + " F)";
        String secondGroupName = "<b>vs previous Fact</b><br> (" + Utils.formatYear(thirdYear) + " / " + Utils.formatYear(secondYear) + " F)";
        revenueModel.addData(firstGroupName, revenueFirst);
        revenueModel.addData(firstGroupName, revenueSecond);
        revenueModel.addData(secondGroupName, revenueThird);
        revenueModel.addData(secondGroupName, revenueSecond);
        double kpiFirst = ((BigDecimal) Utils.defaultIfNull(sumValues, 0, 1, DEFAULT_VALUE)).doubleValue();
        double kpiSecond = ((BigDecimal) Utils.defaultIfNull(sumValues, 1, 1, DEFAULT_VALUE)).doubleValue();
        double kpiThird = ((BigDecimal) Utils.defaultIfNull(sumValues, 2, 1, DEFAULT_VALUE)).doubleValue();

        cmModel.addData(firstGroupName, kpiFirst);
        cmModel.addData(firstGroupName, kpiSecond);
        cmModel.addData(secondGroupName, kpiThird);
        cmModel.addData(secondGroupName, kpiSecond);
        cmModel.addMetaData(PLAN_REVENUE_PERCENT, Utils.getSafePercent(kpiFirst, revenueFirst));
        cmModel.addMetaData(FACT_REVENUE_PERCENT, Utils.getSafePercent(kpiSecond, revenueSecond));
        cmModel.addMetaData(FORECAST_REVENUE_PERCENT, Utils.getSafePercent(kpiThird, revenueThird));
        kpiFirst = ((BigDecimal) Utils.defaultIfNull(sumValues, 0, 2, DEFAULT_VALUE)).doubleValue();
        kpiSecond = ((BigDecimal) Utils.defaultIfNull(sumValues, 1, 2, DEFAULT_VALUE)).doubleValue();
        kpiThird = ((BigDecimal) Utils.defaultIfNull(sumValues, 2, 2, DEFAULT_VALUE)).doubleValue();
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
