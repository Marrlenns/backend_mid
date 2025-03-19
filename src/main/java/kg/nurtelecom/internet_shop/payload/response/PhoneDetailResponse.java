package kg.nurtelecom.internet_shop.payload.response;

public record PhoneDetailResponse (
    String name,
    String description,
    int price
) {
}