package com.weiwu.quickstart;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Author: dww
 * Date: 2020-04-07 13:52
 * Content:
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {

        //1. 创建连接工厂
        ConnectionFactory connectionFactory = new  ConnectionFactory();
        connectionFactory.setHost("192.168.225.131");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //2. 创建一个连接 (真实的TCP连接)
        Connection connection = connectionFactory.newConnection();

        //3. 创建通信信道 (虚拟连接，推荐1个线程1个信道)
        Channel channel = connection.createChannel();

        //4. 设置信道为 确认模式
        channel.confirmSelect();

        //4. 通过channel 发送数据
        String exchangeName = "test_dlx_exchange";
        String routingKey = "dlx.save";

        String msg = "hello rabbitmq dlx message";

        for (int i = 0; i < 5; i++) {
            // mandatory 设置为true
            channel.basicPublish(exchangeName, routingKey, true,null,msg.getBytes());
        }

        // 添加 return listener
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("replyCode = " + replyCode);
                System.out.println("replyText = " + replyText);
                System.out.println("exchange = " + exchange);
                System.out.println("routingKey = " + routingKey);
                System.out.println("body = " + new String(body));
            }
        });

        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("deliveryTag = " + deliveryTag);
                System.out.println("ack=====");
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("deliveryTag = " + deliveryTag);
                System.out.println("nack=====");
            }
        });

    }
}
