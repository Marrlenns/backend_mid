package kg.nurtelecom.internet_shop.payload.response;

public record CartItemResponse(
        String name,
        int price,
        int quantity,
        int subtotal
) {
}
