package com.luxoft.horizon.dir.controllers.app;

import com.luxoft.horizon.dir.controllers.BaseController;
import com.luxoft.horizon.dir.service.domain.IViewLobsService;
import com.luxoft.horizon.dir.service.domain.IViewPracticesService;
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
public class ViewPracticesController extends BaseController{



    @Autowired
    IViewPracticesService service;



    @RequestMapping("/practiceData/{practiceName}")
    public String practiceData(@PathVariable String practiceName){
        return gson.toJson(service.practiceData(practiceName));
    }

    @RequestMapping("/practiceHC/{practiceName}")
    public String lobsHC(@PathVariable String practiceName) {
        return gson.toJson(service.practiceHC(practiceName));
    }



}
