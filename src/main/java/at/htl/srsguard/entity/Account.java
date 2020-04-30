package at.htl.srsguard.entity;

import at.htl.srsguard.util.ApiKeyGenerator;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 3, max = 255, message = "Name needs to be between 3 and 255 characters!")
    @NotBlank(message = "Name may not be empty!")
    private String name;
    private String key;
    @Size(max = 255, message = "Description may not be longer than 255!")
    private String description;
    @ManyToMany
    private List<Role> roles = new LinkedList<>();

    public Account(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Account() {
        this.key = ApiKeyGenerator.generateKey();
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

    public String getKey() {
        return key;
    }

    @JsonbTransient
    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Role> getRoles() {
        return roles;
    }

    @JsonbTransient
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
