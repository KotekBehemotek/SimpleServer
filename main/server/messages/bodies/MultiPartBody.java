package main.server.messages.bodies;

import main.server.messages.bodies.parts.Part;
import main.server.messages.bodies.parts.PartFile;
import main.server.messages.bodies.parts.PartSimple;
import main.utility.Bytes;
import main.utility.SingleByte;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MultiPartBody implements Body {

    private final List<Part> parts = new ArrayList<>();

    private final byte[] separator;

    public MultiPartBody(byte[] separator) {
        this.separator = separator;
    }

    public boolean isEmpty() {
        return parts.isEmpty();
    }

    public void addSimplePart(byte[] name, byte[] disposition, byte[] partBytes) {
        parts.add(new PartSimple(name, disposition, partBytes));
    }

    public void addFilePart(byte[] name, byte[] disposition, byte[] partBytes, byte[] type, byte[] filename) {
        parts.add(new PartFile(name, disposition, partBytes, type, filename));
    }

    public int getNumberOfParts() {
        return parts.size();
    }

    public Part getPartAtIndex(int index) {
        return parts.get(index);
    }

    public byte[] getBodyBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            for (Part part : parts) {
                final HashMap<byte[], byte[]> partData = part.getAllPartData();

                byteArrayOutputStream.write(Bytes.MULTIPART_CONTENT_DISPOSITION.getValue());
                byteArrayOutputStream.write(partData.get(Bytes.MULTIPART_CONTENT_DISPOSITION.getValue()));
                byteArrayOutputStream.write(Bytes.MULTIPART_NAME.getValue());
                byteArrayOutputStream.write(SingleByte.SIGN_QUOTE.getValue());
                byteArrayOutputStream.write(partData.get(Bytes.MULTIPART_NAME.getValue()));
                byteArrayOutputStream.write(SingleByte.SIGN_QUOTE.getValue());

                if (part instanceof PartFile) {
                    byteArrayOutputStream.write(Bytes.MULTIPART_FILENAME.getValue());
                    byteArrayOutputStream.write(SingleByte.SIGN_QUOTE.getValue());
                    byteArrayOutputStream.write(partData.get(Bytes.MULTIPART_FILENAME.getValue()));
                    byteArrayOutputStream.write(SingleByte.SIGN_QUOTE.getValue());
                    byteArrayOutputStream.write(Bytes.SIGN_ENTER.getValue());
                    byteArrayOutputStream.write(Bytes.MULTIPART_CONTENT_TYPE.getValue());
                    byteArrayOutputStream.write(partData.get(Bytes.MULTIPART_CONTENT_TYPE.getValue()));
                }

                byteArrayOutputStream.write(Bytes.SIGN_ENTER.getValue());
                byteArrayOutputStream.write(Bytes.SIGN_ENTER.getValue());
                byteArrayOutputStream.write(part.getPartBytes());
                byteArrayOutputStream.write(Bytes.MULTIPART_BOUNDARY_START.getValue());
                byteArrayOutputStream.write(separator);
            }

            byteArrayOutputStream.write(Bytes.MULTIPART_BOUNDARY_START.getValue());
        } catch (final IOException exception) {
            exception.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }

    public String toString(boolean includeBytes) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Part part : parts) {
            stringBuilder.append(part.toString(includeBytes));
        }

        return stringBuilder.toString();
    }
}
