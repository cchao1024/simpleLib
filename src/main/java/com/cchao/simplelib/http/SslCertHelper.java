package com.cchao.simplelib.http;

import android.os.Build;

import com.cchao.simplelib.core.Logs;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * @author cchao
 * @version 2019-05-07.
 */
public class SslCertHelper {
    /**
     * 信任所有证书
     */
    public static OkHttpClient.Builder enableTrustAllCert(OkHttpClient.Builder builder) {
        try {

            builder.sslSocketFactory(new SSL(TRUST_ALL_CERT), TRUST_ALL_CERT)
                .hostnameVerifier(new TrustAllHostnameVerifier());

        } catch (Exception exc) {
            Logs.logException("Error while enableTrustAllCert" + exc.getMessage());
        }

        return builder;
    }

    /**
     * 定义一个信任所有证书的TrustManager
     */
    static final X509TrustManager TRUST_ALL_CERT = new X509TrustManager() {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }
    };

    private static class TrustAllHostnameVerifier implements HostnameVerifier {

        @SuppressWarnings("TrustAllX509TrustManager")
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public static class SSL extends SSLSocketFactory {
        private SSLSocketFactory mDefaultFactory;
        // Android 5.0+ (API level21) provides reasonable default settings
        // but it still allows SSLv3
        // https://developer.android.com/about/versions/android-5.0-changes.html#ssl
        static String[] protocols = null;
        static String[] cipherSuites = null;

        static {
            try {
                SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket();
                if (socket != null) {
                    /* set reasonable protocol versions */
                    // - enable all supported protocols (enables TLSv1.1 and TLSv1.2 on Android <5.0)
                    // - remove all SSL versions (especially SSLv3) because they're insecure now
                    List<String> protocols = new LinkedList<>();
                    for (String protocol : socket.getSupportedProtocols()) {
                        if (!protocol.toUpperCase(Locale.getDefault()).contains("SSL")) {
                            protocols.add(protocol);
                        }
                    }
                    SSL.protocols = protocols.toArray(new String[protocols.size()]);
                    /* set up reasonable cipher suites */
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        // choose known secure cipher suites
                        List<String> allowedCiphers = Arrays.asList(
                            // TLS 1.2
                            "TLS_RSA_WITH_AES_256_GCM_SHA384",
                            "TLS_RSA_WITH_AES_128_GCM_SHA256",
                            "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256",
                            "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
                            "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384",
                            "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",
                            "TLS_ECHDE_RSA_WITH_AES_128_GCM_SHA256",
                            // maximum interoperability
                            "TLS_RSA_WITH_3DES_EDE_CBC_SHA",
                            "TLS_RSA_WITH_AES_128_CBC_SHA",
                            // additionally
                            "TLS_RSA_WITH_AES_256_CBC_SHA",
                            "TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA",
                            "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
                            "TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA",
                            "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA");
                        List<String> availableCiphers = Arrays.asList(socket.getSupportedCipherSuites());
                        // take all allowed ciphers that are available and put them into preferredCiphers
                        HashSet<String> preferredCiphers = new HashSet<>(allowedCiphers);
                        preferredCiphers.retainAll(availableCiphers);
                        /* For maximum security, preferredCiphers should *replace* enabled ciphers (thus disabling
                         * ciphers which are enabled by default, but have become unsecure), but I guess for
                         * the security level of DAVdroid and maximum compatibility, disabling of insecure
                         * ciphers should be a server-side task */
                        // add preferred ciphers to enabled ciphers
                        HashSet<String> enabledCiphers = preferredCiphers;
                        enabledCiphers.addAll(new HashSet<>(Arrays.asList(socket.getEnabledCipherSuites())));
                        SSL.cipherSuites = enabledCiphers.toArray(new String[enabledCiphers.size()]);
                    }
                }
            } catch (IOException e) {
                Logs.logException(e);
                throw new RuntimeException(e);
            }
        }

        public SSL(X509TrustManager tm) {
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, (tm != null) ? new X509TrustManager[]{tm} : null, null);
                mDefaultFactory = sslContext.getSocketFactory();
            } catch (GeneralSecurityException e) {
                Logs.logException(e);
            }
        }

        private void upgradeTLS(SSLSocket ssl) {
            // Android 5.0+ (API level21) provides reasonable default settings
            // but it still allows SSLv3
            // https://developer.android.com/about/versions/android-5.0-changes.html#ssl
            try {
                if (protocols != null) {
                    ssl.setEnabledProtocols(protocols);
                }
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && cipherSuites != null) {
                    ssl.setEnabledCipherSuites(cipherSuites);
                }
            } catch (Exception e) {
                try {
                    //android 5.0以下某些机型不支持 TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA
                    //因此做移除操作
                    List<String> availableCiphers = Arrays.asList(cipherSuites);
                    HashSet<String> preferredCiphers = new HashSet<>(availableCiphers);
                    preferredCiphers.remove("TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA");
                    SSL.cipherSuites = preferredCiphers.toArray(new String[preferredCiphers.size()]);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && cipherSuites != null) {
                        ssl.setEnabledCipherSuites(cipherSuites);
                    }
                } catch (Exception e2) {
                    Logs.logException(e2);
                }
                Logs.logException(e);
            }
        }

        @Override
        public String[] getDefaultCipherSuites() {
            return cipherSuites;
        }

        @Override
        public String[] getSupportedCipherSuites() {
            return cipherSuites;
        }

        @Override
        public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
            Socket ssl = mDefaultFactory.createSocket(s, host, port, autoClose);
            if (ssl instanceof SSLSocket) {
                upgradeTLS((SSLSocket) ssl);
            }
            return ssl;
        }

        @Override
        public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
            Socket ssl = mDefaultFactory.createSocket(host, port);
            if (ssl instanceof SSLSocket) {
                upgradeTLS((SSLSocket) ssl);
            }
            return ssl;
        }

        @Override
        public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
            Socket ssl = mDefaultFactory.createSocket(host, port, localHost, localPort);
            if (ssl instanceof SSLSocket) {
                upgradeTLS((SSLSocket) ssl);
            }
            return ssl;
        }

        @Override
        public Socket createSocket(InetAddress host, int port) throws IOException {
            Socket ssl = mDefaultFactory.createSocket(host, port);
            if (ssl instanceof SSLSocket) {
                upgradeTLS((SSLSocket) ssl);
            }
            return ssl;
        }

        @Override
        public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
            Socket ssl = mDefaultFactory.createSocket(address, port, localAddress, localPort);
            if (ssl instanceof SSLSocket) {
                upgradeTLS((SSLSocket) ssl);
            }
            return ssl;
        }
    }
}
