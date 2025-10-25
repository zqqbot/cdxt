package com.zqqbot.cdxt;

import com.zqqbot.cdxt.dto.CommissionRate;
import com.zqqbot.cdxt.dto.Order;
import com.zqqbot.cdxt.dto.Position;
import com.zqqbot.cdxt.dto.UserTrades;
import com.zqqbot.cdxt.enums.MarketType;
import com.zqqbot.cdxt.http.HttpExchangeClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * 交易所基础抽象类
 * 实现Exchange接口的通用方法
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class BaseExchange implements Exchange {
    
    protected String apiKey;
    protected String secret;
    protected String passphrase;
    protected boolean sandbox;
    protected String name;
    protected MarketType marketType;
    protected HttpExchangeClient httpClient;

    public BaseExchange() {
        this.sandbox = false;
        this.marketType = MarketType.SPOT; // 默认为现货交易
    }
    
    /**
     * 初始化HTTP客户端
     */
    protected abstract void initHttpClient();

    @Override
    public void setApiKey(String apiKey, String secret, String passphrase) {
        this.apiKey = apiKey;
        this.secret = secret;
        this.passphrase = passphrase;
        initHttpClient();
    }

    @Override
    public void setSandbox(boolean sandbox) {
        this.sandbox = sandbox;
        initHttpClient();
    }

    @Override
    public void setMarketType(MarketType marketType) {
        this.marketType = marketType;
        initHttpClient();
    }

    @Override
    public String getName() {
        return this.name;
    }
    
    /**
     * 获取基础URL
     * @return 基础URL
     */
    protected abstract String getBaseUrl();
    
    /**
     * 获取现货基础URL
     * @return 现货基础URL
     */
    protected abstract String getSpotBaseUrl();
    
    /**
     * 获取合约基础URL
     * @return 合约基础URL
     */
    protected abstract String getFuturesBaseUrl();

    /**
     * 根据市场类型获取基础URL
     * @return 基础URL
     */
    private String getBaseUrlForMarketType() {
        return switch (marketType) {
            case SPOT -> getSpotBaseUrl();
            case FUTURES -> getFuturesBaseUrl();
            default -> getBaseUrl();
        };
    }

    // 默认实现交易所通用方法
    @Override
    public CommissionRate getCommissionRate(String symbol) {
        // 获取API URL和参数
        String endpoint = getCommissionRateEndpoint();
        String params = buildCommissionRateParams(symbol);
        // 获取交易类型
        String baseUrl = getBaseUrlForMarketType();
        // 发送请求并解析响应
        String response = httpClient.get(baseUrl, endpoint, params);
        return parseCommissionRateResponse(response);
    }

    @Override
    public Boolean setFuturesMarginType(String symbol, String marginType) {
        // 获取API URL和参数
        String endpoint = getFuturesMarginTypeEndpoint();
        String params = buildFuturesMarginTypeParams(symbol, marginType);
        // 获取交易类型
        String baseUrl = getBaseUrlForMarketType();
        // 发送请求并解析响应
        String response = httpClient.post(baseUrl, endpoint, params);
        return parseFuturesMarginTypeResponse(response);
    }

    @Override
    public Boolean setFuturesDualSidePosition(boolean dualSidePosition) {
        // 获取API URL和参数
        String endpoint = getFuturesDualSidePositionEndpoint();
        String params = buildFuturesDualSidePositionParams(dualSidePosition);
        // 获取交易类型
        String baseUrl = getBaseUrlForMarketType();
        // 发送请求并解析响应
        String response = httpClient.post(baseUrl, endpoint, params);
        return parseFuturesDualSidePositionResponse(response);
    }

    @Override
    public Boolean setFuturesLeverage(String symbol, int leverage, String mgnMode) {
        // 获取API URL和参数
        String endpoint = getFuturesLeverageEndpoint();
        String params = buildFuturesLeverageParams(symbol, leverage, mgnMode);
        // 获取交易类型
        String baseUrl = getBaseUrlForMarketType();
        // 发送请求并解析响应
        String response = httpClient.post(baseUrl, endpoint, params);
        return parseFuturesLeverageResponse(response);
    }
    
    @Override
    public Order createFuturesLimitOrder(String symbol, String side, String positionSide, BigDecimal quantity, BigDecimal price, String clientOrderId) {
        // 获取API URL和参数
        String endpoint = getFuturesCreateOrderEndpoint();
        String params = buildFuturesCreateLimitOrderParams(symbol, side, positionSide, quantity, price, clientOrderId);
        // 获取交易类型
        String baseUrl = getBaseUrlForMarketType();
        // 发送请求并解析响应
        String response = httpClient.post(baseUrl, endpoint, params);
        return parseFuturesCreateOrderResponse(response);
    }
    
    @Override
    public Order createFuturesMarketOrder(String symbol, String side, String positionSide, BigDecimal quantity, String clientOrderId) {
        // 获取API URL和参数
        String endpoint = getFuturesCreateOrderEndpoint();
        String params = buildFuturesCreateMarketOrderParams(symbol, side, positionSide, quantity, clientOrderId);
        // 获取交易类型
        String baseUrl = getBaseUrlForMarketType();
        // 发送请求并解析响应
        String response = httpClient.post(baseUrl, endpoint, params);
        return parseFuturesCreateOrderResponse(response);
    }
    
    @Override
    public Order createFuturesTakeProfitOrder(String symbol, String side, String positionSide, BigDecimal quantity, BigDecimal stopPrice, BigDecimal price, String clientOrderId) {
        // 获取API URL和参数
        String endpoint = getFuturesCreateOrderEndpoint();
        String params = buildFuturesCreateTakeProfitOrderParams(symbol, side, positionSide, quantity, stopPrice, price, clientOrderId);
        // 获取交易类型
        String baseUrl = getBaseUrlForMarketType();
        // 发送请求并解析响应
        String response = httpClient.post(baseUrl, endpoint, params);
        return parseFuturesCreateOrderResponse(response);
    }
    
    @Override
    public Order createFuturesStopLossOrder(String symbol, String side, String positionSide, BigDecimal quantity, BigDecimal stopPrice, BigDecimal price, String clientOrderId) {
        // 获取API URL和参数
        String endpoint = getFuturesCreateOrderEndpoint();
        String params = buildFuturesCreateStopLossOrderParams(symbol, side, positionSide, quantity, stopPrice, price, clientOrderId);
        // 获取交易类型
        String baseUrl = getBaseUrlForMarketType();
        // 发送请求并解析响应
        String response = httpClient.post(baseUrl, endpoint, params);
        return parseFuturesCreateOrderResponse(response);
    }
    
    @Override
    public Order cancelFuturesOrder(String symbol, String orderId, String clientOrderId) {
        // 获取API URL和参数
        String endpoint = getFuturesCancelOrderEndpoint();
        String params = buildFuturesCancelOrderParams(symbol, orderId, clientOrderId);
        // 获取交易类型
        String baseUrl = getBaseUrlForMarketType();
        // 发送请求并解析响应
        String response = httpClient.delete(baseUrl, endpoint, params);
        return parseFuturesCancelOrderResponse(response);
    }
    
    @Override
    public Order getFuturesOrder(String symbol, String orderId, String clientOrderId) {
        // 获取API URL和参数
        String endpoint = getFuturesGetOrderEndpoint();
        String params = buildFuturesGetOrderParams(symbol, orderId, clientOrderId);
        // 获取交易类型
        String baseUrl = getBaseUrlForMarketType();
        // 发送请求并解析响应
        String response = httpClient.get(baseUrl, endpoint, params);
        return parseFuturesGetOrderResponse(response);
    }

    @Override
    public List<UserTrades> getFuturesUserTrades(String symbol, String orderId, Long startTime, Long endTime) {
        String endpoint = getFuturesUserTradesEndpoint();
        String params = buildFuturesUserTradesParams(symbol, orderId, startTime, endTime);
        String baseUrl = getBaseUrlForMarketType();
        String response = httpClient.get(baseUrl, endpoint, params);
        return parseFuturesUserTradesResponse(response);
    }
    
    @Override
    public List<Position> getFuturesPositions(String symbol) {
        // 获取API URL和参数
        String endpoint = getFuturesPositionsEndpoint();
        String params = buildFuturesPositionsParams(symbol);
        // 获取交易类型
        String baseUrl = getBaseUrlForMarketType();
        // 发送请求并解析响应
        String response = httpClient.get(baseUrl, endpoint, params);
        return parseFuturesPositionsResponse(response);
    }

    // 抽象方法，由子类实现具体的请求端点和参数构建
    protected abstract String getCommissionRateEndpoint();
    protected abstract String buildCommissionRateParams(String symbol);
    //
    protected abstract String getFuturesMarginTypeEndpoint();
    protected abstract String buildFuturesMarginTypeParams(String symbol, String marginType);
    //
    protected abstract String getFuturesDualSidePositionEndpoint();
    protected abstract String buildFuturesDualSidePositionParams(boolean dualSidePosition);
    //
    protected abstract String getFuturesLeverageEndpoint();
    protected abstract String buildFuturesLeverageParams(String symbol, int leverage, String mgnMode);
    //
    protected abstract String getFuturesCreateOrderEndpoint();
    protected abstract String buildFuturesCreateLimitOrderParams(String symbol, String side, String positionSide, BigDecimal quantity, BigDecimal price, String clientOrderId);
    protected abstract String buildFuturesCreateMarketOrderParams(String symbol, String side, String positionSide, BigDecimal quantity, String clientOrderId);
    protected abstract String buildFuturesCreateTakeProfitOrderParams(String symbol, String side, String positionSide, BigDecimal quantity, BigDecimal stopPrice, BigDecimal price, String clientOrderId);
    protected abstract String buildFuturesCreateStopLossOrderParams(String symbol, String side, String positionSide, BigDecimal quantity, BigDecimal stopPrice, BigDecimal price, String clientOrderId);
    //
    protected abstract String getFuturesCancelOrderEndpoint();
    protected abstract String buildFuturesCancelOrderParams(String symbol, String orderId, String clientOrderId);
    //
    protected abstract String getFuturesGetOrderEndpoint();
    protected abstract String buildFuturesGetOrderParams(String symbol, String orderId, String clientOrderId);
    //
    protected abstract String getFuturesUserTradesEndpoint();
    protected abstract String buildFuturesUserTradesParams(String symbol, String orderId, Long startTime, Long endTime);
    //
    protected abstract String getFuturesPositionsEndpoint();
    protected abstract String buildFuturesPositionsParams(String symbol);

    // 抽象方法，由子类实现具体的响应解析
    protected abstract CommissionRate parseCommissionRateResponse(String response);
    protected abstract Boolean parseFuturesMarginTypeResponse(String response);
    protected abstract Boolean parseFuturesDualSidePositionResponse(String response);
    protected abstract Boolean parseFuturesLeverageResponse(String response);
    protected abstract Order parseFuturesCreateOrderResponse(String response);
    protected abstract Order parseFuturesCancelOrderResponse(String response);
    protected abstract Order parseFuturesGetOrderResponse(String response);
    protected abstract List<UserTrades> parseFuturesUserTradesResponse(String response);
    protected abstract List<Position> parseFuturesPositionsResponse(String response);

}