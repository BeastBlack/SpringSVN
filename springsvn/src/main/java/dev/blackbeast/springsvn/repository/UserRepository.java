package dev.blackbeast.springsvn.repository;

import dev.blackbeast.springsvn.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional
    User save(User user);

    List<User> findByUsername(String username);
}
