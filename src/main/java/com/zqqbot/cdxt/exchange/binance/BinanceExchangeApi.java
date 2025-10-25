package com.zqqbot.cdxt.exchange.binance;

import com.zqqbot.cdxt.enums.MarketType;

import java.math.BigDecimal;

/**
 * Binance交易所API端点和参数构建类
 * 用于管理Binance交易所的所有API端点和参数构建逻辑
 */
public record BinanceExchangeApi(boolean sandbox, MarketType marketType) {

    private static final String SPOT_BASE_URL = "https://api.binance.com";
    private static final String SPOT_SANDBOX_URL = "https://demo-api.binance.com";
    private static final String FUTURES_BASE_URL = "https://fapi.binance.com";
    private static final String FUTURES_SANDBOX_URL = "https://demo-fapi.binance.com";//"https://testnet.binancefuture.com";

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

    /**
     * 根据市场类型获取基础URL
     *
     * @return 基础URL
     */
    public String getBaseUrlForMarketType() {
        return switch (marketType) {
            case SPOT -> getSpotBaseUrl();
            case FUTURES -> getFuturesBaseUrl();
            default -> getBaseUrl();
        };
    }

    // API端点方法
    public String getCommissionRateEndpoint() {
        return marketType == MarketType.SPOT ? "/api/v3/account/commission" : "/fapi/v1/commissionRate";
    }
    // 参数构建方法
    public String buildCommissionRateParams(String symbol) {
        return "symbol=" + symbol.replace("/", "") +
                //"&recvWindow=" + System.currentTimeMillis() +
                "&timestamp=" + System.currentTimeMillis();
    }

    public String getFuturesMarginTypeEndpoint() {
        if (marketType == MarketType.FUTURES) {
            return "/fapi/v1/marginType";
        }
        return null;
    }

    public String buildFuturesMarginTypeParams(String symbol, String marginType) {
        if (marketType == MarketType.FUTURES) {
            return "symbol=" + symbol.replace("/", "") +
                    "&marginType=" + marginType +
                    //"&recvWindow=" + System.currentTimeMillis() +
                    "&timestamp=" + System.currentTimeMillis();
        }
        return null;
    }

    public String getFuturesDualSidePositionEndpoint() {
        if (marketType == MarketType.FUTURES) {
            return "/fapi/v1/positionSide/dual";
        }
        return null;
    }

    public String buildFuturesDualSidePositionParams(boolean dualSidePosition) {
        if (marketType == MarketType.FUTURES && dualSidePosition) {
            return "dualSidePosition=true" +
                    "&timestamp=" + System.currentTimeMillis();
        } else {
            return "dualSidePosition=false" +
                    "&timestamp=" + System.currentTimeMillis();
        }
    }

    public String getFuturesLeverageEndpoint() {
        if (marketType == MarketType.FUTURES) {
            return "/fapi/v1/leverage";
        }
        return null;
    }

    public String buildFuturesLeverageParams(String symbol, int leverage, String mgnMode) {
        if (marketType == MarketType.FUTURES) {
            return "symbol=" + symbol.replace("/", "") +
                    "&leverage=" + leverage +
                    "&timestamp=" + System.currentTimeMillis();
        }
        return null;
    }
    
    public String getFuturesCreateOrderEndpoint() {
        if (marketType == MarketType.FUTURES) {
            return "/fapi/v1/order";
        }
        return null;
    }
    
    public String buildFuturesCreateLimitOrderParams(String symbol, String side, String positionSide, BigDecimal quantity, BigDecimal price, String clientOrderId) {
        if (marketType == MarketType.FUTURES) {
            StringBuilder params = new StringBuilder();
            params.append("symbol=").append(symbol.replace("/", ""))
                  .append("&side=").append(side)
                  .append("&positionSide=").append(positionSide)
                  .append("&type=LIMIT")
                  .append("&quantity=").append(quantity.toPlainString())
                  .append("&price=").append(price.toPlainString())
                  .append("&timeInForce=GTC")
                  .append("&timestamp=").append(System.currentTimeMillis());
            if (clientOrderId != null && !clientOrderId.isEmpty()) {
                params.append("&newClientOrderId=").append(clientOrderId);
            }
            return params.toString();
        }
        return null;
    }
    
    public String buildFuturesCreateMarketOrderParams(String symbol, String side, String positionSide, BigDecimal quantity, String clientOrderId) {
        if (marketType == MarketType.FUTURES) {
            StringBuilder params = new StringBuilder();
            params.append("symbol=").append(symbol.replace("/", ""))
                  .append("&side=").append(side)
                  .append("&positionSide=").append(positionSide)
                  .append("&type=MARKET")
                  .append("&quantity=").append(quantity.toPlainString())
                  .append("&timestamp=").append(System.currentTimeMillis());
            if (clientOrderId != null && !clientOrderId.isEmpty()) {
                params.append("&newClientOrderId=").append(clientOrderId);
            }
            return params.toString();
        }
        return null;
    }
    
    public String buildFuturesCreateTakeProfitOrderParams(String symbol, String side, String positionSide, BigDecimal quantity, BigDecimal stopPrice, BigDecimal price, String clientOrderId) {
        if (marketType == MarketType.FUTURES) {
            StringBuilder params = new StringBuilder();
            params.append("symbol=").append(symbol.replace("/", ""))
                  .append("&side=").append(side)
                  .append("&positionSide=").append(positionSide)
                  .append("&type=TAKE_PROFIT")
                  .append("&quantity=").append(quantity.toPlainString())
                  .append("&stopPrice=").append(stopPrice.toPlainString())
                  .append("&price=").append(price.toPlainString())
                  .append("&timeInForce=GTC")
                  .append("&timestamp=").append(System.currentTimeMillis());
            if (clientOrderId != null && !clientOrderId.isEmpty()) {
                params.append("&newClientOrderId=").append(clientOrderId);
            }
            return params.toString();
        }
        return null;
    }
    
    public String buildFuturesCreateStopLossOrderParams(String symbol, String side, String positionSide, BigDecimal quantity, BigDecimal stopPrice, BigDecimal price, String clientOrderId) {
        if (marketType == MarketType.FUTURES) {
            StringBuilder params = new StringBuilder();
            params.append("symbol=").append(symbol.replace("/", ""))
                  .append("&side=").append(side)
                  .append("&positionSide=").append(positionSide)
                  .append("&type=STOP")
                  .append("&quantity=").append(quantity.toPlainString())
                  .append("&stopPrice=").append(stopPrice.toPlainString())
                  .append("&price=").append(price.toPlainString())
                  .append("&timeInForce=GTC")
                  .append("&timestamp=").append(System.currentTimeMillis());
            if (clientOrderId != null && !clientOrderId.isEmpty()) {
                params.append("&newClientOrderId=").append(clientOrderId);
            }
            return params.toString();
        }
        return null;
    }
    
    public String getFuturesCancelOrderEndpoint() {
        if (marketType == MarketType.FUTURES) {
            return "/fapi/v1/order";
        }
        return null;
    }
    
    public String buildFuturesCancelOrderParams(String symbol, String orderId, String clientOrderId) {
        if (marketType == MarketType.FUTURES) {
            StringBuilder params = new StringBuilder();
            params.append("symbol=").append(symbol.replace("/", ""));
            if (orderId != null && !orderId.isEmpty()) {
                params.append("&orderId=").append(orderId);
            }
            if (clientOrderId != null && !clientOrderId.isEmpty()) {
                params.append("&origClientOrderId=").append(clientOrderId);
            }
            params.append("&timestamp=").append(System.currentTimeMillis());
            return params.toString();
        }
        return null;
    }
    
    public String getFuturesGetOrderEndpoint() {
        if (marketType == MarketType.FUTURES) {
            return "/fapi/v1/order";
        }
        return null;
    }
    
    public String buildFuturesGetOrderParams(String symbol, String orderId, String clientOrderId) {
        if (marketType == MarketType.FUTURES) {
            StringBuilder params = new StringBuilder();
            params.append("symbol=").append(symbol.replace("/", ""));
            if (orderId != null && !orderId.isEmpty()) {
                params.append("&orderId=").append(orderId);
            }
            if (clientOrderId != null && !clientOrderId.isEmpty()) {
                params.append("&origClientOrderId=").append(clientOrderId);
            }
            params.append("&timestamp=").append(System.currentTimeMillis());
            return params.toString();
        }
        return null;
    }
    
    public String getFuturesPositionsEndpoint() {
        if (marketType == MarketType.FUTURES) {
            return "/fapi/v2/positionRisk";
        }
        return null;
    }
    
    public String buildFuturesPositionsParams(String symbol) {
        if (marketType == MarketType.FUTURES) {
            StringBuilder params = new StringBuilder();
            if (symbol != null && !symbol.isEmpty()) {
                params.append("symbol=").append(symbol.replace("/", ""));
            }
            params.append("&timestamp=").append(System.currentTimeMillis());
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