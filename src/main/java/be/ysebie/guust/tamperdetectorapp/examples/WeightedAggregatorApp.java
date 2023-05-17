package be.ysebie.guust.tamperdetectorapp.examples;

import be.ysebie.guust.tamperdetector.IContinuationStrategy;
import be.ysebie.guust.tamperdetector.IIndicatorFactory;
import be.ysebie.guust.tamperdetector.IResultAggregator;
import be.ysebie.guust.tamperdetector.Indicator;
import be.ysebie.guust.tamperdetector.TamperDetector;
import be.ysebie.guust.tamperdetector.indicators.OnlyContainsImagesIndicator;
import be.ysebie.guust.tamperdetector.indicators.fonts.FontDestributionIndicator;
import be.ysebie.guust.tamperdetector.indicators.overlap.OverlappingTextIndicator;
import be.ysebie.guust.tamperdetector.results.Result;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class WeightedAggregatorApp implements IResultAggregator, IContinuationStrategy, IIndicatorFactory {

    private static final HashMap<Type, Integer> WEIGHTS;
    private int maxWeight = 5;
    private int currentWeight = 0;

    static {
        WEIGHTS = new HashMap<>();
        WEIGHTS.put(OverlappingTextIndicator.class, 10);
        WEIGHTS.put(OnlyContainsImagesIndicator.class, 1);
        WEIGHTS.put(FontDestributionIndicator.class, 1);
    }

    public static void main(String[] args) throws IOException {
        WeightedAggregatorApp app = new WeightedAggregatorApp();
        TamperDetector detector = new TamperDetector(new byte[0], app, app, app);
        detector.analyze();
    }

    @Override
    public void onNewResult(Result result) {
        if (result.isTamperingDetected()) {
            if (WEIGHTS.containsKey(result.getIndicator().getClass())) {
                currentWeight += WEIGHTS.get(result.getIndicator().getClass());
            }
        }
    }

    @Override
    public boolean shouldContinue(Result result) {
        return maxWeight < currentWeight;
    }


    @Override
    public List<Supplier<Indicator>> indicators() {
        List<Supplier<Indicator>> indicators = new ArrayList<>();
        indicators.add(OverlappingTextIndicator::new);
        indicators.add(OnlyContainsImagesIndicator::new);
        indicators.add(() -> new FontDestributionIndicator(0.2f));
        return indicators;
    }
}
