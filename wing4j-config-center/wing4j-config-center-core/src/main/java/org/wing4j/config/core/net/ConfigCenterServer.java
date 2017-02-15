package org.wing4j.config.core.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

/**
 * Created by wing4j on 2017/2/14.
 */
public class ConfigCenterServer {
    /**
     * 主机IP地址
     */
    String host;
    /**
     * 端口号
     */
    int port;

    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    public void init() throws InterruptedException{
        ServerBootstrap b = new ServerBootstrap();
        b.childOption(ChannelOption.SO_KEEPALIVE, true).childOption(ChannelOption.TCP_NODELAY, true);

        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .childHandler(new XDiamondServerInitializer(projectService, profileService, configService));

        b.bind(host, port).sync().channel().closeFuture().addListener(new FutureListener<Object>() {
            @Override
            public void operationComplete(Future<Object> arg0) throws Exception {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        });
    }

    public void destory(){
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
