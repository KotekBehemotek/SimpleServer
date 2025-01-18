package main.processing.resourcing;

import main.server.messages.ComparableArray;
import main.server.messages.Response;
import main.server.messages.bodies.Body;
import main.server.messages.bodies.MultiPartBody;
import main.server.messages.bodies.SinglePartBody;
import main.utility.Bytes;
import main.utility.SingleByte;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class MessageByteCompiler {

    public static byte[] compileMessageBytes(Response response) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byteArrayOutputStream.write(response.getStatusLineValue(Bytes.VERSION.getValue()));
        byteArrayOutputStream.write(SingleByte.SIGN_SPACE.getValue());
        byteArrayOutputStream.write(response.getStatusLineValue(Bytes.CODE.getValue()));
        byteArrayOutputStream.write(SingleByte.SIGN_SPACE.getValue());
        byteArrayOutputStream.write(response.getStatusLineValue(Bytes.MESSAGE.getValue()));

        for (Map.Entry<ComparableArray, byte[]> entry : response.getHead().entrySet()) {
            byte[] name = entry.getKey().getArray(),
                value = entry.getValue();

            byteArrayOutputStream.write(Bytes.SIGN_ENTER.getValue());
            byteArrayOutputStream.write(name);
            byteArrayOutputStream.write(value);
        }

        Body body = response.getBody();

        if (body != null) {
            if (body instanceof SinglePartBody singlePartBody) {
                byteArrayOutputStream.write(Bytes.SIGN_ENTER.getValue());
                byteArrayOutputStream.write(Bytes.SIGN_ENTER.getValue());
//                byteArrayOutputStream.write(singlePartBody.getName());
//                byteArrayOutputStream.write(ByteToChar.SIGN_EQUALS.getValue());
                byteArrayOutputStream.write(singlePartBody.getBodyBytes());
            }
        }

        System.out.println();
        System.out.println(response.toString(true));

        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] getBodyLength(Body body) {
        if (body instanceof SinglePartBody singlePartBody) {
            return String.valueOf(singlePartBody.getBodyBytes().length).getBytes();
        } else {
            MultiPartBody multiPartBody = (MultiPartBody) body;
            return String.valueOf(multiPartBody.getBodyBytes().length + 4).getBytes();
        }
    }

}
