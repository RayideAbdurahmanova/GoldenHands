package com.matrix.Java._Spring.repository;

import com.matrix.Java._Spring.model.entity.security.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    @EntityGraph(attributePaths = {"authorities"})
    Optional<User> findByUsername(String username);
}
