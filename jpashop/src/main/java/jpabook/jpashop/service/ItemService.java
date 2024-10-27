package jpabook.jpashop.service;

import jpabook.jpashop.domian.Item;
import jpabook.jpashop.exception.IdNotFoundException;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    /**
     * 메뉴 등록 / 수정
     * @param item
     */
    @Transactional
    public Long saveItem(Item item) {
        itemRepository.save(item);
        return item.getId();
    }

    /**
     * 메뉴 삭제
     * @return
     */
    @Transactional
    public void deleteItem(Long itemId) {
        Item item = findItemOrThrow(itemId);

        // 추가적인 비즈니스 로직 (예: 주문 내역 확인 등)

        itemRepository.softDelete(itemId);
    }

    /**
     * 메뉴 복구
     * @return
     */
    @Transactional
    public void restoreItem(Long itemId) {
        Item item = findItemOrThrow(itemId);

        // 추가적인 비즈니스 로직 (예: 주문 내역 확인 등)

        itemRepository.restore(itemId);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    private Item findItemOrThrow(Long itemId) {
        Item item = itemRepository.findOne(itemId);
        if (item == null) {
            throw new IdNotFoundException(String.format("해당 아이디가 존재하지 않습니다. (id: %d)", itemId));
        }
        return item;
    }
}
