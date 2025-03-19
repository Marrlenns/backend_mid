package kg.nurtelecom.internet_shop.mapper;

import kg.nurtelecom.internet_shop.payload.response.ComputerDetailResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ComputerDetailRowMapper implements RowMapper<ComputerDetailResponse> {
    @Override
    public ComputerDetailResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ComputerDetailResponse(
                (UUID) rs.getObject("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("price")
        );
    }
}