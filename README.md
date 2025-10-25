# CDXT - Crypto Exchange Trading SDK

CDXT是一个类似于CCXT的加密货币交易所交易SDK，支持多个交易所的现货和合约交易。

## 支持的交易所

- Binance (现货 & 合约)
- OKX (现货 & 合约)

## 安装

在你的Maven项目中添加依赖：

```xml
<dependency>
    <groupId>com.zqqbot</groupId>
    <artifactId>cdxt</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 使用示例

### 1. 创建交易所实例

```java
import com.zqqbot.cdxt.Exchange;
import com.zqqbot.cdxt.ExchangeFactory;
import com.zqqbot.cdxt.enums.Environment;
import com.zqqbot.cdxt.enums.MarketType;

// 创建Binance现货交易所实例（一行代码，默认使用生产环境）
Exchange binanceSpot = ExchangeFactory.createExchange(
    ExchangeFactory.BINANCE, 
    "your-api-key", 
    "your-secret", 
    "your-passphrase"
    // 默认使用生产环境Environment.RELEASE和现货交易MarketType.SPOT
);

// 创建OKX合约交易所实例（一行代码，指定沙盒环境和合约交易）
Exchange okxFutures = ExchangeFactory.createExchange(
    ExchangeFactory.OKX, 
    "your-api-key", 
    "your-secret", 
    "your-passphrase", 
    Environment.SANDBOX,  // 沙盒环境
    MarketType.FUTURES    // 合约交易
);

// 明确指定生产环境和现货交易
Exchange binanceProdSpot = ExchangeFactory.createExchange(
    ExchangeFactory.BINANCE, 
    "your-api-key", 
    "your-secret", 
    "your-passphrase", 
    Environment.RELEASE,  // 生产环境
    MarketType.SPOT       // 现货交易
);
```

### 2. 获取行情数据

```java


// 获取ticker
Ticker ticker = binanceSpot.fetchTicker("BTC/USDT");
System.out.

        println("最新价格: "+ticker.getLastPrice());

        // 获取订单簿
        OrderBook orderBook = binanceSpot.fetchOrderBook("BTC/USDT", 10);
System.out.

        println("买盘数量: "+orderBook.getBids().

        size());
        System.out.

        println("卖盘数量: "+orderBook.getAsks().

        size());
```

### 3. 账户操作

```java

import java.util.List;

// 获取账户余额
List<Balance> balances = binanceSpot.fetchBalance();
for(
        Balance balance :balances){
        System.out.

        println(balance.getCurrency() +": "+balance.

        getTotal());
        }
```

### 4. 订单操作

```java


// 创建订单
Order order = binanceSpot.createOrder("BTC/USDT", "limit", "buy", 0.001, 50000);

        // 查询订单
        Order fetchedOrder = binanceSpot.fetchOrder(order.getId(), "BTC/USDT");
System.out.

        println("订单状态: "+fetchedOrder.getStatus());

        // 取消订单
        Order canceledOrder = binanceSpot.cancelOrder(order.getId(), "BTC/USDT");
System.out.

        println("取消状态: "+canceledOrder.getStatus());

        // 查询订单列表
        List<Order> orders = binanceSpot.fetchOrders("BTC/USDT", 10);
System.out.

        println("订单数量: "+orders.size());
```

### 5. 合约交易

```java

import java.util.List;

// 获取持仓
Position position = okxFutures.fetchPosition("BTC/USDT");
System.out.

        println("持仓方向: "+position.getSide());
        System.out.

        println("持仓数量: "+position.getAmount());

        // 获取所有持仓
        List<Position> positions = okxFutures.fetchPositions();
for(
        Position pos :positions){
        System.out.

        println(pos.getSymbol() +": "+pos.

        getSide() +" "+pos.

        getAmount());
        }
```

## 异常处理

```java
import com.zqqbot.cdxt.exception.ExchangeException;

try {
    Ticker ticker = binanceSpot.fetchTicker("INVALID_SYMBOL");
} catch (ExchangeException e) {
    System.err.println("交易所错误: " + e.getMessage());
}
```

## 开发指南

### 添加新的交易所

1. 在`com.zqqbot.cdxt.exchange`包下创建新的交易所子包
2. 继承[BaseExchange](file:///Users/Elson/IdeaProjects/zqq-labs-cdxt/src/main/java/com/zqqbot/cdxt/BaseExchange.java#L18-L213)类并实现具体的方法
3. 在[ExchangeFactory](file:///Users/Elson/IdeaProjects/zqq-labs-cdxt/src/main/java/com/zqqbot/cdxt/ExchangeFactory.java#L12-L91)中注册新的交易所

### 项目结构

```
src/main/java/com/zqqbot/cdxt/
├── Exchange.java              # 交易所接口
├── BaseExchange.java          # 交易所基础实现
├── ExchangeFactory.java       # 交易所工厂类
├── enums/
│   ├── Environment.java       # 环境枚举
│   └── MarketType.java        # 市场类型枚举
├── http/
│   └── HttpClient.java        # HTTP客户端工具类
├── exception/
│   └── ExchangeException.java # 交易所异常类
├── dto/
│   ├── Ticker.java            # 行情数据
│   ├── OrderBook.java         # 订单簿
│   ├── Balance.java           # 账户余额
│   ├── Order.java             # 订单
│   └── Position.java          # 持仓
└── exchange/
    ├── binance/
    │   └── BinanceExchange.java # Binance实现
    └── okx/
        └── OkxExchange.java     # OKX实现
```

## License

MIT