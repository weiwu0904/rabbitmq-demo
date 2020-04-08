package com.weiwu.quickstart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

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

        //4. 通过channel 发送数据
        for (int i = 0; i < 5; i++) {
            channel.basicPublish("","test001",null,"hello rabbitmq".getBytes());
        }

        //5. 关闭相关的连接
        channel.close();
        connection.close();
    }
}
