package org.woolf.EComService.models.product;

import org.woolf.EComService.models.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(price, product.price) == 0 && stockQuantity == product.stockQuantity && Double.compare(rating, product.rating) == 0 && Objects.equals(title, product.title) && Objects.equals(description, product.description) && Objects.equals(category, product.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, price, stockQuantity, rating, category);
    }
}
