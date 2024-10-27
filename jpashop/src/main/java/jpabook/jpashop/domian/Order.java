package jpabook.jpashop.domian;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @NotEmpty
    private String customerName;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private LocalDateTime orderTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    // getters and setters

    // 생성메서드
    public static Order createOrder(String customerName, List<OrderItem> orderItems) {
        Order order = new Order();
        order.customerName = customerName;
        order.status = OrderStatus.ORDER;
        order.orderTime = LocalDateTime.now();
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        return order;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    /**
     * 주문 취소
     */
    public void cancel() {
        if (this.status != OrderStatus.ORDER) {
            throw new IllegalStateException("주문된 상태에서만 취소가 가능합니다.");
        }
        this.status = OrderStatus.CANCEL;
    }

    /**
     * 준비완료 처리
     */
    public void ready() {
        if (this.status != OrderStatus.ORDER) {
            throw new IllegalStateException("주문된 상태에서만 준비완료로 변경할 수 있습니다.");
        }
        this.status = OrderStatus.READY;
    }
}
