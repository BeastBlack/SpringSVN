package dev.blackbeast.springsvn.repository;

import dev.blackbeast.springsvn.domain.Config;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConfigRepository extends JpaRepository<Config, Long> {
    List<Config> findAll();
}
