package kg.nurtelecom.internet_shop.service;

import kg.nurtelecom.internet_shop.payload.request.CartItemRequest;
import kg.nurtelecom.internet_shop.payload.response.CartItemResponse;

import java.util.List;
import java.util.UUID;

public interface CartService {
    void deleteItemFromCart(UUID userId, UUID cartItemId);

    UUID addToCart(CartItemRequest request);

    List<CartItemResponse> getCartItems(UUID userId);

    void updateItemQuantity(UUID userId, UUID cartItemId, int quantity);
}
