package com.weiwu.quickstart;

import com.rabbitmq.client.AMQP;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Test
    public void testAdmin() {

        // 声明交换机
        rabbitAdmin.declareExchange(new DirectExchange("test.direct", false, false));
        rabbitAdmin.declareExchange(new TopicExchange("test.topic", false, false));
        rabbitAdmin.declareExchange(new FanoutExchange("test.fanout", false, false));

        // 声明队列
        rabbitAdmin.declareQueue(new Queue("test.direct.queue", false));
        rabbitAdmin.declareQueue(new Queue("test.topic.queue", false));
        rabbitAdmin.declareQueue(new Queue("test.fanout.queue", false));

        // 建立绑定关系
        rabbitAdmin.declareBinding(new Binding(
                "test.direct.queue",
                Binding.DestinationType.QUEUE,
                "test.direct",
                "test.direct",
                new HashMap<>()));

        // 链式编程绑定语法
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("test.topic"))
                .to(new TopicExchange("test.topic",false,false))
                .with("test.*"));
    }
}
