package main.processing;

import main.processing.resourcing.MessageByteCompiler;
import main.processing.resourcing.ResourceProvider;
import main.server.messages.Response;
import main.utility.Bytes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static main.processing.ByteUtility.extractExtensionFromBytes;

public class RequestResponder {

    private RequestResponder() {
    }

    public static byte[] respondWithError(byte[] errorCode, byte[] errorMessage) {
        Response response = new Response();

        response.setStatusLineValue(Bytes.VERSION.getValue(), Bytes.HTTP_1_1.getValue());
        response.setStatusLineValue(Bytes.CODE.getValue(), errorCode);
        response.setStatusLineValue(Bytes.MESSAGE.getValue(), errorMessage);

        try {
            return MessageByteCompiler.compileMessageBytes(response);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return Bytes.CRITICAL_ARRAY.getValue();
        }
    }

    public static byte[] respondWithFile(byte[] fileAddress) throws IOException {
        Response response = new Response();

        try {
            Path pathToFile;

            try {
                pathToFile = ResourceProvider.provideFile(fileAddress);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
                return respondWithError(Bytes.CODE_404.getValue(), Bytes.MESSAGE_NOT_FOUND.getValue());
            }

            byte[] bytesToFile = pathToFile.getFileName().toString().getBytes();
            byte[] foundContentType = findContentType(extractExtensionFromBytes(bytesToFile));

            if (foundContentType != null) {
                response.setSinglePartBody(bytesToFile, Files.readAllBytes(pathToFile));
                response.setHeader(Bytes.HEADER_CONTENT_LENGTH.getValue(), MessageByteCompiler.getBodyLength(response.getBody()));
                response.setHeader(Bytes.HEADER_CONTENT_TYPE.getValue(), foundContentType);

                response.setStatusLineValue(Bytes.VERSION.getValue(), Bytes.HTTP_1_1.getValue());
                response.setStatusLineValue(Bytes.CODE.getValue(), Bytes.CODE_200.getValue());
                response.setStatusLineValue(Bytes.MESSAGE.getValue(), Bytes.MESSAGE_OK.getValue());
            } else {
                respondWithError(Bytes.CODE_418.getValue(), Bytes.MESSAGE_IM_A_TEAPOT.getValue());
            }
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            response.setStatusLineValue(Bytes.VERSION.getValue(), Bytes.HTTP_1_1.getValue());
            response.setStatusLineValue(Bytes.CODE.getValue(), Bytes.CODE_404.getValue());
            response.setStatusLineValue(Bytes.MESSAGE.getValue(), Bytes.MESSAGE_NOT_FOUND.getValue());
        }

        return MessageByteCompiler.compileMessageBytes(response);
    }

    private static byte[] findContentType(byte[] contentType) {
        HashMap<byte[], byte[]> typeConversionMap = new HashMap<>() {{
            // html, text/html
            put(new byte[]{0x68, 0x74, 0x6d, 0x6c}, new byte[]{0x74, 0x65, 0x78, 0x74, 0x2f, 0x68, 0x74, 0x6d, 0x6c});
            // css, text/css
            put(new byte[]{0x63, 0x73, 0x73}, new byte[]{0x74, 0x65, 0x78, 0x74, 0x2f, 0x63, 0x73, 0x73});
            // js, text/javascript
            put(new byte[]{0x6a, 0x73}, new byte[]{0x74, 0x65, 0x78, 0x74, 0x2f, 0x6a, 0x61, 0x76, 0x61, 0x73, 0x63, 0x72, 0x69, 0x70, 0x74});
            // module.js, text/javascript
            put(new byte[]{0x6d, 0x6f, 0x64, 0x75, 0x6c, 0x65, 0x2e, 0x6a, 0x73}, new byte[]{0x74, 0x65, 0x78, 0x74, 0x2f, 0x6a, 0x61, 0x76, 0x61, 0x73, 0x63, 0x72, 0x69, 0x70, 0x74});
            // jpg, image/jpeg
            put(new byte[]{0x6a, 0x70, 0x67}, new byte[]{0x69, 0x6d, 0x61, 0x67, 0x65, 0x2f, 0x6a, 0x70, 0x65, 0x67});
            // jpeg, image/jpeg
            put(new byte[]{0x6a, 0x70, 0x65, 0x67}, new byte[]{0x69, 0x6d, 0x61, 0x67, 0x65, 0x2f, 0x6a, 0x70, 0x65, 0x67});
            // png, image/png
            put(new byte[]{0x70, 0x6e, 0x67}, new byte[]{0x69, 0x6d, 0x61, 0x67, 0x65, 0x2f, 0x70, 0x6e, 0x67});
            // webp, image/webp
            put(new byte[]{0x77, 0x65, 0x62, 0x70}, new byte[]{0x69, 0x6d, 0x61, 0x67, 0x65, 0x2f, 0x77, 0x65, 0x62, 0x70});
            // ttf, font/ttf
            put(new byte[]{0x74, 0x74, 0x66}, new byte[]{0x66, 0x6f, 0x6e, 0x74, 0x2f, 0x74, 0x74, 0x66});
            // glb, model/gltf-binary
            put(new byte[]{0x67, 0x6c, 0x62}, new byte[]{0x6d, 0x6f, 0x64, 0x65, 0x6c, 0x2f, 0x67, 0x6c, 0x74, 0x66, 0x2d, 0x62, 0x69, 0x6e, 0x61, 0x72, 0x79});
        }};

        boolean mimeTypeFound = false;
        byte[] foundMimeType = null;

        for (Map.Entry<byte[], byte[]> entry : typeConversionMap.entrySet()) {
            byte[] extension = entry.getKey();
            byte[] mimeType = entry.getValue();

            if (Arrays.equals(extension, contentType)) {
                foundMimeType = mimeType;
                mimeTypeFound = true;
                break;
            }
        }

        if (mimeTypeFound) {
            return foundMimeType;
        } else {
            return null;
        }
    }

}
