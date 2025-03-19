package kg.nurtelecom.internet_shop.mapper;

import kg.nurtelecom.internet_shop.payload.response.ComputerResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ComputerRowMapper implements RowMapper<ComputerResponse> {
    @Override
    public ComputerResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ComputerResponse(
                (UUID) rs.getObject("id"),
                rs.getString("name"),
                rs.getInt("price")
        );
    }
}