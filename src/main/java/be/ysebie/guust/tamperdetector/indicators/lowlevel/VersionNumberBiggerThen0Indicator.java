package be.ysebie.guust.tamperdetector.indicators.lowlevel;

import com.itextpdf.kernel.pdf.PdfIndirectReference;
import com.itextpdf.kernel.pdf.PdfObject;

import be.ysebie.guust.tamperdetector.AnalysisResult;
import be.ysebie.guust.tamperdetector.Indicator;
import be.ysebie.guust.tamperdetector.contexts.DocumentLevelContext;
import be.ysebie.guust.tamperdetector.results.ListOfResult;
import be.ysebie.guust.tamperdetector.results.MessageResult;
import be.ysebie.guust.tamperdetector.results.Result;
import java.util.ArrayList;
import java.util.List;

public class VersionNumberBiggerThen0Indicator extends Indicator {


    @Override
    public Result onAnalyzeDocumentLevel(DocumentLevelContext context) {
        if(context.hasSignatures() || context.hasForm()){
            return new MessageResult(this, AnalysisResult.NO_ANALYSIS_DONE, "Document has signatures or form fields, skipping this indicator");
        }
        List<Result> result = new ArrayList<>();
        int amountOfObjects = context.getPdfDocument().getNumberOfPdfObjects();
        //starting from 1 because object 0 is the place holder for the xref table
        for (int i = 1; i < amountOfObjects; i++) {
            PdfObject obj = context.getPdfDocument().getPdfObject(i);
            if (obj == null) {
                result.add(new MessageResult(this, AnalysisResult.TAMPERING_DETECTED, "Object " + i + " is null"));
                continue;
            }
            PdfIndirectReference ref = obj.getIndirectReference();
            if (ref.getGenNumber() > 0 ){
                result.add(new MessageResult(this, AnalysisResult.TAMPERING_DETECTED, "Object " + i + " has a gen number bigger then 0"));
                continue;
            }
        }
        if (result.isEmpty()){
            return new Result(this, AnalysisResult.NO_TAMPERING);
        } else {
            return new ListOfResult(this, AnalysisResult.TAMPERING_DETECTED).addAll(result);
        }
    }
}
