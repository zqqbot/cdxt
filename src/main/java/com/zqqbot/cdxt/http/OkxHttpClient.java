package com.zqqbot.cdxt.http;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.zqqbot.cdxt.exception.ExchangeException;
import lombok.Data;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * OKX交易所HTTP客户端
 * 用于处理OKX交易所API的HTTP请求
 */
@Data
public class OkxHttpClient implements HttpExchangeClient {
    
    private String apiKey;
    private String secret;
    private String passphrase;
    private boolean sandbox;
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    // 模拟交易请求的头部添加`x-simulated-trading: 1`  实盘:0 , 模拟盘:1
    private static final String SIMULATED_TRADING_FLAG = "1"; // 实盘:0 , 模拟盘:1
    
    public OkxHttpClient(String apiKey, String secret, String passphrase, boolean sandbox) {
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
            String url = buildUrl(baseUrl, endpoint);
            System.out.println("url: " + url + "?" + params);
            HttpRequest request = requestFunction.apply(url);
            
            // OKX需要特定的请求头
            request.timeout(30000);
            request.header("OK-ACCESS-KEY", apiKey);
            request.header("OK-ACCESS-PASSPHRASE", passphrase);
            request.header("Content-Type", "application/json");
            
            // 使用ISO格式的时间戳（精确到毫秒，符合OKX要求）
            java.time.Instant now = java.time.Instant.now();
            String timestamp = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .withZone(java.time.ZoneOffset.UTC)
                .format(now);
            request.header("OK-ACCESS-TIMESTAMP", timestamp);
            
            // 如果是模拟盘交易，添加特殊请求头
            if (sandbox) {
                request.header("x-simulated-trading", SIMULATED_TRADING_FLAG);
            }
            
            // 处理签名
            String signature;
            if (requiresBody) {
                // POST请求需要将参数转换为JSON格式
                String jsonParams = convertParamsToJSON(params);
                signature = generateSignature(timestamp, "POST", endpoint, jsonParams, secret);
                request.header("OK-ACCESS-SIGN", signature);
                if (jsonParams != null && !jsonParams.isEmpty()) {
                    request.body(jsonParams);
                }
            } else {
                // GET请求也需要签名，参数需要包含在签名中
                String endpointWithParams = endpoint;
                if (params != null && !params.isEmpty()) {
                    endpointWithParams += "?" + params;
                }
                signature = generateSignature(timestamp, "GET", endpointWithParams, null, secret);
                request.header("OK-ACCESS-SIGN", signature);
                // 添加查询参数
                if (params != null && !params.isEmpty()) {
                    url += "?" + params;
                }
                request.setUrl(url);
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
     * 将参数字符串转换为JSON格式
     * @param params 参数字符串，格式为 key1=value1&key2=value2
     * @return JSON格式的字符串
     */
    public String convertParamsToJSON(String params) {
        if (params == null || params.isEmpty()) {
            return "{}"; // 返回空的JSON对象
        }
        
        // 解析参数字符串
        Map<String, String> paramMap = new HashMap<>();
        String[] pairs = params.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                paramMap.put(keyValue[0], keyValue[1]);
            }
        }
        
        // 转换为JSON格式
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            if (!first) {
                json.append(",");
            }
            json.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\"");
            first = false;
        }
        json.append("}");
        
        return json.toString();
    }
    
    /**
     * 构建完整URL
     * @param baseUrl 基础URL
     * @param endpoint API端点
     * @return 完整URL
     */
    private String buildUrl(String baseUrl, String endpoint) {
        return baseUrl + endpoint;
    }
    
    /**
     * 生成OKX所需的HMAC SHA256签名
     * @param timestamp 时间戳 (ISO格式，例如：2020-12-08T09:08:57.715Z)
     * @param method HTTP方法 (GET, POST等)
     * @param endpoint API端点 (例如：/api/v5/account/balance)
     * @param body 请求体 (对于GET请求可以为null)
     * @param secretKey 密钥
     * @return Base64编码的签名字符串
     */
    public static String generateSignature(String timestamp, String method, String endpoint, String body, String secretKey) {
        try {
            // 创建预哈希字符串：timestamp + method + requestPath + body
            StringBuilder signStr = new StringBuilder();
            signStr.append(timestamp).append(method.toUpperCase()).append(endpoint);
            
            // 对于GET请求，body为null，对于POST请求，body是请求体，GET请求没有body，signStr保持不变
            if (body != null && !body.isEmpty()) {
                signStr.append(body);
            }

            // 使用HMAC SHA256签名
            Mac hmacSha256 = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), HMAC_SHA256_ALGORITHM);
            hmacSha256.init(secretKeySpec);
            byte[] hash = hmacSha256.doFinal(signStr.toString().getBytes());
            
            // Base64编码
            return java.util.Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error signing request", e);
        }
    }
}