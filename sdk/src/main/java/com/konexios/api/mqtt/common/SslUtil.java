/*
 * Copyright (c) 2017-2019 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *     Konexios, Inc.
 */

package com.konexios.api.mqtt.common;

import androidx.annotation.NonNull;

import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.openssl.PEMKeyPair;
import org.spongycastle.openssl.PEMParser;
import org.spongycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import timber.log.Timber;

/**
 * Created by osminin on 6/21/2016.
 */

public final class SslUtil {

    @NonNull
    public static SSLSocketFactory getSocketFactory(@NonNull final String caCertContent, @NonNull final String certContent,
                                                    @NonNull final String keyContent) throws Exception {
        Timber.d("getSocketFactory");
        Security.addProvider(new BouncyCastleProvider());

        // load CA certificate
        Certificate caCert = loadCertificate(caCertContent);

        // load client certificate
        Certificate clientCertificate = loadCertificate(certContent);

        // load client private key
        KeyPair clientPrivateKey = loadKeys(keyContent);

        // CA certificate is used to authenticate server
        TrustManagerFactory tmf = getTrustManagerFactory(caCert);

        // client key and certificates are sent to server so it can authenticate us
        KeyManagerFactory kmf = getKeyManagerFactory(clientCertificate, clientPrivateKey);

        // finally, create SSL socket factory
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        SSLSocketFactory noSSLv3Factory = new NoSSLv3SocketFactory(context.getSocketFactory());
        HttpsURLConnection.setDefaultSSLSocketFactory(noSSLv3Factory);

        return noSSLv3Factory;
    }

    public static Certificate loadCertificate(String certContent) throws CertificateException {
        Timber.v("loadCertificate: ");
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return cf.generateCertificate(new ByteArrayInputStream(certContent.getBytes()));
    }

    public static KeyPair loadKeys(String keyContent) throws IOException {
        Timber.v("loadKeys: ");
        PEMParser parser = new PEMParser(new StringReader(keyContent));
        PEMKeyPair pemKeyPair = (PEMKeyPair) parser.readObject();
        JcaPEMKeyConverter keyConverter = new JcaPEMKeyConverter();
        KeyPair key = keyConverter.getKeyPair(pemKeyPair);
        parser.close();
        return key;
    }

    public static TrustManagerFactory getTrustManagerFactory(Certificate caCert) throws KeyStoreException,
            CertificateException, NoSuchAlgorithmException, IOException {
        Timber.v("getTrustManagerFactory: ");
        KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
        caKs.load(null, null);
        caKs.setCertificateEntry("ca-certificate", caCert);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(caKs);
        return tmf;
    }

    public static KeyManagerFactory getKeyManagerFactory(Certificate clientCertificate, KeyPair clientPrivateKey)
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException,
            IOException, UnrecoverableKeyException {
        Timber.v("getKeyManagerFactory: ");
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        ks.setCertificateEntry("certificate", clientCertificate);
        ks.setKeyEntry("private-key", clientPrivateKey.getPrivate(), new char[0], new java.security.cert.Certificate[]{clientCertificate});
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, new char[0]);
        return kmf;
    }
}
