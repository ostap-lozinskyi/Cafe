package ua.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.model.entity.Place;
import ua.model.filter.PlaceFilter;
import ua.model.request.PlaceRequest;
import ua.dto.PlaceDTO;

import java.util.List;

public interface PlaceService {

    Page<PlaceDTO> findAllDTOs(Pageable pageable, PlaceFilter filter);

    List<PlaceDTO> findAllPlaceDTOs();

    List<String> findAllPlacesCountOfPeople();

    List<PlaceDTO> findPlaceIdByUserId();

    void savePlace(PlaceRequest request);

    PlaceRequest findOnePlaceRequest(String id);

    void deletePlace(String id);

    void updatePlaceUserId(String placeId);

    void makePlaceFree(String placeId);

    Place findPlaceById(String placeId);

    Place findPlaceByName(String name);
}
