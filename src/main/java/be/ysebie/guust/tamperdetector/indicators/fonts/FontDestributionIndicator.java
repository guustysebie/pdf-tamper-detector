package be.ysebie.guust.tamperdetector.indicators.fonts;

import be.ysebie.guust.tamperdetector.AnalysisResult;
import be.ysebie.guust.tamperdetector.Indicator;
import be.ysebie.guust.tamperdetector.contexts.DocumentLevelContext;
import be.ysebie.guust.tamperdetector.contexts.PageLevelContext;
import be.ysebie.guust.tamperdetector.contexts.helper.ExtendedTextRenderInfo;
import be.ysebie.guust.tamperdetector.results.ListOfResult;
import be.ysebie.guust.tamperdetector.results.MessageResult;
import be.ysebie.guust.tamperdetector.results.Result;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FontDestributionIndicator extends Indicator {

    private final float warningThreshold;
    private final HashMap<String, Double> fontDestribution = new HashMap<>();

    public FontDestributionIndicator(float fontAppearsLessThenThisPercentage) {
        this.warningThreshold = fontAppearsLessThenThisPercentage;
    }

    @Override
    public Result onAnalyzePageLevel(PageLevelContext context) {
        for (ExtendedTextRenderInfo textRenderInfo : context.getTextRenderInfos()) {
            if (fontDestribution.containsKey(textRenderInfo.getFontName())) {
                fontDestribution.put(textRenderInfo.getFontName(),
                        fontDestribution.get(textRenderInfo.getFontName()) + textRenderInfo.getText().length());
            } else {
                fontDestribution.put(textRenderInfo.getFontName(), (double) textRenderInfo.getText().length());
            }
        }
        return super.onAnalyzePageLevel(context);
    }

    @Override
    public Result onAnalyzePostProcessing(DocumentLevelContext context) {
        double total = 0;
        for (double value : fontDestribution.values()) {
            total  += (value);
        }

        List<Result> messages = new ArrayList<>();

        for (String key : fontDestribution.keySet()) {
            double value = fontDestribution.get(key);
            double percentage = (value / total) * 100;
            if (percentage < this.warningThreshold) {
                messages.add(
                        new MessageResult(this, AnalysisResult.TAMPERING_DETECTED,
                                "Font: " + key + " appears only " + percentage + "% of the time. Min is: " + this.warningThreshold + "%."));
            }
        }
        if (messages.size() > 0) {
            return new ListOfResult(this, AnalysisResult.TAMPERING_DETECTED).addAll(messages);
        }
        return new Result(this, AnalysisResult.NO_TAMPERING);
    }
}

