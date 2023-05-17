package be.ysebie.guust.tamperdetector.indicators.signatures;

import com.itextpdf.signatures.PdfPKCS7;
import com.itextpdf.signatures.SignatureUtil;

import be.ysebie.guust.tamperdetector.AnalysisResult;
import be.ysebie.guust.tamperdetector.Indicator;
import be.ysebie.guust.tamperdetector.contexts.DocumentLevelContext;
import be.ysebie.guust.tamperdetector.results.ExceptionResult;
import be.ysebie.guust.tamperdetector.results.ListOfResult;
import be.ysebie.guust.tamperdetector.results.MessageResult;
import be.ysebie.guust.tamperdetector.results.Result;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class ValidSignatureIndicator extends Indicator {

    @Override
    public Result onAnalyzeDocumentLevel(DocumentLevelContext context) {
        SignatureUtil signUtil = new SignatureUtil(context.getPdfDocument());
        List<String> names = signUtil.getSignatureNames();
        boolean isValid = true;
        List<Result> messages = new ArrayList<>();
        for (String name : names) {
            try {
                PdfPKCS7 pkcs7 = signUtil.readSignatureData(name);
                isValid &= pkcs7.verifySignatureIntegrityAndAuthenticity();
                isValid &= pkcs7.isRevocationValid();
                if (isValid) {
                    messages.add(new MessageResult(this, AnalysisResult.NO_TAMPERING, "Signature " + name + " is valid"));
                } else {
                    messages.add(new MessageResult(this, AnalysisResult.TAMPERING_DETECTED, "Signature " + name + " is NOT valid"));
                }
            } catch (GeneralSecurityException e) {
                isValid = false;
                messages.add(new ExceptionResult(this, AnalysisResult.UNDETERMINED ,"Validating signature " + name + "Threw an exception" , e));
            }
        }
        AnalysisResult result = isValid ? AnalysisResult.NO_TAMPERING : AnalysisResult.TAMPERING_DETECTED;
        return new ListOfResult(this, result).addAll(messages);
    }
}
