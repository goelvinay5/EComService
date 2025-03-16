package org.woolf.EComService.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCartItemtDto {
    private Long productId;
    private int quantity;
}
