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

        //4. 声明/创建 一个队列，用来监听消息
        String exchangeName = "test_return_exchange";
        String routingKey = "return.*";
        String queueName = "test_return_queue";

        channel.exchangeDeclare(exchangeName, "topic", true);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        //5. 创建一个消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);

        //6. 设置channel
        channel.basicConsume(queueName, true, consumer);

        while (true) {
            //7. 获取消息, 配送，
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();

            byte[] body = delivery.getBody();

            String msg = new String(body);

            System.out.println("msg = " + msg);

            Envelope envelope = delivery.getEnvelope();
            envelope.getDeliveryTag();
        }
    }
}
