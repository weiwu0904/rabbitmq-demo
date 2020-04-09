package com.weiwu.quickstart;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Author: dww
 * Date: 2020-04-07 13:52
 * Content:
 */
public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        //1. 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.225.131");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //2. 创建一个连接
        Connection connection = connectionFactory.newConnection();

        //3. 创建通信信道
        Channel channel = connection.createChannel();

        // 创建死信队列
        String dlxExchangeName = "dlx.exchange";
        String dlxQueueName = "dlx.queue";
        channel.exchangeDeclare(dlxExchangeName, "topic", true);
        channel.queueDeclare(dlxQueueName, true, false, false, null);
        channel.queueBind(dlxQueueName, dlxExchangeName, "#");

        // 设置正常队列的 转发的 私信队列

        //4. 声明/创建 一个队列，用来监听消息
        String exchangeName = "test_dlx_exchange";
        String routingKey = "dlx.*";
        String queueName = "test_dlx_queue";

        channel.exchangeDeclare(exchangeName, "topic", true);

        // 设置队列的 arguments， 告知出现死信消息转发到 dlx交换机
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", dlxExchangeName);
        arguments.put("x-message-ttl", 10000);
        channel.queueDeclare(queueName, true, false, false, arguments);
        channel.queueBind(queueName, exchangeName, routingKey);
        //5. 设置qos
        channel.basicQos(0, 1, false);
        // 关闭自动签收 autoAck = false
        channel.basicConsume(queueName, false, new MyConsumer(channel));
    }
}
