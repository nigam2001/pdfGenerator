package com.example.pdfGenerator.controller;

import com.example.pdfGenerator.domain.Request;
import com.example.pdfGenerator.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    private PdfService pdfService;
    @PostMapping("/createPdf")
    public ResponseEntity<InputStreamResource> createPdf(@RequestBody Request request,
                                                         @RequestParam("file_type") String fileType){
        ByteArrayInputStream pdf = pdfService.createPdf(request);

        HttpHeaders httpHeaders = new HttpHeaders();

        if (fileType.equalsIgnoreCase("download"))
            httpHeaders.add("Content-Disposition",
                "attachment;file=lcwd.pdf");
        else
            httpHeaders.add("Content-Disposition",
                    "inline;file=lcwd.pdf");

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }
}
