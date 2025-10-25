package com.zqqbot.cdxt;

import com.zqqbot.cdxt.enums.Environment;
import com.zqqbot.cdxt.enums.MarketType;

/**
 * 交易所工厂类
 * 用于创建不同的交易所实例
 */
public class ExchangeFactory {
    
    public static final String BINANCE = "binance";
    public static final String OKX = "okx";
    
    /**
     * 根据交易所名称创建对应的交易所实例
     * @param exchangeName 交易所名称
     * @return 交易所实例
     */
    public static Exchange createExchange(String exchangeName) {
        return switch (exchangeName.toLowerCase()) {
            case BINANCE -> createBinanceExchange();
            case OKX -> createOkxExchange();
            default -> throw new IllegalArgumentException("Unsupported exchange: " + exchangeName);
        };
    }
    
    /**
     * 根据交易所名称和API密钥创建对应的交易所实例（默认使用生产环境和现货交易）
     * @param exchangeName 交易所名称
     * @param apiKey API Key
     * @param secret Secret Key
     * @param passphrase 密码（部分交易所需要）
     * @return 交易所实例
     */
    public static Exchange createExchange(String exchangeName, String apiKey, String secret, String passphrase) {
        return createExchange(exchangeName, apiKey, secret, passphrase, Environment.RELEASE, MarketType.SPOT);
    }
    
    /**
     * 根据交易所名称、API密钥和环境设置创建对应的交易所实例（默认使用现货交易）
     * @param exchangeName 交易所名称
     * @param apiKey API Key
     * @param secret Secret Key
     * @param passphrase 密码（部分交易所需要）
     * @param environment 运行环境
     * @return 交易所实例
     */
    public static Exchange createExchange(String exchangeName, String apiKey, String secret, String passphrase, Environment environment) {
        return createExchange(exchangeName, apiKey, secret, passphrase, environment, MarketType.SPOT);
    }
    
    /**
     * 根据交易所名称、API密钥、环境设置和市场类型创建对应的交易所实例
     * @param exchangeName 交易所名称
     * @param apiKey API Key
     * @param secret Secret Key
     * @param passphrase 密码（部分交易所需要）
     * @param environment 运行环境
     * @param marketType 市场类型
     * @return 交易所实例
     */
    public static Exchange createExchange(String exchangeName, String apiKey, String secret, String passphrase, Environment environment, MarketType marketType) {
        Exchange exchange = createExchange(exchangeName);
        exchange.setApiKey(apiKey, secret, passphrase);
        exchange.setSandbox(environment == Environment.SANDBOX);
        if (exchange instanceof BaseExchange) {
            exchange.setMarketType(marketType);
        }
        return exchange;
    }
    
    /**
     * 创建Binance交易所实例
     * @return Binance交易所实例
     */
    private static Exchange createBinanceExchange() {
        try {
            Class<?> clazz = Class.forName("com.zqqbot.cdxt.exchange.binance.BinanceExchange");
            return (Exchange) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Binance exchange instance", e);
        }
    }
    
    /**
     * 创建OKX交易所实例
     * @return OKX交易所实例
     */
    private static Exchange createOkxExchange() {
        try {
            Class<?> clazz = Class.forName("com.zqqbot.cdxt.exchange.okx.OkxExchange");
            return (Exchange) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create OKX exchange instance", e);
        }
    }
}