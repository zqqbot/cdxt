package com.zqqbot.cdxt.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 某交易对的成交历史
 */
@Data
@Builder
public class UserTrades {
    private String symbol;// 交易对
    private String id;// 交易ID
    private String orderId;// 订单编号
    private Boolean maker;// 是否是挂单方
    private Boolean buyer;// 是否是买方
    private BigDecimal price;// 成交价
    private BigDecimal qty;// 成交量
    private BigDecimal quoteQty;// 成交额
    private BigDecimal realizedPnl;// 实现盈亏
    private String side;// 买卖方向
    private String positionSide;// 持仓方向
    private BigDecimal commission;// 手续费
    private String commissionAsset;// 手续费计价单位
    private Long time;// 时间
}
