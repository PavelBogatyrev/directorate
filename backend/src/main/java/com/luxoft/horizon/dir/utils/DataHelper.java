package com.luxoft.horizon.dir.utils;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bogatp on 31.03.16.
 */
public class DataHelper {

    public void copyData(InputStream source, InputStream target) throws Exception {
        Workbook wbSource = new XSSFWorkbook(source);
        Workbook wbTarget = new XSSFWorkbook(target);

        Sheet sourceSheet = null;
        int sheetIndex = 0;
        int cellIndex = 0;
        int rowIndex = 0;
        try {
            for (sheetIndex = 0; sheetIndex < wbSource.getNumberOfSheets(); sheetIndex++) {
                sourceSheet = wbSource.getSheetAt(sheetIndex);
                if (!checkWithRegExp(sourceSheet.getSheetName())) {
                    continue;
                }

                Sheet targetSheet = wbTarget.getSheetAt(sheetIndex);

                for (rowIndex = 59; rowIndex < 148; rowIndex++) {
//                for (rowIndex = 120; rowIndex < 148; rowIndex++) {
                    Row sourceRow = sourceSheet.getRow(rowIndex);
                    Row targetRow = null;
                    if (rowIndex <= targetSheet.getLastRowNum()) {
                        targetRow = targetSheet.getRow(rowIndex);
                    } else {
                        targetRow = targetSheet.createRow(rowIndex);
                    }

                    for (cellIndex = 0; cellIndex < 8; cellIndex++) {
                        Object cellValue = null;
                        if (cellIndex < 3) {
                            cellValue = sourceRow.getCell(cellIndex).getStringCellValue();

                            Cell targetCell = null;
                            if (targetRow.getLastCellNum() - 1 < cellIndex) {
                                targetCell = targetRow.createCell(cellIndex, Cell.CELL_TYPE_STRING);
                            } else {
                                targetCell = targetRow.getCell(cellIndex);
                            }
                            targetCell.setCellValue((String) cellValue);

                        } else {
                            cellValue = sourceRow.getCell(cellIndex).getNumericCellValue();

                            Cell targetCell = null;
                            if (targetRow.getLastCellNum() - 1 < cellIndex) {
                                targetCell = targetRow.createCell(cellIndex, Cell.CELL_TYPE_NUMERIC);
                            } else {
                                targetCell = targetRow.getCell(cellIndex);
                            }
                            targetCell.setCellValue((Double) cellValue);

                        }
                        System.out.println(MessageFormat.format("Sheet: {0}, Row: {1}, Cell: {2}, Value: {3}", sourceSheet.getSheetName(), rowIndex, cellIndex, cellValue));
                    }
                }

            }
        } catch (Exception e) {
            System.out.println(MessageFormat.format("Sheet: {0}, Row: {1}, Cell: {2}", sourceSheet.getSheetName(), rowIndex, cellIndex));
            throw e;
        }

        wbTarget.write(new FileOutputStream(new File(IConstants.TMP_DIR + "/random.xlsx")));

    }

    public void fillData(InputStream source) throws Exception{
        Workbook wb = new XSSFWorkbook(source);
        Random rnd = new Random();
        for (Sheet sheet : wb) {
            if (!checkWithRegExp(sheet.getSheetName())) {
                continue;
            }
            for (int i = 1; i < 148; i++) {
                Row row = sheet.getRow(i);

                for (int j = 3; j < 8; j++) {
                    double val = rnd.nextDouble() * 10000000.0;
                    row.getCell(j).setCellValue(val);
                }
            }
        }

        //Clients
        Sheet clientSheet = wb.getSheet("CLIENTS");
        for (int i = 1; i < 204; i++) {
            Row row = clientSheet.getRow(i);
            for (int j = 1; j < 13; j++) {
                double val = rnd.nextDouble() * 10000000.0;
                row.getCell(j).setCellValue(val);
            }
        }

        //Currencies
        Sheet currSheet = wb.getSheet("CURRENCIES");
        for (int i = 2; i <= currSheet.getLastRowNum(); i++) {
            Row row = currSheet.getRow(i);
            for (int j = 1; j < row.getLastCellNum(); j += 2) {
                double val = rnd.nextDouble() * 10000000.0;
                row.getCell(j).setCellValue(val);
            }
        }


        File file = new File(IConstants.TMP_DIR + "/random.xlsx");
        try {
            wb.write(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public boolean checkWithRegExp(String sheetName) {
        Pattern p = Pattern.compile("\\d{4}Q\\d");
        Matcher m = p.matcher(sheetName);
        return m.matches();
    }


}
