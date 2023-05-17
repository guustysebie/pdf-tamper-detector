package be.ysebie.guust.tamperdetector.indicators.line;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import be.ysebie.guust.tamperdetector.TamperDetector;
import be.ysebie.guust.tamperdetector.indicators.util.SimpleInstanceFactory;
import java.io.IOException;
import org.junit.Test;

public class DifferentFontsInASingleLineIndicatorTest {


    @Test
    public void test001() throws IOException {
        DifferentFontsInASingleLineIndicator indicator = new DifferentFontsInASingleLineIndicator();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(baos));
        Document document = new Document(pdfDocument);

        document.add(new Paragraph("Text one"));
        document.add(new Paragraph("Text two"));

        Paragraph paragraph = new Paragraph();
        paragraph.add(new Text("Text three").setFont(PdfFontFactory.createFont(StandardFonts.COURIER_BOLD)));
        paragraph.add(new Text("Text four"));
        document.add(paragraph);
        document.close();

        TamperDetector tamperDetector = new TamperDetector(baos.toByteArray(), new SimpleInstanceFactory(indicator),
                result -> true,
                result -> {
                    System.out.println(result.format());
                });
        tamperDetector.analyze();

    }
}