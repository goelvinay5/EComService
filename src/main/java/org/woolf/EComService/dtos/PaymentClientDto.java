package org.woolf.EComService.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentClientDto {
    @JsonProperty("orderId") // Map JSON field "orderId" to this Java field
    private String paymentOrderId;
    private String paymentId;
    private String refundId;
    private String status;
    private String paymentLink;
}
