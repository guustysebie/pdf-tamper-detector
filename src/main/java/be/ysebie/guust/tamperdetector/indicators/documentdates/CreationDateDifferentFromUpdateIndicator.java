package be.ysebie.guust.tamperdetector.indicators.documentdates;


import be.ysebie.guust.tamperdetector.AnalysisResult;
import be.ysebie.guust.tamperdetector.results.Result;
import be.ysebie.guust.tamperdetector.results.MessageResult;

public class CreationDateDifferentFromUpdateIndicator extends CreationAndModificationDateIndicator {

    @Override
    public Result analyze() {
        if (getCreationDate().equals(getModificationDate())) {
            return new Result(this, AnalysisResult.NO_TAMPERING);
        }
        return new MessageResult(this, AnalysisResult.TAMPERING_DETECTED,
                "CreationDate and ModificationDate are different");
    }
}


