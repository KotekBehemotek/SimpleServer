package main.processing;

import main.server.messages.Request;
import main.server.messages.bodies.Body;
import main.utility.Bytes;
import main.utility.SingleByte;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static main.processing.ByteUtility.*;

public class RequestInterpreter {

    private RequestInterpreter() {
    }

    private static Request parseRequest(InputStream inputStream) throws IOException {

        final Request request = new Request();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        InputStreamReader requestStreamReader = new InputStreamReader(inputStream);

        byte[] separator = null;

        final int SP = 0x20,
                CR = 0x0D,
                LF = 0x0A,
                EOF = -1;

        int readByte = requestStreamReader.read(),
                lastReadByte = readByte,
                contentLengthCounter = 1,
                currentRequestBlock = 0,
                separatorPresent = -1,
                contentLength = 0;

        boolean spPresent = false,
                lfPresent = false;

        requestLoop:
        while (readByte != EOF) {
//            System.out.print((char) readByte);
            switch (currentRequestBlock) {

                // request status line
                case 0:

                    // if currently read byte is space
                    if (readByte == SP) {

                        // if there was a space present in status line already
                        if (spPresent) {

                            // switching to request headers block and saving accumulated bytes as request target
                            currentRequestBlock = 1;
                            request.setStatusLineValue(Bytes.TARGET.getValue(), byteArrayOutputStream.toByteArray());

                            // if it was the first space in the status line
                        } else {

                            // marking there was a space present and saving accumulated bytes as request method
                            spPresent = true;
                            request.setStatusLineValue(Bytes.METHOD.getValue(), byteArrayOutputStream.toByteArray());
                        }

                        // clearing accumulated bytes buffer
                        byteArrayOutputStream.reset();

                        // if currently read byte is not a space
                    } else {

                        // writing byte to the buffer
                        byteArrayOutputStream.write(readByte);
                    }
                    break;

                // request headers
                case 1:

                    // if currently read byte with last read byte make Enter together
                    if (readByte == LF && lastReadByte == CR) {

                        // if there was LF present already
                        if (lfPresent) {

                            // saving accumulated bytes as request header after splitting it on ": "
                            byte[][] readHeader = splitBytesOnBytes(byteArrayOutputStream.toByteArray(), Bytes.HEADER_SEPARATOR.getValue());
                            request.setHeader(readHeader[0], readHeader[1]);

                            // if it is the first Enter in headers block
                        } else {

                            // marking Enter was already present in the header block and saving accumulated bytes as a request version
                            lfPresent = true;
                            request.setStatusLineValue(Bytes.VERSION.getValue(), byteArrayOutputStream.toByteArray());
                        }

                        // clearing the byte buffer
                        byteArrayOutputStream.reset();

                        // if currently read byte is not LF and CR either
                    } else if (readByte != CR) {

                        // saving the currently read byte to the buffer
                        byteArrayOutputStream.write(readByte);
                    }

                    // if lastly read byte is the second byte of Enter and currently read byte is the first byte of Enter
                    if (lastReadByte == LF && readByte == CR) {

                        // if there was no Content-Length header found
                        if (request.getHeader(Bytes.HEADER_CONTENT_LENGTH.getValue()) == null) {

                            // exiting the parser
                            break requestLoop;

                            // if Content-Length header was present
                        } else {
                            contentLength = bytesToInt(request.getHeader(Bytes.HEADER_CONTENT_LENGTH.getValue()));

                            // if Content-Type header is multipart/form-data
                            if (bytesStartsWithBytes(request.getHeader(Bytes.HEADER_CONTENT_TYPE.getValue()), Bytes.HEADER_VALUE_MULTIPART.getValue())) {

                                // saving the multipart request boundary and switching to multipart body request block
                                separator = concatenateTwoByteArrays(Bytes.MULTIPART_BOUNDARY_START.getValue(), request.getSubHeader(Bytes.HEADER_CONTENT_TYPE.getValue(), Bytes.SUB_HEADER_BOUNDARY.getValue()));
                                currentRequestBlock = 2;
                                request.setMultipartBody(separator);

                                // if Content-Type header is not multipart/form-data request has to include a single part body
                            } else {
                                currentRequestBlock = 3;
                            }
                        }
                        // skipping last LF
                        readByte = requestStreamReader.read();
                    }
                    break;

                // There is a multipart body in the request
                case 2:

                    // if byte buffer size is greater than multipart boundary length, and it contains "--"
                    if (byteArrayOutputStream.size() >= separator.length && bytesContainBytes(byteArrayOutputStream.toByteArray(), Bytes.MULTIPART_BOUNDARY_START.getValue())) {

                        // if byte buffer contain multipart boundary
                        if (bytesContainBytes(byteArrayOutputStream.toByteArray(), separator)) {

                            // removing multipart boundary from byte buffer
                            final byte[] replacedBytes = removeBytesFromBytes(byteArrayOutputStream.toByteArray(), separator);

                            // resetting the buffer and saving body part to it if removing the boundary went as planned
                            if (replacedBytes != null) {
                                byteArrayOutputStream.reset();
                                byteArrayOutputStream.write(replacedBytes);
                            }

                            // marking current byte count as the last appearance of the boundary
                            separatorPresent = contentLengthCounter;

                            // if there is anything in the buffer
                            if (byteArrayOutputStream.size() > 0) {

                                // if buffer contain "filename" add it to request object as file body part. Else add it as a simple body part
                                if (bytesContainBytes(byteArrayOutputStream.toByteArray(), Bytes.FILENAME.getValue())) {
//                                    addFilePart(byteArrayOutputStream.toByteArray(), request);
                                } else {
//                                    addSimplePart(byteArrayOutputStream.toByteArray(), request);
                                }

                                // clear the buffer
                                byteArrayOutputStream.reset();
                            }
                        }
                    }

                    // if byte count is greater than last separator presence by two
                    if (contentLengthCounter == separatorPresent + 2) {
                        byte signDash = SingleByte.SIGN_DASH.getValue();

                        // if end of multipart boundary was found exiting the parser
                        if ((char) readByte == signDash && (char) lastReadByte == signDash) {
                            break requestLoop;
                        }
                    }

                    // saving byte to byte buffer and updating the content length counter
                    byteArrayOutputStream.write(readByte);
                    contentLengthCounter++;

                    if (contentLengthCounter >= contentLength) {
                        break requestLoop;
                    }

                    break;

                // There is a single body in the request
                case 3:

                    // if byte count is equal to declared content length saving body to request object and exiting parser
                    if (contentLengthCounter >= contentLength) {
                        addSingleBody(byteArrayOutputStream.toByteArray(), request);
                        break requestLoop;

                        // if byte count does not exceed declared content length writing byte to the buffer and incrementing byte count
                    } else {
                        byteArrayOutputStream.write(readByte);
                        contentLengthCounter++;
                    }

                    break;
            }

            // updating byte variables. Each for currently read byte and lastly read byte
            lastReadByte = readByte;
            readByte = requestStreamReader.read();
        }

        System.out.println(request.toString(false));

        return request;
    }

    private static void addSingleBody(byte[] rawBody, Request request) {
        if (rawBody != null) {
            byte[][] splitBody = splitBytesOnByte(rawBody, SingleByte.SIGN_EQUALS.getValue(), false);
            if (splitBody[0] != null && splitBody[1] != null) {
                request.setSinglePartBody(splitBody[0], splitBody[1]);
            } else {
                throw new IllegalArgumentException("Body is invalid");
            }
        } else {
            throw new IllegalArgumentException("Body is null");
        }
    }

    public static byte[] interpretRequest(InputStream inputStream) {
        final Request request;

        try {
            request = parseRequest(inputStream);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return RequestResponder.respondWithError(Bytes.CODE_400.getValue(), Bytes.MESSAGE_BAD_REQUEST.getValue());
        }

        Body body = request.getBody();

        if (body == null) {
            try {
                return RequestResponder.respondWithFile(request.getStatusLineValue(Bytes.TARGET.getValue()));
            } catch (IOException ioException) {
                ioException.printStackTrace();
                return RequestResponder.respondWithError(Bytes.CODE_500.getValue(), Bytes.MESSAGE_INTERNAL_SERVER_ERROR.getValue());
            }
        } else {
            return RequestResponder.respondWithError(Bytes.CODE_418.getValue(), Bytes.MESSAGE_IM_A_TEAPOT.getValue());
        }
    }

}