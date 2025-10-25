package com.zqqbot.cdxt.dto.futures;

import com.zqqbot.cdxt.dto.CommissionRate;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 合约交易手续费率信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FuturesCommissionRate extends CommissionRate {
    // 特有字段可以在这里添加
    private BigDecimal futuresMakerRate;
    private BigDecimal futuresTakerRate;
}