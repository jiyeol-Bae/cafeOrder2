package jpabook.jpashop.domian;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int quantity;

    private Boolean isIce;
    private Boolean isHot;

    private String etc;

    //생성 메서드
    public static OrderItem createOrderItem(Item item, int quantity, boolean isIce, boolean isHot, String etc) {
        OrderItem orderItem = new OrderItem();
        orderItem.item = item;
        orderItem.quantity = quantity;
        orderItem.isIce = isIce;
        orderItem.isHot = isHot;
        orderItem.etc = etc;
        return orderItem;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
