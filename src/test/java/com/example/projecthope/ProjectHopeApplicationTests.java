package com.example.projecthope;

import com.example.projecthope.dto.Bind;
import com.example.projecthope.entity.ProjectHope;
import com.example.projecthope.processor.AnnoProcessor;
import com.example.projecthope.processor.ExcelProcessor;
import com.example.projecthope.service.HopeService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// @SpringBootTest
class ProjectHopeApplicationTests {

    @Test
    void test() throws IOException {
        System.out.println(123);
        try (Workbook wb = WorkbookFactory.create(true)) {
            Sheet sheet = wb.createSheet("new sheet");
            Row row = sheet.createRow(2);
            row.createCell(0).setCellValue(1.1);
            row.createCell(1).setCellValue(new Date());
            row.createCell(2).setCellValue(Calendar.getInstance());
            row.createCell(3).setCellValue("a string");
            row.createCell(4).setCellValue(true);
            row.createCell(5).setCellType(CellType.ERROR);
            // row.createCell(5).setCellFormula();
            try (OutputStream fileOut = Files.newOutputStream(Paths.get("C:\\Users\\admin\\Desktop\\ss\\workbook.xlsx"))) {
                wb.write(fileOut);
            }
        }
    }

    @Test
    void test2() throws IOException {
        System.out.println(123);
        try (Workbook wb = WorkbookFactory.create(Files.newInputStream(Paths.get("C:\\Users\\admin\\Desktop\\ss\\err_standard.xlsx")))) {
            Sheet sheet = wb.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            Row row0 = sheet.getRow(0);
            short lastCellNum = row0.getLastCellNum();
            System.out.println("lastRowNum: " + lastRowNum + " lastCellNum:" + lastCellNum);
            Cell cell0 = row0.getCell(0);
            Cell cell1 = row0.getCell(1);
            Cell cell5 = row0.getCell(5);
            System.out.println(cell0.getCellType());
            System.out.println(cell1.getCellType());
            System.out.println(cell5.getCellType());

            CellStyle cellStyle = cell0.getCellStyle();
            System.out.println(cellStyle.getAlignment());
            System.out.println(cellStyle.getVerticalAlignment());

            Row row1 = sheet.createRow(26);
            Cell cell = row1.createCell(0);
            System.out.println(cell.getCellStyle().getAlignment());


            System.out.println("-------------------------");

            System.out.println(sheet.getDefaultRowHeight());
        }
    }

    @Test
    void test3() throws IOException {
        try (Workbook wb = WorkbookFactory.create(true)) {
            Sheet sheet = wb.createSheet();
            System.out.println(sheet.getDefaultRowHeight());
            sheet.setDefaultRowHeight((short) 500);
            try (OutputStream fileOut = Files.newOutputStream(Paths.get("C:\\Users\\admin\\Desktop\\ss\\workbook2.xlsx"))) {
                wb.write(fileOut);
            }

        }
    }


    @Test
    void test4() throws IOException {
        List<ProjectHope> list = projectHopes();
        try (Workbook wb = WorkbookFactory.create(HopeService.class.getResourceAsStream("/excel/err_standard.xlsx"))) {
            ExcelProcessor excelProcessor = ExcelProcessor.create(wb);
            for (ProjectHope projectHope : list) {
                excelProcessor.write(projectHope);
            }
            wb.write(Files.newOutputStream(Paths.get("C:\\Users\\Art\\Desktop\\sss\\test.xlsx")));
        }
    }

    @Test
    void test5() throws IOException {
        List<ProjectHope> list = projectHopes();
        try (Workbook wb = WorkbookFactory.create(HopeService.class.getResourceAsStream("/excel/err_standard.xlsx"))) {
            ExcelProcessor excelProcessor = ExcelProcessor.create(wb);
            for (ProjectHope projectHope : list) {
                excelProcessor.writeErr(projectHope,"这是错误原因这是错误原因这是错误原因这是错误原因这是错误原因");
            }
            excelProcessor.writeTo(Files.newOutputStream(Paths.get("C:\\Users\\Art\\Desktop\\sss\\test1.xlsx")));
        }
    }

    @Test
    void test7() throws IOException {
        List<ProjectHope> list = projectHopes();
        try (XSSFWorkbook wb = (XSSFWorkbook)WorkbookFactory.create(HopeService.class.getResourceAsStream("/excel/err_standard.xlsx"))) {
            ExcelProcessor sxssf = ExcelProcessor.createSXSSF(wb, 10);
            for (ProjectHope projectHope : list) {
                sxssf.writeErr(projectHope,"是错误原因这");
            }
            sxssf.writeTo(Files.newOutputStream(Paths.get("C:\\Users\\Art\\Desktop\\sss\\test3.xlsx"), StandardOpenOption.CREATE));
        }
    }

    @Test
    void test8() throws IOException {
        List<ProjectHope> list = projectHopes();
        try (Workbook wb = WorkbookFactory.create(false)) {
            Sheet sheet = wb.createSheet();
            System.out.println(sheet.getLastRowNum());
            Row row0 = sheet.createRow(0);
            System.out.println("----------");
            System.out.println(row0.getLastCellNum());
            Cell cell0 = row0.createCell(0);
            System.out.println(row0.getLastCellNum());
            System.out.println("----------");
            cell0.setCellValue("0");
            Cell cell1 = row0.createCell(1);
            cell1.setCellValue("0");
            Cell cell2 = row0.createCell(2);
            cell2.setCellValue("0");
            System.out.println(sheet.getLastRowNum());
            System.out.println(row0.getLastCellNum());
            Row row1 = sheet.createRow(1);
            System.out.println(sheet.getLastRowNum());
            System.out.println(row1.getLastCellNum());
        }
    }

    List<ProjectHope> projectHopes() {
        List<ProjectHope> list = new ArrayList<>();
        List<Bind> process = AnnoProcessor.process(ProjectHope.class);
        for (int i = 0; i < 50; i++) {
            ProjectHope projectHope = new ProjectHope();
            for (Bind bind : process) {
                projectHope.setVariable(bind.getField(), "[" + bind.getHeaderName() + "]" + i);
            }
            list.add(projectHope);
        }
        return list;
    }
}
