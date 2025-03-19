package kg.nurtelecom.internet_shop.payload.response;

import java.util.UUID;

public record ComputerResponse(
        UUID id,
        String name,
        int price
) {
}
