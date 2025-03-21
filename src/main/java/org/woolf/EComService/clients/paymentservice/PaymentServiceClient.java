package org.woolf.EComService.clients.paymentservice;

import org.woolf.EComService.dtos.PaymentClientDto;
import org.woolf.EComService.exceptions.PaymentClientException;

public interface PaymentServiceClient {
    PaymentClientDto createPaymentOrder(String invoiceNumber, String currency, Double amount) throws PaymentClientException;
    PaymentClientDto getPaymentStatus(String paymentOrderId) throws PaymentClientException;
    PaymentClientDto processRefund(String paymentOrderId) throws PaymentClientException;
}
