package ua.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.model.entity.Ms;

import java.util.List;

public interface MsRepository extends JpaRepository<Ms, String> {

    @Query("SELECT ms.name FROM Ms ms")
    List<String> findAllMsNames();

    boolean existsByName(String name);

    Ms findByName(String name);

    Page<Ms> findAll(Specification<Ms> filterMss, Pageable pageable);
}
