package kg.nurtelecom.internet_shop.repository;

import kg.nurtelecom.internet_shop.payload.response.User;
import kg.nurtelecom.internet_shop.service.ClientService;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ClientRepository implements ClientService {
    private final JdbcClient jdbcClient;

    public ClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }


    public Optional<User> findByUsername(String username) {
        String sql = "SELECT id, username, password, role FROM users WHERE username = ?";
        return jdbcClient.sql(sql)
                .param(1, username)
                .query((rs, rowNum) -> new User(
                        rs.getObject("id", UUID.class),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                ))
                .optional();
    }
}
