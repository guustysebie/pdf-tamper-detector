package be.ysebie.guust.tamperdetector;

import be.ysebie.guust.tamperdetector.results.Result;
import java.util.List;
import java.util.function.Supplier;

public interface IIndicatorFactory {

    List<Supplier<Indicator>> indicators();

}

