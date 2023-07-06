package com.marko.mypet.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Entity
@Table(name = "pet")
@Getter
@Setter
@ToString
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;


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
