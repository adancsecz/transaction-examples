package transactions.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import transactions.jpa.entity.Booking;

@Service
public class JpaBookingService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Inserts booking into database.
     * 
     * @param bookings
     *            - the bookings to be inserted.
     */
    @Transactional
    public void insertBookings(String... bookings) {
        insertMultipleBookings(bookings);
    }

    /**
     * Inserts booking into database with REQUIRED propagation mode.
     * 
     * @param bookings
     *            - the bookings to be inserted.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void insertBookingsWithRequiredPropagation(String... bookings) {
        insertMultipleBookings(bookings);

    }

    /**
     * Inserts booking into database with REQUIRES_NEW propagation mode.
     * 
     * @param bookings
     *            - the bookings to be inserted.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertBookingsWithRequiresNewPropagation(String... bookings) {
        insertMultipleBookings(bookings);
    }

    /**
     * Inserts booking into database with NESTED propagation mode.
     * 
     * @param bookings
     *            - the bookings to be inserted.
     */
    @Transactional(propagation = Propagation.NESTED)
    public void insertBookingsWithNestedPropagation(String... bookings) {
        insertMultipleBookings(bookings);
    }

    /**
     * Inserts booking into database with MANDATORY propagation mode.
     * 
     * @param bookings
     *            - the bookings to be inserted.
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void insertBookingsWithMandatoryPropagation(String... bookings) {
        insertMultipleBookings(bookings);
    }

    /**
     * Inserts booking into database with NEVER propagation mode.
     * 
     * @param bookings
     *            - the bookings to be inserted.
     */
    @Transactional(propagation = Propagation.NEVER)
    public void insertBookingsWithNeverPropagation(String... bookings) {
        insertMultipleBookings(bookings);
    }

    /**
     * counts all the bookings.
     * 
     * @return - the number of bookings.
     */
    public long countAllBookings() {
        return entityManager.createQuery("select count(b.id) from Booking b", Long.class).getSingleResult();

    }

    /**
     * counts all the bookings including inserted but not committed ones.
     * 
     * @return - the number of bookings.
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public long countAllBookingsReadUncommited() {
        return countAllBookings();
    }

    private void insertMultipleBookings(String... bookings) {
        for (String booking : bookings) {
            Booking persistedBooking = new Booking(booking);
            entityManager.persist(persistedBooking);
        }
    }

}
