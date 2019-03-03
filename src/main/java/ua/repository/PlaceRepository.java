package ua.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.entity.Place;
import ua.model.view.PlaceView;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, String> {

    @Query("SELECT new ua.model.view.PlaceView(p.id, p.name, p.countOfPeople, p.isFree) FROM Place p ORDER BY name")
    List<PlaceView> findAllPlaceViews();

    @Query("SELECT distinct p.countOfPeople FROM Place p")
    List<String> findAllPlacesCountOfPeople();

    @Query("SELECT new ua.model.view.PlaceView(p.id, p.name, p.countOfPeople, p.isFree) FROM Place p WHERE p.id=?1")
    PlaceView findPlaceViewById(String id);

    @Query("SELECT new ua.model.view.PlaceView(p.id, p.name, p.countOfPeople, p.isFree) FROM Place p JOIN p.user u WHERE u.id=?1")
    List<PlaceView> findPlaceIdByUserId(String id);

    boolean existsByName(String name);

    Place findPlaceByName(String name);

}
