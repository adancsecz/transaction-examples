package transactions.xa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import transactions.jpa.entity.BankAccount;

@Service
public class BankAccountDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountDao.class);

    @PersistenceContext(unitName = "bankPersistenceUnit")
    private EntityManager entityManager;

    public void save(BankAccount bankAccount) {
        LOGGER.info("inserting bank account");
        entityManager.persist(bankAccount);
    }

    public Integer getAllCount() {
        return entityManager.createQuery("from BankAccount").getResultList()
                .size();
    }

}
