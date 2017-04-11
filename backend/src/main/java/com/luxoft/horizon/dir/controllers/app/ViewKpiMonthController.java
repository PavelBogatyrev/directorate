package com.luxoft.horizon.dir.controllers.app;

import com.luxoft.horizon.dir.controllers.BaseController;
import com.luxoft.horizon.dir.service.app.IConfigurationService;
import com.luxoft.horizon.dir.service.domain.IViewKpiMonthService;
import com.luxoft.horizon.dir.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rlapin
 */
@RestController
@RequestMapping("/api")
public class ViewKpiMonthController extends BaseController {
    @Autowired
    IViewKpiMonthService service;
    @Autowired
    IConfigurationService configurationService;
    @RequestMapping("/kpiMonth/{activePeriod}")
    public String kpiMonth(@PathVariable String activePeriod) {
        Integer[] monthAndYear = Utils.getMonthAndYearFromString(activePeriod);
        return getLobMonth(monthAndYear);
    }

    @RequestMapping("/kpiMonth")
    public String kpiMonth() {

        Integer[] monthAndYear = configurationService.getMonthAndYear();
        return getLobMonth(monthAndYear);
    }

    private String getLobMonth(Integer[] monthAndYear) {
        int year = monthAndYear[1];
        int month = monthAndYear[0];
        return gson.toJson(service.getKpiMonth(month,year));
    }
}
