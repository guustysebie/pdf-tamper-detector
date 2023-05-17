package be.ysebie.guust.tamperdetector;

import be.ysebie.guust.tamperdetector.contexts.ByteLevelContext;
import be.ysebie.guust.tamperdetector.contexts.DocumentLevelContext;
import be.ysebie.guust.tamperdetector.contexts.PageLevelContext;
import be.ysebie.guust.tamperdetector.results.MessageResult;
import be.ysebie.guust.tamperdetector.results.Result;

public abstract class Indicator {

    public Result onAnalyzeByteLevel(ByteLevelContext context) {
        return createNoAnalysisDone("byte");
    }

    public Result onAnalyzeDocumentLevel(DocumentLevelContext context) {
        return createNoAnalysisDone("document");
    }

    public Result onAnalyzePageLevel(PageLevelContext context) {
        return createNoAnalysisDone("page");
    }

    public Result onAnalyzePostProcessing(DocumentLevelContext context) {
        return createNoAnalysisDone("post processing");
    }

    private Result createNoAnalysisDone(String level) {
        return new MessageResult(this, AnalysisResult.NO_ANALYSIS_DONE, level);
    }

}

