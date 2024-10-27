package jpabook.jpashop.controller;

import jpabook.jpashop.domian.Item;
import jpabook.jpashop.domian.Order;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final ItemService itemService;

    @GetMapping("/new")
    public String createForm(Model model) {
        List<Item> items = itemService.findItems().stream()
                .filter(item -> !item.getIsDeleted() && item.getCategory() != null && !item.getCategory().getIsDeleted())
                .toList();
        model.addAttribute("items", items);
        return "orders/createOrderForm";
    }

    @PostMapping("/new")
    public String order(@RequestParam("customerName") String customerName,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count,
                        @RequestParam("temperature") String temperature,
                        @RequestParam("etc") String etc) {
        boolean isIce = "ICE".equals(temperature);
        boolean isHot = "HOT".equals(temperature);
        orderService.order(customerName, itemId, count, isIce, isHot, etc);
        return "redirect:/orders";
    }

    @GetMapping
    public String orderList(Model model) {
        List<Order> orders = orderService.findTodayOrders();
        model.addAttribute("orders", orders);
        return "orders/orderList";
    }

    @PostMapping("/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }

    @PostMapping("/{orderId}/ready")
    public String readyOrder(@PathVariable("orderId") Long orderId) {
        orderService.readyOrder(orderId);
        return "redirect:/orders";
    }
}
