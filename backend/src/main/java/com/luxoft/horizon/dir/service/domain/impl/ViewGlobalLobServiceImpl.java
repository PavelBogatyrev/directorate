package com.luxoft.horizon.dir.service.domain.impl;

import com.luxoft.horizon.dir.service.app.IConfigurationService;
import com.luxoft.horizon.dir.service.domain.IViewGlobalLobService;
import com.luxoft.horizon.dir.service.domain.KPIRepository;
import com.luxoft.horizon.dir.utils.Budget;
import com.luxoft.horizon.dir.utils.RevenueCM;
import com.luxoft.horizon.dir.utils.Utils;
import com.luxoft.horizon.dir.widget.BarChartDataModel;
import com.luxoft.horizon.dir.widget.LineChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author rlapin
 */
@Service
class ViewGlobalLobServiceImpl extends ViewLobsBaseServiceImpl implements IViewGlobalLobService {
    private static final String CURRENCY_ACTUAL = "actual";
    private static final String CURRENCY_PLAN = "plan";
    private static final String GLOBAL = "GLOBAL CENTERS OF EXPERTISE";
    @Autowired
    KPIRepository repository;
    @Autowired
    IConfigurationService config;


    @Override
    public Object globalLobYear() {
        Object[] actualValues = repository.findGlobalLobYear(CURRENCY_ACTUAL);
        Object[] planValues = repository.findGlobalLobYear(CURRENCY_PLAN);
        return calculateValuesForLobs(actualValues, planValues,false);
    }


    @Override
    public Object globalLobMonth(int month, int year) {
        Object[] actualValues = repository.findGlobalLobMonth(month,year,CURRENCY_ACTUAL);
        Object[] planValues = repository.findGlobalLobMonth(month,year,CURRENCY_PLAN);
        return calculateValuesForLobs(actualValues, planValues,true);
    }


    public void addPercentDataForBudget(LineChartModel model, Budget budget, Map<String, Map<Budget, RevenueCM>> values) {

        Set<String> keys = values.keySet();
        RevenueCM revenueCM = new RevenueCM(0, 0);
        boolean isBudgetExists = false;
        for (String key : keys) {
            if (values.get(key).get(budget) != null) {
                isBudgetExists = true;
                revenueCM.cm += values.get(key).get(budget).cm;
                revenueCM.revenue += values.get(key).get(budget).revenue;
            }
        }
        if(isBudgetExists) {
            model.addData("GCoE", Utils.getSafePercent(revenueCM.cm, revenueCM.revenue));
        }
    }

    /**
     * Calculate deviations using plan and actual values
     *
     * @param values
     * @param planValues
     * @return
     */
    public Object calculateDeviations(Map<String, Map<Budget, RevenueCM>> values, Map<String, Map<Budget, RevenueCM>> planValues) {

        List<List<Double>> result = new ArrayList<>();
        String budget =values.entrySet().stream().findFirst().get().getValue().get(new Budget(Budget.FF))==null?Budget.FACT:Budget.FF;
        RevenueCM revenueCMFF = getAccRevenueCM(values, new Budget(budget));
        RevenueCM revenueCMPF = getAccRevenueCM(values, new Budget(Budget.PF));
        RevenueCM revenueCMPL = getAccRevenueCM(values, new Budget(Budget.PLAN));
        RevenueCM planRevenueCMFF = getAccRevenueCM(planValues, new Budget(budget));
        RevenueCM planRevenueCMPF = getAccRevenueCM(planValues, new Budget(Budget.PF));
        RevenueCM planRevenueCMPL = getAccRevenueCM(planValues, new Budget(Budget.PLAN));


        double value;

        List<Double> resultElements = new ArrayList<>();
        //First row
        value = (revenueCMFF.revenue) - (revenueCMPL.revenue);
        resultElements.add(value);
        value = (revenueCMFF.revenue) - (revenueCMPF.revenue);
        resultElements.add(value);

        value = (planRevenueCMFF.revenue) - (planRevenueCMPL.revenue);
        resultElements.add(value);
        value = (planRevenueCMFF.revenue) - (planRevenueCMPF.revenue);
        resultElements.add(value);

        result.add(resultElements);

        //Second row
        resultElements = new ArrayList<>();
        value = (revenueCMFF.cm) - (revenueCMPL.cm);
        resultElements.add(value);
        value = (revenueCMFF.cm) - (revenueCMPF.cm);
        resultElements.add(value);

        value = (planRevenueCMFF.cm) - (planRevenueCMPL.cm);
        resultElements.add(value);
        value = (planRevenueCMFF.cm) - (planRevenueCMPF.cm);
        resultElements.add(value);


        result.add(resultElements);
        //Third row
        resultElements = new ArrayList<>();
        value = Utils.getSafePercent(revenueCMFF.cm, revenueCMFF.revenue) - Utils.getSafePercent(revenueCMPL.cm, revenueCMPL.revenue);
        resultElements.add(value);
        value = Utils.getSafePercent(revenueCMFF.cm, revenueCMFF.revenue) - Utils.getSafePercent(revenueCMPF.cm, revenueCMPF.revenue);
        resultElements.add(value);

        value = Utils.getSafePercent(planRevenueCMFF.cm, planRevenueCMFF.revenue) - Utils.getSafePercent(planRevenueCMPL.cm, planRevenueCMPL.revenue);
        resultElements.add(value);
        value = Utils.getSafePercent(planRevenueCMFF.cm, planRevenueCMFF.revenue) - Utils.getSafePercent(planRevenueCMPF.cm, planRevenueCMPF.revenue);
        resultElements.add(value);
        result.add(resultElements);
        return result;
    }

    /**
     * Calculate acc value for budget using values
     *
     * @param values
     * @param budget
     * @return
     */
    private RevenueCM getAccRevenueCM(Map<String, Map<Budget, RevenueCM>> values, Budget budget) {
        Set<String> keys = values.keySet();
        RevenueCM revenueCM = new RevenueCM(0, 0);
        for (String key : keys) {
            if (values.get(key).get(budget) != null) {
                revenueCM.cm += values.get(key).get(budget).cm;
                revenueCM.revenue += values.get(key).get(budget).revenue;
            }
        }
        return revenueCM;
    }


    /**
     * Get data from values for given budget and add to chartmodel
     *
     * @param model
     * @param budget
     * @param values
     */
    public void addDataForBudget(BarChartDataModel model, Budget budget, Map<String, Map<Budget, RevenueCM>> values) {
        Set<String> keys = values.keySet();
        for (String key : keys) {
            Map<Budget, RevenueCM> map = values.get(key);
            map.keySet().stream().filter(b -> b.equals(budget)).forEach(b -> {
                model.addData(b.getCaption(), map.get(b).revenue);
            });

        }
    }


    @Override
    public Object globalLobHC() {
        return lobsHC(GLOBAL);
    }


}
