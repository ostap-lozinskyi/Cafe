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
import ua.model.entity.*;
import ua.model.filter.OrderFilter;
import ua.model.request.OrderRequest;
import ua.repository.*;
import ua.service.OrderService;
import ua.service.UserService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Optional<Order> orderOptional = repository.findOrderByUserIdAndStatusMealsSelected(userId);
        OrderRequest request = new OrderRequest();
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            request.setId(order.getId());
            request.setUserId(order.getUserId());
            request.setPlace(order.getPlace());
            request.setMeals(order.getMeals());
        }
        LOG.info("Exit from 'findOrderRequestByUserId' method");
        return request;
    }

    @Override
    public OrderDTO findOrderDTOForCurrentUser() {
        LOG.info("In 'findOrderDTOForCurrentUser' method");
        User user = userService.findCurrentUser();
        if (Objects.nonNull(user)) {
            OrderDTO orderDTO = repository.findOrderDTOForUser(user.getId());
            if (Objects.nonNull(orderDTO)) {
                orderDTO.setMealDTOS(findMealDTOsForOrder(orderDTO.getId()));
            }
            LOG.info("Exit from 'findOrderDTOForCurrentUser' method. OrderDTO = {}", orderDTO);
            return orderDTO;
        }
        return null;
    }

    @Override
    public BigDecimal countTotalOrderPrice() {
        OrderDTO orderDTO = findOrderDTOForCurrentUser();
        BigDecimal total = new BigDecimal(0);
        if (Objects.nonNull(orderDTO)) {
            List<MealDTO> mealsDTOs = orderDTO.getMealDTOS();
            for (MealDTO mealDTO : mealsDTOs) {
                total = total.add(mealDTO.getPrice());
            }
        }
        return total;
    }

    @Override
    public void updateOrderStatus(String id, OrderStatus newStatus) {
        LOG.info("In 'updateOrderStatus' method. Id = {}, NewStatus = {}", id, newStatus);
        Order order = repository.findById(id)
                .orElseThrow(() -> new CafeException(String.format("Order with id [%s} not found", id)));
        order.setStatus(newStatus);
        repository.save(order);
        LOG.info("Exit from 'updateOrderStatus' method");
    }

    @Override
    public void acceptOrder(String placeId) {
        LOG.info("In 'acceptOrder' method. PlaceId = {}", placeId);
        User user = userService.findCurrentUser();
        Optional<Order> orderOptional = repository.findOrderByUserIdAndStatusMealsSelected(user.getId());
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(OrderStatus.ACCEPTED);

            Place place = placeRepository.findById(placeId)
                    .orElseThrow(() -> new CafeException(String.format("Place with id [%s} not found", placeId)));
            order.setPlace(place);
            repository.save(order);
        }
        LOG.info("Exit from 'acceptOrder' method");
    }

    @Override
    public void addMealToOrder(String mealId) {
        String userId = userService.findCurrentUser().getId();
        OrderRequest orderRequest = findOrderRequestByUserId(userId);

        Meal newOrderedMeal = mealRepository.findById(mealId)
                .orElseThrow(() -> new CafeException(String.format("Meal with id [%s} not found", mealId)));
        List<Meal> alreadyOrderedMeals = orderRequest.getMeals();
        alreadyOrderedMeals.add(newOrderedMeal);
        orderRequest.setMeals(alreadyOrderedMeals);

        orderRequest.setUserId(userId);
        orderRequest.setStatus(OrderStatus.MEALS_SELECTED);

        Place place = placeRepository.findPlaceByName("Remote");
        if (Objects.isNull(place)) {
            throw new CafeException("Place with name 'Remote' not found");
        }
        orderRequest.setPlace(place);
        saveOrder(orderRequest);
    }

    @Override
    public void removeMealFromCurrentOrder(String mealId) {
        LOG.info("In 'removeMealFromCurrentOrder' method. MealId = {}", mealId);
        User user = userService.findCurrentUser();
        Optional<Order> orderOptional = repository.findOrderByUserIdAndStatusMealsSelected(user.getId());
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            List<Meal> filteredMeals = order.getMeals().stream()
                    .filter(meal -> !mealId.equals(meal.getId())).collect(Collectors.toList());
            order.setMeals(filteredMeals);
            repository.save(order);
        }
        LOG.info("Exit from 'removeMealFromCurrentOrder' method");
    }

    @Override
    public void setPlace(String placeId) {
        LOG.info("In 'setPlace' method. PlaceId = {}", placeId);
        User user = userService.findCurrentUser();
        Optional<Order> orderOptional = repository.findOrderByUserIdAndStatusMealsSelected(user.getId());
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            Place place = placeRepository.findById(placeId)
                    .orElseThrow(() -> new CafeException(String.format("Place with id [%s} not found", placeId)));
            order.setPlace(place);
            repository.save(order);
        }
        LOG.info("Exit from 'setPlace' method");
    }
}