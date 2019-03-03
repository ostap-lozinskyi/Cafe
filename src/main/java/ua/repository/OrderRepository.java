package ua.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.entity.Order;
import ua.model.view.OrderView;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT distinct o.status FROM Order o")
    List<String> findStatusForSearch();

    @Query(value = "SELECT new ua.model.view.OrderView(o.id, p.number, o.status) FROM Order o JOIN o.place p WHERE p.id=?1 AND (NOT (o.status='Is paid') OR o.status=null) ORDER BY o.status DESC")
    List<OrderView> findOrderViewsForTable(String tableId);

    @Query("SELECT o FROM Order o WHERE o.id=?1")
    Order findOrderById(String id);

    @Query("SELECT o FROM Order o WHERE o.place.id=?1")
    List<Order> findOrderByPlaceId(String id);

    Order findRequestByUserId(String userId);

}
