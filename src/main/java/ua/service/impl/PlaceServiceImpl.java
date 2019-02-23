package ua.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.entity.Place;
import ua.entity.User;
import ua.model.filter.PlaceFilter;
import ua.model.request.PlaceRequest;
import ua.model.view.PlaceView;
import ua.repository.PlaceRepository;
import ua.repository.PlaceViewRepository;
import ua.service.PlaceService;
import ua.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PlaceServiceImpl implements PlaceService {
    private static final Logger LOG = LoggerFactory.getLogger(PlaceServiceImpl.class);
    private final PlaceRepository repository;
    private final PlaceViewRepository placeViewRepository;
    private final UserService userService;

    @Autowired
    public PlaceServiceImpl(PlaceRepository repository, PlaceViewRepository placeViewRepository,
                            UserService userService) {
        this.repository = repository;
        this.placeViewRepository = placeViewRepository;
        this.userService = userService;
    }

    @Override
    public Page<PlaceView> findAllView(Pageable pageable, PlaceFilter filter) {
        return placeViewRepository.findAllView(filter, pageable);
    }

    @Override
    public List<String> findAllPlacesCountOfPeople() {
        return repository.findAllPlacesCountOfPeople();
    }

    @Override
    public List<PlaceView> findAllPlaceViews() {
        return repository.findAllPlaceViews();
    }

    @Override
    public List<PlaceView> findPlaceIdByUserId() {
        LOG.info("In 'findPlaceIdByUserId' method");
        User user = userService.findCurrentUser();
        if (Objects.nonNull(user)) {
            return repository.findPlaceIdByUserId(user.getId());
        }
        LOG.info("Exit from 'findPlaceIdByUserId' method");
        return new ArrayList<>();
    }

    @Override
    public void savePlace(PlaceRequest request) {
        LOG.info("In 'savePlace' method");
        Place place = new Place();
        place.setCountOfPeople(Integer.valueOf(request.getCountOfPeople()));
        place.setId(request.getId());
        place.setNumber(Integer.valueOf(request.getNumber()));
        place.setFree(true);
        repository.save(place);
        LOG.info("Exit from 'savePlace' method");
    }

    @Override
    public PlaceRequest findOnePlaceRequest(Integer id) {
        LOG.info("In 'findOnePlaceRequest' method. Id = {}", id);
        Place place = repository.findOneRequest(id);
        PlaceRequest request = new PlaceRequest();
        request.setCountOfPeople(String.valueOf(place.getCountOfPeople()));
        request.setId(place.getId());
        request.setNumber(String.valueOf(place.getNumber()));
        LOG.info("Exit from 'findOnePlaceRequest' method");
        return request;
    }

    @Override
    public void deletePlace(Integer placeId) {
        LOG.info("In 'deletePlace' method. PlaceId = {}", placeId);
        repository.deleteById(placeId);
        LOG.info("Exit from 'deletePlace' method");
    }

    @Override
    public void updatePlaceUserId(Integer placeId) {
        LOG.info("In 'updatePlaceUserId' method. PlaceId = {}", placeId);
        Place place = repository.findPlaceById(placeId);
        User user = userService.findCurrentUser();
        place.setUser(user);
        place.setFree(false);
        repository.save(place);
        LOG.info("Exit from 'updatePlaceUserId' method");
    }

    @Override
    public void makePlaceFree(Integer placeId) {
        LOG.info("In 'makePlaceFree' method. PlaceId = {}", placeId);
        Place place = repository.findPlaceById(placeId);
        place.setFree(true);
        place.setUser(null);
        repository.save(place);
        LOG.info("Exit from 'makePlaceFree' method");
    }
}