package com.joselopezrosario.androidfm;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public final class Fm {
    private static final String GET = "GET";
    private static final String BASIC = "Basic";
    private static final String BEARER = "Bearer";
    public static FmResponse execute(FmRequest fmRequest) {
        return processExecute(fmRequest);
    }

    /**
     * processExecute
     * Connect to the FileMaker Server, send the request, and retrieve the result
     *
     * @param request an FmRequest object
     * @return an FmResponse object
     */
    private static FmResponse processExecute(FmRequest request) {
        if ( !request.isOk()){
            return null;
        }
        String url = request.getEndpoint();
        String method = request.getMethod();
        String body = request.getBody();
        String auth = getAuthString(request);
        if ( request.isSSLDisabled()){
            disableSSL();
        }
        FmResponse response = new FmResponse();
        HttpsURLConnection urlConnection;
        BufferedReader reader = null;
        urlConnection = buildUrlConnection(
                url,
                method,
                auth,
                body
        );
        if (urlConnection == null) {
            return null;
        }
        try {
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return response;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (buffer.length() == 0) {
                return response;
            }
            response.setHttpCode(urlConnection.getResponseCode());
            response.setHttpMessage(buffer.toString());
        } catch (IOException v) {
            response.setHttpMessage(v.toString());
            return response;
        } finally {
            urlConnection.disconnect();
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String x = response.getHttpMessage();
        try {
            JSONObject fmResponse = new JSONObject(x).getJSONObject("response");
            JSONArray fmMessages = new JSONObject(x).getJSONArray("messages");
            response.setFmResponse(fmResponse);
            response.setFmMessageArray(fmMessages);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * buildUrlConnection
     *
     * @param urlString  the url string
     * @param method     the http method
     * @param authString the authorization string
     * @param body       the request's body
     * @return an HttpsUrlConnection object
     */
    private static HttpsURLConnection buildUrlConnection(String urlString, String method, String authString, String body) {
        HttpsURLConnection urlConnection;
        URL url;
        Uri builtUri = Uri.parse(urlString).buildUpon().build();
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod(method);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Authorization", authString);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (method.equals(GET)) {
            return urlConnection;
        }
        urlConnection.setDoOutput(true);
        byte[] data = body.getBytes(StandardCharsets.UTF_8);
        try (DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream())) {
            wr.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return urlConnection;
    }

    /**
     * getAuthString
     *
     * @param request an FmRequest object
     * @return for Basic Auth, "Basic " + the Base64 encoded credentials; for Bearer Auth, "Bearer " + token.
     */
    private static String getAuthString(FmRequest request) {
        switch (request.getAuth()) {
            case BASIC:
                return BASIC + " " + encodeFileMakerCredentials(request.getAccountName(), request.getPassword());
            case BEARER:
                return BEARER + " " + request.getToken();
            default:
                return null;
        }
    }

    /**
     * encodeFileMakerCredentials
     * A utility to encode to Base64 the FileMaker account name and password
     *
     * @param accountName the FileMaker account with fmrest privileges
     * @param password    the FileMaker account's password
     * @return the Base64 encoded credentials
     */
    private static String encodeFileMakerCredentials(String accountName, String password) {
        if (accountName == null || password == null) {
            return null;
        }
        String credentials = accountName + ":" + password;
        String encodedCredentials;
        byte[] credentialBytes;
        try {
            credentialBytes = credentials.getBytes("UTF-8");
            encodedCredentials = Base64.encodeToString(credentialBytes, Base64.DEFAULT).trim();
            return encodedCredentials;
        } catch (UnsupportedEncodingException e) {
            System.out.print("Could not encode credentials: " + e.toString());
            return null;
        }
    }


    /**
     * disableSSL
     * <p>
     * A utility method to disable SSL for testing with the FileMaker Default SSL Certificate
     */
    private static void disableSSL() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @SuppressLint("TrustAllX509TrustManager")
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

}
