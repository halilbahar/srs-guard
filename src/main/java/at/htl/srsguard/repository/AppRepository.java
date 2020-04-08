package at.htl.srsguard.repository;

import at.htl.srsguard.entity.App;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class AppRepository implements PanacheRepository<App> {

    @Transactional
    public void persistApp(App app) {
        this.persist(app);
    }

    @Transactional
    public boolean deleteApp(Long id) {
        return this.delete("id", id) != 0;
    }
}
