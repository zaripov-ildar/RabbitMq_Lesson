package ru.zaripov.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProducerApp {
    private static final String EXCHANGE_NAME = "it_blog";


    public static void main(String[] args) throws TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            var generator = new MessageGenerator(EXCHANGE_NAME, channel);
            generator.start();

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
