package com.luxoft.horizon.dir.service.domain.impl;

import com.google.common.collect.Lists;
import com.luxoft.horizon.dir.service.domain.KPIRepository;
import com.luxoft.horizon.dir.utils.Budget;
import com.luxoft.horizon.dir.utils.RevenueCM;
import com.luxoft.horizon.dir.widget.BarChartDataModel;
import com.luxoft.horizon.dir.widget.LineChartModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author rlapin
 */
public abstract class ViewLobsBaseServiceImpl {

    private static final List<String> LINE_CHART_ORDER = Collections.unmodifiableList(Arrays.asList(Budget.PFACT, Budget.PLAN, Budget.PF, Budget.FACT, Budget.FF));
    @Autowired
    KPIRepository repository;

    /**
     * Basic method that calculate and format data for revenue&cm charts and also deviations
     *
     * @param actualValues values with currency taken as actual
     * @param planValues   values with currency taken as plan
     * @return
     */
    public Object calculateValuesForLobs(Object[] actualValues, Object[] planValues, boolean monthCalculation) {
        BarChartDataModel revenueChartModel = new BarChartDataModel();
        LineChartModel cmChartModel = new LineChartModel();
        Object deviationsModel;
        Map<String, Map<Budget, RevenueCM>> values = getKPIValues(actualValues);
        Map<String, Map<Budget, RevenueCM>> plvalues = getKPIValues(planValues);
        addDataForBudget(revenueChartModel, new Budget(Budget.PFACT), values);
        addDataForBudget(revenueChartModel, new Budget(Budget.PLAN), values);
        addDataForBudget(revenueChartModel, new Budget(Budget.PF), values);
        addDataForBudget(revenueChartModel, new Budget(Budget.FACT), values);
        addDataForBudget(revenueChartModel, new Budget(Budget.FF), values);

        addPercentDataForBudget(cmChartModel, new Budget(Budget.PFACT), values);
        if (monthCalculation) {
            addPercentDataForBudget(cmChartModel, new Budget(Budget.PLAN), values);
            addPercentDataForBudget(cmChartModel, new Budget(Budget.PF), values);
            addPercentDataForBudget(cmChartModel, new Budget(Budget.FACT), values);
        } else {
            addPercentDataForBudget(cmChartModel, new Budget(Budget.FACT), values);
            addPercentDataForBudget(cmChartModel, new Budget(Budget.PLAN), values);
            addPercentDataForBudget(cmChartModel, new Budget(Budget.PF), values);
        }
        addPercentDataForBudget(cmChartModel, new Budget(Budget.FF), values);

        revenueChartModel.setSeriesNames(Lists.newArrayList(values.keySet()));
        List<String> labels = new ArrayList<>();
        values.entrySet().stream().findFirst().ifPresent((value) -> labels.addAll(value.getValue().keySet().stream().map(Budget::getCaption).collect(Collectors.toList())));


        cmChartModel.setLabels(labels);
        revenueChartModel.convert();

        Map<String, Object> resultValues = new HashMap<>();
        resultValues.put("revenue", revenueChartModel);
        resultValues.put("cm", cmChartModel);

        deviationsModel = calculateDeviations(values, plvalues);
        resultValues.put("deviations", deviationsModel);
        return resultValues;
    }

    private int compare(Budget a, Budget b) {
        return LINE_CHART_ORDER.indexOf(a.getBudget()) - LINE_CHART_ORDER.indexOf(b.getBudget());
    }


    /**
     * Get values grouped by kpi
     *
     * @param inputValues [KPITYPE,REVENUE,CM,BUDGET,YEAR,CURRENCY]
     * @return ordered values grouped by kpi
     */
    public Map<String, Map<Budget, RevenueCM>> getKPIValues(Object[] inputValues) {
        Map<String, Map<Budget, RevenueCM>> values = new LinkedHashMap<>();
        for (Object obj : inputValues) {
            if (obj instanceof Object[]) {
                Object[] objValues = (Object[]) obj;
                if (objValues.length > 5) {
                    String kpiType = (String) objValues[0];
                    Map<Budget, RevenueCM> kpiValues = values.getOrDefault(kpiType, new TreeMap<>(this::compare));
                    Budget budget = new Budget((String) objValues[3]);
                    if (objValues.length == 6) {
                        budget.setYear((Integer) objValues[4]);
                    } else {
                        budget.setYear((Integer) objValues[5]);
                        budget.setMonth((Integer) objValues[4]);
                    }
                    kpiValues.put(budget, new RevenueCM(((BigDecimal) objValues[2]).doubleValue(), ((BigDecimal) objValues[1]).doubleValue()));
                    values.putIfAbsent(kpiType, kpiValues);
                }
            }
        }
        return values;
    }

    public Object lobsHC(String lobName) {
        LineChartModel chartModel = new LineChartModel();
        Object[] objects = repository.findHeadCountByLobs(lobName);
        List<String> series = new ArrayList<>();
        for (int i = objects.length - 1; i >= 0; i--) {
            Object obj = objects[i];
            if (obj instanceof Object[]) {
                Object[] objValues = (Object[]) obj;
                chartModel.addData("data", ((BigDecimal) objValues[2]).doubleValue());
                series.add(((String) objValues[1]).replace(" ", "<br>"));
            }
        }

        chartModel.setLabels(series);
        return chartModel;
    }

    protected abstract Object calculateDeviations(Map<String, Map<Budget, RevenueCM>> values, Map<String, Map<Budget, RevenueCM>> plvalues);

    protected abstract void addPercentDataForBudget(LineChartModel cmChartModel, Budget budget, Map<String, Map<Budget, RevenueCM>> values);

    protected abstract void addDataForBudget(BarChartDataModel revenueChartModel, Budget budget, Map<String, Map<Budget, RevenueCM>> values);
}
