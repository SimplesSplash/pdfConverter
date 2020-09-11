package com.company.pdfconverter.service;

public interface ConvertService {
    String NAME = "pdfconverter_ConvertService";

    public String convertPDFtoXLSX(String base64String);
}