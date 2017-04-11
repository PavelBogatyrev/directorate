package com.luxoft.horizon.dir.controllers.app;

import com.luxoft.horizon.dir.controllers.BaseController;
import com.luxoft.horizon.dir.service.domain.IViewKpiYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rlapin
 */
@RestController
@RequestMapping("/api")
public class ViewKpiYearController extends BaseController {
    @Autowired
    IViewKpiYearService service;

    @RequestMapping("/kpiYear")
    public String kpiYear() {
        return gson.toJson(service.getKpiYear());
    }
}
