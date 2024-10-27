package jpabook.jpashop.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domian.AvailableStatus;
import jpabook.jpashop.domian.Category;
import jpabook.jpashop.domian.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit1() {
            Category category = Category.createCategory("COFFEE", "커피");
            em.persist(category);

            Item item1 = Item.createItem("아메리카노", AvailableStatus.AVAILABLE, true, true, category);
            em.persist(item1);

            Item item2 = Item.createItem("카페라떼", AvailableStatus.AVAILABLE, true, true, category);
            em.persist(item2);
        }

        public void dbInit2() {
            Category category = Category.createCategory("TEA", "차");
            em.persist(category);

            Item item1 = Item.createItem("아이스티", AvailableStatus.AVAILABLE, true, false, category);
            em.persist(item1);

            Item item2 = Item.createItem("얼그레이", AvailableStatus.AVAILABLE, true, true, category);
            em.persist(item2);
        }
    }
}
