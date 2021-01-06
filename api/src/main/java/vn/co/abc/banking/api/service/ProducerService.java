package vn.co.abc.banking.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import vn.co.abc.banking.api.message.AbstractMessage;

@Service
public class ProducerService {

    private final KafkaTemplate<String, AbstractMessage> messageTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public ProducerService(KafkaTemplate<String, AbstractMessage> messageTemplate,
                           KafkaTemplate<String, String> kafkaTemplate) {
        this.messageTemplate = messageTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topicName, String message) {

        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata()
                        .offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }

    public void sendMessage(String topicName, AbstractMessage message) {
        ListenableFuture<SendResult<String, AbstractMessage>> future = messageTemplate.send(topicName, message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, AbstractMessage>>() {
            @Override
            public void onSuccess(SendResult<String, AbstractMessage> result) {
                System.out.println("Sent to topic=[" + topicName + "] message=[" + message.toString() + "] with offset=[" + result.getRecordMetadata()
                        .offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=[" + message.toString() + "] due to : " + ex.getMessage());
            }
        });
    }
}
