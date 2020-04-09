package at.htl.srsguard.repository;

import at.htl.srsguard.entity.Stream;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class StreamRepository implements PanacheRepository<Stream> {

    @Transactional
    public void persistStream(Stream stream) {
        this.persist(stream);
    }

    @Transactional
    public boolean deleteStream(Stream stream) {
        if (this.isPersistent(stream)) {
            this.delete(stream);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteStream(Long id) {
        return this.delete("id", id) != 0;
    }
}
