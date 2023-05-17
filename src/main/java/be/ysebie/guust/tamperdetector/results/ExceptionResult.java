package be.ysebie.guust.tamperdetector.results;

import be.ysebie.guust.tamperdetector.AnalysisResult;
import be.ysebie.guust.tamperdetector.Indicator;

public class ExceptionResult extends MessageResult {
    private final Exception exception;

    public ExceptionResult(Indicator indicator,
            AnalysisResult analysisResult, String message, Exception exception) {
        super(indicator, analysisResult, message);
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}
