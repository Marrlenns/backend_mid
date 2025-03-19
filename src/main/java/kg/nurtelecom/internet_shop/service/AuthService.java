package kg.nurtelecom.internet_shop.service;

import kg.nurtelecom.internet_shop.payload.request.RegistrationRequest;

import java.util.Map;
import java.util.UUID;

public interface AuthService {
    UUID registerUser(RegistrationRequest request);

    Map<String, String> login(String username, String password);
}
