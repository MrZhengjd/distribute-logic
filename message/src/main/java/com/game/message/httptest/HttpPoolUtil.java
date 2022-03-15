package com.game.message.httptest;

import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author zheng
 */
public class HttpPoolUtil {
    private static int keepAlive = 30;
    private static final int maxTotal = 1000;
    private static final int defaultMaxPerRoute = 400;
    private static final int maxIdleTime = 10000;
    private static final int connctTimeout = 10000;
    private static final int socketTimeout = 10000;
    private volatile static CloseableHttpClient HTTP_CLIENT ;
    private static ConnectionKeepAliveStrategy strategy = new ConnectionKeepAliveStrategy() {
        @Override
        public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
            HeaderElementIterator headerIterator = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (headerIterator.hasNext()){
                HeaderElement headerElement = headerIterator.nextElement();
                String param = headerElement.getName();
                String value = headerElement.getValue();
                if (value != null && param.equalsIgnoreCase("timeout")){
                    return Long.parseLong(value) * 1000;
                }
            }
            return 60 * 1000;
        }
    };
    private static PoolingHttpClientConnectionManager connectionManager ;
    static {

//        init();
    }
    public static PostUtil createPost(String url){
        return new PostUtil(url);
    }
    public static GetUtil createGetUtil(String url){
        return new GetUtil(url);
    }
    private static HttpClientBuilder clientBuilder;
    private static ScheduledExecutorService executorService;
    private static void init(){
        connectionManager= new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(maxTotal);
        connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        clientBuilder = createClientBuilder();
    }
    public static String doGet(String url,Map<String ,String> params){
        GetUtil getUtil = createGetUtil(url).addParameters(params);
        ResponseWrap execute = getUtil.execute();
        try {
            return EntityUtils.toString(execute.entity, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String doPost(String url,Map<String ,String> params){
        PostUtil getUtil = createPost(url).addParameters(params);
        ResponseWrap execute = getUtil.execute();
        try {
            return EntityUtils.toString(execute.entity, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static class ResponseWrap{
        private CloseableHttpResponse response;
        private HttpEntity entity;

        public ResponseWrap(CloseableHttpResponse response) {
            this.response = response;
            try {
                HttpEntity entity = response.getEntity();
                if (entity == null){
                    this.entity = new BasicHttpEntity();
                }else {
                    this.entity = new BufferedHttpEntity(entity);
                }
                EntityUtils.consume(entity);
                this.response.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private static class Holder{

        private static CloseableHttpClient closeableHttpClient = clientBuilder.build();
        static {
            executorService = Executors.newScheduledThreadPool(1);
            executorService.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    connectionManager.closeExpiredConnections();
                    connectionManager.closeIdleConnections(maxIdleTime,TimeUnit.SECONDS);
                }
            },30,30, TimeUnit.SECONDS);
        }
    }
    private static HttpClientBuilder createClientBuilder() {
        HttpClientBuilder builder = HttpClients.custom().setConnectionManager(connectionManager)
                .setKeepAliveStrategy(strategy);
        return builder;
    }
    public static CloseableHttpClient getInstance(){
        return Holder.closeableHttpClient;
    }
    public static class GetUtil{
        private HttpGet httpGet;
        private URIBuilder uriBuilder;
        private RequestConfig.Builder builder;

        public GetUtil(String uri) {
            this.httpGet =new HttpGet(uri);
            uriBuilder = new URIBuilder().setPath(httpGet.getURI().toString());
            builder = RequestConfig.custom().setConnectTimeout(connctTimeout);

        }
        public GetUtil addHeader(String param,String value){
            httpGet.addHeader(param,value);
            return this;
        }
        public GetUtil addParameter(String param,String value){
            uriBuilder.setParameter(param,value);
            return this;
        }
        public GetUtil addParameters(Map<String ,String > params){
            for (Map.Entry<String ,String > entry : params.entrySet()){
                uriBuilder.setParameter(entry.getKey(),entry.getValue());
            }

            return this;
        }
        public ResponseWrap execute(){
           try {
               URI uri = new URI(uriBuilder.build().toString().replace("%3F","?"));
               httpGet.setURI(uri);
               httpGet.setConfig(builder.build());
           }catch (Exception e){
               e.printStackTrace();
           }
           return exec(httpGet);
        }
    }
    public static class PostUtil{
        private HttpPost httpPost;
        private EntityBuilder entityBuilder;
        private RequestConfig.Builder builder;
        public PostUtil(String uri) {
            this.httpPost =new HttpPost(uri);
            this.entityBuilder = EntityBuilder.create().setParameters(new ArrayList<NameValuePair>());
            builder = RequestConfig.custom().setConnectTimeout(connctTimeout).setSocketTimeout(socketTimeout);

        }
        public PostUtil(String url, ContentType contentType){
            this(url);
            entityBuilder.setContentType(contentType);
        }
        public PostUtil addParameter(String param,String value){
            entityBuilder.getParameters().add(new BasicNameValuePair(param,value));
            return this;
        }
        public PostUtil addParameters(Map<String ,String > parameters){
            for (Map.Entry<String,String> entry :parameters.entrySet()){
                addParameter(entry.getKey(), entry.getValue());
            }
            return this;
        }
        public ResponseWrap execute(){
            HttpEntity httpEntity = entityBuilder.build();
            httpPost.setEntity(httpEntity);
            httpPost.setConfig(builder.build());
            return exec(httpPost);
        }
    }

    private static ResponseWrap exec(HttpRequestBase requestBase){
        ResponseWrap responseWrap = null;
        try {
            HTTP_CLIENT = getInstance();
            HttpClientContext context = HttpClientContext.create();
            CloseableHttpResponse execute = HTTP_CLIENT.execute(requestBase,context);
            responseWrap = new ResponseWrap(execute);
        }catch (Exception e){
            e.printStackTrace();
        }
        return responseWrap;
    }

}
