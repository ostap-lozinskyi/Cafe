package ua.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.dto.OrderDTO;
import ua.model.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("SELECT distinct o.status FROM Order o")
    List<String> findStatusForSearch();

    @Query(value = "SELECT new ua.dto.OrderDTO(o.id, p.name, o.status) FROM Order o JOIN o.place p WHERE p.id=?1 AND (NOT (o.status='Is paid') OR o.status=null) ORDER BY o.status DESC")
    List<OrderDTO> findOrderDTOsForTable(String tableId);

    @Query(value = "SELECT new ua.dto.OrderDTO(o.id, p.name, o.status) FROM Order o JOIN o.place p WHERE o.userId=?1")
    OrderDTO findOrderDTOForUser(String userId);

    @Query("SELECT o FROM Order o WHERE o.place.id=?1")
    List<Order> findOrderByPlaceId(String id);

    Order findRequestByUserId(String userId);

}
