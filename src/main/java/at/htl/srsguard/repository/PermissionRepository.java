package at.htl.srsguard.repository;

import at.htl.srsguard.entity.Permission;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class PermissionRepository implements PanacheRepository<Permission> {

    @Transactional
    public void persistPermission(Permission permission) {
        this.persist(permission);
    }

    @Transactional
    public boolean deletePermission(Long id) {
        return this.delete("id", id) != 0;
    }
}
