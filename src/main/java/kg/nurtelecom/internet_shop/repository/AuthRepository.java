package kg.nurtelecom.internet_shop.repository;


import kg.nurtelecom.internet_shop.payload.request.RegistrationRequest;
import kg.nurtelecom.internet_shop.service.AuthService;
import kg.nurtelecom.internet_shop.service.ClientService;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class AuthRepository implements AuthService {

    private final JdbcClient jdbcClient;
    private final ClientService clientService;
    private static final String SECRET = "secret";

    public AuthRepository(JdbcClient jdbcClient, ClientService clientService) {
        this.jdbcClient = jdbcClient;
        this.clientService = clientService;
    }

    @Override
    public UUID registerUser(RegistrationRequest request) {
        if (!request.password1().equals(request.password2())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        String role = request.role().toUpperCase();
        if (!role.equals("ADMIN") && !role.equals("CLIENT")) {
            throw new IllegalArgumentException("Invalid role. Must be 'ADMIN' or 'CLIENT'");
        }

        return save(request);

    }

    @Override
    public Map<String, String> login(String username, String password) {
        return null;
//        Optional<User> userOptional = clientService.findByUsername(username);
//        if (userOptional.isEmpty()) {
//            throw new IllegalArgumentException("Пользователь не найден");
//        }
//
//        User user = userOptional.get();
//
//        if (!passwordEncoder.matches(password, user.password())) {
//            throw new IllegalArgumentException("Неверный пароль");
//        }
//
//        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
//        String accessToken = JWT.create()
//                .withSubject(user.username())
//                .withClaim("role", user.role())
//                .withClaim("id", Collections.singletonList(user.id()))
//                .withExpiresAt(new Date(System.currentTimeMillis() + 3_600_000 * 24)) // 24 часа
//                .sign(algorithm);
//
//        // 4. Генерация refresh token
//        String refreshToken = JWT.create()
//                .withSubject(user.username())
//                .withExpiresAt(new Date(System.currentTimeMillis() + 3L * 24 * 60 * 60 * 1000)) // 3 дня
//                .sign(algorithm);
//
//        // 5. Возврат токенов
//        Map<String, String> tokens = new HashMap<>();
//        tokens.put("accessToken", accessToken);
//        tokens.put("refreshToken", refreshToken);
//        return tokens;
    }

    public UUID save(RegistrationRequest request) {
        UUID userId = UUID.randomUUID();
        UUID cartId = UUID.randomUUID();

        String userSql = "INSERT INTO users (id, username, password, role) VALUES (?, ?, ?, ?)";
        jdbcClient.sql(userSql)
                .param(1, userId)
                .param(2, request.username())
                .param(3, request.password1())
                .param(4, request.role())
                .update();

        String cartSql = "INSERT INTO cart (id, user_id) VALUES (?, ?)";
        jdbcClient.sql(cartSql)
                .param(1, cartId)
                .param(2, userId)
                .update();

        return userId;
    }

    public boolean existsByUsername(String username) {
        return jdbcClient
                .sql("SELECT COUNT(*) FROM users WHERE username = ?")
                .param(1, username)
                .query(Integer.class)
                .single() > 0;
    }
}
