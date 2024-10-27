package jpabook.jpashop.controller;

import jpabook.jpashop.domian.Category;
import jpabook.jpashop.service.CategoryService;
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
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("categoryForm", new CategoryForm());
        return "categories/createCategoryForm";
    }

    @PostMapping("/new")
    public String create(CategoryForm form, BindingResult result) {
        // 이름 null 체크
        if (form.getName() == null || form.getName().trim().isEmpty()) {
            result.rejectValue("name", "NotEmpty", "카테고리명을 입력해주세요.");
        }

        if (result.hasErrors()) {
            return "categories/createCategoryForm";
        }

        Category category = Category.createCategory(form.getName(), form.getDescription());
        categoryService.saveCategory(category);

        return "redirect:/categories";
    }

    @GetMapping
    public String list(Model model) {
        List<Category> categories = categoryService.findCategories();
        model.addAttribute("categories", categories);
        return "categories/categoryList";
    }

    @GetMapping("/{categoryId}/edit")
    public String updateForm(@PathVariable("categoryId") Long categoryId, Model model) {
        Category category = categoryService.findOne(categoryId);
        model.addAttribute("form", CategoryForm.createCategoryForm(categoryId, category.getName(), category.getDescription()));
        return "categories/updateCategoryForm";
    }

    @PostMapping("/{categoryId}/edit")
    public String update(@PathVariable Long categoryId, CategoryForm form, BindingResult result) {
        // 이름 null 체크
        if (form.getName() == null || form.getName().trim().isEmpty()) {
            result.rejectValue("name", "NotEmpty", "카테고리명을 입력해주세요.");
        }

        if (result.hasErrors()) {
            return "categories/createCategoryForm";
        }

        Category category = categoryService.findOne(categoryId);
        category.update(form.getName(), form.getDescription());
        categoryService.saveCategory(category);

        return "redirect:/categories";
    }

    @PostMapping("/{categoryId}/delete")
    public String delete(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return "redirect:/categories";
    }

    @PostMapping("/{categoryId}/restore")
    public String restore(@PathVariable Long categoryId) {
        categoryService.restoreCategory(categoryId);
        return "redirect:/categories";
    }
}
