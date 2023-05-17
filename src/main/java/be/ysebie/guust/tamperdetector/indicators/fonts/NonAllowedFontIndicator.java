package be.ysebie.guust.tamperdetector.indicators.fonts;

import be.ysebie.guust.tamperdetector.AnalysisResult;
import be.ysebie.guust.tamperdetector.Indicator;
import be.ysebie.guust.tamperdetector.contexts.DocumentLevelContext;
import be.ysebie.guust.tamperdetector.contexts.PageLevelContext;
import be.ysebie.guust.tamperdetector.contexts.helper.ExtendedTextRenderInfo;
import be.ysebie.guust.tamperdetector.results.MessageResult;
import be.ysebie.guust.tamperdetector.results.Result;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NonAllowedFontIndicator extends Indicator {

    private final List<String> acceptedFonts;

    private final Set<String> encounteredFonts;

    public NonAllowedFontIndicator(List<String> acceptedFonts) {
        this.acceptedFonts = acceptedFonts;
        encounteredFonts = new HashSet<>();
    }

    @Override
    public Result onAnalyzePageLevel(PageLevelContext context) {
        for (ExtendedTextRenderInfo textRenderInfo : context.getTextRenderInfos()) {
            encounteredFonts.add(textRenderInfo.getFontName());
        }
        return super.onAnalyzePageLevel(context);
    }


    @Override
    public Result onAnalyzePostProcessing(DocumentLevelContext context) {
        List<String> encounteredNotAllowedFonts = new ArrayList<>();
        for (String encounteredFont : encounteredFonts) {
            if (!acceptedFonts.contains(encounteredFont)) {
                encounteredNotAllowedFonts.add(encounteredFont);
            }
        }
        if (encounteredNotAllowedFonts.size() > 0) {
            return new MessageResult(this, AnalysisResult.TAMPERING_DETECTED,
                    "Encountered not allowed fonts: " + Arrays.toString(encounteredNotAllowedFonts.toArray()));
        }
        return super.onAnalyzePostProcessing(context);
    }
}
