package at.htl.srsguard.repository;

import at.htl.srsguard.entity.Permission;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@Transactional
@ApplicationScoped
public class PermissionRepository implements PanacheRepository<Permission> {
}
