package main.server.messages;

import java.util.Arrays;

public class ComparableArray {

    private final byte[] array;

    public ComparableArray(byte[] inputArray) {
        this.array = inputArray;
    }

    public byte[] getArray() {
        return this.array;
    }

    @Override
    public boolean equals(Object inputObject) {
        if (this == inputObject) {
            return true;
        }

        if (inputObject == null || getClass() != inputObject.getClass()) {
            return false;
        }

        ComparableArray comparableArray = (ComparableArray) inputObject;
        return Arrays.equals(array, comparableArray.getArray());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(array);
    }

}
