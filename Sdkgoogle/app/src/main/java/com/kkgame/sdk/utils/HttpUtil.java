package com.kkgame.sdk.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
    /**
     * 根据地址post访问网络通过指定编码获取字符串
     *
     * @param urlStr
     *            地址
     * @param params
     *            参数
     * @param charSet
     *            编码格式
     * @return
     * @throws Exception
     */
    public static String post(String urlStr, HashMap<String, String> params,
            String charSet) throws Exception {
        byte[] result = post(urlStr, params);
        return new String(result, charSet);
    }

    /**
     * 根据地址post访问网络通过指定编码获取字符串
     *
     * @param urlStr
     *            地址
     * @param params
     *            参数
     * @param charSet
     *            编码格式
     * @return
     * @throws Exception
     */
    public static String post(String urlStr, String params, String charSet)
            throws Exception {
        byte[] result = post(urlStr, params);
        return new String(result, charSet);
    }

    /**
     * 通过post获取字节数组
     *
     * @param urlStr
     * @param params
     * @return
     * @throws Exception
     */
    public static byte[] post(String urlStr, HashMap<String, String> params)
            throws Exception {
        URL url = new URL(urlStr);
        byte[] data = null;
        // if (urlStr.contains("https://")) {

        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);

        SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        HttpParams params1 = new BasicHttpParams();
        HttpProtocolParams.setVersion(params1, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params1, HTTP.UTF_8);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("lidroid", PlainSocketFactory
                .getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params1,
                registry);

        DefaultHttpClient httpClient = new DefaultHttpClient(ccm, params1);
        // 请求超时
        httpClient.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, 10 * 1000);
        // 读取超时
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                30 * 1000);
        httpClient.setHttpRequestRetryHandler(requestRetryHandler);
        HttpPost httpPost = new HttpPost(urlStr);
        LinkedList<BasicNameValuePair> paramslist = new LinkedList<BasicNameValuePair>();
        Set<Entry<String, String>> entrySet = params.entrySet();
        for (Entry<String, String> entry : entrySet) {
            paramslist.add(new BasicNameValuePair(entry.getKey(), entry
                    .getValue()));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(paramslist, "UTF-8"));

        HttpResponse response = httpClient.execute(httpPost);

        String string = EntityUtils.toString(response.getEntity(), "UTF-8");
        return string.getBytes();
        // } else {
        // HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //
        // conn.setRequestMethod("POST");
        // conn.setReadTimeout(15000);
        // conn.setConnectTimeout(5000);
        // conn.setDoInput(true);
        // conn.setDoOutput(true);
        // // Content-Length 33
        // // Content-Type application/x-www-form-urlencoded
        // byte[] content = getContent(params);
        // // System.out.println(content.length);
        // conn.setRequestProperty("Content-Type",
        // "application/x-www-form-urlencoded");
        // conn.setRequestProperty("Content-Length", content.length + "");
        // OutputStream out = conn.getOutputStream();
        // out.write(content);
        // out.flush();
        // out.close();
        // if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
        // InputStream input = conn.getInputStream();
        // data = StreamUtil.readInputStream(input);
        // }
        // }

        // return data;
    }

    /**
     * 通过post获取字节数组
     *
     * @param urlStr
     * @param params
     * @return
     * @throws Exception
     */
    public static byte[] post(String urlStr, String params) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setConnectTimeout(10000);
        conn.setReadTimeout(30000);

        conn.setRequestMethod("POST");

        conn.setDoInput(true);
        conn.setDoOutput(true);
        // Content-Length 33
        // Content-Type application/x-www-form-urlencoded\
        byte[] content = new byte[0];
        if (params != null && !"".equals(params)) {
            content = params.getBytes();
        }
        // System.out.println(content.length);
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", content.length + "");
        OutputStream out = conn.getOutputStream();
        out.write(content);
        out.flush();
        out.close();
        byte[] data = null;
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream input = conn.getInputStream();
            data = StreamUtil.readInputStream(input);
        }
        return data;
    }

    /**
     * 根据参数,获取到参数字符串的字节数组
     *
     * @param params
     * @return
     * @throws Exception
     */
    private static byte[] getContent(HashMap<String, String> params)
            throws Exception {
        Set<Entry<String, String>> set = params.entrySet();
        StringBuilder builder = new StringBuilder();
        for (Entry<String, String> item : set) {
            builder.append(item.getKey()).append("=")
                    .append(URLEncoder.encode(item.getValue(), "UTF-8"))
                    .append("&");
        }
        String content = "";
        if (builder.length() > 0) {
            content = builder.substring(0, builder.length() - 1);
        }
        // System.out.println(content);
        return content.getBytes();
    }

    /**
     * get请求获取字节数组
     *
     * @param urlStr
     * @return
     * @throws Exception
     */
    public static byte[] get(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        byte[] data = null;
        if (conn.getResponseCode() == 200) {
            InputStream input = conn.getInputStream();
            data = StreamUtil.readInputStream(input);
        }
        return data;
    }

    /**
     * get获取输入流
     *
     * @param urlStr
     * @return
     * @throws Exception
     */
    public static InputStream getInputStream(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        InputStream input = null;
        if (conn.getResponseCode() == 200) {
            input = conn.getInputStream();

        }
        return input;
    }

    /**
     * get获取字符串
     *
     * @param urlStr
     * @param charset
     * @return
     * @throws Exception
     * @throws ProtocolException
     * @throws IOException
     */
    public static String get(String urlStr, String charset) throws Exception,
            ProtocolException, IOException {
        return new String(get(urlStr), charset);
    }

    /**
     * 服务器给的地址是重定向地址,如果需要真实的下载地址,需要解析响应头,获取到真实地址
     *
     * @param url
     */
    @SuppressWarnings("unused")
    public static String getRedirectInfo(String url) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext httpContext = new BasicHttpContext();
        HttpGet httpGet = new HttpGet(url);
        String result = null;
        try {
            // 将HttpContext对象作为参数传给execute()方法,则HttpClient会把请求响应交互过程中的状态信息存储在HttpContext中
            HttpResponse response = httpClient.execute(httpGet, httpContext);
            // 获取重定向之后的主机地址信息,即"http://127.0.0.1:8088"
            HttpHost targetHost = (HttpHost) httpContext
                    .getAttribute(ExecutionContext.HTTP_TARGET_HOST);
            // 获取实际的请求对象的URI,即重定向之后的"/blog/admin/login.jsp"
            HttpUriRequest realRequest = (HttpUriRequest) httpContext
                    .getAttribute(ExecutionContext.HTTP_REQUEST);
            // System.out.println("主机地址:" + targetHost);
            // System.out.println("URI信息:" + realRequest.getURI());
            result = targetHost.toString() + realRequest.getURI();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return result;
    }

    /**
     * 获取服务端文件大小
     *
     * @param urlStr
     *            文件的网络地址
     * @return
     * @throws Exception
     */
    public static long getContentLength(String urlStr) throws Exception {
        int mContentLength = 0;
        // 创建地址对象
        URL url = new URL(urlStr);
        // 获取链接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置请求参数
        conn.setRequestMethod("GET");
        conn.setReadTimeout(30 * 1000);
        if (conn.getResponseCode() == 200) {
            mContentLength = conn.getContentLength();

        }
        return mContentLength;
    }

    /**
     * 设置重连机制和异常自动恢复处理
     */
    private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
        // 自定义的恢复策略
        public boolean retryRequest(IOException exception, int executionCount,
                HttpContext context) {
            // 设置恢复策略，在Http请求发生异常时候将自动重试3次
            if (executionCount >= 3) {
                // Do not retry if over max retry count
                return false;
            }
            if (exception instanceof NoHttpResponseException) {
                // Retry if the server dropped connection on us
                return true;
            }
            if (exception instanceof SSLHandshakeException) {
                // Do not retry on SSL handshake exception
                return false;
            }
            HttpRequest request = (HttpRequest) context
                    .getAttribute(ExecutionContext.HTTP_REQUEST);
            boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
            if (!idempotent) {
                // Retry if the request is considered idempotent
                return true;
            }
            return false;
        }
    };
}
