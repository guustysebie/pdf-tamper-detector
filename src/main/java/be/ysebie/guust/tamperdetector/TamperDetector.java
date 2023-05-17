package be.ysebie.guust.tamperdetector;

import com.itextpdf.kernel.pdf.PdfDocument;

import be.ysebie.guust.tamperdetector.contexts.ByteLevelContext;
import be.ysebie.guust.tamperdetector.contexts.DocumentLevelContext;
import be.ysebie.guust.tamperdetector.contexts.PageLevelContext;
import be.ysebie.guust.tamperdetector.results.Result;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TamperDetector {

    private final byte[] pdfDocument;
    private final IIndicatorFactory indicatorFactory;
    private final IContinuationStrategy continuationStrategy;
    private final IResultAggregator resultAggregator;

    public TamperDetector(byte[] pdfDocument, IIndicatorFactory indicatorFactory,
            IContinuationStrategy continuationStrategy, IResultAggregator resultAggregator) {
        this.pdfDocument = pdfDocument;
        this.indicatorFactory = indicatorFactory;
        this.continuationStrategy = continuationStrategy;
        this.resultAggregator = resultAggregator;
    }


    public void analyze() throws IOException {
        List<Supplier<Indicator>> f = indicatorFactory.indicators();
        List<Indicator> indicators = new ArrayList<>();
        for (Supplier<Indicator> supplier : f) {
            indicators.add(supplier.get());
        }
        ByteLevelContext byteLevelContext = new ByteLevelContext(pdfDocument);
        for (Indicator indicator : indicators) {
            Result result = indicator.onAnalyzeByteLevel(byteLevelContext);
            resultAggregator.onNewResult(result);
            if (!continuationStrategy.shouldContinue(result)) {
                return;
            }
        }

        DocumentLevelContext documentLevelContext = new DocumentLevelContext(byteLevelContext);
        for (Indicator indicator : indicators) {
            Result result = indicator.onAnalyzeDocumentLevel(documentLevelContext);
            resultAggregator.onNewResult(result);
            if (!continuationStrategy.shouldContinue(result)) {
                return;
            }
        }

        PdfDocument itextPdfDocument = documentLevelContext.getPdfDocument();
        int numberOfPages = itextPdfDocument.getNumberOfPages();
        for (int i = 1; i <= numberOfPages; i++) {
            PageLevelContext pageLevelContext = new PageLevelContext(documentLevelContext,
                    itextPdfDocument.getPage(i), i);
            for (Indicator indicator : indicators) {
                Result result = indicator.onAnalyzePageLevel(pageLevelContext);
                resultAggregator.onNewResult(result);
                if (!continuationStrategy.shouldContinue(result)) {
                    return;
                }
            }
        }
        for (Indicator indicator : indicators) {
            Result result = indicator.onAnalyzePostProcessing(documentLevelContext);
            resultAggregator.onNewResult(result);
            if (!continuationStrategy.shouldContinue(result)) {
                return;
            }
        }
    }

}
