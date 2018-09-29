package com.avoole.im.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class MqttConnectHandler extends ChannelInitializer {

    private MqttServer server;

    public MqttConnectHandler(MqttServer server) {
        this.server = server;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        Log.d("initChannel: id" + ch.id() + " ip:" + ch.remoteAddress());
    }
}
