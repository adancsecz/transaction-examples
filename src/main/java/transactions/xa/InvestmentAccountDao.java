package transactions.xa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import transactions.jpa.entity.InvestmentAccount;

@Service
public class InvestmentAccountDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvestmentAccountDao.class);

    @PersistenceContext(unitName = "investmentPersistenceUnit")
    private EntityManager entityManager;

    public void save(InvestmentAccount investmentAccount) {
        LOGGER.info("inserting investment account");
        entityManager.persist(investmentAccount);
    }

    public Integer getAllCount() {
        return entityManager.createQuery("from InvestmentAccount")
                .getResultList().size();
    }

}
