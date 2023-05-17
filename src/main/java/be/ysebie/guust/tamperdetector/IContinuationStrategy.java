package be.ysebie.guust.tamperdetector;

import be.ysebie.guust.tamperdetector.results.Result;

public interface IContinuationStrategy {

    boolean shouldContinue(Result result);

}
