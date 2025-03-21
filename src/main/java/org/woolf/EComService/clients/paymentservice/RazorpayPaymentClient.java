package org.woolf.EComService.clients.paymentservice;


import org.woolf.EComService.dtos.PaymentClientDto;
import org.woolf.EComService.exceptions.PaymentClientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service("razorpayPaymentClient")
public class RazorpayPaymentClient implements PaymentServiceClient{

    private RestTemplate restTemplate;
    private String paymentServiceBaseUrl;

    public RazorpayPaymentClient(RestTemplate restTemplate,
                                 @Value("${paymentservice.razorpay.api.url}") String paymentServiceApiUrl,
                                 @Value("${paymentservice.razorpay.api.paths.payments}") String paymentServicePaymentApiPath) {
        this.restTemplate = restTemplate;
        this.paymentServiceBaseUrl = paymentServiceApiUrl+paymentServicePaymentApiPath;
    }

    @Override
    public PaymentClientDto createPaymentOrder(String invoiceNumber, String currency, Double amount) throws PaymentClientException {
        URI uri = UriComponentsBuilder.fromUriString(paymentServiceBaseUrl + "/create-paymentLink")
                .queryParam("amount", amount)
                .queryParam("currency", currency)
                .queryParam("invoiceNo", invoiceNumber)
                .build()
                .toUri();

        // Use an empty Map as the request body
        Map<String, Object> requestBody = new HashMap<>();

        ResponseEntity<PaymentClientDto> response =
                restTemplate.postForEntity(uri,requestBody,PaymentClientDto.class);
        if(response.getStatusCode()!= HttpStatus.CREATED)
        {
            throw new PaymentClientException("Payment link not created.");
        }
        System.out.println();
        return response.getBody();
    }

    @Override
    public PaymentClientDto getPaymentStatus(String paymentOrderId) throws PaymentClientException {

        ResponseEntity<PaymentClientDto> response =
                restTemplate.getForEntity(paymentServiceBaseUrl+"/details/{orderId}",PaymentClientDto.class,paymentOrderId);

        if(response.getStatusCode()!= HttpStatus.OK)
        {
            throw new PaymentClientException("Could not get payment status.");
        }
        return response.getBody();
    }

    @Override
    public PaymentClientDto processRefund(String paymentOrderId) throws PaymentClientException {
        // Use an empty Map as the request body
        Map<String, Object> requestBody = new HashMap<>();

        ResponseEntity<PaymentClientDto> response =
                restTemplate.postForEntity(paymentServiceBaseUrl+"/create-refund/{orderId}",requestBody,PaymentClientDto.class,paymentOrderId);

        if(response.getStatusCode()!= HttpStatus.CREATED)
        {
            throw new PaymentClientException("Could not create refund request.");
        }
        return response.getBody();
    }
}
