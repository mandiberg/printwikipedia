package info.bliki.api;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class TestUser extends User {
    public TestUser(String username, String password, String mediawikiApiUrl) {
        super(username, password, mediawikiApiUrl, "");
        connector = new TestConnector();
    }

    public static class TestConnector extends Connector {
        public TestConnector() {
            super(DEFAULT_HTTPCLIENT_BUILDER
                .setSSLSocketFactory(sslFactory())
                .disableContentCompression());
        }

        private static SSLConnectionSocketFactory sslFactory() {
            try {
                SSLContextBuilder sslBuilder = new SSLContextBuilder();
                sslBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
                return new SSLConnectionSocketFactory(sslBuilder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException ignored) {
                return null;
            }
        }
    }
}
