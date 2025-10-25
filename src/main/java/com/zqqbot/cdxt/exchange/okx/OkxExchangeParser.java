package com.zqqbot.cdxt.exchange.okx;

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
 * OKX交易所响应解析器
 * 负责解析OKX交易所API返回的响应数据
 */
@Data
public class OkxExchangeParser {
    
    private MarketType marketType;
    
    public OkxExchangeParser(MarketType marketType) {
        this.marketType = marketType;
    }

    /**
     * 解析行情响应
     * @param response 响应数据
     * @return CommissionRate对象
     */
    public CommissionRate parseCommissionRateResponse(String response) {
        StaticLog.info("解析 okx 交易所 API 返回的行情响应数据：{}", response);
        // 实际应该解析JSON响应
        JSONObject json = JSONUtil.parseObj(response);
        if (!"0".equals(json.getStr("code"))) {
            StaticLog.error("OKX API 返回错误: {}", json.getStr("msg"));
            return null;
        }
        // 正常解析
        CommissionRate commissionRate = createCommissionRateInstance();
        commissionRate.setSymbol("BTC/USDT");
        commissionRate.setExchangeName("okx");

        // 根据市场类型设置特有字段
        // 备注：手续费率的值（如 maker/taker）：正数，代表是返佣的费率；负数，代表平台扣除的费率。
        if (commissionRate instanceof SpotCommissionRate sc) {
            json.getJSONArray("data").forEach(item -> {
                JSONObject obj = (JSONObject) item;
                String makerStr = obj.getStr("maker");
                String takerStr = obj.getStr("taker");
                BigDecimal makerRate = new BigDecimal(makerStr).multiply(new BigDecimal("100")).setScale(3, RoundingMode.HALF_UP);
                BigDecimal takerRate = new BigDecimal(takerStr).multiply(new BigDecimal("100")).setScale(3, RoundingMode.HALF_UP);
                sc.setSpotMakerRate(makerRate.abs());
                sc.setSpotTakerRate(takerRate.abs());
            });
        } else if (commissionRate instanceof FuturesCommissionRate fc) {
            json.getJSONArray("data").forEach(item -> {
                JSONObject obj = (JSONObject) item;
                String makerStr = obj.getStr("makerU");
                String takerStr = obj.getStr("takerU");
                BigDecimal makerRate = new BigDecimal(makerStr).multiply(new BigDecimal("100")).setScale(3, RoundingMode.HALF_UP);
                BigDecimal takerRate = new BigDecimal(takerStr).multiply(new BigDecimal("100")).setScale(3, RoundingMode.HALF_UP);
                fc.setFuturesMakerRate(makerRate.abs());
                fc.setFuturesTakerRate(takerRate.abs());
            });
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

    public Boolean parseFuturesDualSidePositionResponse(String response) {
        StaticLog.info("解析 okx 交易所 API 更改持仓模式响应数据：{}", response);
        // 实际应该解析JSON响应
        JSONObject json = JSONUtil.parseObj(response);
        if (!"0".equals(json.getStr("code"))) {
            StaticLog.error("OKX API 返回错误: {}", json.getStr("msg"));
            return null;
        }
        // 正常解析
        return true;
    }

    public Boolean parseFuturesLeverageResponse(String response) {
        StaticLog.info("解析 okx 交易所 API 改变杠杆倍数响应数据：{}", response);
        // 实际应该解析JSON响应
        JSONObject json = JSONUtil.parseObj(response);
        if (!"0".equals(json.getStr("code"))) {
            StaticLog.error("OKX API 返回错误: {}", json.getStr("msg"));
            return null;
            // 错误码 -4059 错误信息: 账户已开启多空持仓模式，请先关闭多空持仓模式再进行操作。
        }
        // 正常解析
        return true;
    }
    
    public Order parseFuturesCreateOrderResponse(String response) {
        StaticLog.info("解析 okx 交易所 API 创建订单响应数据：{}", response);
        // 实际应该解析JSON响应
        JSONObject json = JSONUtil.parseObj(response);
        if (!"0".equals(json.getStr("code"))) {
            StaticLog.error("OKX API 返回错误: {}", json.getStr("msg"));
            return null;
        }
        // 解析订单数据
        return json.toBean(Order.class);
    }
    
    public Order parseFuturesCancelOrderResponse(String response) {
        StaticLog.info("解析 okx 交易所 API 取消订单响应数据：{}", response);
        // 实际应该解析JSON响应
        JSONObject json = JSONUtil.parseObj(response);
        if (!"0".equals(json.getStr("code"))) {
            StaticLog.error("OKX API 返回错误: {}", json.getStr("msg"));
            return null;
        }
        // 解析订单数据
        return json.toBean(Order.class);
    }
    
    public Order parseFuturesGetOrderResponse(String response) {
        StaticLog.info("解析 okx 交易所 API 查询订单响应数据：{}", response);
        // 实际应该解析JSON响应
        JSONObject json = JSONUtil.parseObj(response);
        if (!"0".equals(json.getStr("code"))) {
            StaticLog.error("OKX API 返回错误: {}", json.getStr("msg"));
            return null;
        }
        // 解析订单数据
        return json.toBean(Order.class);
    }
    
    public List<Position> parseFuturesPositionsResponse(String response) {
        StaticLog.info("解析 okx 交易所 API 查询持仓响应数据：{}", response);
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
        // 解析持仓数据
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