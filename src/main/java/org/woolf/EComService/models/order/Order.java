package org.woolf.EComService.models.order;

import org.woolf.EComService.models.BaseModel;
import org.woolf.EComService.models.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "`order`") //As order is reserved keyword in my sql. So, Escape the table name
public class Order extends BaseModel {
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private String trackingNumber;
    private String transactionId;

    public Order() {
        this.orderItems = new ArrayList<>();
    }
}
