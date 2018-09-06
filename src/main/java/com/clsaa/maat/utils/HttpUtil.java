package com.clsaa.maat.utils;


import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * <p>
 * HTTP客户端工具类
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2018-09-06
 */
public class HttpUtil {

    private static final MediaType MEDIATYPE = MediaType.APPLICATION_JSON_UTF8;

    private static RequestBodySpec uri(String url, HttpMethod method) {
        return WebClient.create().method(method).uri(url);
    }

    private static void addHeader(Map<String, String> header, RequestBodySpec uri) {
        if (!CollectionUtils.isEmpty(header)) {
            header.forEach(uri::cookie);
        }
    }

    private static <V> V post(RequestBodySpec uri, Object parameter, Class<V> resultType) {
        return uri.contentType(MEDIATYPE).body(Mono.just(parameter), Object.class).retrieve().bodyToMono(resultType)
                .block();
    }

    /**
     * get请求
     *
     * @param url        请求路径
     * @param resultType 返回结果类型
     * @return {@link V}
     */
    public static <V> V get(String url, Class<V> resultType) {
        return uri(url, HttpMethod.GET).retrieve().bodyToMono(resultType).block();
    }

    /**
     * get请求
     *
     * @param url        请求路径
     * @param header     请求头
     * @param resultType 返回结果类型
     * @return {@link V}
     */
    public static <V> V get(String url, Map<String, String> header, Class<V> resultType) {

        RequestBodySpec uri = uri(url, HttpMethod.GET);
        addHeader(header, uri);
        return uri.retrieve().bodyToMono(resultType).block();
    }

    /**
     * post请求
     *
     * @param parameter  请求参数
     * @param url        请求路径
     * @param resultType 返回结果类型
     * @return {@link V}
     */
    public static <V> V post(Object parameter, String url, Class<V> resultType) {

        return post(uri(url, HttpMethod.POST), parameter, resultType);
    }

    /**
     * post请求
     *
     * @param parameter  请求参数
     * @param url        请求路径
     * @param header     请求头
     * @param resultType 返回结果类型
     * @return {@link V}
     */
    public static <V> V post(Object parameter, String url, Map<String, String> header, Class<V> resultType) {
        RequestBodySpec uri = uri(url, HttpMethod.POST);
        addHeader(header, uri);
        return post(uri, parameter, resultType);
    }

}
