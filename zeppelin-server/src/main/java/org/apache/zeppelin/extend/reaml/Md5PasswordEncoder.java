package org.apache.zeppelin.extend.reaml;

public class Md5PasswordEncoder extends MessageDigestPasswordEncoder {

  public Md5PasswordEncoder() {
    super("MD5");
  }
}