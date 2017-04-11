package com.luxoft.horizon.dir.service.domain;

import com.luxoft.horizon.dir.widget.BasicChartModel;

/**
 * @author rlapin
 */
public interface IViewRevenueLobsVsplanForexService {
    BasicChartModel getCMByLobsVsplan();
    BasicChartModel getRevenueByLobsVsplan();
    BasicChartModel getRevenueGCoEByLobsVsplan();
    BasicChartModel getCMGCoEByLobsVsplan();
}
