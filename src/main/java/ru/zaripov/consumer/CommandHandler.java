package ru.zaripov.consumer;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;

public class CommandHandler extends Thread {
    private final Channel channel;
    private final String queueName;
    private final String exchangeName;

    public CommandHandler(Channel channel, String queueName, String exchangeName) {
        this.channel = channel;
        this.queueName = queueName;
        this.exchangeName = exchangeName;
        this.setDaemon(true);
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String input;
        while (true) {
            input = scanner.nextLine();
            String[] topics = input.split(" ");
            switch (topics[0]) {
                case "set_topic" -> changeTopic(topics, channel::queueBind);
                case "remove_topic" -> changeTopic(topics, channel::queueUnbind);
                default -> System.out.println("type: set_topic topic_1 topic_2\n" +
                        "or: remove_topic topic_1 topic_2");
            }
        }
    }

    private void changeTopic(String[] topics, Bindable bindable) {
        try {
            for (int i = 1; i < topics.length; i++) {
                bindable.bindOrUnbind(queueName, exchangeName, topics[i]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
