package at.htl.srsguard.repository;

import at.htl.srsguard.entity.Account;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class AccountRepository implements PanacheRepository<Account> {

    @Transactional
    public void persistAccount(Account account) {
        this.persist(account);
    }

    @Transactional
    public Long deleteAccount(Long id) {
        return this.delete("id", id);
    }
}
