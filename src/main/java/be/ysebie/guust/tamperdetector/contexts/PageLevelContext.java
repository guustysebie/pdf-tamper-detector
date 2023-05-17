package be.ysebie.guust.tamperdetector.contexts;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;

import be.ysebie.guust.tamperdetector.contexts.helper.CustomRenderTextListener;
import be.ysebie.guust.tamperdetector.contexts.helper.ExtendedTextRenderInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PageLevelContext {

    private final DocumentLevelContext documentLevelContext;
    private final PdfPage pdfPage;
    private final int pageNumber;
    private List<ExtendedTextRenderInfo> textRenderInfos;
    private List<List<ExtendedTextRenderInfo>> textLines;


    public List<List<ExtendedTextRenderInfo>> getTextLines() {
        return textLines;
    }

    public PageLevelContext(DocumentLevelContext documentLevelContext, PdfPage pdfPage, int pageNumber) {
        this.documentLevelContext = documentLevelContext;
        this.pdfPage = pdfPage;
        this.pageNumber = pageNumber;
        doCustomCanvasProcessing();
        extractTextLines();
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public List<ExtendedTextRenderInfo> getTextRenderInfos() {
        return textRenderInfos;
    }

    private void extractTextLines() {
        HashMap<Float, Set<ExtendedTextRenderInfo>> textRenderInfoMap = new HashMap<>();
        for (ExtendedTextRenderInfo textRenderInfo : this.textRenderInfos) {
            Rectangle rectangle = textRenderInfo.getBoundingBox();
            float y = rectangle.getY();
            boolean found = false;
            for (Float aFloat : new ArrayList<>(textRenderInfoMap.keySet())) {
                // do some math to compare with epsilon
                float bottom = aFloat - 1;
                float top = aFloat + 1;
                boolean isSame = y > bottom && y < top;
                if (isSame) {
                    textRenderInfoMap.get(aFloat).add(textRenderInfo);
                    found = true;
                    break;
                }
            }
            if(found) {
                continue;
            }
            HashSet<ExtendedTextRenderInfo> extendedTextRenderInfos = new HashSet<>();
            extendedTextRenderInfos.add(textRenderInfo);
            textRenderInfoMap.put(y, extendedTextRenderInfos);
        }

        textLines = new ArrayList<>();
        textRenderInfoMap.keySet().stream().sorted().forEach((aFloat) -> {
            textLines.add(new ArrayList<>(textRenderInfoMap.get(aFloat)));
        });
    }

    private void doCustomCanvasProcessing() {
        CustomRenderTextListener customRenderTextListener = new CustomRenderTextListener();
        PdfCanvasProcessor processor = new PdfCanvasProcessor(customRenderTextListener);
        processor.processPageContent(pdfPage);
        textRenderInfos = customRenderTextListener.getTextRenderInfos();
    }
}
