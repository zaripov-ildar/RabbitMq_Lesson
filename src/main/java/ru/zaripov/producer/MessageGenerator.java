package ru.zaripov.producer;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Random;

public class MessageGenerator {
    private final String[] topics = {"php", "java", "go", "python", "kotlin"};
    private final String exchangeName;
    private final Channel channel;

    public MessageGenerator(String exchangeName, Channel channel) {
        this.exchangeName = exchangeName;
        this.channel = channel;
    }


    public void start() {
        int i = 0;
        while (true) {
            String message = "message #" + i++;
            String topic = generateTopic();

            send(message, topic);
            System.out.printf("Sent topic %s >>> %s%n", topic, message);
            makePause();
        }
    }

    private void makePause() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void send(String message, String topic) {
        try {
            channel.basicPublish(exchangeName, topic, null, message.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateTopic() {
        return topics[(new Random()).nextInt(topics.length)];
    }
}
