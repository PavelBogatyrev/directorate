package com.luxoft.horizon.dir.controllers.app;

import com.luxoft.horizon.dir.controllers.BaseController;
import com.luxoft.horizon.dir.service.app.IConfigurationService;
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
public class ViewLobsController extends BaseController{



    @Autowired
    IViewLobsService service;
    @Autowired
    IConfigurationService configurationService;


    @RequestMapping("/lobsYear/{lobName}")
    public String lobsYear(@PathVariable String lobName){
        return gson.toJson(service.lobsYear(lobName));
    }

    @RequestMapping("/lobsMonth/{lobName}")
    public String lobsMonth(@PathVariable String lobName) {
        Integer[] monthAndYear = configurationService.getMonthAndYear();
        return getLobsMonth(lobName, monthAndYear);
    }
    @RequestMapping("/lobsMonth/{lobName}/{activePeriod}")
    public String lobsMonth(@PathVariable String lobName,@PathVariable String activePeriod) {
        Integer[] monthAndYear = Utils.getMonthAndYearFromString(activePeriod);
        return getLobsMonth(lobName, monthAndYear);
    }

    private String getLobsMonth(@PathVariable String lobName, Integer[] monthAndYear) {
        int month = monthAndYear[0];
        int year = monthAndYear[1];
        return gson.toJson(service.lobsMonth(lobName,month,year));
    }

    @RequestMapping("/lobsHC/{lobName}")
    public String lobsHC(@PathVariable String lobName) {
        return gson.toJson(service.lobsHC(lobName));
    }



}
