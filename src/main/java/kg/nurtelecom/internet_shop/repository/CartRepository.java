package kg.nurtelecom.internet_shop.repository;

import kg.nurtelecom.internet_shop.payload.request.CartItemRequest;
import kg.nurtelecom.internet_shop.payload.response.CartItemResponse;
import kg.nurtelecom.internet_shop.payload.response.Product;
import kg.nurtelecom.internet_shop.service.CartService;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CartRepository implements CartService {
    private final JdbcClient jdbcClient;

    public CartRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public UUID addToCart(CartItemRequest request) {
        // Получаем данные о продукте
        String computerSql = "SELECT name, price FROM computers WHERE id = ?";
        var computerResult = jdbcClient.sql(computerSql)
                .param(1, request.id())
                .query((rs, rowNum) -> new Product(rs.getString("name"), rs.getInt("price")))
                .list();

        Product product;
        if (!computerResult.isEmpty()) {
            product = computerResult.get(0);
        } else {
            // Если не нашли в computers, ищем в phones
            String phoneSql = "SELECT name, price FROM phones WHERE id = ?";
            var phoneResult = jdbcClient.sql(phoneSql)
                    .param(1, request.id())
                    .query((rs, rowNum) -> new Product(rs.getString("name"), rs.getInt("price")))
                    .list();

            if (phoneResult.isEmpty()) {
                throw new IllegalArgumentException("Product not found in computers or phones");
            }
            product = phoneResult.get(0);
        }

        // Вычисляем subtotal
        int subtotal = product.price() * request.quantity();

        // Генерируем UUID для CartItem
        UUID cartItemId = UUID.randomUUID();

        // Вставка CartItem
        String cartItemSql = "INSERT INTO cartitem (id, name, price, quantity, subtotal, cart_id) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcClient.sql(cartItemSql)
                .param(1, cartItemId)
                .param(2, product.name())
                .param(3, product.price())
                .param(4, request.quantity())
                .param(5, subtotal)
                .param(6, UUID.fromString("f5ef5273-b9fb-484c-9652-d2724664ad54"))
                .update();

        return cartItemId;
    }

    public List<CartItemResponse> getCartItemsByCartId(UUID cartId) {
        String sql = "SELECT id, name, price, quantity, subtotal FROM CartItem WHERE cart_id = ?";
        return jdbcClient.sql(sql)
                .param(1, cartId)
                .query((rs, rowNum) -> new CartItemResponse(
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getInt("quantity"),
                        rs.getInt("subtotal")
                ))
                .list();
    }

    public List<CartItemResponse> getCartItems(UUID userId) {
        UUID cartId = getCartIdByUserId(userId);
        if (cartId == null) {
            throw new IllegalArgumentException("Cart not found for user");
        }
        return getCartItemsByCartId(cartId);
    }

    public UUID getCartIdByUserId(UUID userId) {
        String sql = "SELECT id FROM cart WHERE user_id = ?";
        return jdbcClient.sql(sql)
                .param(1, userId)
                .query(UUID.class)
                .single();
    }

    public void deleteCartItem(UUID cartItemId, UUID cartId) {
        String checkSql = "SELECT COUNT(*) FROM CartItem WHERE id = ? AND cart_id = ?";
        int count = jdbcClient.sql(checkSql)
                .param(1, cartItemId)
                .param(2, cartId)
                .query(Integer.class)
                .single();

        if (count == 0) {
            throw new IllegalArgumentException("CartItem not found in the specified cart");
        }

        String deleteSql = "DELETE FROM CartItem WHERE id = ?";
        jdbcClient.sql(deleteSql)
                .param(1, cartItemId)
                .update();
    }

    public void deleteItemFromCart(UUID userId, UUID cartItemId) {
        UUID cartId = getCartIdByUserId(userId);
        if (cartId == null) {
            throw new IllegalArgumentException("Cart not found for user");
        }
        deleteCartItem(cartItemId, cartId);
    }

    public void updateCartItem(UUID cartItemId, UUID cartId, int newQuantity) {
        // Проверяем, существует ли элемент в корзине
        String checkSql = "SELECT price FROM CartItem WHERE id = ? AND cart_id = ?";
        var priceResult = jdbcClient.sql(checkSql)
                .param(1, cartItemId)
                .param(2, cartId)
                .query(Integer.class)
                .optional();

        if (priceResult.isEmpty()) {
            throw new IllegalArgumentException("CartItem not found in the specified cart");
        }

        int price = priceResult.get();
        int newSubtotal = price * newQuantity;

        // Обновляем quantity и subtotal
        String updateSql = "UPDATE CartItem SET quantity = ?, subtotal = ? WHERE id = ?";
        jdbcClient.sql(updateSql)
                .param(1, newQuantity)
                .param(2, newSubtotal)
                .param(3, cartItemId)
                .update();
    }

    public void updateItemQuantity(UUID userId, UUID cartItemId, int newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        UUID cartId = getCartIdByUserId(userId);
        if (cartId == null) {
            throw new IllegalArgumentException("Cart not found for user");
        }
        updateCartItem(cartItemId, cartId, newQuantity);
    }
}
