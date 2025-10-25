package com.zqqbot.cdxt.dto.spot;

import com.zqqbot.cdxt.dto.CommissionRate;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 现货交易手续费率信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SpotCommissionRate extends CommissionRate {
    // 特有字段可以在这里添加
    private BigDecimal spotMakerRate;
    private BigDecimal spotTakerRate;
}