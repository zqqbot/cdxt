package com.zqqbot.cdxt.enums;

/**
 * 市场类型枚举
 * 用于区分现货和合约交易
 */
public enum MarketType {
    /**
     * 现货交易
     */
    SPOT,
    
    /**
     * 合约交易
     */
    FUTURES
}