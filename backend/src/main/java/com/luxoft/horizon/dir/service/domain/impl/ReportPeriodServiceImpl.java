package com.luxoft.horizon.dir.service.domain.impl;

import com.luxoft.horizon.dir.service.domain.IReportPeriodService;
import com.luxoft.horizon.dir.service.domain.KPIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author rlapin
 */
@Service
public class ReportPeriodServiceImpl implements IReportPeriodService{
    @Autowired
    KPIRepository repository;
    @Override
    public List<Date> getReportPeriods() {
        return repository.getReportPeriods();
    }
}
