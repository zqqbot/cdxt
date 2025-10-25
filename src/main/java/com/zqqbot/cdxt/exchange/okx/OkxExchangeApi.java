package com.zqqbot.cdxt.exchange.okx;

import com.zqqbot.cdxt.enums.MarketType;

import java.math.BigDecimal;

/**
 * OKX交易所API端点和参数构建类
 * 用于管理OKX交易所的所有API端点和参数构建逻辑
 */
public record OkxExchangeApi(boolean sandbox, MarketType marketType) {

    private static final String SPOT_BASE_URL = "https://www.okx.com";
    private static final String SPOT_SANDBOX_URL = "https://www.okx.com";
    private static final String FUTURES_BASE_URL = "https://www.okx.com";
    private static final String FUTURES_SANDBOX_URL = "https://www.okx.com";

    /**
     * 获取基础URL
     *
     * @return 基础URL
     */
    public String getBaseUrl() {
        return getSpotBaseUrl();
    }

    /**
     * 获取现货基础URL
     *
     * @return 现货基础URL
     */
    public String getSpotBaseUrl() {
        return sandbox ? SPOT_SANDBOX_URL : SPOT_BASE_URL;
    }

    /**
     * 获取合约基础URL
     *
     * @return 合约基础URL
     */
    public String getFuturesBaseUrl() {
        return sandbox ? FUTURES_SANDBOX_URL : FUTURES_BASE_URL;
    }

    // API端点方法
    public String getCommissionRateEndpoint() {
        return "/api/v5/account/trade-fee";
    }
    // 参数构建方法
    public String buildCommissionRateParams(String symbol) {
        return "instType=" + (marketType == MarketType.SPOT ? "SPOT" : "FUTURES");
    }

    public String getFuturesDualSidePositionEndpoint() {
        if (marketType == MarketType.FUTURES) {
            return "/api/v5/account/set-position-mode";
        }
        return null;
    }

    public String buildFuturesDualSidePositionParams(boolean dualSidePosition) {
        if (marketType == MarketType.FUTURES) {
            // 持仓方式 long_short_mode：双向模式 net_mode：单向持仓
            return "posMode=" + (dualSidePosition ? "long_short_mode" : "net_mode");
        }
        return null;
    }

    public String getFuturesLeverageEndpoint() {
        if (marketType == MarketType.FUTURES) {
            return "/api/v5/account/set-leverage";
        }
        return null;
    }

    public String buildFuturesLeverageParams(String symbol, int leverage, String mgnMode) {
        if (marketType == MarketType.FUTURES) {
            // 杠杆倍数 1 到 125 整数
            return "instId=" + symbol.replace("/", "-") + "&lever=" + leverage + "&mgnMode=" + mgnMode;
        }
        return null;
    }
    
    public String getFuturesCreateOrderEndpoint() {
        if (marketType == MarketType.FUTURES) {
            return "/api/v5/trade/order";
        }
        return null;
    }
    
    public String buildFuturesCreateLimitOrderParams(String symbol, String side, String positionSide, BigDecimal quantity, BigDecimal price, String clientOrderId) {
        if (marketType == MarketType.FUTURES) {
            StringBuilder params = new StringBuilder();
            params.append("{\"instId\":\"").append(symbol.replace("/", "-")).append("\"")
                  .append(",\"tdMode\":\"cross\"")
                  .append(",\"side\":\"").append(side).append("\"")
                  .append(",\"posSide\":\"").append(positionSide).append("\"")
                  .append(",\"ordType\":\"limit\"")
                  .append(",\"sz\":\"").append(quantity.toPlainString()).append("\"")
                  .append(",\"px\":\"").append(price.toPlainString()).append("\"");
            if (clientOrderId != null && !clientOrderId.isEmpty()) {
                params.append(",\"clOrdId\":\"").append(clientOrderId).append("\"");
            }
            params.append("}");
            return params.toString();
        }
        return null;
    }
    
    public String buildFuturesCreateMarketOrderParams(String symbol, String side, String positionSide, BigDecimal quantity, String clientOrderId) {
        if (marketType == MarketType.FUTURES) {
            StringBuilder params = new StringBuilder();
            params.append("{\"instId\":\"").append(symbol.replace("/", "-")).append("\"")
                  .append(",\"tdMode\":\"cross\"")
                  .append(",\"side\":\"").append(side).append("\"")
                  .append(",\"posSide\":\"").append(positionSide).append("\"")
                  .append(",\"ordType\":\"market\"")
                  .append(",\"sz\":\"").append(quantity.toPlainString()).append("\"");
            if (clientOrderId != null && !clientOrderId.isEmpty()) {
                params.append(",\"clOrdId\":\"").append(clientOrderId).append("\"");
            }
            params.append("}");
            return params.toString();
        }
        return null;
    }
    
    public String buildFuturesCreateTakeProfitOrderParams(String symbol, String side, String positionSide, BigDecimal quantity, BigDecimal stopPrice, BigDecimal price, String clientOrderId) {
        if (marketType == MarketType.FUTURES) {
            StringBuilder params = new StringBuilder();
            params.append("{\"instId\":\"").append(symbol.replace("/", "-")).append("\"")
                  .append(",\"tdMode\":\"cross\"")
                  .append(",\"side\":\"").append(side).append("\"")
                  .append(",\"posSide\":\"").append(positionSide).append("\"")
                  .append(",\"ordType\":\"take_profit\"")
                  .append(",\"sz\":\"").append(quantity.toPlainString()).append("\"")
                  .append(",\"tpTriggerPx\":\"").append(stopPrice.toPlainString()).append("\"")
                  .append(",\"tpOrdPx\":\"").append(price.toPlainString()).append("\"");
            if (clientOrderId != null && !clientOrderId.isEmpty()) {
                params.append(",\"clOrdId\":\"").append(clientOrderId).append("\"");
            }
            params.append("}");
            return params.toString();
        }
        return null;
    }
    
    public String buildFuturesCreateStopLossOrderParams(String symbol, String side, String positionSide, BigDecimal quantity, BigDecimal stopPrice, BigDecimal price, String clientOrderId) {
        if (marketType == MarketType.FUTURES) {
            StringBuilder params = new StringBuilder();
            params.append("{\"instId\":\"").append(symbol.replace("/", "-")).append("\"")
                  .append(",\"tdMode\":\"cross\"")
                  .append(",\"side\":\"").append(side).append("\"")
                  .append(",\"posSide\":\"").append(positionSide).append("\"")
                  .append(",\"ordType\":\"stop_loss\"")
                  .append(",\"sz\":\"").append(quantity.toPlainString()).append("\"")
                  .append(",\"slTriggerPx\":\"").append(stopPrice.toPlainString()).append("\"")
                  .append(",\"slOrdPx\":\"").append(price.toPlainString()).append("\"");
            if (clientOrderId != null && !clientOrderId.isEmpty()) {
                params.append(",\"clOrdId\":\"").append(clientOrderId).append("\"");
            }
            params.append("}");
            return params.toString();
        }
        return null;
    }
    
    public String getFuturesCancelOrderEndpoint() {
        if (marketType == MarketType.FUTURES) {
            return "/api/v5/trade/cancel-order";
        }
        return null;
    }
    
    public String buildFuturesCancelOrderParams(String symbol, String orderId, String clientOrderId) {
        if (marketType == MarketType.FUTURES) {
            StringBuilder params = new StringBuilder();
            params.append("{\"instId\":\"").append(symbol.replace("/", "-")).append("\"");
            if (orderId != null && !orderId.isEmpty()) {
                params.append(",\"ordId\":\"").append(orderId).append("\"");
            }
            if (clientOrderId != null && !clientOrderId.isEmpty()) {
                params.append(",\"clOrdId\":\"").append(clientOrderId).append("\"");
            }
            params.append("}");
            return params.toString();
        }
        return null;
    }
    
    public String getFuturesGetOrderEndpoint() {
        if (marketType == MarketType.FUTURES) {
            return "/api/v5/trade/order";
        }
        return null;
    }
    
    public String buildFuturesGetOrderParams(String symbol, String orderId, String clientOrderId) {
        if (marketType == MarketType.FUTURES) {
            StringBuilder params = new StringBuilder();
            params.append("instId=").append(symbol.replace("/", "-"));
            if (orderId != null && !orderId.isEmpty()) {
                params.append("&ordId=").append(orderId);
            }
            if (clientOrderId != null && !clientOrderId.isEmpty()) {
                params.append("&clOrdId=").append(clientOrderId);
            }
            return params.toString();
        }
        return null;
    }
    
    public String getFuturesPositionsEndpoint() {
        if (marketType == MarketType.FUTURES) {
            return "/api/v5/account/positions";
        }
        return null;
    }
    
    public String buildFuturesPositionsParams(String symbol) {
        if (marketType == MarketType.FUTURES) {
            StringBuilder params = new StringBuilder();
            if (symbol != null && !symbol.isEmpty()) {
                params.append("instId=").append(symbol.replace("/", "-"));
            }
            return params.toString();
        }
        return null;
    }

    public String getFuturesUserTradesEndpoint() {
        if (marketType == MarketType.FUTURES) {
            return "/fapi/v1/userTrades";
        }
        return null;
    }

    public String buildFuturesUserTradesParams(String symbol, String orderId, Long startTime, Long endTime) {
        if (marketType == MarketType.FUTURES) {
            StringBuilder params = new StringBuilder();
            params.append("symbol=").append(symbol.replace("/", ""));
            if (orderId != null && !orderId.isEmpty()) {
                params.append("&orderId=").append(orderId);
            }
            if (startTime != null) {
                params.append("&startTime=").append(startTime);
            }
            if (endTime != null) {
                params.append("&endTime=").append(endTime);
            }
            params.append("&timestamp=").append(System.currentTimeMillis());
            return params.toString();
        }
        return null;
    }

}