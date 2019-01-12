package com.diamondLounge.utility;

import com.diamondLounge.entity.models.tables.HeadersPosition;
import com.diamondLounge.entity.models.tables.TableModel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static com.diamondLounge.entity.models.tables.HeadersPosition.*;
import static org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND;
import static org.apache.poi.ss.usermodel.IndexedColors.*;
import static org.springframework.http.ContentDisposition.builder;

public class ExcelConverter {

    public static Workbook convertToWorkbook(TableModel tableModel, String sheetName) {
        Workbook workbook = new XSSFWorkbook();
        CellStyle headerStyle = getHeaderCellStyle(workbook);
        CellStyle regularCellStyle = getRegularCellStyle(workbook);
        CellStyle missingValueStyle = getMissingValueCellStyle(workbook);
        Sheet sheet = workbook.createSheet(sheetName);

        fillWithData(tableModel, sheet);
        formatCells(tableModel, sheet, regularCellStyle, headerStyle, missingValueStyle);
        resizeColumns(tableModel.getWidth(), sheet);

        return workbook;
    }

    private static void formatCells(TableModel tableModel, Sheet sheet, CellStyle regularCellStyle, CellStyle headerStyle, CellStyle missingValueStyle) {
        String[][] table = tableModel.getTable();
        HeadersPosition headersPosition = tableModel.getHeadersPosition();
        int width = table.length;
        int height = table[0].length;

        for (int i = 0; i < height; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < width; j++) {
                Cell cell = row.getCell(j);
                cell.setCellStyle(regularCellStyle);

                if ((i == 0 && (headersPosition == HORIZONTAL || headersPosition == HORIZONTAL_AND_VERTICAL) ||
                     j == 0 && (headersPosition == VERTICAL || headersPosition == HORIZONTAL_AND_VERTICAL))) {
                    cell.setCellStyle(headerStyle);
                } else if (table[j][i] == null && !tableModel.ignoreMissingValues()) {
                    cell.setCellStyle(missingValueStyle);
                    cell.setCellValue("NOT ENOUGH STAFF");
                }
            }
        }
    }

    private static void resizeColumns(int columnCount, Sheet sheet) {
        for (int j = 0; j < columnCount; j++) {
            sheet.autoSizeColumn(j);
        }
    }

    private static void fillWithData(TableModel tableModel, Sheet sheet) {
        String[][] table = tableModel.getTable();
        int width = table.length;
        int height = table[0].length;

        for (int i = 0; i < height; i++) {
            Row row = sheet.createRow(i);

            for (int j = 0; j < width; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(table[j][i]);
            }
        }
    }

    private static CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();

        font.setBold(true);
        font.setColor(WHITE.getIndex());
        font.setFontHeightInPoints((short) 15);

        alignMiddle(cellStyle);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(GREY_50_PERCENT.getIndex());
        cellStyle.setFillPattern(SOLID_FOREGROUND);

        return cellStyle;
    }

    private static void alignMiddle(CellStyle cellStyle) {
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    }

    private static CellStyle getRegularCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();

        alignMiddle(cellStyle);
        cellStyle.setFillForegroundColor(GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(SOLID_FOREGROUND);

        return cellStyle;
    }

    private static CellStyle getMissingValueCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();

        font.setBold(true);
        font.setColor(WHITE.getIndex());

        alignMiddle(cellStyle);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(BROWN.getIndex());
        cellStyle.setFillPattern(SOLID_FOREGROUND);

        return cellStyle;
    }

    public static HttpHeaders getHttpExcelHeaders(String filename) {
        ContentDisposition contentDisposition = builder("inline").filename(filename + ".xlsx").build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDisposition(contentDisposition);
        return headers;
    }
}
