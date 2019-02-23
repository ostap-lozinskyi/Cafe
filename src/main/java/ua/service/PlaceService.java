package ua.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    PlaceRequest findOnePlaceRequest(Integer id);

    void deletePlace(Integer id);

    void updatePlaceUserId(Integer placeId);

    void makePlaceFree(Integer placeId);
}
