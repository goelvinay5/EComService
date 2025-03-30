package org.woolf.EComService.kafka.producer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefundEvent {
    private String orderId;
    private String customerEmail;
    private String message;
    private String refundAmount;
}
