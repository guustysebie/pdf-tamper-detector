package be.ysebie.guust.tamperdetector.results;

import be.ysebie.guust.tamperdetector.AnalysisResult;
import be.ysebie.guust.tamperdetector.Indicator;

public class MessageResult extends Result {

    private final String message;

    public MessageResult(Indicator indicator, AnalysisResult result, String message) {
        super(indicator, result);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String format() {
        return super.format() + " msg: " + message;
    }
}
