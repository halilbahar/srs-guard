package at.htl.srsguard.repository;

import at.htl.srsguard.entity.Account;
import at.htl.srsguard.entity.Role;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class AccountRepository implements PanacheRepository<Account> {

    @Transactional
    public boolean addRole(Account account, Role role) {
        return account.getRolesList().add(role);
    }
}
