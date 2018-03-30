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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MessageDigestPasswordEncoder.
 */
public class MessageDigestPasswordEncoder extends BaseDigestPasswordEncoder {

  private final String algorithm;
  private int iterations = 1;

  /**
   * The digest algorithm to use Supports the named
   * <a href="http://java.sun.com/j2se/1.4.2/docs/guide/security/CryptoSpec.html#AppA">
   * Message Digest Algorithms</a> in the Java environment.
   */
  public MessageDigestPasswordEncoder(String algorithm) {
    this(algorithm, false);
  }

  /**
   * Convenience constructor for specifying the algorithm and whether or not to enable base64
   * encoding
   *
   * @throws IllegalArgumentException if an unknown
   */
  public MessageDigestPasswordEncoder(String algorithm, boolean encodeHashAsBase64)
      throws IllegalArgumentException {
    this.algorithm = algorithm;
    setEncodeHashAsBase64(encodeHashAsBase64);
    // Validity Check
    getMessageDigest();
  }

  /**
   * Encodes the rawPass using a MessageDigest. If a salt is specified it will be merged with the
   * password before encoding.
   *
   * @param rawPass The plain text password
   * @param salt The salt to sprinkle
   * @return Hex string of password digest (or base64 encoded string if encodeHashAsBase64 is
   * enabled.
   */
  public String encodePassword(String rawPass, Object salt) {
    String saltedPass = mergePasswordAndSalt(rawPass, salt, false);

    MessageDigest messageDigest = getMessageDigest();

    byte[] digest = messageDigest.digest(Utf8.encode(saltedPass));

    // "stretch" the encoded value if configured to do so
    for (int i = 1; i < this.iterations; i++) {
      digest = messageDigest.digest(digest);
    }

    if (getEncodeHashAsBase64()) {
      return Utf8.decode(Base64.encode(digest));
    } else {
      return new String(Hex.encode(digest));
    }
  }

  /**
   * Get a MessageDigest instance for the given algorithm. Throws an IllegalArgumentException if
   * <i>algorithm</i> is unknown
   *
   * @return MessageDigest instance
   * @throws IllegalArgumentException if NoSuchAlgorithmException is thrown
   */
  protected final MessageDigest getMessageDigest() throws IllegalArgumentException {
    try {
      return MessageDigest.getInstance(this.algorithm);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalArgumentException(
          "No such algorithm [" + this.algorithm + "]");
    }
  }

  /**
   * Takes a previously encoded password and compares it with a rawpassword after mixing in the salt
   * and encoding that value
   *
   * @param encPass previously encoded password
   * @param rawPass plain text password
   * @param salt salt to mix into password
   * @return true or false
   */
  public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
    String pass1 = "" + encPass;
    String pass2 = encodePassword(rawPass, salt);

    return PasswordEncoderUtils.equals(pass1, pass2);
  }

  public String getAlgorithm() {
    return this.algorithm;
  }

  /**
   * Sets the number of iterations for which the calculated hash value should be "stretched". If
   * this is greater than one, the initial digest is calculated, the digest function will be called
   * repeatedly on the result for the additional number of iterations.
   *
   * @param iterations the number of iterations which will be executed on the hashed password/salt
   * value. Defaults to 1.
   */
  public void setIterations(int iterations) {
    if (iterations <= 0) {
      throw new RuntimeException("Iterations value must be greater than zero");
    }
    this.iterations = iterations;
  }
}
