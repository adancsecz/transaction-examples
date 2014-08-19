package transactions.jdbc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:jdbc-context.xml" })
@EnableTransactionManagement
public class IsolationLevelTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(IsolationLevelTest.class);

    @Autowired
    private BookingService bookingService;

    private boolean readUnCommitted;

    @Before
    public void cleanup() {
        bookingService.createSchema();
        readUnCommitted = false;
    }

    @Test
    public void testReadUnComittedShouldBeFalse() {
        startConcurrentChecker(false);
        bookingService.insertBookings("book1", "book2", "book3");
        Assert.assertEquals(3, bookingService.findAllBookings().size());
        Assert.assertFalse(readUnCommitted);
    }

    @Test
    public void testReadUnComittedShouldBeTrue() {
        startConcurrentChecker(true);
        bookingService.insertBookings("book1", "book2", "book3");
        Assert.assertEquals(3, bookingService.findAllBookings().size());
        Assert.assertTrue(readUnCommitted);
    }

    @Test
    public void testRepeatableReadShouldGiveDifferentResultWhileInserting() {
        startConcurrentInsert();
        int difference = bookingService
                .differenceBetweenFirstAndSeconFindRepeatableRead();
        Assert.assertEquals(-1, difference);
    }

    @Test
    public void testRepeatableReadShouldGiveSameResultWhileInserting() {
        startConcurrentInsert();
        int difference = bookingService
                .differenceBetweenFirstAndSeconFindSerializable();
        Assert.assertEquals(0, difference);
    }

    private void startConcurrentChecker(final boolean checkReadUnCommitted) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int size = getNumberOfBooking(checkReadUnCommitted);
                while (size < 3) {
                    size = getNumberOfBooking(checkReadUnCommitted);
                    LOGGER.info("In thread:" + size);
                    if (0 < size && size < 3) {
                        readUnCommitted = true;
                    }
                }
            }

            private int getNumberOfBooking(boolean checkReadUnCommitted) {
                return checkReadUnCommitted ? bookingService.findAllBookingsReadUnCommitted()
                        .size() : bookingService.findAllBookings().size();
            }
        }).start();
    }

    private void startConcurrentInsert() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage(), e);
                }
                bookingService.insertBookings("book1");
            }
        }).start();
    }

}
