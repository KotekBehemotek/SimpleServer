package main.server.messages.bodies.parts;

import main.utility.Bytes;
import main.utility.SingleByte;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PartSimple extends Part {

    public PartSimple(final byte[] name, final byte[] disposition, final byte[] partBytes) {
        super(name, disposition, partBytes);
    }

    public byte[] toByteArray() {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            byteArrayOutputStream.write(Bytes.MULTIPART_CONTENT_DISPOSITION.getValue());
            byteArrayOutputStream.write(getPartData(Bytes.DISPOSITION.getValue()));
            byteArrayOutputStream.write(Bytes.MULTIPART_NAME.getValue());
            byteArrayOutputStream.write(SingleByte.SIGN_QUOTE.getValue());
            byteArrayOutputStream.write(getPartData(Bytes.NAME.getValue()));
            byteArrayOutputStream.write(SingleByte.SIGN_QUOTE.getValue());
            byteArrayOutputStream.write(Bytes.SIGN_ENTER.getValue());
            byteArrayOutputStream.write(Bytes.SIGN_ENTER.getValue());
            byteArrayOutputStream.write(getPartBytes());
        } catch (final IOException exception) {
            exception.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }

}
