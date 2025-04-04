package org.woolf.EComService.models;

import org.woolf.EComService.models.cart.Cart;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class User {
    @Id
    private Long id;
    private String name;
    private String email;
    private String address;
    @ElementCollection
    private List<String> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart; // Bidirectional relationship
}
