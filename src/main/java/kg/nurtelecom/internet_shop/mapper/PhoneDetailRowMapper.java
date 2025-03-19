package kg.nurtelecom.internet_shop.mapper;

import kg.nurtelecom.internet_shop.payload.response.PhoneDetailResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PhoneDetailRowMapper implements RowMapper<PhoneDetailResponse> {
    @Override
    public PhoneDetailResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new PhoneDetailResponse(
                rs.getString("description"),
                rs.getString("name"),
                rs.getInt("price")
        );
    }
}