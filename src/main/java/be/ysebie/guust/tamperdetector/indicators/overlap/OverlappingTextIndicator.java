package be.ysebie.guust.tamperdetector.indicators.overlap;

import com.itextpdf.kernel.geom.Rectangle;

import be.ysebie.guust.tamperdetector.AnalysisResult;
import be.ysebie.guust.tamperdetector.Indicator;
import be.ysebie.guust.tamperdetector.contexts.PageLevelContext;
import be.ysebie.guust.tamperdetector.contexts.helper.ExtendedTextRenderInfo;
import be.ysebie.guust.tamperdetector.results.OverlappingResult;
import be.ysebie.guust.tamperdetector.results.Result;
import be.ysebie.guust.tamperdetector.utils.T2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OverlappingTextIndicator extends Indicator {


    @Override
    public Result onAnalyzePageLevel(PageLevelContext context) {
        List<T2<ExtendedTextRenderInfo, ExtendedTextRenderInfo>> rectangles = new ArrayList<>();
        Set<Rectangle> uniqueRectangles =new HashSet<>();
        for (ExtendedTextRenderInfo rect1 : context.getTextRenderInfos()) {
            for (ExtendedTextRenderInfo rect2 : context.getTextRenderInfos()) {
                if (rect1 == rect2) {
                    continue;
                }
                if (uniqueRectangles.contains(rect1.getBoundingBox()) || uniqueRectangles.contains(rect2.getBoundingBox())) {
                    continue;
                }
                Rectangle r1 = rect1.getBoundingBox();
                Rectangle r2 = rect2.getBoundingBox();
                if (r1.overlaps(r2)) {
                    uniqueRectangles.add(r1);
                    uniqueRectangles.add(r2);
                    rectangles.add(new T2<>(rect1, rect2));
                }

            }
        }
        if (rectangles.size() > 0) {
            return new OverlappingResult(this, AnalysisResult.TAMPERING_DETECTED, rectangles, context.getPageNumber());
        }
        return new Result(this, AnalysisResult.NO_TAMPERING);
    }
}

