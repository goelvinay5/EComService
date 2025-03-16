package org.woolf.EComService.repositories;

import org.woolf.EComService.models.order.Order;
import org.woolf.EComService.models.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByTransactionId(String transactionId);

    Optional<List<Order>> findByUserId(Long user_id);

    Optional<List<Order>> findByUserIdAndOrderStatus(Long user_id, OrderStatus orderStatus);
}
