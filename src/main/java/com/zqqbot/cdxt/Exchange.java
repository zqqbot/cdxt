package com.zqqbot.cdxt;

import com.zqqbot.cdxt.dto.CommissionRate;
import com.zqqbot.cdxt.dto.Order;
import com.zqqbot.cdxt.dto.Position;
import com.zqqbot.cdxt.dto.UserTrades;
import com.zqqbot.cdxt.enums.MarketType;

import java.math.BigDecimal;
import java.util.List;

/**
 * 交易所接口定义
 * 所有交易所实现都需要遵循此接口规范
 */
public interface Exchange {
    
    /**
     * 获取交易所名称
     * @return 交易所名称
     */
    String getName();
    
    /**
     * 设置API密钥
     * @param apiKey API Key
     * @param secret Secret Key
     * @param passphrase 密码（部分交易所需要）
     */
    void setApiKey(String apiKey, String secret, String passphrase);
    
    /**
     * 设置是否使用沙盒环境
     * @param sandbox 是否使用沙盒环境
     */
    void setSandbox(boolean sandbox);
    
    /**
     * 设置市场类型
     * @param marketType 市场类型（现货或合约）
     */
    void setMarketType(MarketType marketType);
    
    /**
     * 获取交易所手续费率
     * @param symbol 交易对
     * @return 手续费率
     */
    CommissionRate getCommissionRate(String symbol);

    /**
     * 设置合约逐仓或全仓模式
     * @param symbol 交易对
     * @param marginType 保证金模式 ISOLATED(逐仓), CROSSED(全仓)
     * @return 是否设置成功
     */
    Boolean setFuturesMarginType(String symbol, String marginType);

    /**
     * 设置合约双向持仓或单向持仓模式
     * @param dualSidePosition true: 双向持仓, false: 单向持仓
     * @return 是否设置成功
     */
    Boolean setFuturesDualSidePosition(boolean dualSidePosition);

    /**
     * 设置合约杠杆倍数
     * @param symbol 交易对
     * @param leverage 杠杆倍数
     * @param mgnMode 保证金模式 isolated(逐仓), cross(全仓), null表示使用交易所默认模式
     * @return 是否设置成功
     */
    Boolean setFuturesLeverage(String symbol, int leverage, String mgnMode);
    
    /**
     * 创建合约限价单
     * @param symbol 交易对
     * @param side 订单方向 (BUY: 买入, SELL: 卖出)
     * @param positionSide 持仓方向 (LONG: 多头, SHORT: 空头)
     * @param quantity 订单数量
     * @param price 订单价格
     * @param clientOrderId 客户自定义订单ID
     * @return 订单信息
     */
    Order createFuturesLimitOrder(String symbol, String side, String positionSide, BigDecimal quantity, BigDecimal price, String clientOrderId);
    
    /**
     * 创建合约市价单
     * @param symbol 交易对
     * @param side 订单方向 (BUY: 买入, SELL: 卖出)
     * @param positionSide 持仓方向 (LONG: 多头, SHORT: 空头)
     * @param quantity 订单数量
     * @param clientOrderId 客户自定义订单ID
     * @return 订单信息
     */
    Order createFuturesMarketOrder(String symbol, String side, String positionSide, BigDecimal quantity, String clientOrderId);
    
    /**
     * 创建合约止盈订单
     * @param symbol 交易对
     * @param side 订单方向 (BUY: 买入, SELL: 卖出)
     * @param positionSide 持仓方向 (LONG: 多头, SHORT: 空头)
     * @param quantity 订单数量
     * @param stopPrice 止盈价格
     * @param price 订单价格
     * @param clientOrderId 客户自定义订单ID
     * @return 订单信息
     */
    Order createFuturesTakeProfitOrder(String symbol, String side, String positionSide, BigDecimal quantity, BigDecimal stopPrice, BigDecimal price, String clientOrderId);
    
    /**
     * 创建合约止损订单
     * @param symbol 交易对
     * @param side 订单方向 (BUY: 买入, SELL: 卖出)
     * @param positionSide 持仓方向 (LONG: 多头, SHORT: 空头)
     * @param quantity 订单数量
     * @param stopPrice 止损价格
     * @param price 订单价格
     * @param clientOrderId 客户自定义订单ID
     * @return 订单信息
     */
    Order createFuturesStopLossOrder(String symbol, String side, String positionSide, BigDecimal quantity, BigDecimal stopPrice, BigDecimal price, String clientOrderId);
    
    /**
     * 取消合约订单
     * @param symbol 交易对
     * @param orderId 订单ID
     * @param clientOrderId 客户自定义订单ID
     * @return 订单信息
     */
    Order cancelFuturesOrder(String symbol, String orderId, String clientOrderId);
    
    /**
     * 查询合约订单
     * @param symbol 交易对
     * @param orderId 订单ID
     * @param clientOrderId 客户自定义订单ID
     * @return 订单信息
     */
    Order getFuturesOrder(String symbol, String orderId, String clientOrderId);

    /**
     * 获取指定交易对和订单ID的合约用户成交记录。
     *
     * @param symbol 交易对
     * @param orderId 订单ID
     * @param startTime 开始时间（可选，单位：毫秒）
     * @param endTime 结束时间（可选，单位：毫秒）
     * @return 指定交易对和订单ID下的用户成交历史记录
     */
    List<UserTrades> getFuturesUserTrades(String symbol, String orderId, Long startTime, Long endTime);
    
    /**
     * 查询合约持仓
     * @param symbol 交易对
     * @return 持仓信息列表
     */
    List<Position> getFuturesPositions(String symbol);
}