package com.example.pdfGenerator.service;

import com.example.pdfGenerator.domain.Item;
import com.example.pdfGenerator.domain.Request;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@Log4j2
public class PdfService {
    public ByteArrayInputStream createPdf(Request request) {
        log.info("Creating Pdf Started!!");
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        if (Objects.isNull(request))
            throw new RuntimeException("Request cannot be empty");

        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();
        Paragraph paragraph = new Paragraph();
        PdfPTable table = new PdfPTable(4);
        List<Item> items = request.getItems();

        addUserDetails(table, request);
        log.info("Added User Details...");

        addTableHeader(table);
        log.info("Added Headers");

        items.forEach(item -> {
            table.addCell(item.getName());
            table.addCell(item.getQuantity());
            table.addCell(String.valueOf(item.getRate()));
            table.addCell(String.valueOf(item.getAmount()));
        });
        
        paragraph.add(table);
        document.add(paragraph);
        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }


    /**
     * This method adds table header for items
     * @param table
     */
    private void addTableHeader(PdfPTable table) {
        Stream.of("Item", "Quantity", "Rate", "Amount")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(Color.white);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    /**
     * This method adds starting two rows in table
     * @param table
     * @param request
     */
    private void addUserDetails(PdfPTable table, Request request){
        StringBuilder sellerPhrase = new StringBuilder();
        StringBuilder buyerPhrase = new StringBuilder();

        sellerPhrase.append("Seller:\n")
                .append(request.getSeller()).append("\n")
                .append(request.getSellerAddress()).append("\n")
                .append("GSTIN: ")
                .append(request.getSellerGstIn());

        buyerPhrase.append("Buyer:\n")
                .append(request.getBuyer()).append("\n")
                .append(request.getBuyerAddress()).append("\n")
                .append("GSTIN: ")
                .append(request.getBuyerGstIn());

        PdfPCell c1 = new PdfPCell(new Phrase(sellerPhrase.toString()));
        c1.setColspan(2);
        table.addCell(c1);

        PdfPCell c2 = new PdfPCell(new Phrase(buyerPhrase.toString().intern()));
        c2.setColspan(2);
        table.addCell(c2);
    }
}
