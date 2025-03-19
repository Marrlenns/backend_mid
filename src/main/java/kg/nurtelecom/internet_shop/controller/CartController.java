package kg.nurtelecom.internet_shop.controller;

import kg.nurtelecom.internet_shop.payload.request.CartItemRequest;
import kg.nurtelecom.internet_shop.payload.request.UpdateQuantityRequest;
import kg.nurtelecom.internet_shop.payload.response.CartItemResponse;
import kg.nurtelecom.internet_shop.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<UUID> addCartItem(@RequestBody CartItemRequest request) {
        UUID id = cartService.addToCart(request);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @GetMapping("/items/{userId}")
    public ResponseEntity<List<CartItemResponse>> getCartItems(@PathVariable UUID userId) {
        try {
            List<CartItemResponse> items = cartService.getCartItems(userId);
            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @DeleteMapping("/items/{userId}/{cartItemId}")
    public ResponseEntity<String> deleteItem(@PathVariable UUID userId, @PathVariable UUID cartItemId) {
        try {
            cartService.deleteItemFromCart(userId, cartItemId);
            return new ResponseEntity<>("CartItem deleted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @PutMapping("/items/{userId}/{cartItemId}")
    public ResponseEntity<String> updateItemQuantity(
            @PathVariable UUID userId,
            @PathVariable UUID cartItemId,
            @RequestBody UpdateQuantityRequest request) {
        try {
            cartService.updateItemQuantity(userId, cartItemId, request.quantity());
            return new ResponseEntity<>("Quantity updated successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

}
