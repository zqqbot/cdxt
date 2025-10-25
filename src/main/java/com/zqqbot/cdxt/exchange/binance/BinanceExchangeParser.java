package com.zqqbot.cdxt.exchange.binance;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import com.zqqbot.cdxt.dto.CommissionRate;
import com.zqqbot.cdxt.dto.Order;
import com.zqqbot.cdxt.dto.Position;
import com.zqqbot.cdxt.dto.UserTrades;
import com.zqqbot.cdxt.dto.futures.FuturesCommissionRate;
import com.zqqbot.cdxt.dto.spot.SpotCommissionRate;
import com.zqqbot.cdxt.enums.MarketType;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Binance交易所响应解析器
 * 负责解析Binance交易所API返回的响应数据
 */
@Data
public class BinanceExchangeParser {
    
    private MarketType marketType;
    
    public BinanceExchangeParser(MarketType marketType) {
        this.marketType = marketType;
    }
    
    /**
     * 解析手续费响应
     * @param response 响应数据
     * @return CommissionRate对象
     */
    public CommissionRate parseCommissionRateResponse(String response) {
        StaticLog.info("解析 Binance 交易所 API 返回的手续费响应数据：{}", response);
        // 实际应该解析JSON响应
        JSONObject json = JSONUtil.parseObj(response);
        // 正常解析
        CommissionRate commissionRate = createCommissionRateInstance();
        String symbol = json.getStr("symbol");
        if (symbol != null) {
            commissionRate.setSymbol(symbol.replace("USDT", "/USDT"));
        } else {
            commissionRate.setSymbol("BTC/USDT"); // 默认值
        }
        commissionRate.setExchangeName("binance");
        // 根据市场类型设置特有字段
        if (commissionRate instanceof SpotCommissionRate sc) {
            JSONObject standardCommission = json.getJSONObject("standardCommission");
            if (standardCommission != null) {
                String makerStr = standardCommission.getStr("maker");
                String takerStr = standardCommission.getStr("taker");
                BigDecimal makerRate = makerStr != null ? new BigDecimal(makerStr).multiply(new BigDecimal("100")).setScale(3, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                BigDecimal takerRate = takerStr != null ? new BigDecimal(takerStr).multiply(new BigDecimal("100")).setScale(3, RoundingMode.HALF_UP) : BigDecimal.valueOf(0.1);
                sc.setSpotMakerRate(makerRate);
                sc.setSpotTakerRate(takerRate);
            } else {
                sc.setSpotMakerRate(BigDecimal.ZERO);
                sc.setSpotTakerRate(BigDecimal.valueOf(0.1));
            }
        } else if (commissionRate instanceof FuturesCommissionRate fc) {
            String makerStr = json.getStr("makerCommissionRate");
            String takerStr = json.getStr("takerCommissionRate");
            BigDecimal makerRate = makerStr != null ? new BigDecimal(makerStr).multiply(new BigDecimal("100")).setScale(3, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            BigDecimal takerRate = takerStr != null ? new BigDecimal(takerStr).multiply(new BigDecimal("100")).setScale(3, RoundingMode.HALF_UP) : BigDecimal.valueOf(0.02);
            fc.setFuturesMakerRate(makerRate);
            fc.setFuturesTakerRate(takerRate);
        }
        return commissionRate;
    }
    
    /**
     * 根据市场类型创建相应的CommissionRate对象
     * @return CommissionRate对象
     */
    private CommissionRate createCommissionRateInstance() {
        return switch (marketType) {
            case SPOT -> new SpotCommissionRate();
            case FUTURES -> new FuturesCommissionRate();
        };
    }

    public Boolean parseFuturesMarginTypeResponse(String response) {
        StaticLog.info("解析 Binance 交易所 API 更改全仓或逐仓响应数据：{}", response);
        // 实际应该解析JSON响应
        JSONObject json = JSONUtil.parseObj(response);
        // 正常解析
        String code = json.getStr("code");
        String msg = json.getStr("msg");
        return code.equals("200") || code.equals("-4059") || code.equals("-4046");
    }

    public Boolean parseFuturesDualSidePositionResponse(String response) {
        StaticLog.info("解析 Binance 交易所 API 更改持仓模式响应数据：{}", response);
        // 实际应该解析JSON响应
        JSONObject json = JSONUtil.parseObj(response);
        // 正常解析
        String code = json.getStr("code");
        String msg = json.getStr("msg");
        return code.equals("200") || code.equals("-4059") || code.equals("-4046");
    }

    public Boolean parseFuturesLeverageResponse(String response) {
        StaticLog.info("解析 Binance 交易所 API 更改杠杆倍数响应数据：{}", response);
        // 实际应该解析JSON响应
        JSONObject json = JSONUtil.parseObj(response);
        // 正常解析
        int leverage = json.getInt("leverage");
        return leverage > 0;
    }
    
    public Order parseFuturesCreateOrderResponse(String response) {
        StaticLog.info("解析 Binance 交易所 API 创建订单响应数据：{}", response);
        // 实际应该解析JSON响应
        JSONObject json = JSONUtil.parseObj(response);
        // 检查是否存在 code 字段，存在则代表失败
        if (json.containsKey("code")) {
            StaticLog.warn("创建订单失败，错误代码：{}，错误信息：{}", json.getInt("code"), json.getStr("msg"));
            return null;
        }
        // 直接转换为 Order 对象
        return json.toBean(Order.class);
    }
    
    public Order parseFuturesCancelOrderResponse(String response) {
        StaticLog.info("解析 Binance 交易所 API 取消订单响应数据：{}", response);
        // 实际应该解析JSON响应
        JSONObject json = JSONUtil.parseObj(response);
        // 检查是否存在 code 字段，存在则代表取消失败
        if (json.containsKey("code")) {
            StaticLog.warn("取消订单失败，错误代码：{}，错误信息：{}", json.getInt("code"), json.getStr("msg"));
            return null;
        }

        return json.toBean(Order.class);
    }
    
    public Order parseFuturesGetOrderResponse(String response) {
        StaticLog.info("解析 Binance 交易所 API 查询订单响应数据：{}", response);
        // 实际应该解析JSON响应
        JSONObject json = JSONUtil.parseObj(response);
        // 检查是否存在 code 字段，存在则代表失败
        if (json.containsKey("code")) {
            StaticLog.warn("查询订单失败，错误代码：{}，错误信息：{}", json.getInt("code"), json.getStr("msg"));
            return null;
        }

        return json.toBean(Order.class);
    }
    
    public List<Position> parseFuturesPositionsResponse(String response) {
        StaticLog.info("解析 Binance 交易所 API 查询持仓响应数据：{}", response);
        // 实际应该解析JSON响应
        JSONArray json = JSONUtil.parseArray(response);
        List<Position> positions = new ArrayList<>();
        for (int i = 0; i < json.size(); i++) {
            JSONObject pos = json.getJSONObject(i);

            positions.add(Position.builder()
                    .symbol(pos.getStr("symbol").replace("USDT", "/USDT"))
                    .positionAmt(new BigDecimal(pos.getStr("positionAmt")))
                    .entryPrice(new BigDecimal(pos.getStr("entryPrice")))
                    .breakEvenPrice(new BigDecimal(pos.getStr("liquidationPrice")))
                    .markPrice(new BigDecimal(pos.getStr("markPrice")))
                    .unRealizedProfit(new BigDecimal(pos.getStr("unRealizedProfit")))
                    .liquidationPrice(new BigDecimal(pos.getStr("liquidationPrice")))
                    .leverage(pos.getStr("leverage"))
                    .maxNotionalValue(pos.getStr("maxNotionalValue"))
                    .marginType(pos.getStr("marginType"))
                    .isolatedMargin(new BigDecimal(pos.getStr("isolatedMargin")))
                    .positionSide(pos.getStr("positionSide"))
                    .notional(new BigDecimal(pos.getStr("notional")))
                    .isolated(pos.getBool("isolated"))
                    .updateTime(pos.getLong("updateTime"))
                    .build());
        }
        // 正常解析
        return positions;
    }

    public List<UserTrades> parseFuturesUserTradesResponse(String response) {
        StaticLog.info("解析 Binance 交易所 API 查询用户成交记录响应数据：{}", response);
        // 实际应该解析JSON响应
        JSONArray json = JSONUtil.parseArray(response);
        List<UserTrades> trades = new ArrayList<>();
        for (int i = 0; i < json.size(); i++) {
            JSONObject object = json.getJSONObject(i);
            UserTrades trade = object.toBean(UserTrades.class);
            trades.add(trade);
        }
        return trades;
    }
}