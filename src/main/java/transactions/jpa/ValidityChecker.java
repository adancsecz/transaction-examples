package transactions.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import transactions.jpa.entity.Booking;

@Service
public class ValidityChecker {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidityChecker.class);

    @PersistenceContext
    private EntityManager entityManager;

    // TODO input varargs 'bookings' not used in any of the method bodies!

    @Transactional(propagation = Propagation.REQUIRED)
    public void checkValidity(String... bookings) {
        entityManager.persist(new Booking("validation"));
        throw new RuntimeException("Exception by validation");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void checkValidityRequiresNew(String... bookings) {
        LOGGER.debug("inserting validation");
        entityManager.persist(new Booking("validation"));
    }

    @Transactional(propagation = Propagation.NESTED)
    public void checkValidityNested(String... bookings) {
        entityManager.persist(new Booking("validation"));
        throw new RuntimeException("Exception by validation");
    }

}
