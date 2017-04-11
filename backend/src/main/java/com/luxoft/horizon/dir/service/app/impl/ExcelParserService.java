package com.luxoft.horizon.dir.service.app.impl;

import com.luxoft.horizon.dir.DirException;
import com.luxoft.horizon.dir.entities.app.Label;
import com.luxoft.horizon.dir.service.app.ExcelParserException;
import com.luxoft.horizon.dir.service.app.LabelRepository;
import com.luxoft.horizon.dir.service.app.ScreenshotRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bogatp on 30.03.16.
 */
@Service
public class ExcelParserService {

    public static final String LABELS = "LABELS";
    public static final String AVAILABLE_PER = "AVAILABLE_PER";
    public static final String MAP_COUNTRIES = "MAP_COUNTRIES";
    public static final String CLIENTS = "CLIENTS";
    public static final String CURRENCIES = "CURRENCIES";

    private static final Logger LOG = LoggerFactory.getLogger(ExcelParserService.class);
    public static final String REVENUE = "REVENUE";
    public static final String EXPENSES = "EXPENSES";
    private final DataFormatter df = new DataFormatter();
    private XSSFWorkbook wb;
    private FormulaEvaluator evaluator;


    // Dont make it @Autowired to orchestrate transaction
    private LabelRepository labelRepository;
    private ScreenshotRepository screenshotRepository;

    @Autowired
    public ExcelParserService(
            LabelRepository labelRepository,
            ScreenshotRepository screenshotRepository
    ) {
        this.labelRepository = labelRepository;
        this.screenshotRepository = screenshotRepository;
    }

    public void loadFile(String fileName) throws Exception {
        loadFile(new FileInputStream(new File(fileName)));
    }

    public void loadFile(InputStream is) throws Exception {
        try {
            wb = new XSSFWorkbook(is);
            evaluator = wb.getCreationHelper().createFormulaEvaluator();
        } catch (IOException e) {
            throw new ExcelParserException("Error parsing excel file caused by: " + e.getMessage(), e);
        }
    }

    @Transactional(rollbackFor = {DirException.class})
    public void importData() throws Exception {
        try {
            LOG.info("Cleaning repositories...");
            labelRepository.deleteAll();
            screenshotRepository.deleteAll();

            LOG.info("Importing labels...");
            labelRepository.save(parseLabels());


        } catch (Exception e) {
            throw new DirException("Error importing data, caused by: " + e.getMessage());
        }
    }


    public List<Label> parseLabels() throws Exception {
        LinkedList<Label> list = new LinkedList<>();
        int rowIndex = 0;
        int columnIndex = 0;
        Sheet sheet = null;
        try {
            sheet = wb.getSheet(LABELS);

            // Parse overrides



            for (rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                columnIndex = 0;
                Row row = sheet.getRow(rowIndex);

                double id = row.getCell(columnIndex++).getNumericCellValue();
                String name = df.formatCellValue(row.getCell(columnIndex++));
                String label = df.formatCellValue(row.getCell(columnIndex++));
                Label l = new Label(String.valueOf(id), name, label);


                list.add(l);
            }
        } catch (Exception e) {
            throw new DirException(MessageFormat.format("Error parsing excel, sheet: {0}, row: {1}, column: {2}, caused by: {3}",
                    sheet.getSheetName(), rowIndex, columnIndex, e.getMessage()));
        }
        return list;
    }



    private String stringNullSafeGet(Cell cell) {
        String val = "";
        try {
            val = df.formatCellValue(cell);
        } catch (Exception e) {
            LOG.info("Error obtaining value from a cell: " + cell);
            return "";
        }
        return val;
    }

    private double numNullSafeGet(Cell cell) {
        double result = 0.0;
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    result = cell.getNumericCellValue();
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                case Cell.CELL_TYPE_ERROR:
                    System.out.println(cell.getErrorCellValue());
                    break;
                // CELL_TYPE_FORMULA will never occur
                case Cell.CELL_TYPE_FORMULA:
                    evaluator.evaluateFormulaCell(cell);
                    result = cell.getNumericCellValue();
                    break;
            }
        }
        return result;
    }

    public boolean checkWithRegExp(String sheetName) {
        Pattern p = Pattern.compile("\\d{4}Q\\d");
        Matcher m = p.matcher(sheetName);
        return m.matches();
    }


}
