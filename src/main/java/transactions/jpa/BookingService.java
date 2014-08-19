package transactions.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import transactions.jpa.entity.Booking;

@Service
public class BookingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookingService.class);

    @PersistenceContext
    private EntityManager entityManager;

    // TODO - not used; is it needed???
    @Autowired
    private ValidityChecker validityChecker;

    @Autowired
    private JpaTransactionManager transactionManager;

    @Transactional
    public void deleteAllBookings() {
        final Query deleteQuery = entityManager.createQuery("delete from Booking");
        deleteQuery.executeUpdate();
    }

    // TODO - not used? remove
    @Transactional
    public void empty() {
        entityManager.clear();
    }

    @Transactional
    public void insertBookings(String... bookings) {
        insertMultipleBookings(bookings);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void insertBookingsWithRequiredPropagation(String... bookings) {
        insertMultipleBookings(bookings);

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertBookingsWithRequiresNewPropagation(String... bookings) {
        insertMultipleBookings(bookings);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void insertBookingsWithNestedPropagation(String... bookings) {
        insertMultipleBookings(bookings);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void insertBookingsWithMandatoryPropagation(String... bookings) {
        insertMultipleBookings(bookings);
    }

    @Transactional(propagation = Propagation.NEVER)
    public void insertBookingsWithNeverPropagation(String... bookings) {
        final TransactionStatus status = transactionManager
                .getTransaction(new DefaultTransactionDefinition());
        LOGGER.debug("Completed: {}", status.isCompleted());
        insertMultipleBookings(bookings);
    }

    public List<Booking> findAllBookings() {
        // TODO why to get the properties??
        // entityManager.getEntityManagerFactory().getProperties();
        final TypedQuery<Booking> query = entityManager.createQuery("from Booking",
                Booking.class);
        return query.getResultList();

    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public List<Booking> findAllBookingsReadUncommited() {
        return findAllBookings();
    }

    private void insertMultipleBookings(String... bookings) {
        for (String booking : bookings) {
            final Booking persistedBooking = new Booking(booking);
            entityManager.persist(persistedBooking);
        }
    }

}
