package transactions.jpa;

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
@ContextConfiguration(locations = { "classpath:jpa-context.xml" })
@EnableTransactionManagement
public class IsolationLevelTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(IsolationLevelTest.class);

    @Autowired
    private BookingService bookingService;

    @Before
    public void setUp() {
        bookingService.deleteAllBookings();
    }

    @Test
    public void insertValidBookingsExpectedToBeSuccesful() {
        bookingService.insertBookings("User1", "User2", "User3");
        Assert.assertEquals(3, bookingService.findAllBookings().size());
    }

    @Test
    public void testInsertingWithTransactionAndReadingUncommited() {
        startConcurrentCheckerReadUncommited();
        bookingService.insertBookings("User1", "User2", "User3");
    }

    private void startConcurrentCheckerReadUncommited() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int size = bookingService.findAllBookingsReadUncommited()
                        .size();
                while (size < 3) {
                    sleep(100);
                    size = bookingService.findAllBookingsReadUncommited()
                            .size();
                }
            }
        }).start();
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
