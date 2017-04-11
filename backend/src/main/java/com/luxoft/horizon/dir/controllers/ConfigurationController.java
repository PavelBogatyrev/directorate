package com.luxoft.horizon.dir.controllers;

import com.luxoft.horizon.dir.entities.app.Configuration;
import com.luxoft.horizon.dir.entities.app.Label;
import com.luxoft.horizon.dir.entities.app.Period;
import com.luxoft.horizon.dir.service.app.ConfigurationRepository;
import com.luxoft.horizon.dir.service.app.LabelRepository;
import com.luxoft.horizon.dir.service.domain.IReportPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by pavelbogatyrev on 17/04/16.
 */
@RestController
public class ConfigurationController extends BaseController {

    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private ConfigurationRepository configurationRepository;
    @Autowired
    IReportPeriodService service;

    // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    // Configuration
    // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    @RequestMapping(value = "/configuration", method = RequestMethod.GET)
    public String configuration() {
        Configuration cfg = new Configuration();
        Iterable<Configuration> iterable = configurationRepository.findAll();
        if(iterable.iterator().hasNext()) {
            cfg = iterable.iterator().next();
        }

        if (cfg.getCurrentPeriod()==null) {
            List<Period> periods = getPeriods();
            String activePeriod = periods.stream().findFirst().get().getValue();
            cfg.setCurrentPeriod(activePeriod);
        }
        return gson.toJson(cfg);
    }

    @RequestMapping(value = "/configuration", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Configuration update(@RequestBody String activePeriod) {
        Configuration update;
        Iterator<Configuration> iterator = configurationRepository.findAll().iterator();
        if (iterator.hasNext()) {
            update = iterator.next();
        }else{
            update = new Configuration();
        }
        update.setCurrentPeriod(activePeriod);
        return configurationRepository.save(update);
    }

    @RequestMapping("/labels")
    public String cash() {
        Iterable<Label> list = labelRepository.findAll();
        return gson.toJson(list);
    }

    @RequestMapping("/api/periods")
    public List<Period> getPeriods() {
        return service.getReportPeriods().stream().map((date) -> {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH) + 1;
            return new Period(calendar.get(Calendar.YEAR)+""+((month<10)?"0":"")+month);
        }).collect(Collectors.toList());
    }


}
