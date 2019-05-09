package ua.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.model.entity.Ms;
import ua.exception.CafeException;
import ua.model.filter.SimpleFilter;
import ua.repository.MsRepository;
import ua.service.MsService;

@Service
public class MsServiceImpl implements MsService {

    private final MsRepository msRepository;

    @Autowired
    public MsServiceImpl(MsRepository msRepository) {
        this.msRepository = msRepository;
    }

    @Override
    public Page<Ms> findAll(Pageable pageable, SimpleFilter simpleFilter) {
        return msRepository.findAll(filterMss(simpleFilter), pageable);
    }

    private Specification<Ms> filterMss(SimpleFilter filter) {
        return (root, query, cb) -> {
            if (filter.getSearch().isEmpty()) return null;
            return cb.like(root.get("name"), filter.getSearch() + "%");
        };
    }

    @Override
    public void deleteById(String id) {
        msRepository.deleteById(id);
    }

    @Override
    public Ms findById(String id) {
        return msRepository.findById(id)
                .orElseThrow(() -> new CafeException(String.format("Ms with id [%s] not found", id)));
    }

    @Override
    public void save(Ms ms) {
        msRepository.save(ms);
    }

}
