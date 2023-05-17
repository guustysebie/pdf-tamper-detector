package be.ysebie.guust.tamperdetectorapp.examples;

import be.ysebie.guust.tamperdetector.IContinuationStrategy;
import be.ysebie.guust.tamperdetector.IResultAggregator;
import be.ysebie.guust.tamperdetector.indicators.OnlyContainsImagesIndicator;
import be.ysebie.guust.tamperdetector.indicators.fonts.FontDestributionIndicator;
import be.ysebie.guust.tamperdetector.indicators.overlap.OverlappingTextIndicator;
import be.ysebie.guust.tamperdetector.results.Result;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeighedContinuationStrategy implements IContinuationStrategy {


    private static final HashMap<Type, Integer> WEIGHTS;
    private int maxWeight = 5;
    private int currentWeight = 0;

    static {
        WEIGHTS = new HashMap<>();
        WEIGHTS.put(OverlappingTextIndicator.class, 10);
        WEIGHTS.put(OnlyContainsImagesIndicator.class, 1);
        WEIGHTS.put(FontDestributionIndicator.class, 1);
    }

    @Override
    public boolean shouldContinue(Result result) {
        if (result.isTamperingDetected()) {
            if (WEIGHTS.containsKey(result.getIndicator().getClass())) {
                currentWeight += WEIGHTS.get(result.getIndicator().getClass());
            }
        }
        return currentWeight < maxWeight;
    }
}

