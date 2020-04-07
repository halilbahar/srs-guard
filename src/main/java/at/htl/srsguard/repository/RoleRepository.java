package at.htl.srsguard.repository;

import at.htl.srsguard.entity.Role;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class RoleRepository implements PanacheRepository<Role> {

    @Transactional
    public void persistRole(Role role) {
        this.persist(role);
    }

    @Transactional
    public Long deleteRole(Long id) {
        return this.delete("id", id);
    }
}
