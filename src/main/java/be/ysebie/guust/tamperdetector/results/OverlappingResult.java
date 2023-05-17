package be.ysebie.guust.tamperdetector.results;

import be.ysebie.guust.tamperdetector.AnalysisResult;
import be.ysebie.guust.tamperdetector.Indicator;
import be.ysebie.guust.tamperdetector.contexts.helper.ExtendedTextRenderInfo;
import be.ysebie.guust.tamperdetector.utils.T2;
import java.util.List;

public class OverlappingResult extends Result {

    private final List<T2<ExtendedTextRenderInfo, ExtendedTextRenderInfo>> rectangles;

    private final int pageNumber;

    public OverlappingResult(Indicator indicator,
            AnalysisResult result, List<T2<ExtendedTextRenderInfo,
            ExtendedTextRenderInfo>> rectangles,
            int pageNumber) {
        super(indicator, result);
        this.rectangles = rectangles;
        this.pageNumber = pageNumber;
    }


    @Override
    public String format() {
        return super.format() + "\n" + formatRectangles();
    }

    private String formatRectangles() {
        StringBuilder sb = new StringBuilder();
        sb.append("\tPage: ").append(pageNumber).append("\n");
        for (T2<ExtendedTextRenderInfo, ExtendedTextRenderInfo> overlapppers : rectangles) {
            sb.append("\t");
            sb.append("Text: <").append(overlapppers.t1.getText()).append("> overlaps with <")
                    .append(overlapppers.t2.getText()).append("> ").append("\n");
        }
        return sb.toString();
    }
}
