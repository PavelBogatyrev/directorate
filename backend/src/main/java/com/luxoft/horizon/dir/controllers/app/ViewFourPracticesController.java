package com.luxoft.horizon.dir.controllers.app;

import com.luxoft.horizon.dir.controllers.BaseController;
import com.luxoft.horizon.dir.service.domain.IViewFourPracticesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rlapin
 */
@RestController
@RequestMapping("/api")
public class ViewFourPracticesController extends BaseController{
    @Autowired
    IViewFourPracticesService service;

    @RequestMapping("/fourPractices")
    public String getDataForFourPractices() {
        return gson.toJson(service.getDataForFourPractices());
    }
}
