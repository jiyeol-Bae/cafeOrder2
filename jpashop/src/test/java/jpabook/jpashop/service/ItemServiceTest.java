package jpabook.jpashop.service;

import jpabook.jpashop.domian.AvailableStatus;
import jpabook.jpashop.domian.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {
    @Autowired
    ItemService itemService;

    @Test
    //@Rollback(false)
    public void 메뉴저장_아이디리턴() throws Exception {
        // given
        Item item = Item.createItem("테스트 메뉴", AvailableStatus.AVAILABLE, true, true, null);
        
        // when
        Long savedId = itemService.saveItem(item);
        
        // then
        Item foundItem = itemService.findOne(savedId);
        assertNotNull(foundItem, "메뉴가 제대로 저장됐는지 확인");
        assertEquals("테스트 메뉴", foundItem.getName(), "이름이 올바르게 저장됐는지 확인");
        assertEquals(AvailableStatus.AVAILABLE, foundItem.getIsOrderable(), "주문가능 상태가 제대로 저장됐는지 확인");
        assertTrue(foundItem.getIsIceAvailable(), "아이스 옵션 가능 여부 확인");
        assertTrue(foundItem.getIsHotAvailable(), "핫 옵션 가능 여부 확인");
        assertFalse(foundItem.getIsDeleted(), "삭제상태가 아님을 확인");
    }

    @Test
    public void 메뉴정보_수정() {
        // given
        Item item = Item.createItem("초기 메뉴", AvailableStatus.AVAILABLE, true, true, null);
        Long savedId = itemService.saveItem(item);

        // when
        Item updatedItem = itemService.findOne(savedId);
        updatedItem.update("수정된 메뉴", AvailableStatus.UNAVAILABLE, false, true, null);
        itemService.saveItem(updatedItem);

        // then
        Item foundItem = itemService.findOne(savedId);
        assertEquals("수정된 메뉴", foundItem.getName(), "메뉴명이 제대로 수정됐는지 확인");
        assertEquals(AvailableStatus.UNAVAILABLE, foundItem.getIsOrderable(), "주문가능 상태가 제대로 수정됐는지 확인");
        assertFalse(foundItem.getIsIceAvailable(), "아이스 옵션 가능 여부 확인");
        assertTrue(foundItem.getIsHotAvailable(), "핫 옵션 가능 여부 확인");
    }

    @Test
    public void 메뉴삭제() {
        // given
        Item item = Item.createItem("삭제할 메뉴", AvailableStatus.AVAILABLE, true, true, null);
        Long savedId = itemService.saveItem(item);

        // when
        itemService.deleteItem(savedId);

        // then
        Item deletedItem = itemService.findOne(savedId);
        assertTrue(deletedItem.getIsDeleted(), "메뉴가 삭제되었는지 확인");
    }

    @Test
    public void 메뉴복구() {
        // given
        Item item = Item.createItem("복구할 메뉴", AvailableStatus.AVAILABLE, true, true, null);
        Long savedId = itemService.saveItem(item);
        itemService.deleteItem(savedId);

        // when
        itemService.restoreItem(savedId);

        // then
        Item restoredItem = itemService.findOne(savedId);
        assertFalse(restoredItem.getIsDeleted(), "메뉴가 복구되었는지 확인");
    }
}