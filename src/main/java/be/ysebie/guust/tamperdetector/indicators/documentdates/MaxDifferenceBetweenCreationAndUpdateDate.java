package be.ysebie.guust.tamperdetector.indicators.documentdates;

import be.ysebie.guust.tamperdetector.AnalysisResult;
import be.ysebie.guust.tamperdetector.results.Result;
import be.ysebie.guust.tamperdetector.results.MessageResult;
import java.util.Calendar;

public class MaxDifferenceBetweenCreationAndUpdateDate extends CreationAndModificationDateIndicator {

    private final int maxDifferenceInDays;

    public MaxDifferenceBetweenCreationAndUpdateDate(int maxDifferenceInDays) {
        this.maxDifferenceInDays = maxDifferenceInDays;
    }

    @Override
    public Result analyze() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getCreationDateCalendar().getTime());
        cal.add(Calendar.DATE, maxDifferenceInDays);
        if (cal.after(getModificationDateCalendar())) {
            return new MessageResult(this, AnalysisResult.TAMPERING_DETECTED,
                    "ModificationDate is more than " + maxDifferenceInDays + " days after CreationDate");
        }
        return new Result(this, AnalysisResult.NO_TAMPERING);
    }
}
