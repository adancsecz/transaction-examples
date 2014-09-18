package transactions.xa;

import javax.persistence.PersistenceException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import transactions.jpa.entity.BankAccount;
import transactions.jpa.entity.InvestmentAccount;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:xa-context.xml" })
@EnableTransactionManagement
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class XATransactionsTest {

    @Autowired
    private InterAccountService interAccountService;

    @Autowired
    private AccountDao bankAccountDao;

    @Autowired
    private InvestmentAccountDao investmentAccountDao;

    @Test
    public void givenTwoDatasourcesWhenInsertsPerformedThenTothEntityShouldBeInserted() {
        interAccountService.save(new BankAccount("bank"), new InvestmentAccount("invet"));
        Assert.assertEquals(1, bankAccountDao.getAllCount());
        Assert.assertEquals(1, investmentAccountDao.getAllCount());
    }

    @Test
    public void givenTwoDatasourcesWhenOneInsertThrowsExceptionThenNoEntityShouldBeInserted() {
        try {
            interAccountService.save(new BankAccount("bank"), new InvestmentAccount("moreThanEightCharacters"));
            Assert.fail();
        } catch (PersistenceException e) {
            Assert.assertEquals(0, bankAccountDao.getAllCount());
            Assert.assertEquals(0, investmentAccountDao.getAllCount());
        }
    }

}
