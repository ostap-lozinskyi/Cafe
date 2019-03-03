package ua.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ua.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    User findUserByEmail(String email);

    boolean existsUserByEmail(String email);

    @Query("SELECT m.id FROM User u JOIN u.meals m WHERE u.id=?1")
    List<String> findUserMealsIds(String userId);

    @Query("SELECT new ua.model.view.MealView(m.id, m.photoUrl, m.version, m.name, m.fullDescription, m.price, m.weight, c.name, m.rate) FROM User u JOIN u.meals m JOIN m.cuisine c WHERE u.id=?1")
    List<Integer> findUserMealViews(String userId);

    void deleteById(String id);

    Optional<User> findById(String id);

}
