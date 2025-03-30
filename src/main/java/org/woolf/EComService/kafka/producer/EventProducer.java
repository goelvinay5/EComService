package org.woolf.EComService.kafka.producer;


import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.kafka.support.SendResult;
import java.util.concurrent.CompletableFuture;
import org.springframework.messaging.Message;

@Service
public class EventProducer {

    private static final String ORDER_COMPETED_TOPIC = "order-completed-topic";
    private static final String REFUND_INITIATED_TOPIC = "refund-initiated-topic";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderCompletedEvent(OrderCompletedEvent event) {
        //        CompletableFuture<SendResult<String, OrderCompletedEvent>> future =
        //                kafkaTemplate.send(ORDER_COMPETED_TOPIC, event.getOrderId(), event);
        //
        //        future.whenComplete((result, ex) -> {
        //            if (ex == null) {
        //                System.out.println("Sent message=[{"+event+"}] with offset=[{"+result.getRecordMetadata().offset()+"}]" );
        //            } else {
        //                System.out.println("Unable to send message=[{"+event+"}]");
        //            }
        //        });

        // Create a Kafka message with headers
        Message<OrderCompletedEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, ORDER_COMPETED_TOPIC)
                .setHeader(KafkaHeaders.KEY, event.getOrderId())  // Setting key
                .setHeader("type", OrderCompletedEvent.class.getSimpleName())   // 👈 Custom header for deserialization at consumer
                .build();

        sendMessage(message);
    }

    public void sendRefundEvent(RefundEvent event) {
        // Create a Kafka message with headers
        Message<RefundEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, REFUND_INITIATED_TOPIC)
                .setHeader(KafkaHeaders.KEY, event.getOrderId())  // Setting key
                .setHeader("type", RefundEvent.class.getSimpleName())   // 👈 Custom header for deserialization at consumer
                .build();

        sendMessage(message);
    }

    private void sendMessage(Message kafkaMessage)
    {
        // Send message asynchronously
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(kafkaMessage);

        // Handle success or failure
        future.thenAccept(result -> {
            RecordMetadata metadata = result.getRecordMetadata();
            System.out.println("Sent message=[" + metadata.toString() + "] with offset=[" + metadata.offset() + "]");
        }).exceptionally(ex -> {
            System.out.println("Unable to send message due to: " + ex.getMessage());
            return null;
        });
    }
}