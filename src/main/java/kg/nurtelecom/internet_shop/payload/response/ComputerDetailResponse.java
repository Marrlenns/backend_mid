package kg.nurtelecom.internet_shop.payload.response;

import java.util.UUID;

public record ComputerDetailResponse(
        UUID id,
        String name,
        String description,
        int price
) {
}
