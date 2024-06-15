package com.dapa.dapa.utils;

import com.dapa.dapa.entity.Products;
import com.dapa.dapa.entity.Quantity;
import com.dapa.dapa.entity.SumTransaction;
import com.dapa.dapa.entity.Transaction;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class PdfUtil {
    public static byte[] createInvoice(Transaction transaction,List<SumTransaction> sumTransactions) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();

        Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
        Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

        Paragraph title = new Paragraph("Invoice Transaksi", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE));
        Paragraph title2 = new Paragraph("Dapa Electronic Store", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE));
        title.setAlignment(Element.ALIGN_CENTER);
        title2.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(title2);

        document.add(new Paragraph("\n"));

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("Detail Transaksi", fontBold));
        cell.setColspan(2);  
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        table.addCell(new Phrase("ID Transaksi", fontBold));
        table.addCell(new Phrase(transaction.getId(), fontNormal));
        for(Products products : transaction.getProducts()){
                table.addCell(new Phrase("Tanggal Transaksi", fontBold));
                table.addCell(new Phrase(String.valueOf(transaction.getTransactionDate()), fontNormal));
                
                table.addCell(new Phrase("Nama Produk", fontBold));  // Tambah kolom Nama Produk
                table.addCell(new Phrase(products.getProductName(), fontNormal));  // Ambil nama produk dari list produk
                
                table.addCell(new Phrase("Tipe Produk", fontBold));  // Tambah kolom Tipe Produk
                table.addCell(new Phrase(products.getProductType(), fontNormal));  // Ambil tipe produk dari list produk
                
                table.addCell(new Phrase("Harga", fontBold));  // Tambah kolom Harga
                table.addCell(new Phrase(String.valueOf(products.getProductPrice()), fontNormal));  // Ambil harga produk dari list produk
                int quant = 0;
                for(SumTransaction quantity : sumTransactions){
                    if(quantity.getProducts().equals(products)){
                        quant = quantity.getQuantity();
                    }
                }
                table.addCell(new Phrase("Kuantitas", fontBold));  // Tambah kolom Jumlah
                table.addCell(new Phrase(String.valueOf(quant), fontNormal));  // Ambil jumlah dari transaksi
                
                long total = products.getProductPrice() * quant;
                table.addCell(new Phrase("Total Harga", fontBold));
                table.addCell(new Phrase(String.valueOf(total), fontNormal));
            }
        document.add(table);
        document.close();

        return baos.toByteArray();
    }
}

