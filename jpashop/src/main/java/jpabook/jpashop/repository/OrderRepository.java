package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domian.Order;
import jpabook.jpashop.domian.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(final Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    /**
     * 오늘 주문내역 조회
     * @return
     */
    /*public List<Order> findTodayOrders() {
        return em.createQuery(
                        "select o from Order o where o.orderTime >= :today and o.status != :cancelStatus", Order.class)
                .setParameter("today", LocalDate.now().atStartOfDay())
                .setParameter("cancelStatus", OrderStatus.CANCEL)
                .getResultList();
    }*/

    public List<Order> findTodayOrders() {
        return em.createQuery(
                        "select distinct o from Order o " +
                                "join fetch o.orderItems oi " +
                                "join fetch oi.item " +
                                "where o.orderTime >= :today and o.status != :cancelStatus", Order.class)
                .setParameter("today", LocalDate.now().atStartOfDay())
                .setParameter("cancelStatus", OrderStatus.CANCEL)
                .getResultList();
    }
}
