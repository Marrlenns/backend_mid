package kg.nurtelecom.internet_shop.payload.response;

import java.util.UUID;

public record User(
        UUID id,
        String username,
        String password,
        String role
) {
}
