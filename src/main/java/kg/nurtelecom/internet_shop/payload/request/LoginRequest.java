package kg.nurtelecom.internet_shop.payload.request;

public record LoginRequest(
        String username,
        String password
) {
}
