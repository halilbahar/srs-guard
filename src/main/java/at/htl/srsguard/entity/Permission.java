package at.htl.srsguard.entity;

import javax.persistence.*;

@Entity
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private App app;
    @ManyToOne
    private Stream stream;

    public Permission(App app, Stream stream) {
        this.app = app;
        this.stream = stream;
    }

    public Permission() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Stream getStream() {
        return stream;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }
}
