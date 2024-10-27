package jpabook.jpashop.api.controller;

import jpabook.jpashop.api.common.Result;
import jpabook.jpashop.domian.AvailableStatus;
import jpabook.jpashop.domian.Item;
import jpabook.jpashop.service.ItemService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ItemApiController {
    private final ItemService itemService;

    /**
     * 메뉴 목록 조회
     * 삭제되지 않은 메뉴만
     * @return
     */
    @GetMapping("/api/v1/items")
    public Result itemList() {
        List<Item> findItems = itemService.findItems();

        List<ItemDto> collect = findItems.stream()
                .filter(item -> !item.getIsDeleted())
                .map(item -> new ItemDto(item.getName(), getOrderableStatus(item.getIsOrderable()), item.getIsIceAvailable(), item.getIsHotAvailable(), item.getIsDeleted(), item.getCategory().getName()))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    /**
     * 주문가능 여부 확인
     * @param isOrderable
     * @return
     */
    private String getOrderableStatus(AvailableStatus isOrderable) {
        switch (isOrderable) {
            case AVAILABLE:
                return "주문 가능";
            case UNAVAILABLE:
                return "주문 불가";
            default:
                return "알 수 없음";
        }
    }

    @Data
    @AllArgsConstructor
    static class ItemDto {
        private String name;
        private String isOrderable;
        private Boolean isIceAvailable;
        private Boolean isHotAvailable;
        private Boolean isDeleted;
        private String category;
    }
}
