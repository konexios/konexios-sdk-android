/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

package com.arrow.acn.api.mqtt.common;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import org.spongycastle.crypto.examples.JPAKEExample;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.openssl.PEMKeyPair;
import org.spongycastle.openssl.PEMParser;
import org.spongycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by osminin on 6/21/2016.
 */

public final class SslUtil {
    private static final String TAG = SslUtil.class.getName();

    @NonNull
    public static SSLSocketFactory getSocketFactory(@NonNull final String caCertContent, @NonNull final String certContent,
                                                    @NonNull final String keyContent) throws Exception {
        FirebaseCrash.logcat(Log.DEBUG, TAG, "getSocketFactory");
        Security.addProvider(new BouncyCastleProvider());

        // load CA certificate
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate caCert = cf.generateCertificate(new ByteArrayInputStream(caCertContent.getBytes()));


        // load client certificate
        Certificate cert = cf.generateCertificate(new ByteArrayInputStream(certContent.getBytes()));

        // load client private key
        PEMParser parser = new PEMParser(new StringReader(keyContent));
        JPAKEExample jpakeExample;
        PEMKeyPair pemKeyPair = (PEMKeyPair) parser.readObject();
        JcaPEMKeyConverter keyConverter = new JcaPEMKeyConverter();
        KeyPair key = keyConverter.getKeyPair(pemKeyPair);
        parser.close();

        // CA certificate is used to authenticate server
        KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
        caKs.load(null, null);
        caKs.setCertificateEntry("ca-certificate", caCert);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(caKs);

        // client key and certificates are sent to server so it can authenticate
        // us
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        ks.setCertificateEntry("certificate", cert);
        ks.setKeyEntry("private-key", key.getPrivate(), new char[0], new java.security.cert.Certificate[] { cert });
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, new char[0]);

        // finally, create SSL socket factory
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        SSLSocketFactory noSSLv3Factory = new NoSSLv3SocketFactory(context.getSocketFactory());
        HttpsURLConnection.setDefaultSSLSocketFactory(noSSLv3Factory);

        return noSSLv3Factory;
    }
}
