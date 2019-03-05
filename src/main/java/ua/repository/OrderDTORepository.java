package ua.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.model.filter.OrderFilter;
import ua.dto.OrderDTO;

public interface OrderDTORepository {

	Page<OrderDTO> findAllDTOs(OrderFilter filter, Pageable pageable);
		
}
