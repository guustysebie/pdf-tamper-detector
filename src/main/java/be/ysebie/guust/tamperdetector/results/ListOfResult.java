package be.ysebie.guust.tamperdetector.results;

import be.ysebie.guust.tamperdetector.AnalysisResult;
import be.ysebie.guust.tamperdetector.Indicator;
import java.util.ArrayList;
import java.util.List;

public class ListOfResult extends Result {


    private final List<Result> messages;

    public ListOfResult(Indicator indicator,
            AnalysisResult analysisResult) {
        super(indicator, analysisResult);
        this.messages = new ArrayList<>();
    }

    public List<Result> get() {
        return messages;
    }

    public ListOfResult add(Result message) {
        messages.add(message);
        return this;
    }

    public ListOfResult addAll(List<Result> messages) {
        this.messages.addAll(messages);
        return this;
    }

    @Override
    public boolean isTamperingDetected() {
        for (Result message : messages) {
            if (message.isTamperingDetected()) {
                return true;
            }
        }
        return super.isTamperingDetected();
    }

    @Override
    public String format() {
        StringBuilder sb = new StringBuilder();
        for (Result message : messages) {
            sb.append(message.format());
            sb.append("\n");
        }
        return sb.toString();
    }
}
