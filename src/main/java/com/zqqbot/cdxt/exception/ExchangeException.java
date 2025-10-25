package com.zqqbot.cdxt.exception;

/**
 * 交易所异常类
 */
public class ExchangeException extends RuntimeException {
    
    public ExchangeException(String message) {
        super(message);
    }
    
    public ExchangeException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ExchangeException(Throwable cause) {
        super(cause);
    }
}