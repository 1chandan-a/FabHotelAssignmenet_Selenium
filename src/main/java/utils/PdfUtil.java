package utils;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.Pdf;
import org.openqa.selenium.PrintsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.print.PrintOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class PdfUtil {

    public static String generatePagePdf(WebDriver driver, String outputFileName) throws IOException {
        String outputDir = System.getProperty("user.dir") + "/test-output/pdf/";
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String targetPath = outputDir + outputFileName;
        PrintsPage printer = (PrintsPage) driver;
        PrintOptions printOptions = new PrintOptions();

        Pdf pdf = printer.print(printOptions);
        byte[] pdfBytes = Base64.getDecoder().decode(pdf.getContent());

        try (FileOutputStream fos = new FileOutputStream(targetPath)) {
            fos.write(pdfBytes);
        }

        return targetPath;
    }

    public static String extractPdfText(String pdfFilePath) throws IOException {
        File file = new File(pdfFilePath);
        try (PDDocument document = Loader.loadPDF(file)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }
}