package com.luxoft.horizon.dir.controllers.app;

import com.luxoft.horizon.dir.controllers.BaseController;
import com.luxoft.horizon.dir.service.app.IConfigurationService;
import com.luxoft.horizon.dir.service.domain.IViewHorizonLobService;
import com.luxoft.horizon.dir.service.domain.IViewLobsService;
import com.luxoft.horizon.dir.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rlapin
 */
@RestController
@RequestMapping("/api")
public class ViewHorizonLobController extends BaseController{



    @Autowired
    IViewHorizonLobService service;
    @Autowired
    IConfigurationService configurationService;



    @RequestMapping("/horizonLobYear")
    public String horizonLobYear(){
        return gson.toJson(service.horizonLobYear());
    }

    @RequestMapping("/horizonLobMonth/{activePeriod}")
    public String horizonLobMonth(@PathVariable String activePeriod) {
        Integer[] monthAndYear = Utils.getMonthAndYearFromString(activePeriod);
        return getLobMonth(monthAndYear);
    }
    @RequestMapping("/horizonLobMonth")
    public String horizonLobMonth() {
        Integer[] monthAndYear = configurationService.getMonthAndYear();
        return getLobMonth(monthAndYear);
    }

    private String getLobMonth(Integer[] monthAndYear) {
        int year = monthAndYear[1];
        int month = monthAndYear[0];
        return gson.toJson(service.horizonLobMonth(month,year));
    }

    @RequestMapping("/horizonLobHC")
    public String horizonLobHC() {
        return gson.toJson(service.horizonLobHC());
    }



}
