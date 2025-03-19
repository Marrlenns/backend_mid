package kg.nurtelecom.internet_shop.service;

import kg.nurtelecom.internet_shop.payload.request.PhoneRequest;
import kg.nurtelecom.internet_shop.payload.response.PhoneDetailResponse;
import kg.nurtelecom.internet_shop.payload.response.PhoneResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PhoneService {

    List<PhoneResponse> findAll();

    Optional<PhoneDetailResponse> getById(UUID id);

    UUID add(PhoneRequest request);

    boolean update(UUID id, PhoneRequest request);

    boolean delete(UUID id);

}
