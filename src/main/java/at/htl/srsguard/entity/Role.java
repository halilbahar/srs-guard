package at.htl.srsguard.entity;

import at.htl.srsguard.model.AppStream;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToMany
    private List<Permission> permissions;

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Role() {
    }

    public Long getId() {
        return id;
    }

    @JsonbTransient
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AppStream> getPermissions() {
        return permissions.stream()
                .map(permission -> new AppStream(permission.getApp().getName(), permission.getStream().getName()))
                .collect(Collectors.toList());
    }

    @JsonbTransient
    public List<Permission> getPermissionList() {
        return permissions;
    }

    @JsonbTransient
    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
