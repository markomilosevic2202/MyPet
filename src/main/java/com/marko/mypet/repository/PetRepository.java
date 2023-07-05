package com.marko.mypet.repository;

import com.marko.mypet.entity.Pet;
import com.marko.mypet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PetRepository extends JpaRepository<Pet, String> {
}
