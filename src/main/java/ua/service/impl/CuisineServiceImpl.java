package ua.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.entity.Cuisine;
import ua.exception.CafeException;
import ua.model.filter.SimpleFilter;
import ua.repository.CuisineRepository;
import ua.service.CuisineService;

@Service
public class CuisineServiceImpl implements CuisineService {

    private final CuisineRepository cuisineRepository;

    @Autowired
    public CuisineServiceImpl(CuisineRepository cuisineRepository) {
        this.cuisineRepository = cuisineRepository;
    }

    @Override
    public Page<Cuisine> findAll(Pageable pageable, SimpleFilter simpleFilter) {
        return cuisineRepository.findAll(filterCuisines(simpleFilter), pageable);
    }

    private Specification<Cuisine> filterCuisines(SimpleFilter filter) {
        return (root, query, cb) -> {
            if (filter.getSearch().isEmpty()) {
                return null;
            }
            return cb.like(root.get("name"), filter.getSearch() + "%");
        };
    }

    @Override
    public void deleteById(String id) {
        cuisineRepository.deleteById(id);
    }

    @Override
    public Cuisine findById(String id) {
        return cuisineRepository.findById(id)
                .orElseThrow(() -> new CafeException(String.format("Cuisine with id [%s} not found", id)));
    }

    @Override
    public void save(Cuisine cuisine) {
        cuisineRepository.save(cuisine);
    }
}
