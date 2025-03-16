package org.woolf.EComService.controllers;

import org.woolf.EComService.dtos.AddCartItemtDto;
import org.woolf.EComService.dtos.CartResponseDto;
import org.woolf.EComService.exceptions.InsufficientStockException;
import org.woolf.EComService.exceptions.InvalidDataException;
import org.woolf.EComService.exceptions.NotFoundException;
import org.woolf.EComService.models.User;
import org.woolf.EComService.models.cart.Cart;
import org.woolf.EComService.repositories.UserRepository;
import org.woolf.EComService.services.CartService;
import org.woolf.EComService.utils.UserUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/carts")
public class CartController {
    private CartService cartService;
    private UserRepository userRepository;
    public CartController(CartService cartService, UserRepository userRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    @PostMapping("/cart")
    public ResponseEntity<CartResponseDto> addToCart(Authentication authentication, @RequestBody AddCartItemtDto addCartItemtDto) throws InsufficientStockException, NotFoundException {
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        User user = UserUtils.createUserIfNotExist(jwt, userRepository);

        Cart cart = cartService.addItemsToCart(user.getId(), addCartItemtDto);
        return new ResponseEntity<>(CartResponseDto.fromCart(cart), HttpStatus.CREATED);
    }

    @GetMapping("/cart")
    public ResponseEntity<CartResponseDto> getCart(Authentication authentication) throws NotFoundException, InvalidDataException {
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        User user = UserUtils.createUserIfNotExist(jwt, userRepository);

        Cart cart = cartService.getCartItems(user.getId());
        return new ResponseEntity<>(CartResponseDto.fromCart(cart), HttpStatus.OK);
    }

    @PatchMapping("/cart")
    public ResponseEntity<CartResponseDto> updateCart(Authentication authentication,
                                                      @RequestParam Long cartItemId, @RequestParam int quantity) throws InsufficientStockException, InvalidDataException, NotFoundException {
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        User user = UserUtils.createUserIfNotExist(jwt, userRepository);

        Cart cart = cartService.updateItemQuantityInCart(user.getId(), cartItemId, quantity);
        return new ResponseEntity<>(CartResponseDto.fromCart(cart), HttpStatus.OK);
    }

    @PatchMapping("/cart/removeItem/{cartItemId}")
    public ResponseEntity<CartResponseDto> removeFromCart(Authentication authentication, @PathVariable Long cartItemId) throws InvalidDataException, NotFoundException {
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        User user = UserUtils.createUserIfNotExist(jwt, userRepository);

        Cart cart = cartService.removeItemsFromCart(user.getId(),cartItemId);
        return new ResponseEntity<>(CartResponseDto.fromCart(cart), HttpStatus.OK);
    }

    @DeleteMapping("/cart")
    public ResponseEntity<Void>  clearCart(Authentication authentication) throws NotFoundException {
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        User user = UserUtils.createUserIfNotExist(jwt, userRepository);
        cartService.clearCart(user.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
