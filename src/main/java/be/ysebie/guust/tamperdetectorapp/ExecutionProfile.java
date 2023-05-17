package be.ysebie.guust.tamperdetectorapp;

import be.ysebie.guust.tamperdetector.IIndicatorFactory;
import be.ysebie.guust.tamperdetector.Indicator;
import be.ysebie.guust.tamperdetector.indicators.documentdates.CreateOrUpdateDateInTheFutureIndicator;
import be.ysebie.guust.tamperdetector.indicators.documentdates.CreationDateDifferentFromUpdateIndicator;
import be.ysebie.guust.tamperdetector.indicators.documentdates.UpdateDateBeforeCreationDateIndicator;
import be.ysebie.guust.tamperdetector.indicators.fonts.FontDestributionIndicator;
import be.ysebie.guust.tamperdetector.indicators.line.DifferentFontSizeInLineIndicator;
import be.ysebie.guust.tamperdetector.indicators.line.DifferentFontsInASingleLineIndicator;
import be.ysebie.guust.tamperdetector.indicators.lowlevel.VersionNumberBiggerThen0Indicator;
import be.ysebie.guust.tamperdetector.indicators.overlap.OverlappingTextIndicator;
import be.ysebie.guust.tamperdetector.indicators.signatures.ValidSignatureIndicator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class ExecutionProfile implements IIndicatorFactory {
    private final String title;
    private final List<Supplier<Indicator>> indicators;

    public ExecutionProfile(String title, List<Supplier<Indicator>> indicators) {
        this.title = title;
        this.indicators = indicators;
    }

    @Override
    public List<Supplier<Indicator>> indicators() {
        return indicators;
    }

    public static List<ExecutionProfile> getAllProfiles(){
        List<Supplier<Indicator>> indicators = new ArrayList<>();
        indicators.add(OverlappingTextIndicator::new);
        indicators.add(ValidSignatureIndicator::new);
        indicators.add(DifferentFontsInASingleLineIndicator::new);
        indicators.add(DifferentFontSizeInLineIndicator::new);
        indicators.add(CreationDateDifferentFromUpdateIndicator::new);
        indicators.add(VersionNumberBiggerThen0Indicator::new);
        indicators.add(UpdateDateBeforeCreationDateIndicator::new);
        indicators.add(CreateOrUpdateDateInTheFutureIndicator::new);
        indicators.add(() -> new FontDestributionIndicator(0.2f));

        List<ExecutionProfile> profiles = new ArrayList<>();
        for (Supplier<Indicator> indicator : indicators) {
           profiles.add(new ExecutionProfile(indicator.get().getClass().getSimpleName(),
                   Collections.singletonList(indicator)));
        }
        profiles.add(new ExecutionProfile("All", indicators));
        return profiles;
    }


    public String getTitle() {
        return title;
    }
}
