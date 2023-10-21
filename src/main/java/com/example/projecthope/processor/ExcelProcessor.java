package com.example.projecthope.processor;

import com.example.projecthope.dto.Bind;
import com.example.projecthope.dto.ExlBind;
import com.example.projecthope.entity.ProjectHope;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class ExcelProcessor implements Iterable<ProjectHope> {

    public static final List<Bind> BINDS = AnnoProcessor.process(ProjectHope.class);
    private static final Map<String, Bind> BIND_MAP = BINDS.stream().collect(Collectors.toMap(Bind::getHeaderName, Function.identity()));
    private static final Set<Bind> REQUIRED_SET = BIND_MAP.values().stream().filter(Bind::isRequired).collect(Collectors.toSet());
    private static final DataFormatter DATA_FORMATTER = new DataFormatter();
    private final List<ExlBind> headerIndexBind;
    private final Workbook workbook;
    public SXSSFWorkbook sxssfWorkbook;

    private ExcelProcessor(Workbook workbook, List<ExlBind> headerIndexBind) {
        this.workbook = workbook;
        this.headerIndexBind = headerIndexBind;
    }

    public static ExcelProcessor create(Workbook workbook) {
        List<ExlBind> headerIndexBind = processorHeader(workbook);
        Set<String> set = headerIndexBind.stream().map(ExlBind::getHeaderName).collect(Collectors.toSet());
        for (Bind required : REQUIRED_SET) {
            if (!set.contains(required.getHeaderName())) {
                throw new RuntimeException(MessageFormat.format("缺少字段[{0}]", required.getHeaderName()));
            }
        }
        return new ExcelProcessor(workbook, headerIndexBind);
    }

    public static ExcelProcessor createSXSSF(Workbook workbook, int rowAccessWindowSize) {
        ExcelProcessor excelProcessor = create(workbook);
        excelProcessor.sxssfWorkbook = new SXSSFWorkbook((XSSFWorkbook) workbook, rowAccessWindowSize);
        return excelProcessor;
    }

    private static List<ExlBind> processorHeader(Workbook workbook) {
        List<ExlBind> list = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(1);
        if (row == null) {
            throw new RuntimeException("缺少表头");
        }
        for (Cell cell : row) {
            String s = DATA_FORMATTER.formatCellValue(cell);
            Bind bind = BIND_MAP.get(s);
            if (bind != null) {
                ExlBind exlBind = new ExlBind(bind);
                exlBind.setCellIndex(cell.getColumnIndex());
                list.add(exlBind);
            }
        }
        return list;
    }

    public static void check(ProjectHope projectHope) {
        for (Bind bind : REQUIRED_SET) {
            String value = projectHope.getVariable(bind.getField());
            if (StringUtil.isBlank(value)) {
                throw new RuntimeException(MessageFormat.format("字段[{0}]不能为空", bind.getHeaderName()));
            }
        }
    }

    @Override
    public Iterator<ProjectHope> iterator() {
        return new ProjectHopeIterator(workbook.getSheetAt(0).iterator(), headerIndexBind);
    }

    public Row write(ProjectHope projectHope) {
        Workbook wb = sxssfWorkbook == null ? workbook : sxssfWorkbook;
        Sheet sheet = wb.getSheetAt(0);
        int startLineNum = sheet.getLastRowNum() == -1 ? 1 : sheet.getLastRowNum();
        Row row = sheet.createRow(startLineNum + 1);
        row.setHeight(sheet.getDefaultRowHeight());
        for (ExlBind exlBind : headerIndexBind) {
            Cell cell = row.createCell(exlBind.getCellIndex());
            cell.setCellValue(projectHope.getVariable(exlBind.getField()));
        }
        return row;
    }

    public void writeErr(ProjectHope projectHope, String errMsg) {
        Row row = write(projectHope);
        Cell cell = row.createCell(row.getLastCellNum());
        cell.setCellValue(errMsg);
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        Workbook wb = sxssfWorkbook != null ? sxssfWorkbook : workbook;
        wb.write(outputStream);
        wb.close();
        outputStream.close();
        if (sxssfWorkbook != null) {
            sxssfWorkbook.dispose();
        }
    }

    @Getter
    @Setter
    static class ProjectHopeIterator implements Iterator<ProjectHope> {
        private Iterator<Row> iterator;

        private List<ExlBind> headerIndexBind;

        public ProjectHopeIterator(Iterator<Row> iterator, List<ExlBind> headerIndexBind) {
            skipHeader(iterator);
            this.iterator = iterator;
            this.headerIndexBind = headerIndexBind;
        }

        private void skipHeader(Iterator<Row> iterator) {
            for (int i = 0; i < 2; i++) {
                if (iterator.hasNext()) {
                    iterator.next();
                }
            }
        }


        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public ProjectHope next() {
            Row row = iterator.next();
            if (row != null) {
                ProjectHope projectHope = new ProjectHope();
                for (ExlBind exlBind : headerIndexBind) {
                    try {
                        Integer cellIndex = exlBind.getCellIndex();
                        Cell cell = row.getCell(cellIndex);
                        String filedValue = null;
                        if (cell != null) {
                            filedValue = DATA_FORMATTER.formatCellValue(cell);
                        }
                        projectHope.setVariable(exlBind.getField(), filedValue);
                    } catch (Exception e) {
                        log.error("ExcelProcessor[next] occurred err", e);
                    }
                }
                return projectHope;
            }
            return null;
        }
    }
}

