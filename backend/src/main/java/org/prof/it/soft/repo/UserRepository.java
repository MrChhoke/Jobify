package org.prof.it.soft.repo;

import org.prof.it.soft.entity.security.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"permissions", "createdAt"})
    Optional<User> findByUsername(String username);

}
