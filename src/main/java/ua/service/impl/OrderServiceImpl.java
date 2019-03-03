package ua.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.entity.Meal;
import ua.entity.Order;
import ua.entity.User;
import ua.model.filter.OrderFilter;
import ua.model.request.OrderRequest;
import ua.model.view.MealView;
import ua.model.view.OrderView;
import ua.model.view.PlaceView;
import ua.repository.*;
import ua.service.OrderService;
import ua.service.UserService;

import java.util.List;
import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository repository;
    private final OrderViewRepository orderViewRepository;
    private final MealRepository mealRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public OrderServiceImpl(OrderRepository repository, OrderViewRepository orderViewRepository,
                            MealRepository mealRepository, PlaceRepository placeRepository,
                            UserRepository userRepository, UserService userService) {
        this.repository = repository;
        this.orderViewRepository = orderViewRepository;
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
    public List<PlaceView> findAllPlaceViews() {
        return placeRepository.findAllPlaceViews();
    }

    @Override
    public PlaceView findPlaceViewById(String id) {
        return placeRepository.findPlaceViewById(id);
    }

    @Override
    public Order findOrderById(String id) {
        return repository.findOrderById(id);
    }

    @Override
    public List<Order> findOrderByPlaceId(String id) {
        return repository.findOrderByPlaceId(id);
    }

    @Override
    public Page<OrderView> findAll(Pageable pageable, OrderFilter filter) {
        LOG.info("In 'findAll' method");
        Page<OrderView> ordersPage = orderViewRepository.findAllView(filter, pageable);
        for (OrderView orderView : ordersPage) {
            orderView.setMealViews(findMealViewsForOrder(orderView.getId()));
        }
        return ordersPage;
    }

    @Override
    public List<OrderView> findOrderViewsForTable(String tableId) {
        LOG.info("In 'findOrderViewsForTable' method. Id = {}", tableId);
        List<OrderView> ordersPage = repository.findOrderViewsForTable(tableId);
        for (OrderView orderView : ordersPage) {
            orderView.setMealViews(findMealViewsForOrder(orderView.getId()));
        }
        return ordersPage;
    }

    @Override
    public List<MealView> findMealViewsForOrder(String orderId) {
        return mealRepository.findMealViewsForOrder(orderId);
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
    public void updateOrderStatus(String id, String newStatus) {
        LOG.info("In 'updateOrderStatus' method. Id = {}, NewStatus = {}", id, newStatus);
        Order order = repository.findOrderById(id);
        order.setStatus(newStatus);
        repository.save(order);
        LOG.info("Exit from 'updateOrderStatus' method");
    }
}