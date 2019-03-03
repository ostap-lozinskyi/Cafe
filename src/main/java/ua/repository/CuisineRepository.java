package ua.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ua.entity.Cuisine;

import java.util.List;
import java.util.Optional;

public interface CuisineRepository extends JpaNameRepository<Cuisine>, JpaSpecificationExecutor<Cuisine> {

    @Query("SELECT c.name FROM Cuisine c")
    List<String> findAllCuisinesNames();

    void deleteById(String id);

    Optional<Cuisine> findById(String id);

}
