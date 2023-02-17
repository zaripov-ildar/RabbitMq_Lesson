package ru.zaripov.consumer;

import java.io.IOException;

public interface Bindable {
    void bindOrUnbind(String queueName, String exchangeName, String topicName) throws IOException;
}
