package com.zqqbot.cdxt.http;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.zqqbot.cdxt.exception.ExchangeException;
import lombok.Data;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;

/**
 * Binance交易所HTTP客户端
 * 用于处理Binance交易所API的HTTP请求
 */
@Data
public class BinanceHttpClient implements HttpExchangeClient {
    
    private String apiKey;
    private String secret;
    private String passphrase;
    private boolean sandbox;
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    
    public BinanceHttpClient(String apiKey, String secret, String passphrase, boolean sandbox) {
        this.apiKey = apiKey;
        this.secret = secret;
        this.passphrase = passphrase;
        this.sandbox = sandbox;
    }
    
    /**
     * 发送GET请求
     * @param baseUrl 基础URL
     * @param endpoint API端点
     * @param params 请求参数
     * @return 响应字符串
     */
    @Override
    public String get(String baseUrl, String endpoint, String params) {
        return executeRequest(baseUrl, endpoint, params, HttpRequest::get, false);
    }
    
    /**
     * 发送POST请求
     * @param baseUrl 基础URL
     * @param endpoint API端点
     * @param params 请求参数
     * @return 响应字符串
     */
    @Override
    public String post(String baseUrl, String endpoint, String params) {
        return executeRequest(baseUrl, endpoint, params, HttpRequest::post, true);
    }
    
    /**
     * 发送DELETE请求
     * @param baseUrl 基础URL
     * @param endpoint API端点
     * @param params 请求参数
     * @return 响应字符串
     */
    @Override
    public String delete(String baseUrl, String endpoint, String params) {
        return executeRequest(baseUrl, endpoint, params, HttpRequest::delete, true);
    }

    /**
     * 发送PUT请求
     * @param baseUrl 基础URL
     * @param endpoint API端点
     * @param params 请求参数
     * @return 响应字符串
     */
    @Override
    public String put(String baseUrl, String endpoint, String params) {
        return executeRequest(baseUrl, endpoint, params, HttpRequest::put, true);
    }
    
    /**
     * 执行HTTP请求的通用方法
     * @param baseUrl 基础URL
     * @param endpoint API端点
     * @param params 请求参数
     * @param requestFunction 创建HttpRequest的函数
     * @param requiresBody 是否需要请求体
     * @return 响应字符串
     */
    private String executeRequest(String baseUrl, String endpoint, String params, 
                                 Function<String, HttpRequest> requestFunction, boolean requiresBody) {
        try {
            String url = buildUrl(baseUrl, endpoint, params, requiresBody);
            System.out.println("url: " + url + "?" + params);
            HttpRequest request = requestFunction.apply(url);
            request.header("X-MBX-APIKEY", apiKey);
            
            // 对于需要请求体的方法，添加Content-Type和超时设置
            if (requiresBody) {
                request.header("Content-Type", "application/x-www-form-urlencoded");
                request.timeout(30000);
                if (params != null && !params.isEmpty()) {
                    String signature = generateSignature(params, secret);
                    request.body(params + "&signature=" + signature);
                }
            }
            
            try (HttpResponse response = request.execute()) {
                return response.body();
            }
        } catch (Exception e) {
            String method = requiresBody ? "POST/PUT/DELETE" : "GET";
            throw new ExchangeException("Failed to send " + method + " request", e);
        }
    }
    
    /**
     * 构建完整URL
     * @param baseUrl 基础URL
     * @param endpoint API端点
     * @param params 查询参数
     * @param requiresBody 是否需要请求体
     * @return 完整URL
     */
    private String buildUrl(String baseUrl, String endpoint, String params, boolean requiresBody) {
        StringBuilder url = new StringBuilder(baseUrl);
        url.append(endpoint);
        
        // 对于GET请求，参数作为查询参数添加到URL中
        if (!requiresBody && params != null && !params.isEmpty()) {
            url.append("?").append(params);
            String signature = generateSignature(params, secret);
            url.append("&signature=").append(signature);
        }
        
        return url.toString();
    }
    
    /**
     * 生成HMAC SHA256签名
     * @param data 待签名数据
     * @param secretKey 密钥
     * @return 签名字符串
     */
    public static String generateSignature(String data, String secretKey) {
        try {
            Mac hmacSha256 = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), HMAC_SHA256_ALGORITHM);
            hmacSha256.init(secretKeySpec);
            byte[] hash = hmacSha256.doFinal(data.getBytes());
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error signing request", e);
        }
    }
    
    /**
     * 字节数组转十六进制字符串
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}