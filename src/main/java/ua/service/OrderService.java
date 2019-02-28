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

    PlaceView findPlaceViewById(Integer id);

    Page<OrderView> findAll(Pageable pageable, OrderFilter filter);

    List<OrderView> findOrderViewsForTable(Integer tableId);

    List<MealView> findMealViewsForOrder(Integer orderId);

    Order findOrderById(Integer id);

    List<Order> findOrderByPlaceId(Integer id);

    void saveOrder(OrderRequest request);

    OrderRequest findOrderRequestByUserId(Integer userId);

    void updateOrderStatus(Integer id, String newStatus);

}
