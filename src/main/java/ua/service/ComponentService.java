package ua.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.model.filter.ComponentFilter;
import ua.model.request.ComponentRequest;
import ua.model.view.ComponentView;

public interface ComponentService {

	List<String> findAllIngredients();

	List<String> findAllMss();

	Page<ComponentView> findAllView(Pageable pageable, ComponentFilter filter);

	void saveComponent(ComponentRequest request);

	ComponentRequest findOneComponentRequest(String id);

	void deleteById(String id);
}
