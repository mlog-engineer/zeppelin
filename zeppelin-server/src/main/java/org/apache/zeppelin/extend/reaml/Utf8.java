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

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

/**
 * utf8
 */
public class Utf8 {

  private static final Charset CHARSET = Charset.forName("UTF-8");

  /**
   * Get the bytes of the String in UTF-8 encoded form.
   */
  public static byte[] encode(CharSequence string) {
    try {
      ByteBuffer bytes = CHARSET.newEncoder().encode(CharBuffer.wrap(string));
      byte[] bytesCopy = new byte[bytes.limit()];
      System.arraycopy(bytes.array(), 0, bytesCopy, 0, bytes.limit());

      return bytesCopy;
    } catch (CharacterCodingException e) {
      throw new IllegalArgumentException("Encoding failed", e);
    }
  }

  /**
   * Decode the bytes in UTF-8 form into a String.
   */
  public static String decode(byte[] bytes) {
    try {
      return CHARSET.newDecoder().decode(ByteBuffer.wrap(bytes)).toString();
    } catch (CharacterCodingException e) {
      throw new IllegalArgumentException("Decoding failed", e);
    }
  }
}
