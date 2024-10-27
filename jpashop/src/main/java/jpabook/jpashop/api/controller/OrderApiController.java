package jpabook.jpashop.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jpabook.jpashop.api.common.Result;
import jpabook.jpashop.domian.Item;
import jpabook.jpashop.domian.Order;
import jpabook.jpashop.domian.OrderItem;
import jpabook.jpashop.domian.OrderStatus;
import jpabook.jpashop.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "주문", description = "주문 관련 API")
public class OrderApiController {
    private final OrderService orderService;

    @Operation(
            summary = "전체 주문 목록 조회",
            description = "전체 주문 목록을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "주문 목록 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class)
                            )
                    )
            }
    )


    /**
     * 주문 등록
     * @param request
     * @return
     */
    @Operation(
            summary = "주문 생성",
            description = "새로운 주문을 생성합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "주문 생성 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class)
                            )
                    )
            }
    )
    @PostMapping("/api/v1/orders/new")
    public Result orderItem(@RequestBody @Valid OrderRequest request) {
        boolean isIce = "ICE".equals(request.getTemperature());
        boolean isHot = "HOT".equals(request.getTemperature());

        Long orderId = orderService.order(request.getCustomerName(), request.getItemId(), request.getQuantity(), isIce, isHot, request.getEtc());

        // 생성된 주문 조회
        Order order = orderService.findOne(orderId);

        // 응답 생성
        OrderResponse response = new OrderResponse(
                order.getId(),
                order.getStatus(),
                createOrderItemResponse(order.getOrderItems().get(0))
        );

        return new Result<>(response);
    }

    private OrderItemResponse createOrderItemResponse(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getItem().getName(),
                orderItem.getQuantity(),
                orderItem.getIsIce() ? "ICE" : (orderItem.getIsHot() ? "HOT" : "NORMAL"),
                orderItem.getEtc()
        );
    }

    @Schema(description = "주문 요청")
    @Data
    static class OrderRequest {
        @Schema(description = "고객 이름", example = "홍길동", required = true)
        private String customerName;
        @Schema(description = "상품 ID", example = "1", required = true)
        private Long itemId;
        @Schema(description = "주문 수량", example = "2", required = true)
        private int quantity;
        @Schema(description = "온도 (ICE/HOT/null)", example = "ICE")
        private String temperature;  // "ICE" 또는 "HOT"
        @Schema(description = "기타 요청사항", example = "한샷")
        private String etc;
    }

    @Schema(description = "주문 응답")
    @Data
    @AllArgsConstructor
    static class OrderResponse {
        private Long orderId;
        private OrderStatus orderStatus;
        private OrderItemResponse orderItem;
    }

    @Schema(description = "주문 상품 응답")
    @Data
    @AllArgsConstructor
    static class OrderItemResponse {
        private String itemName;
        private int quantity;
        private String temperature;
        private String etc;
    }
}
