package de.brightslearning.webblog.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogUserRepository extends JpaRepository<BlogUser, Integer> {

    Optional<BlogUser> findByUsernameAndPassword(String username, String password);
    boolean existsByUsername(String username);
    BlogUser findByUsername(String username);

}
