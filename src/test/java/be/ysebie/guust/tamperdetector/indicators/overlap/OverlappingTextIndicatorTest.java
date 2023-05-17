package be.ysebie.guust.tamperdetector.indicators.overlap;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import be.ysebie.guust.tamperdetector.IIndicatorFactory;
import be.ysebie.guust.tamperdetector.Indicator;
import be.ysebie.guust.tamperdetector.TamperDetector;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import org.junit.Assert;
import org.junit.Test;

public class OverlappingTextIndicatorTest {

    @Test
    public void onAnalyzePageLevel() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(baos));
        Document document = new Document(pdfDocument);
        document.add(new com.itextpdf.layout.element.Paragraph("Hello World").setFixedPosition(200, 200, 100));
        document.add(new com.itextpdf.layout.element.Paragraph("Bing bong").setFixedPosition(200, 200, 100));
        document.close();

        TamperDetector tamperDetector = new TamperDetector(baos.toByteArray(), new IndicatorFactory(),
                result -> true,
                result -> {
                    System.out.println(result.format());
                });
        tamperDetector.analyze();

    }


    static class IndicatorFactory implements IIndicatorFactory {

        @Override
        public List<Supplier<Indicator>> indicators() {
            Supplier<Indicator> supplier = OverlappingTextIndicator::new;
            return Collections.singletonList(supplier);
        }
    }
}