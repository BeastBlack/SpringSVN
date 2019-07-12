package dev.blackbeast.springsvn.repository;

import dev.blackbeast.springsvn.domain.Config;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ConfigRepository extends JpaRepository<Config, Long> {
    List<Config> findAll();

    @Transactional
    Config save(Config config);

    @Override
    void deleteAll();
}
