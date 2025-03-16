package org.woolf.EComService.dtos;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateProductDto {
    private String title;
    private String description;
    private double price;
    private int stockQuantity;
    private double rating;
    private String category;
}
