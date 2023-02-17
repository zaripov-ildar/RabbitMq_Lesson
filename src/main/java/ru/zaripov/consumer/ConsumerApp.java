package ru.zaripov.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerApp {
    public static final String EXCHANGE_NAME = "it_blog";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();

        CommandHandler commandHandler = new CommandHandler(channel, queueName, EXCHANGE_NAME);
        commandHandler.start();

        DeliverCallback callback = (consumerTag, deliver) -> {
            String msg = new String(deliver.getBody());
            String topic = deliver.getEnvelope().getRoutingKey();
            System.out.printf("%s >>> %s%n", topic, msg);
        };
        channel.basicConsume(queueName, true, callback, consumerTag -> {
        });
    }
}


