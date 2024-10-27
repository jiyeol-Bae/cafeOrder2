package jpabook.jpashop.domian;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    @Enumerated(EnumType.STRING)
    private AvailableStatus isOrderable;
    private Boolean isIceAvailable;
    private Boolean isHotAvailable;
    @Column(nullable = false)
    private Boolean isDeleted = false;  // 기본값을 false로 설정

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // 생성 메서드
    public static Item createItem(String name, AvailableStatus isOrderable, Boolean isIceAvailable, Boolean isHotAvailable, Category category) {
        Item item = new Item();
        item.name = name;
        item.isOrderable = isOrderable;
        item.isIceAvailable = isIceAvailable;
        item.isHotAvailable = isHotAvailable;
        item.category = category;
        return item;
    }

    /**
     * 메뉴삭제
     */
    public void delete() {
        this.isDeleted = true;
    }

    /**
     * 메뉴복구
     */
    public void restore() {
        this.isDeleted = false;
    }

    /**
     * 메뉴수정
     */
    public void update(String name, AvailableStatus isOrderable, Boolean isIceAvailable, Boolean isHotAvailable, Category category) {
        this.name = name;
        this.isOrderable = isOrderable;
        this.isIceAvailable = isIceAvailable;
        this.isHotAvailable = isHotAvailable;
        this.category = category;
    }
}
