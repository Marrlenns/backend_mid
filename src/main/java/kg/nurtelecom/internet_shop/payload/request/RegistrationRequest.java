package kg.nurtelecom.internet_shop.payload.request;

public record RegistrationRequest(
        String username,
        String password1,
        String password2,
        String role
) {
}
