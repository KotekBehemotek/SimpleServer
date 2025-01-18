package main.server.messages;

import main.server.messages.bodies.Body;
import main.server.messages.bodies.MultiPartBody;
import main.server.messages.bodies.SinglePartBody;
import main.server.messages.bodies.parts.Part;
import main.utility.Bytes;
import main.utility.SingleByte;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static main.processing.ByteUtility.*;

public abstract class Message {

    private final HashMap<ComparableArray, byte[]> statusLine = new HashMap<>() {{
        put(new ComparableArray(Bytes.VERSION.getValue()), null);
    }};

    private final HashMap<ComparableArray, byte[]> head = new HashMap<>();

    private Body body;

    public HashMap<ComparableArray, byte[]> getStatusLine() {
        return this.statusLine;
    }

    public byte[] getStatusLineValue(byte[] key) {
        return this.statusLine.get(new ComparableArray(key));
    }

    public void setStatusLineValue(byte[] key, byte[] value) {
        this.statusLine.put(new ComparableArray(key), value);
    }

    public HashMap<ComparableArray, byte[]> getHead() {
        return this.head;
    }

    public byte[] getHeader(byte[] key) {
        return this.head.get(new ComparableArray(key));
    }

    public void setSubHeader(byte[] key, byte[] subValue) {
        setHeader(key, concatenateTwoByteArrays(getHeader(key), subValue));
    }

    public byte[] getSubHeader(byte[] key, byte[] subKey) {
        final byte[] headerSplitOnSubKey = splitBytesOnBytes(getHeader(key), subKey)[1];

        if (bytesContainByte(headerSplitOnSubKey, SingleByte.SIGN_SEMICOLON.getValue())) {
            return splitBytesOnByte(splitBytesOnBytes(getHeader(key), subKey)[1], SingleByte.SIGN_SEMICOLON.getValue(), false)[0];
        } else {
            return headerSplitOnSubKey;
        }
    }

    public void setHeader(byte[] key, byte[] value) {
        this.head.put(new ComparableArray(key), value);
    }

    public void setSinglePartBody(byte[] name, byte[] bytes) {
        this.body = new SinglePartBody(name, bytes);
    }

    public void setMultipartBody(byte[] separator) {
        body = new MultiPartBody(separator);
    }

    public void addSimplePart(byte[] name, byte[] disposition, byte[] partBytes) {
        if (body instanceof MultiPartBody) {
            ((MultiPartBody) body).addSimplePart(name, disposition, partBytes);
        }
    }

    public void addFilePart(byte[] name, byte[] disposition, byte[] partBytes, byte[] type, byte[] filename) {
        if (body instanceof MultiPartBody) {
            ((MultiPartBody) body).addFilePart(name, disposition, partBytes, type, filename);
        }
    }

    public Body getBody() {
        return body;
    }

    public Part getPartAtIndex(int index) {
        if (body instanceof MultiPartBody) {
            return ((MultiPartBody) body).getPartAtIndex(index);
        } else {
            return null;
        }
    }

    public int getNumberOfBodies() {
        if (body instanceof MultiPartBody) {
            return ((MultiPartBody) body).getNumberOfParts();
        }

        return 1;
    }

    public String toString(final boolean includeBytes) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("*** Message ***\n");

        for (ComparableArray key : statusLine.keySet()) {
            stringBuilder.append(new String(key.getArray(), StandardCharsets.UTF_8)).append(": ").append(new String(statusLine.get(key), StandardCharsets.UTF_8)).append("\n");
        }

        stringBuilder.append("\n");

        for (ComparableArray key : head.keySet()) {
            stringBuilder.append(new String(key.getArray(), StandardCharsets.UTF_8)).append(": ").append(new String(head.get(key))).append("\n");
        }

        if (body != null) {
            stringBuilder.append("\n");
            stringBuilder.append(body.toString(includeBytes));
        }

        stringBuilder.append("*** Message End ***");

        return stringBuilder.toString();
    }

}