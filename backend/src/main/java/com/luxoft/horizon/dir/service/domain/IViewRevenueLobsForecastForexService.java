package com.luxoft.horizon.dir.service.domain;

import com.luxoft.horizon.dir.widget.BasicChartModel;

/**
 * @author rlapin
 */
public interface IViewRevenueLobsForecastForexService {
    BasicChartModel getCMByLobs();
    BasicChartModel getRevenueByLobs();
    BasicChartModel getRevenueGCoEByLobs();
    BasicChartModel getCMGCoEByLobs();
}
