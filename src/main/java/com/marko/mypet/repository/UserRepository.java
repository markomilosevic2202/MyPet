package com.marko.mypet.repository;

import com.marko.mypet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select (count(u) > 0) from User u where u.email = ?1")
    Boolean existsByEmail(String email);


    @Query("select u from User u where u.email = ?1")
    Optional<User> findUserByEmail(String email);




}
