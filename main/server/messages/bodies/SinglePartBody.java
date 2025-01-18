package main.server.messages.bodies;

public class SinglePartBody implements Body {

    private final byte[] name;
    private final byte[] bodyBytes;

    public SinglePartBody(final byte[] name, final byte[] bodyBytes) {
        this.name = name;
        this.bodyBytes = bodyBytes;
    }

    public byte[] getName() {
        return name;
    }

    public byte[] getBodyBytes() {
        return bodyBytes;
    }

    @Override
    public String toString(boolean includeBytes) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("*** Single Part Body ***\n")
                .append("Name: ").append(new String(name)).append("\n");

        if (includeBytes) {
            stringBuilder.append("Body: ").append(new String(bodyBytes)).append("\n");
        }

        stringBuilder.append("*** Single Part Body End ***\n");

        return stringBuilder.toString();
    }
}
