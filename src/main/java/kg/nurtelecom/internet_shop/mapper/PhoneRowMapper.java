package kg.nurtelecom.internet_shop.mapper;

import kg.nurtelecom.internet_shop.payload.response.PhoneResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PhoneRowMapper implements RowMapper<PhoneResponse> {
    @Override
    public PhoneResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new PhoneResponse(
                rs.getString("name"),
                rs.getInt("price")
        );
    }
}