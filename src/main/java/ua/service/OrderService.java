package ua.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.dto.MealDTO;
import ua.model.entity.Order;
import ua.model.filter.OrderFilter;
import ua.model.request.OrderRequest;
import ua.dto.OrderDTO;
import ua.dto.PlaceDTO;

import java.util.List;

public interface OrderService {

    List<String> findAllMealsNames();

    List<String> findStatusForSearch();

    List<PlaceDTO> findAllPlaceDTOs();

    PlaceDTO findPlaceDTO(String id);

    Page<OrderDTO> findAll(Pageable pageable, OrderFilter filter);

    List<OrderDTO> findOrderDTOsForTable(String tableId);

    List<MealDTO> findMealDTOsForOrder(String orderId);

    Order findOrderById(String id);

    List<Order> findOrderByPlaceId(String id);

    void saveOrder(OrderRequest request);

    OrderRequest findOrderRequestByUserId(String userId);

    void updateOrderStatus(String id, String newStatus);

}
