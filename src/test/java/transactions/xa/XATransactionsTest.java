package transactions.xa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import transactions.jpa.entity.BankAccount;
import transactions.jpa.entity.InvestmentAccount;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:xa-context.xml" })
@EnableTransactionManagement
public class XATransactionsTest {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(XATransactionsTest.class);

    @Autowired
    private InterAccountService interAccountService;

    @Autowired
    private BankAccountDao bankAccountDao;

    @Autowired
    private InvestmentAccountDao investmentAccountDao;

    @Test
    public void test() {
        interAccountService.save(new BankAccount(), new InvestmentAccount());
        LOGGER.info("Bank accounts: " + bankAccountDao.getAllCount());
        LOGGER.info("Investment accounts: "
                + investmentAccountDao.getAllCount());
    }

}
