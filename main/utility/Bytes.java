package main.utility;

public enum Bytes {

    // CRLF
    SIGN_ENTER(new byte[] {0x0D, 0x0A}),

    // method
    METHOD(new byte[] {0x6d, 0x65, 0x74, 0x68, 0x6f, 0x64}),
    // target
    TARGET(new byte[] {0x74, 0x61, 0x72, 0x67, 0x65, 0x74}),
    // status
    STATUS(new byte[] {0x73, 0x74, 0x61, 0x74, 0x75, 0x73}),
    // version
    VERSION(new byte[] {0x76, 0x65, 0x72, 0x73, 0x69, 0x6f, 0x6e}),
    // message
    MESSAGE(new byte[] {0x6d, 0x65, 0x73, 0x73, 0x61, 0x67, 0x65}),
    // code
    CODE(new byte[] {0x63, 0x6f, 0x64, 0x65}),

    // HTTP/1.1
    HTTP_1_1(new byte[] {0x48, 0x54, 0x54, 0x50, 0x2f, 0x31, 0x2e, 0x31}),

    // name
    NAME(new byte[] {0x6e, 0x61, 0x6d, 0x65}),
    // filename
    FILENAME(new byte[] {0x66, 0x69, 0x6c, 0x65, 0x6e, 0x61, 0x6d, 0x65}),
    // type
    TYPE(new byte[] {0x74, 0x79, 0x70, 0x65}),
    // disposition
    DISPOSITION(new byte[] {0x64, 0x69, 0x73, 0x70, 0x6f, 0x73, 0x69, 0x74, 0x69, 0x6f, 0x6e}),
    // extension
    EXTENSION(new byte[] {0x65, 0x78, 0x74, 0x65, 0x6e, 0x73, 0x69, 0x6f, 0x6e}),

    // : ***
    HEADER_SEPARATOR(new byte[] {0x3a, 0x20}),
    // --
    MULTIPART_BOUNDARY_START(new byte[] {0x2d, 0x2d}),
    // Access-Control-Allow-Origin
    HEADER_ACCESS_CONTROL_ALLOW_ORIGIN(new byte[] {0x41, 0x63, 0x63, 0x65, 0x73, 0x73, 0x2d, 0x43, 0x6f, 0x6e, 0x74, 0x72, 0x6f, 0x6c, 0x2d, 0x41, 0x6c, 0x6c, 0x6f, 0x77, 0x2d, 0x4f, 0x72, 0x69, 0x67, 0x69, 0x6e}),
    // Content-Language
    HEADER_CONTENT_LANGUAGE(new byte[] {0x43, 0x6f, 0x6e, 0x74, 0x65, 0x6e, 0x74, 0x2d, 0x4c, 0x61, 0x6e, 0x67, 0x75, 0x61, 0x67, 0x65}),
    // Content-Length
    HEADER_CONTENT_LENGTH(new byte[] {0x43, 0x6f, 0x6e, 0x74, 0x65, 0x6e, 0x74, 0x2d, 0x4c, 0x65, 0x6e, 0x67, 0x74, 0x68}),
    // Content-Type
    HEADER_CONTENT_TYPE(new byte[] {0x43, 0x6f, 0x6e, 0x74, 0x65, 0x6e, 0x74, 0x2d, 0x54, 0x79, 0x70, 0x65}),

    // boundary=
    SUB_HEADER_BOUNDARY(new byte[] {0x62, 0x6f, 0x75, 0x6e, 0x64, 0x61, 0x72, 0x79, 0x3d}),

    // multipart/form-data
    HEADER_VALUE_MULTIPART(new byte[] {0x6d, 0x75, 0x6c, 0x74, 0x69, 0x70, 0x61, 0x72, 0x74, 0x2f, 0x66, 0x6f, 0x72, 0x6d, 0x2d, 0x64, 0x61, 0x74, 0x61}),

    // PL-pl
    VALUE_PL_PL(new byte[] {0x70, 0x6c, 0x2d, 0x50, 0x4c}),

    // 200
    CODE_200(new byte[] {0x32, 0x30, 0x30}),
    // 303
    CODE_303(new byte[] {0x33, 0x30, 0x33}),
    // 400
    CODE_400(new byte[] {0x34, 0x30, 0x30}),
    // 404
    CODE_404(new byte[] {0x34, 0x30, 0x34}),
    // 411
    CODE_411(new byte[] {0x34, 0x31, 0x31}),
    // 418
    CODE_418(new byte[] {0x34, 0x31, 0x38}),
    // 500
    CODE_500(new byte[] {0x35, 0x30, 0x30}),

    // OK
    MESSAGE_OK(new byte[] {0x4f, 0x4b}),
    // See Other
    MESSAGE_SEE_OTHER(new byte[] {0x53, 0x65, 0x65, 0x20, 0x4f, 0x74, 0x68, 0x65, 0x72}),
    // Bad Request
    MESSAGE_BAD_REQUEST(new byte[] {0x42, 0x61, 0x64, 0x20, 0x52, 0x65, 0x71, 0x75, 0x65, 0x73, 0x74}),
    // Not Found
    MESSAGE_NOT_FOUND(new byte[] {0x4e, 0x6f, 0x74, 0x20, 0x46, 0x6f, 0x75, 0x6e, 0x64}),
    // Length Required
    MESSAGE_LENGTH_REQUIRED(new byte[] {0x4c, 0x65, 0x6e, 0x67, 0x74, 0x68, 0x20, 0x52, 0x65, 0x71, 0x75, 0x69, 0x72, 0x65, 0x64}),
    // I'm a teapot
    MESSAGE_IM_A_TEAPOT(new byte[] {0x49, 0x27, 0x6d, 0x20, 0x61, 0x20, 0x74, 0x65, 0x61, 0x70, 0x6f, 0x74}),
    // Internal Server Error
    MESSAGE_INTERNAL_SERVER_ERROR(new byte[] {0x49, 0x6e, 0x74, 0x65, 0x72, 0x6e, 0x61, 0x6c, 0x20, 0x53, 0x65, 0x72, 0x76, 0x65, 0x72, 0x20, 0x45, 0x72, 0x72, 0x6f, 0x72}),

    // Content-Disposition: ***
    MULTIPART_CONTENT_DISPOSITION(new byte[] {0x43, 0x6f, 0x6e, 0x74, 0x65, 0x6e, 0x74, 0x2d, 0x44, 0x69, 0x73, 0x70, 0x6f, 0x73, 0x69, 0x74, 0x69, 0x6f, 0x6e, 0x3a, 0x20}),
    // Content-Type: ***
    MULTIPART_CONTENT_TYPE(new byte[] {0x43, 0x6f, 0x6e, 0x74, 0x65, 0x6e, 0x74, 0x2d, 0x54, 0x79, 0x70, 0x65, 0x3a, 0x20}),
    // ; name=
    MULTIPART_NAME(new byte[] {0x3b, 0x20, 0x6e, 0x61, 0x6d, 0x65, 0x3d}),
    // ; filename=
    MULTIPART_FILENAME(new byte[] {0x3b, 0x20, 0x66, 0x69, 0x6c, 0x65, 0x6e, 0x61, 0x6d, 0x65, 0x3d}),

    // Application/Octet-Stream
    MULTIPART_OCTET_STREAM_VALUE(new byte[] {0x61, 0x70, 0x70, 0x6c, 0x69, 0x63, 0x61, 0x74, 0x69, 0x6f, 0x6e, 0x2f, 0x6f, 0x63, 0x74, 0x65, 0x74, 0x2d, 0x73, 0x74, 0x72, 0x65, 0x61, 0x6d}),

    // /text
    TEXT_URL_PREFIX(new byte[] {0x2f, 0x74, 0x65, 0x78, 0x74}),
    // /image
    IMAGE_URL_PREFIX(new byte[] {0x2f, 0x69, 0x6d, 0x61, 0x67, 0x65}),

    // Http/1.1 500 Internal Server Error
    CRITICAL_ARRAY(new byte[] {0x48, 0x74, 0x74, 0x70, 0x2f, 0x31, 0x2e, 0x31, 0x20, 0x35, 0x30, 0x30, 0x20, 0x49, 0x6e, 0x74, 0x65, 0x72, 0x6e, 0x61, 0x6c, 0x20, 0x53, 0x65, 0x72, 0x76, 0x65, 0x72, 0x20, 0x45, 0x72, 0x72, 0x6f, 0x72});

    private final byte[] VALUE;

    Bytes(final byte[] value) {
        this.VALUE = value;
    }

    public byte[] getValue() {
        return VALUE;
    }

}
