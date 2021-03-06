package ua.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.model.entity.Cuisine;
import ua.model.filter.SimpleFilter;

public interface CuisineService {

    Page<Cuisine> findAll(Pageable pageable, SimpleFilter filter);

    void deleteById(String id);

    Cuisine findById(String id);

    void save(Cuisine cuisine);
}
