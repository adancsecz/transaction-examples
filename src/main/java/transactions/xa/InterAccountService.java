package transactions.xa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import transactions.jpa.entity.BankAccount;
import transactions.jpa.entity.InvestmentAccount;

@Service
public class InterAccountService {

    @Autowired
    private BankAccountDao bankAccountDao;

    @Autowired
    private InvestmentAccountDao investmentAccountDao;

    @Transactional
    public void save(BankAccount bankAccount,
            InvestmentAccount investmentAccount) {
        bankAccountDao.save(bankAccount);
        investmentAccountDao.save(investmentAccount);

    }

}
