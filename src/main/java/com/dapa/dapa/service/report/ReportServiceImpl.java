
package com.dapa.dapa.service.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dapa.dapa.entity.Transaction;
import com.dapa.dapa.entity.Users;
import com.dapa.dapa.entity.Products;
import com.dapa.dapa.entity.SumTransaction;
import com.dapa.dapa.repository.SumTransactionRepository;
import com.dapa.dapa.repository.TransactionRepository;
import com.dapa.dapa.repository.UserRepository;

@Service
public class ReportServiceImpl implements ReportService{
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SumTransactionRepository sumTransactionRepository;

    @Override
    public byte[] generateReport() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Sheet sheet = workbook.createSheet();

        CellStyle titleCellStyle = createCellStyle(workbook, true, false, false);
        CellStyle headerCellStyle = createCellStyle(workbook, false, true, false);
        CellStyle tableCellStyle = createCellStyle(workbook, false, false, true);

        generateTitle(sheet, titleCellStyle);
        generateHeader(sheet, headerCellStyle);
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Users users = userRepository.findUsersByUsername(username);
        List<Transaction> transactions = transactionRepository.findTransactionByUsers(users);
        if (transactions == null) {
            generateEmptyData(sheet, tableCellStyle);
        } else {
            int dex = transactions.size();
            int currentRowIndex = 5;
            int rowNum = 1;
            Transaction transaction = transactions.get(dex-1);
            for (Products products : transactions.get(dex - 1).getProducts()) {
                SumTransaction quantity = sumTransactionRepository.findByTransactionAndProducts(transaction,products);
                Row row = sheet.createRow(currentRowIndex);
                generateTableData(row, products, tableCellStyle, rowNum,transaction,quantity);
                currentRowIndex++;
                rowNum++;
            }
        }

        for (int i = 0; i < 8; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(byteArrayOutputStream);
        workbook.close();

        return byteArrayOutputStream.toByteArray();
    }

    private CellStyle createCellStyle(Workbook workbook,
                                      boolean isHeader, boolean isTitle, boolean isTable) {
        CellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");

        if (isHeader) {
            font.setFontHeightInPoints((short) 14);
            font.setBold(true);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        }

        if (isTitle) {
            font.setFontHeightInPoints((short) 12);
            font.setBold(true);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
        }

        if (isTable) {
            font.setFontHeightInPoints((short) 12);
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
        }

        cellStyle.setFont(font);
        return cellStyle;
    }

    private void generateTitle(Sheet sheet, CellStyle cellStyle) {
        // Baris Pertama
        Row firstTitleRow = sheet.createRow(0);
        Cell firstTitleCell = firstTitleRow.createCell(0);
        firstTitleCell.setCellValue("Laporan Penjualan");
        firstTitleCell.setCellStyle(cellStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

        // Baris Kedua
        Row secondTitleRow = sheet.createRow(1);
        Cell secondTitleCell = secondTitleRow.createCell(0);
        secondTitleCell.setCellValue("Dapa Electronic Store");
        secondTitleCell.setCellStyle(cellStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));

        // Baris Ketiga
        Row addressRow = sheet.createRow(2);
        Cell addressCell = addressRow.createCell(0);
        addressCell.setCellValue("Jalan Megaraya I No. 20, Sukaraja, Cicendo, Bandung");
        addressCell.setCellStyle(cellStyle);
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 7));
    }

    private void generateHeader(Sheet sheet, CellStyle cellStyle) {
        Row headerRow = sheet.createRow(4);
        int currentCellIndex = 0;
        headerRow.createCell(currentCellIndex).setCellValue("No");
        headerRow.createCell(++currentCellIndex).setCellValue("Tanggal Transaksi");
        headerRow.createCell(++currentCellIndex).setCellValue("Nama Produk");
        headerRow.createCell(++currentCellIndex).setCellValue("Merk");
        headerRow.createCell(++currentCellIndex).setCellValue("Type");
        headerRow.createCell(++currentCellIndex).setCellValue("Harga");
        headerRow.createCell(++currentCellIndex).setCellValue("Kuantitas");
        headerRow.createCell(++currentCellIndex).setCellValue("Total");

        for (int i=0; i <= currentCellIndex; i++) {
            headerRow.getCell(i).setCellStyle(cellStyle);
        }
    }

    private void generateEmptyData(Sheet sheet, CellStyle cellStyle) {
        Row emptyRow = sheet.createRow(5);
        emptyRow.createCell(0).setCellValue("No Data");
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 7));
        emptyRow.getCell(0).setCellStyle(cellStyle);
    }

    private void generateTableData(Row row, Products products, CellStyle cellStyle,
                               int rowNum,Transaction transactions,SumTransaction quantity) {
    int currentCellIndex = 0;

    Cell numberRow = row.createCell(currentCellIndex);
    numberRow.setCellValue(rowNum);
    numberRow.setCellStyle(cellStyle);

    Cell transactionDateRow = row.createCell(++currentCellIndex);
    transactionDateRow.setCellValue(transactions.getTransactionDate().format(DateTimeFormatter.ISO_DATE));
    transactionDateRow.setCellStyle(cellStyle);

    // Products product = products.getProducts().stream().findFirst().orElse(null);

    Cell productNameRow = row.createCell(++currentCellIndex);
    productNameRow.setCellValue(products != null ? products.getProductName() : "-");
    productNameRow.setCellStyle(cellStyle);

    Cell productMerkRow = row.createCell(++currentCellIndex);
    productMerkRow.setCellValue(products != null ? products.getProductMerk() : "-");
    productMerkRow.setCellStyle(cellStyle);

    Cell productTypeRow = row.createCell(++currentCellIndex);
    productTypeRow.setCellValue(products != null && products.getProductType() != null ? products.getProductType() : "-");
    productTypeRow.setCellStyle(cellStyle);

    Cell priceRow = row.createCell(++currentCellIndex);
    priceRow.setCellValue(products != null ? products.getProductPrice() : 0);
    priceRow.setCellStyle(cellStyle);

    Cell quantityRow = row.createCell(++currentCellIndex);
    quantityRow.setCellValue(quantity.getQuantity());
    quantityRow.setCellStyle(cellStyle);

    Cell totalRow = row.createCell(++currentCellIndex);
    totalRow.setCellValue(quantity.getSum());
    totalRow.setCellStyle(cellStyle);
}

}



