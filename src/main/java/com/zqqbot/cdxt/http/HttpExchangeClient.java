package com.zqqbot.cdxt.http;

/**
 * 交易所HTTP客户端接口
 * 定义交易所HTTP客户端的通用方法
 */
public interface HttpExchangeClient {
    
    /**
     * 发送GET请求
     * @param baseUrl 基础URL
     * @param endpoint API端点
     * @param params 请求参数
     * @return 响应字符串
     */
    String get(String baseUrl, String endpoint, String params);
    
    /**
     * 发送POST请求
     * @param baseUrl 基础URL
     * @param endpoint API端点
     * @param params 请求参数
     * @return 响应字符串
     */
    String post(String baseUrl, String endpoint, String params);
    
    /**
     * 发送DELETE请求
     * @param baseUrl 基础URL
     * @param endpoint API端点
     * @param params 请求参数
     * @return 响应字符串
     */
    String delete(String baseUrl, String endpoint, String params);
    
    /**
     * 发送PUT请求
     * @param baseUrl 基础URL
     * @param endpoint API端点
     * @param params 请求参数
     * @return 响应字符串
     */
    String put(String baseUrl, String endpoint, String params);
}