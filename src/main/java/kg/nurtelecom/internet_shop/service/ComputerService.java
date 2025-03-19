package kg.nurtelecom.internet_shop.service;

import kg.nurtelecom.internet_shop.payload.request.ComputerRequest;
import kg.nurtelecom.internet_shop.payload.response.ComputerDetailResponse;
import kg.nurtelecom.internet_shop.payload.response.ComputerResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface ComputerService {

    UUID add(ComputerRequest request);

    void update(UUID id, ComputerRequest request);

    boolean delete(UUID id);

    List<ComputerResponse> findAll();

    Optional<ComputerDetailResponse> getById(UUID id);
}
