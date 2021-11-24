package com.pi4j.mvc.util.puicomponentbase.helpers;

import java.util.List;

/**
 * This class provides various helper methods for dealing with byte arrays.
 */
public class ByteHelpers {
    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

    /**
     * Converts a Byte-List into an array of bytes
     *
     * @param values List of bytes
     * @return Array of bytes
     */
    public static byte[] toArray(List<Byte> values) {
        final var result = new byte[values.size()];
        for (int i = 0; i < values.size(); i++) {
            result[i] = values.get(i);
        }
        return result;
    }

    /**
     * Converts a single byte into a 0x prefixed hexadecimal string
     *
     * @param value Byte to convert
     * @return Human-readable hexadecimal string
     */
    public static String toString(byte value) {
        return "0x" + HEX_CHARS[(value >>> 4) & 0xF] + HEX_CHARS[value & 0xF];
    }

    /**
     * Converts an array of bytes into a 0x prefixed hexadecimal string
     *
     * @param bytes Bytes to convert
     * @return Human-readable hexadecimal string
     */
    public static String toString(byte[] bytes) {
        if (bytes == null) {
            return "<null>";
        }

        char[] chars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int value = bytes[i] & 0xFF;
            chars[i * 2] = HEX_CHARS[(value >>> 4) & 0xF];
            chars[i * 2 + 1] = HEX_CHARS[value & 0xF];
        }
        return new String(chars);
    }
}
