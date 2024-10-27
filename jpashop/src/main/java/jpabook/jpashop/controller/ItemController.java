package jpabook.jpashop.controller;

import jpabook.jpashop.domian.AvailableStatus;
import jpabook.jpashop.domian.Item;
import jpabook.jpashop.service.CategoryService;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final CategoryService categoryService;

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new ItemForm());
        model.addAttribute("categories", categoryService.findCategories());
        model.addAttribute("availableStatuses", AvailableStatus.values());
        return "items/createItemForm";
    }

    @PostMapping("/new")
    public String create(ItemForm form, BindingResult result) {
        // 이름 null 체크
        if (form.getName() == null || form.getName().trim().isEmpty()) {
            result.rejectValue("name", "NotEmpty", "카테고리명을 입력해주세요.");
        }
        if (result.hasErrors()) {
            return "items/createItemForm";
        }

        Item item = Item.createItem(form.getName(), form.getIsOrderable(),
                form.getIsIceAvailable(), form.getIsHotAvailable(),
                categoryService.findOne(form.getCategoryId()));
        itemService.saveItem(item);

        return "redirect:/items";
    }

    @GetMapping
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/{itemId}/edit")
    public String updateForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemService.findOne(itemId);
        ItemForm form = new ItemForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setIsOrderable(item.getIsOrderable());
        form.setIsIceAvailable(item.getIsIceAvailable());
        form.setIsHotAvailable(item.getIsHotAvailable());
        form.setCategoryId(item.getCategory() != null ? item.getCategory().getId() : null);

        model.addAttribute("form", form);
        model.addAttribute("categories", categoryService.findCategories());
        model.addAttribute("availableStatuses", AvailableStatus.values());
        return "items/updateItemForm";
    }

    @PostMapping("/{itemId}/edit")
    public String update(@PathVariable Long itemId, ItemForm form, BindingResult result) {
        if (form.getName() == null || form.getName().trim().isEmpty()) {
            result.rejectValue("name", "NotEmpty", "메뉴명을 입력해주세요.");
        }

        if (result.hasErrors()) {
            return "items/updateItemForm";
        }

        Item item = itemService.findOne(itemId);
        item.update(form.getName(), form.getIsOrderable(),
                form.getIsIceAvailable(), form.getIsHotAvailable(),
                categoryService.findOne(form.getCategoryId()));
        itemService.saveItem(item);

        return "redirect:/items";
    }

    @PostMapping("/{itemId}/delete")
    public String delete(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return "redirect:/items";
    }

    @PostMapping("/{itemId}/restore")
    public String restore(@PathVariable Long itemId) {
        itemService.restoreItem(itemId);
        return "redirect:/items";
    }
}
