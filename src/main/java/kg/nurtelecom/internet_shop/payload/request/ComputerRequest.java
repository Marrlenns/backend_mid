package kg.nurtelecom.internet_shop.payload.request;

public record ComputerRequest(
        String name,
        String description,
        int price
) {
}
