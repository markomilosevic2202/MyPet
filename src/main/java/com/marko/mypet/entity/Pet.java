package com.marko.mypet.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pet")
@Getter
@Setter
@RequiredArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer age;
    @Column(nullable = false)
    private Float weight;
    @Column(nullable = false)
    private boolean lost;
    @Column(nullable = false)
    @JsonIgnore
    private Date createdAt;
    @Column(nullable = false)
    @JsonIgnore
    private Date updatedAt;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "breed_id")
    private Breed breed;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST
            , CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "pet_vet",
            joinColumns = @JoinColumn(name = "pet_id"),
            inverseJoinColumns = @JoinColumn(name = "vet_id"))
    private List<Vet> vets;




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

    public void addVet(Vet theVet) {
        if (vets == null) {
            vets = new ArrayList<>();
        }
        vets.add(theVet);
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", weight=" + weight +
                ", lost=" + lost +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", user=" + user +
                ", vets=" + vets +
                '}';
    }
}
