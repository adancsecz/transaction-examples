package transactions.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class InvestmentAccount {

    private Long id;
    private String name;

    public InvestmentAccount() {

    }

    public InvestmentAccount(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(length = 8)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
