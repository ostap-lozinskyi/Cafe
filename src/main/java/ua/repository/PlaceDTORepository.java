package ua.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.model.filter.PlaceFilter;
import ua.dto.PlaceDTO;

public interface PlaceDTORepository {

	Page<PlaceDTO> findAllDTOs(PlaceFilter filter, Pageable pageable);
		
}
