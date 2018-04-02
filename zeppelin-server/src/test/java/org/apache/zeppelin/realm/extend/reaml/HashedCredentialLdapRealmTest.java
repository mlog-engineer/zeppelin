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

package org.apache.zeppelin.realm.extend.reaml;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniFactorySupport;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.junit.Test;


/**
 * HashedCredentialLdapRealmTest.
 */
public class HashedCredentialLdapRealmTest {

    @Test
    public void testLogInLdap() {
        IniFactorySupport<org.apache.shiro.mgt.SecurityManager> factory =
                new IniSecurityManagerFactory("classpath:shiro.ini");
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken();
        token.setUsername("ldaptest3");
        token.setPassword("123456".toCharArray());
        Subject loginUser =  factory.getInstance().login(currentUser,token);
        System.out.println(loginUser.getPrincipal());
        System.out.println("aa");

    }

    @Test
    public void testLogInSimple() {
        IniFactorySupport<org.apache.shiro.mgt.SecurityManager> factory =
                new IniSecurityManagerFactory("classpath:org/apache/zeppelin/realm/extend/reaml/shiro.ini");
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken();
        token.setUsername("admin");
        token.setPassword("password".toCharArray());
        Subject loginUser =  factory.getInstance().login(currentUser,token);
        System.out.println(loginUser.getPrincipal());
    }
}
