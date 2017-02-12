package org.wing4j.config.client.net;

import org.wing4j.config.client.loader.ConfigCenterLoader;
import io.github.xdiamond.common.ResolvedConfigVO;
import io.github.xdiamond.common.net.MessageDecoder;
import io.github.xdiamond.common.net.MessageEncoder;
import io.github.xdiamond.common.util.ThreadFactoryBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 配置中心入口类
 */
@Slf4j
@Data
public class Client {
    String host;
    int port = 5678;
    int readTimeout = 15;
    int writeTimeout = 5;

    boolean daemon = true;

    // 指数退避的方式增加
    boolean backOffRetryInterval = true;
    // 失败重试的次数
    int maxRetryTimes = Integer.MAX_VALUE;
    // 失败重试的时间间隔
    int retryIntervalSeconds = 5;
    // 默认最大的重试时间间隔，2分钟
    int maxRetryIntervalSeconds = 2 * 60;

    // 当前已经重试的次数
    int currentRetryTimes = 0;

    ConfigCenterLoader configCenter;

    public Client() {
    }

    public Client(ConfigCenterLoader configCenter,
                  String host,
                  int port,
                  boolean backOffRetryInterval,
                  int maxRetryTimes,
                  int retryIntervalSeconds,
                  int maxRetryIntervalSeconds,
                  boolean daemon) {
        this.configCenter = configCenter;
        this.host = host;
        this.port = port;
        this.backOffRetryInterval = backOffRetryInterval;
        this.maxRetryTimes = maxRetryTimes;
        this.retryIntervalSeconds = retryIntervalSeconds;
        this.maxRetryIntervalSeconds = maxRetryIntervalSeconds;
        this.daemon = daemon;
    }

    EventLoopGroup eventLoopGroup = new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("config-center-clientNioEventLoop-thread-%d").setDaemon(daemon).build());
    Bootstrap bootstrap = new Bootstrap();
    ClientHandler clientHandler = new ClientHandler(this);

    public ChannelFuture init() {
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5 * 1000)
                .option(ChannelOption.TCP_NODELAY, true);

        return configureBootstrap(bootstrap, eventLoopGroup).connect();
    }

    public void destory() {
        eventLoopGroup.shutdownGracefully();
    }

    public Future<List<ResolvedConfigVO>> getConfigs(String groupId, String artifactId,
                                                     String version, String profile, String secretKey) {
        return clientHandler.getConfig(groupId, artifactId, version, profile, secretKey);
    }

    Bootstrap configureBootstrap(Bootstrap b, EventLoopGroup g) {
        b.group(g).channel(NioSocketChannel.class).remoteAddress(host, port)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        clientHandler = new ClientHandler(Client.this);
                        if (log.isDebugEnabled()) {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        }
                        ch.pipeline().addLast(
                                new IdleStateHandler(readTimeout, writeTimeout,
                                        readTimeout > writeTimeout ? readTimeout : writeTimeout), new MessageEncoder(),
                                new MessageDecoder(), clientHandler);
                    }
                });

        return b;
    }

    /**
     * only call by ClientHandler
     */
    void channelActive(ChannelHandlerContext ctx) {
        // 当client连接到server时，重连次数重置
        currentRetryTimes = 0;
    }

    /**
     * only call by ClientHandler
     */
    void channelUnregistered(final ChannelHandlerContext ctx) {
        currentRetryTimes++;
        if (currentRetryTimes > maxRetryTimes) {
            return;
        }

        int currentRetryInterval = retryIntervalSeconds;
        if (backOffRetryInterval) {
            // 注意currentRetryInterval 可能会溢出
            currentRetryInterval = retryIntervalSeconds * (currentRetryTimes >= 30 ? 1 << 30 : 1 << currentRetryTimes);
            if (currentRetryInterval <= 0) {
                currentRetryInterval = maxRetryIntervalSeconds;
            }
        }
        if (currentRetryInterval > maxRetryIntervalSeconds) {
            currentRetryInterval = maxRetryIntervalSeconds;
        }
        log.info("Waiting for " + currentRetryInterval + "s to reconnect");

        final EventLoop loop = ctx.channel().eventLoop();
        loop.schedule(new Runnable() {
            @Override
            public void run() {
                log.info("Reconnecting to {}:{}", host, port);
                configureBootstrap(new Bootstrap(), loop).connect().addListener(new FutureListener<Void>() {
                    @Override
                    public void operationComplete(Future<Void> future) throws Exception {
                        if (!future.isSuccess()) {
                            log.error("can not connection to {}:{}", host, port, future.cause());
                        } else {
                            log.info("connection to {}:{} success.", host, port);
                        }
                    }
                });
            }
        }, currentRetryInterval, TimeUnit.SECONDS);
    }

    /**
     * only call by ClientHandler，服务器通知Client配置有更新
     *
     * @return
     */
    void notifyConfigChanged() {
        configCenter.notifyConfigChanged();
    }

    @Override
    public String toString() {
        return "Client{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", readTimeout=" + readTimeout +
                ", writeTimeout=" + writeTimeout +
                ", daemon=" + daemon +
                ", backOffRetryInterval=" + backOffRetryInterval +
                ", maxRetryTimes=" + maxRetryTimes +
                ", retryIntervalSeconds=" + retryIntervalSeconds +
                ", maxRetryIntervalSeconds=" + maxRetryIntervalSeconds +
                ", currentRetryTimes=" + currentRetryTimes +
                ", eventLoopGroup=" + eventLoopGroup +
                ", bootstrap=" + bootstrap +
                ", clientHandler=" + clientHandler +
                '}';
    }
}
