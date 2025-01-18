package main.server.messages.bodies.parts;

import main.utility.Bytes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PartFile extends Part {

    public PartFile(final byte[] name, final byte[] disposition, final byte[] partBytes, final byte[] type, final byte[] filename) {
        super(name, disposition, partBytes);

        setPartData(Bytes.TYPE.getValue(), type);
        setPartData(Bytes.FILENAME.getValue(), filename);
    }

    public byte[] toByteArray() {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            byteArrayOutputStream.write(Bytes.MULTIPART_CONTENT_DISPOSITION.getValue());
            byteArrayOutputStream.write(getPartData(Bytes.DISPOSITION.getValue()));
            byteArrayOutputStream.write(Bytes.MULTIPART_NAME.getValue());
            byteArrayOutputStream.write(getPartData(Bytes.NAME.getValue()));
            byteArrayOutputStream.write(Bytes.MULTIPART_FILENAME.getValue());
            byteArrayOutputStream.write(getPartData(Bytes.FILENAME.getValue()));
            byteArrayOutputStream.write(Bytes.SIGN_ENTER.getValue());
            byteArrayOutputStream.write(Bytes.MULTIPART_CONTENT_TYPE.getValue());
            byteArrayOutputStream.write(getPartData(Bytes.TYPE.getValue()));
            byteArrayOutputStream.write(Bytes.SIGN_ENTER.getValue());
            byteArrayOutputStream.write(Bytes.SIGN_ENTER.getValue());
            byteArrayOutputStream.write(getPartBytes());
        } catch (final IOException exception) {
            exception.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public String toString(boolean includeBytes) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Type: ").append(new String(Bytes.TYPE.getValue())).append("\n");
        stringBuilder.append("Filename: ").append(new String(Bytes.FILENAME.getValue())).append("\n");
        stringBuilder.append(super.toString(includeBytes)).append("\n");

        return stringBuilder.toString();
    }
}
