package be.ysebie.guust.tamperdetectorapp.examples;

import be.ysebie.guust.tamperdetector.IResultAggregator;
import be.ysebie.guust.tamperdetector.results.Result;
import java.util.ArrayList;
import java.util.List;

public class AggregateTamperResultsToList implements IResultAggregator {

    private final List<Result> results = new ArrayList<>();

    @Override
    public void onNewResult(Result result) {
        if (result.isTamperingDetected()){
            results.add(result);
        }
    }
    public List<Result> getResults() {
        return results;
    }
}
