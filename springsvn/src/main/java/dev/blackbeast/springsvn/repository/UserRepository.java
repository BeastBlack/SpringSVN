package dev.blackbeast.springsvn.repository;

import dev.blackbeast.springsvn.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional
    User save(User user);

    List<User> findByUsername(String username);

    @Transactional
    @Modifying
    @Query("UPDATE User SET isEnabled = TRUE WHERE id = :id")
    void activateUser(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE User SET isEnabled = FALSE WHERE id = :id")
    void deactivateUser(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE User SET isAdmin = TRUE WHERE id = :id")
    void grantAdminPermission(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE User SET isAdmin = FALSE WHERE id = :id")
    void revokeAdminPermission(@Param("id") Long id);
}
