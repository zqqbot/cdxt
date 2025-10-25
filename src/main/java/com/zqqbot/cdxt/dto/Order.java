package com.zqqbot.cdxt.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 订单信息DTO
 * 用于封装交易所订单的相关信息
 */
@Data
@Builder
public class Order {

    /**
     * 客户自定义订单ID
     */
    private String clientOrderId;

    /**
     * 订单ID
     */
    private String orderId;
    
    /**
     * 交易对
     */
    private String symbol;
    
    /**
     * 订单类型 (LIMIT: 限价单, MARKET: 市价单, STOP_LOSS: 止损单, TAKE_PROFIT: 止盈单)
     */
    private String orderType;
    
    /**
     * 订单方向 (BUY: 买入, SELL: 卖出)
     */
    private String side;

    /**
     * 持仓方向，用于指定订单是针对多头还是空头持仓进行操作。
     * 可能的值包括：
     * - LONG: 该订单仅影响多头持仓
     * - SHORT: 该订单仅影响空头持仓
     * - BOTH: 该订单同时影响多头和空头持仓
     * 此属性主要用于在双向持仓模式下区分订单对哪种类型的持仓生效。
     */
    private String positionSide;
    
    /**
     * 订单状态 (NEW: 新订单, PARTIALLY_FILLED: 部分成交, FILLED: 完全成交, CANCELED: 已取消, REJECTED: 已拒绝, EXPIRED: 已过期)
     */
    private String status;
    
    /**
     * 订单价格
     */
    private BigDecimal price;

    /**
     * 平均成交价格
     */
    private BigDecimal avgPrice;

    /**
     * 止损价格或止盈价格。对于止损单（STOP_LOSS）和止盈单（TAKE_PROFIT），当市场价格达到此价格时，订单将被激活并按照指定的订单类型执行。
     */
    private BigDecimal stopPrice;

    /**
     * 原始订单数量，表示创建订单时指定的交易数量。
     * 该值用于记录订单最初请求的交易量，可以与当前订单数量(quantity)对比，
     * 以了解订单执行过程中是否进行了调整。
     */
    private BigDecimal origQty;

    /**
     * 表示该订单已经被执行的数量。此值反映了从订单创建到当前时间点为止，实际成交的资产数量。
     * 请注意，executedQty可能小于或等于原始订单数量(origQty)，具体取决于订单的执行情况。
     */
    private BigDecimal executedQty;

    /**
     * 累计成交金额，表示该订单到目前为止已经成交的总金额。
     * 这个值是通过将每次成交的数量乘以成交价格然后累加得到的。
     */
    private BigDecimal cumQuote;

    /**
     * 累计成交数量，表示该订单到目前为止已经成交的总数量。
     */
    private BigDecimal cumQty;

    /**
     * 订单有效类型，定义了订单在市场中的持续时间或条件。
     * 可能的值包括但不限于:
     * - GTC (Good Till Canceled): 一直有效直到被取消
     * - IOC (Immediate or Cancel): 立即成交剩余部分自动取消
     * - FOK (Fill or Kill): 全部立即成交否则全部取消
     * - GTX (Good Till Crossing): 不接受以低于当前市场价格成交
     */
    private String timeInForce;

    /**
     * 触发价格，主要用于止损单和止盈单。
     * 当市场价格达到该触发价格时，订单将被激活并按照指定的订单类型执行。
     */
    private Boolean reduceOnly;

    /**
     * 是否为止损止盈单
     */
    private Boolean closePosition;

    /**
     * 订单创建时间
     */
    private Long createTime;
    
    /**
     * 订单更新时间
     */
    private Long updateTime;
}