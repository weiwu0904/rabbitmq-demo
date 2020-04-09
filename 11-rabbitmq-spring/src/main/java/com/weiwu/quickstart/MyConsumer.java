package com.weiwu.quickstart;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * Author: dww
 * Date: 2020-04-08 15:55
 * Content:
 */
public class MyConsumer extends DefaultConsumer {

    private Channel channel;

    public MyConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    // 处理消息的方法
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//        super.handleDelivery(consumerTag, envelope, properties, body);
        System.out.println("consumerTag = " + consumerTag);
        System.out.println("envelope = " + envelope);
        System.out.println("properties = " + properties);
        System.out.println("body = " + new String(body));

    channel.basicAck(envelope.getDeliveryTag(), false);
    channel.basicNack(envelope.getDeliveryTag(), false, false);
    }
}
