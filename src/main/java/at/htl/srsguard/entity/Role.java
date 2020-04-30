package at.htl.srsguard.entity;

import at.htl.srsguard.model.AppStream;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 3, max = 255, message = "Name needs to be between 3 and 255 characters!")
    private String name;
    @Size(max = 255, message = "Description may not be longer than 255!")
    private String description;
    @ManyToMany
    private List<Permission> permissions = new LinkedList<>();

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
