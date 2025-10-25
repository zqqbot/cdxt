package com.zqqbot.cdxt.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 持仓信息DTO
 * 用于封装交易所持仓的相关信息
 */
@Data
@Builder
public class Position {
    /** 交易对 */
    private String symbol;
    /** 开仓数量 */
    private BigDecimal positionAmt;
    /** 开仓均价 */
    private BigDecimal entryPrice;
    /** 盈亏平衡价 */
    private BigDecimal breakEvenPrice;
    /** 当前标记价格 */
    private BigDecimal markPrice;
    /** 持仓未实现盈亏 */
    private BigDecimal unRealizedProfit;
    /** 参考强平价格 */
    private BigDecimal liquidationPrice;
    /** 当前杠杆倍数 */
    private String leverage;
    /** 当前杠杆倍数允许的名义价值上限 */
    private String maxNotionalValue;
    /** 逐仓模式或全仓模式 */
    private String marginType;
    /** 逐仓保证金 */
    private BigDecimal isolatedMargin;
    /** 持仓方向 */
    private String positionSide;
    /** 持仓名义价值 */
    private BigDecimal notional;
    /** 持仓初始保证金 */
    private Long  updateTime;
    /** 是否是逐仓模式 */
    private Boolean isolated;
}