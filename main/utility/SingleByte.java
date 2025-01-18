package main.utility;

public enum SingleByte {

    // .
    SIGN_DOT((byte) 46),
    // "
    SIGN_QUOTE((byte) 34),
    // *** ***
    SIGN_SPACE((byte) 32),
    // =
    SIGN_EQUALS((byte) 61),
    // -
    SIGN_DASH((byte) 45),
    // ;
    SIGN_SEMICOLON((byte) 59),
    // /
    SIGN_SLASH((byte) 47);

    private final byte VALUE;

    SingleByte(byte value) {
        this.VALUE = value;
    }

    public byte getValue() {
        return VALUE;
    }

}
