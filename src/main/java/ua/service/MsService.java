package ua.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.entity.Ms;
import ua.model.filter.SimpleFilter;

public interface MsService {

	Page<Ms> findAll(Pageable pageable, SimpleFilter filter);

	void deleteById(String id);

	Ms findById(String id);

	void save(Ms ms);
}
