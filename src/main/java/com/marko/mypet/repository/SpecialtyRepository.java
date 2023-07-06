package com.marko.mypet.repository;

import com.marko.mypet.entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  SpecialtyRepository extends JpaRepository<Specialty, String> {
}
