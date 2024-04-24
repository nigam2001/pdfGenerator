package com.example.pdfGenerator.domain;

import lombok.Data;

import java.util.List;

@Data
public class Request {
    private String seller;
    private String sellerAddress;
    private String sellerGstIn;
    private String buyer;
    private String buyerAddress;
    private String buyerGstIn;
    private List<Item> items;
}
