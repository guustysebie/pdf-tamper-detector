package be.ysebie.guust.tamperdetector.indicators.line;

import be.ysebie.guust.tamperdetector.AnalysisResult;
import be.ysebie.guust.tamperdetector.Indicator;
import be.ysebie.guust.tamperdetector.contexts.PageLevelContext;
import be.ysebie.guust.tamperdetector.contexts.helper.ExtendedTextRenderInfo;
import be.ysebie.guust.tamperdetector.results.ListOfResult;
import be.ysebie.guust.tamperdetector.results.MessageResult;
import be.ysebie.guust.tamperdetector.results.Result;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DifferentFontsInASingleLineIndicator extends Indicator {


    @Override
    public Result onAnalyzePageLevel(PageLevelContext context) {
        List<Result> results = new ArrayList<>();
        for (List<ExtendedTextRenderInfo> textLine : context.getTextLines()) {
            Set<String> encounteredFonts = new HashSet<>();
            for (ExtendedTextRenderInfo extendedTextRenderInfo : textLine) {
                encounteredFonts.add(extendedTextRenderInfo.getFontName());
            }
            if (encounteredFonts.size() > 1) {
                Result result = new MessageResult(this, AnalysisResult.TAMPERING_DETECTED,
                        "Encountered different fonts in a single line: " + textLine.get(0).getText());
                results.add(result);
            }
        }

        if (results.size() > 0) {
            return new ListOfResult(this, AnalysisResult.TAMPERING_DETECTED).addAll(results);
        } else {
            return new Result(this, AnalysisResult.NO_TAMPERING);
        }

    }
}
