package com.yupi.rabbitmq.route;

import com.rabbitmq.client.*;
import com.yupi.rabbitmq.ConnectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 功能描述：
 *
 * @author Yupi Li
 * @date 2018/10/25 15:55
 */
public class Consumer1 {

    private final static String EXCHANGE_NAME = "exchange_direct";

    private final static String QUEUE_NAME = "ps_queue1";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 第三个参数为routing-key
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "error");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "info");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "debug");
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("consumer1 with routing-key 'error&info&debug' received message:"
                        + new String(body, StandardCharsets.UTF_8));
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }

}
