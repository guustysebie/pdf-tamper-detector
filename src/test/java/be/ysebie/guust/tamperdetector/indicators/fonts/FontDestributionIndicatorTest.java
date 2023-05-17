package be.ysebie.guust.tamperdetector.indicators.fonts;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.AreaBreakType;

import be.ysebie.guust.tamperdetector.TamperDetector;
import be.ysebie.guust.tamperdetector.indicators.util.SimpleInstanceFactory;
import java.io.IOException;
import org.junit.Test;

public class FontDestributionIndicatorTest {


    @Test
    public void Test001() throws IOException {
        FontDestributionIndicator indicator = new FontDestributionIndicator(5);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(baos));
        Document document = new Document(pdfDocument);
        for (int i = 0; i < 200; i++) {
            document.add(new Paragraph("Page " + i));
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        }
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.COURIER_BOLD);
        document.add(new Paragraph("Page").setFont(bold));
        document.close();


        TamperDetector tamperDetector = new TamperDetector(baos.toByteArray(),new SimpleInstanceFactory(indicator),
                result -> true,
                result -> {
                    System.out.println(result.format());
                });
        tamperDetector.analyze();
    }

}