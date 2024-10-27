package jpabook.jpashop.service;

import jpabook.jpashop.domian.Item;
import jpabook.jpashop.domian.Order;
import jpabook.jpashop.domian.OrderItem;
import jpabook.jpashop.exception.IdNotFoundException;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long order(String customerName, Long itemId, int count, boolean isIce, boolean isHot ,String etc) {
        Item item = itemRepository.findOne(itemId);
        if (item == null) {
            throw new IdNotFoundException("해당 상품이 존재하지 않습니다.");
        }

        OrderItem orderItem = OrderItem.createOrderItem(item, count, isIce, isHot, etc);
        Order order = Order.createOrder(customerName, List.of(orderItem));

        orderRepository.save(order);
        return order.getId();
    }

    /**
     * 주문 취소
     * @param orderId
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = findOrderOrThrow(orderId);
        order.cancel();
    }

    /**
     * 준비완료 처리
     * @param orderId
     */
    @Transactional
    public void readyOrder(Long orderId) {
        Order order = findOrderOrThrow(orderId);
        order.ready();
    }

    public List<Order> findTodayOrders() {
        return orderRepository.findTodayOrders();
    }

    public Order findOne(Long orderId) {
        return orderRepository.findOne(orderId);
    }

    private Order findOrderOrThrow(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        if (order == null) {
            throw new IdNotFoundException(String.format("해당 주문이 존재하지 않습니다. (id: %d)", orderId));
        }
        return order;
    }
}
