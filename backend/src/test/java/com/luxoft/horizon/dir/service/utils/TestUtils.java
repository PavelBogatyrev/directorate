package com.luxoft.horizon.dir.service.utils;

import com.luxoft.horizon.dir.service.domain.impl.DirectorateServiceImplTest;
import com.luxoft.horizon.dir.utils.Utils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author rlapin
 */
public class TestUtils {


    public static final String LAST_EXECUTIVE = "lastExecutive";
    public static final String N_A = "n_a";
    public static final String ACTUAL = "actual";
    public static final String FACT = "fact";
    public static final String FACTFORECAST = "factforecast";
    public static final String PLAN = "plan";

    public static void generateRandomXlsData() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        XSSFCell c;
        CellStyle cellStyle = workbook.createCellStyle();
        CreationHelper creationHelper = workbook.getCreationHelper();
        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd.mm.yy"));
        for(int i=1;i<100;i++) {
            XSSFRow row = sheet.createRow(i);
            int cell = 0;
            c =row.createCell(cell++);
            Random random = new Random();
            LocalDate date = LocalDate.of(2016, random.nextInt(12)+1, random.nextInt(12)+1);

            c.setCellStyle(cellStyle);
            c.setCellValue(Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            c =row.createCell(cell++);
            c.setCellStyle(cellStyle);
            date = LocalDate.of(2016, random.nextInt(12)+1,1);
            c.setCellValue(Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            c =row.createCell(cell++);
            c.setCellValue(random.nextBoolean()? LAST_EXECUTIVE : N_A);
            c =row.createCell(cell++);
            c.setCellValue(random.nextBoolean()? ACTUAL : N_A);
            c =row.createCell(cell++);
            int value = random.nextInt(3);
            c.setCellValue(value==0? PLAN :value==1? FACT : FACTFORECAST);
            c =row.createCell(cell++);
            c.setCellValue(random.nextInt(10000000));
            c =row.createCell(cell++);
            c.setCellValue(random.nextInt(10000000));
            c =row.createCell(cell++);
            c.setCellValue(random.nextInt(10000000));
            c =row.createCell(cell++);
            c.setCellValue(random.nextInt(10000000));
            c =row.createCell(cell++);
            c.setCellValue(random.nextInt(10000000));
            c =row.createCell(cell++);
            c.setCellValue(random.nextInt(10000000));
            c =row.createCell(cell++);
            c.setCellValue(random.nextInt(10000000));
            c =row.createCell(cell++);
            c.setCellValue(random.nextInt(10000000));
            c =row.createCell(cell++);
            c.setCellValue(random.nextInt(10000000));
            c =row.createCell(cell++);
            c.setCellValue(random.nextInt(10000000));
            c =row.createCell(cell++);
            c.setCellValue(random.nextInt(10000000));
            c =row.createCell(cell);
            c.setCellValue(random.nextInt(10000000));

        }
        workbook.write(new FileOutputStream(new File(DirectorateServiceImplTest.TEST_DATA_FILENAME)));

    }

    @Test
    public void testGetDefaultValue() throws Exception {

        int i = Utils.defaultIfNull(null,12,12,10);
        assertEquals(i,10);

        List<Double[]> a = new ArrayList<>();
        Double arr[] = new Double[1];
        arr[0] = 123.0d;
        a.add(arr);
        double d = Utils.defaultIfNull(a, 0, 0, 1.0d);
        assertEquals(d,123,1E-4);
        d = Utils.defaultIfNull(a,0,1,12.0d);
        assertEquals(d, 12, 1E-4);
    }
}
