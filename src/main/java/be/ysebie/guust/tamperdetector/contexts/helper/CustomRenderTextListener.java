package be.ysebie.guust.tamperdetector.contexts.helper;

import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CustomRenderTextListener implements IEventListener {
    private final List<ExtendedTextRenderInfo> extendedTextRenderInfos = new ArrayList<>();


    public List<ExtendedTextRenderInfo> getTextRenderInfos() {
        return extendedTextRenderInfos;
    }

    @Override
    public void eventOccurred(IEventData data, EventType type) {
        if (data != null) {
            if (data instanceof TextRenderInfo) {
                TextRenderInfo textRenderInfo = (TextRenderInfo) data;
                extendedTextRenderInfos.add(new ExtendedTextRenderInfo(textRenderInfo));
            }
        }
    }

    @Override
    public Set<EventType> getSupportedEvents() {
        return new
                HashSet<>(Collections.singletonList(EventType.RENDER_TEXT));
    }


}
