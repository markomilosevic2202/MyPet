package com.marko.mypet.repository;

import com.marko.mypet.entity.User;
import com.marko.mypet.entity.Vet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VetRepository extends JpaRepository<Vet, String> {

    @Query("select v from Vet v where v.specialty.name = :specialtyName")
    List<Vet> findBySpecialtyName(@Param("specialtyName") String specialtyName);

    @Query("select v from Vet v where v.specialty.id = ?1")
    List<Vet> findBySpecialtyId(String id);


}
