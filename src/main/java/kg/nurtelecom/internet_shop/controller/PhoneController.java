package kg.nurtelecom.internet_shop.controller;

import kg.nurtelecom.internet_shop.payload.request.PhoneRequest;
import kg.nurtelecom.internet_shop.payload.response.PhoneDetailResponse;
import kg.nurtelecom.internet_shop.payload.response.PhoneResponse;
import kg.nurtelecom.internet_shop.service.PhoneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/phone")
public class PhoneController {

    private final PhoneService service;

    public PhoneController(PhoneService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PhoneResponse>> getAllPhones() {
        List<PhoneResponse> phones = service.findAll();
        return new ResponseEntity<>(phones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhoneDetailResponse> getPhoneById(@PathVariable UUID id) {
        Optional<PhoneDetailResponse> phone = service.getById(id);
        return phone.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<UUID> addPhone(@RequestBody PhoneRequest request) {
        UUID id = service.add(request);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePhone(
            @PathVariable UUID id,
            @RequestBody PhoneRequest request) {

        boolean updated = service.update(id, request);
        return updated
                ? new ResponseEntity<>("Phone updated successfully!", HttpStatus.OK)
                : new ResponseEntity<>("Phone with this ID not found.", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePhone(@PathVariable UUID id) {
        boolean deleted = service.delete(id);
        return deleted
                ? new ResponseEntity<>("Phone deleted successfully!", HttpStatus.OK)
                : new ResponseEntity<>("Phone with this ID not found.", HttpStatus.NOT_FOUND);
    }
}