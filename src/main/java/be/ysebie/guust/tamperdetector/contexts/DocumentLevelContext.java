package be.ysebie.guust.tamperdetector.contexts;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.signatures.SignatureUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class DocumentLevelContext {

    private final ByteLevelContext byteLevelContext;

    private final PdfDocument pdfDocument;
    public DocumentLevelContext(ByteLevelContext byteLevelContext) throws IOException {
        this.byteLevelContext = byteLevelContext;
        this.pdfDocument = new PdfDocument(new PdfReader(new ByteArrayInputStream(byteLevelContext.getData())));
    }





    public ByteLevelContext getByteLevelContext() {
        return byteLevelContext;
    }

    public PdfDocument getPdfDocument() {
        return pdfDocument;
    }

    public boolean hasForm() {
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDocument, false);
        return form != null;
    }

    public boolean hasSignatures() {
        SignatureUtil signUtil = new SignatureUtil(pdfDocument);
        List<String> names = signUtil.getSignatureNames();
        return !names.isEmpty();
    }
}
