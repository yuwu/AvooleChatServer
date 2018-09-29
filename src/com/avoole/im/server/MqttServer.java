package com.avoole.im.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class MqttServer {
    private static final int port = 8964;

    private ConcurrentHashMap<String, Channel> id2channel = new ConcurrentHashMap<String, Channel>();
    private ConcurrentHashMap<Channel, String> channel2id = new ConcurrentHashMap<Channel, String>();


    public void start() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup(4);
        NioEventLoopGroup workGroup=new NioEventLoopGroup(8);
        bootstrap.group(group,workGroup);

        bootstrap.channel(NioServerSocketChannel.class);//
        bootstrap.localAddress(new InetSocketAddress(port));
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);

        try {
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline()
                            .addLast(new ReadTimeoutHandler(120))
                            .addLast(MqttEncoder.INSTANCE)
                            .addLast(new MqttDecoder())
                            .addLast(new MqttConnectHandler(MqttServer.this));

                }
            });
            ChannelFuture f = bootstrap.bind().sync();
            System.out.println(MqttServer.class.getName() + " started and listen on " + f.channel().localAddress());

            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully().sync();
            workGroup.shutdownGracefully().sync();
        }
    }
}
