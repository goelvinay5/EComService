package org.woolf.EComService.models.product;

import org.woolf.EComService.models.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product extends BaseModel {
    private String title;
    private String description;
    private double price;
    private int stockQuantity;
    private double rating;
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;
}
