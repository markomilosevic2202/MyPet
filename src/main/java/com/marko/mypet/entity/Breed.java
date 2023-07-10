package com.marko.mypet.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "breed")
@Getter
@Setter
@RequiredArgsConstructor
public class Breed {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Date createdAt;
    @Column(nullable = false)
    @JsonIgnore
    private Date updatedAt;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Breed breed = (Breed) o;
        return id != null && Objects.equals(id, breed.id);
    }

}
