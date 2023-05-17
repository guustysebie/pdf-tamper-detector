package be.ysebie.guust.tamperdetector.contexts.helper;

import com.itextpdf.kernel.geom.LineSegment;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;

public class ExtendedTextRenderInfo {


    private final float fontSize;
    Rectangle boundingBox;
    TextRenderInfo textRenderInfo;

    private String text;

    private String fontName;

    public ExtendedTextRenderInfo( TextRenderInfo textRenderInfo) {
        this.textRenderInfo = textRenderInfo;
        LineSegment baseline = textRenderInfo.getBaseline();
        Vector startPoint = baseline.getStartPoint();
        Vector endPoint = baseline.getEndPoint();
        float x = startPoint.get(0);
        float x2 = endPoint.get(0);
        float y = startPoint.get(1);
        float y2 = endPoint.get(1);
        Rectangle rectangle = new Rectangle(
                Math.min(x, x2),
                Math.min(y, y2),
                Math.abs(x - x2),
                Math.abs(y - y2));
        this.boundingBox = rectangle;
        this.fontName = textRenderInfo.getFont().getFontProgram().getFontNames().getFontName();
        this.text = textRenderInfo.getText();
        this.fontSize  = textRenderInfo.getFontSize();
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public String getText() {
        return text;
    }

    public String getFontName() {
        return fontName;
    }

    public Float getFontSize() {
        return fontSize;
    }
}
