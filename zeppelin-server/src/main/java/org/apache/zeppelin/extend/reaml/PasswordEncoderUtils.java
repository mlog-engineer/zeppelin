package org.apache.zeppelin.extend.reaml;

public class PasswordEncoderUtils {

  private PasswordEncoderUtils() {
  }

  /**
   * Constant time comparison to prevent against timing attacks.
   */
  static boolean equals(String expected, String actual) {
    byte[] expectedBytes = bytesUtf8(expected);
    byte[] actualBytes = bytesUtf8(actual);
    int expectedLength = expectedBytes == null ? -1 : expectedBytes.length;
    int actualLength = actualBytes == null ? -1 : actualBytes.length;

    int result = expectedLength == actualLength ? 0 : 1;
    for (int i = 0; i < actualLength; i++) {
      byte expectedByte = expectedLength <= 0 ? 0 : expectedBytes[i % expectedLength];
      byte actualByte = actualBytes[i % actualLength];
      result |= expectedByte ^ actualByte;
    }
    return result == 0;
  }

  private static byte[] bytesUtf8(String s) {
    if (s == null) {
      return null;
    }

    return Utf8.encode(
        s); // need to check if Utf8.encode() runs in constant time (probably not). This may leak length of string.
  }
}
