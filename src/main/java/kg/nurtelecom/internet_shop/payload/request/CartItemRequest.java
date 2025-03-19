package kg.nurtelecom.internet_shop.payload.request;

import java.util.UUID;

public record CartItemRequest(
        UUID id,
        int quantity
) {
}
