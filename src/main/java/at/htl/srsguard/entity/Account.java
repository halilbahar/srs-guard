package at.htl.srsguard.entity;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name may not be empty!")
    private String name;
    private String key;
    private String description;
    // TODO: Find problem with FetchType.LAZY,
    //  (Unable to perform requested lazy initialization - session is closed and settings disallow loading outside the Session
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles = new LinkedList<>();

    public Account(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Account() {
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
