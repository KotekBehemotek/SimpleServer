package main.server.messages.bodies.parts;

import main.utility.Bytes;

import java.util.HashMap;

public abstract class Part {

    private final HashMap<byte[], byte[]> partData = new HashMap<>() {{
        // name
        put(Bytes.NAME.getValue(), null);
        // disposition
        put(Bytes.DISPOSITION.getValue(), null);
    }};

    private final byte[] partBytes;

    public Part(final byte[] name, final byte[] disposition, final byte[] partBytes) {
        partData.put(Bytes.NAME.getValue(), name);
        partData.put(Bytes.DISPOSITION.getValue(), disposition);

        this.partBytes = partBytes;
    }

    public byte[] getPartData(final byte[] key) {
        return partData.get(key);
    }

    public void setPartData(final byte[] key, final byte[] value) {
        partData.put(key, value);
    }

    public HashMap<byte[], byte[]> getAllPartData() {
        return partData;
    }

    public byte[] getPartBytes() {
        return partBytes;
    }

    public String toString(final boolean includeBytes) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Name: ").append(new String(partData.get(Bytes.NAME.getValue()))).append("\n")
                .append("Disposition: ").append(new String(partData.get(Bytes.DISPOSITION.getValue()))).append("\n").append("\n");

        if (includeBytes) {
            stringBuilder.append("Bytes: ").append(new String(partBytes));
        }

        return stringBuilder.toString();
    }

}
