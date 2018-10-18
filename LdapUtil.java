package org.jeecgframework.web.system.controller.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by admPPMSd on 10/17/2018.
 */
public class LdapUtil {
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, FileNotFoundException, IOException, KeyManagementException {
        String[] IP = new String[] {"10.87.1.83", "10.153.3.83"};
        InetAddress inetAddress = null;
        inetAddress=InetAddress.getByName(IP[0].toString().trim());
        System.out.println(IP[0].toString().trim() + ":::" + inetAddress.getHostName());
        InetAddress inetAddress2 = null;
        inetAddress2=InetAddress.getByName(IP[1].toString().trim());
        System.out.println(IP[1].toString().trim() + ":::" + inetAddress2.getHostName());

        System.setProperty("javax.net.debug", "all");
        System.setProperty("com.sun.jndi.ldap.object.disableEndpointIdentification", "true");

        TrustManagerFactory tf = null;

        KeyManagerFactory kf = KeyManagerFactory.getInstance("SunX509");
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("F:\\ProgramFiles\\jdk1.8.0_181\\jre\\lib\\security\\SPCert.jks"), "NARI_$123".toCharArray());
        kf.init(ks, "NARI_$123".toCharArray());
        tf = TrustManagerFactory.getInstance("SunX509");
        tf.init(ks);

        SSLContext sc = SSLContext.getInstance("TLSv1.2");

        sc.init(kf.getKeyManagers(), tf.getTrustManagers(), SecureRandom.getInstanceStrong());

        SSLContext.setDefault(sc);

        String URL = "ldaps://singaporepower.local:636/dc=singaporepower,dc=local";
        String URL2 = "ldaps://sp-is-ad03.singaporepower.local:636/dc=singaporepower,dc=local";
        String FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
        LdapContext ctx = null;

        Hashtable<String, String> env = new Hashtable<String, String>();

        env.put(Context.INITIAL_CONTEXT_FACTORY, FACTORY);
        env.put(Context.PROVIDER_URL, URL2);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "Sap\\ldapPPMSd"); // login ldap account
        env.put(Context.SECURITY_CREDENTIALS, "Welcome@19"); // password
//        env.put("com.sun.jndi.connection.timecout","10000000");
//        env.put(Context.SECURITY_PROTOCOL,"ssl");
//        env.put(Context.REFERRAL,"ignore");
//        env.put("java.naming.ldap.factory.socket","org.jeecgframework.web.system.controller.core.MySSLSocketFactory"); //package+className

        try {
            ctx = new InitialLdapContext(env, null);

            System.out.println( "LDAPS connection... success." );
        } catch (javax.naming.AuthenticationException e) {
            System.out.println("LDAPS connect... failure.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("LDAPS connect...error.");
            e.printStackTrace();
        }
    }
}
