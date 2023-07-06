package com.marko.mypet.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    private String id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private boolean enabled;
    @Column(nullable = false)
    @JsonIgnore
    private Date createdAt;
    @Column(nullable = false)
    @JsonIgnore
    private Date updatedAt;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Pet> pets;

    @PrePersist
    private void prePersist() {
        Date date = new Date();
        this.createdAt = date;
        this.updatedAt = date;
    }


    @PreUpdate
    public void preUpdate() {
        this.updatedAt = new Date();
    }
}
