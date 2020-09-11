package com.company.pdfconverter.service;

import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.graphics.PdfTemplate;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

@Service(ConvertService.NAME)
public class ConvertServiceBean implements ConvertService {

    @Override
    public String convertPDFtoXLSX(String base64String) {
        PdfDocument pdf = new PdfDocument();
        pdf.loadFromBytes(Base64.getDecoder().decode(base64String));
        PdfPageBase firstPage = pdf.getPages().get(0);
        double totalHeight = firstPage.getCanvas().getSize().getHeight();

        for (int pageNumber = 1; pageNumber < pdf.getPages().getCount(); pageNumber++) {
            PdfPageBase page = pdf.getPages().get(pageNumber);
            double currentPageHeight = page.getCanvas().getSize().getHeight();
            PdfTemplate template = page.createTemplate();
            float parseFloat = Float.parseFloat(Double.toString(totalHeight));
            firstPage.getCanvas().drawTemplate(template, new Point2D.Float(0,parseFloat));
            totalHeight+=currentPageHeight;
        }
        while (pdf.getPages().getCount() != 1){
            pdf.getPages().removeAt(1);
        }
        pdf.saveToFile("converted.xlsx", FileFormat.XLSX);
        File file = new File("converted.xlsx");
        try {
            String base64result = encodeFileToBase64String(file);
            file.delete();
            return base64result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64String;
    }

    private String encodeFileToBase64String(File file) throws IOException {
        return Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(file));
    }
}