package com.marko.mypet.repository;

import com.marko.mypet.entity.Breed;
import com.marko.mypet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BreedRepository extends JpaRepository<Breed, String> {

}
