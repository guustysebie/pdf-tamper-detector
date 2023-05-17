package be.ysebie.guust.tamperdetector.indicators.util;

import be.ysebie.guust.tamperdetector.IContinuationStrategy;
import be.ysebie.guust.tamperdetector.results.Result;

public class AlwaysContinueStrategy implements IContinuationStrategy {
    @Override
    public boolean shouldContinue(Result result) {
        return true;
    }
}
