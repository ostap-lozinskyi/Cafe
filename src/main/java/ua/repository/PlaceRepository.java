package ua.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.model.entity.Place;
import ua.dto.PlaceDTO;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, String> {

    @Query("SELECT new ua.dto.PlaceDTO(p.id, p.name, p.countOfPeople, p.isFree) FROM Place p ORDER BY name")
    List<PlaceDTO> findAllPlaceDTOs();

    @Query("SELECT distinct p.countOfPeople FROM Place p")
    List<String> findAllPlacesCountOfPeople();

    @Query("SELECT new ua.dto.PlaceDTO(p.id, p.name, p.countOfPeople, p.isFree) FROM Place p WHERE p.id=?1")
    PlaceDTO findPlaceDTO(String id);

    @Query("SELECT new ua.dto.PlaceDTO(p.id, p.name, p.countOfPeople, p.isFree) FROM Place p JOIN p.user u WHERE u.id=?1")
    List<PlaceDTO> findPlaceIdByUserId(String id);

    boolean existsByName(String name);

    Place findPlaceByName(String name);

}
