package kg.nurtelecom.internet_shop.controller;

import kg.nurtelecom.internet_shop.payload.request.ComputerRequest;
import kg.nurtelecom.internet_shop.payload.response.ComputerDetailResponse;
import kg.nurtelecom.internet_shop.payload.response.ComputerResponse;
import kg.nurtelecom.internet_shop.service.ComputerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/computer")
public class ComputerController {

    private final ComputerService computerService;

    public ComputerController(ComputerService computerService) {
        this.computerService = computerService;
    }

    @PostMapping
    public ResponseEntity<UUID> addComputer(
            @RequestBody ComputerRequest request) {

        UUID id = computerService.add(request);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateComputer(
            @PathVariable UUID id,
            @RequestBody ComputerRequest request) {

        try {
            computerService.update(id, request);
            return new ResponseEntity<>("Computer updated successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Computer with this ID not found!", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComputer(@PathVariable UUID id) {
        boolean deleted = computerService.delete(id);
        return deleted
                ? new ResponseEntity<>("Computer deleted successfully!", HttpStatus.OK)
                : new ResponseEntity<>("Computer with this ID not found!", HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<ComputerResponse>> getAllComputers() {
        List<ComputerResponse> computers = computerService.findAll();
        return new ResponseEntity<>(computers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComputerDetailResponse> getComputerById(@PathVariable UUID id) {
        Optional<ComputerDetailResponse> computer = computerService.getById(id);

        return computer.map(c -> new ResponseEntity<>(c, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

    }
}
