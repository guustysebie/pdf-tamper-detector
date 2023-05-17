package be.ysebie.guust.tamperdetector;

import be.ysebie.guust.tamperdetector.results.Result;

public interface IResultAggregator {
    void onNewResult(Result result);
}
