package org.apache.zeppelin.extend.reaml;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.apache.shiro.realm.ldap.LdapUtils;
import org.apache.zeppelin.conf.ZeppelinConfiguration;
import org.apache.zeppelin.realm.LdapRealm;

public class HashedCredentialLdapRealm extends LdapRealm {

  @Override
  protected AuthenticationInfo queryForAuthenticationInfo(AuthenticationToken token,
      LdapContextFactory ldapContextFactory)
      throws NamingException {

    Object principal = token.getPrincipal();
    Object credentials = token.getCredentials();
    Md5PasswordEncoder encoder = new Md5PasswordEncoder();
    encoder.setEncodeHashAsBase64(true);
    credentials = encoder.encodePassword(credentials.toString(),null);
    principal = getLdapPrincipal(token);
    LdapContext ctx = null;
    try {
      ctx = ldapContextFactory.getLdapContext(principal, credentials);
      //context was opened successfully, which means their credentials were valid.  Return the AuthenticationInfo:
      return createAuthenticationInfo(token, principal, credentials, ctx);
    } finally {
      LdapUtils.closeContext(ctx);
    }
  }


}
