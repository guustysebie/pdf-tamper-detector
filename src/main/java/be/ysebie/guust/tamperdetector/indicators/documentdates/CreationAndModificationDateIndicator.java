package be.ysebie.guust.tamperdetector.indicators.documentdates;

import com.itextpdf.kernel.pdf.PdfDate;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfName;

import be.ysebie.guust.tamperdetector.AnalysisResult;
import be.ysebie.guust.tamperdetector.Indicator;
import be.ysebie.guust.tamperdetector.results.Result;
import be.ysebie.guust.tamperdetector.contexts.DocumentLevelContext;
import be.ysebie.guust.tamperdetector.results.MessageResult;
import java.util.Calendar;

public abstract class CreationAndModificationDateIndicator extends Indicator {

    private DocumentLevelContext context;
    private String creationDate;
    private String modificationDate;



    private Calendar creationDateCalendar;
    private Calendar modificationDateCalendar;

    protected Calendar getCreationDateCalendar() {
        return creationDateCalendar;
    }

    protected Calendar getModificationDateCalendar() {
        return modificationDateCalendar;
    }

    protected String getCreationDate() {
        return creationDate;
    }

    protected String getModificationDate() {
        return modificationDate;
    }

    protected DocumentLevelContext getContext() {
        return context;
    }

    @Override
    public Result onAnalyzeDocumentLevel(DocumentLevelContext context) {
        PdfDocument pdfDocument = context.getPdfDocument();
        PdfDocumentInfo documentInfo = pdfDocument.getDocumentInfo();
        documentInfo.getMoreInfo(PdfName.CreationDate.toString());
        this.creationDate = pdfDocument.getDocumentInfo().getMoreInfo(PdfName.CreationDate.getValue());
        this.modificationDate = pdfDocument.getDocumentInfo().getMoreInfo(PdfName.ModDate.getValue());
        this.creationDateCalendar = PdfDate.decode(creationDate);
        this.modificationDateCalendar = PdfDate.decode(modificationDate);
        if (getCreationDate() == null || getModificationDate() == null) {
            return new MessageResult(this, AnalysisResult.UNDETERMINED,
                    "CreationDate or ModificationDate not found in document info dictionary");
        }

        if (context.hasForm()){
            return new MessageResult(this, AnalysisResult.NO_ANALYSIS_DONE, "Form found");
        }
        if (context.hasSignatures()){
            return new MessageResult(this, AnalysisResult.NO_ANALYSIS_DONE, "Signatures found");
        }
        return analyze();
    }

    public abstract Result analyze();
}
