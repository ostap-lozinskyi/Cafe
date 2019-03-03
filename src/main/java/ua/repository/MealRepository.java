package ua.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.entity.Comment;
import ua.entity.Meal;
import ua.model.view.MealIndexView;
import ua.model.view.MealView;

import java.util.List;

public interface MealRepository extends JpaRepository<Meal, String> {

    @Query("SELECT m.name FROM Meal m")
    List<String> findAllMealsNames();

    @Query("SELECT new ua.model.view.MealView(m.id, m.photoUrl, m.version, m.name, m.fullDescription, m.price, m.weight, c.name, m.rate) FROM Meal m JOIN m.cuisine c JOIN m.orders o WHERE o.id=?1")
    List<MealView> findMealViewsForOrder(String orderId);

    @Query("SELECT new ua.model.view.MealIndexView(m.id, m.photoUrl, m.version, m.rate, m.price, m.weight, m.name, m.shortDescription) FROM Meal m ORDER BY m.rate DESC")
    List<MealIndexView> find5MealIndexViewsByRate();

    @Query("SELECT new ua.model.view.MealView(m.id, m.photoUrl, m.version, m.name, m.fullDescription, m.price, m.weight, c.name, m.rate) FROM Meal m JOIN m.cuisine c WHERE m.id=?1")
    MealView findMealViewById(String id);

    @Query("SELECT c FROM Meal m JOIN m.comments c WHERE m.id=?1")
    List<Comment> findCommentList(String id);

    boolean existsByName(String name);

    Meal findByName(String name);
}
