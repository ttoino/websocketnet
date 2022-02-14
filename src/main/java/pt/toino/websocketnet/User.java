package pt.toino.websocketnet;

import io.netty.channel.Channel;

public abstract class User {

    public final int id;
    public final Channel channel;

    public User(int id, Channel channel) {
        this.id = id;
        this.channel = channel;
    }
}
