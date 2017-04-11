package com.luxoft.horizon.dir.service.domain;

import com.luxoft.horizon.dir.widget.BarChartDataModel;

import java.util.List;

/**
 * @author rlapin
 */
public interface IViewKpiYearService {
    List<BarChartDataModel> getKpiYear();
}
