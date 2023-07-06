package com.marko.mypet.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "specialty")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Specialty {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "id", nullable = false)
    private String name;
    @OneToMany(mappedBy = "specialty", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Vet> vets;

}
