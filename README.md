# Irelia

基于 netty 实现的 api 网关

## 1. 各模块介绍

- irelia-core: 核心模块，存放一些抽象类和接口

- irelia-server: server 模块，接收请求，组装 pipeline，执行 handler 的模块。

- irelia-spi-core: spi 核心模块，提供 spi 的基本方法，如提取 IreliaBean、methodInfo 等。

- irelia-spi-dubbo: 为 dubbo 项目提供的快速接入包。

- Irelia-upstream: upstream 核心模块，提供了 upstream 的核心接口，以及管理类。

- Irelia-upstream-dubbo: dubbo 项目的 upstream 实现。

## 2. 使用方式

### 2.1 server 的配置

1. 继承 `AbstractConfigHandler` 抽象类，实现其中的抽象接口。该类的作用是获取每一个请求中 rpc 的配置，配置可以选择保存在数据库中。

```java
@Component
public class PlacidiumConfigHandler extends AbstractConfigHandler {

    @Autowired
    private RpcInfoService rpcInfoService;

    @Autowired
    private SystemInfoService systemInfoService;

    @Override
    public RpcConfig getRpcConfig(String rpcValue) {
        return rpcInfoService.getByRpcValue(rpcValue).getRpcConfig();
    }

    @Override
    public UpstreamConfig getUpstreamConfig(String sysName) {
        SystemInfo systemInfo = systemInfoService.getByName(sysName);
        DubboUpstreamConfig upstreamConfig = new DubboUpstreamConfig();
        upstreamConfig.setUsername("cf");
        upstreamConfig.setPassword("123");
        upstreamConfig.setAppName(sysName);
        upstreamConfig.setName(sysName);
        upstreamConfig.setAddress(systemInfo.getRegisterUrl());
        return upstreamConfig;
    }

}

```

2. 配置 `Bootstrap` 以及需要的 `Handler`。Bootstrap 需要设置启动的端口。Handler 的具体信息在下一节。

示例配置:

```java
@Configuration
public class IreliaConfig {
    // 使用 http 服务
    @Bean
    public HttpInboundHandler httpInboundHandler() {
        return new HttpInboundHandler();
    }

    // 添加默认路由组件
    @Bean
    public RouteHandler routeHandler() {
        return new RouteHandler();
    }

    // 添加默认身份验证组件
    @Bean
    public SecurityHandler securityHandler() {
        return new SecurityHandler();
    }

    // 启动网关
    @Bean(initMethod = "start")
    public Bootstrap bootstrap() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.setPort(9876);
        return bootstrap;
    }
}
```
### 2.2 handler 的配置

> 网关端配置



引入 pom

```java
<dependency>
    <groupId>cn.fanhub</groupId>
    <artifactId>irelia-server</artifactId>
    <version>0.0.1</version>
</dependency>
```


将 handler 配置为 spring bean 即可。

必须配置的 handler ：

- HttpInboundHandler: 接收 http 请求的组件（如果想使用 tcp，则需要自己编写 Handler）。

- AbstractConfigHandler: 构造网关运行所需要的必要数据，需实现该抽象类。

- RouteHandler: 路由组件


可选的 handler

- SecurityHandler: 安全认证的组件

- ErrorHandler: 统一的异常处理组件


### 2.3 upstream 的配置

> 网关端配置

引入 pom

```java
<dependency>
    <groupId>cn.fanhub</groupId>
    <artifactId>Irelia-upstream-dubbo</artifactId>
    <version>0.0.1</version>
</dependency>
<dependency>
    <groupId>cn.fanhub</groupId>
    <artifactId>Irelia-spi-dubbo</artifactId>
    <version>0.0.1</version>
</dependency>
```

在 `classpath` 下，新建文件: `irelia.upstream`

填写支持的 `upstream`

```java
cn.fanhub.irelia.upstream.dubbo.DubboUpstream
```

> Irelia 只提供了 dubbo upstream，其他 upstream 需要自行实现，多个 upstream 用 `,` 分隔。

这样，网关就配置好了。

### 2.4 spi 的使用

> 业务方配置

引入 pom: 

```java
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>dubbo</artifactId>
        <version>${dubbo.version}</version>
    </dependency>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
    </dependency>
    <dependency>
        <groupId>cn.fanhub</groupId>
        <artifactId>Irelia-spi-dubbo</artifactId>
        <version>0.0.1</version>
    </dependency>
    <dependency>
        <groupId>cn.fanhub</groupId>
        <artifactId>Irelia-upstream-dubbo</artifactId>
        <version>0.0.1</version>
    </dependency>
```

配置 DubboStarter

```java
@Component
public class Config {
    @Bean
    public DubboStarter dubboStarter() {
        DubboStarter dubboStarter = new DubboStarter();
        dubboStarter.setAppName("test");
        dubboStarter.setProtocolPort(20880);
        dubboStarter.setRegistryUrl("multicast://224.5.6.7:1234");
        dubboStarter.setServiceThreads(10);
        return dubboStarter;
    }
}
```

编写接口以及实现, 在方法上添加 `@Rpc` 注解，填写相应信息，将接口的实现发布为 spring bean

```java

public interface DemoService {

    @Rpc(value = "cn.fanhub.dubbo.sayhello", name = "testName", desc = "testDes")
    String sayHello(String name);
}

@Component
public class DemoServiceImpl implements DemoService {
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
```

完整的网关 demo 参考: https://github.com/distributed-and-microservice/Placidium

## 3. 扩展方式

### 3.1 handler 的扩展

irelia handler 使用常见的 PRPE 模式。即 pre、post、route 和 error。

每个 handler 有一个 order 函数，返回值为 int，值小的先执行, 不能为负数。

目前：

- HttpInboundHandler: pre | order 为 0

- AbstractConfigHandler: pre | order 为 10

- SecurityHandler: pre | order 为 20

- RouteHandler: route | order 为 1000

- ErrorHandler: error | order 为 2000




### 3.2 upstream 的扩展

// 待更新，可以参考 dubbo-upstream

### 3.3 spi 的扩展

// 待更新，可以参考 dubbo-spi