package org.woolf.EComService.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemsDto {
    private Long id;
    private Long productId;
    private String productName;
    private int quantity;
    private double priceAtPurchase;
}
