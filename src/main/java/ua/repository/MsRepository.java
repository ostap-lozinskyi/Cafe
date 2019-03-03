package ua.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ua.entity.Ms;

import java.util.List;
import java.util.Optional;

public interface MsRepository extends JpaRepository<Ms, String>, JpaSpecificationExecutor<Ms> {

    @Query("SELECT ms.name FROM Ms ms")
    List<String> findAllMsNames();

    void deleteById(String id);

    Optional<Ms> findById(String id);

    boolean existsByName(String name);

    Ms findByName(String name);
}
