package be.ysebie.guust.tamperdetector.indicators.documentdates;

import be.ysebie.guust.tamperdetector.AnalysisResult;
import be.ysebie.guust.tamperdetector.results.MessageResult;
import be.ysebie.guust.tamperdetector.results.Result;

public class CreateOrUpdateDateInTheFutureIndicator extends CreationAndModificationDateIndicator{
    @Override
    public Result analyze() {
        return new MessageResult(this, AnalysisResult.NO_ANALYSIS_DONE,
                "TODO");
    }
}
