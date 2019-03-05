package ua.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.model.entity.Component;
import ua.model.entity.Ingredient;
import ua.model.entity.Ms;
import ua.dto.ComponentDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ComponentRepository extends JpaRepository<Component, String> {

    @Query("SELECT new ua.dto.ComponentDTO(c.id, c.amount, i.name, ms.name) FROM Component c JOIN c.ingredient i JOIN c.ms ms")
    List<ComponentDTO> findAllComponentDTOs();

    @Query(value = "SELECT new ua.dto.ComponentDTO(c.id, c.amount, i.name, ms.name) FROM Component c JOIN c.ingredient i JOIN c.ms ms WHERE i.id=?1")
    List<ComponentDTO> findComponentDTOsByIngredientId(String id);

    @Query("SELECT c FROM Component c WHERE c.ingredient=?1 AND c.amount=?2 AND c.ms=?3")
    Component existsComponent(Ingredient ingredient, BigDecimal amount, Ms ms);

}
