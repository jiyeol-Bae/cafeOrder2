package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domian.Category;
import jpabook.jpashop.domian.Item;
import jpabook.jpashop.exception.IdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {
    private final EntityManager em;

    public void save(Category category) {
        if (category.getId() == null) {
            em.persist(category);
        } else {
            Category managedCategory = findOne(category.getId());

            // 변경 감지를 활용한 업데이트
            managedCategory.update(category.getName(), category.getDescription());
        }
    }

    /**
     * 카테고리 삭제
     * @param id
     */
    public void softDelete(Long id) {
        Category category = findOne(id);
        if (category != null) {
            category.delete();
        }
    }

    /**
     * 카테고리 복구
     * @param id
     */
    public void restore(Long id) {
        Category category = em.find(Category.class, id);  // 이 메소드는 삭제된 항목도 찾을 수 있어야 함
        if (category != null) {
            category.restore();
        }
    }

    public Category findOne(Long id) {
        return em.find(Category.class, id);
    }

    public List<Category> findAll() {
        //return em.createQuery("select c from Category c where c.isDeleted = false", Category.class)
        //        .getResultList();
        return em.createQuery("select c from Category c ", Category.class)
                .getResultList();
    }
}
