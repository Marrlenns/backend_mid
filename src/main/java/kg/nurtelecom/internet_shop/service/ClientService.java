package kg.nurtelecom.internet_shop.service;

import kg.nurtelecom.internet_shop.payload.response.User;

import java.util.Optional;

public interface ClientService {
    Optional<User> findByUsername(String username);
}
