package org.woolf.EComService.kafka.producer;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderCompletedEvent implements Serializable {
    private String orderId;
    private String customerEmail;
    private String message;

    @Override
    public String toString() {
        return "OrderCompletedEvent{" +
                "orderId='" + orderId + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}