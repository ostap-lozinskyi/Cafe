package ua.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.entity.Order;
import ua.model.filter.OrderFilter;
import ua.model.request.OrderRequest;
import ua.model.view.MealView;
import ua.model.view.OrderView;
import ua.model.view.PlaceView;

import java.util.List;

public interface OrderService {

    List<String> findAllMealsNames();

    List<String> findStatusForSearch();

    List<PlaceView> findAllPlaceViews();

    PlaceView findPlaceViewById(String id);

    Page<OrderView> findAll(Pageable pageable, OrderFilter filter);

    List<OrderView> findOrderViewsForTable(String tableId);

    List<MealView> findMealViewsForOrder(String orderId);

    Order findOrderById(String id);

    List<Order> findOrderByPlaceId(String id);

    void saveOrder(OrderRequest request);

    OrderRequest findOrderRequestByUserId(String userId);

    void updateOrderStatus(String id, String newStatus);

}
