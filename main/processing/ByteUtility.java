package main.processing;

import main.utility.SingleByte;

import java.util.Random;

public final class ByteUtility {

    private ByteUtility() {
    }

    private static final byte[] NOT_ARRAY = new byte[] {-1};

    public static byte[] trimBytesWhite(final byte[] inputArray) {
        final byte SP = 0x20,
                   CR = 0x0D,
                   LF = 0x0A;

        final int inputArrayLength = inputArray.length;

        int trimIndex = 0,
            trimEndIndex = inputArrayLength - 1;

        for (final byte b : inputArray) {
            if (b == SP || b == CR || b == LF) {
                trimIndex ++;
            } else {
                break;
            }
        }

        final byte[] trimmedArray = new byte[inputArrayLength - trimIndex - (inputArrayLength - trimEndIndex)];

        System.arraycopy(inputArray, trimIndex, trimmedArray, 0, trimmedArray.length);

        return trimmedArray;
    }

    public static byte[] trimBytes(final byte[] inputArray, final byte[] bytesToBeTrimmed) {
        final int inputArrayLength = inputArray.length,
                  inputArrayLengthDecreased = inputArrayLength - 1;

        int trimIndex = 0,
            trimEndIndex = inputArrayLengthDecreased;

        frontTrimLoop:
        for (final byte b : inputArray) {
            for (final byte b2 : bytesToBeTrimmed) {
                if (b == b2) {
                    trimIndex ++;
                } else {
                    break frontTrimLoop;
                }
            }
        }

        backTrimLoop:
        for (int i = inputArrayLengthDecreased; i >= 0; i --) {
            for (final byte b : bytesToBeTrimmed) {
                if (b == inputArray[i]) {
                    trimEndIndex --;
                } else {
                    break backTrimLoop;
                }
            }
        }

        final byte[] trimmedArray = new byte[inputArrayLength - trimIndex - (inputArrayLength - trimEndIndex)];

        System.arraycopy(inputArray, trimIndex, trimmedArray, 0, trimmedArray.length);

        return trimmedArray;
    }

    public static byte[][] splitBytesOnByte(final byte[] inputArray, final byte splitByte, final boolean containEdge) {
        int splitIndex = 0,
            iterator = 0;

        // To save some if's containEdge is converted to int
        final int inputArrayLength = inputArray.length,
                  numericNegativeContainEdge = !containEdge ? 1 : 0;

        // Iterating over inputArray to find what index splitByte appears at
        for (final byte b : inputArray) {
            // If splitByte was found splitIndex increments to match current loop circle and loop breaks
            if (b == splitByte) {
                splitIndex++;
                break;
            }
            // SplitIndex increments every loop circle to match inputArray index it was found at
            splitIndex++;
        }

        // If splitIndex is at the beginning or end of the InputArray there's no possibility to split so return null
        if (splitIndex <= 1 || splitIndex >= inputArrayLength - 1) {
            return new byte[][]{NOT_ARRAY, NOT_ARRAY};
        }

        // To avoid mathematical operations in loop here are variables
        final int negativeContainEdgeSplitIndex = splitIndex - numericNegativeContainEdge,
                  decreasedSplitIndex = splitIndex - 1;

        // As method returns two sides of inputArray both before and after splitByte there is a need to
        // create two-dimensional array containing two arrays of bytes
        // First array has length of splitIndex - numericNegativeContainEdge to match number of characters before splitByte
        // Second array has length of inputArrayLength - splitIndex to contain only second side of split inputArray
        byte[][] outputArray = new byte[2][];
        outputArray[0] = new byte[negativeContainEdgeSplitIndex];
        outputArray[1] = new byte[inputArrayLength - splitIndex];

        // Iterating over inputArrayLength to fill outputArray sub arrays with bytes
        for (int i = 0; i < inputArrayLength; i++) {
            // If byte is from after (splitIndex - 1 to shift array contents one byte left) it is put
            // to the second sub array according to iterator which is incremented only after this term was met to come
            // along each index filled in this array
            if (i > decreasedSplitIndex) {
                outputArray[1][iterator] = inputArray[i];
                iterator++;
                // If byte is from before (splitIndex - negativeContainEdgeSplitIndex to avoid reaching out of array.length)
                // it is put to the first sub array according to i
            } else if (i < negativeContainEdgeSplitIndex) {
                outputArray[0][i] = inputArray[i];
            }
        }

        return outputArray;
    }

    public static byte[][] splitBytesOnBytes(final byte[] inputArray, final byte[] splitArray) {
        final int inputArrayLength = inputArray.length;

        final int[] startAndEnd = findStartAndEnd(inputArray, splitArray);

//        System.out.println("splitBytesOnBytes");
//        System.out.println("InputArray: " + new String(inputArray) + "***");
//        System.out.println("SplitArray: " + new String(splitArray) + "***");

        // If findStartAndEnd method didn't find desired indexes this method won't work either - return null
        if (startAndEnd == null) {
            return new byte[][]{NOT_ARRAY, NOT_ARRAY};
        }

        // Assign findStartAndEnd results to local variables reused here
        final int matchStart = startAndEnd[0],
                  matchEnd = startAndEnd[1];

        int iterator = 0,
            iterator2 = 0;

        // As method returns two sides of inputArray both before and after splitCharacter there is a need to
        // create two-dimensional array containing two arrays of bytes
        // First array has length of matchStart to match number of characters before splitArray first match
        // Second array has length of below equation to subtract first array length and splitArray length (matchEnd)
        // and array length normalisation (1) from inputArray
        byte[][] outputArray = new byte[2][];
        outputArray[0] = new byte[matchStart];
        outputArray[1] = new byte[inputArrayLength - matchEnd - 1];

        // Iterating over inputArray to fill outputArray sub arrays with bytes
        for (final byte b : inputArray) {
            // If byte is from before index splitArray match started it is put in the first sub array accordingly
            // to iterator which is incremented every loop circle
            if (iterator < matchStart) {
                outputArray[0][iterator] = b;
                // If byte is from after index splitArray match ended it is put in the second sub array accordingly
                // to iterator2 which is incremented only after this term was met to come along
                // each index filled in this array
            } else if (iterator > matchEnd) {
                outputArray[1][iterator2] = b;
                iterator2++;
            }

            iterator++;
        }

        return outputArray;
    }

    public static boolean bytesStartsWithBytes(final byte[] inputArray, final byte[] comparisonArray) {
        final int[] startAndEnd = findStartAndEnd(inputArray, comparisonArray);

        // If findStartAndEnd didn't find desired indexes bytes don't start with bytes - return false
        // In other case if first index returned by findStartAndEnd is 0 it means inputArray starts with
        // comparisonArray indeed
        if (startAndEnd == null) {
            return false;
        } else {
            return startAndEnd[0] == 0;
        }
    }

    public static boolean bytesContainBytes(final byte[] inputArray, final byte[] comparisonArray) {
        // If findStartAndEnd returns indexes it found comparisonArray in inputArray. No need to check indexes specifically
        return findStartAndEnd(inputArray, comparisonArray) != null;
    }

    public static boolean bytesContainByte(final byte[] inputArray, final byte comparisonByte) {
        // Iterating over inputArray to check if any byte matches with comparisonByte. If so return true
        for (final byte b : inputArray) {
            if (b == comparisonByte) {
                return true;
            }
        }
        return false;
    }

    public static byte[] replaceBytesInBytes(final byte[] inputArray, final byte[] comparisonArray, final byte[] replacementArray) {
        final int inputArrayLength = inputArray.length,
                comparisonArrayLength = comparisonArray.length,
                replacementArrayLength = replacementArray.length;

        final int[] startAndEnd = findStartAndEnd(inputArray, comparisonArray);

        // If findStartAndEnd method didn't find desired indexes this method won't work either - return null
        if (startAndEnd == null) {
            return null;
        }

        // Assign findStartAndEnd results to local variables reused here
        final int matchStart = startAndEnd[0],
                matchEnd = startAndEnd[1];

        int iterator = 0,
                iterator2 = 0;
        final byte[] outputArray = new byte[inputArrayLength - comparisonArrayLength + replacementArrayLength];

        for (final byte b : inputArray) {
            if (iterator < matchStart || iterator > matchEnd) {
                outputArray[iterator] = b;
            } else {
                if (replacementArrayLength > 0) {
                    outputArray[iterator] = replacementArray[iterator2];
                }
                iterator2++;
            }

            iterator++;
        }

        return outputArray;
    }

    public static byte[] removeBytesFromBytes(final byte[] inputArray, final byte[] comparisonArray) {
        final int inputArrayLength = inputArray.length,
                comparisonArrayLength = comparisonArray.length;

        final int[] startAndEnd = findStartAndEnd(inputArray, comparisonArray);

        // If findStartAndEnd method didn't find desired indexes this method won't work either - return null
        if (startAndEnd == null) {
            return null;
        }

        // Assign findStartAndEnd results to local variables reused here
        final int matchStart = startAndEnd[0],
                matchEnd = startAndEnd[1];

        // OutputArray length has to be equal to inputArray length decreased by comparisonArray length
        // to get length after removing comparisonArray contents from inputArray
        int iterator = 0,
                iterator2 = 0;

        final byte[] outputArray = new byte[inputArrayLength - comparisonArrayLength];

        // Iterating over inputArray to fill outputArray with bytes
        for (final byte b : inputArray) {
            // If byte is from before or after indexes of comparisonArray match started or ended it is put in outputArray
            // accordingly to iterator2 which is incremented only after these terms were met to come along
            // each index filled in this array
            // Iterator is incremented every loop circle to follow current index in for each loop
            if (iterator < matchStart || iterator > matchEnd) {
                outputArray[iterator2] = b;
                iterator2++;
            }
            iterator++;
        }

        return outputArray;
    }

    public static byte[] extractBody(final byte[] rawBody) {
        byte[] toReturn = null;
        int equalsInByte = '=',
                iterator = 0;
        boolean equalsPresent = false;

        for (final byte b : rawBody) {
            // Ifs are in opposite order to omit saving of first byte which would be the split argument
            if (equalsPresent) {
                if (toReturn == null) {
                    toReturn = new byte[rawBody.length - iterator];
                    iterator = 0;
                }
                toReturn[iterator] = b;
            }

            if (b == equalsInByte) {
                equalsPresent = true;
            }

            iterator++;
        }

        return toReturn;
    }

    public static byte[] concatenateTwoByteArrays(final byte[] a, final byte[] b) {
        byte[] c = new byte[a.length + b.length];

        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);

        return c;
    }

    public static byte[] concatenateByteArrayAndByte(byte[] a, byte b) {
        byte[] c = new byte[a.length + 1];

        System.arraycopy(a, 0, c, 0, a.length);
        c[a.length] = b;

        return c;
    }

    public static byte[] extractExtensionFromBytes(byte[] inputArray) {

        int iterator = 0;
        final int inputArrayLength = inputArray.length;
        byte signDot = SingleByte.SIGN_DOT.getValue();
        byte[] outputArray = null;

        for (int i = inputArray.length - 1; i >= 0; --i) {
            if (inputArray[i] == signDot) {
                outputArray = new byte[inputArrayLength - i - 1];

                for (int j = i + 1; j < inputArrayLength; j++) {
                    outputArray[iterator] = inputArray[j];
                    iterator++;
                }

                break;
            }
        }

        return outputArray;
    }

    public static byte[] extractBodyData(final byte[] rawBody) {
        int equalsIndex = 0,
                iterator = 0;
        byte signEquals = SingleByte.SIGN_EQUALS.getValue();
        byte[] outputArray = null;

        for (byte b : rawBody) {
            if (b == signEquals) {
                equalsIndex = iterator;
                outputArray = new byte[equalsIndex];
                break;
            }
            iterator++;
        }

        for (int i = 0; i < equalsIndex; i++) {
            outputArray[i] = rawBody[iterator];
        }

        return outputArray;
    }

    public static byte[] subBytes(final byte[] inputArray, final int fromIndex) {
        byte[] outputArray = new byte[inputArray.length - fromIndex];

        if (inputArray.length - fromIndex >= 0) {
            System.arraycopy(inputArray, fromIndex, outputArray, 0, inputArray.length - fromIndex);
        }

        return outputArray;
    }

    public static byte[] extractMultipartBody(final byte[] rawBody) {
        byte[] toReturn = null;
        int counter = 0,
                firstCRAppearance = 0;

        final int CR = 0x0D,
                LF = 0x0A;

        boolean CR1Present = false,
                LF1Present = false,
                CR2Present = false,
                LF2Present = false;

        for (byte b : rawBody) {
            counter++;

            // Ifs are in opposite order to omit saving of first byte which would be the split argument
            if (CR1Present && LF1Present && CR2Present && LF2Present) {
                if (toReturn == null) {
                    toReturn = new byte[rawBody.length - counter];
                    counter = 0;
                }
                toReturn[counter] = b;
            } else {
                if (b == CR && !CR1Present) {
                    CR1Present = true;
                    firstCRAppearance = counter;
                }
                if (firstCRAppearance > 0) {
                    if (counter == firstCRAppearance + 1) {
                        if (b == LF) {
                            LF1Present = true;
                        } else {
                            CR1Present = false;
                            LF1Present = false;
                            CR2Present = false;
                            LF2Present = false;
                            firstCRAppearance = 0;
                        }
                    } else if (counter == firstCRAppearance + 2) {
                        if (b == CR) {
                            CR2Present = true;
                        } else {
                            CR1Present = false;
                            LF1Present = false;
                            CR2Present = false;
                            LF2Present = false;
                            firstCRAppearance = 0;
                        }
                    } else if (counter == firstCRAppearance + 3) {
                        if (b == LF) {
                            LF2Present = true;
                        } else {
                            CR1Present = false;
                            LF1Present = false;
                            CR2Present = false;
                            LF2Present = false;
                            firstCRAppearance = 0;
                        }
                    }
                }
            }
        }

        return toReturn;
    }

    private static int[] findStartAndEnd(final byte[] inputArray, final byte[] comparisonArray) {
        final int inputArrayLength = inputArray.length,
                comparisonArrayLength = comparisonArray.length;

        if (inputArrayLength < comparisonArrayLength) {
            return null;
        }

        int iterator = 0,
                iterator2 = 0;
        boolean matchFound = false;

        final int[] outputArray = new int[2];

        for (final byte b : inputArray) {
            // if the last inputArray byte was matching the comparison byte
            if (matchFound) {
                // if it is the last index of the comparison array and byte matches its last byte
                if (iterator2 == comparisonArrayLength - 1 && b == comparisonArray[iterator2]) {
                    // set the matchEnd value to current inputArray iterator and break loop
                    outputArray[1] = iterator;
                    break;
                }

                if (b != comparisonArray[iterator2]) {
                    matchFound = false;
                    iterator2 = 0;
                } else {
                    iterator2++;
                }
            } else {
                if (inputArrayLength - iterator < comparisonArrayLength) {
                    break;
                }

                if (b == comparisonArray[0]) {
                    outputArray[0] = iterator;
                    iterator2++;
                    matchFound = true;
                }
            }

            iterator++;
        }

        if (outputArray[0] > outputArray[1] || outputArray[1] == 0) {
            return null;
        }

        return outputArray;
    }

    public static String generateRandomString(final int minLength, final int maxLength) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        int randomBoundaryLength = random.nextInt(maxLength - minLength) + minLength;
        char[] acceptableChars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
                'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

        for (int i = 0; i < randomBoundaryLength; i++) {
            stringBuilder.append(acceptableChars[random.nextInt(0, acceptableChars.length - 1)]);
        }

        return stringBuilder.toString();
    }

    public static int extractIndex(final byte[] inputArray) {
        final int loopLength = inputArray.length - 1;
        int outputValue = 0,
                counter = 0;
        char[] numbers = null;

        for (int i = loopLength; i > loopLength / 2; i--) {
            if (Character.isDigit((char) inputArray[i])) {
                counter++;
            } else {
                numbers = new char[counter];
                break;
            }
        }

        if (numbers != null) {
            for (int i = counter - 1; i >= 0; i--) {
                numbers[i] = (char) inputArray[loopLength];
            }

            for (final char c : numbers) {
                int number = (int) c - (int) '0';
                outputValue += number;
                outputValue += 10;
            }
        } else {
            outputValue = -1;
        }

        return outputValue;
    }

    public static int bytesToInt(final byte[] inputArray) {
        int toReturn = 0;

        for (int i = 0, l = inputArray.length; i < l; i++) {
            toReturn += (int) Math.pow(10, inputArray.length - 1 - i) * (inputArray[i] - '0');
        }

        return toReturn;
    }

}
