package com.zqqbot.cdxt.exchange.binance;

import com.zqqbot.cdxt.BaseExchange;
import com.zqqbot.cdxt.dto.CommissionRate;
import com.zqqbot.cdxt.dto.Order;
import com.zqqbot.cdxt.dto.Position;
import com.zqqbot.cdxt.dto.UserTrades;
import com.zqqbot.cdxt.http.BinanceHttpClient;

import java.math.BigDecimal;
import java.util.List;

/**
 * Binance交易所实现
 */
public class BinanceExchange extends BaseExchange {
    
    private BinanceExchangeApi api;
    private BinanceExchangeParser parser;
    
    public BinanceExchange() {
        this.name = "Binance";
        this.api = new BinanceExchangeApi(sandbox, marketType);
        this.parser = new BinanceExchangeParser(marketType);
    }
    
    @Override
    protected void initHttpClient() {
        if (apiKey != null && secret != null) {
            this.httpClient = new BinanceHttpClient(apiKey, secret, passphrase, sandbox);
        }
        // 重新初始化API对象以反映最新的sandbox和marketType设置
        this.api = new BinanceExchangeApi(sandbox, marketType);
        this.parser = new BinanceExchangeParser(marketType);
    }
    
    @Override
    protected String getBaseUrl() {
        return api.getBaseUrl();
    }
    
    @Override
    protected String getSpotBaseUrl() {
        return api.getSpotBaseUrl();
    }
    
    @Override
    protected String getFuturesBaseUrl() {
        return api.getFuturesBaseUrl();
    }

    // 手续费模块
    @Override
    protected String getCommissionRateEndpoint() {
        return api.getCommissionRateEndpoint();
    }
    @Override
    protected String buildCommissionRateParams(String symbol) {
        return api.buildCommissionRateParams(symbol);
    }
    @Override
    protected CommissionRate parseCommissionRateResponse(String response) {
        return parser.parseCommissionRateResponse(response);
    }

    @Override
    protected String getFuturesMarginTypeEndpoint() {
        return api.getFuturesMarginTypeEndpoint();
    }
    @Override
    protected String buildFuturesMarginTypeParams(String symbol, String marginType) {
        return api.buildFuturesMarginTypeParams(symbol, marginType);
    }
    @Override
    protected Boolean parseFuturesMarginTypeResponse(String response) {
        return parser.parseFuturesMarginTypeResponse(response);
    }

    @Override
    protected String getFuturesDualSidePositionEndpoint() {
        return api.getFuturesDualSidePositionEndpoint();
    }
    @Override
    protected String buildFuturesDualSidePositionParams(boolean dualSidePosition) {
        return api.buildFuturesDualSidePositionParams(dualSidePosition);
    }
    @Override
    protected Boolean parseFuturesDualSidePositionResponse(String response) {
        return parser.parseFuturesDualSidePositionResponse(response);
    }

    @Override
    protected String getFuturesLeverageEndpoint() {
        return api.getFuturesLeverageEndpoint();
    }
    @Override
    protected String buildFuturesLeverageParams(String symbol, int leverage, String mgnMode) {
        return api.buildFuturesLeverageParams(symbol, leverage, mgnMode);
    }
    @Override
    protected Boolean parseFuturesLeverageResponse(String response) {
        return parser.parseFuturesLeverageResponse(response);
    }
    
    @Override
    protected String getFuturesCreateOrderEndpoint() {
        return api.getFuturesCreateOrderEndpoint();
    }
    @Override
    protected String buildFuturesCreateLimitOrderParams(String symbol, String side, String positionSide, BigDecimal quantity, BigDecimal price, String clientOrderId) {
        return api.buildFuturesCreateLimitOrderParams(symbol, side, positionSide, quantity, price, clientOrderId);
    }
    @Override
    protected String buildFuturesCreateMarketOrderParams(String symbol, String side, String positionSide, BigDecimal quantity, String clientOrderId) {
        return api.buildFuturesCreateMarketOrderParams(symbol, side, positionSide, quantity, clientOrderId);
    }
    @Override
    protected String buildFuturesCreateTakeProfitOrderParams(String symbol, String side, String positionSide, BigDecimal quantity, BigDecimal stopPrice, BigDecimal price, String clientOrderId) {
        return api.buildFuturesCreateTakeProfitOrderParams(symbol, side, positionSide, quantity, stopPrice, price, clientOrderId);
    }
    @Override
    protected String buildFuturesCreateStopLossOrderParams(String symbol, String side, String positionSide, BigDecimal quantity, BigDecimal stopPrice, BigDecimal price, String clientOrderId) {
        return api.buildFuturesCreateStopLossOrderParams(symbol, side, positionSide, quantity, stopPrice, price, clientOrderId);
    }
    @Override
    protected Order parseFuturesCreateOrderResponse(String response) {
        return parser.parseFuturesCreateOrderResponse(response);
    }
    
    @Override
    protected String getFuturesCancelOrderEndpoint() {
        return api.getFuturesCancelOrderEndpoint();
    }
    @Override
    protected String buildFuturesCancelOrderParams(String symbol, String orderId, String clientOrderId) {
        return api.buildFuturesCancelOrderParams(symbol, orderId, clientOrderId);
    }
    @Override
    protected Order parseFuturesCancelOrderResponse(String response) {
        return parser.parseFuturesCancelOrderResponse(response);
    }
    
    @Override
    protected String getFuturesGetOrderEndpoint() {
        return api.getFuturesGetOrderEndpoint();
    }
    @Override
    protected String buildFuturesGetOrderParams(String symbol, String orderId, String clientOrderId) {
        return api.buildFuturesGetOrderParams(symbol, orderId, clientOrderId);
    }
    @Override
    protected Order parseFuturesGetOrderResponse(String response) {
        return parser.parseFuturesGetOrderResponse(response);
    }

    @Override
    protected String getFuturesUserTradesEndpoint() {
        return api.getFuturesUserTradesEndpoint();
    }
    @Override
    protected String buildFuturesUserTradesParams(String symbol, String orderId, Long startTime, Long endTime) {
        return api.buildFuturesUserTradesParams(symbol, orderId, startTime, endTime);
    }
    @Override
    protected List<UserTrades> parseFuturesUserTradesResponse(String response) {
        return parser.parseFuturesUserTradesResponse(response);
    }
    
    @Override
    protected String getFuturesPositionsEndpoint() {
        return api.getFuturesPositionsEndpoint();
    }
    @Override
    protected String buildFuturesPositionsParams(String symbol) {
        return api.buildFuturesPositionsParams(symbol);
    }
    @Override
    protected List<Position> parseFuturesPositionsResponse(String response) {
        return parser.parseFuturesPositionsResponse(response);
    }

}