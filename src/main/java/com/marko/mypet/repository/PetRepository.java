package com.marko.mypet.repository;

import com.marko.mypet.entity.Pet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface PetRepository extends JpaRepository<Pet, String> {


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM pet_vet WHERE pet_id = ?1 AND vet_id = ?2", nativeQuery = true)
    void deleteVet(String idPet, String idVet);


}
