package com.luxoft.horizon.dir.service.domain.impl;

import com.luxoft.horizon.dir.Application;
import com.luxoft.horizon.dir.entities.domain.KPI;
import com.luxoft.horizon.dir.service.domain.KPIRepository;
import com.luxoft.horizon.dir.service.utils.TestUtils;
import com.luxoft.horizon.dir.widget.BarChartDataModel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
import java.util.*;

import static org.junit.Assert.*;

/**
 * @author rlapin
 */
@RunWith(SpringJUnit4ClassRunner.class)

@SpringApplicationConfiguration(Application.class)
public class DirectorateServiceImplTest {

    public static final String TEST_DATA_FILENAME = "random.xlsx";
    public static final String FACT = "Fact";
    public static final String PLAN = "Plan";
    public static final String FORECAST = "Forecast";
    public static final String FACT_REVENUE_PERCENT = "FACT_REVENUE_PERCENT";
    public static final String PLAN_REVENUE_PERCENT = "PLAN_REVENUE_PERCENT";
    public static final String FORECAST_REVENUE_PERCENT = "FORECAST_REVENUE_PERCENT";
    @Autowired
    KPIRepository kpiRepository;

    @Before
    public void setUp() throws Exception {
        kpiRepository.deleteAll();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(TEST_DATA_FILENAME);
        if(inputStream != null){
            saveDataToDB(inputStream);
        }

    }

    private void saveDataToDB(InputStream inputStream) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        for(int i=1;i<sheet.getLastRowNum();i++){
            Row row = sheet.getRow(i);
            KPI kpi = new KPI();
            int cell = 0;
            kpi.setTimeline(row.getCell(cell++).getDateCellValue());
            kpi.setReportdate(row.getCell(cell++).getDateCellValue());
            kpi.setReportType(row.getCell(cell++).getStringCellValue());
            kpi.setCurrency(row.getCell(cell++).getStringCellValue());
            kpi.setBudget(row.getCell(cell++).getStringCellValue());
            kpi.setCmFact(row.getCell(cell++).getNumericCellValue());
            kpi.setCmPLan(row.getCell(cell++).getNumericCellValue());
            kpi.setCmPreviousFactForecast(row.getCell(cell++).getNumericCellValue());
            kpi.setRevenueFact(row.getCell(cell++).getNumericCellValue());
            kpi.setRevenuePlan(row.getCell(cell++).getNumericCellValue());
            kpi.setRevenuePreviousFactForecast(row.getCell(cell++).getNumericCellValue());
            kpi.setEBITDAFact(row.getCell(cell++).getNumericCellValue());
            kpi.setEBITDAPlan(row.getCell(cell++).getNumericCellValue());
            kpi.setEBITDAPreviousFactForecast(row.getCell(cell++).getNumericCellValue());
            kpi.setNetIncomeFact(row.getCell(cell++).getNumericCellValue());
            kpi.setNetIncomePlan(row.getCell(cell++).getNumericCellValue());
            kpi.setNetIncomePreviousFactForecast(row.getCell(cell).getNumericCellValue());
            kpiRepository.save(kpi);
        }

    }

    public static void main(String[] args) throws IOException {
        TestUtils.generateRandomXlsData();
    }





}