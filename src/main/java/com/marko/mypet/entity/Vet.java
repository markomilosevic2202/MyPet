package com.marko.mypet.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "vet")
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Vet {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ManyToOne(fetch = FetchType.EAGER )
    @JoinColumn(name = "specialty_id", nullable = true)
    private Specialty specialty;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST
            , CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "pet_vet",
            joinColumns = @JoinColumn(name = "vet_id"),
            inverseJoinColumns = @JoinColumn(name = "pet_id"))
    List<Pet> pets;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Vet breed = (Vet) o;
        return id != null && Objects.equals(id, breed.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void addPet(Pet thePet) {
        if (pets == null) {
            pets = new ArrayList<>();
        }
        pets.add(thePet);
    }
}
