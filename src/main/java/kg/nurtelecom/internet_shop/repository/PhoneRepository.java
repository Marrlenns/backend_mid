package kg.nurtelecom.internet_shop.repository;

import kg.nurtelecom.internet_shop.exception.PhoneNotFoundException;
import kg.nurtelecom.internet_shop.mapper.PhoneDetailRowMapper;
import kg.nurtelecom.internet_shop.mapper.PhoneRowMapper;
import kg.nurtelecom.internet_shop.payload.request.PhoneRequest;
import kg.nurtelecom.internet_shop.payload.response.PhoneDetailResponse;
import kg.nurtelecom.internet_shop.payload.response.PhoneResponse;
import kg.nurtelecom.internet_shop.service.PhoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PhoneRepository implements PhoneService {

    private static final Logger log = LoggerFactory.getLogger(PhoneRepository.class);
    private final JdbcClient jdbcClient;

    public PhoneRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public UUID add(PhoneRequest request) {
        UUID id = UUID.randomUUID();
        String sql = "INSERT INTO phones (id, description, name, price) VALUES (?, ?, ?, ?)";

        jdbcClient.sql(sql)
                .params(id, request.description(), request.name(), request.price())
                .update();

        return id;
    }

    public boolean update(UUID id, PhoneRequest request) {

        if(!existsById(id)) {
            log.info("not found phone with id {}", id);
            throw new PhoneNotFoundException("Телефон с id - %s не найден".formatted(id));
        }

        String sql = """
            UPDATE phones 
            SET name = ?, description = ?, price = ?
            WHERE id = ?
        """;
        return jdbcClient.sql(sql)
                .params(
                        request.name(),
                        request.description(),
                        request.price(),
                        id)
                .update() > 0;
    }

    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM phones WHERE id = ?";

        Integer count = jdbcClient.sql(sql)
                .param(1, id)
                .query(Integer.class)
                .single();

        return count != null && count > 0;
    }

    public boolean delete(UUID id) {
        String sql = "DELETE FROM phones WHERE id = ?";

        return jdbcClient.sql(sql)
                .param(1, id)
                .update() > 0;
    }

    public List<PhoneResponse> findAll() {
        String sql = "SELECT name, price FROM phones";

        return jdbcClient.sql(sql)
                .query(new PhoneRowMapper()).list();
    }

    public Optional<PhoneDetailResponse> getById(UUID id) {
        String sql = "SELECT name, description, price FROM phones WHERE id = ?";

        return jdbcClient.sql(sql)
                .param(1, id)
                .query(new PhoneDetailRowMapper())
                .optional();
    }

}
