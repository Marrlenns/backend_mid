package kg.nurtelecom.internet_shop.payload.request;

public record PhoneRequest(
        String name,
        String description,
        int price
) {
}
