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

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.apache.shiro.realm.ldap.LdapUtils;
import org.apache.zeppelin.realm.LdapRealm;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

/**
 * HashedCredentialLdapRealm integrate to ldap custom hash.
 */
public class HashedCredentialLdapRealm extends LdapRealm {

  @Override
  protected AuthenticationInfo queryForAuthenticationInfo(AuthenticationToken token,
                                                          LdapContextFactory ldapContextFactory)
      throws NamingException {

    Object principal = token.getPrincipal();
    Object credentials = token.getCredentials();
    Md5PasswordEncoder encoder = new Md5PasswordEncoder();
    encoder.setEncodeHashAsBase64(true);
    credentials = encoder.encodePassword(new String((char[]) (credentials)), null);
    principal = getLdapPrincipal(token);
    UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
    usernamePasswordToken.setPassword(((String) credentials).toCharArray());
    LdapContext ctx = null;
    try {
      ctx = ldapContextFactory.getLdapContext(principal, credentials);
      return createAuthenticationInfo(token, principal, credentials, ctx);
    } finally {
      LdapUtils.closeContext(ctx);
    }
  }


}
