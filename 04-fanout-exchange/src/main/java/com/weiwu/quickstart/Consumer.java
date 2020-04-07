package com.weiwu.quickstart;

import com.rabbitmq.client.*;

import java.io.IOException;
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

        String exchange = "test_fanout_exchange";
        String queueName = "test_fanout_queue";
        String routingKey = "";// 不需要routing key

        //4. 声明一个交换机
        channel.exchangeDeclare(exchange, "fanout", true);
        //5. 声明一个队列
        channel.queueDeclare(queueName, true, false, false, null);
        //6. 设置队列和交换机的绑定关系
        channel.queueBind(queueName, exchange, routingKey);

        // 消费消息
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        channel.basicConsume(queueName, true, queueingConsumer);

        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            byte[] body = delivery.getBody();
            String msg = new String(body);
            System.out.println("msg = " + msg);
        }
    }
}
