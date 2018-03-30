/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.zeppelin.extend.reaml;

/**
 * PasswordEncoderUtils.
 */
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
        s);
    // need to check if Utf8.encode()
    // runs in constant time (probably not). This may leak length of string.
  }
}
