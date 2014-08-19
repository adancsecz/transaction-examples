package transactions.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookingService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createSchema() {
        try {
            jdbcTemplate.execute("create table bookings("
                    + "FIRST_NAME varchar(5) NOT NULL)");
        } catch (DataIntegrityViolationException e) {
            jdbcTemplate.execute("drop table bookings");
            jdbcTemplate.execute("create table bookings("
                    + "FIRST_NAME varchar(5) NOT NULL)");
        }
    }

    @Transactional
    public void insertBookings(String... bookings) {
        for (String booking : bookings) {
            LOGGER.info("inserting booking: " + booking);
            jdbcTemplate.update("insert into bookings(FIRST_NAME) values (?)",
                    booking);
            sleep(1000);
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<String> findAllBookings() {
        return find();
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public List<String> findAllBookingsReadUnCommitted() {
        return find();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public int differenceBetweenFirstAndSeconFindRepeatableRead() {
        final List<String> first = find();
        sleep(2000);
        final List<String> second = find();
        return first.size() - second.size();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public int differenceBetweenFirstAndSeconFindSerializable() {
        final List<String> first = find();
        sleep(2000);
        final List<String> second = find();
        return first.size() - second.size();
    }

    private List<String> find() {
        LOGGER.info("finding bookings");
        return jdbcTemplate.query("select FIRST_NAME from bookings", new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int rowNumber)
                throws SQLException {
                return resultSet.getString("FIRST_NAME");
            }
        });
    }

    private void sleep(long m) {
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
