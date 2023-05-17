package be.ysebie.guust.tamperdetector.contexts;

public class ByteLevelContext {

    protected final byte[] data;

    public ByteLevelContext(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
}
