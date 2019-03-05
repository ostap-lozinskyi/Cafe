package ua.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.model.entity.Cuisine;

import java.util.List;

public interface CuisineRepository extends JpaRepository<Cuisine, String> {

    @Query("SELECT c.name FROM Cuisine c")
    List<String> findAllCuisinesNames();

    boolean existsByName(String name);

    Cuisine findByName(String name);

    Page<Cuisine> findAll(Specification<Cuisine> filterCuisines, Pageable pageable);
}
