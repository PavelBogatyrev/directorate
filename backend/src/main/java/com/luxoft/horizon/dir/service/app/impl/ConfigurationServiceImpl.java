package com.luxoft.horizon.dir.service.app.impl;

import com.luxoft.horizon.dir.entities.app.Configuration;
import com.luxoft.horizon.dir.service.app.ConfigurationRepository;
import com.luxoft.horizon.dir.service.app.IConfigurationService;
import com.luxoft.horizon.dir.service.domain.IReportPeriodService;
import com.luxoft.horizon.dir.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author rlapin
 */
@Service
public class ConfigurationServiceImpl implements IConfigurationService {
    @Autowired
    IReportPeriodService service;
    @Autowired
    ConfigurationRepository repository;

    @Override
    public Integer[] getMonthAndYear() {
        Iterator<Configuration> iterator = repository.findAll().iterator();
        if (iterator.hasNext()) {
            Configuration val = iterator.next();
            String activePeriod = val.getCurrentPeriod();
            if(activePeriod !=null && !activePeriod.isEmpty()) {
                return Utils.getMonthAndYearFromString(activePeriod);
            }
        }
        return null;
    }

}
