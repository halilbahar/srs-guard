package at.htl.srsguard.entity;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name may not be empty!")
    private String name;
    private String key;
    private String description;
    @ManyToMany
    private List<Role> roles;

    public Account(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    public Account() {
        roles = new LinkedList<>();
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

    public List<String> getRoles() {
        System.out.println(Arrays.toString(roles.toArray()));
        return roles.stream().map(Role::getName).collect(Collectors.toList());
    }

    @JsonbTransient
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @JsonbTransient
    public List<Role> getRolesList() {
        return roles;
    }
}
