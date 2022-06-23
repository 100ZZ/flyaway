package com.lihuia.flyaway.http;

import com.alibaba.fastjson.JSONObject;
import com.lihuia.flyaway.exception.FlyawayException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author lihuia.com
 * @date 2022/6/20 9:57 PM
 */

@Slf4j
public class HttpClientUtil {


    public static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    private static HttpUrl setUrl(String url, Map<String, String> params) {
        checkUrl(url);
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (ObjectUtils.isEmpty(httpUrl)) {
            log.error("url: {}", url);
            throw new FlyawayException("URL解析结果为空");
        }
        HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();
        if (!CollectionUtils.isEmpty(params)) {
            params.forEach(httpUrlBuilder::addQueryParameter);
        }
        return httpUrlBuilder.build();
    }

    private static void checkUrl(String url) {
        if (StringUtils.isBlank(url)) {
            throw new FlyawayException("URL不能为空");
        }
    }

    private static Headers setHeader(Map<String, String> headers) {
        Headers.Builder builder = new Headers.Builder();
        //OKHTTP的要求
        if (CollectionUtils.isEmpty(headers)) {
            throw new FlyawayException("Header不能为空");
        }
        headers.forEach(builder::add);
        return builder.build();
    }

    /**
     * 对应postman里body，raw，json
     * @param body
     * @return
     */
    private static RequestBody setJsonBody(Map<String, String> body) {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        if (CollectionUtils.isEmpty(body)) {
            throw new FlyawayException("Body不能为空");
        }
        return RequestBody.create(JSONObject.toJSONString(body), mediaType);
    }

    /**
     * 对应postman里body，x-www-form-urlencoded
     * @param body
     * @return
     */
    private static RequestBody setFormBody(Map<String, String> body) {
        FormBody.Builder builder = new FormBody.Builder();

        if (CollectionUtils.isEmpty(body)) {
            throw new FlyawayException("Body不能为空");
        }
        body.forEach(builder::add);
        return builder.build();
    }

    /**
     * 对应postman里form-data，上传file
     * @param body
     * @param filePathMap 假如是上传多个multipart file，有可能多个key，value，因此需要保存map
     * @return
     */
    private static RequestBody setMultipartBody(Map<String, String> body, Map<String, String> filePathMap) {
        if (ObjectUtils.isEmpty(body) || ObjectUtils.isEmpty(filePathMap)) {
            throw new FlyawayException("Body和Multipart File都不能为空");
        }

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        for (String fileKey : filePathMap.keySet()) {
            String filePath = filePathMap.get(fileKey);
            File file = new File(filePath);
            if (!file.exists()) {
                log.error("filePath: {}", filePath);
                throw new FlyawayException("filePath不存在");
            }
            String filename = filePath.split("/")[filePath.split("/").length - 1];
            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
            //添加multipart file
            builder.addFormDataPart(fileKey, filename, fileBody);
        }

        if (!CollectionUtils.isEmpty(body)) {
            body.forEach(builder::addFormDataPart);
        }
        return builder.build();
    }

    /**
     * http get请求
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static String doGet(String url, Map<String, String> headers, Map<String, String> params) {
        Request request = new Request.Builder()
                .url(setUrl(url, params))
                .headers(setHeader(headers))
                .build();

        try {
            Response response = httpClient.newCall(request).execute();
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            log.error("get请求异常, request: {}, 错误信息: {}", request, e.getMessage());
        }
        return StringUtils.EMPTY;
    }

    /**
     * http post请求，body为json
     * @param url
     * @param headers
     * @param body
     * @return
     */
    public static String doPostJSON(String url, Map<String, String> headers, Map<String, String> body) {
        checkUrl(url);
        Request request = new Request.Builder()
                .url(url)
                .headers(setHeader(headers))
                .post(setJsonBody(body))
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            log.error("doPostJson请求异常, request: {}, 错误信息: {}", request, e.getMessage());
        }
        return StringUtils.EMPTY;
    }

    /**
     * http post请求，body为x-www-form-urlencoded
     * @param url
     * @param headers
     * @param body
     * @return
     */
    public static String doPostForm(String url, Map<String, String> headers, Map<String, String> body) {
        checkUrl(url);
        Request request = new Request.Builder()
                .url(url)
                .headers(setHeader(headers))
                .post(setFormBody(body))
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            log.error("doPostForm请求异常, request: {}, 错误信息: {}", request, e.getMessage());
        }
        return StringUtils.EMPTY;
    }

    /**
     * http post请求，body为form-data，传file
     * @param url
     * @param headers
     * @param body
     * @param filePathMap
     * @return
     */
    public static String doPostMultipart(String url, Map<String, String> headers, Map<String, String> body, Map<String, String> filePathMap) {
        checkUrl(url);
        Request request = new Request.Builder()
                .url(url)
                .headers(setHeader(headers))
                .post(setMultipartBody(body, filePathMap))
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            log.error("doPostMultipart请求异常, request: {}, 错误信息: {}", request, e.getMessage());
        }
        return StringUtils.EMPTY;
    }
}