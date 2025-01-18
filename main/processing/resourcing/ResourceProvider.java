package main.processing.resourcing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static main.processing.ByteUtility.subBytes;

public class ResourceProvider {

    public static Path provideFile(byte[] address) throws IOException {
        byte[] indexAddress = {0x68, 0x74, 0x6d, 0x6c, 0x2f, 0x69, 0x6e, 0x64, 0x65, 0x78, 0x2e, 0x68, 0x74, 0x6d, 0x6c};

        if (address == null || address.length < 5) {
            return searchForFile(indexAddress);
        } else {
            return searchForFile(subBytes(address, 1));
        }
    }

    private static Path searchForFile(byte[] address) throws IOException {
        String stringAddress = "resources/" + new String(address, StandardCharsets.UTF_8);

        if (new File(stringAddress).isFile()) {
            return Path.of(stringAddress);
        } else {
            throw new FileNotFoundException("provided address: " + stringAddress + " does not match any file");
        }
    }

}
