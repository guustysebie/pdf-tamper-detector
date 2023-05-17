package be.ysebie.guust.tamperdetector.results;

import be.ysebie.guust.tamperdetector.AnalysisResult;
import be.ysebie.guust.tamperdetector.Indicator;

public  class Result {

    private final Indicator indicator;

    private AnalysisResult analysisResult = AnalysisResult.NO_ANALYSIS_DONE;

    public Result(Indicator indicator, AnalysisResult analysisResult) {
        this.indicator = indicator;
        this.analysisResult = analysisResult;
    }

    public Indicator getIndicator() {
        return indicator;
    }

    public AnalysisResult getAnalysisResult() {
        return analysisResult;
    }

    public boolean isTamperingDetected() {
        return analysisResult == AnalysisResult.TAMPERING_DETECTED;
    }

    public String format() {
        return String.format("%-20s for <%s>", this.analysisResult.toString(), indicator.getClass().getSimpleName());
    }


}


