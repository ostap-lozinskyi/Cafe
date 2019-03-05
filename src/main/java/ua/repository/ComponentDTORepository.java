package ua.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.model.filter.ComponentFilter;
import ua.dto.ComponentDTO;

public interface ComponentDTORepository {

	Page<ComponentDTO> findAllDTOs(ComponentFilter filter, Pageable pageable);
		
}
