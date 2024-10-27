package jpabook.jpashop.domian;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Category {
    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;
    private String description;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "category")
    private List<Item> items = new ArrayList<>();

    // 생성 메서드
    public static Category createCategory(String name, String description) {
        Category category = new Category();
        category.name = name;
        category.description = description;
        return category;
    }

    /**
     * 카테고리삭제
     */
    public void delete() {
        this.isDeleted = true;
    }

    /**
     * 카테고리복구
     */
    public void restore() {
        this.isDeleted = false;
    }

    /**
     * 카테고리 수정
     * @param name
     * @param description
     */
    public void update(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

