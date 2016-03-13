package com.mttnow.acceptancetests.steps;

import static com.mttnow.acceptancetests.StoryContext.getFromContext;
import static com.mttnow.acceptancetests.StoryContext.putInContext;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpParams;
import org.jbehave.core.annotations.AfterStory;
import org.jbehave.core.annotations.BeforeStory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Base class for writing REST acceptance tests. Contains setup and convenience methods.
 */
public class BaseRestAcceptanceSteps extends BaseWebAcceptanceSteps {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRestAcceptanceSteps.class);
    private static final String HTTP_CLIENT = "httpClient";

    private HttpClient httpClient;
    protected ObjectMapper objectMapper;
    private Header[] headers;

    @BeforeStory
    public void before() throws Exception {
        putInContext(HTTP_CLIENT, new DefaultHttpClient());
        httpClient = getFromContext(HTTP_CLIENT);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JodaModule());
        headers = new Header[]{
                new BasicHeader("Cache-Control", "no-cache"),
                new BasicHeader("Authorization", "Basic YWRtaW46YWRtaW4="), //admin:admin
                new BasicHeader("Accept-Charse", "utf-8")
        };
    }

    @AfterStory
    public void after() {
        httpClient.getConnectionManager().shutdown();
    }

    /**
     * Convenience method for executing an HTTP GET
     * @param relativePath path to resource
     * @return HttpResponse for the operation
     * @throws IOException if any problems occur
     */
    public HttpResponse get(String relativePath) throws IOException{
        return get(relativePath, null, null);
    }
    
    /**
     * Convenience method for executing an HTTP GET with header
     * @param relativePath path to resource
     * @param header that could overwrite existing ones
     * @return HttpResponse for the operation
     * @throws IOException if any problems occur
     */
    public HttpResponse get(String relativePath, Header header) throws IOException{
      return get(relativePath, null, header);
    }
    
    /**
     * Convenience method for executing an HTTP GET with HTTP params.
     * @param relativePath path to resource
     * @param params http parameters
     * @return HttpResponse for the operation
     * @throws IOException if any problems occur
     */
    public HttpResponse get(String relativePath, HttpParams params) throws IOException {
      return get(relativePath, params, null);
    }

    /**
     * Convenience method for executing an HTTP GET with HTTP params and header.
     * @param relativePath path to resource
     * @param params http parameters
     * @param header that could overwrite existing ones
     * @return HttpResponse for the operation
     * @throws IOException if any problems occur
     */
    public HttpResponse get(String relativePath, HttpParams params, Header newHeader) throws IOException {
        HttpGet httpGet = new HttpGet(requestUrl(relativePath));
        httpGet.setHeaders(this.headers);
        if (headers != null) {
            for (Header header : headers) {
                httpGet.setHeader(header);
            }
        }
        if (params != null) {
            httpGet.setParams(params);
        }
        if(newHeader != null){
          httpGet.setHeader(newHeader);
        }
        return httpClient.execute(httpGet);
    }

    /**
     * Convenience method for executing an HTTP POST.
     * @param relativePath path to resource
     * @param params params in key-value pairs
     * @return HttpResponse for the operation
     * @throws IOException if any problems occur
     */
    public HttpResponse post(String relativePath, List<NameValuePair> params) throws IOException {
        HttpPost httpPost = new HttpPost(requestUrl(relativePath));
        httpPost.setHeaders(this.headers);
        if (params != null) {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
        }
        return httpClient.execute(httpPost);
    }

    /**
     * Convenience method for executing an HTTP POST with json body.
     * @param relativePath path to resource
     * @param jsonObject the stringified json object to be sent
     * @return HttpResponse for the operation
     * @throws IOException if any problems occur
     */
    public HttpResponse post(String relativePath, String jsonObject) throws IOException{
        HttpPost httpPost = new HttpPost(requestUrl(relativePath));
        StringEntity stringEntity = new StringEntity(jsonObject);
        stringEntity.setContentType("application/json");
        httpPost.setHeaders(this.headers);
        httpPost.setEntity(stringEntity);
        HttpResponse response= httpClient.execute(httpPost);
        return response;
    }

    public HttpResponse postMultipart(String relativePath, Map<String, Object> multipartContent) throws IOException {
        HttpPost httpPost = new HttpPost(requestUrl(relativePath));
        MultipartEntity reqEntity = new MultipartEntity();
        for (String key : multipartContent.keySet()) {
            Object val = multipartContent.get(key);
            if(val instanceof String){
                reqEntity.addPart(key,new StringBody((String)val));
            }else if(val instanceof File){
                reqEntity.addPart(key,new FileBody((File)val));
            }

        }
        httpPost.setHeaders(this.headers);
        httpPost.setEntity(reqEntity);
        HttpResponse response = httpClient.execute(httpPost);
        return response;

    }

    /**
     * POST with custom headers
     *
     * @param relativePath
     * @param header
     * @return
     * @throws Exception
     */
    public HttpResponse post(String relativePath, Header header) throws Exception {
        HttpPost httpPost = new HttpPost(requestUrl(relativePath));
        String s = "";
        StringEntity stringEntity = new StringEntity(s);
        stringEntity.setContentType("application/json");
        httpPost.setHeader(header);
        httpPost.setEntity(stringEntity);
        HttpResponse response= httpClient.execute(httpPost);
        return response;
    }

    /**
     * Convenience method for executing an HTTP DELETE.
     * @param relativePath path to resource
     * @param params http parameters
     * @return HttpResponse for the operation
     * @throws IOException if any problems occur
     */
    public HttpResponse delete(String relativePath, HttpParams params) throws IOException{
        HttpDelete httpDelete = new HttpDelete(requestUrl(relativePath));
        httpDelete.setHeaders(this.headers);
        if (headers != null) {
            for (Header header : headers) {
                httpDelete.setHeader(header);
            }
        }
        if(params != null){
            httpDelete.setParams(params);
        }
        return httpClient.execute(httpDelete);
    }

    /**
     * Convenience method for executing an HTTP PUT.
     * @param relativePath path to resource
     * @param params in key-value pairs
     * @return HttpResponse for the operation
     * @throws IOException if any problems occur
     */
    public HttpResponse put(String relativePath, List<NameValuePair> params) throws IOException{
        HttpPut httpPut = new HttpPut(requestUrl(relativePath));
        httpPut.setHeaders(this.headers);
        if (params != null) {
            httpPut.setEntity(new UrlEncodedFormEntity(params));
        }
        return httpClient.execute(httpPut);
    }

    /**
     * Convenience method for executing an HTTP PUT with json body.
     * @param relativePath path to resource
     * @param jsonObject the stringified json object to be sent
     * @return HttpResponse for the operation
     * @throws IOException if any problems occur
     */
    public HttpResponse put(String relativePath, String jsonObject) throws IOException{
        HttpPut httpPut = new HttpPut(requestUrl(relativePath));
        StringEntity stringEntity = new StringEntity(jsonObject);
        stringEntity.setContentType("application/json");
        httpPut.setHeaders(this.headers);
        httpPut.setEntity(stringEntity);
        return httpClient.execute(httpPut);
    }

    public <T extends Object> T parseJSONResponse(HttpResponse response, Class<T> clazz) throws IOException {
        return objectMapper.readValue(response.getEntity().getContent(), clazz);
    }

    private String requestUrl(String relativePath) {
        String requestUrl = getBaseURI() + relativePath;
        LOGGER.debug("getting url: " + requestUrl);
        return requestUrl;
    }

}
