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
public class ItemRepository {
    private final EntityManager em;

    /**
     * 메뉴 저장
     * @param item
     */
    public void save(final Item item) {
        if(item.getId() == null) {
            em.persist(item);
        } else {
            Item managedItem = em.find(Item.class, item.getId());

            // 변경 감지를 활용한 업데이트
            managedItem.update(item.getName(), item.getIsOrderable(),
                    item.getIsIceAvailable(), item.getIsHotAvailable(),
                    item.getCategory());
        }
    }

    /**
     * 메뉴 삭제
     * @param id
     */
    public void softDelete(Long id) {
        Item item = findOne(id);
        if (item != null) {
            item.delete();
        }
    }

    /**
     * 메뉴 복구
     * @param id
     */
    public void restore(Long id) {
        Item item = em.find(Item.class, id);  // 이 메소드는 삭제된 항목도 찾을 수 있어야 함
        if (item != null) {
            item.restore();
        }
    }

    public Item findOne(final Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        /*return em.createQuery("select i from Item i where i.isDeleted = false", Item.class)
                .getResultList();*/
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
