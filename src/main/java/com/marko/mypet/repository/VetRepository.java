package com.marko.mypet.repository;

import com.marko.mypet.entity.User;
import com.marko.mypet.entity.Vet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VetRepository extends JpaRepository<Vet, String> {
}
