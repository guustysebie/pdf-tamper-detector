package be.ysebie.guust.tamperdetector.indicators.util;

import be.ysebie.guust.tamperdetector.IIndicatorFactory;
import be.ysebie.guust.tamperdetector.Indicator;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class SimpleInstanceFactory implements IIndicatorFactory {


    private final Indicator indicator;

    public SimpleInstanceFactory(Indicator indicator) {
        this.indicator = indicator;
    }

    @Override
    public List<Supplier<Indicator>> indicators() {
        return Collections.singletonList(() -> indicator);
    }
}
