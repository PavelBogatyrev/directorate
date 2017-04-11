package com.luxoft.horizon.dir.controllers.app;

import com.luxoft.horizon.dir.controllers.BaseController;
import com.luxoft.horizon.dir.service.app.IConfigurationService;
import com.luxoft.horizon.dir.service.domain.IViewGlobalLobService;
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
public class ViewGlobalLobController extends BaseController {


    @Autowired
    IViewGlobalLobService service;
    @Autowired
    IConfigurationService configurationService;


    @RequestMapping("/globalLobYear")
    public String globalLobYear() {
        return gson.toJson(service.globalLobYear());
    }

    @RequestMapping("/globalLobMonth/{activePeriod}")
    public String globalLobMonth(@PathVariable String activePeriod) {
        Integer[] monthAndYear = Utils.getMonthAndYearFromString(activePeriod);
        return getLobMonth(monthAndYear);
    }

    private String getLobMonth(Integer[] monthAndYear) {
        int year = monthAndYear[1];
        int month = monthAndYear[0];
        return gson.toJson(service.globalLobMonth(month,year));
    }

    @RequestMapping("/globalLobMonth")
    public String globalLobMonth() {
        Integer[] monthAndYear = configurationService.getMonthAndYear();
        return getLobMonth(monthAndYear);
    }
    @RequestMapping("/globalLobHC")
    public String globalLobHC() {
        return gson.toJson(service.globalLobHC());
    }


}
