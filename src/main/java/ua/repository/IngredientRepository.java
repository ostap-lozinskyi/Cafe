package ua.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.model.entity.Comment;
import ua.model.entity.Ingredient;
import ua.dto.IngredientDTO;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, String> {

    @Query("SELECT i.name FROM Ingredient i")
    List<String> findAllIngredientNames();

    @Query("SELECT new ua.dto.IngredientDTO(i.id, i.name) FROM Ingredient i WHERE i.id=?1")
    IngredientDTO findIngredientDTO(String id);

    @Query("SELECT c FROM Ingredient i JOIN i.comments c WHERE i.id=?1")
    List<Comment> findCommentList(String id);

    boolean existsByName(String name);

    Ingredient findByName(String name);

    Page<Ingredient> findAll(Specification<Ingredient> filterIngredients, Pageable pageable);
}
