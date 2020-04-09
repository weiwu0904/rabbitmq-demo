package com.weiwu.quickstart.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author: dww
 * Date: 2020-04-09 17:15
 * Content:
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 配置连接工厂
     * 	<rabbit:connection-factory id="connectionFactory"
     * 		host="114.215.83.3" port="5672" username="test" password="123456" />
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("192.168.225.131");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        return connectionFactory;
    }

    /**
     * 定义mq管理
     * 	<rabbit:admin connection-factory="connectionFactory" />
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }


}
