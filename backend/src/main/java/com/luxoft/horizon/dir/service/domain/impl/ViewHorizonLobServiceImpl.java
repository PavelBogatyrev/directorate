package com.luxoft.horizon.dir.service.domain.impl;

import com.luxoft.horizon.dir.service.app.IConfigurationService;
import com.luxoft.horizon.dir.service.domain.IViewHorizonLobService;
import com.luxoft.horizon.dir.service.domain.KPIRepository;
import com.luxoft.horizon.dir.utils.Budget;
import com.luxoft.horizon.dir.utils.RevenueCM;
import com.luxoft.horizon.dir.utils.Utils;
import com.luxoft.horizon.dir.widget.BarChartDataModel;
import com.luxoft.horizon.dir.widget.LineChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author rlapin
 */
@Service
class ViewHorizonLobServiceImpl extends ViewLobsBaseServiceImpl implements IViewHorizonLobService {
    private static final String CURRENCY_ACTUAL = "actual";
    private static final String CURRENCY_PLAN = "plan";
    static final String FINS = "FinS";
    private static final String ENTS = "EntS";
    public static final String FINS_WO_ENTS = "FINS without CoE";
    private static final String HORIZON = "FinS2 Horizon";
    @Autowired
    KPIRepository repository;
    @Autowired
    IConfigurationService config;


    @Override
    public Object horizonLobYear() {
        Object[] actualValues = repository.findHorizonLobYear(CURRENCY_ACTUAL);
        Object[] planValues = repository.findHorizonLobYear(CURRENCY_PLAN);
        return calculateValuesForLobs(actualValues, planValues, false);
    }

    @Override
    public Object horizonLobMonth(int month, int year) {
        Object[] actualValues = repository.findHorizonLobMonth(month,year,CURRENCY_ACTUAL);
        Object[] planValues = repository.findHorizonLobMonth(month,year,CURRENCY_PLAN);
        return calculateValuesForLobs(actualValues, planValues, true);
    }


    public void addPercentDataForBudget(LineChartModel model, Budget budget, Map<String, Map<Budget, RevenueCM>> values) {

        Map<Budget, RevenueCM> revenueByLobs = values.get(FINS);
        Map<Budget, RevenueCM> revenueByCoe = values.get(ENTS);
        if (revenueByLobs == null || revenueByLobs.get(budget) == null) {
            return;
        }
        RevenueCM revenueCMLob = revenueByLobs.get(budget);
        RevenueCM revenueCMCoe = new RevenueCM(0, 0);
        if (revenueByCoe != null && revenueByCoe.get(budget) != null) {
            revenueCMCoe = revenueByCoe.get(budget);
        }
        if (revenueCMCoe != null && revenueCMLob != null) {
            model.addData("FINS", Utils.getSafePercent(revenueCMLob.cm + revenueCMCoe.cm, revenueCMLob.revenue + revenueCMCoe.revenue));
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
        RevenueCM finsFF, plfinsFF;
        RevenueCM entsFF, plentsFF;
        RevenueCM finsPl, plfinsPl;
        RevenueCM entsPl, plentsPl;
        RevenueCM finsPF, plfinsPF;
        RevenueCM entsPF, plentsPF;
        String firstBudget = values.get(FINS).get(new Budget(Budget.FF))==null?Budget.FACT:Budget.FF;
        List<List<Double>> result = new ArrayList<>();
        //Total fins to plan
        //As is
        finsFF = values.get(FINS).get(new Budget(firstBudget));
        finsPF = values.get(FINS).get(new Budget(Budget.PF));
        finsPl = values.get(FINS).get(new Budget(Budget.PLAN));
        plfinsFF = planValues.get(FINS).get(new Budget(firstBudget));
        plfinsPF = planValues.get(FINS).get(new Budget(Budget.PF));
        plfinsPl = planValues.get(FINS).get(new Budget(Budget.PLAN));

        if (values.get(ENTS) == null) {
            entsFF = new RevenueCM(0, 0);
            entsPl = new RevenueCM(0, 0);
            entsPF = new RevenueCM(0, 0);
            plentsFF = new RevenueCM(0, 0);
            plentsPl = new RevenueCM(0, 0);
            plentsPF = new RevenueCM(0, 0);
        } else {
            entsFF = values.get(ENTS).get(new Budget(firstBudget));
            entsPF = values.get(ENTS).get(new Budget(Budget.PF));
            entsPl = values.get(ENTS).get(new Budget(Budget.PLAN));
            plentsFF = planValues.get(ENTS).get(new Budget(firstBudget));
            plentsPF = planValues.get(ENTS).get(new Budget(Budget.PF));
            plentsPl = planValues.get(ENTS).get(new Budget(Budget.PLAN));
        }


        double value;

        List<Double> resultElements = new ArrayList<>();
        //First row
        value = (finsFF.revenue + entsFF.revenue) - (finsPl.revenue + entsPl.revenue);
        resultElements.add(value);
        value = (finsFF.revenue + entsFF.revenue) - (finsPF.revenue + entsPF.revenue);
        resultElements.add(value);

        value = (plfinsFF.revenue + plentsFF.revenue) - (plfinsPl.revenue + plentsPl.revenue);
        resultElements.add(value);
        value = (plfinsFF.revenue + plentsFF.revenue) - (plfinsPF.revenue + plentsPF.revenue);
        resultElements.add(value);

        result.add(resultElements);

        //Second row
        resultElements = new ArrayList<>();
        value = (finsFF.cm + entsFF.cm) - (finsPl.cm + entsPl.cm);
        resultElements.add(value);
        value = (finsFF.cm + entsFF.cm) - (finsPF.cm + entsPF.cm);
        resultElements.add(value);

        value = (plfinsFF.cm + plentsFF.cm) - (plfinsPl.cm + plentsPl.cm);
        resultElements.add(value);
        value = (plfinsFF.cm + plentsFF.cm) - (plfinsPF.cm + plentsPF.cm);
        resultElements.add(value);

        result.add(resultElements);

        //Third row
        resultElements = new ArrayList<>();
        value = Utils.getSafePercent((finsFF.cm + entsFF.cm), (finsFF.revenue + entsFF.revenue)) - Utils.getSafePercent((finsPl.cm + entsPl.cm), (finsPl.revenue + entsPl.revenue));
        resultElements.add(value);
        value = Utils.getSafePercent((finsFF.cm + entsFF.cm), (finsFF.revenue + entsFF.revenue)) - Utils.getSafePercent((finsPF.cm + entsPF.cm), (finsPF.revenue + entsPF.revenue));
        resultElements.add(value);

        value = Utils.getSafePercent((plfinsFF.cm + plentsFF.cm), (plfinsFF.revenue + plentsFF.revenue)) - Utils.getSafePercent((plfinsPl.cm + plentsPl.cm), (plfinsPl.revenue + plentsPl.revenue));
        resultElements.add(value);
        value = Utils.getSafePercent((plfinsFF.cm + plentsFF.cm), (plfinsFF.revenue + plentsFF.revenue)) - Utils.getSafePercent((plfinsPF.cm + plentsPF.cm), (plfinsPF.revenue + plentsPF.revenue));
        resultElements.add(value);

        result.add(resultElements);

        return result;
    }


    /**
     * Get data from values for given budget and add to chartmodel
     *
     * @param model
     * @param budget
     * @param values
     */
    public void addDataForBudget(BarChartDataModel model, Budget budget, Map<String, Map<Budget, RevenueCM>> values) {
        String[] keys = {FINS, ENTS};
        for (String key : keys) {
            Map<Budget, RevenueCM> map = values.get(key);
            if (map != null) {
                map.keySet().stream().filter(b -> b.equals(budget)).forEach(b -> {
                    model.addData(b.getCaption(), map.get(b).revenue);
                });
            }

        }
    }


    @Override
    public Object horizonLobHC() {
        return lobsHC(HORIZON);
    }


}
