package kg.nurtelecom.internet_shop.repository;

//import com.auth0.jwt.JWT;
//import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import kg.nurtelecom.internet_shop.exception.ComputerNotFoundException;
import kg.nurtelecom.internet_shop.mapper.ComputerDetailRowMapper;
import kg.nurtelecom.internet_shop.mapper.ComputerRowMapper;
import kg.nurtelecom.internet_shop.payload.request.ComputerRequest;
import kg.nurtelecom.internet_shop.payload.response.ComputerDetailResponse;
import kg.nurtelecom.internet_shop.payload.response.ComputerResponse;
import kg.nurtelecom.internet_shop.service.ComputerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ComputerRepository implements ComputerService {

    private static final Logger log = LoggerFactory.getLogger(ComputerRepository.class);
    private final JdbcClient jdbcClient;
    private final HttpServletRequest request;

    public ComputerRepository(JdbcClient jdbcClient, HttpServletRequest request) {
        this.jdbcClient = jdbcClient;
        this.request = request;
    }

    public UUID add(ComputerRequest request) {
        UUID id = UUID.randomUUID();

        // Добавляем запись в базу данных с бинарными данными изображения
        String sql = "INSERT INTO computers (id, name, description, price) VALUES (?, ?, ?, ?)";

        jdbcClient.sql(sql)
                .param(1, id)
                .param(2, request.name())
                .param(3, request.description())
                .param(4, request.price())
                .update();

        return id;
    }

    public void update(UUID id, ComputerRequest request) {

        if(!existsById(id)) {
            log.info("not found computer with id {}", id);
            throw new ComputerNotFoundException("Компьютер с id - %s не найден".formatted(id));
        }

        String sql = """
            UPDATE computers 
            SET name = ?, description = ?, price = ?
            WHERE id = ?
        """;

        jdbcClient.sql(sql)
                .param(1, request.name())
                .param(2, request.description())
                .param(3, request.price())
                .param(4, id)
                .update();
    }

    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM computers WHERE id = ?";

        Integer count = jdbcClient.sql(sql)
                .param(1, id)
                .query(Integer.class)
                .single();

        return count != null && count > 0;
    }

    public boolean delete(UUID id) {
        String sql = "DELETE FROM computers WHERE id = ?";

        return jdbcClient.sql(sql)
                .param(1, id)
                .update() > 0;
    }



    public List<ComputerResponse> findAll() {

        String sql = "SELECT id, name, price FROM computers";

        return jdbcClient.sql(sql)
                .query(new ComputerRowMapper()).list();
    }



    public Optional<ComputerDetailResponse> getById(UUID id) {
        String sql = "SELECT id, name, description, price, image FROM computers WHERE id = ?";

        return jdbcClient.sql(sql)
                .param(1, id)
                .query(new ComputerDetailRowMapper())
                .optional();
    }

}
