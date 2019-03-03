package ua.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.entity.Place;
import ua.model.filter.PlaceFilter;
import ua.model.request.PlaceRequest;
import ua.model.view.PlaceView;

import java.util.List;

public interface PlaceService {

    Page<PlaceView> findAllView(Pageable pageable, PlaceFilter filter);

    List<PlaceView> findAllPlaceViews();

    List<String> findAllPlacesCountOfPeople();

    List<PlaceView> findPlaceIdByUserId();

    void savePlace(PlaceRequest request);

    PlaceRequest findOnePlaceRequest(String id);

    void deletePlace(String id);

    void updatePlaceUserId(String placeId);

    void makePlaceFree(String placeId);

    Place findPlaceById(String placeId);

    Place findPlaceByNumber(String number);
}
