package org.woolf.EComService.dtos;

import org.woolf.EComService.models.cart.Cart;
import org.woolf.EComService.models.cart.CartItem;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CartResponseDto {
    private Long id;
    private Long userId;
    private List<CartItemResponse> items;
    private double totalPrice;

    public static CartResponseDto fromCart(Cart cart) {
        CartResponseDto cartResponseDto = new CartResponseDto();
        cartResponseDto.setId(cart.getId());
        cartResponseDto.setUserId(cart.getUser().getId());
        cartResponseDto.setTotalPrice(cart.getTotalPrice());

        List<CartItemResponse> cartItems = new ArrayList<>();
        for(CartItem item: cart.getProducts())
        {
            CartItemResponse cartItemResponse = new CartItemResponse();
            cartItemResponse.setId(item.getId());
            cartItemResponse.setQuantity(item.getQuantity());
            cartItemResponse.setProductId(item.getProduct().getId());
            cartItemResponse.setProductName(item.getProduct().getTitle());
            cartItemResponse.setPrice(item.getProduct().getPrice());
            cartItems.add(cartItemResponse);
        }
        cartResponseDto.setItems(cartItems);
        return cartResponseDto;
    }
}
