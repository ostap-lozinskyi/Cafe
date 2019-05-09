package ua.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.model.entity.Place;
import ua.model.entity.User;
import ua.exception.CafeException;
import ua.model.filter.PlaceFilter;
import ua.model.request.PlaceRequest;
import ua.dto.PlaceDTO;
import ua.repository.PlaceRepository;
import ua.repository.PlaceDTORepository;
import ua.service.PlaceService;
import ua.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PlaceServiceImpl implements PlaceService {
    private static final Logger LOG = LoggerFactory.getLogger(PlaceServiceImpl.class);
    private final PlaceRepository repository;
    private final PlaceDTORepository placeDTORepository;
    private final UserService userService;

    @Autowired
    public PlaceServiceImpl(PlaceRepository repository, PlaceDTORepository placeDTORepository,
                            UserService userService) {
        this.repository = repository;
        this.placeDTORepository = placeDTORepository;
        this.userService = userService;
    }

    @Override
    public Page<PlaceDTO> findAllDTOs(Pageable pageable, PlaceFilter filter) {
        return placeDTORepository.findAllDTOs(filter, pageable);
    }

    @Override
    public List<String> findAllPlacesCountOfPeople() {
        return repository.findAllPlacesCountOfPeople();
    }

    @Override
    public List<PlaceDTO> findAllPlaceDTOs() {
        return repository.findAllPlaceDTOs();
    }

    @Override
    public List<PlaceDTO> findPlaceIdByUserId() {
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
        place.setId(request.getId());
        place.setName(request.getName());
        place.setCountOfPeople(Integer.valueOf(request.getCountOfPeople()));
        place.setFree(true);
        repository.save(place);
        LOG.info("Exit from 'savePlace' method");
    }

    @Override
    public PlaceRequest findOnePlaceRequest(String id) {
        LOG.info("In 'findOnePlaceRequest' method. Id = {}", id);
        Place place = repository.findById(id)
                .orElseThrow(() -> new CafeException(String.format("Place with id [%s] not found", id)));
        PlaceRequest request = new PlaceRequest();
        request.setCountOfPeople(String.valueOf(place.getCountOfPeople()));
        request.setId(place.getId());
        request.setName(place.getName());
        LOG.info("Exit from 'findOnePlaceRequest' method");
        return request;
    }

    @Override
    public void deletePlace(String placeId) {
        LOG.info("In 'deletePlace' method. PlaceId = {}", placeId);
        repository.deleteById(placeId);
        LOG.info("Exit from 'deletePlace' method");
    }

    @Override
    public void updatePlaceUserId(String placeId) {
        LOG.info("In 'updatePlaceUserId' method. PlaceId = {}", placeId);
        Place place = repository.findById(placeId)
                .orElseThrow(() -> new CafeException(String.format("Place with id [%s] not found", placeId)));
        User user = userService.findCurrentUser();
        place.setUser(user);
        place.setFree(false);
        repository.save(place);
        LOG.info("Exit from 'updatePlaceUserId' method");
    }

    @Override
    public void makePlaceFree(String placeId) {
        LOG.info("In 'makePlaceFree' method. PlaceId = {}", placeId);
        Place place = repository.findById(placeId)
                .orElseThrow(() -> new CafeException(String.format("Place with id [%s] not found", placeId)));
        place.setFree(true);
        place.setUser(null);
        repository.save(place);
        LOG.info("Exit from 'makePlaceFree' method");
    }

    @Override
    public Place findPlaceById(String placeId) {
        return repository.findById(placeId)
                .orElseThrow(() -> new CafeException(String.format("Place with id [%s] not found", placeId)));
    }

    @Override
    public Place findPlaceByName(String name) {
        return repository.findPlaceByName(name);
    }
}