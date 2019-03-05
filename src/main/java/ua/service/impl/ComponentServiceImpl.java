package ua.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.model.entity.Component;
import ua.exception.CafeException;
import ua.model.filter.ComponentFilter;
import ua.model.request.ComponentRequest;
import ua.dto.ComponentDTO;
import ua.repository.ComponentRepository;
import ua.repository.ComponentDTORepository;
import ua.repository.IngredientRepository;
import ua.repository.MsRepository;
import ua.service.ComponentService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ComponentServiceImpl implements ComponentService {
    private static final Logger LOG = LoggerFactory.getLogger(ComponentServiceImpl.class);
    private final ComponentRepository repository;
    private final ComponentDTORepository componentDTORepository;
    private final IngredientRepository ingredientRepository;
    private final MsRepository msRepository;

    @Autowired
    public ComponentServiceImpl(ComponentRepository repository, IngredientRepository ingredientRepository,
                                MsRepository msRepository, ComponentDTORepository componentDTORepository) {
        this.repository = repository;
        this.ingredientRepository = ingredientRepository;
        this.msRepository = msRepository;
        this.componentDTORepository = componentDTORepository;
    }

    @Override
    public List<String> findAllIngredients() {
        return ingredientRepository.findAllIngredientNames();
    }

    @Override
    public List<String> findAllMss() {
        return msRepository.findAllMsNames();
    }

    @Override
    public Page<ComponentDTO> findAllDTOs(Pageable pageable, ComponentFilter filter) {
        return componentDTORepository.findAllDTOs(filter, pageable);
    }

    @Override
    public void saveComponent(ComponentRequest componentRequest) {
        LOG.info("In 'saveComponent' method");
        Component component = new Component();
        component.setAmount(new BigDecimal(componentRequest.getAmount()));
        component.setId(componentRequest.getId());
        component.setIngredient(componentRequest.getIngredient());
        component.setMs(componentRequest.getMs());
        LOG.info("Exit from 'saveComponent' method");
        repository.save(component);
    }

    @Override
    public ComponentRequest findOneComponentRequest(String id) {
        LOG.info("In 'findOneComponentRequest' method. Id = {}", id);
        Component component = repository.findById(id)
                .orElseThrow(() -> new CafeException(String.format("Component with id [%s} not found", id)));
        ComponentRequest request = new ComponentRequest();
        request.setAmount(String.valueOf(component.getAmount()));
        request.setId(component.getId());
        request.setIngredient(component.getIngredient());
        request.setMs(component.getMs());
        LOG.info("Exit from 'findOneComponentRequest' method");
        return request;
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

}