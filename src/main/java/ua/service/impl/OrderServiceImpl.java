package ua.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.dto.MealDTO;
import ua.dto.OrderDTO;
import ua.dto.PlaceDTO;
import ua.exception.CafeException;
import ua.model.entity.Meal;
import ua.model.entity.Order;
import ua.model.entity.User;
import ua.model.filter.OrderFilter;
import ua.model.request.OrderRequest;
import ua.repository.*;
import ua.service.OrderService;
import ua.service.UserService;

import java.util.List;
import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository repository;
    private final OrderDTORepository orderDTORepository;
    private final MealRepository mealRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public OrderServiceImpl(OrderRepository repository, OrderDTORepository orderDTORepository,
                            MealRepository mealRepository, PlaceRepository placeRepository,
                            UserRepository userRepository, UserService userService) {
        this.repository = repository;
        this.orderDTORepository = orderDTORepository;
        this.mealRepository = mealRepository;
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public List<String> findAllMealsNames() {
        return mealRepository.findAllMealsNames();
    }

    @Override
    public List<String> findStatusForSearch() {
        return repository.findStatusForSearch();
    }

    @Override
    public List<PlaceDTO> findAllPlaceDTOs() {
        return placeRepository.findAllPlaceDTOs();
    }

    @Override
    public PlaceDTO findPlaceDTO(String id) {
        return placeRepository.findPlaceDTO(id);
    }

    @Override
    public Order findOrderById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new CafeException(String.format("Order with id [%s} not found", id)));
    }

    @Override
    public List<Order> findOrderByPlaceId(String id) {
        return repository.findOrderByPlaceId(id);
    }

    @Override
    public Page<OrderDTO> findAll(Pageable pageable, OrderFilter filter) {
        LOG.info("In 'findAll' method");
        Page<OrderDTO> ordersPage = orderDTORepository.findAllDTOs(filter, pageable);
        for (OrderDTO orderDTO : ordersPage) {
            orderDTO.setMealDTOS(findMealDTOsForOrder(orderDTO.getId()));
        }
        return ordersPage;
    }

    @Override
    public List<OrderDTO> findOrderDTOsForTable(String tableId) {
        LOG.info("In 'findOrderDTOsForTable' method. Id = {}", tableId);
        List<OrderDTO> ordersPage = repository.findOrderDTOsForTable(tableId);
        for (OrderDTO orderDTO : ordersPage) {
            orderDTO.setMealDTOS(findMealDTOsForOrder(orderDTO.getId()));
        }
        return ordersPage;
    }

    @Override
    public List<MealDTO> findMealDTOsForOrder(String orderId) {
        return mealRepository.findMealDTOsForOrder(orderId);
    }

    @Override
    public void saveOrder(OrderRequest request) {
        LOG.info("In 'saveOrder' method");
        Order order = new Order();
        order.setId(request.getId());
        order.setPlace(request.getPlace());
        order.setMeals(request.getMeals());
        order.setStatus(request.getStatus());
        order.setUserId(request.getUserId());

        User user = userService.findCurrentUser();
        List<Meal> userMeals = user.getMeals();
        for (Meal meal : order.getMeals()) {
            if (!userMeals.contains(meal))
                userMeals.add(meal);
        }
        user.setMeals(userMeals);
        userRepository.save(user);
        repository.save(order);
        LOG.info("Exit from 'saveOrder' method");
    }

    @Override
    public OrderRequest findOrderRequestByUserId(String userId) {
        LOG.info("In 'findOrderRequestByUserId' method. UserId = {}", userId);
        Order order = repository.findRequestByUserId(userId);
        OrderRequest request = new OrderRequest();
        if (Objects.nonNull(order)) {
            request.setId(order.getId());
            request.setUserId(order.getUserId());
            request.setPlace(order.getPlace());
            request.setMeals(order.getMeals());
        }
        LOG.info("Exit from 'findOrderRequestByUserId' method");
        return request;
    }

    @Override
    public OrderDTO findOrderDTOForUser(String userId) {
        LOG.info("In 'findOrderDTOForUser' method. UserId = {}", userId);
        OrderDTO orderDTO = repository.findOrderDTOForUser(userId);
        orderDTO.setMealDTOS(findMealDTOsForOrder(orderDTO.getId()));
        LOG.info("Exit from 'findOrderDTOForUser' method. OrderDTO = {}", orderDTO);
        return orderDTO;
    }

    @Override
    public void updateOrderStatus(String id, String newStatus) {
        LOG.info("In 'updateOrderStatus' method. Id = {}, NewStatus = {}", id, newStatus);
        Order order = repository.findById(id)
                .orElseThrow(() -> new CafeException(String.format("Order with id [%s} not found", id)));
        order.setStatus(newStatus);
        repository.save(order);
        LOG.info("Exit from 'updateOrderStatus' method");
    }
}